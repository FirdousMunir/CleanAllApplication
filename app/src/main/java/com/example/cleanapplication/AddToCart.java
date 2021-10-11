package com.example.cleanapplication;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleanapplication.model.OrderModel;
import com.example.cleanapplication.model.ProductModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AddToCart {

    Context context;
    Dialog dialog;
    int count = 1;
    String productIDCart, productNameCart, productPriceCart;
    String productTotalQuantity;
    int productTotalPriceCart;
    String loginUserToken;
    int price;
    SharedPrefsData sharedPrefsData;
    ProductModel productModel;
    List<OrderModel> orderModelList;
    Gson gson;
    String oldData;
    String testDataCart="";
    ProductModel testProductModel;
    List<ProductModel> productlisting ;

    public AddToCart(Context con) {
        this.context = con;
        sharedPrefsData = new SharedPrefsData(context);
        gson = new Gson();
        oldData = sharedPrefsData.getCartValuesFromPrefs();
        productlisting= gson.fromJson(oldData, new TypeToken<List<ProductModel>>() {}.getType());
        if (productlisting==null)
            productlisting = new ArrayList<>();
    }

    public void showDialog(String proName, final String proPrice, String proID) {

        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_add_items);

        Button addButton = dialog.findViewById(R.id.addButton);
        Button minusButton = dialog.findViewById(R.id.minusButton);
        TextView textViewProductName = dialog.findViewById(R.id.textViewDialogProductName);
        TextView textViewProductPrice = dialog.findViewById(R.id.textViewDialogProductPrice);
        final TextView textViewProductQuantity = dialog.findViewById(R.id.textViewDialogProductQuantity);
        TextView textViewProductID = dialog.findViewById(R.id.textViewDialogProductID);
        Button buttonSave = dialog.findViewById(R.id.buttonAddToCart);

        textViewProductName.setText(proName);
        textViewProductPrice.setText(proPrice);
        textViewProductID.setText("ID " + proID);

        productIDCart = textViewProductID.getText().toString();
        productNameCart = textViewProductName.getText().toString();
        productPriceCart = textViewProductPrice.getText().toString();
        String[] priceString = productPriceCart.split(" ");
        Log.i("checkStringPrice234", priceString[0] + "::::::" + priceString[1]);
        price = Integer.parseInt(priceString[1]);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productTotalQuantity = count + "";
                productTotalPriceCart = price * count;

                productModel = new ProductModel(productNameCart, productPriceCart, productIDCart,
                        loginUserToken, count + "", productTotalPriceCart + "");
                

                testProductModel = new ProductModel(productNameCart,count + "");

                /*if (cartData.isEmpty() || cartData == "" || cartData == null) {
                    cartData = gson.toJson(productModel);
                    cartData = oldData + cartData;
                    sharedPrefsData.setCartValueInPrefs(cartData);
                } else {
                    newString = sharedPrefsData.getCartValuesFromPrefs();
                    cartData = newString + gson.toJson(productModel);
                    cartData = oldData + cartData;
                    sharedPrefsData.setCartValueInPrefs(cartData);
                }*/

                productlisting.add(testProductModel);
                String r = gson.toJson(productlisting, new TypeToken<List<ProductModel>>() {}.getType());
                sharedPrefsData.setCartValueInPrefs(r);
//                if (testDataCart.isEmpty() || testDataCart == "" || testDataCart ==null){
//
//                    testDataCart = gson.toJson(testProductModel);
//                    testDataCart = oldData + testDataCart;
//                    sharedPrefsData.setCartValueInPrefs(testDataCart);
//                }else{
//                    productlisting.add(testProductModel);
//                    newString = sharedPrefsData.getCartValuesFromPrefs();
//                    testDataCart = newString + gson.toJson(testProductModel);
//                    testDataCart = oldData + testDataCart;
//                    sharedPrefsData.setCartValueInPrefs(testDataCart);
//                }

                Log.i("AddToCartClass123" , testDataCart);

                Log.i("CartDataCheck", productNameCart + ":::" + price + ":::" + productIDCart + ":::" + loginUserToken + ":::" + count + ":::" + productTotalPriceCart);
                dialog.dismiss();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(textViewProductQuantity.getText().toString());
                count = count + 1;
                textViewProductQuantity.setText(count + "");
            }

        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(textViewProductQuantity.getText().toString());
                if (count > 1) {
                    count = count - 1;
                    textViewProductQuantity.setText(count + "");
                }
            }
        });

        dialog.show();
    }

    void addItemToCart(ProductModel productModel) {

        boolean isAdded = false;
//        for ( OrderModel orderModel : orderModelList) {
//
//            if (orderModel.productModel.getProductID() == productModel.getProductID()) {
//                isAdded = true;
//                Toast.makeText(context, "Already added", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "ForIfElse", Toast.LENGTH_SHORT).show();
//            }
//        }
        if (!isAdded) {
            Toast.makeText(context, "Add new Order", Toast.LENGTH_SHORT).show();
            ArrayList<OrderModel> mainList = new ArrayList<>();
//            mainList=  mainList.add(orderModelList.add(new OrderModel(productModel)));
            Log.i("checkOrderModelListSize", mainList.size() + "");
            sharedPrefsData.setCartValueInPrefs(orderModelList.toString());
        }

    }
}
