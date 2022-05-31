package com.example.Contribute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class GoodWillHomeScreen extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<model> datalist;
    FirebaseFirestore db;
    GoodWillAdapter adapter;
    ProgressDialog progressBar;
    ImageView logout;
    EditText search_tv;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    public String userID = fAuth.getCurrentUser().getUid();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodwill_layout);
        recyclerView=(RecyclerView)findViewById(R.id.rec_view);
        search_tv=findViewById(R.id.search_tv);
        logout=findViewById(R.id.logout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adapter=new GoodWillAdapter(datalist,this);
        recyclerView.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();

        SubscribeToFirebaseTopic();

        progressBar=new ProgressDialog(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(GoodWillHomeScreen.this, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        search_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing implemented
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searching(s,db);
                if(s.toString().isEmpty()){
                    getData(db);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing implemented
            }
        });
        getData(db);
    }

    private void SubscribeToFirebaseTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic("FOOD")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("GOODWILLHOMESCREEN","subscribed");
                        } else{
                            Log.d("GOODWILLHOMESCREEN","UnSubscribed");
                        }
                    }
                });

    }

    private void getData(FirebaseFirestore db) {
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show()    ;
        db.collection("user data").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        if(list.size()>0){
                            datalist.clear();
                        }
                        for(DocumentSnapshot d:list)
                        {
                            model obj=d.toObject(model.class);
                            datalist.add(obj);
/*
                            String Userid = (String) d.get("userid");
                            if(Userid.equals(userID)) {
                                datalist.add(obj);
                            }
*/
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.dismiss();
                    }
                });
    }

    private void searching(CharSequence s, FirebaseFirestore db) {
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show()    ;

        db.collection("user data").whereEqualTo("pincode",s.toString()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> list=task.getResult().getDocuments();
                        if(list.size()>0){
                            datalist.clear();
                        }
                        for(DocumentSnapshot d:list)
                        {
                            model obj=d.toObject(model.class);
                            datalist.add(obj);
                            /*String Userid = (String) d.get("userid");
                            if(Userid.equals(userID)) {
                                datalist.add(obj);
                            }*/
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }
}

