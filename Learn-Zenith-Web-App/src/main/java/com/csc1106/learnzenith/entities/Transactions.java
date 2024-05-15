package com.csc1106.learnzenith.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Transactions {
    private int TransactionsID; //primary key 
    private int courseID; //foreign key
    private int userID; //foreign key
    private Date transactionDate;
    private Time transactionTime;
    private String paymentStatus;
    private BigDecimal amount;

    public Transactions(int TransactionsID,int courseID,int userID,Date transactionDate,Time transactionTime,String paymentStatus,BigDecimal amount){
        this.TransactionsID = TransactionsID;
        this.courseID = courseID;
        this.userID = userID;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }
    public int getTransactionsID(){
        return TransactionsID;
    }
    public void setTransactionsID(int TransactionsID){
        this.TransactionsID = TransactionsID;
    }
    public int getCourseID(){
        return courseID;
    }
    public void setCourseID(int courseID){
        this.courseID = courseID;
    }
    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
    public Date getTransactionDate(){
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate){
        this.transactionDate = transactionDate;
    }   
    public Time getTransactionTime(){
        return transactionTime;
    }   
    public void setTransactionTime(Time transactionTime){
        this.transactionTime = transactionTime;
    }
    public String getPaymentStatus(){
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus){
        this.paymentStatus = paymentStatus;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
}
    
