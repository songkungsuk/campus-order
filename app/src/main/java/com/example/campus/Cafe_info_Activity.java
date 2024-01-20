package com.example.campus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Cafe_info_Activity extends AppCompatActivity implements OnMapReadyCallback {

//    public static final int Raon = 100;
//    public static final int Cobeti = 101;
//
//    TextView text_info = findViewById(R.id.info_text);
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == Raon){
//            text_info.setText("라온 매장 정보 입니다");
//        }
//        if(resultCode == Cobeti){
//            text_info.setText("코베티 매장 정보 입니다");
//        }
//
//    }
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng KDU = new LatLng(37.809495, 127.070538);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(KDU);
        markerOptions.title("경동 대학교");
        markerOptions.snippet("라온");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KDU, 15));
    }
}