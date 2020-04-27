package com.example.myridetrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinRoute extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference,currentReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id, join_user_id;
    DatabaseReference circleReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_route);
        pinview = (Pinview)findViewbyId(R.id.pinview);
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
          current_user_id = user.getUid();






        currentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void submitButtonClick(View V)

    {
        //check if the input code is present or not in database.
        //if code is present , find that user, AND CREATE a code(CircleMemebers).
        Query query = reference.orderByChild("code").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    CreateUser createUser= null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren()) {
                    CreateUser  = childDss.getValue(CreateUser.class);

                    join_user_id = createUser.userid;

                     circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                              .child(join_user_id).child("RouteMemebers"); 
                      
                    CircleJoin circleJoin = new CircleJoin(current_user_id);
                    CircleJoin circleJoin = new CircleJoin(join_user_id);


                    circleReference.child(user.getUid()).setValue(circleJoin)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                      Toast.makeText(getApplicationContext(),"user joined route succesfully",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }

            else
                {
                    Toast.makeText(getApplicationContext(),"Circle code is not valid",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
