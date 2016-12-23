package com.groupbwt.draw;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.groupbwt.draw.view.DrawCanvas;
import com.thebluealliance.spectrum.SpectrumDialog;

public class DrawActivity extends AppCompatActivity {

    private Button mButtonColor;

    private Button mButtonClear;

    private DrawCanvas mDrawCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mButtonColor = (Button) findViewById(R.id.color_btn);
        mDrawCanvas = (DrawCanvas) findViewById(R.id.draw_canvas);
        mButtonClear = (Button) findViewById(R.id.clear_btn);


        mButtonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorDialog();
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCanvas();
            }
        });

    }

    private void showColorDialog () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SpectrumDialog.Builder builder = new SpectrumDialog.Builder(getApplicationContext());
                int[] colors =  {Color.RED, Color.BLUE, Color.GRAY, Color.GREEN, Color.YELLOW, Color.BLACK};
                builder.setColors(colors);
                builder.setSelectedColor(mDrawCanvas.getStrokeColor());
                SpectrumDialog dialog = builder.build();

                dialog.setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        Log.d("DrawActivity", "color: " + color);
                        if(positiveResult) {
                            mDrawCanvas.setStrokeColor(color);
                        }
                    }
                });
                dialog.show(fragmentManager, "color-dialog");

            }
        });
    }

    private void clearCanvas () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDrawCanvas.clearCanvas();
            }
        });
    }
}
