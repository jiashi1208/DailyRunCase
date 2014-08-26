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
public class SearchPerf_V610 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	int PAUSE_TIME = 1000;
	int MIN_PAUSE_TIME = 20;
	final int TIME_OUT = 10000;
	
	String PATH = "/sdcard/MapPerf/baidu/";
	String PROCESS_NAME = TARGET_PACKAGE_ID;
	
	private static final String APP_PATH = "/data/data/com.baidu.BaiduMap/";
	private static final String UDC_PATH = APP_PATH + "files/udc/";
	private static final String LT_UDC_PATH = UDC_PATH + "lt/";
	private static final String LT_UDC_FILE = "crashlog.tmp";
	private static final String LOG_TIME_FILE = "nettime.csv";

	boolean bExist = false;
	
	final boolean GET_LOG_TIME = true;
	
	private Solo solo;
	private ToolKit mToolKit = null;
	private Activity mActivity = null;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public SearchPerf_V610() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	protected void setUp() throws Exception {
		super.setUp();

		if( GET_LOG_TIME ){
			readConfig();
			String strLast = ToolKit.getEngineLogTime( LT_UDC_PATH + LT_UDC_FILE , mstrKey );
			ToolKit.loge(mstrCaseName + "," + strLast);
			
			if( strLast != null )
				ToolKit.writeToFile(PATH, LOG_TIME_FILE, mstrCaseName + "," + strLast + "\n", true);
			else 
				ToolKit.writeToFile(PATH, LOG_TIME_FILE, mstrCaseName + ",-1,-1,-1\n", true);
			
			ToolKit.clearDir(UDC_PATH);
		}
		
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
		
		checkUpdate();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void checkUpdate() throws Exception{
		String strUpdate = "以后再说";
		boolean bUpdate = solo.waitForText(strUpdate,0,5000);
		if( bUpdate )
		{
			solo.sleep(PAUSE_TIME);
			solo.clickOnText(strUpdate);
		}
	}

	public static String mstrKey = null;
	public static String mstrCaseName = null;
	public static final String SEARCHKEY_CFG = "searchkey.cfg";
	
	private void readConfig(){
		try{
			String strTmp = ToolKit.readLineFromFile(PATH, SEARCHKEY_CFG, 1);
			String[] arrTmp = strTmp.split(",");
			
			if( arrTmp.length != 2 ){
				mstrKey = null;
				mstrCaseName = null;
			}else{
				mstrCaseName = arrTmp[0];
				mstrKey = arrTmp[1];
			}
		}catch(Exception e){}
	}
	
	private void saveConfig( String strKey, String strCaseName ){
		ToolKit.writeToFile(PATH, SEARCHKEY_CFG, strCaseName + "," + strKey + "\n", false);
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
			solo.clickOnText("工具");
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

	private void jumpToCity( String strCityName, int type, int iTimes ){
		solo.sleep(PAUSE_TIME);
		
		for( int i = 0; i < iTimes; i++ ){
			solo.clickOnText("路线");
			solo.sleep(PAUSE_TIME*2);
			
			solo.goBack();
			solo.sleep(PAUSE_TIME*2);
		}

		jumpToCity(strCityName, type);
	}
	
	private void exit( int iTimes ){
		solo.sleep(PAUSE_TIME*2);
		mToolKit.goBack(iTimes);
		
		solo.pressOnMenu();
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("退出");
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME*2);
	}
	
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(350, 120, RIDConfigV610.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(260, 100, RIDConfigV610.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV610.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(460, 100, RIDConfigV610.NAME_ROUTE_FOOT_BUTTON );
	}
	
	public void test_emptytest() throws Exception {
		
	}
	
	private void test_OneSearch_inner( String city, String key, String vrst ){
		mstrKey = "oneS";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 2);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, key);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.sendKey(Solo.ENTER);
//		solo.clickOnText("搜索");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}

	public void test_SingleResult() throws Exception {
		String city = "北京市";
		String keywords = "人民英雄纪念碑";
		String vrst = "详情";
		
		test_OneSearch_inner( city, keywords, vrst );

		String strHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(3);
		}
	}
	
	public void test_MultiResult() throws Exception {
		String city = "北京市";
		String keywords = "百度";
		String vrst = "详情";
		
		test_OneSearch_inner( city, keywords, vrst );
		
		String strHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(3);
		}
	}
	
	public void test_PanSearch() throws Exception {
		String city = "北京市";
		String keywords = "美食";
		String vrst = "排序";

		test_OneSearch_inner( city, keywords, vrst );

		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.getRecord(2);
		
		String strHead = "城市,泛需求检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(3);
		}
	}
	
	public void test_SurroundSearch() throws Exception {
		String city = "北京市";
		String poi = "北京航空航天大学图书馆";
		String keywords = "中餐";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "搜周边");
		assertTrue(bExist);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("搜周边");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("输入关键词");
		solo.sleep(PAUSE_TIME);
		//		输入文本
		solo.clearEditText(0);
		solo.enterText(0, keywords);
		solo.sleep(PAUSE_TIME);
		
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.clickOnText("搜索");
//		solo.clickOnText(keywords);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
//		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.getRecord(2);
		
		String strHead = "城市,地点,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + poi + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( "areaS", this.getName() );
			exit(3);
		}
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
	
	private void test_RouteSearch_inner( String city, String from, String to, String vrst, int type ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 1);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		
		mstrKey = "rbS";
		int rsid = RIDConfigV610.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV610.ID_ROUTE_CAR_BUTTON;
			mstrKey = "rcS";
			break;
			
		case 2:
			rsid = RIDConfigV610.ID_ROUTE_FOOT_BUTTON;
			mstrKey = "rfS";
			break;

		default:
			break;
		}
		
		mToolKit.clickOnViewById(rsid);
		solo.sleep(PAUSE_TIME);
		
		enterRoutePlace(0, from);
//		enterRoutePlace(1, to);
		solo.clickOnEditText(1);
		
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, to);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
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
		String result = "地铁13号线";

		test_RouteSearch_inner(city, from, to, result, 0);

		String strHead = "城市,公交起点,公交终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(2);
		}
	}
	
	public void test_NavRoute() throws Exception {
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "详情";

		test_RouteSearch_inner(city, from, to, result, 1);

		String strHead = "城市,驾车起点,驾车终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(2);
		}
	}
	
	public void test_WalkRoute() throws Exception {
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "详情";

		test_RouteSearch_inner(city, from, to, result, 2);
		
		String strHead = "城市,步行起点,步行终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( mstrKey, this.getName() );
			exit(2);
		}
	}
	
	
	public void test_ReverseGeoCode() throws Exception {
		String city = "北京市";
		int x = 270, y = 250;
//		String vrst = "详情";
		String vrst = "北京市昌平区良庄路";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		
		solo.clickLongOnScreen(x, y);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		String strHead = "城市,反GEO坐标x,反GEO坐标y,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + x + "," + y + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( "geoS", this.getName() );
			exit(1);
		}
	}
	
	
	public void test_SuggestionOnline() throws Exception {
		String city = "北京市";
		String keywords = "南";
		String vrst = "南礼士路";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		
		solo.enterText(0, keywords);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		String strHead = "城市,suggestion输入关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		String strContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx + "\n";
		ToolKit.writeRecordsToFile( PATH, this.getName(), strHead, strContent );
		
		if( GET_LOG_TIME ){
			saveConfig( "sugS", this.getName() );
			exit(1);
		}
	}
}
