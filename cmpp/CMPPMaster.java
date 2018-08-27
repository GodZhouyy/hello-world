package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPPMaster</P>   
 * <P>Description: �й��ƶ���������ͨѶ����</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0  
 */  
   
import java.io.InputStream;   
import java.io.FileInputStream;   
import java.util.Properties;   
import java.util.Vector;

import com.bettersoft.admin.UserBean;
        
public class CMPPMaster{   
   
  private String configProperty ="";  
    
  private CMPPMTSocketProcess MTSocketProcess;   
   
  private CMPPMTSendThread MTSendThread;   
   
  private CMPPMTReceiveThread MTReceiveThread;   
  
  private CMPPMOSocketProcess MOSocketProcess;   
  
  private CMPPMOSendThread MOSendThread;   
   
  private CMPPMOReceiveThread MOReceiveThread; 
  
  public static Vector vctMTData = new Vector(1,1); 
  
  public static Vector vctMOData = new Vector(1,1); 
  
  public static Vector vctRespMsg = new Vector(1,1);
  
  private static CMPPMaster master;
 
  public CMPPMaster() {   
   
    //�������ò���    
    //loadInitCfg();   
   
    //������־·��    
    LogUtil.setLogPath(CMPParameter.LogFilePath);   
   
    //��ʼ�����ӳ�    
    //initConnectionPool();   
   
   
  }   
  public static CMPPMaster getInstance(){   
	    
	  if(master == null){   
	    	master = new CMPPMaster();
	    	//String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	    	//System.out.println(""+path);
	    	System.out.println(System.getProperty("user.dir"));//user.dirָ���˵�ǰ��·��
	    	String path = System.getProperty("user.dir");

	        CMPParameter.loadConfig(path+"/WebContent/WEB-INF");
	    	
	    } 
	    return master;   
	  } 
   
