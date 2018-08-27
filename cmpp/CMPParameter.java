package com.bettersoft.util.cmpp;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CMPParameter {   
	   
	  /**源地址，此处为SP_Id，即SP的企业代码。**/   
	  public static String SP_Id  = "928426";   
	  
	  /** 服务ID ，用于显示在目标手机上**/
	  public static String Service_Id ="10658503";
	   
	  /** 用于鉴别源地址 **/   
	  public static String SP_Pwd = "infox1234";   
	   
	  /**服务器地址 **/   
	  public static String ServerIp = "218.206.164.183";   
	   
	  public static String Gateway_ID ="181";   
	   
	  /**MT**/   
	  public static int MTServerPort  = 7890;   
	  /**MO**/   
	  public static int MOServerPort  = 7890;   
	   
	  /**#链路测试时间(秒)**/   
	  public static int ActiveTestTime = 30;   
	   
	  /**服务器支持的最高版本号，对于3.0的版本，高4bit为3，低4位为0 **/   
	  public static int Version = 33;   
	   
	  /**网关类型(1双连接,2单连接) **/   
	  public static int ISMGType = 2;   
	   
	  /**MT发送速度(用于流量控制)单位条/秒 **/   
	  public static int MTSpeed = 20;   
	   
	  /*=============================================================================*/   
	  /*           以下参数是连接数据库的配置,系统启动后由SPAMaster更改                      */   
	  /*=============================================================================*/   
	//  public static String DBDriver    = "com.microsoft.jdbc.sqlserver.SQLServerDriver";    
	//  public static String Connect_URL = "jdbc:microsoft:sqlserver://211.154.52.171:9999;DatabaseName=stock";    
	   
	  /**public static String DBUser      = "smscom";   
	  public static String DBPass      = "smscom";   
	  public static String DB_ResendInterval = "20";   
	  public static int DBPoolSize = 5;   
	  public static int DBType     = 1;  //数据库类型，1表示oracle，2表示sqlserver    
	  public static String DBDriver = "oracle.jdbc.driver.OracleDriver";   
	  public static String Connect_URL = "jdbc:oracle:thin:@192.168.1.77:1521:smsdb";   */
	   
	   
	  public static String LogFilePath = "";   
	   
	  public static int IfDebug = 1;   
	   
	  public CMPParameter() {   
	  }   
	  public static void loadConfig(String applicationPath)
		{
			try
			{
				String configPath   = applicationPath+"/interfaceConfig.xml";
				SAXReader read = new SAXReader();
				Document doc = read.read(configPath);
				Element root = doc.getRootElement();
				ServerIp = root.element("ismg-ip").getTextTrim();
				MTServerPort = Integer.parseInt(root.element("ismg-port").getTextTrim());
				SP_Id = root.element("sp-id").getTextTrim();
				SP_Pwd = root.element("sp-pwd").getTextTrim();
				Service_Id = root.element("service-id").getTextTrim();
				System.out.println(ServerIp);
				System.out.println(MTServerPort);
				System.out.println(Service_Id);
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
				
			}
		} 
	}   

