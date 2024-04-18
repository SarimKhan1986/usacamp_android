package com.usacamp.model;

import androidx.cardview.widget.CardView;

public interface CardAdapter {

    float MAX_ELEVATION_FACTOR = 0.1f;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
