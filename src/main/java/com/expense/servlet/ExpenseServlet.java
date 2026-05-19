package com.expense.servlet;

import com.expense.dao.ExpenseDAO;
import com.expense.model.Expense;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * ExpenseServlet.java - Central Controller for all Expense operations
 * 
 * Design Pattern: Front Controller
 * - Handles ALL HTTP requests (/expense)
 * - Routes requests using "action" parameter
 * - No business logic (delegates to DAO)
 * - No view rendering (uses JSP for display)
 * - Cleans separation of concerns
 * 
 * Request Flow:
 * 1. Request arrives with action parameter
 * 2. Router determines operation
 * 3. DAO performs database operation
 * 4. Data forwarded to JSP via request attributes
 * 5. JSP renders response
 * 
 * @author Expense Tracker Team
 * @version 1.0
 */
@WebServlet("/expense")
public class ExpenseServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ExpenseDAO expenseDAO = new ExpenseDAO();
    
    // View paths (JSP pages)
    private static final String VIEW_LIST = "/expenses.jsp";
    private static final String VIEW_ADD = "/add-expense.jsp";
    private static final String VIEW_EDIT = "/edit-expense.jsp";
    
    /**
     * Handle GET requests
     * - Fetch and display expenses
     * - Operations: list, view single, filter by category
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "list":
                    listExpenses(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "add":
                    request.getRequestDispatcher(VIEW_ADD).forward(request, response);
                    break;
                case "filterByCategory":
                    filterByCategory(request, response);
                    break;
                default:
                    listExpenses(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher(VIEW_LIST).forward(request, response);
        }
    }
    
    /**
     * Handle POST requests
     * - Create new expense
     * - Update existing expense
     * - Delete expense
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "add":
                    addExpense(request, response);
                    break;
                case "update":
                    updateExpense(request, response);
                    break;
                case "delete":
                    deleteExpense(request, response);
                    break;
                default:
                    listExpenses(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher(VIEW_LIST).forward(request, response);
        }
    }
    
    /**
     * Display all expenses (default action)
     * Sets attributes for JSP rendering:
     * - expenses: List of all Expense objects
     * - totalAmount: Sum of all expenses
     * - categories: Distinct category list
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void listExpenses(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Fetch data from DAO
        List<Expense> expenses = expenseDAO.getAllExpenses();
        double totalAmount = expenseDAO.getTotalExpenses();
        List<String> categories = expenseDAO.getAllCategories();
        
        // Set request attributes for JSP
        request.setAttribute("expenses", expenses);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("categories", categories);
        
        // Forward to JSP for rendering
        request.getRequestDispatcher(VIEW_LIST).forward(request, response);
    }
    
    /**
     * Filter expenses by category
     * Sets attributes for JSP:
     * - expenses: Filtered list by selected category
     * - totalAmount: Sum of filtered expenses
     * - categories: All category options
     * - selectedCategory: Currently selected category
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void filterByCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String category = request.getParameter("category");
        
        List<Expense> expenses;
        double totalAmount = 0.0;
        
        if (category != null && !category.isEmpty()) {
            expenses = expenseDAO.getExpensesByCategory(category);
            // Calculate total for filtered list
            for (Expense e : expenses) {
                totalAmount += e.getAmount();
            }
        } else {
            expenses = expenseDAO.getAllExpenses();
            totalAmount = expenseDAO.getTotalExpenses();
        }
        
        // Set request attributes
        request.setAttribute("expenses", expenses);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("categories", expenseDAO.getAllCategories());
        request.setAttribute("selectedCategory", category);
        
        // Forward to JSP
        request.getRequestDispatcher(VIEW_LIST).forward(request, response);
    }
    
    /**
     * Display form for editing existing expense
     * Fetches expense by ID and shows edit form
     * Sets attributes:
     * - expense: Expense object to be edited
     * - categories: All category options for dropdown
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Expense expense = expenseDAO.getExpenseById(id);
            
            if (expense != null) {
                request.setAttribute("expense", expense);
                request.setAttribute("categories", expenseDAO.getAllCategories());
                request.getRequestDispatcher(VIEW_EDIT).forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Expense not found!");
                listExpenses(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid expense ID!");
            listExpenses(request, response);
        }
    }
    
    /**
     * Add new expense
     * Validates input from form and inserts into database
     * Validation:
     * - Required fields not empty
     * - Amount is valid double/numeric
     * - Date is valid LocalDate format
     * 
     * After successful insertion:
     * - Redirect to list view
     * - Show success message
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void addExpense(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Extract form parameters
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            String amountStr = request.getParameter("amount");
            String dateStr = request.getParameter("expenseDate");
            
            // Validate inputs
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Category is required!");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Description is required!");
            }
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Amount is required!");
            }
            if (dateStr == null || dateStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Date is required!");
            }
            
            // Parse and validate numeric amount
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Amount must be a valid number!");
            }
            
            // Parse and validate date
            LocalDate expenseDate;
            try {
                expenseDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD");
            }
            
            // Create Expense object and insert
            Expense expense = new Expense(category, description, amount, expenseDate);
            boolean success = expenseDAO.addExpense(expense);
            
            if (success) {
                request.setAttribute("successMessage", "Expense added successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to add expense!");
            }
            
            // Redirect to list view
            listExpenses(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("categories", expenseDAO.getAllCategories());
            request.getRequestDispatcher(VIEW_ADD).forward(request, response);
        }
    }
    
    /**
     * Update existing expense
     * Validates input and updates database record
     * Similar validation as addExpense
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void updateExpense(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Extract form parameters
            int id = Integer.parseInt(request.getParameter("id"));
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            String amountStr = request.getParameter("amount");
            String dateStr = request.getParameter("expenseDate");
            
            // Validate inputs
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Category is required!");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Description is required!");
            }
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Amount is required!");
            }
            if (dateStr == null || dateStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Date is required!");
            }
            
            // Parse and validate amount
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Amount must be a valid number!");
            }
            
            // Parse and validate date
            LocalDate expenseDate;
            try {
                expenseDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD");
            }
            
            // Update expense
            Expense expense = new Expense(id, category, description, amount, expenseDate);
            boolean success = expenseDAO.updateExpense(expense);
            
            if (success) {
                request.setAttribute("successMessage", "Expense updated successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to update expense!");
            }
            
            listExpenses(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid ID or amount!");
            listExpenses(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showEditForm(request, response);
        }
    }
    
    /**
     * Delete expense by ID
     * Removes record from database
     * Returns to list view after deletion
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    private void deleteExpense(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = expenseDAO.deleteExpense(id);
            
            if (success) {
                request.setAttribute("successMessage", "Expense deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to delete expense!");
            }
            
            listExpenses(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid expense ID!");
            listExpenses(request, response);
        }
    }
}
