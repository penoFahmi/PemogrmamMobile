package com.peno.learnasynctask;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsycnTask extends AsyncTask<Void, Integer, String> {


    private WeakReference<TextView> statusTextView;
    private WeakReference<ProgressBar> statusProgressBar;

    public SimpleAsycnTask(TextView statusTextView, ProgressBar statusProgressBar) {
        this.statusTextView = new WeakReference<>(statusTextView);
        this.statusProgressBar = new WeakReference<>( statusProgressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        statusProgressBar.get().setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random random = new Random();
        int number = random.nextInt(11);
        int seconds = number * 200;
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Process executed in " + seconds + " milliseconds";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        statusProgressBar.get().setVisibility(ProgressBar.INVISIBLE);
        statusTextView.get().setText(s);
    }
}
