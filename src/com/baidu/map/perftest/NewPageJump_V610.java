/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class NewPageJump_V610 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	static int PAUSE_TIME = 1000;
	static int MIN_PAUSE_TIME = 20;
	static String PATH = "/sdcard/MapPerf/baidu/";
	static String PROCESS_NAME = TARGET_PACKAGE_ID;

	static final int TIME_OUT = 10000;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public NewPageJump_V610() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}

	Activity baidu;
	Instrumentation baiduins;
	private Solo solo;
	private ToolKit mToolKit = null;
	boolean bExist = false;
	
	@Override
	protected void setUp() throws Exception {
		m_ulStartTime = System.currentTimeMillis();
		
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setActivityInitialTouchMode(false);
		baiduins = getInstrumentation();
		baidu = getActivity();
		solo = new Solo(baiduins, baidu);
		mToolKit = new ToolKit(solo, baidu, PROCESS_NAME);
		
//		solo.sleep(PAUSE_TIME*3);
//		solo.drag(400, 200, 200, 200, 1);
		solo.sleep(PAUSE_TIME*2);
	}

	/*
	 * 垃圾清理与资源回收
	 * @see android.test.InstrumentationTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		solo.sleep(3000);
		try {
			//			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void OnSearch()
	{
		mToolKit.clickOnViewById(RIDConfigV610.ID_ONESEARCH_BOX);
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
	
	
	public void exit( int itimes ) throws Exception {
		solo.sleep(PAUSE_TIME*3);
		
//		mToolKit.goBack(itimes);
//		
//		solo.pressMenuItem(0);
//		solo.sleep(PAUSE_TIME);
//		solo.clickOnText("确定");
//		solo.sleep(PAUSE_TIME);
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
			mToolKit.clickOnText("个人");
			mToolKit.clickOnText("离线地图");
			mToolKit.clickOnText(strCityName);
			mToolKit.clickOnText("查看地图");
		}else if( 1 == type )
		{
			
		}
	}
	
	
	private final int ONE_SEARCH_BOX_X = 360;		// Mi-2s
	private final int ONE_SEARCH_BOX_Y = 115;
	
//	private final int ONE_SEARCH_BOX_X = 360;		// Mi-1s
//	private final int ONE_SEARCH_BOX_Y = 360;
	
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME*2);
		mToolKit.getSingleId(360, 115, RIDConfigV610.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(260, 100, RIDConfigV610.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV610.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(460, 100, RIDConfigV610.NAME_ROUTE_FOOT_BUTTON );
	}
	
	public void getSpecialId() throws Exception {
		mToolKit.printAllViews();
	}
	
	public void prepare(){
//		mToolKit.clickOnText("附近");
//		solo.sleep(PAUSE_TIME);
//		mToolKit.goBack(1);
//		solo.sleep(PAUSE_TIME*3);
	}

	public void test_exit() throws Exception {
		exit(0);
	}
	
//	final int BEFORE_WAIT_TIME = 65000;
//	final int AFTER_WAIT_TIME = 30000;
	
	final int BEFORE_WAIT_TIME = 0;
	final int AFTER_WAIT_TIME = 3000;
	
	long m_ulStartTime = 0, m_ulEndTime = 0;
	
	public void test_empty() throws Exception{
		
	}
	
	private void myWait( int type ){
		if( 0 == type ){
			mToolKit.sleep( BEFORE_WAIT_TIME - (int)(System.currentTimeMillis() - m_ulStartTime) );
		}else{
			mToolKit.sleep(AFTER_WAIT_TIME);
		}
	}
	
	private void camClick( int viewid ){
		myWait(0);
		mToolKit.clickOnViewById( viewid );
		myWait(1);
	}
	
	private void camClick( String text ){
		myWait(0);
		solo.clickOnText(text);
		myWait(1);
	}
	
	private void camClick( int x, int y ){
		myWait(0);
		solo.clickOnScreen(x, y);
		myWait(1);
	}
	
	private void camClickInList( int line ){
		myWait(0);
		solo.clickInList(line);
		myWait(1);
	}
	
	private void camBack(){
		myWait(0);
		solo.goBack();
		myWait(1);
	}
	
	private void enterRoutePlace( int idx, String text ){
		solo.clickOnEditText(idx);
		
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, text);
		solo.sleep(PAUSE_TIME);
//		solo.sendKey(Solo.ENTER);
		
		solo.clickOnText("搜索");
		solo.sleep(PAUSE_TIME);
	}
	
	private void routeSearch_inner( String from, String to, String vrst, int type ){
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME*2);
		
		int rsid = RIDConfigV610.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV610.ID_ROUTE_CAR_BUTTON;
			break;
			
		case 2:
			rsid = RIDConfigV610.ID_ROUTE_FOOT_BUTTON;
			break;

		default:
			break;
		}
		
		mToolKit.clickOnViewById(rsid);
		solo.sleep(PAUSE_TIME*2);
		
		enterRoutePlace(0, from);
		enterRoutePlace(1, to);
		
//		solo.clickOnText("查看路线");
		bExist = solo.waitForText(vrst);
		assertTrue(bExist);
	}
	
	public void oneSearchBox() throws Exception{
		prepare();
		camClick( RIDConfigV610.ID_ONESEARCH_BOX );
		mToolKit.goBack(1);
	}
	
	public void nearbyIcon() throws Exception{
		mToolKit.clickOnViewById( RIDConfigV610.ID_ONESEARCH_BOX );
		solo.sleep(PAUSE_TIME*2);
		mToolKit.goBack(2);
		solo.sleep(PAUSE_TIME*3);

		camClick("附近");
	}
	
	public void routeIcon() throws Exception{
		prepare();
		camClick("路线");
	}
	
	public void toolsIcon() throws Exception{
		prepare();
		camClick("工具");
	}
	
	public void personalIcon() throws Exception{
		prepare();
		camClick("我的");
	}
	
	public void oneSearchBox_back() throws Exception{
		mToolKit.clickOnViewById(RIDConfigV610.ID_ONESEARCH_BOX);
		solo.sleep(PAUSE_TIME*2);
		mToolKit.goBack(1);
		solo.sleep(PAUSE_TIME*3);
		
		camBack();
	}
	
	public void routeIcon_back() throws Exception{
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME*3);
		
		camBack();
	}
	
	private final int RGC_X = 300;
	private final int RGC_Y = 300;
	private String RGC_VRST = "详情";
	
	private void ReverseGeoClick( int x, int y, String vrst ){
		solo.clickLongOnScreen(x, y);
		solo.sleep(PAUSE_TIME*3);
		
		bExist = solo.waitForText(vrst);
		assertTrue(bExist);
	}
	
	public void poiDetailClose() throws Exception{
		ReverseGeoClick( RGC_X, RGC_Y, RGC_VRST );
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("详情");
		solo.sleep(PAUSE_TIME*3);
		
		camClick("地图");
	}
	
	public void poiDetailOpen() throws Exception{
		ReverseGeoClick( RGC_X, RGC_Y, RGC_VRST );
		solo.sleep(PAUSE_TIME*3);
		
		camClick("详情");
	}
	
	public void busRouteDetail() throws Exception{
		routeSearch_inner( "清华大学医学部", "人民英雄纪念碑", "选项", 0 );
		solo.sleep(PAUSE_TIME*2);

		camClickInList(2);
	}
	
	public void naviDetailOpen() throws Exception{
		routeSearch_inner( "清华大学医学部", "人民英雄纪念碑", "详情", 1 );
		solo.sleep(PAUSE_TIME*3);

		camClick("详情");
	}
	
	public void naviDetailClose() throws Exception{
		routeSearch_inner( "清华大学医学部", "人民英雄纪念碑", "详情", 1 );
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("详情");
		solo.sleep(PAUSE_TIME*3);

		camClick("地图");
	}
}
