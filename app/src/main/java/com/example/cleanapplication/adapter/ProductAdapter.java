package com.example.cleanapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.AddToCart;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.ProductModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CategoryProductsViewHolder> {

    List<ProductModel> productsItems = new ArrayList<>();
    Context context;
    AddToCart addToCart;
    Dialog dialog;
    int count = 0;
    String cartData ="";
    String newString="";
    String loginUserToken;
    String productIDCart, productNameCart, productPriceCart;
    String productTotalQuantity;
    int productTotalPriceCart;
    int price;
    SharedPrefsData sharedPrefsData;
    ProductModel productModel;
    Gson gson;

    @NonNull
    @Override
    public ProductAdapter.CategoryProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        addToCart = new AddToCart(context);
        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;
        gson = new Gson();
        return new ProductAdapter.CategoryProductsViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.CategoryProductsViewHolder holder, int position) {
        holder.bindView(productsItems.get(position));
    }


    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public void categoriesProducts(List<ProductModel> productModelList, Context c) {
        context = c;
        productsItems = productModelList;
    }

    public class CategoryProductsViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewProductLayoutName;
        TextView textViewProductLayoutID;
        TextView textViewProductLayoutPrice;
        TextView textViewAdd;
        CardView productCardView;

        TextView token, type;

        public CategoryProductsViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewProductLayoutName = itemView.findViewById(R.id.textViewProductName);
            textViewProductLayoutPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductLayoutID = itemView.findViewById(R.id.textViewProductID);
            textViewAdd = itemView.findViewById(R.id.textViewAdd);

            token = itemView.findViewById(R.id.textViewTokenProduct);
            type = itemView.findViewById(R.id.textViewAuthTypeProduct);

            productCardView = itemView.findViewById(R.id.cardViewCategoryProducts);
            productCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(con, textViewProductLayoutID.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            textViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addToCart.showDialog(textViewProductLayoutName.getText().toString(),textViewProductLayoutPrice.getText().toString(),textViewProductLayoutID.getText().toString());
                }
            });

        }

        public void bindView(ProductModel productModel) {
            textViewProductLayoutName.setText(productModel.getProductName());
            textViewProductLayoutPrice.setText(productModel.getProductPrice());
            textViewProductLayoutID.setText(productModel.getProductID());

            token.setText(productModel.getToken());
            type.setText(productModel.getType());
        }
    }
}
