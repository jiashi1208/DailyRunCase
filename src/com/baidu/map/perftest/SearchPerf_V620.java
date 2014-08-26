/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

//import com.baidu.BaiduMap.Perftest.ToolKit;
import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class SearchPerf_V620 extends ActivityInstrumentationTestCase2 {	 
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

	boolean bExist = false;
	
	final boolean GET_LOG_TIME = false;
	
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
	public SearchPerf_V620() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	private boolean mbHaveSolo = false;
	protected void setUp() throws Exception {
		super.setUp();
		
		try{
			File file = new File( PATH + SEARCHKEY_CFG );
			if( !GET_LOG_TIME )
				file.delete();
			
			if( file.exists() ){
				readConfig();
				file.delete();
				getNetTime();
				
				mbHaveSolo = false;
			}else{
				ToolKit.clearDir(UDC_PATH);
				
				setActivityInitialTouchMode(false);
				mActivity = getActivity();
				solo = new Solo(getInstrumentation(), mActivity);
				mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
				
				mbHaveSolo = true;
				checkUpdate();
				checkGpsOpen();
				checkNearbyRadar();
			}
		}catch( Exception e ){}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void checkUpdate(){
		String strUpdate = "以后再说";
		boolean bUpdate = solo.waitForText(strUpdate,0,5000);
		if( bUpdate )
		{
			solo.sleep(PAUSE_TIME);
			solo.clickOnText(strUpdate);
		}
	}
	
	private void checkGpsOpen(){
		boolean bCheck = solo.waitForText("不再显示此内容", 0, 5000);
		if( bCheck ){
			solo.sleep(PAUSE_TIME);
			solo.clickOnText("不再显示此内容");
			solo.sleep(PAUSE_TIME*2);
			solo.clickOnText("取消");
			solo.sleep(PAUSE_TIME*2);
		}
	}
	
	private void checkNearbyRadar(){
//		boolean bCheck = solo.waitForText("不再显示此内容", 0, 5000);
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
		}catch(Exception e){
			mstrKey = null;
			mstrCaseName = null;
		}
	}
	
	private void saveConfig( String strKey, String strCaseName ){
		ToolKit.writeToFile(PATH, SEARCHKEY_CFG, strCaseName + "," + strKey + "\n", false);
	}
	
	private void OnSearch()
	{
//		mToolKit.clickOnViewById(RIDConfigV620.ID_ONESEARCH_BOX);
		solo.clickOnText("搜索");
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
		solo.clickOnText("搜索");
		solo.enterText(0, "北京");
		solo.clickOnText("搜索");
		
//		if( 0 == type )
//		{
//			solo.clickOnText("工具");
//			solo.sleep(PAUSE_TIME);
//			
//			solo.clickOnText("离线地图");
//			solo.sleep(PAUSE_TIME);
//			
//			solo.clickOnText(strCityName);
//			solo.sleep(PAUSE_TIME);
//			
//			solo.clickOnText("查看地图");
//			solo.sleep(PAUSE_TIME);
//		}else if( 1 == type )
//		{
//			
//		}
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
	
	private void exit( int iTimes, String strName, String strKey ){
		if( GET_LOG_TIME ){
			solo.sleep(PAUSE_TIME*2);
			mToolKit.goBack(iTimes);
			
	//		solo.pressOnMenu();
	//		solo.sleep(PAUSE_TIME*2);
	//		solo.clickOnText("退出");
	//		solo.sleep(PAUSE_TIME*2);
			
			solo.clickOnText("工具");
			solo.sleep(PAUSE_TIME*2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME*2);
	
			solo.clickOnText("退出百度地图");
			solo.sleep(PAUSE_TIME*2);
			
			ToolKit.writeRecordsToFile( PATH, strName, mstrHead, mstrContent + "," );
			
			saveConfig( strKey, strName );
			solo.clickOnText("确定");
		}else{
//			ToolKit.writeRecordsToFile( PATH, strName, mstrHead, mstrContent + "\n" );
			String strNet = "," + mToolKit.miNetTime + "," + mToolKit.miNetRcvFlax + "," + mToolKit.miNetSndFlax;
			ToolKit.writeRecordsToFile( PATH, strName, mstrHead, mstrContent + strNet + "\n" );
		}
	}
	
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(350, 120, RIDConfigV620.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(260, 100, RIDConfigV620.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV620.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(460, 100, RIDConfigV620.NAME_ROUTE_FOOT_BUTTON );
	}
	
	public void test_emptytest() throws Exception {
		
		solo.sleep(PAUSE_TIME);
		ToolKit.RootCmd("input keyevent 3");
		
	}
	
	private void getNetTime() {
		String strLast = ToolKit.getEngineLogTime( LT_UDC_PATH + LT_UDC_FILE , mstrKey );
		ToolKit.loge(mstrCaseName + "," + strLast);
		ToolKit.writeToFile(PATH, mstrCaseName+".csv", strLast + "\n", true);
	}
	
	public void test_getNetTime() throws Exception {
		getNetTime();
	}
	
	private void test_OneSearch_inner( String city, String key, String vrst ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		solo.enterText(0, key);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.sendKey(Solo.ENTER);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	private String mstrHead = null;
	private String mstrContent = null;

//	private final String strUrl_1 = "http://client.map.baidu.com/?qt=s&c=0&l=12&b=(12936206,4785961;12982288,4864685)&ie=utf-8&tn=wl01&pn=0&rn=10&lc=0&oue=0&lrn=20&extinfo=32&wd=%E4%BA%BA%E6%B0%91%E8%8B%B1%E9%9B%84%E7%BA%AA%E5%BF%B5%E7%A2%91&bt=poi_user_input&da_src=PoiSearchPG.searchBt&route_traffic=1&sug=0&loc=(12948058,4845174)&req=1&version=3&mb=MI%202S&os=Android16&sv=6.1.0&im=imei&imrand=&net=1&resid=01&deviceid=&channel=baidu&screen=%28720%2C1280%29&dpi=%28320%2C320%29&ver=1&cuid=3ADF39E4330C1811BB1A4B224544E9E2%7C071518920213068&ctm=1381851281.815000&rc=0";
	public void test_SingleResult() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "人民英雄纪念碑";
		String vrst = "详情";
		
		test_OneSearch_inner( city, keywords, vrst );

		mstrHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "oneS");
	}
	
	public void test_MultiResult() throws Exception {
		if( !mbHaveSolo )
			return;

		String city = "北京市";
		String keywords = "百度";
		String vrst = "详情";
		
		test_OneSearch_inner( city, keywords, vrst );
		
		mstrHead = "城市,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "oneS");
	}
	
	public void test_PanSearch() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "美食";
		String vrst = "排序";

		test_OneSearch_inner( city, keywords, vrst );

		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.getRecord(2);
		
		mstrHead = "城市,泛需求检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "oneS");
	}
	
	
	public void test_HotelSearch() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "酒店";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME*2);
		
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.clickOnText(keywords);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);


		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.getRecord(2);
		
		mstrHead = "城市,泛需求检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "oneS");
	}
	
	
	public void test_SurroundSearch() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String poi = "北京航空航天大学图书馆";
		String keywords = "中餐";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "搜周边");
		assertTrue(bExist);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("搜周边");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("搜索");
		solo.sleep(PAUSE_TIME);
		//		输入文本
		solo.clearEditText(0);
		solo.enterText(0, keywords);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		solo.clickOnText("搜索");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		mToolKit.getRecord(2);
		
		mstrHead = "城市,地点,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + poi + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "areaS");
	}	
	
	private void enterRoutePlace( int idx, String text ){
		solo.clickOnEditText(idx);
		
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, text);
		solo.sleep(PAUSE_TIME);
