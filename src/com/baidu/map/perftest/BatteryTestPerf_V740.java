/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class BatteryTestPerf_V740 extends TestSuite {
	
	public static TestSuite suite() { 
        TestSuite suite = new TestSuite("TestSuite Test"); 
        suite.addTestSuite(POISearch1.class); 
       // suite.addTestSuite(POISearch2.class); 
        return suite;	
}
}


class POISearch1 extends ActivityInstrumentationTestCase2 {

	private static final String PROCESS_NAME = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	private static final int PAUSE_TIME = 1000;
	private Activity mActivity;
	private Solo solo;
	private ToolKit mToolKit;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public POISearch1() {
		super(PROCESS_NAME, launcherActivityClass);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
	}

	public void test_POISearch1() throws Exception {

		mToolKit.Sleep(PAUSE_TIME * 2);
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("西单", "详情");
		mToolKit.Sleep(PAUSE_TIME * 6);

		mToolKit.BackToMapMain();

		mToolKit.SearchForText("东单", "详情");
		mToolKit.Sleep(PAUSE_TIME * 6);
		mToolKit.BackToMapMain();

		mToolKit.SearchForText("苹果园", "详情");
		mToolKit.Sleep(PAUSE_TIME * 6);
		mToolKit.BackToMapMain();
		mToolKit.SearchForText("大望路", "详情");
		mToolKit.Sleep(PAUSE_TIME * 6);
		mToolKit.BackToMapMain();
		mToolKit.SearchForText("芍药居", "详情");
		mToolKit.Sleep(PAUSE_TIME * 6);
		mToolKit.BackToMapMain();
	}}

class POISearch2 extends ActivityInstrumentationTestCase2{

	private static final String PROCESS_NAME = "com.baidu.BaiduMap";
	private static final int PAUSE_TIME = 1000;
	private Activity mActivity;
	private Solo solo;
	private ToolKit mToolKit;

	public POISearch2(String pkg, Class activityClass) {
		super(pkg, activityClass);
		// TODO Auto-generated constructor stub
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
	}
	
	public void test_POISearch2() throws Exception{
		
		mToolKit.Sleep(PAUSE_TIME*2);
    	mToolKit.jumpToCity("北京市", 0);	
		mToolKit.Sleep(PAUSE_TIME); 
		mToolKit.SearchForText("西直门", "详情");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 东直门");
		mToolKit.SearchForText("东直门", "详情");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 崇文门");
		mToolKit.SearchForText("崇文门", "详情");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 西二旗");
		mToolKit.SearchForText("西二旗", "详情");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 知春路");
		mToolKit.SearchForText("知春路", "详情");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
	}}