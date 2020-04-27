package com.example.myridetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRouteActivity extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createuser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference,usersReference;
String circlememberid;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_route);

        recyclerView= (RecyclerView)findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namelist= new ArrayList<>();
         layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);
         recyclerView.setHasFixedSize(true);

         usersReference= FirebaseDatabase.getInstance().getReference().child("users");
         reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("active route members");


reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        namelist.clear();
        if(DataSnapshot.exists()) {
            for (DataSnapshot dataSnapshot.getChildren())
            {
                circlememberid  = dss.child("circlememberid").getValue(String.class);
                  usersReference.child(circlememberid)
                          .addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                  createuser = dataSnapshot.getValue(CreateUser.class);
                                  namelist.add(createuser);
                                  adapter.notifyDataSetChanged();
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError)
                              {
                                  Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                              }
                          });

                }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError)
    {
        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
    }
});

adapter = new MembersAdapter(namelist,getApplicationContext());
recyclerView.setAdapter(adapter);
adapter.notifyDataSetChanged();

    }
}
