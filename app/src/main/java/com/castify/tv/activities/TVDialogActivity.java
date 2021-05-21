package com.castify.tv.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

import java.util.List;

public class TVDialogActivity extends FragmentActivity implements DialogContract {
    private static final int YES = 0;
    private static final int NO = 1;

    public static final String ARG_TITLE_RES = "arg_title";
    public static final String ARG_POSITIVE_RES = "arg_yes";
    public static final String ARG_NEGATIVE_RES = "arg_no";
    public static final String ARG_ICON_RES = "arg_icon";
    public static final String ARG_DESC_RES = "arg_desc";
    public static final String REQUEST_CODE = "rcode";

    public static int REQUEST_CODE_VALUE;

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        Bundle args = getIntent().getExtras();
        int iconRes = args.getInt(ARG_ICON_RES);
        int yesRes = args.getInt(ARG_POSITIVE_RES);
        int noRes = args.getInt(ARG_NEGATIVE_RES);
        String descRes = args.getString(ARG_DESC_RES);
        String titleRes = args.getString(ARG_TITLE_RES);
        REQUEST_CODE_VALUE = args.getInt(REQUEST_CODE);
        GlobalFuncs.changeToTRL(this);

        GuidedStepSupportFragment.add(getSupportFragmentManager(), AlertSelectionFragment.newInstance(titleRes, iconRes, descRes, yesRes, noRes));
    }

    private static void addAction(List<GuidedAction> actions, long id, String title) {
        actions.add(new GuidedAction.Builder(mContext)
                .id(id)
                .title(title)
                .description("")
                .build());
    }

    @Override
    public void onSelection(boolean positive) {
        if(positive){
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }


    public static class AlertSelectionFragment extends BaseGuidedStepFragment<DialogContract> {

        public  static  AlertSelectionFragment newInstance(String titleRes, int iconRes, String descRes,int yesRes,int noRes) {
            Bundle args = new Bundle();
            args.putString(ARG_TITLE_RES,titleRes);
            args.putInt(ARG_ICON_RES,iconRes);
            args.putInt(ARG_NEGATIVE_RES,noRes);
            args.putInt(ARG_POSITIVE_RES,yesRes);
            args.putString(ARG_DESC_RES,descRes);


            AlertSelectionFragment fragment = new AlertSelectionFragment();
            fragment.setArguments(args);
            return fragment;

        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            assert getArguments() != null;
            String title = getArguments().getString(ARG_TITLE_RES);
            String breadcrumb = "";
            String description = getArguments().getString(ARG_DESC_RES);
            Drawable icon = requireActivity().getDrawable(getArguments().getInt(ARG_ICON_RES));
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
            addAction(actions, YES,
                    getResources().getString(getArguments().getInt(ARG_POSITIVE_RES)));
            addAction(actions, NO,
                    getResources().getString(getArguments().getInt(ARG_NEGATIVE_RES)));
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {

            try {

                Intent intent=new Intent();

                if (action.getId() == YES) {
                    intent.putExtra(GlobalVars.QUIT_MSG,"QUIT");
                } else {
                    intent.putExtra(GlobalVars.QUIT_MSG,"CONTINUE");
                }

                requireActivity().setResult(REQUEST_CODE_VALUE, intent);

                requireActivity().finish();

                Log.e("RIGAN", "TV Dialog");

            }catch (Exception e) {
                Log.e(getClass().getSimpleName(), "TV Dialog", e);
            }

        }
    }
}