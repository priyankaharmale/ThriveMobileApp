package hnweb.com.thrivemobileapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hnweb.com.thrivemobileapp.R;

/**
 * Created by neha on 6/28/2017.
 */

public class ProgressUploadAdapter extends RecyclerView.Adapter<ProgressUploadAdapter.ProgressViewHoldet> {

    Activity activity;
    ArrayList<String> imagesList;

    public ProgressUploadAdapter(Activity activity, ArrayList<String> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;
    }


    @Override
    public ProgressUploadAdapter.ProgressViewHoldet onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(activity).inflate(R.layout.book_store_item, null);
        ProgressViewHoldet dlvh = new ProgressViewHoldet(layoutView);
        return dlvh;
    }

    @Override
    public void onBindViewHolder(final ProgressUploadAdapter.ProgressViewHoldet holder, final int position) {

        Glide.with(activity).load(imagesList.get(position).toString()).override(100, 100).into(holder.imageIV);

//        holder.checkLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.setCB.setVisibility(View.VISIBLE);
//            }
//        });

        holder.setCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesList.remove(imagesList.get(position).toString());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ProgressViewHoldet extends RecyclerView.ViewHolder {

        public ImageView imageIV;
        public ImageView setCB;
        public LinearLayout checkLL;


        public ProgressViewHoldet(View itemView) {
            super(itemView);

//            checkLL = (LinearLayout) itemView.findViewById(R.id.checkLL);
            imageIV = (ImageView) itemView.findViewById(R.id.bookIV);
            setCB = (ImageView) itemView.findViewById(R.id.setCB);
        }
    }
}
