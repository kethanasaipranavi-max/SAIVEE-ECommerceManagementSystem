package com.ecommerce.model;

import java.time.LocalDateTime;

public class Review {
    private final long reviewId;
    private final int customerId;
    private final String customerEmail;
    private final String customerName;
    private final int productId;
    private final String productName;
    private final int stars;
    private final String text;
    private final LocalDateTime createdAt;
    private String adminReply;
    private LocalDateTime repliedAt;

    public Review(long reviewId, Customer customer, Product product, int stars, String text) {
        this.reviewId = reviewId;
        this.customerId = customer.getUserId();
        this.customerEmail = customer.getEmail();
        this.customerName = customer.getName();
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.stars = stars;
        this.text = text == null ? "" : text.trim();
        this.createdAt = LocalDateTime.now();
    }
    public Review(long id,int customerId,String email,String name,int productId,String productName,int stars,String text,LocalDateTime createdAt,String reply,LocalDateTime repliedAt){
        this.reviewId=id; this.customerId=customerId; this.customerEmail=email; this.customerName=name; this.productId=productId; this.productName=productName; this.stars=stars; this.text=text; this.createdAt=createdAt; this.adminReply=reply; this.repliedAt=repliedAt;
    }
    public long getReviewId(){return reviewId;} public int getCustomerId(){return customerId;} public String getCustomerEmail(){return customerEmail;} public String getCustomerName(){return customerName;} public int getProductId(){return productId;} public String getProductName(){return productName;} public int getStars(){return stars;} public String getText(){return text;} public LocalDateTime getCreatedAt(){return createdAt;} public String getAdminReply(){return adminReply;} public LocalDateTime getRepliedAt(){return repliedAt;}
    public void setAdminReply(String reply){adminReply=reply; repliedAt=LocalDateTime.now();}
    public boolean hasReply(){return adminReply!=null&&!adminReply.isBlank();}
}
