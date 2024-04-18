package com.usacamp.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.LevelItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class AdapterGridSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;

    private List<LevelItem> items = new ArrayList<>();

    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, LevelItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterGridSectioned(Context context, List<LevelItem> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageLock;
        public LinearLayout exply, camply,camply1;
        public TextView camptxtCn, camptxtEn, levelstatustxt;
        public OriginalViewHolder(View v) {
            super(v);
            imageLock = (ImageView) v.findViewById(R.id.image_level_lock);
            camptxtCn = (TextView) v.findViewById(R.id.text_level_id1);
            camptxtEn = (TextView) v.findViewById(R.id.text_level_id);
            levelstatustxt = (TextView) v.findViewById(R.id.text_level_status1);
            exply = (LinearLayout) v.findViewById(R.id.explyt);
            camply = (LinearLayout) v.findViewById(R.id.lyt_study_level);
            camply1 = (LinearLayout) v.findViewById(R.id.lyt_study_level1);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView title_section;

        public SectionViewHolder(View v) {
            super(v);
            title_section = (TextView) v.findViewById(R.id.title_section);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_study_level_item, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final LevelItem s = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.camptxtEn.setText("CAMP " + s.mnId);
            view.camptxtCn.setText("级别 " + s.mnId);
            switch (s.mnStatus){
                case Constants.LEVEL_UNAVAILABLE:
                    view.camply.setBackgroundResource(R.drawable.gridbutton);
                    view.camply1.setBackgroundColor(Color.TRANSPARENT);
                    view.imageLock.setVisibility(View.VISIBLE);
                    view.imageLock.setBackgroundColor(Color.TRANSPARENT);
                    view.exply.setVisibility(View.INVISIBLE);

                    break;
                case Constants.LEVEL_ALREADY_PURCHASE:

                    view.imageLock.setVisibility(View.INVISIBLE);
                    view.levelstatustxt.setBackgroundResource(R.drawable.blue_complete_radius);
                    view.levelstatustxt.setText("待\n学\n习");
                    view.levelstatustxt.setTextColor(ctx.getResources().getColor(R.color.white));
                    view.exply.setVisibility(View.VISIBLE);

                    break;
                case Constants.LEVEL_ALREADY_FINISH:

                    view.imageLock.setVisibility(View.INVISIBLE);
                    view.levelstatustxt.setBackgroundResource(R.drawable.gray_complete_radius);
                    view.levelstatustxt.setTextColor(ctx.getResources().getColor(R.color.white));
                    view.levelstatustxt.setText("可\n复\n习");
                    view.exply.setVisibility(View.VISIBLE);

                    break;
                case Constants.LEVEL_STUDYING:

                    view.imageLock.setVisibility(View.INVISIBLE);
                    view.levelstatustxt.setBackgroundResource(R.drawable.green_complete_radius);
                    view.levelstatustxt.setTextColor(ctx.getResources().getColor(R.color.white));
                    view.levelstatustxt.setText("学\n习\n中");
                    view.exply.setVisibility(View.VISIBLE);
                    break;
                case Constants.LEVEL_EXPERIENCE:

                    view.imageLock.setVisibility(View.INVISIBLE);
                    view.levelstatustxt.setBackgroundResource(R.drawable.dark_yellow_radius);
                    view.levelstatustxt.setText("体\n验\n中");
                    view.exply.setVisibility(View.VISIBLE);

                    break;
                default:
                    view.camply.setBackgroundResource(R.drawable.gridbutton);
                    view.camply1.setBackgroundColor(Color.TRANSPARENT);
                    view.imageLock.setVisibility(View.VISIBLE);
                    view.imageLock.setBackgroundColor(Color.TRANSPARENT);
                    view.exply.setVisibility(View.INVISIBLE);


                    break;
            }
            view.camply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, s, position);
                    }
                }
            });
        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            view.title_section.setText(s.groupTitle);
        }

        if (s.isSection) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).isSection? VIEW_SECTION : VIEW_ITEM;
    }
}