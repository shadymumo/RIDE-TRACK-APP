package com.example.myridetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth Auth;
    EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = (EditText)findViewById(R.id.editText);
        e2 = (EditText)findViewById(R.id.editText3);
        Auth = FirebaseAuth.getInstance();

    }
    public void login(View V) {
        Auth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),"user is logged in sucessfully",Toast.LENGTH_LONG ).show();
                            FirebaseUser user = auth.getCurrentUser;
                            if(user.isEmailVerified())
                            {

                                Intent Intent= new Intent(LoginActivity.this,MyNavigationActivity.class);
                                startActivity(Intent);
                                finish();
                            }
                        }
                        else
                            {
                                Toast.makeText(getApplicationContext(),"email si not yet verified",Toast.LENGTH_LONG ).show();
                            }
                        else
                        {

                            Toast.makeText(getApplicationContext(),"wrong password or email",Toast.LENGTH_LONG ).show();
                        }
                    }
                });
    }
}