//		solo.sendKey(Solo.ENTER);
		
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME);
	}
	
	private void test_RouteSearch_inner( String city, String from, String to, String vrst, int type, int iTimes ){
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, iTimes);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		
		int rsid = RIDConfigV620.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV620.ID_ROUTE_CAR_BUTTON;
			break;
			
		case 2:
			rsid = RIDConfigV620.ID_ROUTE_FOOT_BUTTON;
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
		solo.clickOnText("确定");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	public void test_BusRoute() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "地铁13号线";

		test_RouteSearch_inner(city, from, to, result, 0, 1);

		mstrHead = "城市,公交起点,公交终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(2, this.getName(), "rbS");
	}
	
	public void test_NavRoute() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "详情";

		test_RouteSearch_inner(city, from, to, result, 1, 1);

		mstrHead = "城市,驾车起点,驾车终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(2, this.getName(), "rcS");
	}
	
	public void test_WalkRoute() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String from = "百度大厦";
		String to = "北京航空航天大学";
		String result = "详情";

		test_RouteSearch_inner(city, from, to, result, 2, 1);
		
		mstrHead = "城市,步行起点,步行终点,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + from + "," + to + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(2, this.getName(), "rfS");
	}
	
	
	public void test_ReverseGeoCode() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		int x = 270, y = 250;
//		String vrst = "详情";
		String vrst = "北京市昌平区";
		
		solo.sleep(PAUSE_TIME);
		solo.clickLongOnScreen(x, y);
		solo.sleep(PAUSE_TIME);
		solo.goBack();
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		
		solo.clickLongOnScreen(x, y);
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		
		mstrHead = "城市,反GEO坐标x,反GEO坐标y,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + x + "," + y + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(1, this.getName(), "geoS");
	}
	
	
	public void test_SuggestionOnline() throws Exception {
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "南";
		String vrst = "南锣鼓巷";
//		String vrst = "南菜园";
		
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
		
		mstrHead = "城市,suggestion输入关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(1, this.getName(), "sugS");
	}
	
	public void test_mytest(){
		solo.sleep(PAUSE_TIME*3);
//		solo.clickLongOnScreen(300, 300, 1000);
		solo.clickLongOnText("附近", 0, 10000);
		solo.sleep(PAUSE_TIME*3);
	}
}
