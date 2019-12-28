package com.example.rentalCar.model;

import org.springframework.stereotype.Component;

@Component
public class ResultBean {
    
    private Order order;
    private String result;
    private String errorMsg;
    
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
