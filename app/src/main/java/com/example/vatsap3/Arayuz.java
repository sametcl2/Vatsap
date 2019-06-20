package com.example.vatsap3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Arayuz extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arayuz);

        arrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {   //Firebase veri çekme, sadece sayfa yüklenirken çekiliyor.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                    FirebaseUser user=dataSnapshot2.getValue(FirebaseUser.class);
                    arrayList.add(user.getAdSoyad());
                }

                adapter=new Adapter(arrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

class Adapter extends RecyclerView.Adapter<Adapter.Hodor>{  //RecyclerView kısmı

    ArrayList<String> data;
    public int position;

    class Hodor extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        public Hodor(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView=itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Toast.makeText(itemView.getContext(), "ÇALIŞIYOR " + position, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(v.getContext(), Chat.class);
            intent.putExtra("you", data.get(position));
            v.getContext().startActivity(intent); // Context alımı
        }
    }

    public Adapter(ArrayList<String> data){
        this.data=data;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}