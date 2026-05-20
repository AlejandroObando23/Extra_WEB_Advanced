package com.almacen.controller;

import com.almacen.dao.ProductDAO;
import com.almacen.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductController extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            try {
                String name = request.getParameter("name");
                String quantityStr = request.getParameter("quantity");
                String priceStr = request.getParameter("price");
                String category = request.getParameter("category");
                String description = request.getParameter("description");
                String supplier = request.getParameter("supplier");
                
                // Server-side validations
                if (name == null || name.trim().isEmpty() || quantityStr == null || priceStr == null || category == null) {
                    throw new IllegalArgumentException("Required fields are missing");
                }
                
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);
                
                if (quantity < 1 || price <= 0) {
                     throw new IllegalArgumentException("Quantity must be >= 1 and price > 0");
                }
                
                Product p = new Product(name.trim(), quantity, price, category, description, supplier);
                productDAO.insertProduct(p);
                
                response.sendRedirect("products?action=list&success=saved");
            } catch (Exception e) {
                request.setAttribute("error", "Error saving product: " + e.getMessage());
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } else if ("delete".equals(action)) {
            String id = request.getParameter("id");
            if (id != null && !id.trim().isEmpty()) {
                boolean deleted = productDAO.deleteProduct(id);
                if (deleted) {
                    response.sendRedirect("products?action=list&success=deleted");
                } else {
                    response.sendRedirect("products?action=list&error=delete_failed");
                }
            } else {
                response.sendRedirect("products?action=list&error=invalid_id");
            }
        } else if ("update".equals(action)) {
            try {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String quantityStr = request.getParameter("quantity");
                String priceStr = request.getParameter("price");
                String category = request.getParameter("category");
                String description = request.getParameter("description");
                String supplier = request.getParameter("supplier");
                
                if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty() || quantityStr == null || priceStr == null || category == null) {
                    throw new IllegalArgumentException("Required fields are missing");
                }
                
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);
                
                if (quantity < 1 || price <= 0) {
                     throw new IllegalArgumentException("Quantity must be >= 1 and price > 0");
                }
                
                Product p = new Product(name.trim(), quantity, price, category, description, supplier);
                boolean updated = productDAO.updateProduct(id, p);
                
                if (updated) {
                    response.sendRedirect("products?action=list&success=updated");
                } else {
                    throw new Exception("Update failed, product not found.");
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error updating product: " + e.getMessage());
                // We forward back to edit, but we'd need the product data to populate the form again.
                // For simplicity, redirect to list with error
                response.sendRedirect("products?action=list&error=update_error");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("list".equals(action)) {
            List<Product> products = productDAO.getAll();
            
            double totalWarehouseValue = 0;
            int totalQuantity = 0;
            
            for (Product p : products) {
                totalWarehouseValue += p.getTotal();
                totalQuantity += p.getQuantity();
            }
            
            double weightedAveragePrice = 0;
            if (totalQuantity > 0) {
                weightedAveragePrice = totalWarehouseValue / totalQuantity;
            }
            
            request.setAttribute("products", products);
            request.setAttribute("totalWarehouseValue", totalWarehouseValue);
            request.setAttribute("totalQuantity", totalQuantity);
            request.setAttribute("weightedAveragePrice", weightedAveragePrice);
            
            request.getRequestDispatcher("/WEB-INF/results.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            String id = request.getParameter("id");
            if (id != null && !id.trim().isEmpty()) {
                Product product = productDAO.getProductById(id);
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                } else {
                    response.sendRedirect("products?action=list&error=not_found");
                }
            } else {
                response.sendRedirect("products?action=list&error=invalid_id");
            }
        } else {
            // Default to insert page
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
