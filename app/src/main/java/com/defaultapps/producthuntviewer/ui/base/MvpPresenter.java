package com.defaultapps.producthuntviewer.ui.base;


public interface MvpPresenter<T> {
    void setView(T view);
    void detachView();
}
