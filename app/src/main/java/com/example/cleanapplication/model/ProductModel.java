package com.example.cleanapplication.model;

public class ProductModel {
    String productName, productPrice, genderName, productID;
    String slug, productImage, special, active;
    String createdAt, updatedAt, statusText;
    String token, type;
    String totalQuantity, totalPrice;

    public ProductModel() { }

    public ProductModel(String productName, String totalQuantity) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }

    public ProductModel(String productName, String productPrice, String productID, String token, String totalQuantity, String totalPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productID = productID;
        this.token = token;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public ProductModel(String productName, String productPrice, String productID, String token, String type) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productID = productID;
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
