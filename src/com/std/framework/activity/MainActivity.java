package com.std.framework.activity;

import android.app.Activity;
import android.os.Bundle;

import com.std.framework.R;
import com.std.framework.fragment.ShareFragment;

public class MainActivity extends Activity {
	private static final String TAG="LX";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(savedInstanceState == null){
			getFragmentManager().beginTransaction().add(R.id.content, new ShareFragment()).commit();
		}
	}
	
}
