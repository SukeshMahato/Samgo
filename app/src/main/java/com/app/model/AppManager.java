package com.app.model;

import android.content.Context;

public class AppManager {

	private static AppManager instance = null;

	private static Context context = null;

	public User user = null;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		AppManager.context = context;
	}

	private AppManager() {
		user = new User();
	}

	public static AppManager getSinleton() {
		if (instance == null) {
			instance = new AppManager();

		}
		return instance;
	}
}
