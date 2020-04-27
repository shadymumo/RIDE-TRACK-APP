package com.example.myridetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,date,issharing,code;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1=(TextView)findViewById(R.id.textView);
        auth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        Intent myIntent = getIntent();
        reference= FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");


        if(myIntent!=null)

        {
            name= myIntent.getStringExtra("name");
            email= myIntent.getStringExtra("email");

            password= myIntent.getStringExtra("password");
            code= myIntent.getStringExtra("code");
            issharing= myIntent.getStringExtra("issharing");
            imageUri= myIntent.getParcelableExtra("imageUri");
        }
        t1.setText(code);
    }
    public void registerUser(View V)
    {

        progressDialog.setMessage("Please wait as we are creating an account for you.");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //insert realtime database


                            user = auth.getCurrentUser();
                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","na");
                            user = auth.getCurrentUser();
                            userId = user.getUid();


                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                //save the image to firebase storage

                                               StorageReference sr = storageReference.child(user.getUid() + ".jpg" );
                                               sr.putFile(imageUri)
                                                       .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()); {
                                                public void onComplete(Task<UploadTask.TaskSnapshot> task)
                                               {
                        if(task.isSuccessful())
                        {
                            String download_image_path = task.getResult().getDownloadUrl().toString();
                            reference.child(user.getUid().child("imageUrl").setValue(download_image_path))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
 if(task.isSuccessful())
 {
     progressDialog.dismiss();
    //Toast.makeText(getApplicationContext(), "email sent for verufication",Toast.LENGTH_SHORT).show();
     sendVerificationEmail();
     Intent myIntent = new Intent(InviteCodeActivity.this, MainActivity.class);
     startActivity(myIntent);

 }
 else
 {
progressDialog.dismiss();
Toast.makeText(getApplicationContext(), "An error occured while creating account",Toast.LENGTH_SHORT).show();


 }
                                        }





                                    });



                            {








                        }





                        }



                                                };

                                            }

                                            }


                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"could not insert values in database/register any user",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    })



                            ;

                        }

                    }
                });

    }

        public void sendVerificationEmail()
        {
           user.sendEmailVerification();
            .addOnCompleteListener(new OnCompletelistener<Void>()


                public void onComplete(Task <Void> task) {
                if(task.isSucessful())
                {
                    Toast.makeText(getApplicationContext(), "email sent for verification",Toast.LENGTH_SHORT).show();
                    finish();
                    auth.signOut();
                }

                 else
                     {
                         Toast.makeText(getApplicationContext(), "could not send email",Toast.LENGTH_SHORT).show();
                     }
            }

                );


        }





}
