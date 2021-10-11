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
import com.example.cleanapplication.model.SubCatModel;
import com.example.cleanapplication.view.ProductActivity;

import java.util.ArrayList;
import java.util.List;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.SubCategoryViewHolder> {

    List<SubCatModel> catItems = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public SubCatAdapter.SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SubCatAdapter.SubCategoryViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sub_category, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        holder.bindView(catItems.get(position));
    }


    @Override
    public int getItemCount() {
        return catItems.size();
    }

    public void callSubCategories(List<SubCatModel> modelList , Context c){
        context = c;
        catItems = modelList;
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewSubCatName;
        TextView textViewCatID;
        TextView textViewDesc;
        TextView textViewImage;
        TextView textViewActive;
        TextView textViewSlug;

        CardView subCatCardView;

        public SubCategoryViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewSubCatName = itemView.findViewById(R.id.textViewSubCatName);
            textViewCatID = itemView.findViewById(R.id.textViewSubCatID);
            textViewDesc = itemView.findViewById(R.id.textViewSubCatDesc);
            textViewImage = itemView.findViewById(R.id.textViewSubCatImage);
            textViewActive = itemView.findViewById(R.id.textViewSubCatStatusText);
            textViewSlug = itemView.findViewById(R.id.textViewSubCatSlug);

            subCatCardView  =itemView.findViewById(R.id.subCategoryCardView);
            subCatCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    con.startActivity(new Intent(con, SubCatActivity.class));
                    Intent intent = new Intent(con, ProductActivity.class);
                    intent.putExtra("SubCategoryID",textViewCatID.getText().toString());
                    con.startActivity(intent);
                }
            });
        }

        public void bindView(SubCatModel subcatModel)
        {
            textViewSubCatName.setText(subcatModel.getSubCatName());
            textViewCatID.setText(subcatModel.getSubCatID());

        }

    }
}
