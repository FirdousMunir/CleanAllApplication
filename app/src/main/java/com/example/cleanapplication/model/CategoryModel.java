package com.example.cleanapplication.model;

public class CategoryModel {

    String categoryID, categoryName, categorySlug, categoryDesc, categoryImage, categoryActive;
    String categoryCreatedAt, categoryUpdatedAt,categoryStatusText;
    String imageLink;
    String token,auth;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public CategoryModel(String categoryID, String categoryName, String token, String auth) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.token = token;
        this.auth = auth;
    }

    public CategoryModel(String categoryID, String categoryName, String categoryDesc, String categoryImage, String categoryStatusText) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDesc = categoryDesc;
        this.categoryImage = categoryImage;
        this.categoryStatusText = categoryStatusText;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryActive() {
        return categoryActive;
    }

    public void setCategoryActive(String categoryActive) {
        this.categoryActive = categoryActive;
    }

    public String getCategoryCreatedAt() {
        return categoryCreatedAt;
    }

    public void setCategoryCreatedAt(String categoryCreatedAt) {
        this.categoryCreatedAt = categoryCreatedAt;
    }

    public String getCategoryUpdatedAt() {
        return categoryUpdatedAt;
    }

    public void setCategoryUpdatedAt(String categoryUpdatedAt) {
        this.categoryUpdatedAt = categoryUpdatedAt;
    }

    public String getCategoryStatusText() {
        return categoryStatusText;
    }

    public void setCategoryStatusText(String categoryStatusText) {
        this.categoryStatusText = categoryStatusText;
    }
}
