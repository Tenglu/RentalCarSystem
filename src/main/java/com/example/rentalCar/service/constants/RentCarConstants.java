package com.example.rentalCar.service.constants;

public class RentCarConstants {
    public static final String RESULT_SUCCESS="success";
    public static final String RESULT_FAILURE="failure";
    
    public static final String ORDER_STATE_CONFIRM="confirm";
    public static final String ORDER_STATE_DRAFT="draft";
    public static final String ORDER_STATE_CANCEL="cancel";
    
    public static final String ERRORMSG_NO_CAR="There is no more car in stock.";
    public static final String ERRORMSG_NO_ORDER="There is no such order in system.";
    public static final String ERRORMSG_ORDER_ALREADY_CONFIRM="The order is already in confirm status.";
    public static final String ERRORMSG_ORDER_ALREADY_CANCELLED="The order is already in cancelled/draft status.";

    public static final String ORDER_DATE_INVALID="Date is invalid, please input a valid date.";
    public static final String ORDER_LOCATION_INVALID="Location is invalid, please input a valid location.";
    public static final String ORDER_USERID_INVALID="User id is invalid, please input a valid user id.";

    public static final String ORDER_DUPLICATE="You already have an confirmed order.";
}
