package com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.DateConverter;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.CommentData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.GalleryViewHolder>{

    private Context context;
    private List<CommentData> commentsList;

    public class GalleryViewHolder extends RecyclerView.ViewHolder {

        public TextView commentBy,commentTime;
        public TextView commentText;
        CircleImageView commenterImage;


        public GalleryViewHolder(View itemView) {
            super(itemView);

            commentBy = (TextView)itemView.findViewById(R.id.commentBy);
            commentText= (TextView) itemView.findViewById(R.id.commentText);
            commentTime= (TextView)itemView.findViewById(R.id.commentTime);
            commenterImage = (CircleImageView)itemView.findViewById(R.id.commenterImage);
        }
    }


    public commentAdapter(Context context, List<CommentData> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        CommentData commentData = commentsList.get(position);
        holder.commentBy.setText(commentData.getUSERNAME());
        holder.commentText.setText(commentData.getCOMMENTTEXT());
        DateConverter mydate = new DateConverter();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(commentData.getCOMMENTDATETIME());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        long seconds=TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days= TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

        if(seconds<60)
        {
            System.out.println(seconds+" seconds ago");
            holder.commentTime.setText(seconds+" sec ago");
        }
        else if(minutes<60)
        {
            System.out.println(minutes+" minutes ago");
            holder.commentTime.setText(minutes+" min ago");

        }
        else if(hours<24)
        {
            System.out.println(hours+" hours ago");
            holder.commentTime.setText(hours+" hours ago");
        }
        else
        {
            System.out.println(days+" days ago");
            holder.commentTime.setText(days+" days ago");
        }
       // holder.commentTime.setText(mydate.convertDate(commentData.getCOMMENTDATETIME()));
        String commenterImage = Appconstant.weburl+commentData.getUSERIMAGE();
        if (commenterImage.equals("")){
            Glide.with(context).load(R.drawable.ic_account_circle_black_36dp).into(holder.commenterImage);
        }else {
            Glide.with(context).load(commenterImage).into(holder.commenterImage);
        }

    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}
