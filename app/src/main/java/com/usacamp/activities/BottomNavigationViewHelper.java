package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.usacamp.R;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            menuView.clearAnimation();
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShifting(false);

                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());

            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}

///** @hide */
//@RestrictTo(LIBRARY_GROUP)
//
//class BottomNavigationCampView extends BottomNavigationItemView {
//
//    @SuppressLint("RestrictedApi")
//    public BottomNavigationCampView(@NonNull Context context) {
//
//        super(context);
//    }
//
//    @Override
//    public  void setChecked(boolean checked) {
//        mLargeLabel.setPivotX(mLargeLabel.getWidth() / 2);
//        mLargeLabel.setPivotY(mLargeLabel.getBaseline());
//        mSmallLabel.setPivotX(mSmallLabel.getWidth() / 2);
//        mSmallLabel.setPivotY(mSmallLabel.getBaseline());
//        if (mShiftingMode) {
//
//            if (checked) {
//                LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();
//                iconParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                iconParams.topMargin = mDefaultMargin;
//                mIcon.setLayoutParams(iconParams);
//                mLargeLabel.setVisibility(VISIBLE);
//                mLargeLabel.setScaleX(1f);
//                mLargeLabel.setScaleY(1f);
//            } else {
//                LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();
//                iconParams.gravity = Gravity.CENTER;
//                iconParams.topMargin = mDefaultMargin;
//                mIcon.setLayoutParams(iconParams);
//                mLargeLabel.setVisibility(INVISIBLE);
//                mLargeLabel.setScaleX(0.5f);
//                mLargeLabel.setScaleY(0.5f);
//            }
//            mSmallLabel.setVisibility(INVISIBLE);
//        } else {
//            if (checked) {
//                LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();
//                iconParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                iconParams.topMargin = mDefaultMargin + mShiftAmount;
//                mIcon.setLayoutParams(iconParams);
//                mLargeLabel.setVisibility(VISIBLE);
//                mSmallLabel.setVisibility(INVISIBLE);
//
//                mLargeLabel.setScaleX(1f);
//                mLargeLabel.setScaleY(1f);
//                mSmallLabel.setScaleX(mScaleUpFactor);
//                mSmallLabel.setScaleY(mScaleUpFactor);
//            } else {
//                LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();
//                iconParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//                iconParams.topMargin = mDefaultMargin;
//                mIcon.setLayoutParams(iconParams);
//                mLargeLabel.setVisibility(INVISIBLE);
//                mSmallLabel.setVisibility(VISIBLE);
//
//                mLargeLabel.setScaleX(mScaleDownFactor);
//                mLargeLabel.setScaleY(mScaleDownFactor);
//                mSmallLabel.setScaleX(1f);
//                mSmallLabel.setScaleY(1f);
//            }
//        }
//
//        super.refreshDrawableState();
//    }
//
//
//}