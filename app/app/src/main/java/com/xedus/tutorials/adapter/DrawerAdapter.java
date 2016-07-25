package com.xedus.tutorials.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.xedus.tutorials.R;
import com.xedus.tutorials.api.models.Categories;

import com.xedus.tutorials.api.models.callback.OnClickListener;


import java.util.ArrayList;

import java.util.List;


public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Categories> categories;
    OnClickListener mListener;
    public DrawerAdapter(List<Categories> mCategory) {
       if(mCategory==null)
           categories = new ArrayList<>();
        else
           categories = mCategory;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType<0)
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_header_main,parent,false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_item,parent,false);
        return new DataViewHolder(view);
    }

    public void setOnClickListener(OnClickListener mListener){
        this.mListener = mListener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0)
            return;
         Categories mPost = categories.get(position-1);
        ((DataViewHolder)holder).category.setText(mPost.name);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return -1;
            return 1;
    }
    public String getCategory(int id){
        for(Categories cat : categories){
            if(cat.id==id)
                return cat.name;
        }
        return "Uncategorized";
    }
    @Override
    public int getItemCount() {
        return categories.size()+1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView category;

        public DataViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.drawer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null)
                mListener.onClickListener(view,getAdapterPosition());
        }
    }

}
