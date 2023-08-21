package com.bumbumapps.hue;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdsLoader {
    public static RewardedAd rewardedVideoAd ;

    public static void loadAds(Context mContext) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(mContext, "ca-app-pub-8444865753152507/3060392547",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedVideoAd = null;
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedVideoAd = ad;
                    }
                });
    }
}
