package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment utF = UserTimelineFragment.newInstance(getIntent().getStringExtra("screen_name"));
		ft.replace(R.id.flUserTimeline, utF);
		ft.commit();
		
		loadProfileInfo(getIntent().getStringExtra("screen_name"));

	}
	
	private void loadProfileInfo(String screenName) {
		// TODO Auto-generated method stub
		TwitterApplication.getRestClient().getUserInfo(screenName,new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject json) {
				User u = User.fromJson(json);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
			};
		});			
	}
	
	protected void populateProfileHeader(User u) {
		// TODO Auto-generated method stub
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagLine());
		tvFollowers.setText(u.getFollowers() + " Followers");
		tvFollowing.setText(u.getFollowing() + " Following");
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(u.getProfileImageUrl(), ivProfileImage);		
	}
}
