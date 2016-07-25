package com.xedus.tutorials.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xedus.tutorials.R;
import com.xedus.tutorials.api.models.Categories;
import com.xedus.tutorials.api.models.Posts;
import com.xedus.tutorials.api.models.callback.OnButtonClickListener;
import com.xedus.tutorials.api.models.callback.OnClickListener;
import com.xedus.tutorials.utils.DateFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Posts> mPostsList;
    HashMap<Integer,String> mCat;
    OnClickListener mListener;
    OnButtonClickListener mButtonListener;
    public static final int VH_ITEM= 0;
    public static final int VH_FOOTER = 1;
    private boolean isFinished=false,isCard = false;
    public MainAdapter(List<Posts> mPosts) {
       if(mPosts==null)
           mPostsList = new ArrayList<>();
        else
        mPostsList = new ArrayList<>(mPosts);
    }
    public void add(Posts mPost){
        mPostsList.add(mPost);
        notifyItemInserted(mPostsList.size()-1);
    }
    public void remove(int position){
        mPostsList.remove(position);
        notifyItemRemoved(position);
    }

    public void isFinished(boolean state){
        isFinished=state;
        notifyItemChanged(mPostsList.size());
    }
    public void merge(List<Posts> mPosts){
        mPostsList.addAll(mPosts);
        notifyItemRangeChanged(mPostsList.size()-mPosts.size(),mPosts.size());
    }
    public void setCateogries(List<Categories> mCategories){
       if(mCategories==null)
           return;
        mCat = new HashMap<>();
        for(Categories cat : mCategories){
            mCat.put(cat.id,cat.name);
        }
    }
    public void setOnButtonClickListener(OnButtonClickListener mButtonListener){
        this.mButtonListener = mButtonListener;
    }
    @Override
    public long getItemId(int position){
        if(position<0 || position>=mPostsList.size())
            return 0;
        return mPostsList.get(position).id;
    }
    @Override
    public int getItemViewType(int position) {
        if(position==mPostsList.size())
            return VH_FOOTER;
        return VH_ITEM;
    }
    public void useCard(boolean state){
        isCard=state;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VH_ITEM) {
           if(isCard)
               return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_grid, parent, false));
            return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_card, parent, false));
        }
        return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_footer,parent,false));
    }
    public Posts get(int i){
        return mPostsList.get(i);
    }
    public void setOnClickListener(OnClickListener mListener){
        this.mListener = mListener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if(getItemViewType(position)==VH_FOOTER){
           FooterViewHolder mHolder = (FooterViewHolder) holder;
           if(mPostsList.size()==0)
               isFinished=true;
           if(isFinished) {
               mHolder.mTextView.setVisibility(View.VISIBLE);
               mHolder.mProgressBar.setVisibility(View.GONE);
           }
           else {
               mHolder.mProgressBar.setVisibility(View.VISIBLE);
               mHolder.mTextView.setVisibility(View.GONE);
           }
       }
        else {
           DataViewHolder mHolder= (DataViewHolder) holder;
           Posts mPost = mPostsList.get(position);
           mHolder.post_heading.setText(mPost.title.rendered);
           mHolder.post_date.setText(DateFormatter.format(mPost.date));
           if(!isCard) {
               String category = "uncategorized";
               if (mCat != null && mCat.containsKey(mPost.categories[0])) {
                   category = mCat.get(mPost.categories[0]);
               }
               mHolder.post_category.setText(category);
           }
           ImageLoader.getInstance().displayImage(mPost.featured_media_url, mHolder.post_image, new DisplayImageOptions.Builder()
                   .cacheOnDisk(true)
                   .imageScaleType(ImageScaleType.EXACTLY)
                   .displayer(new FadeInBitmapDisplayer(300))
                   .build());
       }
    }

    @Override
    public int getItemCount() {
        return mPostsList.size()+1;
    }
    public class FooterViewHolder extends RecyclerView.ViewHolder{
       ProgressBar mProgressBar;
       TextView mTextView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.moreprogressBar);
            mTextView = (TextView) itemView.findViewById(R.id.no_posts);
        }
    }
    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        ImageView post_image;
        TextView post_heading,post_date,post_category;

        public DataViewHolder(View itemView) {
            super(itemView);
            post_image = (ImageView) itemView.findViewById(R.id.post_image);
            post_heading = (TextView) itemView.findViewById(R.id.post_heading);
            post_date = (TextView) itemView.findViewById(R.id.post_date);
            post_category = (TextView) itemView.findViewById(R.id.post_category);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            if(isCard && mButtonListener!=null){
                itemView.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mButtonListener.onButtonClick(view,getAdapterPosition());
                    }
                });
            }
        }

        @Override
        public boolean onLongClick(View view){
            if(mListener!=null) {
                mListener.onLongClickListener(view, getAdapterPosition());
                return true;
            }
            return false;
        }
        @Override
        public void onClick(View view) {
            if(mListener != null)
                mListener.onClickListener(view,getAdapterPosition());
        }
    }
}
