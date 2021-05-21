package com.castify.tv.pages;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PageModel;
import com.castify.tv.rowvideos.SearchVideos;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

import java.util.Objects;

public class SearchPage extends Fragment {

    // Edit Texts
    public EditText searchInput;
    public LinearLayout alphabetsKeys0, alphabetsKeys1, numericKeys;
    FrameLayout searchArea;

    PageGraphics pageGraphics ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchView =  inflater.inflate(R.layout.menu_search, container, false);
        TextView searchResultCount = searchView.findViewById(R.id.searchResultCount);
        searchInput = searchView.findViewById(R.id.searchInput);
        searchArea = searchView.findViewById(R.id.searchArea);

        FontStyles.setFontArimoBold(requireContext(), searchInput, false);
        FontStyles.setFontArimoBold(requireContext(), searchResultCount, false);

        alphabetsKeys0 = searchView.findViewById(R.id.alphabetsKeys0);
        alphabetsKeys1 = searchView.findViewById(R.id.alphabetsKeys1);
        numericKeys = searchView.findViewById(R.id.numericKeys);

        Bundle bundle = getArguments();
        assert bundle != null;
        PageModel pageModel = GlobalFuncs.getPage(bundle.getInt(GlobalVars.currentPageID));
        if (pageModel != null ){
            pageGraphics = pageModel.getGraphic();
            GlobalFuncs.setUpPageTitle(searchView, pageModel, requireContext(), false, pageGraphics);
            if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ) {
                searchResultCount.setTextColor(Color.parseColor(pageGraphics.getText_color()));
                searchInput.setHintTextColor(Color.parseColor(pageGraphics.getText_color()));
                searchInput.setTextColor(Color.parseColor(pageGraphics.getText_color()));
                GradientDrawable drawable = (GradientDrawable)searchInput.getBackground();
                drawable.setStroke(2, Color.parseColor(pageGraphics.getText_color())); //
                searchInput.setBackground(drawable);
            }
        }


        Bundle searchInputExtras = searchInput.getInputExtras(true);
        searchInputExtras.putString("label", "Search in Videos");
        searchInputExtras.putString("backLabel", "Cancel"); // 3 - back button label
        searchInputExtras.putString("nextLabel", "Go"); // 3 - back button label
        buildKeyBoard();

        searchInput.setText(null);
        alphabetsKeys0.requestFocus();

        FragmentManager fragmentManager = this.getParentFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.searchArea, new SearchVideos(alphabetsKeys0, searchInput, searchResultCount, pageModel));
        ft.commit();

        return searchView;
    }

    private void buildKeyBoard() {
        String[] alphabets = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };

        for (int x = 0; x < 13; x ++) {
            Button key = button(alphabets[x]);
            alphabetsKeys0.addView(key);
        }

        for (int x = 13; x < alphabets.length; x++) {
            alphabetsKeys1.addView(button(alphabets[x]));
        }

        for (int x = 0; x < 10; x++) {
            numericKeys.addView(button(String.valueOf(x)));

        }



        numericKeys.addView(spaceButton());
        numericKeys.addView(deleteButton());

        // from m to n
        focusToView((Button) alphabetsKeys0.getChildAt(alphabetsKeys0.getChildCount() - 1), alphabetsKeys1.getChildAt(0), KeyEvent.KEYCODE_DPAD_RIGHT);

        // from  n to m
        focusToView((Button) alphabetsKeys1.getChildAt(0), alphabetsKeys0.getChildAt(alphabetsKeys0.getChildCount() - 1), KeyEvent.KEYCODE_DPAD_LEFT);

        // From z to 0
        focusToView((Button) alphabetsKeys1.getChildAt(alphabetsKeys1.getChildCount() - 1), numericKeys.getChildAt(0), KeyEvent.KEYCODE_DPAD_RIGHT);

        // From 0 to z
        focusToView((Button) numericKeys.getChildAt(0), alphabetsKeys1.getChildAt(alphabetsKeys1.getChildCount() - 1), KeyEvent.KEYCODE_DPAD_LEFT);

        // From x to search videos
        focusToView((Button) numericKeys.getChildAt(numericKeys.getChildCount() - 1), searchArea, KeyEvent.KEYCODE_DPAD_RIGHT);


        // from all last keys to search videos
        for (int x = 0; x < numericKeys.getChildCount() - 1; x++) {
            focusToView((Button) numericKeys.getChildAt(x), searchArea, KeyEvent.KEYCODE_DPAD_DOWN);

        }

        // From upper keys to nothing
        for (int x = 0; x < alphabetsKeys0.getChildCount() - 1; x++) {
            focusToView((Button) alphabetsKeys0.getChildAt(x), alphabetsKeys0.getChildAt(x), KeyEvent.KEYCODE_DPAD_UP);
        }

    }

    private Button button(String text) {
        Button key = new Button(requireContext());
        key.setText(text);
        LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams(100, 100);
        keyLayoutParams.setMargins(3, 3, 0, 0);
        key.setLayoutParams(keyLayoutParams);
        key.setTransformationMethod(null);
        key.setFocusable(true);
        FontStyles.setFontArimoBold(requireContext(), key, false);

        key.setTextColor(Color.parseColor("#ffffff"));
        key.setMinHeight(50);
        key.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.searchkey));
        key.setOnClickListener(v -> {
            searchInput.append(text);
        });
        key.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (GlobalVars.menuBar.getMenu_text_color_hover().equals("#ffffff")) {
                    key.setTextColor(Color.parseColor("#000000"));
                    return;
                }
                key.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
            } else {
                key.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        return key;
    }


    private Button spaceButton() {
        Button key = new Button(requireContext());
        key.setText("⎵");
        key.setTextSize(13);
        LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams(170, 100);
        keyLayoutParams.setMargins(3, 3, 0, 0);
        key.setLayoutParams(keyLayoutParams);
        key.setTransformationMethod(null);
        key.setFocusable(true);
        FontStyles.setFontArimoBold(requireContext(), key, true);

        key.setTextColor(Color.parseColor("#ffffff"));
        key.setMinHeight(50);
        key.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.searchkey));
        key.setOnClickListener(v -> searchInput.append(" "));
        key.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                key.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
            } else {
                key.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        return key;
    }

    private Button deleteButton() {
        Button key = new Button(requireContext());
        LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams(132, 100);
        keyLayoutParams.setMargins(3, 4, 0, 0);
        key.setText("X");
        key.setTypeface(null, Typeface.BOLD);
        key.setTextSize(13);        key.setLayoutParams(keyLayoutParams);
        key.setTransformationMethod(null);
        key.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        key.setFocusable(true);
        FontStyles.setFontArimoBold(requireContext(), key, true);

        key.setTextColor(Color.parseColor("#ffffff"));
        key.setMinHeight(50);
        key.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.searchkey));
        key.setOnClickListener(v -> {
            int length = searchInput.getText().length();
            if (length > 0) {
                searchInput.getText().delete(length - 1, length);
            }
        });
        key.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                key.setTextColor(Color.parseColor(GlobalVars.menuBar.getMenu_text_color_hover()));
            } else {
                key.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        key.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                searchInput.setText(null);
                return true;
            }
        });
        return key;
    }

    private void focusToView(Button key, View viewToFocus, int keyEvent) {
//        key.setOnKeyListener((v, keyCode, event) -> {
//            if (event.getAction()!=KeyEvent.ACTION_DOWN ) {
//                return true;
//            }
//            if (keyEvent > 0 && keyCode == keyEvent && key.hasFocus()) {
//                if (viewToFocus != null) {
//                    viewToFocus.requestFocus();
//                }
//            }
//            return true;
//        });

        key.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                if (keyEvent > 0 && keyCode == keyEvent && key.hasFocus()) {
                    if (viewToFocus != null) {
                        viewToFocus.requestFocus();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return false;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });
    }
}
