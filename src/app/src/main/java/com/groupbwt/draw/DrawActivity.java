package com.groupbwt.draw;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

    private int[] mColors = {
            Color.RED,
            Color.parseColor("#F44336"),
            Color.parseColor("#E91E63"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#673AB7"),
            Color.parseColor("#3F51B5"),
            Color.parseColor("#2196F3"),
            Color.BLUE,
            Color.parseColor("#03A9F4"),
            Color.parseColor("#00BCD4"),
            Color.parseColor("#009688"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#8BC34A"),
            Color.GREEN,
            Color.parseColor("#CDDC39"),
            Color.parseColor("#FFEB3B"),
            Color.YELLOW,
            Color.parseColor("#FFC107"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#795548"),
            Color.parseColor("#9E9E9E"),
            Color.parseColor("#607D8B"),
            Color.BLACK
    };

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

        setColorBtnColor(mDrawCanvas.getStrokeColor());

    }

    private void showColorDialog () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SpectrumDialog.Builder builder = new SpectrumDialog.Builder(getApplicationContext());
                builder.setColors(mColors);
                builder.setSelectedColor(mDrawCanvas.getStrokeColor());
                SpectrumDialog dialog = builder.build();

                dialog.setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        Log.d("DrawActivity", "color: " + color);
                        if(positiveResult) {
                            mDrawCanvas.setStrokeColor(color);
                            setColorBtnColor(color);
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

    private void setColorBtnColor (int color) {
        GradientDrawable bgShape = (GradientDrawable)mButtonColor.getBackground();
        bgShape.setColor(color);
    }
}
