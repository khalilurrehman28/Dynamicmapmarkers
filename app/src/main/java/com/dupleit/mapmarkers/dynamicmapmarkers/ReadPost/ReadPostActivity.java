package com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.DateConverter;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.commentMessageObject;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.UI.ReadComments;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.model.PostDatum;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.model.UserPost;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    @BindView(R.id.imageDescription) TextView postDescription;
    @BindView(R.id.Likes) TextView Likes;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.ListerImage) CircleImageView ListerImage;
    @BindView(R.id.layoutLike) LinearLayout layoutLike;
    @BindView(R.id.card_view) CardView cardView;
    @BindView(R.id.progressBar) ProgressBar progressBar;


    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;

    @OnClick(R.id.layoutComment)
    public void showComments(){
        startActivity(new Intent(this, ReadComments.class).putExtra("PostID",getIntent().getStringExtra("PostID")));
    }

    @OnClick(R.id.comments)
    public void readComments(){
        startActivity(new Intent(this, ReadComments.class).putExtra("PostID",getIntent().getStringExtra("PostID")));

    }
    @OnClick(R.id.layoutLike)
    public void likePost(){
        startActivity(new Intent(this, ReadComments.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_show);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
        mFirebaseReference = database.getReference().child("post");
        //Toast.makeText(this, ""+getIntent().getStringExtra("PostID"), Toast.LENGTH_SHORT).show();
        UpdateUI(getIntent().getStringExtra("PostID"));
        setTitle("Profile");
        getcommentCount(getIntent().getStringExtra("PostID"));
    }

    private void getcommentCount(String postID) {
        //Toast.makeText(this, ""+postID, Toast.LENGTH_SHORT).show();


        mFirebaseReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    Log.e(snap.getKey(),snap.getChildrenCount() + "");

                }*/
                comments.setText(dataSnapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateUI(String postID) {
        APIService service = ApiClient.getClient().create(APIService.class);
        Call<UserPost> userCall = service.getUserDataByPost(postID);
        userCall.enqueue(new Callback<UserPost>() {
            @Override
            public void onResponse(Call<UserPost> call, retrofit2.Response<UserPost> response) {
                progressBar.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                if (response.isSuccessful() && response.body().getStatus()){
                    //Toast.makeText(ReadPostActivity.this, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    List<PostDatum> postData = response.body().getPostData();
                    //Toast.makeText(ReadPostActivity.this, ""+postData.size(), Toast.LENGTH_SHORT).show();
                    for (PostDatum post : postData) {
                        userName.setText(post.getUSERNAME());
                        uploadImageTime.setText("posted on "+(new DateConverter().convertDate(post.getPOSTDATETIME())));
                        String des =post.getPOSTDESCRIPTION();
                        if (des.equals("")){
                            postDescription.setText("");
                            postDescription.setVisibility(View.GONE);
                        }else {
                            postDescription.setText(des.substring(1,des.length() -1)); //replace is use to replace ("")

                        }

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
