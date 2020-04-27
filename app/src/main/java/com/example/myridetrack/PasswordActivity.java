package com.example.myridetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.Attributes;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText e3_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        e3_password =(EditText)findViewById(R.id.editText3);

        setContentView(R.layout.activity_password);

        Intent myIntent = getIntent();
        if(myIntent!=null) {
            email = myIntent.getStringExtra("email");

        }
    }

    public void gotoNamePicActivity(View V) {
        if(e3_password.getText().toString().length()>6)
        {
            Intent myIntent = new Intent(PasswordActivity.this, Attributes.Name.class);
            myIntent.putExtra("email",email );
            myIntent.putExtra("password",e3_password.getText().toString());
            startActivity(myIntent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"password length should be more than 6 characters",Toast.LENGTH_SHORT).show();

        }



    }
}
