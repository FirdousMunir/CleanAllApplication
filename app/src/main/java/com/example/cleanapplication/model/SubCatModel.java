package com.example.cleanapplication.model;

public class SubCatModel {

    String subCatID, subCatName, parentID, subCatSlug, subCatDesc;
    String subCatImage, createdAt,updatedAt, active, status_text;

    public SubCatModel(String subCatID, String subCatName) {
        this.subCatID = subCatID;
        this.subCatName = subCatName;
    }

    public SubCatModel(String subCatID, String subCatName, String parentID, String subCatSlug, String subCatDesc, String subCatImage, String createdAt, String updatedAt, String active, String status_text) {
        this.subCatID = subCatID;
        this.subCatName = subCatName;
        this.parentID = parentID;
        this.subCatSlug = subCatSlug;
        this.subCatDesc = subCatDesc;
        this.subCatImage = subCatImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.status_text = status_text;
    }

    public String getSubCatID() {
        return subCatID;
    }

    public void setSubCatID(String subCatID) {
        this.subCatID = subCatID;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getSubCatSlug() {
        return subCatSlug;
    }

    public void setSubCatSlug(String subCatSlug) {
        this.subCatSlug = subCatSlug;
    }

    public String getSubCatDesc() {
        return subCatDesc;
    }

    public void setSubCatDesc(String subCatDesc) {
        this.subCatDesc = subCatDesc;
    }

    public String getSubCatImage() {
        return subCatImage;
    }

    public void setSubCatImage(String subCatImage) {
        this.subCatImage = subCatImage;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }
}

