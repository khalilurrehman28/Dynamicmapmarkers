package com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.UI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.Model.UploadImageResponse;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.ProgressRequestBody;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.MainActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.BitmapFactory.decodeFile;

public class PostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ProgressRequestBody.UploadCallbacks{

    private static final int REQUEST= 112;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int CROP_IMAGE = 301;
    @BindView(R.id.imageUpload) ImageView userImage;
    @BindView(R.id.postDescription)
    EditText postDescription;
    @BindView(R.id.uploadPost)
    FloatingActionButton uploadPost;
    @BindView(R.id.frame)
    RelativeLayout frame;
    Snackbar snackbar;
    @BindView(R.id.goBackActivity) ImageView goback;
    @BindView(R.id.doCrop) ImageView doCrop;
    String mediaPath;
    double latitude,longitude;
    ProgressDialog pDialog;
    String Address,Address1,City,State,Country,PostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_post);
        ButterKnife.bind(this);
        latitude=0.0;
        longitude=0.0;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mediaPath = getIntent().getStringExtra("mediaPath");

        if (!mediaPath.equals("")){
           // userImage.setImageBitmap(decodeSampledBitmapFromResource(mediaPath, 4000, 4000));
            //Toast.makeText(PostActivity.this, "Image selected "+mediaPath, Toast.LENGTH_LONG).show();
            File imgFile = new  File(mediaPath);

            if(imgFile.exists()){

                Bitmap myBitmap = decodeFile(imgFile.getAbsolutePath());
                userImage.setImageBitmap(myBitmap);

            }
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        doCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage(mediaPath);
            }
        });

        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String post_des = postDescription.getText().toString();

                    if (latitude != 0.0 && longitude!= 0.0){
                        Log.e("getLocation","address "+Address+"address1 "+Address1+"city "+City+"state "+State+"country "+Country+"postalCode "+PostalCode);
                        uploadYourPost(mediaPath,post_des);
                        Toast.makeText(PostActivity.this, "address "+Address+" address1 "+Address1+" city "+City+" state "+State+" country "+Country+" postalCode "+PostalCode, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PostActivity.this, "We don't find your location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void cropImage(String mediaPath) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            File f = new File(mediaPath);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            cropIntent.putExtra("crop", "false");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 1024); //512
            cropIntent.putExtra("outputY", 1024); //512

            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_IMAGE);
        }
        catch (ActivityNotFoundException e) {
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                   Uri uri = data.getData();
                    if (uri != null) {
                        Bitmap photo = decodeUriAsBitmap(uri);
                        mediaPath = getRealPathFromURI(uri);
                        userImage.setImageBitmap(photo);
                    } else {
                        Toast.makeText(this, "Image not cropped", Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getApplicationContext().getContentResolver().query(contentURI, null, null, null, null);
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

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    private void uploadYourPost(String mediaPath,  String postDescription) {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading Post");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        showpDialog();
        File file = new File(mediaPath);
        File compressedImageFile = null;
        try {
            compressedImageFile = new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProgressRequestBody fileBody = new ProgressRequestBody(compressedImageFile, this);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("POST_IMAGE", file.getName(), fileBody);
        if (!checkInternetState.getInstance(PostActivity.this).isOnline()) {
            hidepDialog();
            Toast.makeText(PostActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<UploadImageResponse> call = service.upload_post_request(fileToUpload,Integer.parseInt((new PreferenceManager(PostActivity.this).getUserID())),postDescription,latitude,longitude,Address,PostalCode,City);
            call.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                    hidepDialog();
                    if (response.body().getStatus()) {
                        //new PrefManager(PostActivity.this).saveUserImage(response.body().getMessage());
                        Log.e("Message true", response.body().getMessage());
                        Toast.makeText(PostActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(PostActivity.this,MainActivity.class);
                        // intent.putExtra("studentId",getStudentID());
                        //startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                    } else {
                        Toast.makeText(PostActivity.this, "post not uploaded", Toast.LENGTH_SHORT).show();
                        Log.e("Message", response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                }
            });
        }
    }

    private boolean validate() {
        if (mediaPath.equals("")){
            snackbar = Snackbar.make(frame, "Please select some image", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            return false;
        }
       // checkPermissionUser();
        return true;
    }
    //to show progress dialog
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    //to hide progress
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       // mLocationRequest.setInterval(10); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Address loactionAddress;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            loactionAddress = addresses.get(0);
            if(loactionAddress!=null)
            {

                String address = loactionAddress.getAddressLine(0);
                String address1 = loactionAddress.getAddressLine(1);
                String city = loactionAddress.getLocality();
                String state = loactionAddress.getAdminArea();
                String country = loactionAddress.getCountryName();
                String postalCode = loactionAddress.getPostalCode();


                String currentLocation;

                if(!TextUtils.isEmpty(address))
                {
                    currentLocation=address;
                    Address=address;

                    if (!TextUtils.isEmpty(address1))
                        currentLocation+="\n"+address1;
                    Address1 =address1;

                    if (!TextUtils.isEmpty(city))
                    {
                        currentLocation+="\n"+city;
                        City = city;
                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+=" - "+postalCode;
                        PostalCode = postalCode;
                    }
                    else
                    {
                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+="\n"+postalCode;
                    }

                    if (!TextUtils.isEmpty(state))
                        State =state;
                        currentLocation+="\n"+state;

                    if (!TextUtils.isEmpty(country))
                        currentLocation+="\n"+country;
                        Country = country;
                    Log.d("location",""+currentLocation);

                    //Toast.makeText(this, "location  "+currentLocation, Toast.LENGTH_SHORT).show();
                }

            }
            else
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (mLocationRequest!=null){
            //txtOutput.setText(location.toString());
            //Toast.makeText(this, ""+location.getLatitude()+"--"+location.getLongitude(), Toast.LENGTH_SHORT).show();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Location", ""+location.getLatitude()+"--"+location.getLongitude());
            //txtOutput.setText();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}
