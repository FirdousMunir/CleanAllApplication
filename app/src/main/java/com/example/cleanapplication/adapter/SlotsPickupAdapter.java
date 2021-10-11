package com.example.cleanapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.MainDaySlot;
import com.example.cleanapplication.model.SlotsModel;

import java.util.ArrayList;
import java.util.List;

public class SlotsPickupAdapter extends RecyclerView.Adapter<SlotsPickupAdapter.PickupViewHolder> {

    List<MainDaySlot> slotsItems = new ArrayList<>();
    Context context;
    String pickupID = null;
    SharedPrefsData sharedPrefsData;
    String dayName;
    List<SlotsModel> slotsModelList = new ArrayList<>();

    @NonNull
    @Override
    public PickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sharedPrefsData = new SharedPrefsData(context);
        return new PickupViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pickup_date_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PickupViewHolder holder, int position) {
        holder.bindViews(slotsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return slotsItems.size();
    }

    public void pickupSlots(List<MainDaySlot> slotsModels, Context c) {
        context = c;
        slotsItems = slotsModels;
    }

    public class PickupViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewDay;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewIDTime;
        TextView textViewIDDate;
        LinearLayout pickupTime, pickupDate;

        public PickupViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewDate = itemView.findViewById(R.id.textViewPickupDate);
            textViewDay = itemView.findViewById(R.id.textViewPickupDay);
            textViewTime = itemView.findViewById(R.id.textViewPickupTime);
            textViewIDTime = itemView.findViewById(R.id.textViewPickupIdTime);
            pickupDate = itemView.findViewById(R.id.linearLayoutPickupDate);
            pickupTime = itemView.findViewById(R.id.linearLayoutPickupTime);

            pickupTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickupID = textViewIDTime.getText().toString();
                    Toast.makeText(con, pickupID, Toast.LENGTH_SHORT).show();
                    sharedPrefsData.setPickupID(pickupID,textViewDate.getText().toString(),textViewTime.getText().toString());
                    sharedPrefsData.setPickDeliver(false);
                }
            });

            pickupDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(con, "Date", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindViews(MainDaySlot slotsModel) {
            /*textViewDay.setText(slotsModel.getDayOfWeek());
            textViewDate.setText(slotsModel.getDateOfWeek());
            textViewTime.setText(slotsModel.getAvailableTime());
            textViewIDDate.setText(slotsModel.getPickupID());
            textViewIDTime.setText(slotsModel.getPickupID());*/

            dayName = textViewDay.getText().toString();
            textViewDay.setText(slotsModel.getDayname());
            textViewDate.setText(slotsModel.getDate());
            slotsModelList = slotsModel.getSlotsList();
//            textViewTime.setText(slotsModel.getSlotsList().size()+"");

            for (int i= 0; i<slotsModelList.size(); i++) {
                if (textViewDay.getText().toString() == slotsModelList.get(i).getDayOfWeek()) {
                    textViewTime.setText(slotsModelList.get(i).getAvailableTime());
                    textViewIDTime.setText(slotsModelList.get(i).getPickupID());
                }else{
//                    Toast.makeText(con, "day not matched", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
