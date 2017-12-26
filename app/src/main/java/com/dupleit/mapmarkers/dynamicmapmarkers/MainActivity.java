package com.dupleit.mapmarkers.dynamicmapmarkers;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.UI.PostActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.BuilderManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost.gridUiActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.LoginActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Profile.ProfileActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.ReadPostActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.backgroundOperations.backgroundoperation;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.multiPIcs.MultiDrawable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Datum>,
        ClusterManager.OnClusterInfoWindowClickListener<Datum>,
        ClusterManager.OnClusterItemClickListener<Datum>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Datum>,View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //static final float COORDINATE_OFFSET = 0.002f;
    private static final int REQUEST= 112;
    private static final int GALLERY_REQUEST = 101;
    private static final int CAMERA_REQUEST= 100;
    private Uri picUri;
    private File pic;

    private ClusterManager<Datum> mClusterManager;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_main,fab_gallery,fab_camera;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    String checktype=null;
    String current_location;
    String address,address1,city,state,country,postalCode,currentLocation;
    CircleImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_main);
        if ((new PreferenceManager(this).getUserID()).equals("")){
            startActivity(new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish(); //to end current activity
        }
        // for boom menu option on action bar



        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBar = mInflater.inflate(R.layout.activity_boom, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);
        userImage =findViewById(R.id.UserProfileImage);
        String sharedUserImage = Appconstant.weburl+(new PreferenceManager(this).getUserImage());
        if (!sharedUserImage.equals("")){
            Glide.with(this).load(sharedUserImage).into(userImage);
        }else {
            Glide.with(this).load(R.drawable.ic_account_circle_black_36dp).into(userImage);
        }
        BoomMenuButton rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);
        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        ArrayList<Integer> names = new ArrayList<>();
        names.add(R.string.profile);
        names.add(R.string.logout);

        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
            rightBmb.addBuilder(BuilderManager.getHamButtonBuilder(names.get(i)));

        rightBmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if (index == 0){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }else if(index == 1){
                    new PreferenceManager(getApplicationContext()).saveUserDetails("","","","","");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });



        // for creatting map
        setUpMap();
        fab_main = findViewById(R.id.fab_main);
        fab_gallery = findViewById(R.id.fab_gallery);
        fab_camera = findViewById(R.id.fab_camera);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab_main.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);
        fab_camera.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_main:

                animateFAB();
                break;
            case R.id.fab_gallery:
                checkPermissionUser("gallery");
                checktype = "gallery";
                animateFAB();
                //startActivity(new Intent(this,UploadPostActivity.class));
                break;
            case R.id.fab_camera:
                checkPermissionUser("camera");
                checktype = "camera";
                animateFAB();
                break;
        }
    }

    public void animateFAB(){

        if(isFabOpen){

            fab_main.startAnimation(rotate_backward);
            fab_gallery.startAnimation(fab_close);
            fab_camera.startAnimation(fab_close);
            fab_gallery.setClickable(false);
            fab_camera.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {
            fab_main.startAnimation(rotate_forward);
            fab_gallery.startAnimation(fab_open);
            fab_camera.startAnimation(fab_open);
            fab_gallery.setClickable(true);
            fab_camera.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    private void checkPermissionUser(String typeImage) {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
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
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
    private void openCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {

            /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, CAMERA_REQUEST);*/
            try {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                pic = new File(Environment.getExternalStorageDirectory(),
                        "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                picUri = Uri.fromFile(pic);

                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);

                cameraIntent.putExtra("return-data", true);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    private void selectFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && picUri != null) {

          /*  Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);*/

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(picUri));
            String mediaPath = String.valueOf(finalFile);
            if (!mediaPath.equals("")) {
                Intent cameradata = new Intent(getApplicationContext(), PostActivity.class);
                cameradata.putExtra("mediaPath", mediaPath);
                Log.e("cameraPath ",mediaPath);
                startActivity(cameradata);
            }else {
                showToast("something went wrong");
            }

            Log.e("cameraUri  ","bitmap "+" image uri"+mediaPath);

        }

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            String mediaPath = getRealPathFromURI(imageUri);

            if (!mediaPath.equals("")) {
                Intent cameradata = new Intent(getApplicationContext(), PostActivity.class);
                cameradata.putExtra("mediaPath", mediaPath);
                Log.e("GalleryPath ",mediaPath);

                startActivity(cameradata);
            }else {
                showToast("something went wrong");
            }
            Log.e("GalleryURi  ","image uri"+mediaPath);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = MainActivity.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        startDemo();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new backgroundoperation(mClusterManager,getMap(),getApplicationContext()).execute();
            }
        }, 2000);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        Address loactionAddress;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            loactionAddress = addresses.get(0);
            if(loactionAddress!=null)
            {

                address = loactionAddress.getAddressLine(0);
                address1 = loactionAddress.getAddressLine(1);
                city = loactionAddress.getLocality();
                state = loactionAddress.getAdminArea();
                country = loactionAddress.getCountryName();
                postalCode = loactionAddress.getPostalCode();

                if(!TextUtils.isEmpty(address))
                {
                    currentLocation=address;

                    if (!TextUtils.isEmpty(address1))
                        currentLocation+="\n"+address1;
                    /*if (!TextUtils.isEmpty(city))
                    {
                        currentLocation+="\n"+city;
                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+=" - "+postalCode;
                    }
                    else
                    {
                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+="\n"+postalCode;
                    }

                    if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                    if (!TextUtils.isEmpty(country))
                        currentLocation+="\n"+country;*/
                    Log.d("location",""+currentLocation);
                    current_location = currentLocation;
                    //Toast.makeText(this, "location  "+address1, Toast.LENGTH_SHORT).show();
                }

            }
            else
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this, "address "+Address+"address1 "+Address1+"city "+City+"state "+State+"country "+Country+"postalCode "+PostalCode, Toast.LENGTH_SHORT).show();

        //for set on marker on current location
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(current_location);
        Log.e("address"," Address "+address1+" , "+city+" state "+state+" country "+country);
        //Toast.makeText(this, "current "+current_location, Toast.LENGTH_SHORT).show();

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //mCurrLocationMarker.setTag(info);
       // mCurrLocationMarker.showInfoWindow();
        //move map camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        
        
    }



    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000);
        //mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
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
            List<Drawable> profilePhotos = new ArrayList<>(Math.min(2, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Datum p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
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
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Datum> cluster) {
        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.
        // Create the builder to collect all essential cluster items for the bounds.
        //Log.d("Zoom",""+mMap.getCameraPosition().zoom);
        if (mMap.getCameraPosition().zoom >=15.0 && mMap.getCameraPosition().zoom<=21.0){
            List<LatLng> userLatLang = new ArrayList<>();
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
           final LatLngBounds bounds = builder.build();
            // Animate camera to the bounds
            //bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            try {
                getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
        //Toast.makeText(this, ""+item.getUSERNAME(), Toast.LENGTH_SHORT).show();
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
        getMap().setOnCameraIdleListener(mClusterManager);

    }

    private Bitmap convertUrlToDrawable(String urlResource) throws IOException {
        URL url = new URL(Appconstant.weburl+urlResource);

        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
    }

}
