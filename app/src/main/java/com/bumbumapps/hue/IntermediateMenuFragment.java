package com.bumbumapps.hue;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

// Fragment that holds all the Intermediate Levels.
public class IntermediateMenuFragment extends Fragment {

    // MARK: ONCREATEVIEW
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setUpView(inflater, container);
    }


    // MARK: ONACTIVITYCREATED
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    // MARK: HELPER METHODS
    @NonNull
    private View setUpView(LayoutInflater inflater, ViewGroup container) {

        View view = setFont(inflater, container);

        // Fill the gridview with the correct levels.
        GridView gridView = (GridView) view.findViewById(R.id.intermediate_menu_gridview);

        // Get the Levels
        LevelStrategy[] strategies = LevelStrategyFactory.createIntermediateLevelStrategies(getActivity());

        // Set the Adapter.
        gridView.setAdapter(new LevelAdapter(getActivity(), getResources().getColor(R.color.intermediate_level_text), getActivity(), strategies));

        return view;
    }

    @NonNull
    private View setFont(LayoutInflater inflater, ViewGroup container) {
        // Set the font correctly.
        View view = inflater.inflate(R.layout.activity_intermediate_menu, container, false);
        TextView textView = (TextView) view.findViewById(R.id.intermediate_menu_header);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "roboto-thin.ttf");
        textView.setTypeface(font);
        return view;
    }
}