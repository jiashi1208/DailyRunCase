/**
 * @Author：zhanghongliang
 * @Time：2011-10-26 下午4:59:26
 * @Project：BaiduMapTest
 */
package com.baidu.map.perftest;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class SmsVoice_V600 extends ActivityInstrumentationTestCase2 {	 
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
	public SmsVoice_V600() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	private void versionUpdateProc(){
		String strKey = "以后再说";
		if( solo.waitForText(strKey, 1, 5000) )
			solo.clickOnText(strKey);
		
		solo.sleep(PAUSE_TIME);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
		
		versionUpdateProc();
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

	public final int ID_VOICE_START_BUTTON = 2131034304;
	public final int ID_VOICE_RECORD_BUTTON = 2131034305;
	
	public void getAllID() throws Exception {
		test_Updata_prepare("测试用");
		
		int id = mToolKit.getSingleId(70, 1235, "VoiceButton");
		mToolKit.clickOnViewById(id);
		solo.sleep(PAUSE_TIME*2);
		
		mToolKit.getSingleId(360, 1235, "VoiceRecordButton");
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
		
		bExist = solo.waitForText(vrst);
		mToolKit.getRecord(1);

		assertTrue(bExist);
	}

	public void test_exit() throws Exception {
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME*2);
		solo.scrollDown();
		solo.clickOnText("退出百度地图");
		solo.sleep(PAUSE_TIME*2);
		solo.clickOnText("确定");
		solo.sleep(PAUSE_TIME*2);
	}
	
	private void test_Updata_prepare( String actyName ) {
		solo.sleep(PAUSE_TIME*2);
		
		solo.clickOnText("我的");
		solo.sleep(PAUSE_TIME*2);
		
		solo.scrollDown();
		solo.sleep(PAUSE_TIME);
		
		solo.clickOnText("位置共享");
		solo.waitForText(actyName, 1, 5000);
		solo.sleep(PAUSE_TIME*2);
		
		solo.clickOnText(actyName);
		solo.sleep(PAUSE_TIME*3);
	}
	
	private void test_Updata_inner(int rtime) {
		test_Updata_prepare("测试用");
		
		mToolKit.clickOnViewById(ID_VOICE_START_BUTTON);
		solo.sleep(PAUSE_TIME*2);
		
		View v = solo.getView(ID_VOICE_RECORD_BUTTON);
		assertTrue( v != null );
		
		solo.clickLongOnView( v, rtime*1000-100 );
		
		if( rtime > 45 )
			solo.sleep(PAUSE_TIME*20);
		
		mToolKit.waitForRxTxCompleted(1000, 5);
		solo.sleep(PAUSE_TIME*2);
	}
	
	public void test_Updata_5s() throws Exception {
		test_Updata_inner(5);
	}
	
	public void test_Updata_10s() throws Exception {
		test_Updata_inner(10);
	}
	
	public void test_Updata_30s() throws Exception {
		test_Updata_inner(30);
	}
	
	public void test_Updata_55s() throws Exception {
		test_Updata_inner(55);
	}
}
