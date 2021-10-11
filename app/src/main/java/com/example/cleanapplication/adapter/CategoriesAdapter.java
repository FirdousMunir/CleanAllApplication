package com.example.cleanapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.view.SubCatActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    List<CategoryModel> catItems = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CategoryViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindView(catItems.get(position));
    }

    @Override
    public int getItemCount() {
        return catItems.size();
    }

    public void callCategories(List<CategoryModel> modelList, Context c) {
        context = c;
        catItems = modelList;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewCatName;
        TextView textViewCatDescp;
        TextView textViewCatStatus;
        TextView textViewCatImage;
        TextView textViewCatID;
        CardView catCardView;

        TextView token, authType;

        public CategoryViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewCatImage = itemView.findViewById(R.id.textViewCatImage);
            textViewCatName = itemView.findViewById(R.id.textViewCatName);
            textViewCatDescp = itemView.findViewById(R.id.textViewCatDescp);
            textViewCatStatus = itemView.findViewById(R.id.textViewCatStatus);
            textViewCatID = itemView.findViewById(R.id.textViewCatID);
            catCardView = itemView.findViewById(R.id.categoryCardView);

            token = itemView.findViewById(R.id.textViewLoginTokenMainAct);
            authType = itemView.findViewById(R.id.textViewAuthTypeMainAct);

            catCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(con, SubCatActivity.class);
                    intent.putExtra("CategoryID", textViewCatID.getText().toString());
                    con.startActivity(intent);
                }
            });
        }

        public void bindView(CategoryModel catModel) {
//            textViewCatImage.setText(catModel.getCategoryImage());
            textViewCatName.setText(catModel.getCategoryName());
//            textViewCatDescp.setText(catModel.getCategoryDesc());
//            textViewCatStatus.setText(catModel.getCategoryStatusText());
            textViewCatID.setText(catModel.getCategoryID());
            token.setText(catModel.getToken());
            authType.setText(catModel.getAuth());
        }


    }
}
