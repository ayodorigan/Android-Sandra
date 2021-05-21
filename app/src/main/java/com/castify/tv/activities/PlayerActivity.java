
package com.castify.tv.activities;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.castify.tv.R;
import com.castify.tv.dialogs.AlertDialogs;
import com.castify.tv.dialogs.ErrorDialogs;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.models.VideoCard;
import com.castify.tv.rowvideos.PlayerVideos;
import com.castify.tv.utils.ColorCircleDrawable;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.utils.SendLogs;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.castify.tv.utils.GlobalVars.adModel;
import static com.castify.tv.utils.GlobalVars.graphics;

public class PlayerActivity extends FragmentActivity  {
    final int PLAY_INCREMENT = 5000;

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;

    private PlaybackStateListener playbackStateListener;
    private static final String TAG = PlayerActivity.class.getName();

    private VideoCard videoNext, videoPlaying;
    private ImaAdsLoader imaAdsLoader;
    private String AD_URL;

    TextView exo_position, videoPlayingTitle,nextVideoTitle,nextVideoDuration,playNext,exo_duration;

    LinearLayout nextVideoCard, videoTimingContainer,topControls, liveVideoView, customControlPanel;
    ImageView playerWaterMark, nextVideoThumbnail;
    FrameLayout playerVideos;

    DefaultTimeBar exo_progress;
    public ImageButton playBtn, pauseBtn;

    private LinkedHashMap<PlayList, ArrayList<VideoCard>> videos = new LinkedHashMap<>();
    ArrayList<PlayList> categories;
    boolean adIsPlaying;
    PageModel pageModel;
    
