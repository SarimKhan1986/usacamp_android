package com.usacamp.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.usacamp.R;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;
import com.usacamp.model.ImageObj;
import com.usacamp.model.ShareTypeItem;

import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShareSliderAdapter extends RecyclerView.Adapter<ShareSliderAdapter.ShareSliderHolder>{
    Activity act;
    private List<ShareTypeItem> items;
    private final String TAG = "ShareSlider";
    private ShareSliderAdapter.OnItemClickListener onItemClickListener;
    private final HashMap<Integer,View> shareLayoutList = new HashMap<>(5);
    private final HashMap<Integer,View> container = new HashMap<>(5);

    class ShareSliderHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final ConstraintLayout cardLayout;
        private final ConstraintLayout cardLayout1;
        private final TextView tvName;
        private final TextView tvCurrentProgress;
        private final TextView tvstaticstr;
        private final TextView cardBtn;
        private final TextView cardtext_1;
        private final TextView cardtext_1Name;
        private final TextView cardtext_1Progress;
        private final TextView cardtext_1day;
        private final TextView cardtext_1quizCount;
        private final TextView cardtext_1shareCount;
        private ImageView ImgQrCode ;
        CardView ClContainer;

        private final View mitemView;
        public ShareSliderHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shareimage);
            cardLayout = itemView.findViewById(R.id.cardly);
            tvName = itemView.findViewById(R.id.sharetext1);
            tvCurrentProgress = itemView.findViewById(R.id.sharetext2);
            cardLayout1 = itemView.findViewById(R.id.cardCL);
            cardBtn = itemView.findViewById(R.id.sharebutton);
            cardtext_1 = itemView.findViewById(R.id.textView40);
            //ImgQrCode = itemView.findViewById(R.id.shareqrcode);
            tvstaticstr = itemView.findViewById(R.id.sharetext3);
            cardtext_1Name = itemView.findViewById(R.id.textView42);
            cardtext_1Progress = itemView.findViewById(R.id.textView46);
            cardtext_1day = itemView.findViewById(R.id.textView28);
            cardtext_1quizCount = itemView.findViewById(R.id.textView35);
            cardtext_1shareCount = itemView.findViewById(R.id.textView38);
            mitemView = itemView;
            ClContainer = itemView.findViewById(R.id.relativeLayout7);
//            applyConstraintLayout();
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        void SetLayoutContent(ShareTypeItem item)
        {
            imageView.setImageDrawable(item.mImg);
            String cardtxt = "哥伦比亚大学专家团队研发\n" +
                    "限时0元体验·全部课程";

            Spannable spannable = new SpannableString(cardtxt);
            spannable.setSpan(new ForegroundColorSpan(item.cardcolor), 15, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(item.cardcolor), 20, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(item.cardtvtxt1);
            tvCurrentProgress.setText(spannable, TextView.BufferType.SPANNABLE);

            String cardtxt1 = "哥伦比亚大学专家团队研发·AI英语课程\n" +
                    "限时0元体验·让孩子轻松学会英语";

            Spannable spannable1 = new SpannableString(cardtxt1);
            spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB584E")), 13, 20 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB584E")), 22, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cardtext_1.setText(spannable1, TextView.BufferType.SPANNABLE);

            cardtext_1Name.setText(item.cardtvtxt1);
            cardtext_1Progress.setText(item.cardtvtxt2);

            cardtext_1day.setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnStudyDays));
            cardtext_1quizCount.setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnQuizCount));
            cardtext_1shareCount.setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnSharingCount));
//            if(MyApplication.getInstance().isTablet)//pad
//            {
//                tvCurrentProgress.setTextSize(12.0f);
//            } else {
//                tvCurrentProgress.setTextSize(12.0f);
//            }

            tvCurrentProgress.setTextColor(Color.BLACK);

            tvstaticstr.setVisibility(View.GONE);
            cardBtn.setTextColor(item.cardcolor);
            switch (item.idx)
            {
                case 0:
                    cardLayout1.setVisibility(View.VISIBLE);
                    cardLayout.setVisibility(View.GONE);
                    ImgQrCode = itemView.findViewById(R.id.qrcode1);
                    break;
                case 1:
                    cardLayout1.setVisibility(View.VISIBLE);
                    cardLayout.setVisibility(View.GONE);
                    ImgQrCode = itemView.findViewById(R.id.qrcode1);
                    break;
                case 2 :
                    cardLayout1.setVisibility(View.GONE);
                    cardLayout.setVisibility(View.VISIBLE);
                    ImgQrCode = itemView.findViewById(R.id.shareqrcode);
                    cardBtn.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_yello));
                    ImgQrCode.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_yello_qr));
                    break;
                case 3:
                    cardLayout1.setVisibility(View.GONE);
                    cardLayout.setVisibility(View.VISIBLE);
                    ImgQrCode = itemView.findViewById(R.id.shareqrcode);
                    cardBtn.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_green));
                    ImgQrCode.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_green_qr));
                    break;
                case 4:
                    cardLayout1.setVisibility(View.GONE);
                    cardLayout.setVisibility(View.VISIBLE);
                    ImgQrCode = itemView.findViewById(R.id.shareqrcode);
                    cardBtn.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_greenlight));
                    ImgQrCode.setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_greenlight_qr));
                    break;
            }
            applyQrCode(ImgQrCode);
//            applyConstraintLayout();
        }

    }


    private interface OnItemClickListener {
        void onItemClick(View view, ImageObj obj);
    }
    @NonNull
    @Override
    public ShareSliderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareSliderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shareslider,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ShareSliderHolder holder, int position) {
        holder.SetLayoutContent(items.get(position));
        Log.d("debug1", String.valueOf(position));
        shareLayoutList.put(position, holder.mitemView.findViewById(R.id.shareCL));
        container.put(position, holder.mitemView);
        Intent intent = new Intent();
        intent.setAction(Constants.SHARE_PAGE_RESIZE_MAG);
        MyApplication.getInstance().getCurrentActivity().sendBroadcast(intent);
    }

    public void setOnItemClickListener(ShareSliderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // constructor
    public ShareSliderAdapter(Activity activity, List<ShareTypeItem> items) {
        act = activity;
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    public View getItemView(int pos) {
        return shareLayoutList.get(pos);

    }

    public View getContainermView(int pos) {
        return container.get(pos);
    }
    public ShareTypeItem getItem(int pos) {
        return items.get(pos);
    }

    public void setItems(List<ShareTypeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    private void applyQrCode(ImageView view)
    {
        Bitmap qrCodeBitmap = null;

        String strUrl = MyApplication.getInstance().SERVER_URL + "Code/sharedCode?p=" + MyApplication.mNetProc.mLoginUserInf.mnbase64UserID + "&w=1";

        qrCodeBitmap = QRGenerater.QRGenerater(strUrl, 300, 300);

        view.setImageBitmap(qrCodeBitmap);
    }

}
