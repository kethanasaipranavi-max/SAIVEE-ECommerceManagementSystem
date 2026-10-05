# SAIVEE – E-Commerce Management System

## 📌 Project Overview

**SAIVEE – E-Commerce Management System** is a Java Swing based desktop application developed to simulate and manage the major operations of an online shopping platform.

The system provides separate **Customer** and **Administrator** modules and supports the complete e-commerce workflow, including registration, email verification, product browsing, cart management, checkout, payment simulation, order management, inventory management, notifications, returns, refunds, ratings, reviews, and professional email communication.

---

## 🎯 Objectives

The main objectives of the SAIVEE project are:

- Provide a user-friendly desktop e-commerce shopping experience.
- Manage customer accounts and persistent order history.
- Provide email OTP verification during registration.
- Support product browsing, cart, checkout, and order placement.
- Provide simulated UPI, Card, Net Banking, and Cash on Delivery payments.
- Generate transaction references for simulated payments.
- Manage order statuses and order tracking.
- Provide cancellation, return, and refund workflows.
- Maintain accurate inventory and stock quantities.
- Provide an in-app notification center.
- Allow customers to rate and review delivered products.
- Allow administrators to reply to customer reviews.
- Send professional event-based email notifications.

---

## ✨ Key Features

### 👤 Customer Module

- Customer registration
- Email OTP verification
- Customer login
- Customer profile
- Product catalogue
- Product details
- Stock availability
- Shopping cart
- Quantity management
- Checkout
- Multiple payment options
- Transaction reference generation
- Order history
- Order tracking
- Order cancellation
- Return request
- Return reason
- Return pickup and inspection
- Refund status
- Notification center
- Read/unread notifications
- Product ratings
- Product reviews
- Administrator review replies

### 🔐 Administrator Module

- Administrator login
- Admin dashboard
- Product management
- Inventory management
- Stock addition
- Stock removal
- Order management
- Order status management
- Payment and transaction information
- Return management
- Refund management
- Notification monitoring
- Customer review management
- Administrator replies to reviews

---

## 🛒 Customer Shopping Workflow

Registration → Email OTP Verification → Customer Login → Browse Products → View Product Details → Add Products to Cart → Checkout → Select Payment Method → Payment Simulation → Order Confirmation → Order Processing → Shipping → Out for Delivery → Delivered → Rating / Review

---

## 💳 Payment Module

SAIVEE supports the following simulated payment methods:

- UPI
- Credit/Debit Card
- Net Banking
- Cash on Delivery (COD)

For successful simulated payments, the system generates a transaction reference.

**Note:** The payment system is for academic simulation only. No real financial transactions are performed.

---

## 📦 Order Management

The system supports the following order lifecycle:

PLACED → CONFIRMED → PROCESSING → SHIPPED → OUT_FOR_DELIVERY → DELIVERED

Additional order states include:

- CANCELLED
- RETURN_REQUESTED
- RETURNED
- REFUNDED
- FAILED

Administrators can manage supported order status transitions from the Admin module.

---

## 🔄 Returns and Refunds

Customers can request returns for eligible delivered orders.

The return lifecycle includes:

1. Return request
2. Return reason
3. Pickup
4. Inspection
5. Approval or rejection
6. Refund processing
7. Refund completion

The system maintains return and refund information and provides corresponding notifications and email updates.

---

## 📧 Professional Email Notification System

SAIVEE includes an event-based email notification system for important customer activities.

Email notifications can be generated for:

- Registration / OTP verification
- Order placed
- Payment updates
- Order confirmed
- Order shipped
- Out for delivery
- Order delivered
- Order cancellation
- Return requested
- Return approved / rejected
- Refund processing / completion
- Review submitted
- Administrator review reply

The emails are designed to provide relevant customer, order, transaction, and status information.

The email service uses SMTP configuration through environment variables.

**Sensitive email credentials are not stored in the source code.**

---

## 🔔 Notification Center

The application includes an in-app Notification Center with:

- Notification history
- Read/unread status
- Order notifications
- Payment notifications
- Shipping notifications
- Delivery notifications
- Return notifications
- Refund notifications
- Review notifications

Email notifications are handled separately from in-app notifications.

---

## ⭐ Ratings and Reviews

Customers can submit ratings and reviews after successful delivery.

The review system supports:

- Star ratings
- Written reviews
- Product-specific reviews
- Customer and order association
- Review persistence
- Administrator replies
- Email notification for administrator replies
- Display of reviews in the product catalogue

---

## 📦 Inventory and Stock Management

The inventory module manages product stock and helps maintain accurate stock quantities.

It supports:

- Adding stock
- Removing stock
- Checking stock availability
- Inventory reservation
- Stock updates during order processing
- Stock release when applicable

The system is designed to prevent incorrect double reduction of stock during inventory operations.

---

## 🏗️ System Architecture

SAIVEE follows a layered application structure:

UI Layer
    ↓
Service Layer
    ↓
