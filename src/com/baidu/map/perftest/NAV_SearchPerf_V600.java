/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class NAV_SearchPerf_V600 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
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
	public NAV_SearchPerf_V600() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	private void OnSearch()
	{
		mToolKit.clickOnViewById(RIDConfigV600.ID_ONESEARCH_BOX);
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
		solo.clickOnText("搜索");
//		solo.sendKey(Solo.ENTER);
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

	
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(350, 100, RIDConfigV600.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(250, 100, RIDConfigV600.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV600.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(470, 100, RIDConfigV600.NAME_ROUTE_FOOT_BUTTON );
	}
	
	public void test_emptytest() throws Exception {
		
	}
	
	private void test_OneSearch_inner( String city, String key, String vrst ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, key);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.sendKey(Solo.ENTER);
//		solo.clickOnText("搜索");
		
//		bExist = solo.waitForText(vrst);
		bExist = solo.waitForText(vrst, 1, 180000);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	
	private void enterRoutePlace( int idx, String text ){
		solo.clickOnEditText(idx);
		
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, text);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("确认");
		solo.sleep(PAUSE_TIME);
	}
	
	private void test_RouteSearch_inner( String city, String from, String to, String vrst, int type ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		
		int rsid = RIDConfigV600.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV600.ID_ROUTE_CAR_BUTTON;
			break;
			
		case 2:
			rsid = RIDConfigV600.ID_ROUTE_FOOT_BUTTON;
			break;

		default:
			break;
		}
		
		mToolKit.clickOnViewById(rsid);
		solo.sleep(PAUSE_TIME);
		
		enterRoutePlace(0, from);
		enterRoutePlace(1, to);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.clickOnText("搜索");
		
		//bExist = solo.waitForText(vrst);
		bExist = solo.waitForText(vrst, 1, 200000);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	public void routeSearch_inner( String from, String to, String funcName ){
		String city = "北京市";
		String result = "详情";
		
		String tmp = "\n==========================\n";
//		ToolKit.writeToFile( "/sdcard/", "baidutest.txt", tmp, true );
		test_RouteSearch_inner(city, from, to, result, 1);

		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
//		ToolKit.writeToFile( "/sdcard/", "baidutest.txt", strContent, true );
	}
	
	public void test_NavRoute1() throws Exception {
		routeSearch_inner( "奎科科技大厦", "先锋大厦", this.getName() );
	}
	
	public void test_NavRoute2() throws Exception {
		routeSearch_inner( "百度大厦", "上地七街", this.getName() );
	}
	
	public void test_NavRoute3() throws Exception {
		routeSearch_inner( "百度大厦", "西三旗桥东", this.getName() );
	}
	
	public void test_NavRoute4() throws Exception {
		routeSearch_inner( "百度大厦A座", "清华大学西门", this.getName() );
	}
	
	public void test_NavRoute5() throws Exception {
		routeSearch_inner( "百度大厦", "北京理工大学图书馆", this.getName() );
	}
	
	public void test_NavRoute6() throws Exception {
		routeSearch_inner( "百度大厦", "万柳高尔夫俱乐部", this.getName() );
	}
	
	public void test_NavRoute7() throws Exception {
		routeSearch_inner( "百度大厦", "中央财经大学东门", this.getName() );
	}
	
	public void test_NavRoute8() throws Exception {
		routeSearch_inner( "百度大厦", "故宫", this.getName() );
	}
	
	public void test_NavRoute9() throws Exception {
		routeSearch_inner( "百度大厦", "世界公园", this.getName() );
	}
	
	public void test_NavRoute10() throws Exception {
		routeSearch_inner( "百度大厦", "北京野生动物园", this.getName() );
	}
	
	public void test_NavRoute11() throws Exception {
		routeSearch_inner( "北京野生动物园", "密云县", this.getName() );
	}
	
	public void test_NavRoute12() throws Exception {
		routeSearch_inner( "北京野生动物园", "赤城县", this.getName() );
	}
	
	public void test_NavRoute13() throws Exception {
		routeSearch_inner( "北京野生动物园", "沽源县", this.getName() );
	}
	
	public void test_NavRoute14() throws Exception {
		routeSearch_inner("北京野生动物园", "多伦县", this.getName() );
	}
	
	public void test_NavRoute15() throws Exception {
		routeSearch_inner( "北京野生动物园", "宁城县", this.getName() );
	}
	
	public void test_NavRoute16() throws Exception {
		routeSearch_inner( "北京野生动物园", "铁岭市", this.getName() );
	}
	
	public void test_NavRoute17() throws Exception {
		routeSearch_inner( "北京野生动物园", "辽源市", this.getName() );
	}
	
	public void test_NavRoute18() throws Exception {
		routeSearch_inner( "北京野生动物园", "齐齐哈尔市", this.getName() );
	}
	
	public void test_NavRoute19() throws Exception {
		routeSearch_inner( "北京野生动物园", "漠河县", this.getName() );
	}
	
	public void test_NavRoute20() throws Exception {
		routeSearch_inner("漠河县","枣庄市", this.getName() );
	}
	
	public void test_NavRoute21() throws Exception {
		routeSearch_inner( "漠河县","杭州市", this.getName() );
	}
	
	public void test_NavRoute22() throws Exception {
		routeSearch_inner( "漠河县", "福州市", this.getName() );	
	}
	
	public void test_NavRoute23() throws Exception {
		routeSearch_inner( "漠河县", "海口市", this.getName() );	
	}
	
}
