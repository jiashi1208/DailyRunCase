package com.baidu.map.perftest;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
    private WifiManager mWifiManager;   //定义WifiManager对象  
    
    private WifiInfo mWifiInfo; //定义WifiInfo对象  
  
    private List<ScanResult> mWifiList;   //扫描出的网络连接列表  
  
    private List<WifiConfiguration> mWifiConfiguration;   //网络连接列表  
  
    WifiLock mWifiLock; //定义一个WifiLock  
  
    public  WifiAdmin(Context context){  
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);        //取得WifiManager对象  
        mWifiInfo = mWifiManager.getConnectionInfo();       //取得WifiInfo对象  
    }  
    
    //打开WIFI  
    public void openWifi(){  
        if (!mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(true);  
        }  
    }  
    
    //关闭WIFI  
    public void closeWifi(){  
        if (mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(false);   
        }  
    }  
    
    //锁定WifiLock，当下载大文件时需要锁定  
    public void acquireWifiLock(){  
        mWifiLock.acquire();  
    }  
      
    public void releaseWifiLock(){//解锁WifiLock  
        //判断时候锁定  
        if (mWifiLock.isHeld()){  
            mWifiLock.acquire();  
        }  
    }  
      
    public void creatWifiLock(){//创建一个WifiLock  
        mWifiLock = mWifiManager.createWifiLock("Test");  
    }  
  
    public List<WifiConfiguration> getConfiguration(){    //得到配置好的网络  
        return mWifiConfiguration;  
    }  
      
    public void connectConfiguration(int index){//指定配置好的网络进行连接  
        //索引大于配置好的网络索引返回  
        if(index > mWifiConfiguration.size()){  
            return;  
        }  
        
        //连接配置好的指定ID的网络  
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);  
    }  
      
    public void startScan(){  
        mWifiManager.startScan();       //得到扫描结果   
        mWifiList = mWifiManager.getScanResults();   
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();//得到配置好的网络连接  
    }  
      
    public List<ScanResult> getWifiList(){//得到网络列表  
        return mWifiList;  
    }  
      
    public StringBuilder lookUpScan(){//查看扫描结果  
        StringBuilder stringBuilder = new StringBuilder();  
        for (int i = 0; i < mWifiList.size(); i++){  
            stringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");  
            //将ScanResult信息转换成一个字符串包  
            //其中把包括：BSSID、SSID、capabilities、frequency、level  
            stringBuilder.append((mWifiList.get(i)).toString());  
            stringBuilder.append("\n");  
        }  
        return stringBuilder;  
    }  
      
    //得到MAC地址  
    public String getMacAddress()   {  
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();  
    }  
      
    //得到接入点的BSSID  
    public String getBSSID(){  
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();  
    }
      
    //得到IP地址  
    public int getIPAddress(){  
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();  
    }  
      
    //得到连接的ID  
    public int getNetworkId(){
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();  
    }  
    
    //得到WifiInfo的所有信息包  
    public String getWifiInfo(){
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();  
    }  
      
    //添加一个网络并连接  
    public void addNetwork(WifiConfiguration wcg){  
        int wcgID = mWifiManager.addNetwork(wcg);   
        mWifiManager.enableNetwork(wcgID, true);   
    }  
    
    //断开指定ID的网络  
    public void disconnectWifi(int netId){  
        mWifiManager.disableNetwork(netId);  
        mWifiManager.disconnect();
    }
    
    public WifiConfiguration createWifiInfo(String SSID, String Password, int Type)  
    {  
    	WifiConfiguration config = new WifiConfiguration();    
    	config.allowedAuthAlgorithms.clear();  
    	config.allowedGroupCiphers.clear();  
    	config.allowedKeyManagement.clear();  
    	config.allowedPairwiseCiphers.clear();  
    	config.allowedProtocols.clear();  
    	config.SSID = "\"" + SSID + "\"";    
           
    	WifiConfiguration tempConfig = this.isExsits(SSID);            
    	if(tempConfig != null) {   
    		mWifiManager.removeNetwork(tempConfig.networkId);   
    	} 
           
    	if(Type == 1) //WIFICIPHER_NOPASS 
    	{  
    		config.wepKeys[0] = "";  
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
    		config.wepTxKeyIndex = 0;  
    	}  

    	if(Type == 2) //WIFICIPHER_WEP 
    	{  
    		config.hiddenSSID = true; 
    		config.wepKeys[0]= "\""+Password+"\"";  
    		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
    		config.wepTxKeyIndex = 0;  
    	}  
    	
    	if(Type == 3) //WIFICIPHER_WPA 
    	{  
    		config.preSharedKey = "\""+Password+"\"";  
    		config.hiddenSSID = true;    
    		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                          
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
    		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
    		//config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
    		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
    		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
    		config.status = WifiConfiguration.Status.ENABLED;    
    	} 
    	
    	return config;  
    }
    
    private WifiConfiguration isExsits(String SSID)   
    {   
    	List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();   
    	for (WifiConfiguration existingConfig : existingConfigs)    
    	{   
    		if (existingConfig.SSID.equals("\""+SSID+"\""))   
    		{   
    			return existingConfig;   
    		}   
    	}   
    	return null;    
    } 
    
//    public void connectWifi( String name, String ssid, String psw ){
//    	WifiConfiguration wcfconfig = createWifiInfo(SSID, Password, Type);
//    }
}
