package com.hauntersoft.indiewallpapers.Utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

public class AdMobService {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    private Activity activity;

    public AdMobService( Activity activity, View view){

        this.activity = activity;
        this.mAdView = (AdView) view;
    }

    public void loadBannerAd(){
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void createPersonalizedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }

    private void createInterstitialAd(AdRequest adRequest){
        InterstitialAd.load(activity,"ca-app-pub-8553235049340962/3812020527", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("---AdMob", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                mInterstitialAd = null;
                                Log.d("---AdMob", "The ad was dismissed.");
                                createPersonalizedAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("---AdMob", "The ad failed to show.");
                                createPersonalizedAd();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("---AdMob", "The ad was shown.");
                                createPersonalizedAd();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("---AdMob", loadAdError.getMessage());
                        mInterstitialAd = null;
                        createPersonalizedAd();
                    }
                }
        );


    }

    public void showAd(int n){
        Random rand = new Random();
        int i = rand.nextInt(n);
        Log.i("---AdMob-Resume: ",Integer.toString(i));
        if (i == 1) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                Log.d("---AdMob-Resume", "The interstitial ad wasn't ready yet.");
            }
        }
    }


}
