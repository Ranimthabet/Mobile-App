package com.example.projetfinetude;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class RegionInformation extends AppCompatActivity {
    TextView region_wifi,region_reseau,region_resto,region_home
            ,region_coffe,region_bar,region_kiosque,
             region_gaz,region_store,region_kids,
            region_bus,region_taxi,region_boats,
            region_cycle,region_rabbish,region_metro
            ,region_park,region_energie,region_light;
    ImageView wifi,reseau,resto,home,coffee,bar,kiosque,
            gaz,store,kids,bus,taxi,boats
            ,cycle,rabbish,metro,park,energie,light;
    DatabaseReference directionRef;
    String testRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_information);
        region_wifi = findViewById(R.id.wifi_region);
        wifi = findViewById(R.id.logo_wifi);reseau = findViewById(R.id.resrau_logo);resto = findViewById(R.id.resto_logo);
        home = findViewById(R.id.home_logo);coffee = findViewById(R.id.coffe_logo);bar = findViewById(R.id.bar_logo);
        kiosque = findViewById(R.id.kiosque_logo);gaz = findViewById(R.id.gaz_logo);store = findViewById(R.id.store_logo);
        kids = findViewById(R.id.kis_logo);bus = findViewById(R.id.bus_logo);taxi = findViewById(R.id.taxi_logo);
        boats = findViewById(R.id.boats_logo);cycle = findViewById(R.id.cycle_logo);rabbish = findViewById(R.id.rabbish_logo);
        metro = findViewById(R.id.metro_logo);park = findViewById(R.id.park_logo);energie = findViewById(R.id.energie_logo);
        light = findViewById(R.id.light_logo);

        region_reseau = findViewById(R.id.reseau_region);
        region_resto = findViewById(R.id.resto_region);
        region_home = findViewById(R.id.home_region);
        region_coffe = findViewById(R.id.coffe_region);
        region_bar = findViewById(R.id.bar_region);
        region_kiosque = findViewById(R.id.kiosque_region);
        region_gaz = findViewById(R.id.gaz_region);
        region_store = findViewById(R.id.store_region);
        region_kids = findViewById(R.id.kids_region);
        region_bus = findViewById(R.id.bus_region);
        region_taxi = findViewById(R.id.taxi_region);
        region_boats = findViewById(R.id.boats_region);
        region_cycle = findViewById(R.id.cycle_region);
        region_rabbish = findViewById(R.id.rabbish_region);
        region_metro = findViewById(R.id.metro_region);
        region_park = findViewById(R.id.park_region);
        region_energie = findViewById(R.id.energie_region);
        region_light = findViewById(R.id.light_region);
        TextView textView = new TextView(this);
        testRef =getIntent().getStringExtra("Direction");

        directionRef = FirebaseDatabase.getInstance().getReference().child("Directions").child(testRef).child("Proprities");
        directionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ref001 = snapshot.child("wifi").getValue().toString();
                    String ref002 = snapshot.child("reseau").getValue().toString();
                    String ref003 = snapshot.child("resto").getValue().toString();
                    String ref004 = snapshot.child("home").getValue().toString();
                    String ref005 = snapshot.child("coffee").getValue().toString();
                    String ref006 = snapshot.child("bar").getValue().toString();
                    String ref007 = snapshot.child("kiosque").getValue().toString();
                    String ref008 = snapshot.child("gaz").getValue().toString();
                    String ref009 = snapshot.child("store").getValue().toString();
                    String ref010 = snapshot.child("kids").getValue().toString();
                    String ref011 = snapshot.child("bus").getValue().toString();
                    String ref012 = snapshot.child("taxi").getValue().toString();
                    String ref013 = snapshot.child("boats").getValue().toString();
                    String ref014 = snapshot.child("cycle").getValue().toString();
                    String ref015 = snapshot.child("rabbish").getValue().toString();
                    String ref016 = snapshot.child("metro").getValue().toString();
                    String ref017 = snapshot.child("park").getValue().toString();
                    String ref018 = snapshot.child("energie").getValue().toString();
                    String ref019 = snapshot.child("light").getValue().toString();
                    if(ref001.equals("Available")){
                        region_wifi.setText(ref001);
                        wifi.setColorFilter(Color.GREEN);
                    }else{
                        region_wifi.setText(ref001);
                        wifi.setColorFilter(Color.RED);
                        wifi.setImageResource(R.drawable.ic_nowifi);
                    }
                    if(ref002.equals("Available")){
                        region_reseau.setText(ref002);
                        reseau.setColorFilter(Color.GREEN);
                    }else{
                        region_reseau.setText(ref002);
                        reseau.setColorFilter(Color.RED);
                        reseau.setImageResource(R.drawable.ic_nodata);
                    }
                    if(ref003.equals("Available")){
                        region_resto.setText(ref003);
                        resto.setColorFilter(Color.GREEN);
                    }else{
                        region_resto.setText(ref003);
                        resto.setColorFilter(Color.RED);
                        resto.setImageResource(R.drawable.ic_noresto);
                    }
                    if(ref004.equals("Available")){
                        region_home.setText(ref004);
                        home.setColorFilter(Color.GREEN);
                    }else{
                        region_home.setText(ref004);
                        home.setColorFilter(Color.RED);
                    }
                    if(ref005.equals("Available")){
                        region_coffe.setText(ref005);
                        coffee.setColorFilter(Color.GREEN);
                    }else{
                        region_coffe.setText(ref005);
                        coffee.setColorFilter(Color.RED);
                    }
                    if(ref006.equals("Available")){
                        region_bar.setText(ref006);
                        bar.setColorFilter(Color.GREEN);
                    }else{
                        region_bar.setText(ref006);
                        bar.setColorFilter(Color.RED);
                    }
                    if(ref007.equals("Available")){
                        region_kiosque.setText(ref007);
                        kiosque.setColorFilter(Color.GREEN);
                    }else{
                        region_kiosque.setText(ref007);
                        kiosque.setColorFilter(Color.RED);
                    }
                    if(ref008.equals("Available")){
                        region_gaz.setText(ref008);
                        gaz.setColorFilter(Color.GREEN);
                    }else{
                        region_gaz.setText(ref008);
                        gaz.setColorFilter(Color.RED);
                        gaz.setImageResource(R.drawable.ic_nogaz);
                    }
                    if(ref009.equals("Available")){
                        region_store.setText(ref009);
                        store.setColorFilter(Color.GREEN);
                    }else{
                        region_store.setText(ref009);
                        store.setColorFilter(Color.RED);
                        store.setImageResource(R.drawable.ic_nostore);
                    }
                    if(ref010.equals("Available")){
                        region_kids.setText(ref010);
                        kids.setColorFilter(Color.GREEN);
                    }else{
                        region_kids.setText(ref010);
                        kids.setColorFilter(Color.RED);
                    }
                    if(ref011.equals("Available")){
                        region_bus.setText(ref011);
                        bus.setColorFilter(Color.GREEN);

                    }else{
                        region_bus.setText(ref011);
                        bus.setColorFilter(Color.RED);
                        bus.setImageResource(R.drawable.ic_nobus);
                    }
                    if(ref012.equals("Available")){
                        region_taxi.setText(ref012);
                        taxi.setColorFilter(Color.GREEN);
                    }else{
                        region_taxi.setText(ref012);
                        taxi.setColorFilter(Color.RED);
                    }
                    if(ref013.equals("Available")){
                        region_boats.setText(ref013);
                        boats.setColorFilter(Color.GREEN);
                    }else{
                        region_boats.setText(ref013);
                        boats.setColorFilter(Color.RED);
                    }
                    if(ref014.equals("Available")){
                        region_cycle.setText(ref014);
                        cycle.setColorFilter(Color.GREEN);
                    }else{
                        region_cycle.setText(ref014);
                        cycle.setColorFilter(Color.RED);
                    }
                    if(ref015.equals("Available")){
                        region_rabbish.setText(ref015);
                        rabbish.setColorFilter(Color.GREEN);
                    }else{
                        region_rabbish.setText(ref015);
                        rabbish.setColorFilter(Color.RED);
                    }
                    if(ref016.equals("Available")){
                        region_metro.setText(ref016);
                        metro.setColorFilter(Color.GREEN);
                    }else{
                        region_metro.setText(ref016);
                        metro.setColorFilter(Color.RED);
                    }
                    if(ref017.equals("Available")){
                        region_park.setText(ref017);
                        park.setColorFilter(Color.GREEN);
                    }else{
                        region_park.setText(ref017);
                        park.setColorFilter(Color.RED);
                    }
                    if(ref018.equals("Available")){
                        region_energie.setText(ref018);
                        energie.setColorFilter(Color.GREEN);
                    }else{
                        region_energie.setText(ref018);
                        energie.setColorFilter(Color.RED);
                    }
                    if(ref019.equals("Available")){
                        region_light.setText(ref019);
                        light.setColorFilter(Color.GREEN);
                    }else{
                        region_light.setText(ref019);
                        light.setColorFilter(Color.RED);
                    }
  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })      ;
    }
}