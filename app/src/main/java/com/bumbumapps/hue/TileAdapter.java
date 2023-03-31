package com.bumbumapps.hue;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by sandra on 08.09.17.
 */

// Adapter for the ColorTile array for each level of the game.
public class TileAdapter extends BaseAdapter implements RewardedVideoAdListener {

    // MARK: VARS
    RewardedVideoAd rewardedVideoAd;
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
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(mContext);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadAds();
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
             Button rewardvideo=dialog.findViewById(R.id.reward_btn);
             rewardvideo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (rewardedVideoAd.isLoaded())
                         rewardedVideoAd.show();
                     dialog.dismiss();
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



    private void loadAds() {
        rewardedVideoAd.loadAd("ca-app-pub-8444865753152507/3060392547",new AdRequest.Builder().build());
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

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
          loadAds();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        colorTiles[posOld].setCurColor(colorTiles[posNew].getRealColor());
        colorTiles[k].setCurColor(changingColor);
        notifyDataSetChanged();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
    
}


