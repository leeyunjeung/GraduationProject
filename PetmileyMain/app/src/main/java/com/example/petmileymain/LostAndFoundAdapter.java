package com.example.petmileymain;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.CustomViewHolder>{

    private ArrayList<LostAndFoundData> mList = null;
    private Activity context = null;


    public LostAndFoundAdapter(Activity context, ArrayList<LostAndFoundData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView m_f;
        protected TextView missing_date;
        protected TextView place;
        protected RoundedImageView imgview;


        public CustomViewHolder(View view) {
            super(view);

            this.m_f = (TextView) view.findViewById(R.id.textView_list_m_f);
            this.missing_date = (TextView) view.findViewById(R.id.textView_list_missingdate);
            this.place = (TextView) view.findViewById(R.id.textView_list_place);
            this.imgview = (RoundedImageView) view.findViewById(R.id.imgview);


        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lostlist, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        Bitmap tmp;


        viewholder.m_f.setText(mList.get(position).getM_f());
        viewholder.missing_date.setText(mList.get(position).getMissing_date());
        viewholder.place.setText(mList.get(position).getPlace());
        viewholder.imgview.setImageBitmap(mList.get(position).getPicture());

    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            encodedString = encodedString.replace(" ","+");
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;

        } catch (Exception e) {
            Log.e("exception",e.getMessage());
            //e.getMessage();
            return null;

        }

    }
}
