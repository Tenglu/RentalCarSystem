package com.example.rentalCar.model;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


public class Cars {
    
	private int id;
	
	private String carModelName;
	
	private int inStockNumber;
	
	private List<Order> relatedOrders;

	public Cars() {
	    relatedOrders=new ArrayList<Order>();
	}
	
	public List<Order> getRelatedOrders() {
        return relatedOrders;
    }

    public void addOrder(Order relatedComfirmedOrders) {
        this.relatedOrders.add(relatedComfirmedOrders);
    }
    
    public Cars(int id,String carModelName, int inStockNumber) {
		this.id=id;
		this.carModelName=carModelName;
		this.inStockNumber=inStockNumber;
	}
	
	public String getCarModelName() {
		return carModelName;
	}
	public void setCarModelName(String carModelName) {
		this.carModelName = carModelName;
	}
	public int getInStockNumber() {
		return inStockNumber;
	}
	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
