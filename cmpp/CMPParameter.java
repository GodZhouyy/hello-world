package com.bettersoft.util.cmpp;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CMPParameter {   
	   
	  /**Դ��ַ���˴�ΪSP_Id����SP����ҵ���롣**/   
	  public static String SP_Id  = "928426";   
	  
	  /** ����ID ��������ʾ��Ŀ���ֻ���**/
	  public static String Service_Id ="10658503";
	   
	  /** ���ڼ���Դ��ַ **/   
	  public static String SP_Pwd = "infox1234";   
	   
	  /**��������ַ **/   
	  public static String ServerIp = "218.206.164.183";   
	   
	  public static String Gateway_ID ="181";   
	   
	  /**MT**/   
	  public static int MTServerPort  = 7890;   
	  /**MO**/   
	  public static int MOServerPort  = 7890;   
	   
	  /**#��·����ʱ��(��)**/   
	  public static int ActiveTestTime = 30;   
	   
	  /**������֧�ֵ���߰汾�ţ�����3.0�İ汾����4bitΪ3����4λΪ0 **/   
	  public static int Version = 33;   
	   
	  /**��������(1˫����,2������) **/   
	  public static int ISMGType = 2;   
	   
	  /**MT�����ٶ�(������������)��λ��/�� **/   
	  public static int MTSpeed = 20;   
	   
	  /*=============================================================================*/   
	  /*           ���²������������ݿ������,ϵͳ��������SPAMaster����                      */   
	  /*=============================================================================*/   
	//  public static String DBDriver    = "com.microsoft.jdbc.sqlserver.SQLServerDriver";    
	//  public static String Connect_URL = "jdbc:microsoft:sqlserver://211.154.52.171:9999;DatabaseName=stock";    
	   
	  /**public static String DBUser      = "smscom";   
	  public static String DBPass      = "smscom";   
	  public static String DB_ResendInterval = "20";   
	  public static int DBPoolSize = 5;   
	  public static int DBType     = 1;  //���ݿ����ͣ�1��ʾoracle��2��ʾsqlserver    
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

