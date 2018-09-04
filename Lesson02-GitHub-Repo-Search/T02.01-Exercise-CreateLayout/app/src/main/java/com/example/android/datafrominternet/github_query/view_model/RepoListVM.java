package com.example.android.datafrominternet.github_query.view_model;

import android.support.annotation.Nullable;

public class RepoListVM
        implements
            IRepoListVM   ,

// TODO: extract dedicated structures when things get too complicated
            IRepoListState,
            ILoadingState

{
    private String  _errorText = null ;
    private String  _result    = null ;
    private boolean _isLoading = false;
    private boolean _hasResult = false;
    private boolean _hasError  = false;

    private IRepoListVMDelegate _delegate = null;

    private String _userInput = "";
    private

    // ===== IRepoListVM
//
    public void setSearchQuery(String userInput)
    {
        // TODO: cancel loading side effect
        //

        this._userInput = "";
    }

    public void loadRepoListAsync()
    {

    }


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




