package com.example.android.datafrominternet.github_query.view_model;

import android.support.annotation.Nullable;

public interface IRepoListState
{
    @Nullable String getResultOptional();
    @Nullable String getErrorTextOptional();

    boolean hasResult();
    boolean hasError();
}
