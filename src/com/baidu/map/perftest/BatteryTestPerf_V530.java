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
public class BatteryTestPerf_V530 extends ActivityInstrumentationTestCase2 {	 
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
	public BatteryTestPerf_V530() {
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
			solo.sleep(PAUSE_TIME/3);
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
//		OnSearch();
		solo.clickOnText("搜索");
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
		}
		else if( 1 == type )
		{
			
		}
	}
	

	private void recordViewId( String text ){
		ToolKit.loge(text);
	}
	
	public void getAllID() throws Exception {
		int id = -1;
		
		solo.sleep(PAUSE_TIME);
	
		solo.clickOnScreen(680, 280);
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(150, 380, RIDConfigV530.NAME_LAYER_STAR_BUTTON );
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(360, 380, RIDConfigV530.NAME_LAYER_FLOOT_BUTTON );
		solo.clickOnScreen(680, 280);
		
		mToolKit.getSingleId(680, 200, RIDConfigV530.NAME_ROUTE_BUTTON );
		mToolKit.getSingleId(680, 280, RIDConfigV530.NAME_LAYER_BUTTON );
		
		mToolKit.getSingleId(680, 1040, RIDConfigV530.NAME_ZOOM_OUT_BUTTON );
		mToolKit.getSingleId(680, 1120, RIDConfigV530.NAME_ZOOM_IN_BUTTON );
		
		
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
		solo.sleep(PAUSE_TIME);
		
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
	
	//返回主界面
	public void Back_MapMain(int num) throws Exception{
		for( int i=1; i<=num; i++)
		{
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
	}
	
	public void test_emptytest() throws Exception {
		
	}
	
	public void TestBattery() throws Exception{
		testBattery();
		solo.sleep(PAUSE_TIME*2);
		testBattery();
	}
	
	public void testBattery() throws Exception {
		solo.sleep(PAUSE_TIME);
    	jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("西单", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("东单", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("苹果园", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("大望路", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("芍药居", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("西直门", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("东直门", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("崇文门", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("西二旗", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("知春路", "列表");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		
		searchBusRoute();
        searchBankPlace();
		mToolKit.clickOnViewById(RIDConfigV530.ID_ROUTE_BUTTON);
		solo.sleep(PAUSE_TIME*10);
		for( int i=1; i<=5; i++)
		{
			mToolKit.clickOnViewById(RIDConfigV530.ID_ZOOM_OUT_BUTTON);// 放大
			solo.sleep(PAUSE_TIME*10);
		}
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_BUTTON);//510.250 
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_STAR_BUTTON);//点击卫星图
		solo.sleep(PAUSE_TIME*10);
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		for( int i=1; i<=5; i++)
		{
			mToolKit.clickOnViewById(RIDConfigV530.ID_ZOOM_IN_BUTTON);// 缩小
			solo.sleep(PAUSE_TIME*10);
		}
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_FLOOT_BUTTON);//点击平面图
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV530.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV530.ID_ROUTE_BUTTON);//关闭路况
		solo.sleep(PAUSE_TIME);
	
		searchSurround1();
		searchSurround2();
		SearchForText("趵突泉", "列表");
		solo.sleep(PAUSE_TIME*3);
		Back_MapMain(3);
		searchSurround3();
	}
	
	public void searchBusRoute() throws Exception{
		
		jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("中关村", "列表");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("从这出发");
		
		solo.clearEditText(1);
		solo.enterText(1, "奎科科技大厦");
		solo.sleep(PAUSE_TIME*2);
		mToolKit.clickOnViewById(RIDConfigV530.ID_ROUTE_BUS_BUTTON);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("少换乘");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("少步行");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("不坐地铁");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("01");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("查看地图");
		solo.sleep(PAUSE_TIME*2);
		
		Back_MapMain(3);
	}
	
public void searchBankPlace() throws Exception{
		
		jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("西单", "列表");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("在附近找");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("银行");
		solo.sleep(PAUSE_TIME*3);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("到这里去");
		solo.sleep(PAUSE_TIME*2);
		
		mToolKit.clickOnViewById(RIDConfigV530.ID_ROUTE_CAR_BUTTON);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnButton("列表");
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("最短距离");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("不走高速");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnButton("地图");
		solo.sleep(PAUSE_TIME*2);
		
		Back_MapMain(3);
	}
	public void searchSurround1() throws Exception{
		
		jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("海淀区", "5公里");
		solo.sleep(PAUSE_TIME*3);
		SearchForText("餐馆", "地图");
		solo.sleep(PAUSE_TIME*3);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("在附近找");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("银行");
		solo.sleep(PAUSE_TIME*3);
		
		solo.clickOnText("范围筛选");
		solo.sleep(PAUSE_TIME/2);
		solo.clickOnText("1000米");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnText("地图");
		solo.sleep(PAUSE_TIME*2);
		Back_MapMain(3);
	}
	public void searchSurround2() throws Exception{
		
		jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("奎科大厦", "列表");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("在附近找");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("公交站");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
			
		solo.clickOnText("范围筛选");
		solo.sleep(PAUSE_TIME/2);
		solo.clickOnText("500米");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnButton("地图");
		solo.sleep(PAUSE_TIME*2);
		Back_MapMain(3);
	}
	
	public void searchSurround3() throws Exception{
	
		jumpToCity("北京", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("上地十街", "列表");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("在附近找");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("银行");
		solo.sleep(PAUSE_TIME*3);
		
		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("到这里去");
		solo.sleep(PAUSE_TIME*2);
		mToolKit.clickOnViewById(RIDConfigV530.ID_ROUTE_FOOT_BUTTON);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		Back_MapMain(3);
		
}
	
	public void test_exit() throws Exception {
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("退出");
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME*2);
	}
	
	public void Test_RSS() throws Exception{
		solo.sleep(PAUSE_TIME);
//		jumpToCity("北京市", 0);
//		solo.sleep(PAUSE_TIME);
//		
//		Zoom( 12, 14);
//		
		int iLoop = 50;
		for( int i = 0; i < iLoop; i++ ){
			solo.scrollToSide(Solo.LEFT);
			solo.sleep(PAUSE_TIME*2);
		}
		
		solo.sleep(PAUSE_TIME/3);
		solo.sleep(PAUSE_TIME);
			
		}
	public void Zoom( int fromLv, int toLv ) {
		int deltaLv = toLv - fromLv;
		
		if( deltaLv > 0 ){
			ZoomOut( deltaLv );
		}else if( deltaLv < 0 ){
			ZoomIn( 0 - deltaLv );
		}
	}
	// 缩小
	public void ZoomIn( int deltaLv ){
		for( int i = 0; i < deltaLv; i++ ){
			mToolKit.clickOnViewById(RIDConfigV530.ID_ZOOM_IN_BUTTON);
			solo.sleep(PAUSE_TIME);
		}
	}
	
	// 放大
	public void ZoomOut( int deltaLv ) {
		for( int i = 0; i < deltaLv; i++ ){
			mToolKit.clickOnViewById(RIDConfigV530.ID_ZOOM_OUT_BUTTON);
			solo.sleep(PAUSE_TIME);
		}
	}
	public void test_dragMap()  throws Exception{
		String city = "北京";
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		Zoom( 12, 19 );
		
		int iLoop = 20;
		for( int i = 0; i < iLoop; i++ ){
			solo.scrollToSide(Solo.LEFT);
			solo.sleep(100);
		}
		
		solo.sleep(PAUSE_TIME/3);
		
		String title = this.getName() + "_" + System.currentTimeMillis() + "_V5_3";
//		String title = this.getName() + "_" + System.currentTimeMillis();
		screenShot( title );
//		solo.takeScreenshot(this.getName() + "_V5_0");
		solo.sleep(PAUSE_TIME*10);
	}
	
	private void screenShot( String title ){
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.scrollDown();
		solo.clickOnText("工具箱");
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("截图");
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.sleep(PAUSE_TIME/3);
		solo.enterText(0, title);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME*2);
	}
}
