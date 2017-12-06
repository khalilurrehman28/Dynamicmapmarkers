package com.dupleit.mapmarkers.dynamicmapmarkers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.UI.PostActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost.gridUiActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.LoginActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.ReadPostActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.backgroundOperations.backgroundoperation;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.multiPIcs.MultiDrawable;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Datum>,
        ClusterManager.OnClusterInfoWindowClickListener<Datum>,
        ClusterManager.OnClusterItemClickListener<Datum>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Datum> {

    //static final float COORDINATE_OFFSET = 0.002f;
    private static final int REQUEST= 112;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private ClusterManager<Datum> mClusterManager;
    private GoogleMap mMap;
    @BindView(R.id.fab_menu) FloatingActionMenu fam;
    @BindView(R.id.fab1) FloatingActionButton fabCamera;
    @BindView(R.id.fab2) FloatingActionButton fabGallery;
    String checktype=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if ((new PreferenceManager(this).getUserID()).equals("")){
            startActivity(new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish(); //to end current activity
        }

        setUpMap();
        forFab();
    }

    private void forFab() {
        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    //showToast("Menu is opened");
                } else {
                    //showToast("Menu is closed");
                }
            }
        });

        //handling each floating action button clicked
        fabCamera.setOnClickListener(onButtonClick());
        fabGallery.setOnClickListener(onButtonClick());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
    }
    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabCamera) {
                    checkPermissionUser("camera");
                    checktype = "camera";
                    //showToast("Button Add clicked");
                } else if (view == fabGallery){
                    checkPermissionUser("gallery");
                    checktype = "gallery";
                }
                fam.close(true);
            }
        };
    }

    private void checkPermissionUser(String typeImage) {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
                if (typeImage.equals("camera")){
                    openCamera();
                }else{
                    selectFromGallery();
                }
            }
        } else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
            if (typeImage.equals("camera")){
                openCamera();
            }else{
                selectFromGallery();
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    if (checktype.equals("camera")){
                        openCamera();
                    }else{
                        selectFromGallery();
                    }
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
            break;

        }
    }
    private void openCamera() {
        showToast("camera");
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    private void selectFromGallery() {
        showToast("Gallery");
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate  (R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uploadPost:
                Intent intent= new Intent(this,PostActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    /**
     * Run the demo-specific code.
     */

    protected GoogleMap getMap() {
        return mMap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        /*try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));

            if (!success) {
                Log.e("Map", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map", "Can't find style. Error: ", e);
        }*/

        startDemo();
        new backgroundoperation(mClusterManager,getMap()).execute();
    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<Datum> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Datum person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            Drawable draw = null;
            try {
                draw = new BitmapDrawable(getResources(),convertUrlToDrawable(person.getPOSTIMAGEURL()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.setImageDrawable(draw);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.getUSERNAME());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Datum> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<>(Math.min(1, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Datum p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 2) break;
                    Drawable drawable = null;
                try {
                    drawable = new BitmapDrawable(getResources(),convertUrlToDrawable(p.getUSERIMAGE()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);
            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() >= 3;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Datum> cluster) {
        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.
        // Create the builder to collect all essential cluster items for the bounds.
        //Log.d("Zoom",""+mMap.getCameraPosition().zoom);
        List<LatLng> userLatLang = new ArrayList<>();
        if (mMap.getCameraPosition().zoom >=15.0 && mMap.getCameraPosition().zoom<=21.0){
            for (ClusterItem item : cluster.getItems()) {
                Log.d("User",""+item.getTitle());
                userLatLang.add(item.getPosition());
            }
            gotonextPage(userLatLang);

        }else{
            // Show a toast with some info when the cluster is clicked.
            String firstName = cluster.getItems().iterator().next().getUSERNAME();
            Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (ClusterItem item : cluster.getItems()) {
                builder.include(item.getPosition());
            }
            // Get the LatLngBounds
            LatLngBounds bounds = builder.build();
            // Animate camera to the bounds
            //bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            try {
                getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 7));
                //getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void gotonextPage(List<LatLng> userLatLang) {
        Intent i = new Intent(MainActivity.this, gridUiActivity.class);
        i.putParcelableArrayListExtra("userlatlang", (ArrayList<? extends Parcelable>) userLatLang);
        startActivity(i);
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Datum> cluster) {
        // Does nothing, but you could go to a list of the users.
        //LatLngBounds.Builder builder = LatLngBounds.builder();

    }

    @Override
    public boolean onClusterItemClick(Datum item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Toast.makeText(this, ""+item.getUSERNAME(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ReadPostActivity.class).putExtra("PostID",item.getPOSTID()));
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Datum item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    protected void startDemo() {
        //getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
        mClusterManager = new ClusterManager<Datum>(this, getMap());
        mClusterManager.setRenderer(new PersonRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.clearItems();
        mClusterManager.cluster();
        getMap().setOnCameraIdleListener(mClusterManager);

    }

    private Bitmap convertUrlToDrawable(String urlResource) throws IOException {
        URL url = new URL(Appconstant.weburl+urlResource);
        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
    }

   /* @OnClick(R.id.uploadPost)
    public void fabView(){
        startActivity(new Intent(this,PostActivity.class));
    }*/
}
