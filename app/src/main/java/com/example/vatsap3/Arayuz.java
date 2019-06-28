package com.example.vatsap3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Arayuz extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListIds;
    ArrayList<String> eMails;
    String id;
    FirebaseAuth auth;
    com.google.firebase.auth.FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arayuz);
        final ConstraintLayout layout=findViewById(R.id.layout);

        arrayList=new ArrayList<>();
        arrayListIds=new ArrayList<>();
        eMails=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {   //Firebase veri çekme, sadece sayfa yüklenirken çekiliyor.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(isNetworkConnected()){
                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                        FirebaseUser user=dataSnapshot2.getValue(FirebaseUser.class);
                        arrayList.add(user.getAdSoyad());
                        arrayListIds.add(user.getId());
                        eMails.add(user.geteMail());
                    }

                    adapter=new Adapter(arrayList, id, arrayListIds, eMails);
                    recyclerView.setAdapter(adapter);
                } else {
                    Snackbar.make(layout, "Internet Bağlantınızı Kontrol Edin", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

class Adapter extends RecyclerView.Adapter<Adapter.Hodor>{  //RecyclerView kısmı

    ArrayList<String> data;
    ArrayList<String> dataIds;
    ArrayList<String> eMails;
    public int position;
    String id;

    class Hodor extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        TextView textView2;
        public Hodor(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.textView2);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Toast.makeText(itemView.getContext(), "ÇALIŞIYOR " + position, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(v.getContext(), Chat.class);
            intent.putExtra("you", data.get(position));
            intent.putExtra("youId", dataIds.get(position));
            intent.putExtra("id", id);
            intent.putExtra("sayac", 0);
            v.getContext().startActivity(intent); // Context alımı
        }
    }

    public Adapter(ArrayList<String> data, String id, ArrayList<String> arrayListIds, ArrayList<String> eMails){
        this.data=data;
        this.id=id;
        this.dataIds=arrayListIds;
        this.eMails=eMails;
    }

    @NonNull
    @Override
    public Adapter.Hodor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);

        return new Hodor(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.Hodor holder, int position) {
        this.position=position;
        holder.textView.setText(data.get(position));
        holder.textView2.setText(eMails.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}