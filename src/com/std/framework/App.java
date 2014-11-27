package com.std.framework;

import android.app.Application;


public class App extends Application {
	/**全局应用实例*/
	public static App stdApp;
	private static final String TAG = "App";

	public App() {
		stdApp = this;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
}
