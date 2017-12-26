package com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;

import java.util.List;

/**
 * Created by mandeep on 5/8/17.
 */

public class personAdapter extends RecyclerView.Adapter<personAdapter.MyViewHolder> {

    private Context mContext;
    private List<Datum> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView personImage;
public TextView personName;
        public MyViewHolder(View view) {
            super(view);
            cardView = itemView.findViewById(R.id.card_view);
            personImage = view.findViewById(R.id.person_image);
            personName= view.findViewById(R.id.person_name);


        }
    }
    public personAdapter(Context mContext, List<Datum> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_persons, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Datum person = albumList.get(position);
        Glide.with(mContext).load(Appconstant.weburl+person.getPOSTIMAGEURL()).into(holder.personImage);
        if (!person.getUSERNAME().equals("")){
            holder.personName.setText(person.getUSERNAME());
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
