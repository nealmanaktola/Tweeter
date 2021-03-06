package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.Utils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TweetsListFragment {

	private TwitterClient client;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		ArrayList<Tweet> dbTweets = (ArrayList<Tweet>) Tweet.getAll();
		
		if (Utils.isOnline(getActivity()))
			populateTimeline(-1,1,-1,25);
		else
			addAll(-1,dbTweets);
	}
	
	public void populateTimeline(final int index, long sinceId, long maxId, int count) {
		client.getMentionsTimeline(sinceId, maxId,count,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT);
				addAll(index,Tweet.fromJSONArray(json));
				//onRefreshComplete();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT);
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("fail", arg1.toString());
			}
		});
	}

}
