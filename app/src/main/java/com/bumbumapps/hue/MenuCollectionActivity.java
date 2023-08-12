package com.bumbumapps.hue;

import android.content.Intent;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

// This activity holds the different difficulty screens so you can switch in between.
public class MenuCollectionActivity extends FragmentActivity {

    // MARK: VARS
    MenuCollectionPagerAdapter pagerAdapter;
    ViewPager viewPager;
    Fragment[] fragments;


    // MARK: ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_collection);

        setUp();

    }


    // MARK: ONBACKPRESSED
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(this,MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    // MARK: HELPER METHODS
    public void setUp() {
        fragments = new Fragment[]{new EasyMenuFragment(), new IntermediateMenuFragment(), new ExpertMenuFragment()};
        pagerAdapter = new MenuCollectionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, new FadePageTransformer());

    }
}



