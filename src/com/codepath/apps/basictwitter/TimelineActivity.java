package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.ComposeDialog.ComposeDialogListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity implements ComposeDialogListener {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private PullToRefreshListView lvTweets;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		populateTimeline(1,-1,25);
		
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);

		lvTweets.setOnScrollListener(new EndlessScrollListener (){
		
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (tweets.size() > 0)
				{
					Tweet lastTweet = (Tweet) lvTweets.getItemAtPosition(totalItemsCount -1);
					long maxId = lastTweet.getUid() - 1;
					populateTimeline(-1,maxId,25);
				}
			}			
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
				Tweet firstTweet = (Tweet) tweets.get(0);
				long sinceId = firstTweet.getUid();
				refreshTimeline(sinceId,-1,25);
			}
			
		});
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }
    private void showComposeDialog()
    {
    	FragmentManager fm = getFragmentManager();
    	ComposeDialog composeDialog = ComposeDialog.newInstance("Compose Tweet");
    	composeDialog.show(fm,"fragment_compose");
    }
	public void onFinishComposeDialog(String tweetBody) {
		Toast.makeText(this, "Tweeting, " + tweetBody, Toast.LENGTH_SHORT).show();
		postTweet(tweetBody);
	}
    private void postTweet(String tweetBody) {
		// TODO Auto-generated method stub
    	client.postTweet(new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(JSONObject tweetJson) {
    			// TODO Auto-generated method stub
    	    	Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    	    	tweets.add(0,Tweet.fromJSON(tweetJson));
    	    	aTweets.notifyDataSetChanged();
    		}
    		@Override
    		public void onFailure(Throwable arg0) {
    			// TODO Auto-generated method stub
    			Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
    		}
    	}, tweetBody);
		
	}
	public void onComposeAction(MenuItem mi)
    {
    	showComposeDialog(); 	
    }
	
	public void loadTweets(int id)
	{
		
	}
	public void populateTimeline(long sinceId, long maxId, int count) {
		client.getHomeTimeline(sinceId, maxId,count,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
				aTweets.addAll(Tweet.fromJSONArray(json));
			}
			@Override
			public void onFailure(Throwable e, String s) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT);
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	
	}
	public void refreshTimeline(long sinceId, long maxId, int count) {
		client.getHomeTimeline(sinceId, maxId,count,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
				tweets.addAll(0,Tweet.fromJSONArray(json));
				aTweets.notifyDataSetChanged();
				lvTweets.onRefreshComplete();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				// TODO Auto-generated method stub
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});

	}
}
