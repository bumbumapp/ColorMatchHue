package com.bumbumapps.hue;

import static com.bumbumapps.hue.AdsLoader.rewardedVideoAd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


/**
 * Created by sandra on 08.09.17.
 */

// Adapter for the ColorTile array for each level of the game.
public class TileAdapter extends BaseAdapter {

    // MARK: VARS

    private Context mContext;
    private ColorTile[] colorTiles;
    PreferenceCoin preferenceCoin;
    int k;
    int posOld;
    int posNew;
    Color changingColor;

    // Height of each cell. (Since GridViews are adjusted dynamically without my control)
    private int height;


    // MARK: CONSTRUCTOR
    public TileAdapter(Context c, ColorTile[] colorTiles, int height) {
        this.mContext = c;
        this.colorTiles = colorTiles;
        this.height = height;
    }


    // MARK: OVERRIDE METHODS
    @Override
    public int getCount() {
        return colorTiles.length;
    }

    @Override
    public Object getItem(int i) {
        return colorTiles[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        TextView tileView = new TextView(mContext);

        // Set the maximum height for all tiles
        setMaxHeight(viewGroup, tileView);

        // In case there are not enough tiles for a level, just fill it default with black.
        if (colorTiles[position] == null) {
            setViewForTile(new Color(), tileView, null);
            return tileView;
        }

        ColorTile curTile = colorTiles[position];
        // Remember, we're only displaying the current color, not the real color
        // The real color is being used to check if the tile is in its correct
        // place.

        setViewForTile(curTile.getCurColor(), tileView, curTile);
        return tileView;
    }

    // MARK: Helper Methods
    private void setViewForTile(Color color, TextView tileView, ColorTile curTile) {

        // FIXME: Make Hint Tiles more prominent, e.g. with circle on them or smth.

        if (curTile.isHint() && curTile != null) {
            tileView.setText("\u25A0");
            tileView.setTextColor(Color.BLACK);
            tileView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tileView.setGravity(Gravity.CENTER);
        }

        tileView.setBackgroundColor(Color.rgb(color.red(), color.green(), color.blue()));
//        MobileAds.initialize(mContext,"ca-app-pub-2158389106066570~1508854721");

    }

    // Swap method for changing two tile's current colors.
    public void swap(final int oldPos, int newPos) {

        final Color temp = colorTiles[oldPos].getCurColor();
        preferenceCoin=new PreferenceCoin(mContext);
        posNew=newPos;
        posOld=newPos;
        changingColor=temp;
        for (int i = 0; i < colorTiles.length; i++) {
            if (colorTiles[i].getCurColor() == colorTiles[oldPos].getRealColor()) {
                 k=i;
            }
        }


         if (oldPos==newPos){
             final Dialog dialog = new Dialog(mContext);
             dialog.setCancelable(false);
             dialog.setContentView(R.layout.coin_diolog);
             dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
             Button spend=dialog.findViewById(R.id.spend_btn);
             TextView cancel=dialog.findViewById(R.id.cancel);
             final Button rewardvideo=dialog.findViewById(R.id.reward_btn);
             rewardvideo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (rewardedVideoAd != null) {
                         rewardedVideoAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                             @Override
                             public void onAdDismissedFullScreenContent() {
                                 AdsLoader.loadAds(mContext);
                             }
                         });
                         rewardedVideoAd.show((Activity) mContext, new OnUserEarnedRewardListener() {
                             @Override
                             public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                 colorTiles[posOld].setCurColor(colorTiles[posNew].getRealColor());
                                 colorTiles[k].setCurColor(changingColor);
                                 notifyDataSetChanged();
                                 dialog.dismiss();
                             }
                         });
                     }

                 }
             });
             cancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     dialog.dismiss();
                 }
             });
             spend.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (preferenceCoin.getInteger("coin")>=5){
                         colorTiles[oldPos].setCurColor(colorTiles[oldPos].getRealColor());
                         colorTiles[k].setCurColor(temp);
                         preferenceCoin.putInteger("coin",preferenceCoin.getInteger("coin")-5);
                         notifyDataSetChanged();
                     }
                     else{
                         Toast.makeText(mContext,"Not enough coin",Toast.LENGTH_LONG).show();
                     }
                     dialog.dismiss();


                 }
             });

             dialog.show();
         }
         else{
             colorTiles[oldPos].setCurColor(colorTiles[newPos].getCurColor());
             colorTiles[newPos].setCurColor(temp);
         }


        this.notifyDataSetChanged();

    }






    // Checks if the puzzle is solved yet.
    public boolean isPuzzleSolved() {

        for (int i = 0; i < colorTiles.length; i++) {
            if (!colorTiles[i].isSolved()) {
                return false;
            }
        }
        return true;
    }

    // Set the maximal height for a color tile.
    private void setMaxHeight(ViewGroup viewGroup, TextView tileView) {
        int maxHeight = colorTiles.length / height;
        tileView.setHeight(viewGroup.getHeight() / maxHeight);
    }




    
}


