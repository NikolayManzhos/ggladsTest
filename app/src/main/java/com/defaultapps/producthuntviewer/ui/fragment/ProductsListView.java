package com.defaultapps.producthuntviewer.ui.fragment;

import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.ui.base.MvpView;

import java.util.List;

public interface ProductsListView extends MvpView {

    void updateView(List<Post> postList);
    void showProductsList();
    void hideProductsList();
}
