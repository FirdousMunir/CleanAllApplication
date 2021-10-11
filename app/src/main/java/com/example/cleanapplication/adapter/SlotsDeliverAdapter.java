package com.example.cleanapplication.adapter;

import android.content.Context;
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

public class SlotsDeliverAdapter extends RecyclerView.Adapter<SlotsDeliverAdapter.DeliverViewHolder> {

    List<MainDaySlot> slotsItems = new ArrayList<>();
    Context context;
    List<SlotsModel> slotsModelList = new ArrayList<>();
    String deliverID = "";
    SharedPrefsData sharedPrefsData;

    @NonNull
    @Override
    public SlotsDeliverAdapter.DeliverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sharedPrefsData = new SharedPrefsData(context);
        return new SlotsDeliverAdapter.DeliverViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_deliver_date_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlotsDeliverAdapter.DeliverViewHolder holder, int position) {
        holder.bindViews(slotsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return slotsItems.size();
    }

    public void deliverSlots(List<MainDaySlot> slotsModels, Context c) {
        context = c;
        slotsItems = slotsModels;
    }

    public class DeliverViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewDay;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewIDTime;
        TextView textViewIDDate;
        LinearLayout deliverTime, deliverDate;


        public DeliverViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewDate = itemView.findViewById(R.id.textViewDeliverDate);
            textViewDay = itemView.findViewById(R.id.textViewDeliverDay);
            textViewTime = itemView.findViewById(R.id.textViewDeliverTime);
            textViewIDDate = itemView.findViewById(R.id.textViewDeliverIdDate);
            textViewIDTime = itemView.findViewById(R.id.textViewDeliverIdTime);
            deliverDate = itemView.findViewById(R.id.linearLayoutDeliverDate);
            deliverTime = itemView.findViewById(R.id.linearLayoutDeliverTime);

            deliverTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliverID = textViewIDTime.getText().toString();
                    Toast.makeText(con, deliverID, Toast.LENGTH_SHORT).show();
                    sharedPrefsData.setDeliverID(deliverID,textViewDate.getText().toString(),textViewTime.getText().toString());
                    sharedPrefsData.setPickDeliver(true);
                }
            });

            deliverDate.setOnClickListener(new View.OnClickListener() {
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

            textViewDay.setText(slotsModel.getDayname());
            textViewDate.setText(slotsModel.getDate());
            slotsModelList = slotsModel.getSlotsList();
//            textViewTime.setText(slotsModel.getSlotsList().size()+"");

            for (int i = 0; i < slotsModelList.size(); i++) {
                if (textViewDay.getText().toString() == slotsModelList.get(i).getDayOfWeek()) {
                    textViewTime.setText(slotsModelList.get(i).getAvailableTime());
                    textViewIDTime.setText(slotsModelList.get(i).getPickupID());
                } else {
//                    Toast.makeText(con, "day not matched", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
}
