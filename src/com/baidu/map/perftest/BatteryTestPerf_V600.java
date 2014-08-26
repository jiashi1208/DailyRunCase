/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class BatteryTestPerf_V600 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	int PAUSE_TIME = 1000;
	int MIN_PAUSE_TIME = 20;
	final int TIME_OUT = 10000;
	static long start_time = 0, start_rx = 0, start_tx = 0;

	static long end_time = 0, end_rx = 0, end_tx = 0;
	
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
	public BatteryTestPerf_V600() {
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
//		OnSearch();
		solo.clickOnScreen(220, 80);
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
		solo.clickOnScreen(680, 280);
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(130, 430, RIDConfigV600.NAME_LAYER_STAR_BUTTON );
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(370, 430, RIDConfigV600.NAME_LAYER_FLOOT_BUTTON );
		solo.clickOnScreen(680, 280);
		
		mToolKit.getSingleId(680, 200, RIDConfigV600.NAME_ROUTE_BUTTON );
		mToolKit.getSingleId(680, 280, RIDConfigV600.NAME_LAYER_BUTTON );
		
		mToolKit.getSingleId(680, 1060, RIDConfigV600.NAME_ZOOM_OUT_BUTTON );
		mToolKit.getSingleId(680, 1130, RIDConfigV600.NAME_ZOOM_IN_BUTTON );
		
		mToolKit.getSingleId(350, 100, RIDConfigV600.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(250, 100, RIDConfigV600.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV600.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(470, 100, RIDConfigV600.NAME_ROUTE_FOOT_BUTTON );
		mToolKit.getSingleId(50, 240, RIDConfigV600.NAME_ROUTE_CHANGE_BUTTON );
	}
	
	public void test_emptytest() throws Exception {
		
	}

	
	//返回主界面
		public void Back_MapMain(int num) throws Exception{
			for( int i=1; i<=num; i++)
			{
				solo.goBack();
				solo.sleep(PAUSE_TIME);
			}
		}
	public void searchBusRoute() throws Exception{
		
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("中关村", "详情");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(0);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_CHANGE_BUTTON);
		solo.clearEditText(1);
		solo.enterText(0, "奎科科技大厦");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("确认");
		solo.sleep(PAUSE_TIME*2);
		mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_BUS_BUTTON);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("少换乘");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("少步行");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("不坐地铁");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("1");
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("地图");
		solo.sleep(PAUSE_TIME*2);
		Back_MapMain(4);
	}
	
	public void searchBankPlace() throws Exception{
		solo.sleep(PAUSE_TIME*2);
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("西单", "详情");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnText("列表");
		solo.sleep(PAUSE_TIME);
		solo.clickInList(0);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME);
		solo.scrollDown();
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("银行");
		solo.sleep(PAUSE_TIME*3);

		solo.clickInList(1);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_CAR_BUTTON);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		
		solo.clickOnScreen(420, 90);
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("躲避拥堵");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnScreen(420, 90);
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("最短路程");
		solo.sleep(PAUSE_TIME*5);
		solo.clickOnScreen(420, 90);
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("少走高速");
		solo.sleep(PAUSE_TIME*5);
		
		Back_MapMain(2);
	}
	public void searchSurround1() throws Exception{
		
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("海淀区");
		solo.sleep(PAUSE_TIME*3);
		SearchForText("银行", "地图");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnButton("地图");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME);
