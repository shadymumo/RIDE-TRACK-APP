package com.example.myridetrack;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserLocationMainActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{


    FirebaseAuth auth;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latlng;
    GoogleMap mMap;
    DatabaseReference databaseReference;
    FirebaseUser user;

    String current_user_name;
    String current_user_email;
     String current_user_imageUrl;
     View header;
     TextView t1_currentName,t2_currentEmail;
     ImageView i1;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       auth=FirebaseAuth.getInstance();
       user = FirebaseUser.getCurrentUser();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, "Open navigation drawer" , "Close navigation drawer"
        );

      drawer.setDrawerListener(toggle);
      toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       
       View header= navigationView.getHeaderView(0);

      t1_currentName = header.findViewById(R.id.title_text);
       t2_currentEmail = header.findViewById(R.id.email_text);
       i1 = header.findViewById(R.id.imageView);
databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");



databaseReference.addValueEventListener(new ValueEventListener) {

@Override
public void onDataChange(DataSnapshot dataSnapshot) 
{
   current_user_name = dataSnapshot.child(user.getUid().child("name").getValue(String.class)); 
    current_user_email = dataSnapshot.child(user.getUid().child("email").getValue(String.class)); 
    current_user_imageUrl = dataSnapshot.child(user.getUid().child("imageUrl").getValue(String.class));   

    t1_currentName.setText(current_user_name);
    t2_currentEmail.setText(current_user_email);

Picasso.get().load(current_user_imageUrl).into(i1);

}
@Override
public void onCancelled(DatabaseError databaseError ) 
{}




}


    }

    public void onBackPressed()
    {
      DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
      if(drawer.isDrawerOpen(GravityCompat.START))
      {
          drawer.closeDrawer(GravityCompat.START);
      }
      else  {
          super.onBackPressed();


      }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        client = new GoogleApiClient.Builder(this)
              .addApi(LOCATION_SERVICE.API)
              .addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
        .build();
        ;
client.connect();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }
    @Override
 public boolean onOptionsItemSelected(MenuItem item){

       //handles bar item clicks
       //specify parent activity in androidmanifest.xml

       int id = item.getItemId();

       //noninspection simplifiableifstatement

       if(id == R.id.action_settings){return true; }
       return super.onOptionsItemSelected(item);


        }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       // handles navigation view item clicks

        int id = item.getItemId();
        if(id==R.id.nav_joinRoute) { }

        Intent myIntent  = new Intent(UserLocationMainActivity.this,JoinRoute.class);
        startActivity(myIntent);


        else if (id==R.id.nav_joinedRoute) { }
        else if (id==R.id.nav_shareLoc) { }
        
        else if (id==R.id.nav_inviteUsers) { }

          Intent i = new Intent (Intent.ACTION_SEND);
        ((Intent) i).putExtra(Intent EXTRA_TEXT,"My location is :"+"https://www.google.com/maps/@"+latlng.latitude", "latlng.longitude+"17z");

        startActivity(i.createChooser(i,"share using:"));


          else if (id==R.id.nav_signOut) { }
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null)
        {
            auth.signOut();
            finish();
            Intent myIntent = new Intent(UserLocationMainActivity.this,MainActivity.class);
        }

DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
 request = new LocationRequest().create();
 request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
 request.setInterval(3000);


     if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED);
        LocationServices.FusedLocationApi.requestLocationUpdates(client,request,this);


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
      if(location == null)

      {
          Toast.makeText(getApplicationContext(),"could not get location", Toast.LENGTH_SHORT).show();
      }
else
    {
        latlng = new LatLng(location.getLatitude(),location.getLongitude());
    MarkerOptions options = new MarkerOptions();
    options.position(latlng);
    options.title("current location");
    mMap.addMarker(options);

    }


    }





}
