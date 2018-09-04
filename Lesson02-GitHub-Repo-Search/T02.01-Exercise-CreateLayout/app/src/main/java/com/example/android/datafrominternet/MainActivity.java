/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import com.example.android.datafrominternet.github_query.network.DownloadRepoListTask;
import com.example.android.datafrominternet.github_query.network.NetworkUtils;
import com.example.android.datafrominternet.github_query.network.RepoListDownloadListener;
import com.example.android.datafrominternet.github_query.view_model.ILoadingState;
import com.example.android.datafrominternet.github_query.view_model.IRepoListState;
import com.example.android.datafrominternet.github_query.view_model.IRepoListVM;
import com.example.android.datafrominternet.github_query.view_model.IRepoListVMDelegate;
import com.example.android.datafrominternet.github_query.view_model.RepoListVM;

public class MainActivity
        extends
            AppCompatActivity
        implements
            IRepoListVMDelegate
{

    private TextView lblUrlDisplay     = null;
    private TextView lblSearechResults = null;
    private EditText slQueryInput      = null;
    private Button   btnSubmit         = null;

    private IRepoListVM _viewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lblUrlDisplay     = (TextView) findViewById(R.id.lblQueryUrl);
        this.lblSearechResults = (TextView) findViewById(R.id.lblRawSearchResult);
        this.slQueryInput      = (EditText) findViewById(R.id.slQueryInput);
        this.btnSubmit         = (Button) findViewById(R.id.btnSubmit);


        RepoListVM viewModel = new RepoListVM();
        viewModel.setDelegate(this);
        this._viewModel = viewModel;


        // this.disableNetworkMainThreadAssertionsForPrototyping();

        this.btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO: how to not leak ??
                performSearchAsync();
            }
        });
    }


    private void updateGuiFromViewModel()
    {
        ILoadingState loadingState = this._viewModel.getLoadingState();

        if (loadingState.isLoading())
        {
            this.showLoadingIndicator();
        }
        else
        {
            this.hideLoadingIndicator();

            IRepoListState listState = this._viewModel.getRepoListState();
            if (listState.hasResult())
            {
                this.renderResult();
            }
            else
            {
                assert listState.hasError();
                this.renderError();
            }
        }

    } // func updateGuiFromViewModel()

    private void renderError()
    {

        String txtError = this._viewModel.getRepoListState().getErrorTextOptional();
        assert (null != txtError);

        // TODO: replace stub wiwh proper layout
        //
        Toast alert =
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR : " + txtError,
                        Toast.LENGTH_SHORT
                );

        alert.show();
    }

    private void renderResult()
    {
        assert null != this._viewModel;

        String maybeResultText = this._viewModel.getRepoListState().getResultOptional();
        assert null != maybeResultText;

        this.lblSearechResults.setText(maybeResultText);
    }

    private void showLoadingIndicator()
    {
        // TODO: replace stub wiwh proper layout
        //
        Toast alert =
                Toast.makeText(
                        getApplicationContext(),
                        "Start Loading",
                        Toast.LENGTH_SHORT
                );


        alert.show();
    }

    private void hideLoadingIndicator()
    {
        // TODO: replace stub wiwh proper layout
        //

        Toast alert =
            Toast.makeText(
                    getApplicationContext(),
                    "STOP Loading",
                    Toast.LENGTH_SHORT
            );


        alert.show();

    }


    public void onRepoListLoaded()
    {
        this.updateGuiFromViewModel();

    } // func IRepoListVMDelegate.onRepoListLoaded()

    private void performSearchAsync()
    {
        String txtUserInput = this.slQueryInput.getText().toString();
        this.updateSearchUrlLabel(txtUserInput);

        this._viewModel.setSearchQuery(txtUserInput);
        this._viewModel.loadRepoListAsync();

        this.updateGuiFromViewModel();

    } // func performSearchAsync()

    private void updateSearchUrlLabel(String txtUserInput )
    {
        URL url =  NetworkUtils.buildUrl(txtUserInput);

        if (null != url)
        {
            this.lblUrlDisplay.setText(url.toString());
        }
        else
        {
            Log.w("network", "url building failed 1");
        }
    }

}
