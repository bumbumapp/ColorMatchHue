package com.bumbumapps.hue;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

// Main  Menu Activity which holds connections to the game menu, about, and how to.
public class MainMenuActivity extends AppCompatActivity {
    PreferenceCoin preferenceCoin;
    // MARK: ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        }
        // Set the layout for the UI elements.
        preferenceCoin=new PreferenceCoin(this);
        TextView coinsValue=findViewById(R.id.coins_value);
        TextView privacy = findViewById(R.id.main_menu_privacy);
        privacy.setOnClickListener(v -> AdsConsent.showPrivacyOptions(this));
        AdsConsent.gather(this, () -> {
            privacy.setVisibility(AdsConsent.isPrivacyOptionsRequired(this) ? View.VISIBLE : View.GONE);
            AdsLoader.loadAds(this);
            InterstitialHelper.preload(this);
        });
        if (preferenceCoin.getInteger("coin") != 0)
            coinsValue.setText(String.valueOf(preferenceCoin.getInteger("coin")));
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        ((TextView) findViewById(R.id.greeting)).setText(hour < 12 ? R.string.greeting_morning
                : hour < 18 ? R.string.greeting_afternoon : R.string.greeting_evening);

        // Hero opens the level pager on Easy; the three tiles open it on their difficulty.
        findViewById(R.id.main_menu_start_game).setOnClickListener(v -> openLevels(0));
        setUpLevelTile(R.id.tile_easy, R.string.level_easy, R.string.level_easy_sub, R.drawable.swatch_easy, 0);
        setUpLevelTile(R.id.tile_medium, R.string.level_intermediate, R.string.level_intermediate_sub, R.drawable.swatch_medium, 1);
        setUpLevelTile(R.id.tile_expert, R.string.level_expert, R.string.level_expert_sub, R.drawable.swatch_expert, 2);
        addHowToListener(findViewById(R.id.main_menu_how_to));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.coins_value)).setText(String.valueOf(preferenceCoin.getInteger("coin")));
    }

    private void setUpLevelTile(int tileId, int name, int sub, int swatch, int page) {
        View tile = findViewById(tileId);
        ((TextView) tile.findViewById(R.id.level_name)).setText(name);
        // Only the level count ("6 levels") fits under the small tile.
        String count = getString(sub);
        int dot = count.indexOf('·');
        ((TextView) tile.findViewById(R.id.level_count)).setText(dot > 0 ? count.substring(0, dot).trim() : count);
        tile.findViewById(R.id.level_swatch).setBackgroundResource(swatch);
        tile.setOnClickListener(v -> openLevels(page));
    }

    private void openLevels(int page) {
        Intent intent = new Intent(this, MenuCollectionActivity.class);
        intent.putExtra(MenuCollectionActivity.EXTRA_PAGE, page);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    // MARK: HELPER METHODS
    // Listener for the about button.


    // Listener for the howto button.
    private void addHowToListener(View how_to) {
        how_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HowToActivity.class);
                MainMenuActivity.this.startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


}
