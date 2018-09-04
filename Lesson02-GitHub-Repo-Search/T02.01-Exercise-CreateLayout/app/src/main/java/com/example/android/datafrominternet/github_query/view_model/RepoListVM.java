package com.example.android.datafrominternet.github_query.view_model;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.android.datafrominternet.github_query.network.DownloadRepoListTask;
import com.example.android.datafrominternet.github_query.network.RepoListDownloadListener;


public class RepoListVM
        implements
            IRepoListVM             ,
            RepoListDownloadListener,

// TODO: extract dedicated structures when things get too complicated
            IRepoListState,
            ILoadingState

{
    private String  _errorText = null ;
    private String  _result    = null ;
    private boolean _isLoading = false;


    private IRepoListVMDelegate _delegate = null;

    private String _userInput = "";
    private DownloadRepoListTask _loader = null;

// ===== RepoListDownloadListener
//
    public void onRepoListLoaded(String rawResult)
    {
        this._result = rawResult;
        this._errorText = null;

        this._isLoading = false;
        this._loader    = null;

        this._delegate.onRepoListLoaded();

    } // func RepoListDownloadListener.onRepoListLoaded()

    public void onRepoListLoadFailed(Object error)
    {
        this._result = null;
        this._errorText = "Something went wrong";

        this._isLoading = false;
        this._loader = null;

        this._delegate.onRepoListLoaded();

    } // func RepoListDownloadListener.onRepoListLoadFailed()

// ===== IRepoListVM
//
    public void setSearchQuery(String userInput)
    {
        // TODO: maybe apply the side effect in a more elegant way
        this.cancelNetworkRequestIfNeeded();

        this._userInput = userInput;

    } // // func IRepoListVM.setSearchQuery()

    private void cancelNetworkRequestIfNeeded()
    {
        if (null != this._loader)
        {

            if (AsyncTask.Status.RUNNING == this._loader.getStatus())
            {
                this._loader.cancel(true);
                this._loader = null;
            }
        }

    } // func cancelNetworkRequestIfNeeded()


    public void loadRepoListAsync()
    {
        // TODO: maybe add throttling
        //===

        this.cancelNetworkRequestIfNeeded();

        this._isLoading = true;

        DownloadRepoListTask loader = new DownloadRepoListTask(this);
        this._loader = loader;

        loader.execute(this._userInput);

    } // func IRepoListVM.loadRepoListAsync()


    public IRepoListVMDelegate getDelegate()
    {
        return this._delegate;
    }

    public void setDelegate(IRepoListVMDelegate newValue)
    {
        if (null != newValue)
        {
            assert (null == this._delegate);
        }

        this._delegate = newValue;
    }


    public IRepoListState getRepoListState()
    {
        // TODO: extract dedicated structures when things get too complicated
        return this;
    }

    public ILoadingState getLoading()
    {
        // TODO: extract dedicated structures when things get too complicated
        return this;
    }

// ===== ILoadingState
//
    public boolean isLoading()
    {
        return this._isLoading;

    } // func ILoadingState.isLoading()


// ===== IRepoListState
//
    public @Nullable String getErrorTextOptional()
    {
        return this._errorText;

    } // func IRepoListState.getErrorTextOptional()

    @Nullable
    public String getResultOptional()
    {
        return this._result;

    } // func IRepoListState.getResultOptional()

    public boolean hasResult()
    {
        return (null != this._result);

    } // func IRepoListState.hasResult()

    public boolean hasError()
    {
        return (null != this._errorText);

    } // func IRepoListState.hasError()

} // class RepoListVM




