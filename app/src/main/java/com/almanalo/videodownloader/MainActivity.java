package com.almanalo.videodownloader;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {

    EditText urlInput;
    Button pasteButton;
    Button downloadButton;
    Spinner qualitySpinner;
    ProgressBar progressBar;
    TextView statusText;

    final String SERVER_URL =
            "http://127.0.0.1:5000/download";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        urlInput = findViewById(R.id.urlInput);
        pasteButton = findViewById(R.id.pasteButton);
        downloadButton = findViewById(R.id.downloadButton);
        qualitySpinner = findViewById(R.id.qualitySpinner);
        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.statusText);


        String[] qualities = {
                "best",
                "720",
                "480",
                "360"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(

                        this,

                        android.R.layout.simple_spinner_dropdown_item,

                        qualities
                );


        qualitySpinner.setAdapter(adapter);


        pasteButton.setOnClickListener(v -> pasteLink());


        downloadButton.setOnClickListener(v -> startDownload());

    }


    private void pasteLink() {

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(
                                Context.CLIPBOARD_SERVICE
                        );


        if (clipboard.hasPrimaryClip()) {

            CharSequence text =
                    clipboard
                            .getPrimaryClip()
                            .getItemAt(0)
                            .coerceToText(this);


            if (text != null) {

                urlInput.setText(text);

            }

        }

    }


    private void startDownload() {

        String videoUrl =
                urlInput.getText()
                        .toString()
                        .trim();


        if (videoUrl.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please paste a video link",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }


        progressBar.setVisibility(View.VISIBLE);

        statusText.setText("Downloading...");

        downloadButton.setEnabled(false);


        String quality =
                qualitySpinner
                        .getSelectedItem()
                        .toString();


        new Thread(() -> {

            try {

                URL url = new URL(SERVER_URL);


                HttpURLConnection connection =
                        (HttpURLConnection)
                                url.openConnection();


                connection.setRequestMethod("POST");

                connection.setRequestProperty(
                        "Content-Type",
                        "application/json"
                );


                connection.setConnectTimeout(10000);

                connection.setReadTimeout(600000);

                connection.setDoOutput(true);


                JSONObject json = new JSONObject();

                json.put("url", videoUrl);

                json.put("quality", quality);


                OutputStream output =
                        connection.getOutputStream();


                output.write(
                        json.toString().getBytes("UTF-8")
                );


                output.close();


                int responseCode =
                        connection.getResponseCode();


                runOnUiThread(() -> {

                    progressBar.setVisibility(View.GONE);

                    downloadButton.setEnabled(true);


                    if (responseCode == 200) {

                        statusText.setText(
                                "Download Complete!\n\n" +
                                "Saved in Downloads/VideoDownloader"
                        );

                    } else {

                        statusText.setText(
                                "Download failed"
                        );

                    }

                });


            } catch (Exception e) {

                runOnUiThread(() -> {

                    progressBar.setVisibility(View.GONE);

                    downloadButton.setEnabled(true);

                    statusText.setText(
                            "Error: " + e.getMessage()
                    );

                });

            }

        }).start();

    }

}
