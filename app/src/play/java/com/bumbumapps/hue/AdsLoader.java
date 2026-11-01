package com.bumbumapps.hue;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
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

    public static boolean isRewardedReady() {
        return rewardedVideoAd != null;
    }

    /**
     * Shows the loaded rewarded ad: {@code onReward} runs when the user earns the reward,
     * {@code onDismissed} after the ad is closed (a new one is loaded first). No-op if none is loaded.
     */
    public static void showRewarded(Activity activity, Runnable onReward, Runnable onDismissed) {
        if (rewardedVideoAd == null) return;
        final RewardedAd shownAd = rewardedVideoAd;
        rewardedVideoAd = null;
        shownAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                loadAds(activity);
                onDismissed.run();
            }
        });
        shownAd.show(activity, rewardItem -> onReward.run());
    }
}
