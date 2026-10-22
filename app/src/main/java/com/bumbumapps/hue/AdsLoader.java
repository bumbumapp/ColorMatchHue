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
        if (rewardedVideoAd != null || !AdsConsent.canRequestAds(mContext)) return;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(mContext.getApplicationContext(), mContext.getString(R.string.admob_rewarded_id),
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
