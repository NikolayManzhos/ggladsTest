package com.defaultapps.producthuntviewer.data.interactor;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.local.LocalService;
import com.defaultapps.producthuntviewer.data.local.sp.SharedPreferencesManager;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.defaultapps.producthuntviewer.data.model.post.PostsResponse;
import com.defaultapps.producthuntviewer.data.net.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class ProductsListInteractor {
    private Context context; //Application context
    private NetworkService networkService;
    private LocalService localService;
    private SharedPreferencesManager sharedPreferencesManager;
    private ProductsListViewInteractorCallback callback;
    private boolean responseStatus = false;

    private AsyncTask<Void, Void, Void> downloadPostsTask;
    private AsyncTask<Void, Void, Void> loadFromCacheTask;

    private final long CACHE_EXP_TIME = 86400000;

    private List<Post> postList;

    public interface ProductsListViewInteractorCallback {
        void onSuccess(List<Post> postList);
        void onFailure();
    }

    @Inject
    public ProductsListInteractor(NetworkService networkService,
                              LocalService localService,
                              SharedPreferencesManager sharedPreferencesManager,
                              Context context) {
        this.context = context;
        this.networkService = networkService;
        this.localService = localService;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public void bindInteractor(ProductsListViewInteractorCallback productsListViewInteractorCallback) {
        this.callback = productsListViewInteractorCallback;
    }

    public void loadPostsData() {
        downloadPostsTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Response<PostsResponse> response = networkService
                            .getNetworkCall()
                            .getPosts(context.getString(R.string.PRODUCT_HUNT_KEY), sharedPreferencesManager.getCategory())
                            .execute();
                    if (response.isSuccessful()) {
                        parseData(response.body());
                        sharedPreferencesManager.setCacheTime(System.currentTimeMillis());
                        localService.writeResponseToFile(response.body());
                        responseStatus = true;
                    } else {
                        responseStatus = false;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    responseStatus = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) {
                    if (responseStatus) {
                        callback.onSuccess(postList);
                    } else {
                        callback.onFailure();
                    }
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public void loadDataFromCache() {
        Log.d("NETWORK", String.valueOf(localService.isCacheAvailable()));
        if (localService.isCacheAvailable()
                && sharedPreferencesManager.getCacheTime() != 0
                && (System.currentTimeMillis() - sharedPreferencesManager.getCacheTime()) < CACHE_EXP_TIME ) {
            loadFromCacheTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        PostsResponse data = localService.readResponseFromFile();
                        parseData(data);
                        responseStatus = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.d("AsyncTaskLocal", "FAILED TO READ DATA");
                        responseStatus = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (callback != null) {
                        if (responseStatus) {
                            Log.d("AsyncTaskLocal", "SUCCESS");
                            callback.onSuccess(postList);
                        } else {
                            Log.d("AsyncTaskLocal", "FAILURE");
                            callback.onFailure();
                        }
                    }
                }
            };
            loadFromCacheTask.execute();

        } else {
            localService.deleteCache();
            loadPostsData();
        }
    }

    public void setCategory(String category, int position) {
        sharedPreferencesManager.setCategory(category.toLowerCase());
        sharedPreferencesManager.setCategoryNumber(position);
    }

    public int getCategory() {
        return sharedPreferencesManager.getCategoryNumber();
    }

    private void parseData(PostsResponse posts) {
        postList = new ArrayList<>(posts.getPosts());
    }

}
