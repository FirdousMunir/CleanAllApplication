package com.example.cleanapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.model.AddressModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    List<AddressModel> addressItems = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AddressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_addresses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.bindView(addressItems.get(position));
    }

    @Override
    public int getItemCount() {
        return addressItems.size();
    }

    public void callAddresses(List<AddressModel> modelList, Context c) {
        context = c;
        addressItems = modelList;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        Context con;
        View view;
        TextView textViewAddress;
        TextView textViewAddressID;
        TextView textViewUserID;

        public AddressViewHolder(Context c, @NonNull View itemView) {
            super(itemView);
            this.con = c;
            this.view = itemView;

            textViewAddress = itemView.findViewById(R.id.textViewUserAddress);
            textViewAddressID = itemView.findViewById(R.id.textViewAddressID);
            textViewUserID = itemView.findViewById(R.id.textViewAddressUserID);

        }

        public void bindView(AddressModel addressModel) {
            textViewAddress.setText(addressModel.getAddress());
            textViewAddressID.setText(addressModel.getAddressID());
            textViewUserID.setText(addressModel.getUserID());

        }


    }

}
