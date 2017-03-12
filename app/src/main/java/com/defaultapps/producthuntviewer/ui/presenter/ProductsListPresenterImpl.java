package com.defaultapps.producthuntviewer.ui.presenter;

import com.defaultapps.producthuntviewer.data.interactor.ProductsListInteractor;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.ui.fragment.ProductsListViewImpl;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class ProductsListPresenterImpl implements ProductsListPresenter, ProductsListInteractor.ProductsListViewInteractorCallback {

    private ProductsListInteractor productsListInteractor;
    private ProductsListViewImpl view;

    private boolean taskStatus = false;
    private boolean errorVisibility = false;


    @Inject
    public ProductsListPresenterImpl(ProductsListInteractor productsListInteractor) {
        this.productsListInteractor = productsListInteractor;
    }

    @Override
    public void setView(ProductsListViewImpl view) {
        productsListInteractor.bindInteractor(this);
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
        productsListInteractor.bindInteractor(null);
    }

    @Override
    public void restoreViewState() {
        if (view != null) {
            if (taskStatus) {
                view.showLoading();
                view.hideError();
                view.hideProductsList();
            } else if (errorVisibility) {
                view.showError();
                view.hideLoading();
                view.hideProductsList();
            } else {
                requestCache();
            }
        }
    }

    @Override
    public void requestUpdate() {
        setTaskStatus(true);
        setErrorVisibility(false);
        if (view != null) {
            view.showLoading();
            view.hideProductsList();
            view.hideError();
        }
        productsListInteractor.loadPostsData();
    }

    @Override
    public void requestCache() {
        if (view != null) {
            view.showLoading();
            view.hideProductsList();
            view.hideError();
        }
        productsListInteractor.loadDataFromCache();
    }

    @Override
    public void setCurrentCategory(String category, int position) {
        productsListInteractor.setCategory(category, position);
    }

    @Override
    public int getCurrentCategory() {
        return productsListInteractor.getCategory();
    }

    @Override
    public void onSuccess(List<Post> postList) {
        if (view != null) {
            view.updateView(postList);
            view.hideLoading();
            view.hideError();
            view.showProductsList();
        }
        setTaskStatus(false);
        setErrorVisibility(false);
    }

    @Override
    public void onFailure() {
        if (view != null) {
            view.hideLoading();
            view.hideProductsList();
            view.showError();
        }
        setTaskStatus(false);
        setErrorVisibility(true);
    }

    @VisibleForTesting
    void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    @VisibleForTesting
    void setErrorVisibility(boolean errorVisibility) {
        this.errorVisibility = errorVisibility;
    }
}
