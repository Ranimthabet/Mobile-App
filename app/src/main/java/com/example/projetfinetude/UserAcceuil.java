package com.example.projetfinetude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAcceuil extends AppCompatActivity implements OnMapReadyCallback {
    final static float DEFAULT_ZOOM=7;
    final static float REGION_ZOOM=8.5f;
    ImageView btn_map21,btn_map22,ic_dashboard;
    FirebaseAuth firebaseAuth ;
    FirebaseUser userUID ;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acceuil);
        firebaseAuth = FirebaseAuth.getInstance();
        userUID = firebaseAuth.getCurrentUser();
        ic_dashboard = findViewById(R.id.dashboard_logo);
        ic_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAcceuil.this,ReceptionAcceuil.class);
                startActivity(intent);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btn_map21 = findViewById(R.id.btn_map21);
        btn_map22 = findViewById(R.id.btn_map22);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.892166, 9.5615555),DEFAULT_ZOOM));

        MarkerOptions optionBizert =new MarkerOptions().title("Bizerte").position(new LatLng(37.2720905,9.8708565)) ;
        MarkerOptions optionTunis =new MarkerOptions().title("Tunis").position(new LatLng(36.81897,10.16579)) ;
        MarkerOptions optionJandouba =new MarkerOptions().title("Jandouba").position(new LatLng(36.524557, 8.763106)) ;
        MarkerOptions optionSfax =new MarkerOptions().title("Sfax").position(new LatLng(34.777793787204665, 10.751092244881887)) ;
        MarkerOptions optionsBeja =new MarkerOptions().title("Béja").position(new LatLng(36.733717, 9.184808));
        MarkerOptions optionMahdeya =new MarkerOptions().title("Mahdeya").position(new LatLng(35.554495495308764, 11.041718824150989));
        MarkerOptions optionSousse =new MarkerOptions().title("Sousse").position(new LatLng(35.818765746985825, 10.634706253430696));
        MarkerOptions optionMounastir =new MarkerOptions().title("Mounastir").position(new LatLng(35.775154728551506, 10.810348681489808)) ;
        MarkerOptions optionKairouen =new MarkerOptions().title("AL Kairouen").position(new LatLng(35.66354792662059, 10.118989296693233)) ;
        MarkerOptions optionSidiBouzid =new MarkerOptions().title("Sidi Bouzid").position(new LatLng(35.10351674756425, 9.41936562020932)) ;
        MarkerOptions optionGafsa =new MarkerOptions().title("Gafsa").position(new LatLng(34.47532851267564, 8.77277538083702));
        MarkerOptions optionKasserine = new MarkerOptions().title("Al KAssrine").position(new LatLng(35.262402670571845, 8.827714420783687));
        MarkerOptions optionGabes =new MarkerOptions().title("Gabes").position(new LatLng(33.84833022301929, 10.067539590284955));
        MarkerOptions optionZarzis = new MarkerOptions().title("Zarzis").position(new LatLng(33.482186909839136, 11.064976977110694));
        MarkerOptions optionDjerba =new MarkerOptions().title("Djerba").position(new LatLng(33.871580765087664, 10.854274757626872));
        MarkerOptions optionMednine =new MarkerOptions().title("Mednine").position(new LatLng(33.336847050269824, 10.482302938209216));
        googleMap.addMarker(optionBizert);       googleMap.addMarker(optionTunis);       googleMap.addMarker(optionJandouba);
        googleMap.addMarker(optionSfax);       googleMap.addMarker(optionsBeja);       googleMap.addMarker(optionMahdeya);
        googleMap.addMarker(optionSousse);googleMap.addMarker(optionMounastir);googleMap.addMarker(optionKairouen);googleMap.addMarker(optionSidiBouzid);
        googleMap.addMarker(optionGafsa);googleMap.addMarker(optionKasserine);googleMap.addMarker(optionGabes);googleMap.addMarker(optionZarzis);
        googleMap.addMarker(optionDjerba);googleMap.addMarker(optionMednine);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                switch (marker.getTitle()){
                    case "Bizerte" :{
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.2720905,9.8708565),REGION_ZOOM));
                        MarkerOptions capserat =new MarkerOptions().title("Cap Serat").position(new LatLng(37.24078492410884, 9.217328293952988)) ;
                        MarkerOptions rafraf =new MarkerOptions().title("Raf Raf").position(new LatLng(37.19227305726121, 10.198390882631475)) ;
                        MarkerOptions sidimechreg =new MarkerOptions().title("Sidi Mechreg").position(new LatLng(37.165609713381414, 9.140109382954295)) ;
                        MarkerOptions sidighrib =new MarkerOptions().title("Sidi Ghrib").position(new LatLng(37.22113912643934, 9.247158181974836));
                        googleMap.addMarker(capserat);
                        googleMap.addMarker(rafraf);
                        googleMap.addMarker(sidimechreg);
                        googleMap.addMarker(sidighrib);
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                switch (marker.getTitle()){
                                    case "Cap Serat":
                                    {
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.24078492410884, 9.217328293952988),REGION_ZOOM));
                                        String direction = "Cap Serat";
                                        Intent intent = new Intent(getApplicationContext(),Region.class);
                                        intent.putExtra("direction",direction);
                                        intent.putExtra("useruid",userUID.getUid());
                                        Toast.makeText(getApplicationContext(), ""+direction, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), ""+userUID.getUid(), Toast.LENGTH_SHORT).show();

                                        startActivity(intent);
                                        return true;
                                    }
                                    case "Sidi Ghrib":
                                    {
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.22113912643934, 9.247158181974836),REGION_ZOOM));
                                        String direction = "Sidi Ghrib";
                                        Intent intent = new Intent(getApplicationContext(),Region.class);
                                        intent.putExtra("direction",direction);
                                        intent.putExtra("useruid",userUID.getUid());
                                        Toast.makeText(getApplicationContext(), ""+direction, Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        return true;
                                    }
                                }
                                return false;
                            }
                        });





                        return true;
                    }
                }
                return false;
            }
        });
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        btn_map21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Toast.makeText(getApplicationContext(), "Mode:SATELLITE", Toast.LENGTH_SHORT).show();
            }
        });
        btn_map22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Toast.makeText(getApplicationContext(), "Mode:HYBRID", Toast.LENGTH_SHORT).show();


            }
        });
    }
}