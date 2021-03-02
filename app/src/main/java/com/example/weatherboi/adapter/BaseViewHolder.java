package com.example.weatherboi.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> views;

    private BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.mOnItemClickListener = onItemClickListener;
        this.views = new SparseArray<View>();
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public TextView getTextView(int viewid) {
        return retrieveView(viewid);
    }


    public RecyclerView getRecyclerView (int viewid) {
        return retrieveView(viewid);
    }

    public Button getButton(int viewid) {
        return retrieveView(viewid);
    }

    public ImageView getImageView(int viewid) {
        return retrieveView(viewid);
    }

    public RatingBar getRatingBar(int viewid) {
        return retrieveView(viewid);
    }

    public SimpleDraweeView getSimpleDraweeView(int viewid) {
        return retrieveView(viewid);
    }

    public ConstraintLayout getConstraintLayout(int viewid) {
        return retrieveView(viewid);
    }

    public LinearLayout getLinearLayout(int viewid) {
        return retrieveView(viewid);
    }

    public View getView(int viewid) {
        return retrieveView(viewid);
    }


    private <T extends View> T retrieveView(int viewid) {

        View view = views.get(viewid);
        if (view == null) {
            view = itemView.findViewById(viewid);
            views.put(viewid, view);
        }

        return (T) view;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v, getLayoutPosition());
        }
    }
}
