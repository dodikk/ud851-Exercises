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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;

import java.net.URL;

import com.example.android.datafrominternet.github_query.network.NetworkUtils;
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

    private TextView    _lblUrlDisplay     = null;
    private TextView    _lblSearechResults = null;
    private EditText    _slQueryInput      = null;
    private ProgressBar _progressIndicator = null;


    private IRepoListVM _viewModel = null;


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInlater = getMenuInflater();
        menuInlater.inflate(R.menu.search_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search_menu_button:
            {
                this.hideKeyboard();
                this.performSearchAsync();
                return true;
            }

            default:
            {
                return super.onOptionsItemSelected(item);
            }

        } // switch

    } // func onOptionsItemSelected()


    private void hideKeyboard()
    {
        // http://chintanrathod.com/show-hide-soft-keyboard-programmatically-in-android/
        //

        this._slQueryInput.clearFocus();

        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(
                this._slQueryInput.getWindowToken(),
                0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._lblUrlDisplay     = (TextView   ) findViewById(R.id.lblQueryUrl       );
        this._lblSearechResults = (TextView   ) findViewById(R.id.lblRawSearchResult);
        this._slQueryInput      = (EditText   ) findViewById(R.id.slQueryInput      );
        this._progressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);

        RepoListVM viewModel = new RepoListVM();
        viewModel.setDelegate(this);
        this._viewModel = viewModel;



//        this.btnSubmit.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                // TODO: how to not leak ??
//                performSearchAsync();
//            }
//        });
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

    } // func renderError()

    private void renderResult()
    {
        assert null != this._viewModel;

        String maybeResultText = this._viewModel.getRepoListState().getResultOptional();
        assert null != maybeResultText;

        this._lblSearechResults.setText(maybeResultText);

    } // func renderResult()

    private void showLoadingIndicator()
    {
        this._progressIndicator.setVisibility(ProgressBar.VISIBLE);

    } // func showLoadingIndicator()

    private void hideLoadingIndicator()
    {
        this._progressIndicator.setVisibility(ProgressBar.GONE);

    } // func hideLoadingIndicator()


    public void onRepoListLoaded()
    {
        this.updateGuiFromViewModel();

    } // func IRepoListVMDelegate.onRepoListLoaded()

    private void performSearchAsync()
    {
        String txtUserInput = this._slQueryInput.getText().toString();
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
            this._lblUrlDisplay.setText(url.toString());
        }
        else
        {
            Log.w("network", "url building failed 1");
        }

    } // func updateSearchUrlLabel()

} // class MainActivity
