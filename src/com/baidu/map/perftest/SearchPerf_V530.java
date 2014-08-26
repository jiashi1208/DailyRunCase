/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class SearchPerf_V530 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.BaiduMap.map.mainmap.MainMapActivity";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.BaiduMap.BaiduMap";
	private static Class<?> launcherActivityClass;

	int PAUSE_TIME = 1000;
	int MIN_PAUSE_TIME = 20;
	final int TIME_OUT = 10000;
	
	String PATH = "/sdcard/MapPerf/baidu/";
	String PROCESS_NAME = TARGET_PACKAGE_ID;

	boolean bExist = false;
	
	private Solo solo;
	private ToolKit mToolKit = null;
	private Activity mActivity = null;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
//			android.util.Log.e("yanyuan", "exp: " + e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public SearchPerf_V530() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
		Log.i( "ActivityManager", "BaiduMap SetUp" );
		Log.e( "baidumaptest", "SetUp" );
		solo.sleep(PAUSE_TIME);
		String strUpdate = "以后再说";
		boolean bUpdate = solo.waitForText(strUpdate,0,PAUSE_TIME*2);
		if( bUpdate )
		{
			solo.sleep(PAUSE_TIME);
			solo.clickOnText(strUpdate);
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	private void OnSearch()
	{
		mToolKit.clickOnViewById(RIDConfigV530.ID_ONESEARCH_BOX);
	}

	/**	搜索指定文本
	 * @param strKey		搜索关键字
	 */
	private void SearchForText( String strKey ){
		//	点击搜索框
		OnSearch();
		solo.sleep(PAUSE_TIME);

		//	输入文本
		solo.clearEditText(0);
		solo.enterText(0, strKey);
		solo.sleep(PAUSE_TIME);

		//	点击搜索按钮
		solo.sendKey(Solo.ENTER);
	}
	
	private boolean SearchForText( String strKey, String vrst ){
		return SearchForText(strKey, vrst, TIME_OUT);
	}
	
	private boolean SearchForText( String strKey, String vrst, int timeout ){
		SearchForText(strKey);
		solo.sleep(PAUSE_TIME);
		return solo.waitForText(vrst, 0, timeout);
	}


	/**	跳转到指定城市
	 * @param strCityName
	 * @param type			跳转方式:0-离线包；1-搜索
	 */
	private void jumpToCity( String strCityName, int type ){
		solo.sleep(PAUSE_TIME);
		
		if( 0 == type )
		{
			solo.clickOnText("我的");
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText("离线地图");
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText(strCityName);
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText("查看地图");
			solo.sleep(PAUSE_TIME);
		}else if( 1 == type )
		{
			
		}
	}
	

	private void recordViewId( String text ){
		ToolKit.loge(text);
	}
	
	public void getAllID() throws Exception {
		int id = -1;
		
		solo.sleep(PAUSE_TIME);
		id = mToolKit.getViewIdByPoint(new int[]{ RIDConfigV530.ID_ONESEARCH_BOX_X, RIDConfigV530.ID_ONESEARCH_BOX_Y });
		recordViewId("public static final int " + RIDConfigV530.ID_ONESEARCH_BOX_NAME + " = " + id);
		
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		
		id = mToolKit.getViewIdByPoint(new int[]{ RIDConfigV530.ID_ROUTE_BUS_BUTTON_X, RIDConfigV530.ID_ROUTE_BUS_BUTTON_Y });
		recordViewId("public static final int " + RIDConfigV530.ID_ROUTE_BUS_BUTTON_NAME + " = " + id);
		
		id = mToolKit.getViewIdByPoint(new int[]{ RIDConfigV530.ID_ROUTE_CAR_BUTTON_X, RIDConfigV530.ID_ROUTE_CAR_BUTTON_Y });
		recordViewId("public static final int " + RIDConfigV530.ID_ROUTE_CAR_BUTTON_NAME + " = " + id);
		
		id = mToolKit.getViewIdByPoint(new int[]{ RIDConfigV530.ID_ROUTE_FOOT_BUTTON_X, RIDConfigV530.ID_ROUTE_FOOT_BUTTON_Y });
		recordViewId("public static final int " + RIDConfigV530.ID_ROUTE_FOOT_BUTTON_NAME + " = " + id);
	}
	
	private void test_OneSearch_inner( String city, String key, String vrst ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, key);
		
		mToolKit.waitForRxTxCompleted(1000, 5);
		
		mToolKit.getRecord(0);
		solo.sendKey(Solo.ENTER);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}

	public void test_SingleResult() throws Exception {
		String city = "北京市";
		String keywords = "人民英雄纪念碑";
		String vrst = "附近";
		
		test_OneSearch_inner( city, keywords, vrst );
		
		String strHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_MultiResult() throws Exception {
		String city = "北京市";
		String keywords = "百度";
		String vrst = "列表";
		
		test_OneSearch_inner( city, keywords, vrst );
		
		String strHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_PanSearch() throws Exception {
		String city = "北京市";
		String keywords = "美食";
		String vrst = "选择排序";
		
		test_OneSearch_inner( city, keywords, vrst );
		
//		mToolKit.waitForRxTxCompleted(1000, 5);
//		mToolKit.getRecord(2);
		
		String strHead = "城市,泛需求检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_SurroundSearch() throws Exception {
		String city = "北京市";
		String poi = "北京航空航天大学图书馆";
		String keywords = "中餐";
		String vrst = "选择排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "列表");
		assertTrue(bExist);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*4);
		
		solo.clickOnText("在附近找");
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, keywords);
		
		mToolKit.waitForRxTxCompleted(1000, 5);
		
		mToolKit.getRecord(0);
		mToolKit.clickOnTextExactly("搜索");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
//		mToolKit.waitForRxTxCompleted(1000, 5);
//		mToolKit.getRecord(2);
		
		String strHead = "城市,地点,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + poi + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	
	private void test_RouteSearch_inner( String city, String from, String to, String vrst, int type ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		
		int rsid = RIDConfigV530.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV530.ID_ROUTE_CAR_BUTTON;
			break;
			
		case 2:
			rsid = RIDConfigV530.ID_ROUTE_FOOT_BUTTON;
			break;

		default:
			break;
		}
		
		mToolKit.clickOnViewById(rsid);
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, from);
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(1);
		solo.enterText(1, to);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 5);
		
		mToolKit.getRecord(0);
		solo.clickOnText("搜索");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	public void test_BusRoute() throws Exception {
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "公交方案";
		
		test_RouteSearch_inner(city, from, to, result, 0);

		String strHead = "城市,公交起点,公交终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_NavRoute() throws Exception {
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "列表";
		
		test_RouteSearch_inner(city, from, to, result, 1);

		String strHead = "城市,驾车起点,驾车终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_WalkRoute() throws Exception {
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "列表";
		
		test_RouteSearch_inner(city, from, to, result, 2);

		String strHead = "城市,步行起点,步行终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	
	public void test_ReverseGeoCode() throws Exception {
		String city = "北京市";
		int x = 270, y = 250;
		String vrst = "北京市昌平区回南路";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 5);
		
		mToolKit.getRecord(0);
		
		solo.clickLongOnScreen(x, y);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		String strHead = "城市,反GEO坐标x,反GEO坐标y,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + x + "," + y + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	
	public void test_SuggestionOnline() throws Exception {
		String city = "北京市";
		String keywords = "南";
		String vrst = "南礼士路";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		mToolKit.waitForRxTxCompleted(1000, 5);
		
		mToolKit.getRecord(0);
		
		solo.enterText(0, keywords);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		String strHead = "城市,suggestion输入关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
	}
	
	public void test_exit() throws Exception {
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("退出");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME);
	}
}