  /** ά��MT�߳���**/   
  public void maintenanceMTProcess(){   
    MTSocketProcess = CMPPMTSocketProcess.getInstance();  
    MTSocketProcess.isAvail = false;
    if (MTReceiveThread != null) {   
        CMPPMTReceiveThread.isAvail = false;   
      }   
      if (MTSendThread != null) {   
        CMPPMTSendThread.isAvail = false;   
      }   
      //�ȴ�1��    
      PublicFunction.sleep(1000);            
   
      try {   
        MTSocketProcess.connectSMSG();   
        if (MTSocketProcess.isAvail) {   
          MTReceiveThread = new CMPPMTReceiveThread();   
          MTReceiveThread.start();   
   
          MTSendThread = new CMPPMTSendThread();   
          MTSendThread.start();   
        }   
      }   
      catch (Exception ex) {   
        Debug.outError(ex);   
      }   
  } 
  /** ά��MO�߳���**/   
  public void maintenanceMOProcess(){   
    MOSocketProcess = CMPPMOSocketProcess.getInstance();   
    if(!MOSocketProcess.isAvail){   
      //MOSocketProcess������Чʱ,ֹͣMO���ӵ��շ��߳�    
      if(MOReceiveThread != null){   
        CMPPMOReceiveThread.isAvail = false;   
      }   
      if(MOSendThread != null){   
        CMPPMOSendThread.isAvail = false;   
      }   
   
      //�ȴ�1��    
      PublicFunction.sleep(1000);   
   
      try{   
           
        if(MOSocketProcess.isAvail){   
          MOReceiveThread = new CMPPMOReceiveThread();   
          MOReceiveThread.start();   
   
          MOSendThread = new CMPPMOSendThread();   
          MOSendThread.start();   
        }   
      }   
      catch(Exception ex){   
        Debug.outError(ex);   
      }   
    }   
  }  
  /**  
   * ��ȡ�����ļ�  
   */   
  public void loadInitCfg(){   
    String cfgFile = "";   
    try{ 
    	
      cfgFile = System.getProperties().getProperty(configProperty);   
      cfgFile = cfgFile == null ? "Cmpp.cfg":cfgFile;   
      Debug.outDebug("Config file path:"+cfgFile);   
      InputStream is = new FileInputStream(cfgFile) ;   
      Properties pt = new Properties() ;   
      pt.load(is) ;   
      is.close();
   
      /** ����SOCKETͨѶ���ò��� **/   
      CMPParameter.ServerIp = pt.getProperty("ServerIP", "127.0.0.1");   
      System.out.println("ServerIp: \t" + CMPParameter.ServerIp);   
   
      CMPParameter.MTServerPort = Integer.parseInt(pt.getProperty("MTServerPort", "1020"));   
      System.out.println("MTServerPort: \t" + CMPParameter.MTServerPort);   
      CMPParameter.MOServerPort = Integer.parseInt(pt.getProperty("MOServerPort", "1020"));   
      System.out.println("MOServerPort: \t" + CMPParameter.MOServerPort);   
   
      CMPParameter.SP_Id = pt.getProperty("Gateway_ID", "181");   
      System.out.println("Gateway_ID: \t" + CMPParameter.Gateway_ID);   
   
      CMPParameter.SP_Id = pt.getProperty("SP_Id", "901234");   
      System.out.println("SP_Id: \t" + CMPParameter.SP_Id);   
   
      CMPParameter.SP_Pwd = pt.getProperty("SP_Pwd", "1234");   
      System.out.println("SP_Pwd: \t" + CMPParameter.SP_Pwd);   
   
      CMPParameter.ISMGType = Integer.parseInt(pt.getProperty("ISMGType", "2"));   
      System.out.println("ISMGType: \t" + CMPParameter.ISMGType);   
   
      CMPParameter.Version = Integer.parseInt(pt.getProperty("Version", "2")) * 16;   
      System.out.println("Version: \t" + CMPParameter.Version);   
   
      CMPParameter.ActiveTestTime = Integer.parseInt(pt.getProperty("ActiveTestTime", "30")) * 1000;   
      System.out.println("ActiveTestTime: \t" + CMPParameter.ActiveTestTime);   
      CMPParameter.MTSpeed = Integer.parseInt(pt.getProperty("MTSpeed", "20"));   
      System.out.println("MTSpeed: \t" + CMPParameter.MTSpeed);   
   
      CMPParameter.LogFilePath = pt.getProperty("LogFilePath", "sa");   
      System.out.println("LogFilePath: \t" + CMPParameter.LogFilePath);   
   
      CMPParameter.IfDebug = Integer.parseInt(pt.getProperty("IfDebug", "0"));   
      Debug.outDebug("IfDebug: \t" + CMPParameter.IfDebug);   
   
   
//      CMPParameter.MTPrefixWord = new String(pt.getProperty("MTPrefixWord", "").getBytes("ISO-8859-1"),"GB2312");    
    }   
    catch(Exception ex){   
      Debug.outDebug("[CMPP] loadInitCfg error :" +ex) ;   
    }   
  }   
  public void sendMessage(String message_content,String to_person){
	  
	  master.maintenanceMTProcess();
	  
	  String msgcontent   = message_content;      
	   
      msgcontent.length();   

	  CMPP mtMsg   = new CMPP(CMPP.CMPP_SUBMIT);   
      mtMsg.Dest_terminal_Id = to_person;   
      mtMsg.Dest_terminal_type = 0;   
      mtMsg.Msg_Content = msgcontent;   
      mtMsg.Msg_Length  = (byte)mtMsg.Msg_Content.getBytes().length;   
      //mtMsg.LinkID = rs.getString("LINK_ID");   
    //��Ŀ�������ƷѺ��벻ͬʱ�����ļƷ��û������ֶ�    
      /**if(!mtMsg.Fee_terminal_Id.equals(mtMsg.Dest_terminal_Id))   
        mtMsg.Fee_UserType = 3;  */ 

      Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+   
                    " <MT>DestNumber:" +mtMsg.Dest_terminal_Id + " ServiceID: "+ mtMsg.Service_Id +   
                    " FeeType:"+mtMsg.FeeType +" FeeCode:"+mtMsg.FeeCode + " FeeUserType:"+mtMsg.Fee_UserType +   
                    " FeeUserNumber:"+mtMsg.Fee_terminal_Id +" DestUserType:"+mtMsg.Dest_terminal_type +   
                    " MsgContent:"+ mtMsg.Msg_Content +"  LinkId:"+mtMsg.LinkID+"  SrcNumber:"+mtMsg.Src_terminal_Id);   

      
      synchronized(vctMTData){   
        vctMTData.addElement(mtMsg);   
      } 
      


  }
  /**
   * �ṩ��ȫ��userbean����  �Ա����� 2013-9-17 ��˫
   * @param message_content
   * @param userbean
   */
  public void sendMessage(String message_content,UserBean userbean){
	  
	  master.maintenanceMTProcess();
	  
	  String msgcontent   = message_content;      
	   
      msgcontent.length();   

	  CMPP mtMsg   = new CMPP(CMPP.CMPP_SUBMIT);   
//      mtMsg.Dest_terminal_Id = to_person;   
      mtMsg.Dest_terminal_Id = userbean.getMobile();     
	  mtMsg.Dest_terminal_type = 0;   
	  mtMsg.Msg_Content = msgcontent;   
	  mtMsg.Msg_Length  = (byte)mtMsg.Msg_Content.getBytes().length;   
	  //mtMsg.LinkID = rs.getString("LINK_ID");   
	  //��Ŀ�������ƷѺ��벻ͬʱ�����ļƷ��û������ֶ�    
	  /**if(!mtMsg.Fee_terminal_Id.equals(mtMsg.Dest_terminal_Id))   
        mtMsg.Fee_UserType = 3;  */ 
	  
	  Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+   
			  " <MT>DestNumber:" +mtMsg.Dest_terminal_Id + " ServiceID: "+ mtMsg.Service_Id +   
			  " FeeType:"+mtMsg.FeeType +" FeeCode:"+mtMsg.FeeCode + " FeeUserType:"+mtMsg.Fee_UserType +   
			  " FeeUserNumber:"+mtMsg.Fee_terminal_Id +" DestUserType:"+mtMsg.Dest_terminal_type +   
			  " MsgContent:"+ mtMsg.Msg_Content +"  LinkId:"+mtMsg.LinkID+"  SrcNumber:"+mtMsg.Src_terminal_Id);   
	  
	  
	  synchronized(vctMTData){   
		  vctMTData.addElement(mtMsg);   
	  } 
	  
	  
	  
  }
 
   public void distroy(){
	   MTSocketProcess.disclose();
	   MTReceiveThread.stop();
	   MTSendThread.stop();
   }
  public static void main(String args[]){   
   
    try{      
    	
    	for(int i=0;i<1;i++){
    		CMPPMaster master= CMPPMaster.getInstance();
    		master.sendMessage(""
, "18700963289"); 
    		Thread.sleep(10000);  
     	}
    }   
    catch (Exception ex) {   
      ex.printStackTrace();   
    }     
   
   
  }   
}   

