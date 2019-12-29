package com.example.rentalCar.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


public class UserInfo {
    
    private int userId;
    private List<Integer> orderIdList;
    
    public UserInfo() {
        orderIdList=new ArrayList<Integer>();
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getOrderIdList() {
        return orderIdList;
    }
    
    public void setOrderIdList(List<Integer> orderIdList) {
        this.orderIdList = orderIdList;
    }
    
    public void setOrderId(int orderId) {
        orderIdList.add(orderId);
    }

    





	
}
