package com.defaultapps.producthuntviewer.ui.presenter;


import android.util.Log;


import com.defaultapps.producthuntviewer.data.interactor.ProductsListInteractor;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.ui.fragment.ProductsListViewImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ProductsDescriptionPresenterUnitTest {

    private ProductsListPresenterImpl productsListPresenter;

    private List<Post> postList;

    @Mock
    ProductsListInteractor productsListInteractor;

    @Mock
    ProductsListViewImpl productsListView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);
        productsListPresenter = new ProductsListPresenterImpl(productsListInteractor);
        productsListPresenter.setView(productsListView);
        postList = new ArrayList<>();

    }
    @Test
    public void requestUpdateTest() throws Exception {
        productsListPresenter.requestUpdate();

        verify(productsListView).showLoading();
        verify(productsListView).hideProductsList();
        verify(productsListView).hideError();
    }


    @Test
    public void onFailureTest() throws Exception {
        productsListPresenter.onFailure();

        verify(productsListView).hideLoading();
        verify(productsListView).hideProductsList();
        verify(productsListView).showError();
    }

    @Test
    public void onSuccessTest() throws Exception {
        productsListPresenter.onSuccess(postList);

        verify(productsListView).hideLoading();
        verify(productsListView).hideError();
        verify(productsListView).showProductsList();
        verify(productsListView).updateView(postList);
    }

    @Test
    public void detachViewTest() throws Exception {
        productsListPresenter.detachView();
    }

    /**
     * Testing config changes behavior.
     * In this test case data is still loading, so when config appears progressBar will be shown.
     */
    @Test
    public void restoreViewStateTestLoading() throws Exception {
        productsListPresenter.setTaskStatus(true);

        productsListPresenter.restoreViewState();

        verify(productsListView).showLoading();
        verify(productsListView).hideProductsList();
        verify(productsListView).hideError();
    }

    /**
     * Testing config changes behavior.
     * In this test case data is failed to load and error screen displayed.
     */
    @Test
    public void restoreViewStateTestError() throws Exception {
        productsListPresenter.setTaskStatus(false);
        productsListPresenter.setErrorVisibility(true);
        productsListPresenter.restoreViewState();

        verify(productsListView).showError();
        verify(productsListView).hideLoading();
        verify(productsListView).hideProductsList();
    }

    /**
     * Config changes behavior.
     * If taskRunning == false and errorVisible == false when latest data should be displayed.
     */
    @Test
    public void restoreViewState() {
        productsListPresenter.setTaskStatus(false);
        productsListPresenter.setErrorVisibility(false);
        productsListPresenter.restoreViewState();

        verify(productsListView).showLoading();
        verify(productsListView).hideProductsList();
        verify(productsListView).hideError();
        verify(productsListInteractor).loadDataFromCache();
    }
}