package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		// TODO Auto-generated constructor stub
	}
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tweet tweet = getItem(position);
		View v;
		
		if (convertView == null)
		{
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item,parent,false);

		} else {
			v = convertView;
		}
		
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
		TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		ivProfileImage.setImageResource(android.R.color.transparent);
		ivProfileImage.setClickable(true);
		ivProfileImage.setOnClickListener(new OnClickListener(){		
			@Override
			public void onClick(View v)
			{
				Log.d("click", v.getTag().toString());
				Intent i = new Intent(getContext(),ProfileActivity.class);
				i.putExtra("screen_name", v.getTag().toString());		
				v.getContext().startActivity(i);
			}
		})
		
;
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvScreenName.setText("@" + tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		ivProfileImage.setTag(tweet.getUser().getScreenName());
		tvDate.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
		tvUserName.setText(tweet.getUser().getName());
		
		return v;
		
	}

}
