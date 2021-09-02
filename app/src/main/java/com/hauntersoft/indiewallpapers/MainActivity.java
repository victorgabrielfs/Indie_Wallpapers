package com.hauntersoft.indiewallpapers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import hotchemi.android.rate.AppRate;
import com.hauntersoft.indiewallpapers.Utils.AdMobService;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private ArrayList<String> list;
    private WallpaperAdapter adapter;

    private AdMobService adMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference().child("indie_wallpapers_images");
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        adMob = new AdMobService(MainActivity.this,
                findViewById(R.id.adView));
        //loadBannerAd();
        adMob.loadBannerAd();
        getData();

        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        adMob.createPersonalizedAd();
    }
    @Override
    protected void onResume() {
        super.onResume();
        adMob.showAd(3);
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();

                for(DataSnapshot shot : snapshot.getChildren()){
                    String data = shot.getValue().toString();
                    list.add(data);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2 ));



                adapter = new WallpaperAdapter(list, MainActivity.this, MainActivity.this, adMob);




                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: "+error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*private void loadBannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void createPersonalizedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }

    private void createInterstitialAd(AdRequest adRequest){
        InterstitialAd.load(MainActivity.this,"ca-app-pub-3940256099942544/1033173712", adRequest,
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

    private void showAd(){
        Random rand = new Random();
        int i = rand.nextInt(3);
        Log.i("---AdMob-Resume: ",Integer.toString(i));
        if (i == 1) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("---AdMob-Resume", "The interstitial ad wasn't ready yet.");
            }
        }
    }
    */
}