Model Layer
    ↓
Persistence Layer
    ↓
Utility Layer

### Main Layers

**UI Layer**  
Provides the Java Swing screens and user interaction.

**Service Layer**  
Contains business logic for customers, orders, payments, notifications, returns, reviews, and other operations.

**Model Layer**  
Contains the application's data models such as customers, products, orders, payments, reviews, and notifications.

**Persistence Layer**  
Handles local data storage and retrieval.

**Utility Layer**  
Provides supporting functionality such as validation, email services, invoices, and other utilities.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java | Core Programming Language |
| Java Swing | Graphical User Interface |
| JDK | Compilation and Execution |
| VS Code | Development Environment |
| Local File / Serialization | Data Persistence |
| SMTP | Email Notifications |
| Environment Variables | Email Configuration |

---

## 📁 Project Structure

ECommerceManagementSystem/
│
├── src/
│   └── com/
│       └── ecommerce/
│           ├── Main.java
│           ├── model/
│           ├── service/
│           ├── ui/
│           └── utility/
│
├── .gitignore
└── README.md

---

## ▶️ How to Run

### Prerequisites

- Java Development Kit (JDK)
- Java Compiler
- Java Runtime Environment
- Windows PowerShell or Command Prompt

### Compile the Project

Open the terminal in the project directory and run:

Get-ChildItem .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt

javac -encoding UTF-8 -d out @sources.txt

### Run the Application

java -cp out com.ecommerce.Main

---

## 📧 Email Configuration

The email notification system uses environment variables for SMTP configuration.

The following variables are used:

SAIVEE_FROM_EMAIL
SAIVEE_SMTP_HOST
SAIVEE_SMTP_PORT
SAIVEE_SMTP_USERNAME
SAIVEE_SMTP_PASSWORD

For security reasons:

- SMTP passwords should not be stored in source code.
- API keys should not be committed to GitHub.
- Sensitive credentials should be stored using environment variables.
- The .gitignore file is used to prevent unnecessary generated files from being committed.

---

## 🧪 Testing

The system can be tested for the following scenarios:

- Customer registration
- Email OTP verification
- Customer login
- Product browsing
- Product details
- Cart operations
- Quantity management
- Stock validation
- Payment simulation
- Order placement
- Transaction reference generation
- Order status updates
- Order cancellation
- Order delivery
- Product rating
- Product review
- Administrator review reply
- Return request
- Return approval/rejection
- Refund processing
- Notification center
- Email notifications
- Inventory stock updates

---

## 🔒 Security and Privacy

The project follows basic security practices suitable for an academic application:

- SMTP credentials are configured through environment variables.
- Sensitive credentials are not included in the source code.
- Email OTP verification is used during registration.
- Payment processing is simulated.
- Sensitive real payment information is not stored.
- Customer information is used only where required by the application.
- .gitignore helps prevent unnecessary generated files from being committed.

---

## ⚠️ Limitations

- Payment processing is simulated and does not involve real financial transactions.
- Refund processing is simulated within the application.
- Email delivery depends on SMTP configuration and network availability.
- The application currently uses local persistence instead of a production database.
- Production deployment would require stronger authentication, database security, access control, and auditing.

---

## 🚀 Future Enhancements

Possible future improvements include:

- Real payment gateway integration
- MySQL / PostgreSQL database integration
- Secure password hashing
- Stronger authentication and authorization
- Real-time courier tracking
- Automated email retry and monitoring
- Sales and inventory analytics
- Wishlist functionality
- Coupon and discount management
- Product recommendation system
- Fraud detection
- Advanced audit logging
- Web-based version
- Mobile application

---

## 🎓 Project Information

| Details | Information |
|---------|-------------|
| Project Name | SAIVEE – E-Commerce Management System |
| Project Type | Desktop Application |
| Domain | E-Commerce |
| Programming Language | Java |
| GUI Technology | Java Swing |
| Developer | Sai Pranavi |

---

## 📚 Academic Purpose

SAIVEE was developed as an academic project to demonstrate the practical implementation of:

- Object-Oriented Programming
- Java GUI Development
- Software Architecture
- File-Based Persistence
- E-Commerce Workflows
- Inventory Management
- Order Management
- Payment Simulation
- Notification Systems
- Email Communication
- Return and Refund Management
- Ratings and Reviews

---

## 🔗 Repository

GitHub Repository:

https://github.com/kethanasaipranavi-max/SAIVEE-ECommerceManagementSystem

---

## 👩‍💻 Developer

**Kethana Sai Pranavi Atmuri**

Computer Science

---

## 📌 Conclusion

SAIVEE demonstrates a complete academic e-commerce workflow using Java Swing. The system integrates customer management, product catalogue, cart and checkout, simulated payments, order processing, inventory management, returns, refunds, notifications, ratings, reviews, and professional event-based email communication into a single desktop application.

The project provides a practical demonstration of how different components of an e-commerce system can be integrated into a Java-based desktop application.
