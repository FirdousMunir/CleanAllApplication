package com.example.cleanapplication.model;

public class OrderModel {

   public ProductModel productModel;
   public String name;

    public OrderModel() { }

    public OrderModel(ProductModel productModel) {
        this.productModel = productModel;
        name = productModel.productName;
    }
}
