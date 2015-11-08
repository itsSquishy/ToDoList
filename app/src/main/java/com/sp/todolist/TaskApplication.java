package com.sp.todolist;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Muhd on 8/7/2015.
 */
public class TaskApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mUgZrL2qHCQj47pSbvSW7dJ2X";
    private static final String TWITTER_SECRET = "vKtcaypqZs5rpUlTbcvvwxXCS1jyyoybvtKBtuI8qRZ8dCF26n";


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());

        Parse.initialize(this, "LMUoflnof1knwHwRbdK6oXntgJJNtn8jU11U2d6I", "MKaarHJjG5L1ZjDzo8TUb4coDeGPqcx3fy9xymoY");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}
