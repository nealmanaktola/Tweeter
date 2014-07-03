package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.Utils;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class TweetsListFragment extends Fragment {
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private PullToRefreshListView lvTweets;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	}
	/*
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof OnScrollListener){
			listener = (OnScrollListener) activity;
		}
		else {
			throw new ClassCastException(activity.toString() + "must implement MyListFragment.OnSelected");
		}
	}
	*/
	public abstract void populateTimeline(int index, long max_id, long since_id, int count);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container,false);
		
		//Assign our view references
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener (){
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreTweets(totalItemsCount);
			}			
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub						
				/* Refresh Tweets */
				refreshList();
			}
			
		});
		//lvItems = ...
		//Return the layoutView
		return v;
	}
	protected void refreshList() {
		
		if (Utils.isOnline(getActivity()))
		{
			Tweet firstTweet = (Tweet) aTweets.getItem(0);
			long sinceId = firstTweet.getUid();
			populateTimeline(0,sinceId,-1,25);
			
		}
		else
		{
			Toast.makeText(getActivity(), "Not connected to Internet", Toast.LENGTH_SHORT).show();
		}	
		lvTweets.onRefreshComplete();
	}

	
	public void loadMoreTweets(int totalItemsCount)
	{
		if (tweets.size() > 0)
		{
			Tweet lastTweet = (Tweet) lvTweets.getItemAtPosition(totalItemsCount -1);
			long maxId = lastTweet.getUid() - 1;
			populateTimeline(-1,-1,maxId,25);
		}
	}
	
	public void addAll(final int index,ArrayList<Tweet> newTweets)
	{
		if (index == 0)
		{
			tweets.addAll(0,newTweets);
			aTweets.notifyDataSetChanged();
		}
		else
		{
			aTweets.addAll(newTweets);
		}
	}
	public void add(Tweet newTweet)
	{
		tweets.add(0,newTweet);
		aTweets.notifyDataSetChanged();
	}
	
}
