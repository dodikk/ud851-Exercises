package com.example.android.datafrominternet.github_query.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

//import android.annotation.MainThread;
//import android.annotation.WorkerThread;


public class DownloadRepoListTask
    extends AsyncTask<String, Void, String>
{
    private RepoListDownloadListener _listener;

    public DownloadRepoListTask(
        RepoListDownloadListener listener)
    {
        this._listener = listener;
    }

    @Override
//    @WorkerThread
    protected String doInBackground(String... singleUserInputParams)
    {
        String userInput = singleUserInputParams[0];

        URL fullQueryUrl = NetworkUtils.buildUrl(userInput);
        if (null == fullQueryUrl)
        {
            // TODO: exception marshalling.
            //
            // TODO: how to do `maybe` monad in java?
            // https://medium.com/@afcastano/monads-for-java-developers-part-1-the-optional-monad-aa6e797b8a6e
            //

            return "";
        }


        try
        {
            String result = NetworkUtils.getResponseFromHttpUrl(fullQueryUrl);
            return result;
        }
        catch (IOException ex)
        {
            // TODO: exception marshalling
            // TODO: how to do `maybe` monad in java?
            //

            ex.printStackTrace();
            return "";
        }

    } // func AsyncTask.doInBackground()


    @Override
    // @MainThread
    protected void onPostExecute(String result)
    {
        this._listener.onRepoListLoaded(result);

    } // func AsyncTask.onPostExecute

} // class
