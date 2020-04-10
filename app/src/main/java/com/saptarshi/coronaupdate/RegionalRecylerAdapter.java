package com.saptarshi.coronaupdate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RegionalRecylerAdapter extends RecyclerView.Adapter<RegionalRecylerAdapter.RegionalViewHolder> {
     List<Regional> regionalList;
    public RegionalRecylerAdapter(List<Regional> regionalList){
        this.regionalList=regionalList;
    }

    @NonNull
    @Override
    public RegionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.regionaldata_list,parent,false);


        return new RegionalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionalViewHolder holder, int position) {
        holder.state_name.setText(regionalList.get(position).getLoc().toUpperCase());
        holder.cnf_total.setText(regionalList.get(position).getConfirmedCasesIndian().toString());
        int active=regionalList.get(position).getConfirmedCasesIndian()-regionalList.get(position).getDischarged()-regionalList.get(position).getDeaths();
        holder.active_total.setText((String.valueOf(active)));
        holder.recvd_total.setText(regionalList.get(position).getDischarged().toString());
        holder.death_total.setText(regionalList.get(position).getDeaths().toString());


    }

    @Override
    public int getItemCount() {
        return regionalList.size();
    }

    public class RegionalViewHolder extends RecyclerView.ViewHolder{
            TextView state_name,cnf_total,active_total,recvd_total,death_total;
        public RegionalViewHolder(@NonNull View itemView) {
            super(itemView);
            state_name=itemView.findViewById(R.id.state_name);
            cnf_total=itemView.findViewById(R.id.cnfmd_total);
            active_total=itemView.findViewById(R.id.actv_total);
            recvd_total=itemView.findViewById(R.id.recvd_total);
            death_total=itemView.findViewById(R.id.dth_total);
        }
    }
}
