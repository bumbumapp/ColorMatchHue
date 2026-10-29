package com.bumbumapps.hue;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by sandra on 14.09.17.
 */

public class LevelAdapter extends BaseAdapter {

    // MARK: VARS
    private Context mContext;
    // Color for the text
    private int hexColor;
    private Activity mActivity;

    // FIXME: For the moment this is simply a string.
    private LevelStrategy[] levelStrategies;


    // MARK: CONSTRUCTOR
    public LevelAdapter(Context c, int hexColor, Activity mActivity,
                        LevelStrategy[] levelStrategies) {
        this.mContext = c;
        this.hexColor = hexColor;
        this.mActivity = mActivity;
        this.levelStrategies = levelStrategies;
    }


    // MARK: OVERRIDE METHODS
    @Override
    public int getCount() {
        return levelStrategies.length;
    }

    @Override
    public Object getItem(int i) {
        return levelStrategies[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // Bento level tile: the level's four corner colours as a swatch, and its number.
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_level, viewGroup, false);
        }
        LevelStrategy levelStrategy = levelStrategies[position];
        int[] ids = {R.id.c1, R.id.c2, R.id.c3, R.id.c4};
        for (int i = 0; i < ids.length; i++) {
            int color = Color.GRAY;
            if (levelStrategy.hexCodes != null && i < levelStrategy.hexCodes.length) {
                try {
                    color = Color.parseColor(levelStrategy.hexCodes[i]);
                } catch (IllegalArgumentException ignored) {
                }
            }
            view.findViewById(ids[i]).setBackgroundColor(color);
        }
        ((TextView) view.findViewById(R.id.level_number))
                .setText(mContext.getString(R.string.level_number, position + 1));
        view.setOnClickListener(new OnClickCreateLevelListener(mActivity, GameActivity.class, levelStrategy));
        return view;
    }
}


