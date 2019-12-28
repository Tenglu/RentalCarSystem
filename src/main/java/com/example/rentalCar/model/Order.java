package com.example.rentalCar.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.rentalCar.service.constants.RentCarConstants;

public class Order {
    
    private static int generatedOrderId=1;
    
    private Date startDate;
    private Date endDate;
    private int userId;
    private int carId;
    private int orderId;
    private String orderStatus;
    private String location;
    
    public Order(int userId, Date startDate,Date endDate, String location) {
        this.userId=userId;
        this.startDate=startDate;
        this.endDate=endDate;
        this.location=location;
        this.orderStatus=RentCarConstants.ORDER_STATE_DRAFT;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getCarId() {
        return carId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        Order other = (Order) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }

    public static int generateOrderId() {
        return generatedOrderId++;
    }

    public String CheckOrderValidation() {
        StringBuffer errorMsg=new StringBuffer();
        if(startDate==null||endDate==null) {
            errorMsg.append(RentCarConstants.ORDER_DATE_INVALID);
        }else if(location==null) {
            errorMsg.append(RentCarConstants.ORDER_LOCATION_INVALID);
        }else if(userId==0) {
            errorMsg.append(RentCarConstants.ORDER_USERID_INVALID);
        }
        return errorMsg.toString();
    }
}
