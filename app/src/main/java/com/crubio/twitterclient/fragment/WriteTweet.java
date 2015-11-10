package com.crubio.twitterclient.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class WriteTweet extends DialogFragment{
    private static final String CLASS = WriteTweet.class.getSimpleName();
    private static final int MAX_CHARS = 140;
    private static final String TO = "TO";
    private int curLength;
    private EditText tweet;
    private ImageView profileImage;
    private ImageView close;
    private User user;
    private TextView characters;
    private Button send;

    public static WriteTweet newInstance(String to) {
        WriteTweet myFragment = new WriteTweet();

        Bundle args = new Bundle();
        args.putString(TO, to);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_write_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tweet = (EditText) view.findViewById(R.id.et_tweet);
        if(getArguments() != null && getArguments().containsKey(TO)){
            String to = getArguments().getString(TO);
            tweet.setText(to);
        }
        characters = (TextView) view.findViewById(R.id.tv_characters);
        profileImage = (ImageView) view.findViewById(R.id.iv_user);
        close = (ImageView) view.findViewById(R.id.iv_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        send = (Button) view.findViewById(R.id.bt_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curLength >= 0){
                    TwitterClient client = RestApplication.getRestClient();
                    client.postTweet(tweet.getText().toString(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e(CLASS, "Error sending the tweet. Status: " + statusCode, error);
                            Toast.makeText(getActivity(), "Error sending the tweet, please try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Your tweet is too long", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Show soft keyboard automatically and request focus to field
        tweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                curLength = MAX_CHARS-s.length();
                characters.setText(""+curLength);
                if(curLength < 0){
                    characters.setTextColor(Color.RED);
                    send.setBackgroundColor(getResources().getColor(R.color.primaryColorLight));
                }else{
                    characters.setTextColor(getResources().getColor(R.color.lighter_gray));
                    send.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        fetchUser();
    }

    private void fetchUser(){
        TwitterClient client = RestApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                user = new User(jsonObject);
                Picasso.with(getActivity())
                       .load(user.getUserProfileImage())
                        .into(profileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.e(CLASS, "timeline error: " + jsonObject);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}