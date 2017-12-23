package com.dupleit.mapmarkers.dynamicmapmarkers.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.ProgressRequestBody;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.Profile.model.uploadImageResponse;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.CommentResponse;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks{

    @BindView(R.id.UserProfileImage)
    CircleImageView userImage;

    @BindView(R.id.userEmail)
    TextView userEmail;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etContact)
    EditText etContact;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mediaPath = "";
        initializeView();
    }

    @OnClick(R.id.updateProfile)
    public void updateProfile(){
        if (!checkInternetState.getInstance(this).isOnline()) {
            Toast.makeText(this, "Cannot connect to network", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<CommentResponse> userCall = service.updateuserprofile_request(etName.getText().toString(),etContact.getText().toString(),Integer.parseInt((new PreferenceManager(getApplicationContext()).getUserID())));
            userCall.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    if (response.body().getStatus()) {
                        new PreferenceManager(getApplicationContext()).saveUserName(etName.getText().toString());
                        new PreferenceManager(getApplicationContext()).saveUserMobile(etContact.getText().toString());
                        Toast.makeText(ProfileActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Sorry Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }
    }

    private void initializeView() {
        Glide.with(this).load(Appconstant.weburl+(new PreferenceManager(this).getUserImage())).into(userImage);
        userEmail.setText((new PreferenceManager(getApplicationContext()).getUserEmail()));
        etName.setText((new PreferenceManager(getApplicationContext()).getUsername()));
        etContact.setText((new PreferenceManager(getApplicationContext()).getUserNumber()));
    }

    @OnClick(R.id.UserProfileImage)
    public void UserProfileImage(){
        Toast.makeText(this, "I am Clicked", Toast.LENGTH_SHORT).show();
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = "";
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = result.getUri();
                mediaPath = getRealPathFromURI(selectedImageUri);
                //Toast.makeText(ProfileActivity.this, "media path  "+mediaPath, Toast.LENGTH_SHORT).show();
                if (!mediaPath.equals("")){
                    userImage.setImageBitmap(decodeSampledBitmapFromResource(mediaPath, 200, 200));
                    uploadImage(mediaPath);
                    //Toast.makeText(ProfileActivity.this, "Image selected "+mediaPath, Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    private void uploadImage(String mediaPath) {
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        pd.setTitle("Upload Image");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        File file = new File(mediaPath);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("user_image", file.getName(), fileBody);
        String extension = mediaPath.substring(mediaPath.lastIndexOf(".") + 1);
        //Toast.makeText(mContext, ""+extension, Toast.LENGTH_SHORT).show();
        APIService service = ApiClient.getClient().create(APIService.class);
        Call<uploadImageResponse> call = service.updateuserimage_request(fileToUpload,(Integer.parseInt(new PreferenceManager(ProfileActivity.this).getUserID())));
        call.enqueue(new Callback<uploadImageResponse>() {
            @Override
            public void onResponse(Call<uploadImageResponse> call, Response<uploadImageResponse> response) {
                pd.hide();
                if (response.body().getStatus()) {
                    new PreferenceManager(ProfileActivity.this).saveUserImage(response.body().getMessage());
                    Log.e("Message true",response.body().getMessage());
                    Toast.makeText(ProfileActivity.this, "Image successfully uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ProfileActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                    Log.e("Message",response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<uploadImageResponse> call, Throwable t) {
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = ProfileActivity.this.getContentResolver().query(contentURI, null, null, null, null);
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
    public static Bitmap decodeSampledBitmapFromResource(String resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
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
