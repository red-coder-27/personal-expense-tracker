<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Expense - Personal Expense Tracker</title>
    
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px 0;
        }
        
        .navbar {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%) !important;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .navbar-brand {
            font-weight: 700;
            font-size: 1.5rem;
            letter-spacing: 0.5px;
        }
        
        .form-container {
            max-width: 600px;
            margin: 40px auto;
        }
        
        .card {
            border: none;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
            border-radius: 10px;
        }
        
        .card-header {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px 10px 0 0 !important;
            font-weight: 600;
            padding: 1.5rem;
        }
        
        .form-control, .form-select {
            border-radius: 6px;
            border: 1px solid #dee2e6;
            padding: 0.75rem;
            margin-bottom: 15px;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 8px;
        }
        
        .btn-submit {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
            padding: 0.75rem 2rem;
            font-weight: 500;
            border-radius: 6px;
            transition: all 0.3s;
            width: 100%;
        }
        
        .btn-submit:hover {
            background: linear-gradient(90deg, #5568d3 0%, #6b4397 100%);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            padding: 0.75rem 2rem;
            font-weight: 500;
            border-radius: 6px;
            width: 100%;
        }
        
        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        
        .button-group .btn {
            flex: 1;
        }
        
        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .form-info {
            background-color: #e7f3ff;
            border-left: 4px solid #667eea;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            font-size: 0.9rem;
            color: #004085;
        }
        
        .required {
            color: #dc3545;
        }
        
        .current-value {
            font-size: 0.85rem;
            color: #6c757d;
            font-style: italic;
            margin-top: 5px;
        }
        
        @media (max-width: 768px) {
            .form-container {
                margin: 20px 10px;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-dark navbar-expand-lg sticky-top">
        <div class="container-fluid">
            <span class="navbar-brand">
                <i class="bi bi-wallet2"></i> Personal Expense Tracker
            </span>
        </div>
    </nav>
    
    <!-- Form Container -->
    <div class="form-container">
        
        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <!-- Edit Expense Form Card -->
        <div class="card">
            <div class="card-header">
                <i class="bi bi-pencil-square"></i> Edit Expense
            </div>
            <div class="card-body p-4">
                
                <!-- Form Information -->
                <div class="form-info">
                    <i class="bi bi-info-circle"></i> 
                    Update the expense details below. All fields marked with <span class="required">*</span> are required.
                </div>
                
                <!-- Form -->
                <form method="post" action="${pageContext.request.contextPath}/expense" novalidate>
                    
                    <!-- Hidden fields -->
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${expense.id}">
                    
                    <!-- Category Field -->
                    <div class="mb-3">
                        <label for="category" class="form-label">
                            Category <span class="required">*</span>
                        </label>
                        <input type="text" class="form-control" id="category" name="category" 
                               value="${expense.category}" 
                               placeholder="e.g., Food, Transportation, Utilities" 
                               required>
                    </div>
                    
                    <!-- Description Field -->
                    <div class="mb-3">
                        <label for="description" class="form-label">
                            Description <span class="required">*</span>
                        </label>
                        <textarea class="form-control" id="description" name="description" 
                                  rows="3" 
                                  required>${expense.description}</textarea>
                    </div>
                    
                    <!-- Amount Field -->
                    <div class="mb-3">
                        <label for="amount" class="form-label">
                            Amount (₹) <span class="required">*</span>
                        </label>
                        <input type="number" class="form-control" id="amount" name="amount" 
                               value="<fmt:formatNumber value="${expense.amount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>" 
                               step="0.01" min="0.01" 
                               required>
                    </div>
                    
                    <!-- Date Field -->
                    <div class="mb-3">
                        <label for="expenseDate" class="form-label">
                            Date <span class="required">*</span>
                        </label>
                        <input type="date" class="form-control" id="expenseDate" name="expenseDate" 
                               value="${expense.expenseDate}" 
                               required>
                    </div>
                    
                    <!-- Submit and Cancel Buttons -->
                    <div class="button-group">
                        <button type="submit" class="btn btn-submit">
                            <i class="bi bi-check-circle"></i> Update Expense
                        </button>
                        <a href="${pageContext.request.contextPath}/expense?action=list" 
                           class="btn btn-secondary btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Cancel
                        </a>
                    </div>
                    
                </form>
            </div>
        </div>
        
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Form Validation JavaScript -->
    <script>
        // Client-side form validation
        const form = document.querySelector('form');
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    </script>
</body>
</html>
