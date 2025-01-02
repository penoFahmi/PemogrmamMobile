package com.peno.learnasynctaskloader;

import android.content.AsyncTaskLoader;
import android.content.Context;


public class BookLoader extends AsyncTaskLoader<String> {

    private String query;

    public BookLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtilities.getBooksUrl(query);
    }
}
