package com.ecommerce.service;

import com.ecommerce.model.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

/** Persistent simulation ledger for payment gateway transactions. No real bank movement occurs. */
public class PaymentHistoryService {
 private static final PaymentHistoryService INSTANCE=new PaymentHistoryService();
 private final List<Record> records=new ArrayList<>(); private final Path file=Paths.get("data","payments.dat");
 private PaymentHistoryService(){load();} public static PaymentHistoryService getInstance(){return INSTANCE;}
 public synchronized void recordPending(Customer c,PaymentMethod p){if(c==null||p==null)return; records.add(new Record(p.getPaymentId(),0,c.getEmail(),method(p),p.getAmount(),p.getStatus(),LocalDateTime.now()));save();}
 public synchronized void linkLatestToOrder(String email,double amount,int orderId){for(int i=records.size()-1;i>=0;i--){Record r=records.get(i);if(r.orderId==0&&r.email.equalsIgnoreCase(email)&&Math.abs(r.amount-amount)<0.01){r.orderId=orderId;save();return;}}}
 public synchronized Record findByOrder(int orderId){for(Record r:records)if(r.orderId==orderId)return r;return null;}
 public synchronized void markRefunded(int orderId){Record r=findByOrder(orderId);if(r!=null){r.status=PaymentStatus.REFUNDED;save();}}
 public synchronized List<Record> getRecords(){return List.copyOf(records);}
 private String method(PaymentMethod p){if(p instanceof CashOnDeliveryPayment)return "Cash on Delivery";if(p instanceof UpiPayment)return "UPI";if(p instanceof NetBankingPayment)return "Net Banking";if(p instanceof CardPayment)return "Card";return p.getClass().getSimpleName();}
 private void save(){try{Files.createDirectories(file.getParent());List<String> l=new ArrayList<>();for(Record r:records)l.add(b(r.paymentId)+"|"+r.orderId+"|"+b(r.email)+"|"+b(r.method)+"|"+r.amount+"|"+r.status+"|"+r.date);Files.write(file,l,StandardCharsets.UTF_8);}catch(Exception e){System.out.println("Payment history save failed: "+e.getMessage());}}
 private void load(){if(!Files.exists(file))return;try{for(String s:Files.readAllLines(file,StandardCharsets.UTF_8)){String[]x=s.split("\\|",-1);if(x.length<7)continue;try{records.add(new Record(u(x[0]),Integer.parseInt(x[1]),u(x[2]),u(x[3]),Double.parseDouble(x[4]),PaymentStatus.valueOf(x[5]),LocalDateTime.parse(x[6])));}catch(Exception ignored){}}}catch(Exception e){System.out.println("Payment history load failed: "+e.getMessage());}}
 private static String b(String s){return Base64.getEncoder().encodeToString((s==null?"":s).getBytes(StandardCharsets.UTF_8));}private static String u(String s){return new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8);}
 public static class Record {private final String paymentId,email,method;private int orderId;private final double amount;private PaymentStatus status;private final LocalDateTime date;Record(String p,int o,String e,String m,double a,PaymentStatus st,LocalDateTime d){paymentId=p;orderId=o;email=e;method=m;amount=a;status=st;date=d;}public String getPaymentId(){return paymentId;}public int getOrderId(){return orderId;}public String getEmail(){return email;}public String getMethod(){return method;}public double getAmount(){return amount;}public PaymentStatus getStatus(){return status;}public LocalDateTime getDate(){return date;}}
}
