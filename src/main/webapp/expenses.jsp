<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Personal Expense Tracker</title>
    
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
        
        .container-main {
            max-width: 1000px;
            margin: 40px auto;
        }
        
        .card {
            border: none;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
            border-radius: 10px;
            margin-bottom: 30px;
        }
        
        .card-header {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px 10px 0 0 !important;
            font-weight: 600;
            padding: 1.5rem;
        }
        
        .stats-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }
        
        .stats-card h5 {
            color: #667eea;
            font-weight: 600;
            margin-bottom: 10px;
        }
        
        .stats-card .value {
            font-size: 2rem;
            font-weight: 700;
            color: #764ba2;
        }
        
        .table {
            margin-bottom: 0;
        }
        
        .table thead {
            background-color: #f8f9fa;
        }
        
        .table thead th {
            border-bottom: 2px solid #dee2e6;
            font-weight: 600;
            color: #495057;
            padding: 1.25rem 0.75rem;
        }
        
        .table tbody tr {
            transition: background-color 0.2s;
        }
        
        .table tbody tr:hover {
            background-color: #f8f9fa;
        }
        
        .btn-sm {
            padding: 0.4rem 0.8rem;
            font-size: 0.85rem;
            border-radius: 6px;
        }
        
        .btn-edit {
            background-color: #667eea;
            border: none;
            color: white;
        }
        
        .btn-edit:hover {
            background-color: #5568d3;
            color: white;
        }
        
        .btn-delete {
            background-color: #ff6b6b;
            border: none;
            color: white;
        }
        
        .btn-delete:hover {
            background-color: #ee5a52;
            color: white;
        }
        
        .btn-add {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
            padding: 0.6rem 1.5rem;
            font-weight: 500;
        }
        
        .btn-add:hover {
            background: linear-gradient(90deg, #5568d3 0%, #6b4397 100%);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .form-select, .form-control {
            border-radius: 6px;
            border: 1px solid #dee2e6;
            padding: 0.75rem;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #6c757d;
        }
        
        .no-data i {
            font-size: 3rem;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        }
        
        .action-buttons {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
        
        @media (max-width: 768px) {
            .action-buttons {
                flex-direction: column;
            }
            
            .table {
                font-size: 0.9rem;
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
    
    <!-- Main Container -->
    <div class="container-main">
        
        <!-- Success/Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <!-- Statistics Section -->
        <div class="row mb-4">
            <div class="col-md-6 col-lg-4">
                <div class="stats-card">
                    <h5><i class="bi bi-cash-coin"></i> Total Expenses</h5>
                    <div class="value">
                        <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₹" maxFractionDigits="2"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-4">
                <div class="stats-card">
                    <h5><i class="bi bi-list-check"></i> Total Records</h5>
                    <div class="value">${expenses.size()}</div>
                </div>
            </div>
            <div class="col-md-6 col-lg-4">
                <div class="stats-card">
                    <h5><i class="bi bi-tag"></i> Categories</h5>
                    <div class="value">${categories.size()}</div>
                </div>
            </div>
        </div>
        
        <!-- Add Button -->
        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/expense?action=add" class="btn btn-add">
                <i class="bi bi-plus-circle"></i> Add New Expense
            </a>
        </div>
        
        <!-- Filter Section -->
        <div class="filter-section">
            <form method="get" action="${pageContext.request.contextPath}/expense" class="row g-3">
                <input type="hidden" name="action" value="filterByCategory">
                
                <div class="col-md-8">
                    <select name="category" class="form-select" onchange="this.form.submit()">
                        <option value="">-- View All Expenses --</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat}" <c:if test="${cat eq selectedCategory}">selected</c:if>>
                                ${cat}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-outline-primary w-100">
                        <i class="bi bi-funnel"></i> Filter
                    </button>
                </div>
            </form>
        </div>
        
        <!-- Expenses Table -->
        <div class="card">
            <div class="card-header">
                <i class="bi bi-table"></i> Expense Records
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty expenses}">
                        <div class="no-data">
                            <i class="bi bi-inbox"></i>
                            <p>No expenses found. Start tracking your expenses!</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Category</th>
                                        <th>Description</th>
                                        <th>Amount</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${expenses}" var="expense">
                                        <tr>
                                            <td>
                                                <fmt:formatDate value="${expense.expenseDateAsUtilDate}" pattern="MMM dd, yyyy"/>
                                            </td>
                                            <td>
                                                <span class="badge bg-info">${expense.category}</span>
                                            </td>
                                            <td>${expense.description}</td>
                                            <td>
                                                <strong><fmt:formatNumber value="${expense.amount}" type="currency" currencySymbol="₹" maxFractionDigits="2"/></strong>
                                            </td>
                                            <td>
                                                <div class="action-buttons">
                                                    <a href="${pageContext.request.contextPath}/expense?action=edit&id=${expense.id}" 
                                                       class="btn btn-sm btn-edit" title="Edit">
                                                        <i class="bi bi-pencil"></i> Edit
                                                    </a>
                                                    <form method="post" action="${pageContext.request.contextPath}/expense" 
                                                          style="display:inline;" 
                                                          onsubmit="return confirm('Are you sure you want to delete this expense?');">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${expense.id}">
                                                        <button type="submit" class="btn btn-sm btn-delete">
                                                            <i class="bi bi-trash"></i> Delete
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
