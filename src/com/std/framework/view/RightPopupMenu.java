package com.std.framework.view;

import java.util.ArrayList;

import android.R.anim;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.std.framework.R;
import com.umeng.socialize.net.v;

public class RightPopupMenu extends PopupWindow {
	private Context context;
	private View v_content;
	private ListView lst_menu;
	private ArrayList<MenuItem> menus;
	private MenuAdapter adapter;
	private OnMenuClickListener onMenuClickListener;
	private boolean isMenuChanged;

	public RightPopupMenu(Context context) {
		this.context = context;
		initData();
		initContentView();
		setDefaultAttrs();
	}

	public RightPopupMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
		initContentView();
		setDefaultAttrs();
	}

	private void initContentView() {
		v_content = LayoutInflater.from(context).inflate(R.layout.panel_popup_right, null);
		lst_menu = (ListView) v_content.findViewById(R.id.lst_menu);
		lst_menu.setAdapter(adapter);
		lst_menu.setOnItemClickListener(onItemClickListener);
		setContentView(v_content);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if (onMenuClickListener != null)
				onMenuClickListener.onClick(menus.get(position));
		}
	};

	private void initData() {
		menus = new ArrayList<RightPopupMenu.MenuItem>();
		adapter = new MenuAdapter();
	}

	private void setDefaultAttrs() {
		setFocusable(true);
		setOutsideTouchable(true);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		setAnimationStyle(android.R.style.Animation_Dialog);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// TODO Auto-generated method stub
		setWidth(windowAdjustContent());
		super.showAtLocation(parent, gravity, x, y);
	}
	
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		// TODO Auto-generated method stub
		setWidth(windowAdjustContent());
		super.showAsDropDown(anchor, xoff, yoff);
	}

	private int windowAdjustContent() {
		if (isMenuChanged) {
			v_content.measure(MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST));
			isMenuChanged = false;
		}
		return v_content.getMeasuredWidth();
	}

	public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
		this.onMenuClickListener = onMenuClickListener;
	}

	public void addMenu(MenuItem item) {
		menus.add(item);
		isMenuChanged = true;
	}

	public class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menus != null ? menus.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (menus != null && menus.size() > position)
				return menus.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.menu_item_right, null);
				holder = new ViewHolder();
				holder.ico = (ImageView) convertView.findViewById(R.id.img_item_ico);
				holder.name = (TextView) convertView.findViewById(R.id.tv_item_name);
				holder.tag = convertView.findViewById(R.id.img_item_tag);
				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			if (menus != null) {
				MenuItem item = menus.get(position);
				holder.ico.setImageResource(item.resId);
				holder.name.setText(item.name);
			}
			return convertView;
		}

		class ViewHolder {
			public ImageView ico;
			public TextView name;
			public View tag;
		}

	}

	public interface OnMenuClickListener {
		void onClick(MenuItem menu);
	}

	public class MenuItem {
		public int id;
		public int resId;
		public String name;
		public String tag;

		public MenuItem(int id, int resId, String name, String tag) {
			super();
			this.id = id;
			this.resId = resId;
			this.name = name;
			this.tag = tag;
		}
	}

}
