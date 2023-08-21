package com.bumbumapps.hue;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

// Main  Menu Activity which holds connections to the game menu, about, and how to.
public class MainMenuActivity extends AppCompatActivity {
    PreferenceCoin preferenceCoin;
    // MARK: ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
         FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        // Set the layout for the UI elements.
        preferenceCoin=new PreferenceCoin(this);
        TextView coinsValue=findViewById(R.id.coins_value);
        AdsLoader.loadAds(this);
        if(preferenceCoin.getInteger("coin")!=0)
        coinsValue.setText(String.valueOf(preferenceCoin.getInteger("coin")));
        Button start_game = (Button) findViewById(R.id.main_menu_start_game);
        Button how_to = (Button) findViewById(R.id.main_menu_how_to);

        // Add the respective listeners to each button
        addStartGameListener(start_game);
        addHowToListener(how_to);

    }

    // MARK: HELPER METHODS
    // Listener for the about button.


    // Listener for the howto button.
    private void addHowToListener(Button how_to) {
        how_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HowToActivity.class);
                MainMenuActivity.this.startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    // Listener for the startgame button.
    private void addStartGameListener(Button start_game) {
        start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainMenuActivity.this, MenuCollectionActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainMenuActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    // Sets up custom font for all the buttons and header.
    private void setUpFont(TextView header, Button start_game, Button how_to, Button about) {
        Typeface font = Typeface.createFromAsset(getAssets(), "roboto-thin.ttf");
        header.setTypeface(font);
        start_game.setTypeface(font);
        how_to.setTypeface(font);
        about.setTypeface(font);
    }
}
