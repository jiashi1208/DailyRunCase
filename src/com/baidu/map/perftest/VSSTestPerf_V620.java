/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class VSSTestPerf_V620 extends ActivityInstrumentationTestCase2 {	 
	private static final String TARGET_PACKAGE_ID = "com.baidu.BaiduMap";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.MapsActivity";
//	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.baidu.baidumaps.WelcomeScreen";
	private static Class<?> launcherActivityClass;

	int PAUSE_TIME = 1000;
	int MIN_PAUSE_TIME = 20;
	final int TIME_OUT = 10000;
	
//	String PATH = "/sdcard/MapPerf/baidu/";
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
	public VSSTestPerf_V620() {
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

	private int ID_ONESEARCH_BOX_INNER = RIDConfigV610.ID_ONESEARCH_BOX;
	private void OnSearch()
	{
//		mToolKit.clickOnViewById(ID_ONESEARCH_BOX_INNER);
		//solo.clickOnText("搜索");
		try {
			mToolKit.clickOnViewById(RIDConfigV630.ID_SEARCH_HOME_TEXT, PAUSE_TIME*6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	
	public void getAllID() throws Exception {
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(350, 100, RIDConfigV600.NAME_ONESEARCH_BOX );
		
	}
	
	public void test_emptytest() throws Exception {
		
	}
	
	private void test_innertest( String ctext, int vid ){
		float[] xy = mToolKit.getViewCenterByText(ctext);
		test_innertest(xy, vid);
	}
	private void test_innertest( float[] xy, int vid ){
		assertFalse(mToolKit.checkViewExist(vid));
				
		solo.clickOnScreen(xy[0], xy[1]);
		
		mToolKit.waitForViewById(vid);
		
	}
	private void test_innertest( int cid, int vid ){
		View v = solo.getView(cid);
		if( v == null )
			return;
		
		test_innertest(v, vid);
	}
	private void test_innertest( View v, int vid ){
		float[] xy = mToolKit.getViewCenter(v);
		test_innertest(xy, vid);
	}
	
	public void empty() throws Exception{
		
	}
	static int num=50;
	
	/**
	 * 
	 * 测试内存泄漏
	 */
	public void test_VmRss() throws Exception{
		solo.sleep(PAUSE_TIME);
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		
		Log.i("test_VmRss", "place_start" + df.format(new Date()));
		vss_place(num);
		Log.i("test_VmRss", "near_start " + df.format(new Date()));
		vss_near(num);
		Log.i("test_VmRss", "search_start" + df.format(new Date()));
		vss_search(num);
		Log.i("test_VmRss", "route_start" + df.format(new Date()));
		vss_route(num);
		
		Log.i("test_VmRss", "toolbox_start" + df.format(new Date()));
		//vss_toolbox(num);	
		
		Log.i("test_VmRss", "store_start" + df.format(new Date()));
		vss_store(num);		
		Log.i("test_VmRss", "myorder_start" + df.format(new Date()));
		//vss_myorder(num);
		Log.i("test_VmRss", "offline_start" + df.format(new Date()));
		vss_offline(num);
		Log.i("test_VmRss", "smshare_start" + df.format(new Date()));
		vss_smshare(num);
		Log.i("test_VmRss", "set_start" + df.format(new Date()));
		vss_set(num);
		Log.i("test_VmRss", "dache_start" + df.format(new Date()));
//		vss_dache(num);
//		Log.i("shijing_start", " " + df.format(new Date()));
//		vss_shijing(num);	
	}
	
	
	
	public void vss_near(int num) throws Exception{
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("附近");
		solo.sleep(PAUSE_TIME);
		
		for ( int i=1; i<=num; i++)
		{
			solo.drag(250, 450, 350, 350,1);	
			solo.sleep(PAUSE_TIME);
			solo.drag(450, 250, 350, 350,1);
			solo.sleep(PAUSE_TIME);
		}
		
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME);
		}
		
		for ( int i=1; i<=num; i++)
		{
			solo.goBack();	
			solo.sleep(PAUSE_TIME);
			solo.clickOnText("附近");
			solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
	}
	
	public void vss_search(int num)throws Exception{
		
		solo.sleep(PAUSE_TIME);
	
		for ( int i=1; i<num; i++)
		{	
			//solo.clickOnText("搜索");
			mToolKit.clickOnViewById(RIDConfigV630.ID_SEARCH_HOME_TEXT, PAUSE_TIME*6);
			solo.sleep(PAUSE_TIME);
			
			solo.goBack();
			solo.sleep(PAUSE_TIME);
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
	}
	
    public void vss_shijing(int num)throws Exception{
		
		solo.sleep(PAUSE_TIME);
	
		mToolKit.SearchForText("西二旗");
		solo.sleep(PAUSE_TIME * 2);
		solo.clickOnText("地铁13");
		solo.sleep(PAUSE_TIME * 2);
		for ( int i=1; i<num; i++)
		{	
			solo.clickOnText("进入全景");
			solo.sleep(PAUSE_TIME * 2);
			
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		solo.goBack();
		solo.sleep(PAUSE_TIME);
	}
    
    public void vss_dache(int num) throws Exception {
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		for ( int i=1; i<=num; i++)
		{
		solo.clickOnText("打车");
		
		solo.sleep(PAUSE_TIME * 2);
		
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
	}
	
	
	public void vss_route(int num) throws Exception{
		
		for ( int i=1; i<=num; i++)
		{
		
			//solo.clickOnText("路线");
			mToolKit.clickOnViewById(RIDConfigV630.ID_ROUTE_BUTTON_1, PAUSE_TIME*4);
			solo.sleep(PAUSE_TIME);
			
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
	}
	
	public void vss_toolbox(int num)throws Exception{
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("工具");
		solo.sleep(PAUSE_TIME);
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
		}
		
		for ( int i=1; i<=num; i++)
		{
			solo.goBack();	
			solo.sleep(PAUSE_TIME);
			solo.clickOnText("工具");
			solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		
	}
	
	public void vss_store(int num)throws Exception{
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("收藏");
		solo.sleep(PAUSE_TIME);
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.clickOnText("路线");
		//mToolKit.clickOnViewById(RIDConfigV630.ID_ROUTE_BUTTON_1, PAUSE_TIME*4);
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		solo.goBack();
		solo.sleep(PAUSE_TIME);
	}
	
	public void vss_myorder(int num)throws Exception{
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		
		for ( int i=1; i<=num; i++)
		{
			solo.clickOnText("我的订单");
			solo.sleep(PAUSE_TIME);
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		
	}
	public void vss_smshare(int num)throws Exception{
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("更多工具");
		solo.sleep(PAUSE_TIME);
		
		for ( int i=1; i<=num; i++)
		{
			solo.clickOnText("位置共享");
			solo.sleep(PAUSE_TIME/2);
			solo.goBack();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		
	}

	public void vss_place(int num)throws Exception{

		solo.sleep(PAUSE_TIME);
		
		SearchForText("美食", "地图");
		
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/3);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/3);
			
		}
				
		solo.sleep(PAUSE_TIME);
	
		for( int i=1; i<=num; i++)
		{
//			solo.clickInList(1, 1);
//			solo.get
			solo.clickOnText("人均");
			solo.sleep(PAUSE_TIME*2);
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
	}

	public void vss_offline(int num)throws Exception{
		
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("离线地图");
		solo.sleep(PAUSE_TIME);
		solo.clickOnText("城市列表");
		solo.sleep(PAUSE_TIME/2);
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
//		solo.goBack();
//		solo.sleep(PAUSE_TIME/2);
		solo.clickOnText("更多工具");
		solo.sleep(PAUSE_TIME/2);
		
		solo.clickOnText("导航资源");
		solo.sleep(PAUSE_TIME/2);
		solo.clickOnText("省市列表");
		solo.sleep(PAUSE_TIME/2);
		for ( int i=1; i<=num; i++)
		{
			solo.scrollDown();
			solo.scrollDown();
			solo.sleep(PAUSE_TIME/2);
			solo.scrollUp();
			solo.scrollUp();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
		
		solo.goBack();
		solo.sleep(PAUSE_TIME/2);
	}
	
	public void vss_set(int num)throws Exception{
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME);
		solo.scrollDown();
		solo.sleep(PAUSE_TIME);
		
		
		for ( int i=1; i<=num; i++)
		{
//			solo.clickOnText("设置");
			solo.clickOnScreen(655, 129);
			solo.sleep(PAUSE_TIME/2);
			solo.goBack();
			solo.sleep(PAUSE_TIME/2);
		}
		solo.goBack();
		solo.sleep(PAUSE_TIME);
		
	}

}
