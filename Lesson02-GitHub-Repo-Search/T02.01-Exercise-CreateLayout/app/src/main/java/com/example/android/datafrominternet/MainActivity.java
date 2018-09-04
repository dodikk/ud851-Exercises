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
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // TODO (26) Create an EditText variable called mSearchBoxEditText

    // TODO (27) Create a TextView variable called mUrlDisplayTextView
    // TODO (28) Create a TextView variable called mSearchResultsTextView


    private TextView lblUrlDisplay = null;
    private TextView lblSearechResults = null;
    private EditText slQueryInput = null;
    private Button   btnSubmit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lblUrlDisplay = (TextView) findViewById(R.id.lblQueryUrl);
        this.lblSearechResults = (TextView) findViewById(R.id.lblRawSearchResult);
        this.slQueryInput = (EditText) findViewById(R.id.slQueryInput);
        this.btnSubmit = (Button)findViewById(R.id.btnSubmit);

        this.disableNetworkMainThreadAssertionsForPrototyping();

        this.btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO: how to not leak ??
                performSearchSync();
            }
        });
    }

    private void disableNetworkMainThreadAssertionsForPrototyping()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    private void performSearchSync()
    {
        String txtUserInput = this.slQueryInput.getText().toString();
        this.updateSearchUrlLabel(txtUserInput);



        String txtDownloaded = this.implPerformSearchSync(txtUserInput);
        this.lblSearechResults.setText(txtDownloaded);
    }

    private void performSearchAsync()
    {


    }

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

    private String implPerformSearchSync(String txtUserInput)
    {
        URL url = NetworkUtils.buildUrl(txtUserInput);
        if (null == url)
        {
            Log.w("network", "url building failed 2");
        }


        try
        {
            String txtDownloaded = NetworkUtils.getResponseFromHttpUrl(url);
            return txtDownloaded;
        }
        catch (IOException ex)
        {
            Log.w("network", "download failed");
            ex.printStackTrace();

            return "";
        }
    }
}
