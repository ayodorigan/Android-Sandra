package com.castify.tv.pages;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.castify.tv.R;
import com.castify.tv.activities.MainActivity;
import com.castify.tv.adapters.CategoryAdapter;
import com.castify.tv.models.MenuPageItem;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.rowvideos.CategoryVideos;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryPage extends Fragment implements CategoryAdapter.OnCatListener {

    private HorizontalGridView categoryRecyclerView;
    private FrameLayout catVideos;

    private PageModel pageModel;
    ArrayList<MenuPageItem> menuPageItems = new ArrayList<>();
    List<PlayList> pageCategories;
    PlayList catPageContainer;
    PageGraphics pageGraphics;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View catPage =  inflater.inflate(R.layout.cat_page, container, false);
        categoryRecyclerView = catPage.findViewById(R.id.categoryRecyclerView);
        catVideos = catPage.findViewById(R.id.catVideos);

        Bundle bundle = getArguments();
        assert bundle != null;
        int pageID = bundle.getInt(GlobalVars.currentPageID);
        pageModel = GlobalFuncs.getPage(pageID);

        if ( pageModel != null ) {
            menuPageItems = GlobalVars.menus.get(pageModel);
            pageGraphics = pageModel.getGraphic();
            catPageContainer = GlobalFuncs.getContainer(pageModel.getPage_id());
            if (catPageContainer != null) {
                pageCategories = GlobalFuncs.getContainerCategories(catPageContainer);
                if (pageCategories.size() > 0) {
                    CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), pageCategories, catPageContainer.getKeyword(), this, catVideos ,pageGraphics, ((MainActivity)requireActivity()));
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    loadVideos(pageCategories.get(0));
                }
            }
        }

        categoryRecyclerView.requestFocus(); // Request focus to this recycler view as soon as the page is opened.
//        categoryRecyclerView.setOnKeyListener(new Key);
        return catPage;
    }

    @Override
    public void OnCatClickListener(int pos, View view, boolean isSelected) {
        PlayList category = pageCategories.get(pos);
        loadVideos(category);
    }



    public void loadVideos(PlayList category) {
        ArrayList<PlayList> l = new ArrayList<>();
        l.add(category);

        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fragmentManager).beginTransaction();
        ft.replace(R.id.catVideos, new CategoryVideos(categoryRecyclerView, l, pageModel));
        ft.commit();
    }
}
