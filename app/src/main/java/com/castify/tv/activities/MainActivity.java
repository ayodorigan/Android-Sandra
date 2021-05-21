
package com.castify.tv.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.method.KeyListener;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.castify.tv.R;
import com.castify.tv.models.MenuPageItem;
import com.castify.tv.models.PageModel;
import com.castify.tv.pages.AboutPage;
import com.castify.tv.pages.CategoryPage;
import com.castify.tv.pages.ContactPage;
import com.castify.tv.pages.InitialHomePage;
import com.castify.tv.pages.LivePage;
import com.castify.tv.pages.MoreChannelsPage;
import com.castify.tv.pages.SearchPage;
import com.castify.tv.pages.SocialMediaPage;
import com.castify.tv.pages.StorePage;
import com.castify.tv.utils.GlideApp;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.utils.SendLogs;
import com.castify.tv.utils.SvgSoftwareLayerSetter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.castify.tv.dialogs.AlertDialogs.appExit;

//import com.tv.mars.tv.Utils.GlideApp;


public class MainActivity extends FragmentActivity {
    // Helps in always maintaining the focused menu item to InitialHomePage when the menu bar is opened
    // if the menu is closed set this value to 0. if the menu is opened and the it is 0, focus goes to home menu item
    // otherwise increment it.
    int menusFocusCount;

    // Views
    public PageModel pageModel;

    public Button exitApp, menuHomeButton, activeMenuItem, lastHomeButton;
    FadingEdgeLayout main_layoutHolder;
    public View activeLineIndicator;
    public ImageView menuWaterMark, bgShadow;

    public static FragmentActivity screenSaverActivity;

    //Layouts
    public RelativeLayout menuBarPanel;
    public LinearLayout menuItemsHolder;
    public LinearLayout main_layout, lowerMenuLayout;


    // Layouts
    public RelativeLayout homeRootLayout;


    //Images
    public ImageView appLogo;

    public Uri mBackgroundURI;
    static Handler handler;
    static Runnable r;


//    Data structures
    Map<Button, String> menuItemsButton = new LinkedHashMap<>();
    ArrayList<View> lineIndicators = new ArrayList<>();
    Map<Button, PictureDrawable> menuIcons = new LinkedHashMap<>();

//    Pages
    public MoreChannelsPage moreChannelsPage = new MoreChannelsPage();
    public AboutPage aboutPage = new AboutPage();
    public CategoryPage categoryPage = new CategoryPage();
    public ContactPage contactPage = new ContactPage();
    public LivePage livePage = new LivePage();
    public InitialHomePage initialHomePage = new InitialHomePage();
    public SearchPage searchPage = new SearchPage();
    public SocialMediaPage socialMediaPage = new SocialMediaPage();
    public StorePage storePage = new StorePage();


    @Override public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            startIdleListener();
            initMainActivityViews();

            View lineGradient = findViewById(R.id.lineGradient);
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.BR_TL,
                    new int[] {0xFF616261, Color.parseColor("#00FFFF"), 0xFF131313});
            gd.setCornerRadius(0f);
            lineGradient.setBackground(gd);

