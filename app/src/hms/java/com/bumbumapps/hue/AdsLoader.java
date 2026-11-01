package com.bumbumapps.hue;

import android.app.Activity;
import android.content.Context;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;

/** Huawei Ads twin of the Play build's rewarded-ad loader (same API). */
public class AdsLoader {
    private static RewardAd rewardedVideoAd;
    private static boolean loading;

    public static void loadAds(Context mContext) {
        if (rewardedVideoAd != null || loading || !AdsConsent.canRequestAds(mContext)) return;
        Context app = mContext.getApplicationContext();
        final RewardAd ad = new RewardAd(app,
                AdsConsent.slot(app, R.string.hms_rewarded_id, R.string.hms_test_rewarded_id));
        loading = true;
        ad.loadAd(new AdParam.Builder().build(), new RewardAdLoadListener() {
            @Override
            public void onRewardedLoaded() {
                loading = false;
                rewardedVideoAd = ad;
            }

            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
                loading = false;
                rewardedVideoAd = null;
            }
        });
    }

    public static boolean isRewardedReady() {
        return rewardedVideoAd != null && rewardedVideoAd.isLoaded();
    }

    /**
     * Shows the loaded rewarded ad: {@code onReward} runs when the user earns the reward,
     * {@code onDismissed} after the ad is closed (a new one is loaded first). No-op if none is loaded.
     */
    public static void showRewarded(Activity activity, Runnable onReward, Runnable onDismissed) {
        if (!isRewardedReady()) return;
        final RewardAd shownAd = rewardedVideoAd;
        rewardedVideoAd = null;
        shownAd.show(activity, new RewardAdStatusListener() {
            @Override
            public void onRewardAdClosed() {
                loadAds(activity);
                onDismissed.run();
            }

            @Override
            public void onRewarded(Reward reward) {
                onReward.run();
            }

            @Override
            public void onRewardAdFailedToShow(int errorCode) {
                loadAds(activity);
            }
        });
    }
}
