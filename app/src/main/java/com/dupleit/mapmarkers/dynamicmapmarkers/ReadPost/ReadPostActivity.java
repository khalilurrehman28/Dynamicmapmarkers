package com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.UI.ReadComments;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.model.PostDatum;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.model.UserPost;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by android on 16/11/17.
 */

public class ReadPostActivity extends AppCompatActivity {
    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.uploadImageLocation) TextView uploadImageLocation;
    @BindView(R.id.uploadImageTime) TextView uploadImageTime;
    @BindView(R.id.play_list_cover) ImageView play_list_cover;
    @BindView(R.id.imageDescription) TextView imageDescription;
    @BindView(R.id.Likes) TextView Likes;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.ListerImage) CircleImageView ListerImage;
    @BindView(R.id.layoutFavorite) LinearLayout layoutFavorite;

    @OnClick(R.id.layoutComment)
    public void showComments(){
        startActivity(new Intent(this, ReadComments.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_show);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toast.makeText(this, ""+getIntent().getStringExtra("PostID"), Toast.LENGTH_SHORT).show();
        UpdateUI(getIntent().getStringExtra("PostID"));
        setTitle("Profile");
    }

    private void UpdateUI(String postID) {
        APIService service = ApiClient.getClient().create(APIService.class);
        Call<UserPost> userCall = service.getUserDataByPost(postID);
        userCall.enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, retrofit2.Response<UserPost> response) {
                if (response.isSuccessful() && response.body().getStatus()){
                    Toast.makeText(ReadPostActivity.this, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    List<PostDatum> postData = response.body().getPostData();
                    Toast.makeText(ReadPostActivity.this, ""+postData.size(), Toast.LENGTH_SHORT).show();
                    for (PostDatum post : postData) {
                        userName.setText(post.getUSERNAME());
                        uploadImageTime.setText(post.getPOSTDATETIME());
                        imageDescription.setText(post.getPOSTDESCRIPTION());

                        Glide
                             .with(getApplicationContext())
                             .load(Appconstant.weburl+post.getPOSTIMAGEURL())
                             //.override(600, 200) // resizes the image to these dimensions (in pixel). resize does not respect aspect ratio
                             .into(play_list_cover);
                        Glide
                                .with(getApplicationContext())
                                .load(Appconstant.weburl+post.getUSERIMAGE())
                                //.override(600, 200) // resizes the image to these dimensions (in pixel). resize does not respect aspect ratio
                                .into(ListerImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
