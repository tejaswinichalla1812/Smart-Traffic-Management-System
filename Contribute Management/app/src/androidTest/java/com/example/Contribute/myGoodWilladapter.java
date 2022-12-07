/*
package com.example.aahaarapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;




class MyGoodWilladapter extends RecyclerView.Adapter<MyGoodWilladapter.myviewholder>
{
    ArrayList<model> datalist;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    public String userID = fAuth.getCurrentUser().getUid();
    public String uid;
    GoodWillHomeScreen context;


    public MyGoodWilladapter(ArrayList<model> datalist, GoodWillHomeScreen userdataActivity) {
        this.datalist = datalist;
        this.context=userdataActivity;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.tname.setText(datalist.get(position).getName());
        holder.ttype.setText(datalist.get(position).getType());
        holder.tdescription.setText(datalist.get(position).getDescription());
        holder.pincode_tv.setText(datalist.get(position).getPincode());
        holder.phoneNumber.setText(datalist.get(position).getPhone());


        holder.userDetailsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserDetailsItems.class);
                intent.putExtra("ADDRESS",datalist.get(position).getAddress());
                intent.putExtra("NAME",datalist.get(position).getName());
                intent.putExtra("PHONE",datalist.get(position).getPhone());
                intent.putExtra("TYPE",datalist.get(position).getType());
                intent.putExtra("DESCRIPTION",datalist.get(position).getDescription());
                context.startActivity(intent);
            }
        });

    }

    public void deleteItem(int position){
        //getSnapshots().getSnapshot(position).getReference().delete();
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView tname,ttype,tdescription,pincode_tv,phoneNumber;
        CardView userDetailsItem;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.name);
            ttype = itemView.findViewById(R.id.type);
            userDetailsItem = itemView.findViewById(R.id.userDetailsItem);
            pincode_tv = itemView.findViewById(R.id.pincode_tv);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            tdescription = itemView.findViewById(R.id.description);
        }
    }
}
*/
