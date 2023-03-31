package com.bumbumapps.hue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class GameActivity extends AppCompatActivity {


    // MARK: VARS
    private String[] hexCodes;
    private int rowLength;
    private int columnLength;
    private int hintMode;
    public static int oldPos = 0;
    public static int newPos = 0;
    GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // Set all needed parameters for generating the game field.
        setParamsForColGeneration();

        // Create the game field with given params.
        ColorTile[] generatedColorTiles = ColorTileFactory.getGameColorTiles(hexCodes,
                columnLength, rowLength, hintMode);


        // Set adapter and init gridView.
        gridView = setUpGridView(generatedColorTiles);

        // Init the onTouch Listener for starting to drag around the tile.
        gridView.setOnTouchListener(new CustomOnTouchListener(oldPos, gridView));


        // Init the onDrag Listener for dropping the tile.
        gridView.setOnDragListener(new CustomOnDragListener(this, oldPos, newPos, gridView,rowLength));
    }

    // MARK: Helper Methods.
    private void setParamsForColGeneration() {
        Intent intent = getIntent();
        hexCodes = intent.getStringArrayExtra("hexCodes");
        rowLength = intent.getIntExtra("rowLength", 0);
        columnLength = intent.getIntExtra("columnLength", 0);
        hintMode = intent.getIntExtra("hintMode", 0);
    }

    private GridView setUpGridView(ColorTile[] generatedColorTiles) {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new TileAdapter(this, generatedColorTiles, columnLength));
        gridView.setNumColumns(columnLength);
        return gridView;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(this,MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}


