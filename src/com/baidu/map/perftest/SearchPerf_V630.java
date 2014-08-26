/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class SearchPerf_V630 extends ActivityInstrumentationTestCase2 {	
	
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
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
	public static String mstrKey = null;
	public static String mstrCaseName = null;
	public static final String SEARCHKEY_CFG = "searchkey.cfg";
	private String mstrHead = null;
	private String mstrContent = null;
	private boolean mbHaveSolo = false;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public SearchPerf_V630() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	protected void setUp() throws Exception {
		Log.i("shijia","set up");
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
//				checkUpdate();
			}
			if (solo.waitForText("不要福利")) {
				solo.clickOnText("不要福利");
				solo.sleep(PAUSE_TIME);
			}
		}catch( Exception e ){}
		
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/lt/");
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/tm/");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * 获取组件ID
	 * @throws Exception
	 */
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(350, 120, RIDConfigV620.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(260, 100, RIDConfigV620.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV620.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(460, 100, RIDConfigV620.NAME_ROUTE_FOOT_BUTTON );
	}
	
	/**
	 * 空方法，用于使地图正常退出
	 * @throws Exception
	 */
	public void test_emptytest() throws Exception {
		
	}
	
	/**
	 * 获取网络时间
	 * @throws Exception
	 */
	public void test_getNetTime() throws Exception {
		getNetTime();
	}
	
//周边搜索 酒店+美食  集成测试运行，dailyrun不需要，changed by majingting
	/**
	 * 周边搜索（美食） 
	 */
	
	public void test_surroundFood(){
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String poi = "百度大厦";
		String keywords = "美食";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "搜周边");
		assertTrue(bExist);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("搜周边");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("美食");
		solo.sleep(PAUSE_TIME);
		
		bExist = solo.waitForText(vrst);
		assertTrue(bExist);

		mstrHead = "城市,地点,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + poi + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "areaS");
		
	}
	/**
	 * 周边搜索（酒店）
	 */
	public void test_surroundHotel(){
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String poi = "百度大厦";
		String keywords = "酒店";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "搜周边");
		assertTrue(bExist);
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("搜周边");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("酒店");
		solo.sleep(PAUSE_TIME);
		
		bExist = solo.waitForText(vrst);
		assertTrue(bExist);

		mstrHead = "城市,地点,检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + poi + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "areaS");
		
	}
	
	
	
	/**
	 * 单一结果搜索
	 * @throws Exception
	 */
	public void test_SingleResult() throws Exception {
		Log.i("shijia", "单结果搜索");
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
	
	/**
	 * 多结果搜索
	 * @throws Exception
	 */
	public void test_MultiResult() throws Exception {
		Log.i("shijia", "多结果搜索");
		
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
	
	/**
	 * 泛需求搜索
	 * @throws Exception
	 */
	public void test_PanSearch() throws Exception {
		Log.i("shijia", "泛搜索");
		
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "中餐";
		String vrst = "排序";

		test_OneSearch_inner( city, keywords, vrst );

		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.getRecord(2);
		
		mstrHead = "城市,泛需求检索关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		
		exit(3, this.getName(), "oneS");
	}
	
	/**
	 * 周边搜索
	 * @throws Exception
	 */
	public void test_SurroundSearch() throws Exception {
		Log.i("shijia", "周边搜索");
		
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String poi = "北京航空航天大学图书馆";
		String keywords = "酒店";
		String vrst = "排序";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		bExist = SearchForText(poi, "搜周边");
		Log.i("shijia","bExist "+bExist);
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
	
	/**
	 * 公交线路搜索
	 * @throws Exception
	 */
	public void test_BusRoute() throws Exception {
		Log.i("shijia", "公交搜索");
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
	
	/**
	 * 驾车线路搜索
	 * @throws Exception
	 */
	public void test_NavRoute() throws Exception {
		Log.i("shijia", "驾车搜索");
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
	
	/**
	 * 步行线路搜索
	 * @throws Exception
	 */
	public void test_WalkRoute() throws Exception {
		Log.i("shijia", "步行线路搜索");
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
	
	/**
	 * 反geo搜索
	 * @throws Exception
	 */
	public void test_ReverseGeoCode() throws Exception {
		Log.i("shijia", "反Geo搜索");
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		int x = 270 , y = 250;
		String vrst = "北京市";
		
		solo.sleep(PAUSE_TIME);
		solo.clickLongOnScreen(x + 50, y + 50);
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
	
	/**
	 * sug搜索
	 * @throws Exception
	 */
	public void test_SuggestionOnline() throws Exception {
		Log.i("shijia", "sug 搜索");
		if( !mbHaveSolo )
			return;
		
		String city = "北京市";
		String keywords = "南";
		String vrst = "北京南站";
		
		solo.sleep(PAUSE_TIME);
		jumpToCity(city, 0, 0);
		solo.sleep(PAUSE_TIME);
		
		OnSearch();
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		mToolKit.waitForRxTxCompleted(1000, 3);
		
		mToolKit.getRecord(0);
		
		solo.enterText(0, keywords);
		solo.sleep(PAUSE_TIME);
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
		mstrHead = "城市,suggestion输入关键词,搜索响应时间 (ms),接收数据量 (bytes),发送数据量 (bytes)\n";
		mstrContent = city + "," + keywords + "," + mToolKit.stime + "," + mToolKit.srx + "," + mToolKit.stx;
		exit(1, this.getName(), "sugS");
	}
	
	public void test_mytest(){
		solo.sleep(PAUSE_TIME*3);
		solo.clickLongOnText("附近", 0, 10000);
		solo.sleep(PAUSE_TIME*3);
	}
	
	
//##########################以下为私有方法，不是测试case####################################################
//##########################以下为私有方法，不是测试case####################################################
//##########################以下为私有方法，不是测试case####################################################
	
	private void checkUpdate() throws Exception{
		String strUpdate = "以后再说";
		boolean bUpdate = solo.waitForText(strUpdate,0,5000);
		if( bUpdate )
		{
			solo.sleep(PAUSE_TIME);
			solo.clickOnText(strUpdate);
		}
	}

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
		solo.clickOnText("搜索");
	}

	/**	搜索指定文本
	 * @param strKey		搜索关键字
	 */
	private void SearchForText( String strKey ){
		//	点击搜索框
		OnSearch();
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, strKey);
		solo.sleep(PAUSE_TIME);
	//	solo.clickOnText("搜索");
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
		
		
		if( 0 == type )
		{
			solo.clickOnText("我的");
			//solo.clickOnText("工具");
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText("离线地图");
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText(strCityName);
			solo.sleep(PAUSE_TIME);
			
			solo.clickOnText("查看地图");
			solo.sleep(PAUSE_TIME);
		}else if( 1 == type )
		{
			solo.sleep(PAUSE_TIME);
		solo.clickOnText("搜索");
		solo.enterText(0, "北京");
		solo.clickOnText("搜索");
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
	
	private void exit( int iTimes, String strName, String strKey ){
		if( GET_LOG_TIME ){
			solo.sleep(PAUSE_TIME*2);
			mToolKit.goBack(iTimes);
			
			solo.clickOnText("我的");
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
	
	private void getNetTime() {
		String strLast = ToolKit.getEngineLogTime( LT_UDC_PATH + LT_UDC_FILE , mstrKey );
		ToolKit.loge(mstrCaseName + "," + strLast);
		ToolKit.writeToFile(PATH, mstrCaseName+".csv", strLast + "\n", true);
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
		
		Point rsid = new Point();
		rsid.x = 262;
		rsid.y = 105;
		
//		int rsid = RIDConfigV630.ID_ROUTE_BUS_BUTTON;
		switch (type) {
		case 1:
//			rsid = RIDConfigV630.ID_ROUTE_CAR_BUTTON;
			rsid.x = 356;
			rsid.y = 105;
			break;
			
		case 2:
//			rsid = RIDConfigV630.ID_ROUTE_FOOT_BUTTON;
			rsid.x = 453;
			rsid.y = 105;
			break;

		default:
			break;
		}
		
		mToolKit.clickOnScreen(rsid, PAUSE_TIME);
//		solo.clickOnScreen(rsid.x, rsid.y);
//		solo.sleep(PAUSE_TIME);
		
		enterRoutePlace(0, from);
//		enterRoutePlace(1, to);
		solo.clickOnEditText(1);
		
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, to);
		solo.sleep(PAUSE_TIME);
		
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/lt/");
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/tm/");
		mToolKit.waitForRxTxCompleted(1000, 3);
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/lt/");
		mToolKit.clearDir("/data/data/com.baidu.BaiduMap/files/udc/tm/");
		
		mToolKit.getRecord(0);
		solo.clickOnText("确定");
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}
	
	public void prepareForTest() {
		solo.sleep(PAUSE_TIME);
		deleteDialog ();
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("离线地图");
		solo.clickOnText("下载管理");
		
		if (solo.waitForText("北京市", 0, 5000) == false) {
			solo.clickOnText("城市列表");
			solo.sleep(PAUSE_TIME / 2);
			solo.clickOnText("北京市");
			solo.sleep(PAUSE_TIME);
			solo.clickOnText("下载管理");
			solo.sleep(30000);
			if (solo.waitForText("正在下载", 0, 5000)){
				solo.sleep(30000);
			}
		} 
		solo.clickOnText("北京市");
		solo.sleep(PAUSE_TIME / 2);
		solo.clickOnText("查看地图");
		solo.sleep(PAUSE_TIME / 2);

		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		solo.clickOnScreen(356, 105);
//		mToolKit.clickOnScreen(RIDConfigV630.ID_ROUTE_CAR_BUTTON, PAUSE_TIME);
		
		solo.clickOnEditText(0);
		solo.clearEditText(0);
		solo.enterText(0, "奎科科技大厦");
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME);
		solo.clickOnEditText(1);
		solo.clearEditText(0);
		solo.enterText(0, "西二旗地铁站");
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME * 3);
		solo.clickOnScreen(300, 300);
		solo.sleep(PAUSE_TIME * 2);
	}
	
	private void deleteDialog () {
		Map<String, String> dialogs = new HashMap();
		dialogs.put("接受", "百度地图服务条款");//服务条款
		dialogs.put("精确度", "不再显示此内容");//GPS和Wifi
		dialogs.put("立即查看", "以后再说");//离线地图
		dialogs.put("不要福利", "不要福利");//为人民服务
		dialogs.put("添加身边雷达", "默默飘过");//身边雷达
		for(int i = 0; i < (dialogs.size() + 2); i ++) {
			for(String s : dialogs.values()) {
	        	if (solo.waitForText(s, 0, PAUSE_TIME)) {
	        		if (s.equals("不再显示此内容")) {
	        			solo.clickOnText(s);
	        			solo.sleep(PAUSE_TIME / 2);
	        			solo.clickOnText("取消");
	        		} else if (s.equals("百度地图服务条款")) {
	        			solo.clickOnText("接受");
	        			solo.sleep(PAUSE_TIME);
	        			solo.goBack();
	        		} else {
	        			solo.clickOnText(s);
	        		}
	        		solo.sleep(PAUSE_TIME / 2);
	        		break;
	        	}
	        }
		}
        
	}
}
