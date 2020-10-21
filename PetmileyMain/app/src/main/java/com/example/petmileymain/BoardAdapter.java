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

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CustomViewHolder>{

    private static final String TAG = "adapter";
    private ArrayList<BoardData> mList = null;
    private Activity context = null;
    private String board = null;

    public BoardAdapter(Activity context,String board, ArrayList<BoardData> list) {
        this.context = context;
        this.mList = list;
        this.board = board;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView promote_local;
        protected TextView nickname;
        protected TextView note_title;
        protected TextView review_categorize;
        protected RoundedImageView imgview;


        public CustomViewHolder(View view) {
            super(view);

            this.nickname = (TextView) view.findViewById(R.id.textView_list_nickname);

            this.note_title = (TextView) view.findViewById(R.id.textView_list_note_title);
            this.imgview = (RoundedImageView) view.findViewById(R.id.imgview);
            this.promote_local = (TextView) view.findViewById(R.id.textView_list_local);
            this.review_categorize = (TextView) view.findViewById(R.id.textView_list_categorize);

            if(board.equals("promote")) { //수정 추가
                review_categorize.setVisibility(View.GONE);
            }
            else {
                promote_local.setVisibility(View.GONE);
            }
        }
    }


    public void clearItem() {
        mList.clear();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.nickname.setText(mList.get(position).getUser_nickname());

        viewholder.note_title.setText(mList.get(position).getPost_note_title());
        viewholder.imgview.setImageBitmap(mList.get(position).getPicture());

        //수정
        if(board.equals("promote")){
            viewholder.promote_local.setText(mList.get(position).getPromote_local());
        }
        else{
            viewholder.review_categorize.setText(mList.get(position).getReview_categorize());

        }
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
