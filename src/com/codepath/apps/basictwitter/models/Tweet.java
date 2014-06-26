package com.codepath.apps.basictwitter.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name = "Tweets")
public class Tweet extends Model {
	@Column(name = "body")
	private String body;
	
	@Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long tweet_id;
	
	@Column(name = "created_at")
	private String createdAt;
	 
	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	
	public Tweet(){
		super();
	}
    public static List<Tweet> getAll(){
        // This is how you execute a query
        return new Select()
          .from(Tweet.class)
          .orderBy("user_id DESC")
          .execute();
    }
	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		//Extract Values from Json to populate the member variables
		
		try {
			tweet.body = jsonObject.getString("text");
			tweet.tweet_id = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		tweet.save();
		Log.d("debug", "saved " + tweet.tweet_id);

		return tweet;
	}
	
	
	public String getBody() {
		return body;
	}

	public long getUid() {
		return tweet_id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}


	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
		
	}


	@Override
	public String toString() {
		return getBody() + " - " + getUser().getScreenName();
	}
	
}