//            Change to RTL if true
            GlobalFuncs.changeToTRL(this);

            buildMenuItems();
            new SendLogs(this, GlobalVars.APP_OPEN, null, pageModel).start();

            loadPage(pageModel);
        }catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error", e);
        }

    }

    /**
     * Starts the timer for screensaver and listens for any interactions with the app from the user.
     * Restarts when user interacts the app
     */
    void startIdleListener() {
        Intent intent = new Intent(this, ScreenSaver.class);
        handler = new Handler();
        r = () -> startActivity(intent);
        startHandler();

    }


    /**
     * Initializes all the main activity view
     */
    void initMainActivityViews() {
        main_layout = findViewById(R.id.main_layout);
        homeRootLayout = findViewById(R.id.homeRootLayout);
        bgShadow = findViewById(R.id.bgShadow);

        if (!GlobalVars.showMenu) {
            return;
        }

        menuBarPanel = findViewById(R.id.menuBarPanel);
        menuItemsHolder = findViewById(R.id.menuHolder);
        appLogo = findViewById(R.id.appLogo);
        exitApp = findViewById(R.id.exitApp);
        menuHomeButton = new Button(this);
        lastHomeButton = new Button(this);

        main_layoutHolder = findViewById(R.id.main_layoutHolder);

        exitApp.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color()));
        exitApp.setNextFocusLeftId(main_layout.getId());
        exitApp.setTextSize(13); // Zoom in a bit the text size if the button
        exitApp.setTransformationMethod(null);
        exitApp.setTextAppearance(this, R.style.Widget_Leanback_Row_Header);
        //Update the exit button in the bottom
        String btnTxt = "Exit";// + GlobalVars.graphics.getAppName();
        exitApp.setText(btnTxt);

        lowerMenuLayout = findViewById(R.id.lowerMenuLayout);
        menuWaterMark = findViewById(R.id.menuWaterMark);

        if (GlobalFuncs.hasValue(GlobalVars.menuBar.getMenu_icon())) {
            menuWaterMark.setVisibility(View.VISIBLE);
            GlobalFuncs.loadImage(GlobalVars.menuBar.getMenu_icon(), this, menuWaterMark);
        }
        exitApp.setOnClickListener(view -> appExit(this));

        main_layout.setNextFocusLeftId(menuBarPanel.getId());
        exitApp.setKeyListener(new KeyListener() {
               @Override
               public int getInputType() {
                   return 0;
               }

               @Override
               public boolean onKeyDown(View view, Editable editable, int keyCode, KeyEvent keyEvent) {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_UP:
                            menuItemsHolder.requestFocus();
                            lastHomeButton.requestFocus();
                            return true;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            menuHomeButton.requestFocus();
                            return true;
                        default:
                            return false;
                    }
                }

               @Override
               public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
                   return false;
               }

               @Override
               public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
                   return false;
               }

               @Override
               public void clearMetaKeyState(View view, Editable editable, int i) {

               }
           });

        exitApp.setOnFocusChangeListener((view, hasFocus) -> {

            if (hasFocus) {
                menusFocusCount += 1; // Increment to allow setting focus to other menu items
                //Check if the menuHomeButton is focused and the menu item is 0, if so focus on it
                if (!menuHomeButton.isFocused() && menusFocusCount == 0 ) {
//                    menuHomeButton.requestFocus();
                    return;
                }
                exitApp.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
                exitApp.setTextSize(15); // Zoom in a bit the text size if the button
            } else {

                exitApp.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color()));
                exitApp.setTextSize(13); // Zoom in a bit the text size if the button
            }
            showHideMenu();
        });
    }



    @Override
    public void onBackPressed() {
        try {
            if (!GlobalVars.showMenu) {
                appExit(this);
                return;
            }

            // open menu if there is not menu item focused including the exit button
            if (!isAnyMenuHasFocused() && !exitApp.isFocused()) {
                menuHomeButton.requestFocus();
                return;
            }

            main_layout.requestFocus();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Back Press Error", e);
        }
    }


    /**
     * Sets the app background image
     * @param url - url of the image to be set as background
     */
    public void setBackground(String url) {
        if (!GlobalFuncs.hasValue(url)) {
            bgShadow.setVisibility(View.GONE);
            homeRootLayout.setBackground(null);
            homeRootLayout.setBackgroundColor(Color.parseColor("#000000"));
            return;
        }
        Glide.with(this)
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        homeRootLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


    /**
     * Set app logo on the menu
     * @param logoURL - url of the image
     */
    public void setAppLogo(String logoURL) {
        try {
            RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.default_background);

            Glide.with(this)
                    .asBitmap()
                    .load(logoURL)
                    .apply(options)
                    .into(appLogo);
        }catch (Exception e) {
            Log.e(getClass().getSimpleName(), "App Logo Error:", e);

        }
    }



    /**
     * Builds tha app Menus and setting up event listeners
     */
    private void buildMenuItems() {
        if (!GlobalVars.showMenu) {
            return;
        }
        menuBarPanel.setVisibility(View.VISIBLE);

        for (Map.Entry<PageModel, ArrayList<MenuPageItem>> menu : GlobalVars.menus.entrySet()) {
            PageModel pageModel = menu.getKey();
            if (pageModel != null) {
                String title;
                if (pageModel.getMenu_title() != null && !pageModel.getMenu_title().isEmpty()) {
                    title = pageModel.getMenu_title();
                } else  {
                    title = pageModel.getPage_title();
                }

                if (title != null && !title.isEmpty()) {

                    Button menuBtn = new Button(this);
                    menuBtn.setText(title);
                    menuBtn.setTextAppearance(this, R.style.Widget_Leanback_Row_Header);
                    menuBtn.setTransformationMethod(null);
                    menuBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    menuBtn.setFocusable(true);
                    menuBtn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    menuBtn.setTextSize(13);
                    menuBtn.setPadding(10, 0, 20, 0);
                    menuBtn.setMinWidth(320);
                    menuBtn.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color()));

                    menuItemsButton.put(menuBtn, title);

                    //Get the home button
                    if (title.equalsIgnoreCase("Home")) {
                        menuHomeButton = menuBtn;
                        this.pageModel = pageModel;
                    }

                    setMenuItemIcon(menuBtn, pageModel);

                    // Line selection indicator  View
                    View lineIndicator = new View(this);
                    ViewGroup.LayoutParams lineIndicatorParams= new ViewGroup.LayoutParams(5, LinearLayout.LayoutParams.MATCH_PARENT);
                    lineIndicator.setLayoutParams(lineIndicatorParams);
                    lineIndicator.setPadding(10,0,0,0);
                    menuBtn.setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
                            // Check if the menuHomeButton is focused and the menusFocusCount is 0, if so focus on it
                            if (!menuHomeButton.isFocused() && !exitApp.isFocused() && menusFocusCount == 0 ) {
                                menuHomeButton.requestFocus();
                                return;
                            }
                            menusFocusCount += 1; // Increment to allow setting focus to other menu items

                            menuBtn.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
                            menuBtn.setTextSize(15);
                        } else {
                            if (activeMenuItem != menuBtn) {
                                menuBtn.setTextSize(13);
                            }
                            menuBtn.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color()));
                        }
                        runOnUiThread(this::showHideMenu);
                    });

                    lineIndicators.add(lineIndicator);
                    menuBtn.setOnClickListener(view -> {
                        //Add line indicator
                        for (View indicator :  lineIndicators) {
                            indicator.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        }

                        activeLineIndicator = lineIndicator;
                        activeMenuItem = menuBtn;
                        this.pageModel = pageModel;
                        loadPage(this.pageModel);

                    });

                    lastHomeButton = menuBtn;
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(lineIndicator);
                    linearLayout.addView(menuBtn);
                    menuItemsHolder.addView(linearLayout);
                }
            }
        }

        menuHomeButton.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 2;
            }

            @Override
            public boolean onKeyDown(View view, Editable editable, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    menusFocusCount = GlobalVars.menus.size(); // Increment to allow setting focus to other menu items
                    exitApp.requestFocus();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
                return false;
            }

            @Override
            public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable editable, int i) {

            }
        });

        lastHomeButton.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 2;
            }

            @Override
            public boolean onKeyDown(View view, Editable editable, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {//                        menusFocusCount = GlobalVars.menus.size(); // Increment to allow setting focus to other menu items
                    lowerMenuLayout.requestFocus();
                    exitApp.requestFocus();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
                return false;
            }

            @Override
            public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable editable, int i) {

            }
        });

        showHideMenu();

        activeLineIndicator = lineIndicators.get(0);
        activeMenuItem = menuHomeButton;

    }


    /**
     * Checks if the is any of the menu items that is currently focused
     * @return false if not
     */
    private boolean isAnyMenuHasFocused() {
        for (Map.Entry<Button, String> menuItem : menuItemsButton.entrySet()) {
            if (menuItem.getKey().isFocused()) {
                return true;
            }
        }
        menusFocusCount = 0; // reset the value to 0 if the menu bar has not any focused menu item
        return false;
    }


    /**
     * Hide or show the menu. It id controlled by the currently focused Item
     */
    private void showHideMenu() {
        if (!GlobalVars.showMenu) {
            return;
        }

        final ChangeBounds transition = new ChangeBounds();
        transition.setDuration(25L);
        TransitionManager.beginDelayedTransition(menuBarPanel, transition);

        if (exitApp.isFocused() || isAnyMenuHasFocused()) {
            for (Map.Entry<Button, String> menuItem : menuItemsButton.entrySet()) {
                menuItem.getKey().setText(menuItem.getValue());
            }

            // Maximize the panel
            menuBarPanel.setLayoutParams(new RelativeLayout.LayoutParams(350, RelativeLayout.LayoutParams.MATCH_PARENT));
            GlobalFuncs.animateShowView(lowerMenuLayout);

            main_layoutHolder.setForeground(ContextCompat.getDrawable(this,  R.drawable.gradient_left_right) );
            menuBarPanel.setBackground(null);
            setAppLogo(GlobalVars.graphics.getAppLogo()); //set the large logo when the menu is maximized
            return;
        }

        // Minimize menu
        for (Map.Entry<Button, String> menuItem : menuItemsButton.entrySet()) {
            menuItem.getKey().setText(null);
        }

        // Minimize the panel
        menuBarPanel.setLayoutParams(new RelativeLayout.LayoutParams(120, RelativeLayout.LayoutParams.MATCH_PARENT));
        GlobalFuncs.animateHideHide(lowerMenuLayout);
        main_layoutHolder.setForeground(null );
        menuBarPanel.setBackground(ContextCompat.getDrawable(this,  R.drawable.menu_bar_gradient_left_right));

        // Add the background color of the menu bar to translucent when the menu is closed
        setAppLogo(GlobalVars.menuBar.getMenu_logo_image()); // set the small logo

    }


    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==23) {
            String quitMsg = data.getStringExtra(GlobalVars.QUIT_MSG);
            assert quitMsg != null;
                new SendLogs(this, GlobalVars.EXIT_APP, null, pageModel).start();
                try {
                    Thread.sleep(2000);
                }catch (Exception ignored) {}
                finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public static void stopHandler() {
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(r);
        if (screenSaverActivity != null) {
            screenSaverActivity.finish();
        }
    }
    public static void startHandler() {
       try {

           if (GlobalVars.graphics.getScreen_saver_time_on() > 0) {
               long sec = Long.parseLong(String.valueOf(GlobalVars.graphics.getScreen_saver_time_on()));
               long milliSecs = TimeUnit.SECONDS.toMillis(sec);
               handler.postDelayed(r, milliSecs);
           }
       }catch (Exception e) {
           Log.e(MainActivity.class.getSimpleName(), "Screen Saver error", e);
       }
    }


    /**
     * Sets up the menu Icon with '.svg' or other extensions
     * @param menuBtn Menu buttons to get the icon
     * @param pageModel The page model that contains the icon details
     */
    private void setMenuItemIcon(Button menuBtn, PageModel pageModel) {
        if (pageModel.getPage_menu_icon().endsWith("svg")) {
            RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(this).as(PictureDrawable.class).listener(new SvgSoftwareLayerSetter());

            requestBuilder.diskCacheStrategy(DiskCacheStrategy.DATA).load(pageModel.getPage_menu_icon()).into(new CustomTarget<PictureDrawable>(35, 35) {
                @Override
                public void onResourceReady(@NonNull PictureDrawable resource, @Nullable Transition<? super PictureDrawable> transition) {
                    menuBtn.setCompoundDrawablesWithIntrinsicBounds( resource,null, null, null);
                    menuBtn.setCompoundDrawablePadding(20);
                    menuIcons.put(menuBtn, resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) { }

            });

        } else  {
            Glide.with(this).load(pageModel.getPage_menu_icon()).into(new CustomTarget<Drawable>(35,35) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        menuBtn.setCompoundDrawablesWithIntrinsicBounds( resource,null, null, null);
                        menuBtn.setCompoundDrawablePadding(20);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        menuBtn.setCompoundDrawablesWithIntrinsicBounds( placeholder,null, null, null);
                        menuBtn.setCompoundDrawablePadding(20);
                    }
                }
            );
        }
    }


    /**
     * Loads the Selected Page into view
     * @param pageModel Details of the targeted page
     */
    private void loadPage(PageModel pageModel) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment page = null;

        if (!GlobalVars.showMenu) {
            page = initialHomePage;
            ft.replace(main_layout.getId(), page);
            ft.addToBackStack(null);
            ft.commit();
            return;
        }

        switch (pageModel.getPage_type_id()) {
            case 1 :
                page = initialHomePage;
                break;

            case 2 :
                page = searchPage;
                break;

            case 3 :
                page = aboutPage;
                break;

            case 4 :
                page = storePage;
                break;

            case 5 :
                page = socialMediaPage;
                break;

            case 6 :
                page = moreChannelsPage;
                break;

            case 7 :
                page = contactPage;
                break;

            case 10 :

            case 16 :
                page = categoryPage;
                break;

            case 15 :
                page = livePage;
                break;

        }

        if (page != null) {
            resetMenuItems();
            setBackground(pageModel.getPage_background_image());
            Bundle bundle = new Bundle();
            bundle.putParcelable(GlobalVars.currentPageModelTag, pageModel);
            bundle.putInt(GlobalVars.currentPageID, pageModel.getPage_id());

            page.setArguments(bundle);
            if (page.isAdded()) {
                ft.remove(page);
            }
            ft.replace(main_layout.getId(), page);
            ft.addToBackStack(null);
            ft.commit();
            showHideMenu();
//            if (pageModel.getPage_background_image() == null || pageModel.getPage_background_image().isEmpty()) {
//                mBackgroundManager.clearDrawable();
//            }
            new SendLogs(this, GlobalVars.USER_CLICK, null, pageModel).start();

        }
    }

    public void resetMenuItems() {
        for (Button menuBtn : menuItemsButton.keySet()) {
            menuBtn.setTypeface(null, Typeface.NORMAL);
            menuBtn.setTextSize(13);
        }

        activeLineIndicator.setBackgroundColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
        activeMenuItem.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
        activeMenuItem.setTextSize(15);
        activeMenuItem.setTypeface(null, Typeface.NORMAL);
        main_layout.removeAllViews();

    }
}
