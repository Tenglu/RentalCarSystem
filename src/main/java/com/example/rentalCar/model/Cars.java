package com.example.rentalCar.model;
import org.springframework.stereotype.Component;


public class Cars {
    
	private int id;
	
	private String carModelName;
	
	private int inStockNumber;
	

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
