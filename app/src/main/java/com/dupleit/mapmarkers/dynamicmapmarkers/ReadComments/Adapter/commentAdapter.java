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
import java.util.List;
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
        holder.commentTime.setText(mydate.convertDate(commentData.getCOMMENTDATETIME()));
        String commenterImage = Appconstant.weburl+commentData.getUSERIMAGE();
        if (commenterImage.equals("")){
            Glide.with(context).load(R.drawable.ic_account_circle_black_36dp).into(holder.commenterImage);
        }else {
            Glide.with(context).load(commenterImage).into(holder.commenterImage);
        }

    }
    /*private String getTimeStamp(String messageTime) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(Long.parseLong(messageTime));

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "d/MM/yy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1 ){
            return "YESTERDAY";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        }
    }*/

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}