//		solo.scrollDown();
//		solo.sleep(PAUSE_TIME);
		solo.clickOnText("中餐");
		solo.sleep(PAUSE_TIME*3);

		solo.clickOnText("距离");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("1000米");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnButton("地图");
		solo.sleep(PAUSE_TIME);
		Back_MapMain(3);
	}
	public void searchSurround2() throws Exception{
		
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("奎科科技大厦", "详情");
		solo.sleep(PAUSE_TIME*3);

		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("旅馆");
		solo.sleep(PAUSE_TIME*3);
	
		solo.clickOnText("距离");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("500米");
		solo.sleep(PAUSE_TIME*3);
		solo.clickOnButton("地图");
		solo.sleep(PAUSE_TIME);
	
		Back_MapMain(3);
	}
	public void searchSurround3() throws Exception{
	
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("奥林匹克森林公园", "详情");
		solo.sleep(PAUSE_TIME*3);

		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME);
		
		solo.scrollDown();
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("银行");
		solo.sleep(PAUSE_TIME*3);
		
		solo.clickInList(0);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("路线");
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_BUS_BUTTON);
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnButton("搜索");
		solo.sleep(PAUSE_TIME*5);
		Back_MapMain(2);
		
}
	public void TestBattery() throws Exception{
		testBattery();
		solo.sleep(PAUSE_TIME*2);
		testBattery();
//		solo.sleep(PAUSE_TIME*2);
//		testBattery();
	}
	public void testBattery() throws Exception {
		solo.sleep(PAUSE_TIME*2);
    	jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("西单", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("东单", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("苹果园", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("大望路", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("芍药居", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("西直门", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("东直门", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("崇文门", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("西二旗", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		SearchForText("知春路", "详情");
		solo.sleep(PAUSE_TIME*6);
		Back_MapMain(3);
		
		searchBusRoute();
      	searchBankPlace();
      	mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_BUTTON);
		solo.sleep(PAUSE_TIME*10);
		for( int i=1; i<=5; i++)
		{
			mToolKit.clickOnViewById(RIDConfigV600.ID_ZOOM_OUT_BUTTON);// 放大
			solo.sleep(PAUSE_TIME*10);
		}
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_BUTTON);//510.250 
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_STAR_BUTTON);//点击卫星图
		solo.sleep(PAUSE_TIME*10);
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		for( int i=1; i<=5; i++)
		{
			mToolKit.clickOnViewById(RIDConfigV600.ID_ZOOM_IN_BUTTON);// 缩小
			solo.sleep(PAUSE_TIME*10);
		}
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_FLOOT_BUTTON);//点击平面图
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV600.ID_ROUTE_BUTTON);//关闭路况
		solo.sleep(PAUSE_TIME);
		
		searchSurround1();
		searchSurround2();
		jumpToCity("北京市", 0);
		solo.sleep(PAUSE_TIME);
		SearchForText("趵突泉", "详情");
		solo.sleep(PAUSE_TIME*3);
		Back_MapMain(3);
		searchSurround3();
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
			mToolKit.clickOnViewById(RIDConfigV600.ID_ZOOM_IN_BUTTON);
			solo.sleep(PAUSE_TIME);
		}
	}
	
	// 放大
	public void ZoomOut( int deltaLv ) {
		for( int i = 0; i < deltaLv; i++ ){
			mToolKit.clickOnViewById(RIDConfigV600.ID_ZOOM_OUT_BUTTON);
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
		
		String title = this.getName() + "_" + System.currentTimeMillis() + "_V6_0";
//		String title = this.getName() + "_" + System.currentTimeMillis();
		screenShot( title );
//		solo.takeScreenshot(this.getName() + "_V5_0");
		solo.sleep(PAUSE_TIME*10);
	}

	private void screenShot( String title ){
		solo.clickOnText("个人");
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
	
	public void testCPU_dragMap() throws Exception{
//		String city = "北京";
//		jumpToCity(city, 0);
		SearchForText("北京市");
		solo.sleep(PAUSE_TIME*3);
		
		Zoom( 12, 16 );
		final int testtime = 300000;
		start_time = System.currentTimeMillis();
		
		while( true )
		{
			int iLoop = 10;
			for( int i = 0; i < iLoop; i++ ){
				solo.scrollToSide(Solo.LEFT);
				solo.sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 300, 500, 40);				
				solo.sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			for( int i = 0; i < iLoop; i++ ){
				solo.scrollToSide(Solo.RIGHT);
				solo.sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 500, 300, 40);
				solo.sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
		    }
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
		
		}
	}

}
