package com.usacamp.utils;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.usacamp.R;
import com.usacamp.model.ShareTypeItem;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ShareCustomSliderAdapter extends PagerAdapter {
    Activity act;
    private List<ShareTypeItem> items;
    private final String TAG = "ShareCustomSliderAdapter";
    private ShareCustomSliderAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ShareTypeItem obj);
    }

    public void setOnItemClickListener(ShareCustomSliderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // constructor
    public ShareCustomSliderAdapter(Activity activity, List<ShareTypeItem> items) {
        act = activity;
        this.items = items;

    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    public ShareTypeItem getItem(int pos) {
        return items.get(pos);
    }

    public void setItems(List<ShareTypeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    @Override public float getPageWidth(int position) { return(0.2f); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ShareTypeItem o = items.get(position);
        LayoutInflater inflater = (LayoutInflater)act.getLayoutInflater();
        RelativeLayout v = (RelativeLayout)inflater.inflate(R.layout.sharecustomslider, null);

        ImageView image = (ImageView) v.findViewById(R.id.sharecustomimage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {

                    onItemClickListener.onItemClick(v, o);
                }
            }
        });
        image.setImageDrawable(o.mImg);
        if(o.idx == 0)
            image.setBackground(act.getDrawable(R.drawable.sharecustomitembackground));
        ((ViewPager) container).addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