    public  PlayerActivity() { }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GlobalFuncs.changeToTRL(this);

        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.player_view);
        playerVideos = findViewById(R.id.playerVideos);
        exo_position = findViewById(R.id.exo_position);
        nextVideoTitle = findViewById(R.id.nextVideoTitle);
        nextVideoDuration = findViewById(R.id.nextVideoDuration);
        nextVideoThumbnail = findViewById(R.id.nextVideoThumbnail);
        playNext = findViewById(R.id.playNext);
        nextVideoCard = findViewById(R.id.nextVideoCard);
        exo_duration = findViewById(R.id.exo_duration);
        liveVideoView = findViewById(R.id.liveVideoView);
        videoTimingContainer = findViewById(R.id.videoTimingContainer);
        exo_progress = findViewById(R.id.exo_progress);
        customControlPanel = findViewById(R.id.customControlPanel);
        playBtn = findViewById(R.id.exo_play);
        pauseBtn = findViewById(R.id.exo_pause);
        videoPlayingTitle = findViewById(R.id.videoPlayingTitle);
        topControls = findViewById(R.id.topControls);
        playerWaterMark = findViewById(R.id.playerWaterMark);

        FontStyles.setFontArimoBold(this, exo_position, false);
        FontStyles.setFontArimoBold(this, videoPlayingTitle, false);
        FontStyles.setFontArimoBold(this, nextVideoTitle, false);
        FontStyles.setFontArimoBold(this, nextVideoDuration, false);
        FontStyles.setFontArimoBold(this, playNext, false);
        FontStyles.setFontArimoBold(this, exo_duration, false);
        playerView.setControllerHideDuringAds(true);
        playerView.setControllerAutoShow(true);

        customControlPanel.setPadding(0, 650,0, 0);
        if (!graphics.getShow_carousels()) {
            customControlPanel.setPadding(0, 750,0, 0);
        }

        int bgColor  = Color.parseColor("#4dffffff");
        playBtn.setBackground(new ColorCircleDrawable(bgColor));
        pauseBtn.setBackground(new ColorCircleDrawable(bgColor));
        playBtn.requestFocus();

        setPlayPauseFocus(playBtn);
        setPlayPauseFocus(pauseBtn);
        exo_progress.setOnFocusChangeListener((view, hasFocus) -> {
            if (videoPlaying != null && videoPlaying.isIs_live_streaming()) {
                return;
            }
            if (hasFocus) {
                exo_progress.setPlayedColor(Color.parseColor("#42CEFE"));
                exo_progress.setScrubberColor(Color.parseColor("#42CEFE"));

                return;
            }
            exo_progress.setPlayedColor(Color.parseColor("#ffffff"));
            exo_progress.setScrubberColor(Color.parseColor("#ffffff"));

        });

        exo_progress.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    fastForwardRewind(true);
                    return true;
                }
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    fastForwardRewind(false);
                    return true;
                }
            }
            return false;
        });

        if (GlobalVars.graphics!=null && GlobalVars.graphics.isRtl()) {
            playNext.setTextDirection(View.TEXT_DIRECTION_RTL);
            customControlPanel.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        loadWaterMark();
        nextVideoCard.setBackgroundColor(Color.TRANSPARENT);

        AD_URL = GlobalFuncs.setMacros(adModel.getVastURL(), videoNext, pageModel, GlobalVars.CAROUSEL_CLICK);
        loadPlayerVideoContents();
        dataSourceFactory = new DefaultDataSourceFactory(this, GlobalVars.USER_AGENT);
        MainActivity.stopHandler();
    }


    @Override
    public void onStart() {
        super.onStart();
//      if (Util.SDK_INT <= 23) {
        initializePlayer();
//      }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
//        if ((Util.SDK_INT <= 23 || Player == null)) {
        if (player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (Util.SDK_INT <= 23) {
            releasePlayer();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (Util.SDK_INT > 23) {
            releasePlayer();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (Util.SDK_INT > 23) {
            releasePlayer();
//        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (adIsPlaying) {
            return true;
        }


        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (playerView.isControllerVisible()) {
                    playerView.hideController();
                } else {
                    if (player != null) {
                        player.stop();
                        player.release();
                    }

                    if (graphics != null && GlobalFuncs.hasValue(graphics.getHomeScreen()) && Integer.parseInt(graphics.getHomeScreen()) <= 0) {
                        AlertDialogs.appExit(this);
                    } else {
                        finish();
                    }
                }
            }
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (!playerView.isControllerVisible()) {
                    if (graphics.getShow_carousels()) {
                        customControlPanel.setPadding(0, 650, 0, 0);
                    } else {
                        customControlPanel.setPadding(0, 750, 0, 0);
                    }

                    topControls.setVisibility(View.VISIBLE);
                    if (pauseBtn.getVisibility() == View.VISIBLE) {
                        pauseBtn.requestFocus();
                    }
                    if (playBtn.getVisibility() == View.VISIBLE) {
                        playBtn.requestFocus();
                    }

                    // if the controls are not visible and the user clicks the ok button then show controls and pause video
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                        pauseBtn.performClick();
                    }
                    return true;
                }
            }
        }

        // See whether the Player view wants to handle media or DPAD keys events.
        return playerView.dispatchKeyEvent(event);
    }


    private VideoCard getNextVideo(VideoCard videoPlaying) {
        try {
            PlayList currentContent = null;
            int nextVideoIndex, currentVideoIndex = -1;
            List<PlayList> allCategoryContents =  new ArrayList<>(videos.keySet());

            for (Map.Entry<PlayList, ArrayList<VideoCard>> catVideo : videos.entrySet()) {
                for (VideoCard videoCard : catVideo.getValue()) {
                    if (videoCard.getId().equals(videoPlaying.getId())) {
                        currentVideoIndex = catVideo.getValue().indexOf(videoCard);
                        currentContent = catVideo.getKey();
                        break;
                    }
                }
                if (currentVideoIndex >= 0 ) {
                    break;
                }
            }

            nextVideoIndex = currentVideoIndex + 1;
            if (nextVideoIndex >= Objects.requireNonNull(videos.get(currentContent)).size() ) {
                int nextContentIndex = allCategoryContents.indexOf(currentContent) + 1;
                if (nextContentIndex >= allCategoryContents.size()) {
                    nextContentIndex = 0;
                }
                return Objects.requireNonNull(videos.get(allCategoryContents.get(nextContentIndex))).get(0);
            } else {
                return Objects.requireNonNull(videos.get(currentContent)).get(nextVideoIndex);
            }
        }catch (Exception  e) {
            Log.e(TAG, "Error getting next video", e);
        }
        return null;
    }

    private void showNextVideo() {

        try {

            if (graphics != null && !graphics.getShow_carousels()) {
                return;
            }

            nextVideoCard.setVisibility(View.VISIBLE);

            nextVideoCard.setOnClickListener(v -> playVideo(videoNext));

            nextVideoCard.setOnFocusChangeListener((view, motionEvent) -> {
                if (nextVideoCard.isFocused()) {
                    nextVideoCard.setBackgroundColor(Color.WHITE);
                    playNext.setTextColor(Color.BLACK);
                    nextVideoTitle.setTextColor(Color.BLACK);
                } else {
                    nextVideoCard.setBackgroundColor(Color.TRANSPARENT);
                    playNext.setTextColor(Color.WHITE);
                    nextVideoTitle.setTextColor(Color.WHITE);
                }
            });

            nextVideoCard.setOnKeyListener((v, keyCode, event) -> {
                if (graphics.getShow_carousels() && event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    playerVideos.requestFocus();
                    customControlPanel.setPadding(0, 550, 0, 0);
                    topControls.setVisibility(View.GONE);
                    return  true;
                }
                return false;
            });


            nextVideoTitle.setText(videoNext.getTitle());

            if (videoNext.getVideoDuration() != null) {
                nextVideoDuration.setText(GlobalFuncs.getDurationString(Integer.parseInt(videoNext.getVideoDuration())));
                nextVideoDuration.setBackgroundColor(Color.parseColor("#000000"));

            }

            if (videoNext.isIs_live_streaming()) {
                nextVideoDuration.setText(R.string.live);
                nextVideoDuration.setBackgroundColor(Color.parseColor("#FF0000"));
            }

            GlobalFuncs.loadImage(videoNext.getThumbnail(), this, nextVideoThumbnail );

        } catch (Exception e) {
            Log.e(TAG, "Next Video Error", e);
        }
    }



    private void    initializePlayer() {
        AdEvent.AdEventListener adEventListener = adEvent -> {
            switch (adEvent.getType()) {
                case AD_BREAK_READY:
                    new SendLogs(PlayerActivity.this, GlobalVars.AD_BREAK, videoNext, pageModel).start();
                    break;

                case LOADED:
                    new SendLogs(PlayerActivity.this, GlobalVars.AD_REQUEST, videoNext, pageModel).start();
                    break;

                case STARTED:
                    adIsPlaying = true;
                    playerView.hideController();
                    new SendLogs(PlayerActivity.this,  GlobalVars.AD_IMPRESSION, videoNext, pageModel).start();

                    break;

                case COMPLETED:
                    adIsPlaying = false;
                    new SendLogs(PlayerActivity.this, GlobalVars.AD_COMPLETE, videoNext, pageModel).start();
                    break;

                case ALL_ADS_COMPLETED:
                    adIsPlaying = false;
                    break;

                default:
                    break;
            }
        };
        AdErrorEvent.AdErrorListener adErrorListener = adErrorEvent -> new SendLogs(PlayerActivity.this, GlobalVars.AD_ERROR, videoNext, pageModel).start();
        imaAdsLoader = new ImaAdsLoader.Builder(this).setAdEventListener(adEventListener).setPlayAdBeforeStartPosition(true).setAdErrorListener(adErrorListener).build();

        MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory)
                .setAdsLoaderProvider(unusedAdTagUri -> imaAdsLoader).setAdViewProvider(playerView);

        player = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
        playerView.setPlayer(player);
        imaAdsLoader.setPlayer(player);
        playbackStateListener = new PlaybackStateListener();
        player.addListener(playbackStateListener);
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS);
        playVideo(videoNext);

    }


    private MediaSource createLeafMediaSource(Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        MediaItem mediaItem = new MediaItem.Builder().setUri(uri).build();

        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_RTSP:
                return new RtspMediaSource.Factory()
                        .createMediaSource(mediaItem);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }


    private void releasePlayer() {

        if (player != null) {
            player.removeListener(playbackStateListener);
            imaAdsLoader.setPlayer(null);
            playerView.setPlayer(null);
            player.release();
            player = null;
        }

    }



    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }




    private class PlaybackStateListener implements Player.Listener {

        @Override
        public void onTimelineChanged(@NotNull Timeline timeline, int reason) {
//            if (player.isCurrentWindowDynamic() && player.isCurrentWindowLive()) {
//                exo_progress.setPosition(player.getCurrentPosition());
//            }
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            if (playWhenReady) {
                new SendLogs(PlayerActivity.this, GlobalVars.PLAY_VIDEO, videoNext, pageModel).start();
            } else {
                new SendLogs(PlayerActivity.this, GlobalVars.PAUSE_VIDEO, videoNext, pageModel).start();
            }
        }

        @Override
        public void onPlaybackStateChanged(int state) {
            switch (state) {
                case ExoPlayer.STATE_READY:
                    updateControls(videoPlaying);
                    break;
                case ExoPlayer.STATE_ENDED:
                    playVideo(videoNext);
                    break;
                case Player.STATE_BUFFERING:
                case Player.STATE_IDLE:
                    break;
            }
        }


        @Override
        public void onPlayerError(@NotNull ExoPlaybackException e) {
            Log.e(TAG, "Stream Error", e);
            if (GlobalFuncs.isPlayerDirectChannel()) { // Exit if home screen is 0
                ErrorDialogs.showVideStreamError(PlayerActivity.this, ()-> System.exit(1), () -> playVideo(videoNext));
            } else {
                ErrorDialogs.showVideStreamError(PlayerActivity.this, PlayerActivity.this::finish, () -> playVideo(videoNext));
            }
            new SendLogs(PlayerActivity.this, GlobalVars.ERROR, videoNext, pageModel).start();
        }
    }


    public void playVideo(VideoCard videoCard) {
        if (videoCard != null) {
            player.setMediaSource(createLeafMediaSource(Uri.parse(videoCard.getStreamURL())));
            MediaItem mediaItem = new MediaItem.Builder().setUri(videoCard.getStreamURL()).setAdTagUri(Uri.parse(AD_URL)).build();
            player.setMediaItem(mediaItem, true);
            player.prepare();
            player.setPlayWhenReady(true);
            videoPlaying = videoCard;
            videoNext = getNextVideo(videoCard);
            new SendLogs(PlayerActivity.this, GlobalVars.PLAY_VIDEO, videoNext, pageModel).start();
        }
    }

    public void updateControls(VideoCard videoCard){
        videoCard.setIs_live_streaming(player.isCurrentWindowDynamic() && player.isCurrentWindowLive());
        videoPlayingTitle.setText(videoCard.getTitle() != null ? videoCard.getTitle() : "");
        exo_duration.setText(GlobalFuncs.getDurationString(Integer.parseInt(videoCard.getVideoDuration())));
        showNextVideo();

        if (videoCard.isIs_live_streaming()) {
            liveVideoView.setVisibility(View.VISIBLE);
            videoTimingContainer.setVisibility(View.GONE);
            exo_progress.setVisibility(View.GONE);
        } else  {
            exo_progress.setVisibility(View.VISIBLE);
            liveVideoView.setVisibility(View.GONE);
            videoTimingContainer.setVisibility(View.VISIBLE);
        }


    }


    private void setPlayPauseFocus(ImageButton imageButton) {
        imageButton.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                imageButton.setBackground(new ColorCircleDrawable(Color.parseColor("#42CEFE")));
            }else {
                imageButton.setBackground(new ColorCircleDrawable(Color.TRANSPARENT));
            }
        });

        imageButton.setOnKeyListener((v, keyCode, event) -> {
            if (graphics.getShow_carousels() && event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                playerVideos.requestFocus();
                customControlPanel.setPadding(0, 550, 0, 0);
                topControls.setVisibility(View.GONE);
                return  true;
            }
            return false;
        });
    }


    private void loadPlayerVideoContents(){
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        categories = (ArrayList<PlayList>) bundle.getSerializable(GlobalVars.categoriesTag);
        pageModel = bundle.getParcelable(GlobalVars.currentPageModelTag);
        videoNext = bundle.getParcelable(GlobalVars.selectedVideoToPlayTag);
        List<PlayList> categoriesContents = GlobalFuncs.getCategoryContents(categories);
        if (categoriesContents.size() > 0) {
            videos = new LinkedHashMap<>(GlobalFuncs.getContentVideos(categoriesContents));
        } else  {
            videos = new LinkedHashMap<>(GlobalVars.videos);
        }

        String searchKeyWord = bundle.getString(GlobalVars.searchKeyWord);
        loadPlayerCarousels(searchKeyWord);

    }

    public void loadSearchVideos(String searchKeyWord) {
        List<VideoCard> filteredArticleList = (Stream.of(GlobalVars.allVideos).filter(videoCard -> videoCard.getTitle().toLowerCase().contains(searchKeyWord.toLowerCase()))).collect(Collectors.toList());
        if (filteredArticleList == null) {
            return;
        }
        PlayList searchedCategory = new PlayList();
        searchedCategory.setName("Search Results for " + searchKeyWord);
        videos.put(searchedCategory, new ArrayList<>(filteredArticleList));
        for (PageModel pageModel : GlobalVars.menus.keySet()) {
            if (pageModel.getMenu_title().equalsIgnoreCase("home")) {
                PlayList homeContainer = GlobalFuncs.getContainer(pageModel.getPage_id());
                if (homeContainer != null) {
                    List<PlayList> homeCats = GlobalFuncs.getContainerCategories(homeContainer);
                    ArrayList<PlayList> l = new ArrayList<>();
                    l.add(homeContainer);
                    l.addAll(homeCats);
                    categories = l;
                    List<PlayList> categoryContents = GlobalFuncs.getCategoryContents(l);
                    videos.putAll(GlobalFuncs.getContentVideos(categoryContents));
                }
                break;
            }
        }
    }


    void loadWaterMark() {
        if (!GlobalFuncs.hasValue(GlobalVars.graphics.getWatermark_image())) {
            return;
        }
        GlobalFuncs.loadImage(GlobalVars.graphics.getWatermark_image(), this, playerWaterMark);
        if (GlobalFuncs.checkString(GlobalVars.graphics.getWatermark_player_permanent()) != null) {
            int watermark_player_permanent = Integer.parseInt(GlobalVars.graphics.getWatermark_player_permanent());
            if (watermark_player_permanent < 0) {
                playerWaterMark.setVisibility(View.GONE);
                return;
            }
            if (watermark_player_permanent > 0) {
                Handler handler = new Handler();
                handler.postDelayed(() -> playerWaterMark.setVisibility(View.GONE), watermark_player_permanent);
            }
        }
    }

    public void loadPlayerCarousels(String searchKeyWord) {
        if (graphics != null && !graphics.getShow_carousels()) {
            customControlPanel.setPadding(0, 750,0, 0);
            return;
        }

        PlayerVideos playerVideosFragment = new PlayerVideos(playBtn, pauseBtn,  videos, pageModel, topControls, customControlPanel);
        Bundle playerVideosBundle = new Bundle();
        if (searchKeyWord != null && !searchKeyWord.isEmpty()) {
            playerVideosBundle.putString(GlobalVars.searchKeyWord, searchKeyWord);
            playerVideosFragment.setArguments(playerVideosBundle);
            loadSearchVideos(searchKeyWord);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.playerVideos, playerVideosFragment);
        ft.commit();
    }



    private void fastForwardRewind(boolean fastForward) {
        long pointToSeek = player.getCurrentPosition();
        if (fastForward) {
            pointToSeek += PLAY_INCREMENT;
            if ( pointToSeek <= player.getDuration()) {
                player.seekTo(pointToSeek);
            }
        } else {
            pointToSeek -= PLAY_INCREMENT;
            if ( pointToSeek >= 0) {
                player.seekTo(pointToSeek);
            }
        }
    }
}
