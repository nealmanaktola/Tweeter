package com.codepath.apps.basictwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
@Table(name = "Users")
public class User extends Model {
	@Column(name = "name")
	private String name;
	
	
	@Column(name = "user_id")
	private long uid;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	
	public User() {
		super();
	}
	
	public List<Tweet> tweets() {
		return getMany(Tweet.class, "User");
	}
	
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		//Extract Values from JSON to populate the member variables
		
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		user.save();
		return user;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

}
