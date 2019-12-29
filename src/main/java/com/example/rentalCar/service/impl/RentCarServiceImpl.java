package com.example.rentalCar.service.impl;

import static org.mockito.Mockito.mockingDetails;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	public List<Cars> queryCars(String location,String startDateStr,String endDateStr) {	    
	    Date startDate=strToDate(startDateStr);
	    Date endDate=strToDate(endDateStr);
	    
	   List<Cars> availableCars=cars.stream().filter(car-> checkStockNumber(car.getId(),startDate,endDate)>0).collect(Collectors.toList());
	   availableCars.stream().forEach(e -> e.setInStockNumber(checkStockNumber(e.getId(),startDate,endDate)));

		return availableCars;
	}

	@Override
	public ResultBean reserveCar(int userId, String location,String startDateStr,String endDateStr,int carId) {
	    Date startDate=strToDate(startDateStr);
	    Date endDate=strToDate(endDateStr);
	    UserInfo userInfo=userList.stream().
              filter(userList -> userList.getUserId()==userId).findAny().orElseGet(() -> new UserInfo());     
	    if(!userList.contains(userInfo)) {
	        userInfo.setUserId(userId);
	        userList.add(userInfo);
	    }
      
	    Order order=new Order(userId,startDate,endDate,location,carId);
	    orderList.add(order);
	    userInfo.setOrderId(order.getOrderId());
    
	    if(order.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)){
		    resultBean.setErrorMsg(RentCarConstants.ERRORMSG_ORDER_ALREADY_CONFIRM);
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
	    }else if(checkStockNumber(carId,startDate,endDate)<=0) {
		    resultBean.setErrorMsg(RentCarConstants.ERRORMSG_NO_CAR);
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
	    }else if(!order.CheckOrderValidation().equals("")) {
		    resultBean.setErrorMsg(order.CheckOrderValidation());
		    resultBean.setResult(RentCarConstants.RESULT_FAILURE);
	    }else {
		    resultBean.setResult(RentCarConstants.RESULT_SUCCESS);
            resultBean.setErrorMsg("");
            order.setOrderStatus(RentCarConstants.ORDER_STATE_CONFIRM);
	    }
		
	    resultBean.setOrder(order);
		
	return resultBean;
	}

    @Override
    public List<Order> getUserOrders(int userId) {
        UserInfo user= userList.stream().filter(u -> u.getUserId()==userId).findAny().orElse(null);
        if(user==null) {
            return null;
        }
        return user.getOrderIdList().stream().map(orderId -> orderList.stream().filter(o -> o.getOrderId()==orderId).findAny().orElse(null)).collect(Collectors.toList());
        
        
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

    public static Date strToDate(String strDate) {
        System.out.println(strDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf);
        try {
            System.out.println(sdf.parse(strDate));
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
     }



    @Override
    public ResultBean updateOrder(int orderId, String location, String startDate, String endDate,int carId) {
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
                resultBean=reserveCar(order.getUserId(), location, startDate, endDate, carId);
            }
        }

        return resultBean;
    }

    
    public int checkStockNumber(int carId,Date startDate,Date endDate) { 
        Cars car=cars.stream().filter(c -> c.getId()==carId).findAny().orElse(null);
        if(car==null) {
            return 0;
        }
        int totalNumber=car.getInStockNumber();

        for(Order eachOrder :orderList) {            
            if(eachOrder.getOrderStatus().equals(RentCarConstants.ORDER_STATE_CONFIRM)&&eachOrder.getCarId()==carId) {

                if(startDate.before(eachOrder.getEndDate())||startDate.equals(eachOrder.getEndDate())&&startDate.after(eachOrder.getStartDate())||startDate.equals(eachOrder.getStartDate())){

                    totalNumber--;
                }else if(endDate.before(eachOrder.getEndDate())||endDate.equals(eachOrder.getEndDate())&&endDate.after(eachOrder.getStartDate())||endDate.equals(eachOrder.getStartDate())){
                    totalNumber--;
                }
                if(totalNumber<=0) {
                    return 0;
                }
            }
        }
        return totalNumber;
    }


}
