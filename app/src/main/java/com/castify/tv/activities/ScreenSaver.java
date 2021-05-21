package com.castify.tv.activities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.castify.tv.R;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.VideoCard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ScreenSaver extends FragmentActivity {

    private ImageView backgroundImg1;
    private ImageView backgroundImg2;
    private TextView videoTitle;
    FadingEdgeLayout screenSaverHolder;
    RelativeLayout mainLayoutScreenSaver;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    Random rand = new Random();
    Object key;
    int randomCat;

    ArrayList<VideoCard> videoCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        backgroundImg1 = findViewById(R.id.backgroundImg1);
        backgroundImg2 = findViewById(R.id.backgroundImg2);
        videoTitle = findViewById(R.id.videoTitle);
        ImageView channelLogo = findViewById(R.id.channelLogo);
        screenSaverHolder = findViewById(R.id.screenSaverHolder);
        mainLayoutScreenSaver = findViewById(R.id.mainLayoutScreenSaver);
        ImageView waterMark = findViewById(R.id.waterMark);
        FontStyles.setFontArimoBold(this, videoTitle, false);

        MainActivity.screenSaverActivity = this;



        screenSaverHolder.setFadeEdges(false, true, true, false);
        screenSaverHolder.setFadingEdgeLength(100);
        screenSaverHolder.setFadeSizes(0, 0, 2000, 0);
        screenSaverHolder.setBackgroundColor(Color.BLACK);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        GlobalFuncs.loadImage(GlobalVars.graphics.getWatermark_image(), this, waterMark);
        GlobalFuncs.loadImage(GlobalVars.graphics.getAppLogo(), this, channelLogo);

        randomCat = rand.nextInt((GlobalVars.videos.size() - 1) + 1); // get random category
        key = Objects.requireNonNull(GlobalVars.videos.keySet().toArray())[randomCat];
        videoCards = GlobalVars.videos.get(key);


        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300000L);
        animator.addUpdateListener(animation -> {
            final float progress = (float) animation.getAnimatedValue();
            final float width = backgroundImg1.getWidth();
            final float translationX = width * progress;
            backgroundImg1.setTranslationX(translationX);
            backgroundImg2.setTranslationX(translationX - width);

        });
        animator.start();
        backgroundImg2.bringToFront();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    if (videoCards != null  && videoCards.size() > 0 ) {
                        int randomVid = rand.nextInt((videoCards.size() -  1) + 1); // get random Video
                        VideoCard videoCard = videoCards.get(randomVid);
                        videoTitle.setText(videoCard.getTitle());
                        Glide.with(getApplicationContext()).load(videoCard.getThumbnail())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(backgroundImg1);
                        Glide.with(getApplicationContext()).load(videoCard.getThumbnail())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(backgroundImg2);
                        animator.reverse();
//                        backgroundImg1.setRotationY(180);
                        if (videoCards.get(videoCards.size() -1) == videoCard) {
                            randomCat = rand.nextInt((GlobalVars.videos.size() - 1) + 1); // get random category
                            key = Objects.requireNonNull(GlobalVars.videos.keySet().toArray())[randomCat];
                            videoCards = GlobalVars.videos.get(key);                        }
                    }
                }
                catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error: ", e);
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 10000);
                }
            }
        };

        handler.post(runnable);

    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 700.0f));
            backgroundImg1.setScaleX(mScaleFactor);
            backgroundImg1.setScaleY(mScaleFactor);
            backgroundImg2.setScaleX(mScaleFactor);
            backgroundImg2.setScaleY(mScaleFactor);
            return true;
        }
    }


}
