package com.ecommerce.model;
import java.time.LocalDateTime;
public class ReturnRecord {
 private final int orderId; private final String customerEmail; private final String reason; private ReturnStatus status; private LocalDateTime pickupDate; private String inspectionNotes; private String refundReference;
 public ReturnRecord(int id,String email,String reason){this.orderId=id;this.customerEmail=email;this.reason=reason;this.status=ReturnStatus.REQUESTED;}
 public int getOrderId(){return orderId;} public String getCustomerEmail(){return customerEmail;} public String getReason(){return reason;} public ReturnStatus getStatus(){return status;} public LocalDateTime getPickupDate(){return pickupDate;} public String getInspectionNotes(){return inspectionNotes;} public String getRefundReference(){return refundReference;}
 public void setStatus(ReturnStatus s){status=s;} public void setPickupDate(LocalDateTime d){pickupDate=d;} public void setInspectionNotes(String n){inspectionNotes=n;} public void setRefundReference(String r){refundReference=r;}
}
