/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class BatteryTestPerf_V630 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	int PAUSE_TIME = 1000;
	int MIN_PAUSE_TIME = 20;
	final int TIME_OUT = 10000;
	static long start_time = 0, start_rx = 0, start_tx = 0;
	static long end_time = 0, end_rx = 0, end_tx = 0;
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
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public BatteryTestPerf_V630() {
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
		//super.tearDown();
		Log.i("shijia","tear down");
	}

	/**
	 * 获取组件ID
	 * @throws Exception
	 */
	public void getAllID() throws Exception {
		mToolKit.Sleep(PAUSE_TIME);
		solo.clickOnScreen(660, 320);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.getSingleId(130, 450, RIDConfigV630.NAME_LAYER_STAR_BUTTON );
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.getSingleId(360, 450, RIDConfigV630.NAME_LAYER_FLOOT_BUTTON );
		solo.clickOnScreen(660, 320);
		
		mToolKit.getSingleId(660, 240, RIDConfigV630.NAME_ROUTE_BUTTON );
		mToolKit.getSingleId(660, 320, RIDConfigV630.NAME_LAYER_BUTTON );
		
		mToolKit.getSingleId(660, 1020, RIDConfigV630.NAME_ZOOM_OUT_BUTTON );
		mToolKit.getSingleId(660, 1110, RIDConfigV630.NAME_ZOOM_IN_BUTTON );
		
		mToolKit.getSingleId(350, 120, RIDConfigV630.NAME_ONESEARCH_BOX );

		mToolKit.clickOnText("路线");
		mToolKit.getSingleId(260, 100, RIDConfigV630.NAME_ROUTE_BUS_BUTTON );
		mToolKit.getSingleId(360, 100, RIDConfigV630.NAME_ROUTE_CAR_BUTTON );
		mToolKit.getSingleId(460, 100, RIDConfigV630.NAME_ROUTE_FOOT_BUTTON );
		mToolKit.getSingleId(60, 260, RIDConfigV630.NAME_ROUTE_CHANGE_BUTTON );
	}
	
	/**
	 * 吊起地图，确保地图正常退出
	 * @throws Exception
	 */
	public void test_emptytest() throws Exception {
		
	}
	
	/**
	 * test_SearchBusRoute()
	 * author:shijia
	 * 7.3.0修改了【到这去】 步骤。 必须要选中去bus。
	 */
	public void test_searchBusRoute() throws Exception{
		
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		Log.i("shijia","开始搜索公交线路 搜索中关村");
		
		boolean iSearch=mToolKit.SearchForText("中关村", "到这去");
		/*if(!iSearch){
		 //试验截屏
			mToolKit.takeScreen();
			//assertTrue(iSearch);
		}*/
		
		mToolKit.Sleep(PAUSE_TIME*5);
		//mToolKit.clickOnText( "详情", true, PAUSE_TIME*2);
		mToolKit.clickOnViewById("tv_showdetail", PAUSE_TIME*2);
		mToolKit.clickOnText( "到这去", true, PAUSE_TIME*2);
		mToolKit.Sleep(PAUSE_TIME*4);
		mToolKit.clickOnViewById("iv_topbar_middle_bus",PAUSE_TIME*2);
		mToolKit.clickOnText( "推荐", true, PAUSE_TIME/2);
		Log.i("shijia","开始搜索公交线路  点击 少换乘");
		mToolKit.clickOnText( "少换乘", true, PAUSE_TIME*5);
		mToolKit.clickOnText( "少换乘", true, PAUSE_TIME/2);
		Log.i("shijia","开始搜索公交线路  点击 少步行");
		mToolKit.clickOnText( "少步行", true, PAUSE_TIME*5);
		mToolKit.clickOnText( "少步行", true, PAUSE_TIME/2);
		Log.i("shijia","开始搜索公交线路  点击 不坐地铁");
		mToolKit.clickOnText( "不坐地铁", true, PAUSE_TIME*5);
		mToolKit.clickOnText("不坐地铁", true, PAUSE_TIME/2);
		Log.i("shijia","开始搜索公交线路  点击 推荐2");
		mToolKit.clickOnText("推荐",true, PAUSE_TIME*5);
		//mToolKit.clickOnText( "717", true, PAUSE_TIME*2);
		//mToolKit.clickOnText( "公里", true, PAUSE_TIME*2);
		Log.i("shijia","开始搜索公交线路  点击 到主页");
		mToolKit.BackToMapMain();
	}
	
	/**
	 * test_POISearch1()
	 */
	public void test_POISearch1() throws Exception{
		mToolKit.Sleep(PAUSE_TIME*2);
    	mToolKit.jumpToCity("北京市", 0);	
		mToolKit.Sleep(PAUSE_TIME); 
		mToolKit.SearchForText("西单", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6); 

		mToolKit.BackToMapMain();
		
		mToolKit.SearchForText("东单", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
		mToolKit.BackToMapMain();
		
		mToolKit.SearchForText("苹果园", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
		mToolKit.BackToMapMain();
		mToolKit.SearchForText("大望路", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
		mToolKit.BackToMapMain();
		mToolKit.SearchForText("芍药居", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
		mToolKit.BackToMapMain();
	}
	
	/**
	 * test poisearch2
	 */
	public void test_POISearch2() throws Exception{
		mToolKit.Sleep(PAUSE_TIME*2);
    	mToolKit.jumpToCity("北京市", 0);	
		mToolKit.Sleep(PAUSE_TIME); 
		mToolKit.SearchForText("西直门", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 东直门");
		mToolKit.SearchForText("东直门", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 崇文门");
		mToolKit.SearchForText("崇文门", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 西二旗");
		mToolKit.SearchForText("西二旗", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
        
        Log.i("shijia","开始POI Search2 搜索 知春路");
		mToolKit.SearchForText("知春路", "到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
        mToolKit.BackToMapMain();
	}
	
	/**
	 * test_searchBankPlace
	 */
	public void test_searchBankPlace() throws Exception{
		mToolKit.Sleep(PAUSE_TIME*2);
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("西单", "到这去");
		mToolKit.Sleep(PAUSE_TIME*5);
		
		//mToolKit.clickOnText( "详情", true, PAUSE_TIME*5);
		mToolKit.clickOnViewById("tv_showdetail", PAUSE_TIME*5);
		mToolKit.clickOnText( "搜周边", true, PAUSE_TIME);

		solo.scrollDown();
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.clickOnText( "银行", true, 0);
		//solo.waitForText("范围", 1, PAUSE_TIME*4);
		solo.waitForText("品牌", 1, PAUSE_TIME*10);
		mToolKit.Sleep(PAUSE_TIME*2);
		//solo.clickInList(1);
		solo.clickOnText("到这去",0);
		mToolKit.Sleep(PAUSE_TIME*6);
		//mToolKit.clickOnText( "到这去", true, PAUSE_TIME*6);
		Log.i("shijia","搜索银行 点击 驾车图标");
		mToolKit.clickOnViewById("iv_topbar_middle_car",PAUSE_TIME*6);
		//mToolKit.clickOnText( "选方案", true, PAUSE_TIME);	
		mToolKit.clickOnText( "偏好", true, PAUSE_TIME);
		mToolKit.clickOnText( "高速优先", true, PAUSE_TIME * 3);
		//mToolKit.clickOnText( "搜索", true, PAUSE_TIME*5);
		mToolKit.clickOnText( "确认", true, PAUSE_TIME*5);
		mToolKit.BackToMapMain();
		
	}
	
	/**
	 * 开始搜索周边1
	 */
	public void test_searchSurround1() throws Exception{
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("海淀区");
		mToolKit.Sleep(PAUSE_TIME*3);
		mToolKit.SearchForText("银行", "地图");
		mToolKit.Sleep(PAUSE_TIME*3);	
		mToolKit.clickOnText( "地图", true, PAUSE_TIME*2);
		mToolKit.clickOnText( "搜周边", true, PAUSE_TIME);
		mToolKit.clickOnText( "中餐", true, 0);
		solo.waitForText("范围", 1, 5000); 
		//solo.waitForText("区域", 1, 10); 
		mToolKit.Sleep(PAUSE_TIME*3);
		//mToolKit.clickOnText( "范围", true, PAUSE_TIME);
		/*mToolKit.clickOnText( "区域", true, PAUSE_TIME);
		mToolKit.clickOnText( "1000米", true, PAUSE_TIME*3);*/
		mToolKit.clickOnText( "地图", true, PAUSE_TIME);
		//mToolKit.BackToMapMain(3);	
		mToolKit.BackToMapMain();
	}
	
	
	/**
	 * 搜索周边2
	 * @throws Exception
	 */
	public void test_searchSurround2() throws Exception{
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("奎科科技大厦", "到这去");
		mToolKit.Sleep(PAUSE_TIME*3);
		mToolKit.clickOnText( "搜周边", true, PAUSE_TIME);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.clickOnText( "小吃快餐", true, PAUSE_TIME);
		solo.waitForText("范围", 1, 10);
		mToolKit.Sleep(PAUSE_TIME*3);
		mToolKit.clickOnText( "地图", true, PAUSE_TIME * 2);
		//mToolKit.BackToMapMain(3);
		mToolKit.BackToMapMain();
	}
	
	/**
	 * 搜索周边3
	 * @throws Exception
	 */
	//涉及到webview的自动化。
	public void test_searchSurround3() throws Exception{
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("奥林匹克森林公园", "到这去");
		mToolKit.Sleep(PAUSE_TIME*3);
		
		mToolKit.clickOnText( "搜周边", true, PAUSE_TIME);
		solo.scrollDown();
		mToolKit.Sleep(PAUSE_TIME);
		
		solo.clickOnText("银行");
		solo.waitForText("范围", 1, 3000);
		mToolKit.Sleep(PAUSE_TIME*2);
		
		solo.clickInList(0);
		mToolKit.Sleep(PAUSE_TIME*3);
		solo.clickOnText("到这去");
		mToolKit.Sleep(PAUSE_TIME*6);
		mToolKit.clickOnViewById("iv_topbar_middle_foot",PAUSE_TIME*2);
		//mToolKit.clickOnScreen(RIDConfigV630.ID_ROUTE_FOOT_BUTTON, PAUSE_TIME*2);
		mToolKit.Sleep(PAUSE_TIME*5);
		//mToolKit.BackToMapMain(2);
		mToolKit.BackToMapMain();
	}
	
	/**
	 * 常规操作耗电量
	 * @throws Exception
	 */
	public void TestBattery() throws Exception{
		testBattery();
		mToolKit.Sleep(PAUSE_TIME*2);
		testBattery();
	}
	
	/**
	 * 耗电量操作序列
	 * @throws Exception
	 */
	public void testBattery() throws Exception {
		Log.i("shijia","开始POI Search1");
		test_POISearch1();
		Log.i("shijia","开始POI Search2");
		test_POISearch2();
	    Log.i("shijia","开始搜索公交线路");
	    test_searchBusRoute();
		Log.i("shijia","开始搜索银行");
		test_searchBankPlace();
		Log.i("shijia","开始运行图层");
        testLayer();
		Log.i("shijia","开始搜索附近1");
		test_searchSurround1();
		Log.i("shijia","开始搜索附近2");
		test_searchSurround2();
		Log.i("shijia","开始搜索跨城市");
		test_CrossCity();
		Log.i("shijia","开始搜索附近3");
		//test_searchSurround3();
	}
	
	/**
	 * 测试跨城市
	 * @throws Exception
	 */
	public void test_CrossCity() throws Exception{
		mToolKit.jumpToCity("北京市", 0);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.SearchForText("趵突泉", "到这去");
		mToolKit.Sleep(PAUSE_TIME*3);
		//mToolKit.BackToMapMain(2);
		mToolKit.BackToMapMain();
	}
	
	/**
	 * 测试图层 testLayer()
	 */
	public void testLayer() throws Exception{
			
		mToolKit.clickOnViewById(RIDConfigV630.ID_ROAD_CONDITION_BUTTON_1, PAUSE_TIME*10);
		
		for( int i=1; i<=5; i++)
		{
			solo.clickOnScreen(300, 500, 2);//双击屏幕中间
			mToolKit.Sleep(PAUSE_TIME*10);
		}
		
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_BUTTON_1, PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_SATELLITE_BUTTON_1, PAUSE_TIME*10);
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_BUTTON_1, PAUSE_TIME);
		for( int i=1; i<=5; i++)
		{
			mToolKit.clickOnViewById(RIDConfigV630.ID_ZOOM_IN_BUTTON_1, PAUSE_TIME*10);
		}
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_BUTTON_1, PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_2D_BUTTON_1, PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV630.ID_LAYER_BUTTON_1, PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV630.ID_ROAD_CONDITION_BUTTON_1, PAUSE_TIME);
		
		mToolKit.BackToMapMain();
	}
	
	
	
	/**
	 * 内存测试
	 * @throws Exception
	 */
	public void Test_RSS() throws Exception{
		mToolKit.Sleep(PAUSE_TIME);
		int iLoop = 50;
		for( int i = 0; i < iLoop; i++ ){
			solo.scrollToSide(Solo.LEFT);
			mToolKit.Sleep(PAUSE_TIME*2);
		}
		
		mToolKit.Sleep(PAUSE_TIME/3);
		mToolKit.Sleep(PAUSE_TIME);
			
		}
	
	/**
	 * 托图测试
	 * @throws Exception
	 */
	public void test_dragMap() throws Exception{
		String city = "北京";
		mToolKit.jumpToCity(city, 0);
		mToolKit.Sleep(PAUSE_TIME);
		
		Zoom( 12, 19 );
		int iLoop = 20;
		for( int i = 0; i < iLoop; i++ ){
			solo.scrollToSide(Solo.LEFT);
		}
		
		mToolKit.Sleep(PAUSE_TIME*10);
		String title = this.getName() + "_" + System.currentTimeMillis() + "_V7_4.jpg";
		//screenShot( title );
        String cmd="screencap -p /mnt/sdcard/FPS/"+title;
        
        Log.i("shijia",cmd);
		
		Process p = Runtime.getRuntime().exec(cmd);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line = null;
		String s=null;
		while ((line = in.readLine()) != null) {
			s += line + "/n";
		}
		
		Log.i("shijia",s);
		
		
		//solo.takeScreenshot(title);
		mToolKit.Sleep(PAUSE_TIME*10);
	}

	/**
	 * 托图CPU测试
	 * @throws Exception
	 */
	public void testCPU_dragMap() throws Exception{
		mToolKit.SearchForText("北京市");
		mToolKit.Sleep(PAUSE_TIME*3);
		
		Zoom( 12, 16 );
		final int testtime = 300000;
		start_time = System.currentTimeMillis();
		
		while( true )
		{
			int iLoop = 10;
			for( int i = 0; i < iLoop; i++ ){
				solo.scrollToSide(Solo.LEFT);
				mToolKit.Sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 300, 500, 40);				
				mToolKit.Sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			for( int i = 0; i < iLoop; i++ ){
				solo.scrollToSide(Solo.RIGHT);
				mToolKit.Sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
			}
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 500, 300, 40);
				mToolKit.Sleep(PAUSE_TIME*10);
				end_time = System.currentTimeMillis();			
				if((end_time - start_time) >= testtime)
					break;
		    }
			end_time = System.currentTimeMillis();			
			if((end_time - start_time) >= testtime)
				break;
		
		}
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
	
	public void prepareForTestMI2() throws Exception {
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
		//solo.clickOnText("路线");
	    mToolKit.clickOnViewById(RIDConfigV630.ID_ROUTE_BUTTON_1, PAUSE_TIME*6); //点击路线
		solo.sleep(PAUSE_TIME);

		//solo.clickOnScreen(356, 105);
		mToolKit.clickOnViewById(RIDConfigV630.ID_ROUTE_CAR_BUTTON_1, PAUSE_TIME*6);//点击驾车按钮
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
	
	public void prepareForTestMIONE() {
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
		mToolKit.clickOnScreen(RIDConfigV630.ID_ROUTE_CAR_BUTTON, PAUSE_TIME);
		
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
	
	
//##########################以下为私有方法，不是测试case###############################
//##########################以下为私有方法，不是测试case###############################
//##########################以下为私有方法，不是测试case###############################
	
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
//			mToolKit.clickOnViewById(RIDConfigV630.ID_ZOOM_IN_BUTTON, PAUSE_TIME);
			mToolKit.clickOnScreen(RIDConfigV630.ID_ZOOM_IN_BUTTON, PAUSE_TIME);
		}
	}
	
	// 放大
	public void ZoomOut( int deltaLv ) {
		for( int i = 0; i < deltaLv; i++ ){
//			mToolKit.clickOnViewById(RIDConfigV630.ID_ZOOM_OUT_BUTTON, PAUSE_TIME);
			mToolKit.clickOnScreen(RIDConfigV630.ID_ZOOM_OUT_BUTTON, PAUSE_TIME);
		}
	}
	
    private void screenShot( String title ){
		
		solo.clickOnText("工具");
		mToolKit.Sleep(PAUSE_TIME);
		solo.scrollDown();
		mToolKit.Sleep(PAUSE_TIME);
		solo.clickOnText("截图");
		mToolKit.Sleep(PAUSE_TIME);
		
		solo.clearEditText(0);
		mToolKit.Sleep(PAUSE_TIME/3);
		solo.enterText(0, title);
		mToolKit.Sleep(PAUSE_TIME);
		
		solo.clickOnText("确定");
		mToolKit.Sleep(PAUSE_TIME*2);
	}
    
    public void test_test() throws Exception{
		mToolKit.Sleep(PAUSE_TIME*2);
    	mToolKit.jumpToCity("北京市", 0);	
    	
    	mToolKit.SearchForText("奎科科技大厦", "详情");
		mToolKit.Sleep(PAUSE_TIME*3);
		mToolKit.clickOnText( "搜周边", true, PAUSE_TIME);
		mToolKit.Sleep(PAUSE_TIME);
		mToolKit.BackToMapMain();
	}
    	
	

}
