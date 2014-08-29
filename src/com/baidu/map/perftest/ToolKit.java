package com.baidu.map.perftest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import com.jayway.android.robotium.solo.Solo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.net.TrafficStats;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ToolKit {
	public int uid = -1;
	public long stime = 0, srx = 0, stx = 0;	//	搜索时间，接收数据量，发送数据量
	public long start_time = 0, start_rx = 0, start_tx = 0;
	public long end_time = 0, end_rx = 0, end_tx = 0;

	public int TIME_OUT = 15000;
	public int PAUSE_TIME = 2000;
	public int MIN_PAUSE_TIME = 20;
	public String PROCESS_NAME = null;
	
	private static Activity mActivity = null;
	private Solo solo = null;
	
	private FlaxMonitorThread fmt = new FlaxMonitorThread();
	
	public ToolKit( Solo _solo, Activity _act, String _procname ){
		if( _solo == null || _act == null || _procname == null )
			return;
		
		solo = _solo;
		mActivity = _act;
		PROCESS_NAME = _procname;
		
		uid = getProcessUid(PROCESS_NAME);
	}
	
	@SuppressWarnings("deprecation")
	public static int getProcessUid(String processName){
		ActivityManager mActivityManager = (ActivityManager)mActivity.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager 
				.getRunningAppProcesses(); 

		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) { 
			if(appProcessInfo.processName.equals(processName)){
//				loge(appProcessInfo.processName + " | " + appProcessInfo.uid);
				//Android 2.2以上版本支持uid
				if(Integer.parseInt(android.os.Build.VERSION.SDK) >= 8){
					return appProcessInfo.uid;
				}
				break;
			}
		}
		
		return -1;
	}
	
	public static void writeRecordsToFile( String strPath, String strName, String strHead, String strContent ){
		try {
			loge(strContent);
			
			File sddir = new File(strPath);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			File sdfile = new File(strPath + strName +".csv");
			if (!sdfile.exists()){
				FileOutputStream fos = new FileOutputStream(strPath + strName +".csv");
				OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
				BufferedWriter bufferedWriter = new BufferedWriter(osw);
				if (bufferedWriter != null) {
					bufferedWriter.write( strHead );
					bufferedWriter.write( strContent );
				}
				bufferedWriter.close();
				osw.close();
				fos.close();
			}else{
				FileOutputStream fos = new FileOutputStream(strPath + strName +".csv",true);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
				BufferedWriter bufferedWriter = new BufferedWriter(osw);
				if (bufferedWriter != null) {
					bufferedWriter.append( strContent );
				}
				bufferedWriter.close();
				osw.close();
				fos.close();
			}
		} catch (Exception e) {
		}
	}
	
	public static void writeToFile( String strPath, String strFile, String strContent, boolean bAppend ){
		try {
			if( strPath == null || strFile == null || strContent == null )
				return;
			
			File sddir = new File(strPath);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			File sdfile = new File(strPath + strFile);
			if (!sdfile.exists()){
				FileOutputStream fos = new FileOutputStream(strPath + strFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
				BufferedWriter bufferedWriter = new BufferedWriter(osw);
				if (bufferedWriter != null) {
					bufferedWriter.write( strContent );
				}
				bufferedWriter.close();
				osw.close();
				fos.close();
			}else{
				FileOutputStream fos = null;
				fos = new FileOutputStream(strPath + strFile, bAppend);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
				BufferedWriter bufferedWriter = new BufferedWriter(osw);
				if (bufferedWriter != null) {
					bufferedWriter.append( strContent );
				}
				bufferedWriter.close();
				osw.close();
				fos.close();
			}
		} catch (Exception e) {}
	}
	
	public static String readLineFromFile( String strPath, String strFile, int lineNum ){
		try{
			File file = new File(strPath + strFile);
			if( !file.exists() )
				return null;
			
//			BufferedReader bReader = new BufferedReader(new FileReader(file), "GBK");
			BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			
			for( int i = 0; i < lineNum-1; i++ )
				bReader.readLine();
			
			String strLine = bReader.readLine();
			
			bReader.close();
			return strLine;
		}catch(Exception e){
			return null;
		}
	}
	
	public static int clearCacheFolder(File dir) {         
		int deletedFiles = 0;        
		if (dir!= null && dir.isDirectory()) {            
			try {               
				for (File child:dir.listFiles()) {   
					if (child.isDirectory()) {             
						deletedFiles += clearCacheFolder(child);         
					}   
					if (child.delete()) {                  
						deletedFiles++;          
					}    
				}            
			} catch(Exception e) {      
				e.printStackTrace();   
			}    
		}      
		return deletedFiles;    
	}
	
	public static int clearDir(String strDir) {
		File dir = new File(strDir);
		if( !dir.exists() || !dir.isDirectory() )
			return -1;
		
		return clearCacheFolder(dir);
	}


	public void clickOnViewById( int id, int time ){
		View v = solo.getView(id);
		solo.clickOnView(v);
		if (time > 0) {
			solo.sleep(time);
		}
	}
	
	public void clickOnViewById( int id){
		View v = solo.getView(id);
		solo.clickOnView(v);
	}
	
	public void clickLongOnViewById( int id, int time ){
		View v = solo.getView(id);
		solo.clickLongOnView(v, time);
	}
	
	/**
	 * 
	 * @param string id
	 * @param int time
	 * @throws Exception 
	 */
	
	public boolean clickOnViewById(String id,int time) throws Exception {
		if (id == "") {
			return false;
		}
		try {
			Activity act = solo.getCurrentActivity();
			int mID = act.getResources().getIdentifier(id, "id",
					solo.getCurrentActivity().getPackageName());
			View view = solo.getView(mID);
			solo.clickOnView(view);
		} catch (Exception ex) {
			Log.e("Exception", ex.getMessage());
			throw ex;
		}
		if (time > 0) {
			solo.sleep(time);
		}
		return true;
	}


	public void jumpToCity( String strCityName, int type ){
		
		
		if( 0 == type )
		{
			solo.clickOnText("我的");
			//solo.clickOnText("工具");
			solo.waitForText("离线地图", 1, PAUSE_TIME*2);
			solo.clickOnText("离线地图");
			solo.waitForText(strCityName, 1, PAUSE_TIME*2);
			solo.clickOnText(strCityName);
			solo.waitForText("查看地图", 1, PAUSE_TIME*2);
			solo.clickOnText("查看地图");
			solo.waitForText("路线", 1, PAUSE_TIME*2);
		}else if( 1 == type )
		{
			solo.clickOnText("搜地点");
			solo.waitForText("搜索", 1, PAUSE_TIME*2);
			solo.enterText(0, "北京");
			solo.clickOnText("搜索");
		}
	}
	
	/**	搜索指定文本
	 * @param strKey
	 */
	public void SearchForText2( String strKey ){
		solo.clickOnText("搜索");
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
		solo.enterText(0, strKey);
		solo.sleep(PAUSE_TIME);
		//不能识别 搜索按钮？？ 改用enter
		//solo.sendKey(solo.ENTER);
		clickOnText(0, "搜索", PAUSE_TIME*2);
		solo.sleep(PAUSE_TIME);
	}
	
	
	/**	搜索指定文本,并点击。
	 * @param strKey
	 */
	public void SearchForText( String strKey ){
		//	点击搜索框
		solo.sleep(PAUSE_TIME);//添加SJ
		//solo.clickOnText("搜索"); //sj
		try {
			this.clickOnViewById(RIDConfigV630.ID_SEARCH_HOME_TEXT, PAUSE_TIME*6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solo.sleep(PAUSE_TIME);
		solo.clearEditText(0);
       // inputQuery_SearchBoxPage("搜索"); //为啥不能执行，需要查看下。
		solo.enterText(0, strKey);
		solo.waitForText("搜索", 1, PAUSE_TIME*2);
		clickOnText(0, "搜索", PAUSE_TIME*2);
	}
	
	public boolean SearchForText( String strKey, String vrst ){
		return SearchForText(strKey, vrst, TIME_OUT);
	}
	
	public boolean SearchForText( String strKey, String vrst, int timeout ){
		SearchForText(strKey);
		return solo.waitForText(vrst, 0, timeout);
	}
	
	//返回主界面
	public void BackToMapMain(int num) throws Exception{
			for( int i=1; i<=num; i++)
			{
				solo.goBack();
				solo.sleep(PAUSE_TIME);
			}
	}
	
	/**
	 * 
	 * 返回主界面
	 */
	
	public void BackToMapMain() throws Exception{
		
		for (int count = 0; count < 10; count++) {
			try {
				// 在当前页面获取 "我的" 和 "附近" 的View,若获取不到,会抛出异常,由catch分支处理
				int zoom_int = mActivity.getResources().getIdentifier(
						"zoom_in", "id",
						solo.getCurrentActivity().getPackageName());
				int mine_int = mActivity.getResources().getIdentifier(
						"to_personal", "id",
						solo.getCurrentActivity().getPackageName());
				solo.getView(zoom_int);
				solo.getView(mine_int);
				break;
			} catch (Throwable e) {
				solo.goBack();
				sleep(PAUSE_TIME / 2);
				continue;
			}
		}
	}
	
	public void Sleep(int time) {
		solo.sleep(time);
	}
	
	
	public void waitForRxTxCompleted( int pausetime, int vtimes ){
		long last_rx = 0, last_tx = 0;
		int t = 0;

		uid = getProcessUid(PROCESS_NAME);
		last_rx = TrafficStats.getUidRxBytes(uid);
		last_tx = TrafficStats.getUidTxBytes(uid);
		
		while( true )
		{
			solo.sleep(pausetime);

			end_rx = TrafficStats.getUidRxBytes(uid);
			end_tx = TrafficStats.getUidTxBytes(uid);
			
			if( end_rx == last_rx && end_tx == last_tx )
				t++;
			else
				t = 0;

			if( t >= vtimes )
				return;

			last_rx = end_rx;
			last_tx = end_tx;
		}
	}
	
	public void sleep( int time ){
		if( time <= 0 )
			return;
		
		solo.sleep(time);
	}

	public static void loge( String text ){
		android.util.Log.e("yanyuan", text);
	}
	
	public static void loge( String tag, String text ){
		android.util.Log.e(tag, text);
	}
	
	public void printAllViews() {
		solo.sleep(PAUSE_TIME);
		
		List<View> arrViews = solo.getCurrentViews();
		if( arrViews == null || arrViews.size() == 0 )
			return;
		
		int idx = 0;
		for( View v : arrViews ){
			String viewClass = v.getClass().toString();
			int id = v.getId();
			String text = null;
			
			try{
				TextView tv = (TextView)v;
				text = tv.getText().toString();
			}catch(Exception e){}
			
			loge("idx:" + idx + ", class:" + viewClass + ", id: " + id + " , text:" + text);
			
			idx++;
		}
	}
	
	// left, right, bottom ,top
	private int[] getViewBound( View v ){
		if( v == null )
			return null;
		
		int[] rst = new int[4];
		
		int[] xy = new int[2];			//	left-bottom
		v.getLocationOnScreen(xy);
		final int viewWidth = v.getWidth();
		final int viewHeight = v.getHeight();
		
		rst[0] = xy[0];
		rst[1] = xy[0] + viewWidth;
		rst[2] = xy[1];
		rst[3] = xy[1] + viewHeight;
		
		return rst;
	}
	
	private boolean isPointInView( int[] xy, View v ){
		if( xy == null || v == null )
			return false;
		
		int[] bound = getViewBound(v);
		if( bound == null )
			return false;
		
		if( xy[0] >= bound[0] && xy[0] <= bound[1] && xy[1] >= bound[2] && xy[1] <= bound[3] )
			return true;
		else 
			return false;
	}
	
	private boolean isViewClassMatch( View v ){
		final String[] arrClasses = {"Button","TextView","EditText","ImageView","ImageButton"};
		
		String viewClass = v.getClass().toString();
		for( int i = 0; i < arrClasses.length; i++ ){
			if( viewClass.contains(arrClasses[i]) )
				return true;
		}
		
		return false;
	}
	
	public View getViewByPoint( int x, int y ){
		int[] xy = new int[]{x,y};
		return getViewByPoint(xy);
	}
	
	public View getViewByPoint( int[] xy ){
		List<View> arrViews = solo.getCurrentViews();
		if( arrViews == null ){
//			loge("view not found");
			return null;
		}
		
		for( int i = 0; i < arrViews.size(); i++ ){
			View v = arrViews.get(i);
			if( isViewClassMatch(v) && isPointInView(xy, v) ){
//				loge("view class: " + v.getClass().toString() + ", id: " + v.getId());
				return v;
			}
		}
		
		return null;
	}
	
	public int getViewIdByPoint( int[] xy ){
		View v = getViewByPoint(xy);
		if( v == null )
			return -1;
		
//		loge("view id: " + v.getId() + ", class:" + v.getClass().toString());
		return v.getId();
	}
	
	public int getViewIdByPoint( int x, int y ){
		int[] xy = new int[]{ x, y };
		return getViewIdByPoint(xy);
	}
	
	public int getViewIdByText( String text ){
		if( !solo.waitForText(text, 0, TIME_OUT) ){
//			loge("view not found");
			return -1;
		}
		
		TextView tv = solo.getText(text);
//		loge("view id: " + tv.getId() + ", text:" + tv.getText().toString());
		return tv.getId();
	}
	
	public int getViewIdByClassIndex( Class<?> clazz, int idx ){
		List<View> arrViews = solo.getCurrentViews();
		int pos = -1;
		
		for( View v : arrViews ){
			if( v.getClass() == clazz ){
				pos++;
				if( pos == idx ){
//					loge("view id: " + v.getId() + ", class:" + v.getClass().toString());
					return v.getId();
				}
			}
		}
		
//		loge("view not found");
		return -1;
	}
	
	public float[] getViewCenter( View v ){
		if( v == null )
			return null;
		
		float[] rst = new float[2];
		int[] xy = new int[2];
		v.getLocationOnScreen(xy);
		final int viewWidth = v.getWidth();
		final int viewHeight = v.getHeight();
		
		rst[0] = xy[0] + (viewWidth / 2.0f);
		rst[1] = xy[1] + (viewHeight / 2.0f);
		
		return rst;
	}
	
	public float[] getViewCenterByText( String text ){
		TextView tv = solo.getText(text);
		return getViewCenter(tv);
	}
	
	public boolean checkViewExist( int id ){
		List<View> arrViews = solo.getCurrentViews();
		
		if( arrViews == null || arrViews.size() == 0 ){
//			loge("acitivity has no views");
			return false;
		}
		
		for( View v : arrViews ){
			if( v.getId() == id ){
//				loge("view exists, class:" + v.getClass().toString() + ", id:" + v.getId() );
				return true;
			}
		}
		
//		loge("view not exists");
		return false;
	}
	
	public boolean waitForViewById( int id, int timeout ){
		long tstime = System.currentTimeMillis();
		
		List<View> arrViews = null;
		int iCount = 0;
		while( true ){
			arrViews = solo.getCurrentViews();
			iCount = arrViews.size();
//			loge("solo loop: " + (System.currentTimeMillis()-sstime) );
			
			for( int i = 0; i < iCount; i++ ){
				View v = arrViews.get(i);
				if( v.getId() == id ){
//				if( v.getVisibility() == 1 && v.getId() == id ){
					return true;
				}
			}
			
			if( System.currentTimeMillis() - tstime > timeout )
				return false;
		}
	}
	
	public boolean waitForViewById( int id ) {
		List<View> arrViews = null;
		int iCount = 0;
		while( true ){
			if( solo == null )
				continue;
			
			arrViews = solo.getCurrentViews();
			iCount = arrViews.size();
//			loge("solo loop: " + (System.currentTimeMillis()-sstime) );
			
			for( int i = 0; i < iCount; i++ ){
				View v = arrViews.get(i);
				if( v.getVisibility() == 0 && v.getId() == id ){
					return true;
				}
			}
		}
	}
	
	public boolean waitForViewDisappearById( int id ){
		List<View> arrViews = null;
		int iCount = 0;
		
		while( true ){
			arrViews = solo.getCurrentViews();
			iCount = arrViews.size();
//			loge("solo loop: " + (System.currentTimeMillis()-sstime) );

			if( iCount == 0 )
				continue;
			
			int i = 0;
			for( ; i < iCount; i++ ){
				View v = arrViews.get(i);
				if( v.getId() == id ){
					break;
				}
			}
			
			if( i == iCount )
				return true;
		}
	}
	
	public void clickOnScreen(Point point, int sleep) {
		solo.clickOnScreen(point.x, point.y);
		if (sleep > 0) {
			solo.sleep(sleep);
		}
	}
	
	public int miNetTime = -1;
	public int miNetSndFlax = -1;
	public int miNetRcvFlax = -1;
	public void getRecord( int type ){
		//TODO
		uid = getProcessUid(PROCESS_NAME);
		
		if( type == 0 ){
			start_rx = TrafficStats.getUidRxBytes(uid);
			start_tx = TrafficStats.getUidTxBytes(uid);
			start_time = System.currentTimeMillis();
			
			fmt.startMonitor(uid, start_tx, start_rx);
			fmt.start();
			
//			loge("uid:" + uid + ", srx:" + start_rx + ", stx:" + start_tx);
		}else if( type == 1 ){
			end_time = System.currentTimeMillis();
			end_rx = TrafficStats.getUidRxBytes(uid);
			end_tx = TrafficStats.getUidTxBytes(uid);
			
			stime = end_time - start_time;
			srx = end_rx - start_rx;
			stx = end_tx - start_tx;
			
			fmt.stopMonitor();
//			fmt.analyzeData();
			int[] data = fmt.procData();
			
			miNetTime = data[0];
			miNetSndFlax = data[1];
			miNetRcvFlax = data[2];
//			loge("" + data[0] + "," + data[1] + "," + data[2]);
//			fmt.flushData();
			
//			loge("uid:" + uid + ", erx:" + end_rx + ", etx:" + end_tx);
		}else if( type == 2 ){
			end_rx = TrafficStats.getUidRxBytes(uid);
			end_tx = TrafficStats.getUidTxBytes(uid);
			
			srx = end_rx - start_rx;
			stx = end_tx - start_tx;
		}
	}
	
	public boolean clickOnTextExactly( String text ){
		List<View> arrViews = solo.getCurrentViews();
		for( View v : arrViews ){
			try{
				TextView tv = (TextView)v;
				if( tv.getText().toString().equals(text) ){
					solo.clickOnView(v);
					return true;
				}
			}catch(Exception e){}
		}
		
		return false;
	}
	
	public void goBack( int times ){
		for( int i = 0; i < times; i++ ){
			solo.goBack();
			solo.sleep(PAUSE_TIME);
		}
	}
	
	public void clickOnText( String text ){
		clickOnText( text, true, PAUSE_TIME );
	}
	
	public void clickOnText( String text, int sleeptime ){
		clickOnText( text, true, sleeptime );
	}
	
	public void clickOnText( String text,  boolean scroll, int sleeptime ){
		solo.clickOnText(text, 0, scroll);
		
		if( sleeptime > 0 )
			solo.sleep(sleeptime);
	}
	
	
	public void recordViewId( String name, int id ){
		recordViewId("public static final int " + name + " = " + id + ";");
	}
	
	public void recordViewId( String text ){
		ToolKit.loge(text);
	}
	
	public int getSingleId( int cid, String vtext, String name, int backtimes ){
		clickOnViewById(cid, 0);
		int id = getSingleId( vtext, name );
		goBack(backtimes);
		return id;
	}
	
	public int getSingleId( int cx, int cy, String vtext, String name, int backtimes ){
		solo.clickOnScreen(cx, cy);
		solo.sleep(PAUSE_TIME);
		int id = getSingleId( vtext, name );
		goBack(backtimes);
		return id;
	}
	
	public int getSingleId( int x, int y, String name ){
		int id = getViewIdByPoint(x, y);
		if( id > 0 )
			recordViewId(name, id);
		return id;
	}
	
	public int getSingleId( String text, String name ){
		int id = getViewIdByText(text);
		if( id > 0 )
			recordViewId(name, id);
		return id;
	}
	
	public int getSingleId( String ctext, String vtext, String name, int backtimes ){
		clickOnText(ctext);
		int id = getSingleId( vtext, name );
		goBack(backtimes);
		return id;
	}
	
	public void enterText( int idx, String text ){
		solo.clearEditText(idx);
		solo.enterText(idx, text);
		solo.sleep(PAUSE_TIME);
	}
	
	public static byte[] readBinaryStream( String strFile, int offset ){
		try{
			File sfile = new File(strFile);
			if( !sfile.exists() )
				return null;
			
			int iLen = (int)sfile.length() - offset;
			if( iLen <= 0 )
				return null;
			
			byte[] rst = new byte[iLen];
			DataInputStream dis = new DataInputStream(new FileInputStream(sfile));
			
			for( int i = 0; i < offset; i++ )
				dis.readByte();
			
			dis.read(rst, 0, iLen);
			
			dis.close();
			return rst;
		}catch(Exception e){
			loge("readBinaryStream excep: " + e);
			return null;
		}
	}
	
	public static byte[] decompress(byte[] data) {	
		byte[] output = new byte[0];
		Inflater decompresser = new Inflater();	
		decompresser.reset();
		decompresser.setInput(data);
		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		
		try {
			byte[] buf = new byte[1024];	
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		decompresser.end();
		return output;
	}
	
	
	public static String ungzip( byte[] buffer ){
		try{
			byte[] rst = decompress(buffer);
			String strRst = new String(rst);
			return strRst;
		}catch(Exception e){
			loge("ungzip excep: " + e);
			return null;
		}
	}
	
	public static int miDownloadFlax = 0;
	public static int miUploadFlax = 0;
	public static int miNetTransTime = 0;
	
	public static String getLogValue( String strContent, String strKey ){
//		writeAppLog("full log: " + strContent + "\n");
		
		if( strContent == null )
			return "-2,-2,-2";
//		loge(strContent);
		
		String strActKey = "\"act\":\"" + strKey + "\"";
		if( !strContent.contains(strActKey) )
			return "-3,-3,-3";
		
		int iStartPos = strContent.lastIndexOf(strActKey);
		
		int iPos1 = strContent.indexOf("{", iStartPos);
		if( iPos1 < 0 )
			return "-4,-4,-4";
		
		int iPos2 = strContent.indexOf("}", iPos1);
		if( iPos2 < 0 )
			return "-5,-5,-5";
		
		String strSubString = strContent.substring(iPos1+1, iPos2);
//		loge(strSubString);
//		writeAppLog("sub log: " + strSubString + "\n");
		
		String[] arrTmp = strSubString.split(",");
		if( arrTmp.length != 4 )
			return "-6,-6,-6";
		
		String[] arrTtt = null;
		
		arrTtt = arrTmp[0].split(":");
		miDownloadFlax = Integer.parseInt( arrTtt[1] );
		
		arrTtt = arrTmp[1].split(":");
		miUploadFlax = Integer.parseInt( arrTtt[1] );
		
		arrTtt = arrTmp[2].split(":");
		miNetTransTime = Integer.parseInt( arrTtt[1] );
		
		String strRst = "" + miNetTransTime + "," + miDownloadFlax + "," + miUploadFlax;
//		loge(strRst);
		return strRst;
	}

	public static String getEngineLogTime( String strFile, String strKey ){
		try{
//			beginAppLog("/sdcard/mapperf/baidu/");
//			writeAppLog("key: " + strKey + "\n");
			
			byte[] buffer = readBinaryStream(strFile, 8);
			String strLog = ungzip(buffer);
			String strLast = getLogValue(strLog, strKey);
			
//			endAppLog();
			return strLast;
		}catch(Exception e){
//			endAppLog();
			return "-1,-1,-1";
		}
	}
	
	private static Process mSRProc = null;
	private static DataOutputStream mSROutput = null;
    public static boolean RootCmd(String cmd) {
        try {
        	if( mSRProc == null )
        		mSRProc = Runtime.getRuntime().exec("su");
        	
        	if( mSROutput == null )
        		mSROutput = new DataOutputStream(mSRProc.getOutputStream());
        	
        	mSROutput.writeBytes(cmd + "\n");
        	mSROutput.writeBytes("exit\n");
        	mSROutput.flush();
        	
            InputStream inputstream = mSRProc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            // read the ls output
//            String line = "";
//            while ((line = bufferedreader.readLine()) != null) {
                //System.out.println(line);
//            }
            while( bufferedreader.readLine() != null ){
            	
            }
            
        	mSRProc.waitFor();
        } catch (Exception e) {
                return false;
        } finally {
        	try {
        		if (mSROutput != null) {
        			mSROutput.close();
        		}
        		mSRProc.destroy();
        	} catch (Exception e) {}
        }
        
        return true;
    }
    
    public static int httpGetRequest( String strUrl ){
    	try{
    		long st = System.currentTimeMillis();
    		
	        URL url = new URL(strUrl);
	        InputStream in = url.openStream();
	        BufferedReader bin = new BufferedReader(new InputStreamReader(in, "GB2312"));
	        String s = null;
	        while((s=bin.readLine())!=null){
//	            loge(s);
	        }
	        
	        long et = System.currentTimeMillis();
	        bin.close();
	        loge("req time: " + (et-st));
	        return (int)(et-st);
    	}catch(Exception e){
    		return -1;
    	}
    }
    
    public static String getCurrentFormatTime( String strFormat ){
    	Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
    	String strTime = sdf.format(d);
    	return strTime;
    }
    
    public static String getCurrentTime(){
    	return getCurrentFormatTime("yyyyMMddHHmmss");
    }
    
    private static String strAppLogPath = null;
    private static String strAppLogFile = null;
    private static boolean bAppLoging = false;
    public static void beginAppLog( String strPath ){
    	bAppLoging = true;
    	strAppLogPath = strPath;
    	strAppLogFile = "log_" + getCurrentTime() + ".log";
    	
//    	loge("beginAppLog: " + strAppLogPath + ", " + strAppLogFile + ", " + bAppLoging);
    }
    
    public static void endAppLog(){
    	bAppLoging = false;
    }
    
    public static void writeAppLog( String strContent ){
//    	loge("writeAppLog: " + strAppLogPath + ", " + strAppLogFile + ", " + bAppLoging + ", content: " + strContent);
    	if( !bAppLoging )
    		return;
    	
    	writeToFile( strAppLogPath , strAppLogFile, strContent, true );
    }
    
	public static String timeStampToDate( long lTimeStamp ){
		String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS").format(new Date(lTimeStamp));
		return date;
	}
    
    public class FlaxData{
    	public long mlCurrTime = 0;
    	public long mlSndFlax = 0;
    	public long mlRcvFlax = 0;
    	
    	public FlaxData( long lTime, long lSndFlax, long lRcvFlax ){
    		mlCurrTime = lTime;
    		mlSndFlax = lSndFlax;
    		mlRcvFlax = lRcvFlax;
    	}
    }
    
    public class FlaxMonitorThread extends Thread {
    	private boolean mbRunning = true;
    	private int miMonitorUID = 0;
    	private ArrayList<FlaxData> marrFlaxData = null;
    	private long mlInitSndFlax = 0;
    	private long mlInitRcvFlax = 0;
    	
    	private final int INTERTIME = 10;
    	
    	public FlaxMonitorThread(){
    		marrFlaxData = new ArrayList<FlaxData>();
    	}
    	
    	public void startMonitor( int uid, long lInitSndFlax, long lInitRcvFlax ){
    		mbRunning = true;
    		marrFlaxData.clear();
    		mlInitSndFlax = lInitSndFlax;
    		mlInitRcvFlax = lInitRcvFlax;
    		miMonitorUID = uid;
    	}
    	
    	public void stopMonitor(){
    		mbRunning = false;
    	}
    	
    	@Override
    	public void run(){
    		try{
    			while(mbRunning){
    				long lTime = System.currentTimeMillis();
    				long lRcvFlax = TrafficStats.getUidRxBytes(miMonitorUID);
    				long lSndFlax = TrafficStats.getUidTxBytes(miMonitorUID);
    				marrFlaxData.add( new FlaxData(lTime, lSndFlax-mlInitSndFlax, lRcvFlax-mlInitRcvFlax) );
    				
    				Thread.sleep(INTERTIME);
    			}
    		}catch(Exception e){}
    	}
    	
    	public void flushData( String strPath, String strFile ){
    		try{
    			File file = new File(strPath + strFile);
    			if( !file.exists() )
    				return;
    		}catch(Exception e){}
    	}
    	
    	public void analyzeData(){
    		ArrayList<FlaxData> arrTmp = new ArrayList<FlaxData>();
    		
    		long lTime = marrFlaxData.get(0).mlCurrTime;
    		long lLastSndFlax = marrFlaxData.get(0).mlSndFlax;
    		long lLastRcvFlax = marrFlaxData.get(0).mlRcvFlax;
    		arrTmp.add( new FlaxData(lTime, lLastSndFlax, lLastRcvFlax) );
    		
    		int iSize = marrFlaxData.size();
    		for( int i = 1; i < iSize; i++ ){
    			FlaxData tmpData = marrFlaxData.get(i);
    			if( tmpData.mlSndFlax == lLastSndFlax && tmpData.mlRcvFlax == lLastRcvFlax )
    				continue;
    			
    			lTime = tmpData.mlCurrTime;
    			lLastSndFlax = tmpData.mlSndFlax;
    			lLastRcvFlax = tmpData.mlRcvFlax;
    			arrTmp.add( new FlaxData(lTime, lLastSndFlax, lLastRcvFlax) );
    		}
    		
			lTime = marrFlaxData.get(iSize-1).mlCurrTime;
			lLastSndFlax = marrFlaxData.get(iSize-1).mlSndFlax;
			lLastRcvFlax = marrFlaxData.get(iSize-1).mlRcvFlax;
			arrTmp.add( new FlaxData(lTime, lLastSndFlax, lLastRcvFlax) );
    		
    		for( int i = 0; i < arrTmp.size(); i++ ){
    			FlaxData tmp = arrTmp.get(i);
    			loge("[ana] " + timeStampToDate(tmp.mlCurrTime) + ", rcv:" + tmp.mlRcvFlax + ", snd:" + tmp.mlSndFlax);
    		}
    	}

    	public int[] procData(){
    		int[] data = new int[3];

    		int iStartPos = -1, iEndPos = -1;
    		long lMaxSndFlax = -1, lMaxRcvFlax = -1;
    		long lInitSndFlax = marrFlaxData.get(0).mlSndFlax;
    		long lInitRcvFlax = marrFlaxData.get(0).mlRcvFlax;

    		for( int i = 1; i < marrFlaxData.size(); i++ ){
    			FlaxData tmp = marrFlaxData.get(i);
//    			loge("[start]" + i + "," + tmp.mlCurrTime + "," + tmp.mlRcvFlax + "," + tmp.mlSndFlax + " | " + iStartPos);
    			lMaxSndFlax = tmp.mlSndFlax;
    			if( tmp.mlSndFlax != lInitSndFlax && iStartPos < 0 )
    				iStartPos = i;
    			
    			if( tmp.mlRcvFlax != lInitRcvFlax )
    				break;
    		}

    		for( int i = marrFlaxData.size() - 1; i >= iStartPos; i-- ){
    			FlaxData tmp = marrFlaxData.get(i);
//    			loge("[end]" + i + "," + tmp.mlCurrTime + "," + tmp.mlRcvFlax + "," + tmp.mlSndFlax + " | " + iEndPos);
    			
    			if( tmp.mlSndFlax != lMaxSndFlax )
    				continue;
    			else{
    				if( lMaxRcvFlax < 0 )
    					lMaxRcvFlax = tmp.mlRcvFlax;
    				
    				if( tmp.mlRcvFlax != lMaxRcvFlax )
    					break;
    				
    				iEndPos = i;
    			}
    		}
    		
    		if( iStartPos != -1 && iEndPos != -1 ){
//    			loge("pos:"+iStartPos+","+iEndPos);
    			FlaxData tmp1 = marrFlaxData.get(iStartPos);
    			FlaxData tmp2 = marrFlaxData.get(iEndPos);
    			data[0] = (int)(tmp2.mlCurrTime - tmp1.mlCurrTime);
    			data[1] = (int)lMaxSndFlax;
    			data[2] = (int)lMaxRcvFlax;
    		}else{
    			data[0] = -1;
    			data[1] = -1;
    			data[2] = -1;
    		}

    		return data;
    	}
    	
    	public void flushData(){
    		for( int i = 0; i < marrFlaxData.size(); i++ ){
    			FlaxData tmp = marrFlaxData.get(i);
    			loge("[ori] " + timeStampToDate(tmp.mlCurrTime) + ", rcv:" + tmp.mlRcvFlax + ", snd:" + tmp.mlSndFlax);
    		}
    	}
    }
    
    public void clickOnText(int preSleep, String textClicked, int postSleep, Object... pageElement) {
		solo.sleep(preSleep >= 0 ? preSleep : 0);
		solo.clickOnText(textClicked);
		solo.sleep(postSleep >= 0 ? postSleep : 0);
	}
    
    /**
	 *	仅完成搜索框输入对应文字、输入、参数：搜索词（代码实现的时候，需要先点下搜索框）
	 *  暂时不能找到对应ID的View。why?
	 *  清楚搜索框内容，输入内容。
	 */
	public void inputQuery_SearchBoxPage(String query){
		//EditText poiEdit = (EditText) getViewById(EditInPoiTop);
		Activity act = solo.getCurrentActivity();
		int mID = act.getResources().getIdentifier("tv_searchbox_home_text", "id",solo.getCurrentActivity().getPackageName());
		EditText poiEdit =(EditText) solo.getView(mID);
		
		clearEditText(poiEdit);
		enterText(poiEdit, query);
		sleep(PAUSE_TIME);
	}
	
	/**
	 * 清空指定输入框
	 * 
	 * @param editText
	 */
	public void clearEditText(EditText editText) {
		solo.clearEditText(editText);
	}
	
	/**
	 * 输入指定框中输入文本信息
	 * 
	 * @param editText
	 * @param text
	 */
	public void enterText(EditText editText, String text) {
		solo.enterText(editText, text);
	}
	
	/**
	 * 运行错误的时候截屏处理
	 */
	
	public void takeScreen(){
		//String name=System.currentTimeMillis()+"";
		Log.i("shijia","开始截屏");
		solo.takeScreenshot();
	}
    
}
