package com.std.framework.activity;

import android.R.integer;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.std.framework.R;
import com.std.framework.fragment.ShareFragment;
import com.std.framework.view.RightPopupMenu;
import com.std.framework.view.RightPopupMenu.MenuAdapter;
import com.std.framework.view.RightPopupMenu.OnMenuClickListener;

public class MainActivity extends Activity {
	private static final String TAG = "LX";
	private int titleBarHeight;
	private int statusBarHeight;
	private RightPopupMenu popupMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.content, new ShareFragment()).commit();
		}
		calcBar();
	}

	private void calcBar() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;

		getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		//statusBarHeight是上面所求的状态栏的高度  
		titleBarHeight = contentTop - statusBarHeight;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		initRightPop();
		menu.add("pop").setIcon(R.drawable.ic_launcher).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void initRightPop(){
		popupMenu = new RightPopupMenu(this);
		com.std.framework.view.RightPopupMenu.MenuItem m = popupMenu.new MenuItem(1, R.drawable.more_xiaoxi, "消息", "");
		com.std.framework.view.RightPopupMenu.MenuItem m1 = popupMenu.new MenuItem(1, R.drawable.more_home, "首页", "");
		com.std.framework.view.RightPopupMenu.MenuItem m2 = popupMenu.new MenuItem(1, R.drawable.more_share, "分享", "");
		popupMenu.setOnMenuClickListener(onMenuClickListener);
		popupMenu.addMenu(m);
		popupMenu.addMenu(m1);
		popupMenu.addMenu(m2);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().toString().equals("pop")) {
			popupMenu.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT | Gravity.TOP, 0, 140);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private OnMenuClickListener onMenuClickListener = new OnMenuClickListener() {
		
		@Override
		public void onClick(com.std.framework.view.RightPopupMenu.MenuItem menu) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, menu.name, 0).show();
			popupMenu.dismiss();
		}
	};

}
