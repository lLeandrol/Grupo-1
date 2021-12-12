package com.example.prueba10;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleProductoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tev_detalle_nombre;
    private TextView tev_detalle_categoria;
    private ImageView imv_detalle_imagen;
    private GoogleMap map_detalle;
    private Double latitud;
    private Double longitud;

    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        tev_detalle_nombre = findViewById(R.id.tev_detalle_nombre);
        tev_detalle_categoria = findViewById(R.id.tev_detalle_categoria);
        imv_detalle_imagen = findViewById(R.id.imv_detalle_imagen);

        Bundle extras = getIntent().getExtras();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_detalle);

        mapFragment.getView().setVisibility(View.GONE);
        //mapFragment.getMapAsync(this);

        if (extras != null) {

            try {
                JSONObject producto = new JSONObject(extras.getString("producto"));

                String nombre = producto.getString("nombre");
                String categoria = producto.getString("categoria");
                String imagen = producto.getString("image");
                latitud = producto.getDouble("latitud");
                longitud = producto.getDouble("longitud");

                if (latitud != 0.0 && longitud != 0.0) {
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    mapFragment.getMapAsync(this);
                }

                tev_detalle_nombre.setText(nombre);
                tev_detalle_categoria.setText(categoria);

                Glide.with(this).load(imagen).into(imv_detalle_imagen);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        LatLng miUbicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));

        // Move the camera instantly to Sydney with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(miUbicacion )      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}