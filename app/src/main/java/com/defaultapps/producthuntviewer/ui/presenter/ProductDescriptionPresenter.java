package com.defaultapps.producthuntviewer.ui.presenter;

import com.defaultapps.producthuntviewer.ui.base.MvpPresenter;
import com.defaultapps.producthuntviewer.ui.fragment.ProductsListViewImpl;

public interface ProductDescriptionPresenter extends MvpPresenter<ProductsListViewImpl> {
    void update();
}
