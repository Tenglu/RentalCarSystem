package com.example.rentalCar.model;

import org.springframework.stereotype.Component;


public class UserInfo {
    
    private int userId;
    private int orderId;
    
	public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + orderId;
        result = prime * result + userId;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserInfo other = (UserInfo) obj;
        if (orderId != other.orderId)
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }





	
}
