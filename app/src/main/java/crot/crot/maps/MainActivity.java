package crot.crot.maps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {
    private MapController mapController;
    EditText etLat,etLong;
    private MapView mapApp;
    private Button btGo;
    Double LatCurrentPosition=0.0, LongCurrentPosition=0.0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        AksesPermissionAndroid();
        mapApp = (MapView) findViewById(R.id.MyMap);
        etLat = (EditText) findViewById(R.id.editTextP1);
        etLong = (EditText) findViewById(R.id.editTextTextP2);
        btGo = (Button) findViewById(R.id.buttonGo);

        mapApp.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        Configuration.getInstance().setUserAgentValue("com.example.petaku");
        mapApp.setBuiltInZoomControls(true);
        mapApp.setMultiTouchControls(true);
        mapController = (MapController) mapApp.getController();
        mapController.setZoom(15);
        GeoPoint point2 = new GeoPoint(-8.689226991247727, 115.26714178826778);
        mapController.setCenter(point2);

        final MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),mapApp);
        myLocationNewOverlay.enableMyLocation();

        mapApp.getOverlays().add(myLocationNewOverlay);
        myLocationNewOverlay.runOnFirstFix(new Runnable() {

            @Override
            public void run() {
                if (myLocationNewOverlay.getMyLocation() != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapApp.getController().animateTo(myLocationNewOverlay.getMyLocation());
                            LatCurrentPosition = myLocationNewOverlay.getMyLocation().getLatitude();
                            LongCurrentPosition = myLocationNewOverlay.getMyLocation().getLongitude();
                        }
                    });

                    GeoPoint mPoint = new GeoPoint(-8.606184934333571, 115.21349053955346);
                    Marker startMarker = new Marker(mapApp);
                    startMarker.setPosition(mPoint);
                    startMarker.setTitle("Resto Ku");
                    startMarker.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
                    mapApp.getOverlays().add(startMarker);

                    MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
                        @Override
                        public boolean singleTapConfirmedHelper(GeoPoint p) {
                            Toast.makeText(getBaseContext(),p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_SHORT).show();
                            etLat.setText(String.valueOf(p.getLatitude()));
                            etLong.setText(String.valueOf(p.getLongitude()));
                            return false;

                        }

                        @Override
                        public boolean longPressHelper(GeoPoint p) {
                            return false;
                        }
                    };


                    btGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this,Maps.class);
                            intent.putExtra("LatTujuan",etLat.getText().toString());
                            intent.putExtra("LongTujuan",etLong.getText().toString());
                            intent.putExtra("LatAsal",String.valueOf(LatCurrentPosition));
                            intent.putExtra("LongAsal",String.valueOf(LongCurrentPosition));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void AksesPermissionAndroid(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET},1001);
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1002);
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1003);
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1004);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted Fine Location", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisson Denied Fine Location", Toast.LENGTH_LONG).show();
                }
                break;
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted Access Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisson Denied Access Internet", Toast.LENGTH_LONG).show();
                }
                break;
            case 1002:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted Access Network State", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisson Denied Access Network State", Toast.LENGTH_LONG).show();
                }
                break;
            case 1003:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted Read External Storage", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisson Denied Read External Storage", Toast.LENGTH_LONG).show();
                }
                break;
            case 1004:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted Write External Storage", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permisson Denied Write External Storage", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}