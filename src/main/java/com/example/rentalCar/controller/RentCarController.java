package com.example.rentalCar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.rentalCar.model.Cars;
import com.example.rentalCar.model.Order;
import com.example.rentalCar.model.ResultBean;
import com.example.rentalCar.model.UserInfo;
import com.example.rentalCar.service.RentCarService;
import com.google.common.base.Optional;

import io.swagger.annotations.ApiOperation;


@RestController()
public class RentCarController {
	@Autowired
	private RentCarService rentCarService;
    
	@ApiOperation(value = "Input booking location and reserve time, return all the available cars in stock")
    @RequestMapping(value="/location", method = RequestMethod.GET, produces = "application/json")
    public Map<String,Object> rentLocation(@RequestParam int userId, @RequestParam String location, @RequestParam String startDate, @RequestParam String endDate) {
	    
    	return rentCarService.getAvailableCarsInfo(userId,location,startDate,endDate);
    }
    
	@ApiOperation(value = "Select the car and return if the order is confirmed")
    @RequestMapping(value="/reservation", method = RequestMethod.GET, produces = "application/json")
    public ResultBean reserveCar(@RequestParam int orderId,@RequestParam int carId) {
		return rentCarService.getReservationInfo(orderId,carId);
    }

	@ApiOperation(value = "Input order id to cancel order")
	@RequestMapping(value="/cancelOrder", method = RequestMethod.GET, produces = "application/json")
    public ResultBean cancelOrder(@RequestParam int orderId){
        return rentCarService.cancelOrder(orderId);
    }
 
	@ApiOperation(value = "Input user id to get user's related order information")
	@RequestMapping(value="/getOrderInfo", method = RequestMethod.GET, produces = "application/json")
    public UserInfo getOrderInfo(@RequestParam int userId){
	    
        return rentCarService.getOrderInfo(userId);
    }
	
	 @RequestMapping(value="/getAllUsersInfo", method = RequestMethod.GET, produces = "application/json")
	public List<UserInfo> getAllUsersInfo(){
		return rentCarService.getAllUsersInfo();
	}
    
	 
	 
	 @RequestMapping(value="/getStockInfo", method = RequestMethod.GET, produces = "application/json")
     public List<Cars> getStockInfo(){
         return rentCarService.getStockInfo();
     }
  

}
