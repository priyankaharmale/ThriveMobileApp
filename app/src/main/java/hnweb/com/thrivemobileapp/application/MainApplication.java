package hnweb.com.thrivemobileapp.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Created by Ganesh.Kulkarni on 16/2/2017.
 */
public class MainApplication extends Application {
    public static final String TAG = MainApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static MainApplication mInstance;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mInstance = this;
        MultiDex.install(context);
// Initialize the SDK before executing any other operations,
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new Si(this.mRequestQueue,
//                    (ImageCache) new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}