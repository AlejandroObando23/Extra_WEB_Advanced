<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty product ? 'Product Registration' : 'Edit Product'} - Warehouse</title>
    <style>
        :root {
            --primary: #4F46E5;
            --primary-hover: #4338CA;
            --bg-color: #F3F4F6;
            --card-bg: #FFFFFF;
            --text-main: #1F2937;
            --text-muted: #6B7280;
        }
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-main);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 2rem 0;
        }
        .container {
            background-color: var(--card-bg);
            padding: 2.5rem;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
            width: 100%;
            max-width: 550px;
        }
        h1 {
            margin-top: 0;
            font-size: 1.8rem;
            color: var(--text-main);
            text-align: center;
            margin-bottom: 1.5rem;
        }
        .form-group {
            margin-bottom: 1.2rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: var(--text-muted);
        }
        input[type="text"], input[type="number"], select, textarea {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #D1D5DB;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 1rem;
            transition: border-color 0.2s;
            font-family: inherit;
        }
        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        button {
            width: 100%;
            padding: 0.85rem;
            background-color: var(--primary);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.2s, transform 0.1s;
            margin-top: 1rem;
        }
        button:hover {
            background-color: var(--primary-hover);
        }
        button:active {
            transform: scale(0.98);
        }
        .links {
            text-align: center;
            margin-top: 1.5rem;
        }
        .links a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s;
        }
        .links a:hover {
            color: var(--primary-hover);
            text-decoration: underline;
        }
        .grid-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        .alert {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            font-size: 0.95rem;
            font-weight: 500;
        }
        .alert-error {
            background-color: #FEF2F2;
            color: #DC2626;
            border: 1px solid #F87171;
        }
        .alert-success {
            background-color: #ECFDF5;
            color: #059669;
            border: 1px solid #34D399;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${empty product ? 'Register Product' : 'Edit Product'}</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        <% if ("saved".equals(request.getParameter("success"))) { %>
            <div class="alert alert-success">
                Product registered successfully!
            </div>
        <% } %>

        <form action="products" method="post">
            <input type="hidden" name="action" value="${empty product ? 'save' : 'update'}">
            <c:if test="${not empty product}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>
            
            <div class="form-group">
                <label for="name">Product Name</label>
                <input type="text" id="name" name="name" required placeholder="e.g. Wireless Mouse" value="${product.name}">
            </div>
            
            <div class="grid-2">
                <div class="form-group">
                    <label for="category">Category</label>
                    <select id="category" name="category" required>
                        <option value="" ${empty product ? 'selected' : ''} disabled>Select category...</option>
                        <option value="Electronics" ${product.category == 'Electronics' ? 'selected' : ''}>Electronics</option>
                        <option value="Furniture" ${product.category == 'Furniture' ? 'selected' : ''}>Furniture</option>
                        <option value="Office Supplies" ${product.category == 'Office Supplies' ? 'selected' : ''}>Office Supplies</option>
                        <option value="Peripherals" ${product.category == 'Peripherals' ? 'selected' : ''}>Peripherals</option>
                        <option value="Other" ${product.category == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="supplier">Supplier</label>
                    <input type="text" id="supplier" name="supplier" required placeholder="e.g. TechCorp" value="${product.supplier}">
                </div>
            </div>

            <div class="grid-2">
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input type="number" id="quantity" name="quantity" required min="0" placeholder="e.g. 10" value="${product.quantity}" oninvalid="this.setCustomValidity('Por favor, ingrese un número no negativo')" oninput="this.setCustomValidity('')">
                </div>
                
                <div class="form-group">
                    <label for="price">Unit Price ($)</label>
                    <input type="number" id="price" name="price" required min="0" step="0.01" placeholder="e.g. 29.99" value="${product.price}" oninvalid="this.setCustomValidity('Por favor, ingrese un número no negativo')" oninput="this.setCustomValidity('')">
                </div>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" placeholder="Short description of the product...">${product.description}</textarea>
            </div>
            
            <button type="submit">${empty product ? 'Save Product' : 'Update Product'}</button>
        </form>
        
        <div class="links">
            <a href="products?action=list">View Warehouse Data &rarr;</a>
        </div>
    </div>
</body>
</html>
