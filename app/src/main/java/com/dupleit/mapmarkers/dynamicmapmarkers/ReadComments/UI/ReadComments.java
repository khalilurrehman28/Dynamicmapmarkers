package com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.UI;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.GridSpacingItemDecoration;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Adapter.commentAdapter;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.CommentData;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.CommentResponse;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.commentMessageObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadComments extends AppCompatActivity {
    @BindView(R.id.messageRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.emoji_btn) ImageView emojiImageView;
    public EmojIconActions emojIcon;
    ArrayList<CommentData> commentList;
    commentAdapter mAdapter;
    @BindView(R.id.root_view)
    public View rootView;
    @BindView(R.id.messageEditText) EmojiconEditText mMessageEditText;
    @BindView(R.id.sendButton) ImageButton mSendButton;
    @BindView(R.id.selectImage) ImageButton selectImage;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comment);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initilize();
        setTitle("Comment");

    }

    private void initilize() {
        commentList = new ArrayList<>();
        mAdapter = new commentAdapter(getApplicationContext(), commentList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        database = FirebaseDatabase.getInstance();
        mFirebaseReference = database.getReference().child("post");


        prepareComments();

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectImage.setVisibility(View.VISIBLE);
                mSendButton.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    selectImage.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.VISIBLE);
                    mSendButton.setEnabled(true);

                    }else {
                    mSendButton.setEnabled(false);
                    selectImage.setVisibility(View.VISIBLE);
                    mSendButton.setVisibility(View.GONE);

                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emojIcon = new EmojIconActions(this, rootView, mMessageEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.input_emoji);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                //Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                //Log.e(TAG, "Keyboard closed");
            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mMessageEditText.getText().toString().trim().equals("")){
                    sendComment(mMessageEditText.getText().toString().trim());
                }
            }
        });
    }

    private void sendComment(String getEditTextValue) {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String DateToStr = format.format(curDate);
        commentMessageObject comment = new commentMessageObject(false,DateToStr,getEditTextValue,false,false,"",Integer.parseInt(new PreferenceManager(getApplicationContext()).getUserID()),(new PreferenceManager(getApplicationContext()).getUsername()));
        mFirebaseReference.child(getIntent().getStringExtra("PostID")).push().setValue(comment);
        mMessageEditText.setText("");
        /*if (!checkInternetState.getInstance(ReadComments.this).isOnline()) {
            Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            //new CustomToast().Show_Toast(ctx, view,"Please Check Your Internet Connection." );
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<CommentResponse> userCall = service.addcommenttopost_request(1,2,getEditTextValue);
            userCall.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    if (response.body().getStatus()) {
                        //Toast.makeText(ReadComments.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                        CommentData oth = new CommentData();
                        oth.setCOMMENTDATETIME(DateToStr);
                        oth.setUSERIMAGE(Appconstant.weburl+"upload/image/userimage.jpg");
                        oth.setUSERNAME("Dupleit2");
                        oth.setCOMMENTTEXT(getEditTextValue);
                        commentList.add(oth);
                        //notifyItemRemoved(position);
                        mAdapter.notifyDataSetChanged();
                        mMessageEditText.setText("");
                    }else{
                        Toast.makeText(ReadComments.this, "Comment not added", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }*/
       /* commentMessageObject comment = new commentMessageObject(1,"Khalil","hello","21321321321","ksmclasckmalsc",true,true,true);
        mFirebaseReference.push().setValue(comment);*/
    }



    private void prepareComments() {
        if (!checkInternetState.getInstance(ReadComments.this).isOnline()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ReadComments.this, "Please Check your internet connection.", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.GONE);
            mFirebaseReference.child(getIntent().getStringExtra("PostID")).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    commentMessageObject commentMessageObject = dataSnapshot.getValue(com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.commentMessageObject.class);
                    CommentData oth = new CommentData();
                    oth.setCOMMENTDATETIME(commentMessageObject.getComment_datetime());
                    oth.setCOMMENTDELETE("nope");
                    oth.setCOMMENTIMAGEURL("");
                    oth.setUSEREMAIL("");
                    oth.setUSERIMAGE("");
                    oth.setUSERNAME(commentMessageObject.getUser_name());
                    oth.setCOMMENTTEXT(commentMessageObject.getCommment_text());
                    /* this.questionList.add(0, question);
    notifyItemInserted(0);
    mRecyclerView.smoothScrollToPosition(0);
                    * */

                    commentList.add(0,oth);
                    mAdapter.notifyItemInserted(0);
                    recyclerView.smoothScrollToPosition(0);
                    //mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



           /* APIService service = ApiClient.getClient().create(APIService.class);
            Call<CommentResponse> userCall = service.getpostcomment_request(1);
            userCall.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body().getStatus()) {
                        for (int i = 0; i < response.body().getCommentData().size(); i++) {
                            CommentData oth = new CommentData();
                            oth.setCOMMENTDATETIME(response.body().getCommentData().get(i).getCOMMENTDATETIME());
                            oth.setCOMMENTDELETE(response.body().getCommentData().get(i).getCOMMENTDELETE());
                            oth.setCOMMENTIMAGEURL(response.body().getCommentData().get(i).getCOMMENTIMAGEURL());
                            oth.setUSEREMAIL(response.body().getCommentData().get(i).getUSEREMAIL());
                            oth.setUSERIMAGE(response.body().getCommentData().get(i).getUSERIMAGE());
                            oth.setUSERNAME(response.body().getCommentData().get(i).getUSERNAME());

                            oth.setCOMMENTTEXT(response.body().getCommentData().get(i).getCOMMENTTEXT());
                            //Log.d("MoneyConvert",new digitToNumber().Converter(mView.getContext()(),response.body().getListData().get(i).getCommentData().getpROPPRICE(),true));
                            commentList.add(oth);
                            mAdapter.notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(ReadComments.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });*/
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
