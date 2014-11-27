package com.std.framework.interfaces;

import android.content.Intent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar.LayoutParams;

public interface IBaseActivity extends IRequest {
	/**扫描功能不可用*/
	public int SCANNER_DISABLE = 0;
	/**扫描功能可用*/
	public int SCANNER_ENABLE = 1;
	
	/**
	 * 
	 * 描      述 ：设置标题栏是否居中显示
	 * 创建日期 ： 2014-4-24
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isCenter 
	 * 		true: 居中
	 * 		false: 采用系统默认标题不居中
	 */
	void requestWindowTitleCenter(boolean isCenter);

	/**
	 * 
	 * 描      述 ：是否显示ActionBar
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow 
	 * 	true:显示
	 * 	false:隐藏
	 *
	 */
	void displayActionBar(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示用户自定义bar
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayActionBarCustomLayout(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示系统图标
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayHomeIcon(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示标题
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayTitle(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示返回上一级标识
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayHomeAsUp(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示进度条
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayProgress(boolean isShow);

	/**
	 * 
	 * 描      述 ：是否显示Indeterminate进度条
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param isShow
	 *
	 */
	void displayIndeterminateProgress(boolean isShow);
	
	/**
	 * 
	 * 描      述 ：设置是否显示用户自定义logo
	 * 创建日期 ： 2014-4-25
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param enable
	 *
	 */
	void displayUseLogo(boolean enable);

	/**
	 * 
	 * 描      述 ：设置子标题
	 * 创建日期 ： 2014-4-10
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param subTitle
	 *
	 */
	void setSubTitle(CharSequence subTitle);

	/**
	 * 
	 * 描      述 ：设置标题栏用户自定义view
	 * 创建日期 ： 2014-4-21
	 * 作      者 ： lx
	 * 修改日期 ： 
	 * 修  改 者 ：
	 * @version： 1.0
	 * @param view
	 *
	 */
	void setActionBarCustomView(View view);

	void setActionBarCustomView(int layoutId);

	void setActionBarCustomView(View view, LayoutParams params);

	void setHomeButtonEnabled(boolean enable);

}

interface IRequest {
	/**
	 * 
	 * 描 述 ：异步加载数据
	 * 创建日期 ： 2014-4-9
	 * 作 者 ： lx
	 * 修改日期 ：
	 * 修 改 者 ：
	 * 
	 * @version： 1.0
	 * @param intent
	 * @return
	 * 
	 */
	void AsynLoadData(Intent intent);
}
