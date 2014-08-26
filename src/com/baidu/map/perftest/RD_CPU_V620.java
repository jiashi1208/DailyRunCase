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
public class RD_CPU_V620 extends ActivityInstrumentationTestCase2 {	 
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
	public RD_CPU_V620() {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		mToolKit = new ToolKit(solo, mActivity, PROCESS_NAME);
		
		checkUpdate();
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
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	private void OnSearch()
	{
		mToolKit.clickOnViewById(RIDConfigV620.ID_ONESEARCH_BOX);
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
		solo.clickOnScreen(660, 320);
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(130, 450, RIDConfigV620.NAME_LAYER_STAR_BUTTON );
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(360, 450, RIDConfigV620.NAME_LAYER_FLOOT_BUTTON );
		solo.sleep(PAUSE_TIME);
		mToolKit.getSingleId(590, 450, RIDConfigV620.NAME_LAYER_3D_BUTTON );
		solo.clickOnScreen(660, 320);
		
		mToolKit.getSingleId(660, 240, RIDConfigV620.NAME_ROUTE_BUTTON );
		mToolKit.getSingleId(660, 320, RIDConfigV620.NAME_LAYER_BUTTON );
		
		mToolKit.getSingleId(660, 1020, RIDConfigV620.NAME_ZOOM_OUT_BUTTON );
		mToolKit.getSingleId(660, 1110, RIDConfigV620.NAME_ZOOM_IN_BUTTON );
		
		mToolKit.getSingleId(350, 120, RIDConfigV620.NAME_ONESEARCH_BOX );

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
	static	int sleeptime = 1000; //底图加载等待时间
	static	int drap_sleeptime = 1000; //拖图等待时间
	static int num = 4; //等待时间倍数
	static int starnum=8;
	
	public void ChooseLayer(int type) throws Exception {
		
		int rsid = RIDConfigV620.ID_LAYER_FLOOT_BUTTON;
		switch (type) {
		case 1:
			rsid = RIDConfigV620.ID_LAYER_STAR_BUTTON;
			break;
			
		case 2:
			rsid = RIDConfigV620.ID_LAYER_3D_BUTTON;
			break;

		default:
			break;
		}
		mToolKit.clickOnViewById(RIDConfigV620.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(rsid);
		solo.sleep(PAUSE_TIME);
		mToolKit.clickOnViewById(RIDConfigV620.ID_LAYER_BUTTON);
		solo.sleep(PAUSE_TIME);
		
	}
	
public void testDragCPU() throws Exception {
		
		solo.sleep(PAUSE_TIME*3);
		SearchForText("北京市","5公里");
//		ChooseLayer(2);
		solo.sleep(sleeptime*10);
		
		Zoom( 9, 13,num);  //栅格图
		dragMap(3,num);      //拖图一周

		Zoom( 13, 9,num);
		Zoom( 9,13,num);
		ChooseLayer(1);//卫星图
		solo.sleep(sleeptime*15);
		dragMap(3,starnum);      //拖图一周

		Zoom( 13, 9,starnum);
		Zoom( 9,13,starnum);
		ChooseLayer(0);//关闭卫星图
//		ChooseLayer(2);
		
		
		Zoom( 13, 15,num);  //房屋图
		dragMap(3,num);      //拖图一周

		Zoom( 15, 16,num);
		Zoom( 16,15,num);
		ChooseLayer(1);//卫星图
		solo.sleep(sleeptime*15);
		dragMap(3,starnum);      //拖图一周

		Zoom( 15, 16,starnum);
		Zoom( 16,15,starnum);
		ChooseLayer(0);//关闭卫星图
//		ChooseLayer(2);
		
		Zoom( 15,9,num);
		
		dragMap(3,num);      //拖图一周

		Zoom( 9, 5,num);
		Zoom( 5, 9,num);
		ChooseLayer(1);//卫星图
		solo.sleep(sleeptime*15);
		dragMap(3,starnum);      //拖图一周

		Zoom( 9, 5,starnum);
		Zoom( 5, 9,starnum);
		ChooseLayer(0);//关闭卫星图
		
			
		
	}
	
	public void Zoom( int fromLv, int toLv,int num ) {
		int deltaLv = toLv - fromLv;
		
		if( deltaLv > 0 ){
			ZoomOut( deltaLv,num);
		}else if( deltaLv < 0 ){
			ZoomIn( 0 - deltaLv,num );
		}
	}
	
	
	// 缩小
	public void ZoomIn( int deltaLv ,int num){
		for( int i = 0; i < deltaLv; i++ ){
//			mToolKit.clickOnViewById(Constant_CPU_V311.ID_ZOOM_IN_BUTTON);
			solo.multiTouchOnScreen(160, 550, 320, 400);
			solo.sleep(sleeptime*num);
		}
	}
	
	// 放大
	public void ZoomOut( int deltaLv,int num ) {
		
		for( int i = 0; i < deltaLv; i++ ){
//			mToolKit.clickOnViewById(Constant_CPU_V311.ID_ZOOM_OUT_BUTTON);
			solo.clickOnScreen(240, 450, 2);
			solo.sleep(sleeptime*num);
		}
	}
	
	
	public void dragMap(int iLoop,int num) throws Exception{

			for( int i = 0; i < iLoop; i++ ){
//				solo.scrollToSide(Solo.LEFT);
				solo.drag(100, 300, 500, 500, 50);
				solo.sleep(drap_sleeptime*num);
				
			}
			
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 300, 500, 50);				
				solo.sleep(drap_sleeptime*num);
				
			}
			
			for( int i = 0; i < iLoop; i++ ){
//				solo.scrollToSide(Solo.RIGHT);
				solo.drag(300, 100, 500, 500, 50);
				solo.sleep(drap_sleeptime*num);
			
			}
		
			for( int i = 0; i < iLoop; i++ ){
				solo.drag(250, 250, 500, 300, 50);
				solo.sleep(drap_sleeptime*num);
			
		    }
	}
	

}
