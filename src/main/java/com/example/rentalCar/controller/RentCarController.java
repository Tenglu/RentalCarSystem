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
    @RequestMapping(value="/queryCars", method = RequestMethod.GET, produces = "application/json")
    public List<Cars> queryCars(@RequestParam String location, @RequestParam String startDate, @RequestParam String endDate) {
	    
    	return rentCarService.queryCars(location,startDate,endDate);
    }
    
	@ApiOperation(value = "Select the car and return if the order is confirmed")
    @RequestMapping(value="/reserveCar", method = RequestMethod.GET, produces = "application/json")
    public ResultBean reserveCar(@RequestParam int userId, @RequestParam String location, @RequestParam String startDate, @RequestParam String endDate, @RequestParam int carId) {
		return rentCarService.reserveCar(userId, location, startDate, endDate, carId);
    }

	@ApiOperation(value = "Input order id to cancel order")
	@RequestMapping(value="/cancelOrder", method = RequestMethod.GET, produces = "application/json")
    public ResultBean cancelOrder(@RequestParam int orderId){
        return rentCarService.cancelOrder(orderId);
    }
 
	@ApiOperation(value = "Input user id to get user's related order information")
	@RequestMapping(value="/retrieveOrder", method = RequestMethod.GET, produces = "application/json")
    public List<Order> getOrderInfo(@RequestParam int userId){
	    
        return rentCarService.getUserOrders(userId);
    }
	
	@ApiOperation(value = "Input order id and to get user's related order information")
    @RequestMapping(value="/updateOrder", method = RequestMethod.GET, produces = "application/json")
    public ResultBean updateOrder(@RequestParam int orderId, @RequestParam(required=false) String location, @RequestParam(required=false) String startDate, @RequestParam(required=false) String endDate, @RequestParam(required=false) int carId){
        
        return rentCarService.updateOrder(orderId,location,startDate,endDate,carId);
    }


}
