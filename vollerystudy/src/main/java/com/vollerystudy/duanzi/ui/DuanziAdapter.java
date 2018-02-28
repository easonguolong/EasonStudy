package com.vollerystudy.duanzi.ui;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vollerystudy.R;
import com.vollerystudy.duanzi.bean.DuanziBean;
import com.vollerystudy.duanzi.utils.ItemClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/7/1.
 */

public class DuanziAdapter extends RecyclerView.Adapter<DuanziAdapter.DuanziViewHolder> {

    private Fragment mFragment;
    private List<DuanziBean> mDuanziBeanList;
    private ItemClickListener itemClickListener;

    public DuanziAdapter(Fragment mFragment, List<DuanziBean> mDuanziBeanList) {
        this.mFragment = mFragment;
        this.mDuanziBeanList = mDuanziBeanList;
    }

    @Override
    public DuanziViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duanzi,null);
        return new DuanziViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(DuanziViewHolder holder, int position) {
        try{
            DuanziBean duanziBean = mDuanziBeanList.get(position);
            Glide.with(mFragment).load(duanziBean.getGroupBean().getUser().getAvatar_url()).into(holder.mCivAvatar);
            holder.mTvContent.setText(duanziBean.getGroupBean().getText());
            holder.mTvAuthor.setText(duanziBean.getGroupBean().getUser().getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDuanziBeanList.size();
    }

    public static class DuanziViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView mCivAvatar;
        private TextView mTvAuthor;
        private TextView mTvContent;
        private ItemClickListener itemClickListener;
        public DuanziViewHolder(View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            mCivAvatar = (CircleImageView) itemView.findViewById(R.id.duanzi_civ_avatar);
            mTvAuthor = (TextView) itemView.findViewById(R.id.duanzi_tv_author);
            mTvContent = (TextView) itemView.findViewById(R.id.duanzi_tv_content);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (itemClickListener!=null){
                itemClickListener.OnItemClick(view,getPosition());
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}
