package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.basictwitter.ComposeDialog.ComposeDialogListener;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements ComposeDialogListener{
	
	TwitterClient client;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		setupTabs();
		//fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
		
//		Log.d("Arraylist", "Array " + tweets.toString());

	}	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }
    private void showComposeDialog()
    {
    	FragmentManager fm = getSupportFragmentManager();
    	ComposeDialog composeDialog = ComposeDialog.newInstance("Compose Tweet");
    	composeDialog.show(fm,"fragment_compose");
    }
    
	public void onFinishComposeDialog(String tweetBody) {
		Toast.makeText(this, "Tweeting, " + tweetBody, Toast.LENGTH_SHORT).show();
		postTweet(tweetBody);
	}
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("screen_name", "nealmanaktola");
		startActivity(i);
	}

    private void postTweet(String tweetBody) {
		// TODO Auto-generated method stub
    	client.postTweet(new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(JSONObject tweetJson) {
    			// TODO Auto-generated method stub
    	    	Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    	    	//fragmentTweetsList.add(Tweet.fromJSON(tweetJson));
    		}
    		@Override
    		public void onFailure(Throwable arg0) {
    			// TODO Auto-generated method stub
    			if (!Utils.isOnline(getApplicationContext()))
    				Toast.makeText(getApplicationContext(), "Failed: Not Connected to Internet", Toast.LENGTH_SHORT).show();
    			else
    				Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
    		}
    	}, tweetBody);
		
	}

	
	public void onComposeAction(MenuItem mi)
    {
    	showComposeDialog(); 	
    }
	

	
}
