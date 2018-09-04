package com.example.android.datafrominternet.github_query.view_model;

import android.support.annotation.Nullable;

public interface IRepoListVM
//        extends
//            IRepoListState,
//            ILoadingState
{
    IRepoListVMDelegate getDelegate();
    void setDelegate(IRepoListVMDelegate newValue);


    IRepoListState getRepoListState();
    ILoadingState  getLoading();


    void setSearchQuery(String userInput);
    void loadRepoListAsync();

}
