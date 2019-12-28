package com.example.rentalCar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.rentalCar.model.Cars;
import com.example.rentalCar.model.Order;
import com.example.rentalCar.model.ResultBean;
import com.example.rentalCar.model.UserInfo;

public interface RentCarService {

	public Map<String,Object> getAvailableCarsInfo(int userId, String location,String startDate,String endDate);

	public ResultBean reserveCar(int orderId,int carId);

	public ResultBean updateOrderInfo(int orderId, String location,String startDate,String endDate, int carId );
	
	public List<UserInfo> getAllUsersInfo();
	
	public Order getOrderInfo(int userId);
	
	public ResultBean cancelOrder(int orderId);
	
	public List<Cars> getStockInfo();
	
}
