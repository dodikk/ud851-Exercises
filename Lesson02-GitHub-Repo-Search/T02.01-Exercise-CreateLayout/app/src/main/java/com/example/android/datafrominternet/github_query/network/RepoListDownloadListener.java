package com.example.android.datafrominternet.github_query.network;

public interface RepoListDownloadListener
{
    void onRepoListLoaded(String rawResult);

    // TODO: pick a reasonable error class.
    // maybe exception copy ???
    //
    void onRepoListLoadFailed(Object error);

}
