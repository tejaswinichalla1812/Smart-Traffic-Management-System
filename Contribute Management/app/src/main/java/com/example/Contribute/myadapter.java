package com.example.Contribute;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {
    ArrayList<model> datalist;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = fStore.collection("user data");

    public String userID = fAuth.getCurrentUser().getUid();
    public String uid;
    UserdataActivity context;


    public myadapter(ArrayList<model> datalist, UserdataActivity userdataActivity) {
        this.datalist = datalist;
        this.context = userdataActivity;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        Timestamp ts = datalist.get(position).getTimestamp();
        //String dateandtime=String.valueOf(ts);
        String dateandtime = String.valueOf(ts.toDate());
        String date = DateFormat.getTimeInstance().format(ts.toDate());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position,datalist.get(position).getTimeID());
            }
        });
        holder.userDetailsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailsItems.class);
                intent.putExtra("ADDRESS", datalist.get(position).getAddress());
                intent.putExtra("NAME", datalist.get(position).getName());
                intent.putExtra("PHONE", datalist.get(position).getPhone());
                intent.putExtra("TYPE", datalist.get(position).getType());
                intent.putExtra("LATITUDE", datalist.get(position).getLatitude());
                intent.putExtra("LONGITUDE", datalist.get(position).getLongitude());
                intent.putExtra("DESCRIPTION", datalist.get(position).getDescription());
                intent.putExtra("ISOPEN", datalist.get(position).getIsOpen());
                intent.putExtra("foodDurationTime", datalist.get(position).getFoodDurationTime());

                context.startActivity(intent);
            }
        });

    }

    public void deleteItem(int position, String timeID) {
        Task<Void> docRef = collectionReference.document(timeID).delete();
        datalist.remove(position);
        notifyDataSetChanged();

        // getSnapshots().getSnapshot(position).getReference().delete();
        //notifyDataSetChanged();
        // collectionReference.
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView tname, ttype, tdescription, pincode_tv, phoneNumber;
        CardView userDetailsItem, delete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.name);
            ttype = itemView.findViewById(R.id.type);
            userDetailsItem = itemView.findViewById(R.id.userDetailsItem);
            delete = itemView.findViewById(R.id.delete);
            pincode_tv = itemView.findViewById(R.id.pincode_tv);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            tdescription = itemView.findViewById(R.id.description);
        }
    }
}
