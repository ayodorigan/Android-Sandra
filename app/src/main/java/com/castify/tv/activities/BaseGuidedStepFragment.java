package com.castify.tv.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepSupportFragment;

public abstract class BaseGuidedStepFragment<D> extends GuidedStepSupportFragment {
    protected Activity activity;
    protected boolean visible;

    @Override
    public void onAttach(@NonNull Context context) {
        this.activity = requireActivity();

        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        visible = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
