package com.defaultapps.producthuntviewer.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.defaultapps.producthuntviewer.App;
import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.ui.adapter.ProductsAdapter;
import com.defaultapps.producthuntviewer.ui.presenter.ProductsListPresenterImpl;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ProductsListViewImpl extends Fragment implements ProductsListView, SwipeRefreshLayout.OnRefreshListener, ProductsAdapter.Listener {

    private Unbinder unbinder;


    @BindView(R.id.productsRecycler)
    RecyclerView productsRecycler;

    @BindView(R.id.errorTextView)
    TextView errorText;

    @BindView(R.id.errorButton)
    Button errorButton;

    @BindView(R.id.swipeRefreshProductsList)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ProductsAdapter productsAdapter;

    @Inject
    ProductsListPresenterImpl productsListPresenter;

    private OnPostClickCallback onPostClickCallback;

    public interface OnPostClickCallback {
        void onPostClick();
        void onSettingsClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onPostClickCallback = (OnPostClickCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        App.getAppComponent(getContext()).inject(this);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initToolbar();
        initRecyclerView();
        productsListPresenter.setView(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        productsAdapter.setListener(this);
        if (savedInstanceState != null) {
            productsListPresenter.restoreViewState();
        } else {
            productsListPresenter.requestCache();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.settings)
                .setIcon(new IconDrawable(getActivity().getApplicationContext(), MaterialIcons.md_settings)
                .colorRes(R.color.colorIcons)
                .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                onPostClickCallback.onSettingsClick();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productsListPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPostClickCallback = null;
    }

    @Override
    public void onRefresh() {
        productsListPresenter.requestUpdate();
    }

    @Override
    public void onProductClick(int position) {
        onPostClickCallback.onPostClick();
    }

    @OnClick(R.id.errorButton)
    void onClick() {
        productsListPresenter.requestUpdate();
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideError() {
        errorText.setVisibility(View.GONE);
        errorButton.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        errorText.setVisibility(View.VISIBLE);
        errorButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProductsList() {
        productsRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showProductsList() {
        productsRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateView(List<Post> postList) {
        productsAdapter.setData(postList);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        productsRecycler.setLayoutManager(gridLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(productsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productsRecycler.getContext(), gridLayoutManager.getOrientation());
        scaleInAnimationAdapter.setFirstOnly(false);
        productsRecycler.addItemDecoration(dividerItemDecoration);
        productsRecycler.setAdapter(scaleInAnimationAdapter);
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
