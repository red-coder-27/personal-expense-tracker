package com.expense.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Expense.java - POJO (Plain Old Java Object) for Expense Model
 * 
 * Responsibility: Represent expense data with getters and setters only
 * - Data encapsulation
 * - No business logic
 * - JDBC layer converts DB rows to Expense objects
 * 
 * @author Expense Tracker Team
 * @version 1.0
 */
public class Expense implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Instance variables
    private int id;
    private String category;
    private String description;
    private double amount;
    private LocalDate expenseDate;
    
    // Constructors
    
    /**
     * Default constructor
     */
    public Expense() {
    }
    
    /**
     * Constructor with all parameters
     * 
     * @param id Unique expense identifier
     * @param category Expense category (e.g., Food, Transportation)
     * @param description Brief description of expense
     * @param amount Expense amount
     * @param expenseDate Date of expense
     */
    public Expense(int id, String category, String description, double amount, LocalDate expenseDate) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }
    
    /**
     * Constructor without ID (for new expenses before DB insertion)
     * 
     * @param category Expense category
     * @param description Brief description
     * @param amount Expense amount
     * @param expenseDate Date of expense
     */
    public Expense(String category, String description, double amount, LocalDate expenseDate) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }
    
    // Getters
    
    /**
     * Get expense ID
     * @return unique expense identifier
     */
    public int getId() {
        return id;
    }
    
    /**
     * Get expense category
     * @return category name
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Get expense description
     * @return brief description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get expense amount
     * @return amount in decimal
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Get expense date
     * @return LocalDate object
     */
    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    /**
     * Get expense date as java.util.Date for JSTL formatting
     * @return java.util.Date object
     */
    public java.util.Date getExpenseDateAsUtilDate() {
        return expenseDate != null ? java.sql.Date.valueOf(expenseDate) : null;
    }
    
    // Setters
    
    /**
     * Set expense ID
     * @param id unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Set expense category
     * @param category category name
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * Set expense description
     * @param description brief description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Set expense amount
     * @param amount amount value
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Set expense date
     * @param expenseDate date of expense
     */
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
    
    // Utility method
    
    /**
     * String representation of Expense object
     * @return formatted string of expense details
     */
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                '}';
    }
}
