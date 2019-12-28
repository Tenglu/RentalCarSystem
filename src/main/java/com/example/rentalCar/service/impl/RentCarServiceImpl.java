package com.example.rentalCar.service.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rentalCar.model.Cars;
import com.example.rentalCar.model.Order;
import com.example.rentalCar.model.ResultBean;
import com.example.rentalCar.model.UserInfo;
import com.example.rentalCar.service.RentCarService;
import com.example.rentalCar.service.constants.RentCarConstants;



@Service
public class RentCarServiceImpl implements RentCarService {

	private List<Cars> cars;
	private List <UserInfo> userList;
	private List <Order> orderList;
	
	@Autowired
	private ResultBean resultBean;
	
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
	    Order order=new Order(userId,startDate,endDate,location);
	    if(!orderList.contains(order)) {
	        order.setOrderId(order.generateOrderId());
	        orderList.add(order);
	        userInfo.setOrderId(order.getOrderId());
	    }
	    map.put("orderId", order.getOrderId());
	    map.put("carList", cars);
		return map;
	}

	@Override
	public ResultBean reserveCar(int orderId,int carId) {
	    Order order=orderList.stream().
				filter(o -> o.getOrderId()==orderId).findAny().orElse(null);
		if(order==null) {		    
		    resultBean.setErrorMsg(RentCarConstants.ERRORMSG_NO_ORDER);
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);	
		}else if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)){
		    resultBean.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CONFIRM);
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
		}else if(!checkStockNumber(carId,orderId)) {
		    resultBean.setErrorMsg(RentCarConstants.ERRORMSG_NO_CAR);
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
		}else if(!order.CheckOrderValidation().equals("")) {
		    resultBean.setErrorMsg(order.CheckOrderValidation());
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
		}else {
		    order.setCarId(carId);
		    resultBean.setResult(RentCarConstants.RESULT_SUCCESS);
            resultBean.setErrorMsg("");
            order.setOrderStatus(RentCarConstants.ORDER_STATE_CONFIRM);
		}
		
		resultBean.setOrder(order);
		
		return resultBean;
	}

	@Override
	public List<UserInfo> getAllUsersInfo() {
		return userList;
	}

    @Override
    public Order getOrderInfo(int userId) {
         return userList.stream().
                filter(user -> user.getUserId()==userId).map(user -> orderList.stream().filter(o ->o.getOrderId()==user.getOrderId()).findAny().orElse(null)).findAny().orElse(null);
    }


    @Override
    public ResultBean cancelOrder(int orderId) {

        Order order=orderList.stream().
                filter(o -> o.getOrderId()==orderId).findAny().orElse(null);
        if(order==null) {
            resultBean.setErrorMsg(RentCarConstants.ERRORMSG_NO_ORDER);
            resultBean.setResult(RentCarConstants.RESULT_FAILURE);
        }else if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CANCEL)||order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_DRAFT)) {
            resultBean.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CANCELLED);
            resultBean.setResult(RentCarConstants.RESULT_FAILURE);
        }else {
            resultBean.setResult(RentCarConstants.RESULT_SUCCESS);
            resultBean.setErrorMsg("");
            order.setOrderStatus(RentCarConstants.ORDER_STATE_CANCEL);
        }
        resultBean.setOrder(order);
        return resultBean;
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



    @Override
    public ResultBean updateOrderInfo(int orderId, String location, String startDate, String endDate,int carId) {
        Order order=orderList.stream().
                filter(o -> o.getOrderId()==orderId).findAny().orElse(null);
        if(order==null) {
            resultBean.setErrorMsg(RentCarConstants.ERRORMSG_NO_ORDER);
            resultBean.setResult(RentCarConstants.RESULT_FAILURE);
        }else if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)) {
            resultBean.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CONFIRM);
            resultBean.setResult(RentCarConstants.RESULT_FAILURE);
        }else { 
            if(location!=null) {
                order.setLocation(location);
            }
            if(startDate!=null) {
                order.setStartDate(strToDate(startDate));
            }
            if(endDate!=null) {
                order.setEndDate(strToDate(endDate));
            }
            if(carId!=0) {
                resultBean=reserveCar(orderId,carId);
            }
        }

        return resultBean;
    }
    
    public Boolean checkStockNumber(int carId, int orderId) { 
        Cars car=cars.stream().filter(c -> c.getId()==carId).findAny().orElse(null);
        if(car==null) {
            return false;
        }
        Order order=orderList.stream().filter(o -> o.getOrderId()==orderId).findAny().orElse(null);
        int totalNumber=car.getInStockNumber();

        for(Order eachOrder :orderList) {            
            if(eachOrder.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)&&eachOrder.getCarId()==carId) {

                if(order.getStartDate().before(eachOrder.getEndDate())||order.getStartDate().equals(eachOrder.getEndDate())&&order.getStartDate().after(eachOrder.getStartDate())||order.getStartDate().equals(eachOrder.getStartDate())){

                    totalNumber--;
                }else if(order.getEndDate().before(eachOrder.getEndDate())||order.getEndDate().equals(eachOrder.getEndDate())&&order.getEndDate().after(eachOrder.getStartDate())||order.getEndDate().equals(eachOrder.getStartDate())){
                    totalNumber--;
                }
                if(totalNumber<=0) {
                    return false;
                }
            }
        }
        return true;
    }


}
