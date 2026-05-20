package com.almacen.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Product {
    @BsonId
    private ObjectId id;
    
    @BsonProperty("name")
    private String name;
    
    @BsonProperty("quantity")
    private int quantity;
    
    @BsonProperty("price")
    private double price;

    @BsonProperty("category")
    private String category;
    
    @BsonProperty("description")
    private String description;
    
    @BsonProperty("supplier")
    private String supplier;

    public Product() {}

    public Product(String name, int quantity, double price, String category, String description, String supplier) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.description = description;
        this.supplier = supplier;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    
    public double getTotal() {
        return this.quantity * this.price;
    }
}
