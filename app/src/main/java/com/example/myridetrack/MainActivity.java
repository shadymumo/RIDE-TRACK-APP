package com.example.myridetrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    PermissionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (auth == null)
        {
            setContentView(R.layout.activity_main);
            manager = new PermissionManager() {
            };
            manager.checkAndRequestPermissions(this);

        }
        else
            {
                Intent myIntent= new Intent(MainActivity.this,UserLocationMainActivity.class);
              startActivity(myIntent);
              finish();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);
        ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;
        if(denied_permissions.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "permissions enabled",Toast.LENGTH_SHORT);

        }
    }

    public void goToLogin(View V) {

        Intent myIntent= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(myIntent);



    }
    public void goToRegister(View V)
    {
        Intent myIntent= new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(myIntent);

    }
}
