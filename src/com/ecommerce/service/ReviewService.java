package com.ecommerce.service;

import com.ecommerce.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class ReviewService {
    private static final ReviewService INSTANCE = new ReviewService();
    private final List<Review> reviews = new ArrayList<>();
    private long nextId = 1;
    private final Path file = Paths.get("data","reviews.dat");
    private final EmailService emailService = new EmailService();
    private ReviewService(){load();}
    public static ReviewService getInstance(){return INSTANCE;}
    public synchronized boolean hasReviewed(Customer c, Product p){return c!=null&&p!=null&&reviews.stream().anyMatch(r->r.getCustomerId()==c.getUserId()&&r.getProductId()==p.getProductId());}
    public synchronized Review addReview(Customer c, Product p, int stars, String text){
        if(c==null||p==null||stars<1||stars>5||hasReviewed(c,p)) return null;
        Review r=new Review(nextId++,c,p,stars,text); reviews.add(r); p.addRating(stars); save();
        emailService.sendEmail(c.getEmail(),"SAIVEE review received - "+p.getProductName(),"Hi "+c.getName()+",\n\nThank you for reviewing "+p.getProductName()+". Your "+stars+"/5 rating and review have been recorded in the SAIVEE catalogue.\n\nReview: "+r.getText(),reviewHtml(r),null);
        return r;
    }
    public synchronized List<Review> getAll(){return Collections.unmodifiableList(new ArrayList<>(reviews));}
    public synchronized void rebuildProductRatings(ProductService products){ if(products==null)return; for(Review r:reviews){ Product p=products.findProductById(r.getProductId()); if(p!=null)p.addRating(r.getStars()); } }
    public synchronized List<Review> getProductReviews(int productId){return reviews.stream().filter(r->r.getProductId()==productId).toList();}
    public synchronized boolean reply(long id,String reply){Review r=find(id); if(r==null||reply==null||reply.isBlank())return false; r.setAdminReply(reply.trim()); save(); emailService.sendEmail(r.getCustomerEmail(),"SAIVEE replied to your review - "+r.getProductName(),"Hi "+r.getCustomerName()+",\n\nSAIVEE has replied to your review of "+r.getProductName()+":\n\n"+r.getAdminReply(),replyHtml(r),null); return true;}
    private Review find(long id){return reviews.stream().filter(r->r.getReviewId()==id).findFirst().orElse(null);}
    private String reviewHtml(Review r){return "<html><body style='font-family:Arial;background:#f4f6f8;padding:25px'><div style='max-width:650px;margin:auto;background:white;border-radius:14px;overflow:hidden'><div style='background:#111827;color:white;padding:25px'><h1>SAIVEE</h1><p>Review received</p></div><div style='padding:28px'><p>Hi "+esc(r.getCustomerName())+" 👋</p><h2>Thank you for your review</h2><p><b>"+esc(r.getProductName())+"</b></p><p style='font-size:22px'>"+"★".repeat(r.getStars())+"☆".repeat(5-r.getStars())+"</p><div style='background:#f8fafc;padding:18px;border-radius:10px'>"+esc(r.getText())+"</div><p>Your feedback is now recorded in the SAIVEE shopping catalogue.</p></div></div></body></html>";}
    private String replyHtml(Review r){return "<html><body style='font-family:Arial;background:#f4f6f8;padding:25px'><div style='max-width:650px;margin:auto;background:white;border-radius:14px;overflow:hidden'><div style='background:#111827;color:white;padding:25px'><h1>SAIVEE</h1><p>Response to your review</p></div><div style='padding:28px'><p>Hi "+esc(r.getCustomerName())+" 👋</p><p>Our team responded to your review of <b>"+esc(r.getProductName())+"</b>:</p><div style='background:#f8fafc;padding:18px;border-radius:10px'>"+esc(r.getAdminReply())+"</div><p>Thank you for helping us improve SAIVEE.</p></div></div></body></html>";}
    private static String esc(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");}
    private void save(){try{Files.createDirectories(file.getParent());try(BufferedWriter w=Files.newBufferedWriter(file,StandardCharsets.UTF_8)){for(Review r:reviews)w.write(r.getReviewId()+"|"+r.getCustomerId()+"|"+b(r.getCustomerEmail())+"|"+b(r.getCustomerName())+"|"+r.getProductId()+"|"+b(r.getProductName())+"|"+r.getStars()+"|"+b(r.getText())+"|"+r.getCreatedAt()+"|"+b(r.getAdminReply())+"|"+(r.getRepliedAt()==null?"":r.getRepliedAt())+"\n");}}catch(Exception e){System.out.println("Review history save failed: "+e.getMessage());}}
    private void load(){if(!Files.exists(file))return;try{for(String line:Files.readAllLines(file,StandardCharsets.UTF_8)){String[] x=line.split("\\|",-1);if(x.length<11)continue;try{Review r=new Review(Long.parseLong(x[0]),Integer.parseInt(x[1]),u(x[2]),u(x[3]),Integer.parseInt(x[4]),u(x[5]),Integer.parseInt(x[6]),u(x[7]),LocalDateTime.parse(x[8]),u(x[9]),x[10].isBlank()?null:LocalDateTime.parse(x[10]));reviews.add(r);nextId=Math.max(nextId,r.getReviewId()+1);}catch(Exception ignored){}}}catch(Exception e){System.out.println("Review history load failed: "+e.getMessage());}}
    private String b(String s){return Base64.getEncoder().encodeToString((s==null?"":s).getBytes(StandardCharsets.UTF_8));} private String u(String s){return s==null||s.isBlank()?"":new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8);}
}
