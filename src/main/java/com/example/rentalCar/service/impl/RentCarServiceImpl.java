package com.example.rentalCar.service.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.rentalCar.model.Cars;
import com.example.rentalCar.model.Order;
import com.example.rentalCar.model.ResultBean;
import com.example.rentalCar.model.UserInfo;
import com.example.rentalCar.service.RentCarService;
import com.example.rentalCar.service.constants.RentCarConstants;



@Service
public class RentCarServiceImpl implements RentCarService {

	List<Cars> cars;
	List <UserInfo> userList;
	List <Order> orderList;
	
	public RentCarServiceImpl () {
		cars=new ArrayList<Cars>();
		cars.add(new Cars(1,"Toyota Camry",2));
		cars.add(new Cars(2,"BMW 650",2));
		
		userList=new ArrayList<UserInfo>();
		orderList=new ArrayList<Order>();
	}
	
	
	
	@Override
	public Map<String,Object> getAvailableCarsInfo(int userId, String location,String startDateStr,String endDateStr) {
	    Map<String,Object> map=new HashMap<String,Object>();
	    
	    Date startDate=strToDate(startDateStr);
	    Date endDate=strToDate(endDateStr);
	    
	    UserInfo userInfo=userList.stream().
				filter(userList -> userList.getUserId()==userId).findAny().orElseGet(() -> new UserInfo());	    
		
	    if(!userList.contains(userInfo)) {
		    userInfo.setUserId(userId);
		    userList.add(userInfo);
		}
	    Order order=new Order(userId,startDate,endDate);
	    if(!orderList.contains(order)) {
	        order.setOrderId(order.generateOrderId());
	        orderList.add(order);
	    }
        System.out.println("2 order id:"+order.getOrderId());
	    map.put("orderId", order.getOrderId());
	    map.put("carList", cars);
		return map;
	}

	@Override
	public ResultBean getReservationInfo(int orderId,int carId) {
	    ResultBean result=new ResultBean();
	    Order order=orderList.stream().
				filter(o -> o.getOrderId()==orderId).findAny().orElse(null);
		if(order==null) {		    
		    result.setErrorMsg(RentCarConstants.ERRORMSG_NO_ORDER);
            result.setResult(RentCarConstants.RESULT_FAILURE);	
		}else if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)){
		    result.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CONFIRM);
            result.setResult(RentCarConstants.RESULT_FAILURE);
		}else if(!checkAvailable(carId)) {
		    result.setErrorMsg(RentCarConstants.ERRORMSG_NO_CAR);
            result.setResult(RentCarConstants.RESULT_FAILURE);
		}else {
            result.setResult(RentCarConstants.RESULT_SUCCESS);
            order.setOrderStatus(RentCarConstants.ORDER_STATE_CONFIRM);
		}
		
		result.setOrder(order);
		
		return result;
	}
	
	public boolean checkAvailable(int carId) {
	    Cars car=cars.stream().filter(c -> c.getId()==carId).findAny().get();
	    if(car==null) {
	        return false;
	    }else if(car.getInStockNumber()<=0) {
	        return false;
	    }
	    return true;
	}


	@Override
	public List<UserInfo> getAllUsersInfo() {
		return userList;
	}

    @Override
    public UserInfo getOrderInfo(int userId) {
        return userList.stream().
                filter(userList -> userList.getUserId()==userId).findAny().orElse(null);
    }


    @Override
    public ResultBean cancelOrder(int orderId) {
        ResultBean result=new ResultBean();
        Order order=orderList.stream().
                filter(o -> o.getOrderId()==orderId).findAny().get();
        if(order==null) {
            result.setErrorMsg(RentCarConstants.ERRORMSG_NO_ORDER);
            result.setResult(RentCarConstants.RESULT_FAILURE);
        }else if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CANCEL)||order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_DRAFT)) {
            result.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CANCELLED);
            result.setResult(RentCarConstants.RESULT_FAILURE);
        }else {
            result.setResult(RentCarConstants.RESULT_SUCCESS);
        }
        result.setOrder(order);
        return result;
    }
        
    @Override
    public List<Cars> getStockInfo() {
        return cars;
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
     }


}
