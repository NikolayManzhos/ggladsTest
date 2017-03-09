package com.defaultapps.producthuntviewer.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProductDescriptionViewImpl extends Fragment {

    private Unbinder unbinder;
    private Post post;
    private ProductDescriptionViewCallback productDescriptionViewCallback;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.screenshot)
    ImageView screenshot;


    public interface ProductDescriptionViewCallback {
        void openLink(String productName, String url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        productDescriptionViewCallback = (ProductDescriptionViewCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            post = new Gson().fromJson((String) arguments.getSerializable("Post"), Post.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        initToolbar();
        toolbar.setTitle(post.getName());

        name.setText(post.getName());
        description.setText(post.getTagline());
        Glide
                .with(this)
                .load(post.getScreenshotUrl().get850px())
                .placeholder(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(screenshot);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        productDescriptionViewCallback = null;
    }

    @OnClick(R.id.getProduct)
    void onClick() {
        productDescriptionViewCallback.openLink(post.getName(), post.getRedirectUrl());
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
