-- ============================================
-- Personal Expense Tracker Database Schema
-- Database: expense_tracker_db
-- ============================================

-- Create Database
CREATE DATABASE IF NOT EXISTS expense_tracker_db;
USE expense_tracker_db;

-- Create Expenses Table
CREATE TABLE IF NOT EXISTS expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    expense_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Index on category for faster filtering
CREATE INDEX idx_category ON expenses(category);
CREATE INDEX idx_expense_date ON expenses(expense_date);

-- Insert Sample Data
INSERT INTO expenses (category, description, amount, expense_date) VALUES
('Food', 'Lunch at restaurant', 12.50, '2026-04-20'),
('Transportation', 'Gas fill-up', 45.00, '2026-04-21'),
('Utilities', 'Monthly electricity bill', 89.99, '2026-04-22');

-- Display total expenses
SELECT 
    COUNT(*) as total_count,
    SUM(amount) as total_amount,
    AVG(amount) as average_amount,
    MIN(expense_date) as earliest_date,
    MAX(expense_date) as latest_date
FROM expenses;

-- Display all expenses
SELECT * FROM expenses ORDER BY expense_date DESC;
