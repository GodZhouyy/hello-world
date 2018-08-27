package com.bettersoft.util.cmpp;

/** 
 * <P>Title: CMPP Interface</P> 
* <P>Description: 中国移动短信网关通讯程序</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0
 */  
  
import java.io.*;   
  
public class CMPPMOReceiveThread extends Thread{  
  
  /** 该线程运行标志 true 在运行,false停止 **/  
  public static boolean isAvail = false;  
  
  private static DataInputStream  inputStream;  
  
  private static DataOutputStream outputStream;  
  
  
  /**  初始化构造器 
   *   使用CMPPMOSocketProcess唯一的实例的输入流和输出流**/  
  public CMPPMOReceiveThread() {  
    inputStream  = CMPPMOSocketProcess.getInstance().inputStream;  
    outputStream = CMPPMOSocketProcess.getInstance().outputStream;  
    isAvail = true;  
  }  
  
  public void run(){  
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO连接("+CMPParameter.MOServerPort+")->接收线程启动 ...");  
  
    while(CMPPMOSocketProcess.getInstance().isAvail && isAvail){  
      try{ 
    	if(inputStream.available()>0){  
	        byte[] recByte    = PublicFunction.recv(inputStream);  
	        CMPP   deliverMsg = new CMPP();  
	        deliverMsg.parsePack(recByte);  
	  
	        switch(deliverMsg.Command_Id){  
	          case CMPP.CMPP_DELIVER:  
	  
	            //将Deliver消息保存在接收缓冲中,由CMPPMOSocketProcess的CMPPMODataSaveThread   
	            //线程将MO数据保存在数据库中   
	            synchronized(CMPPMaster.vctMOData){  
	            	CMPPMaster.vctMOData.addElement(deliverMsg);  
	            }  
	//            CMPPDBAccess.getInstance().saveMOData(deliverMsg);   
	  
	            //打印接收信息   
	            if(deliverMsg.Registered_Delivery==1){  
	              Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+" <REPORT>SrcNumber:"+deliverMsg.Src_terminal_Id+" Report_Stat:"+deliverMsg.Report_Stat+  
	                            " Report_Dest_terminal_Id:"+deliverMsg.Report_Dest_terminal_Id +"  @"+deliverMsg.Dest_terminal_Id + " Report_ID:"+deliverMsg.Report_Msg_Id);  
	            }  
	            else{  
	              Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+" <MO>SrcNumber:"+deliverMsg.Src_terminal_Id+ " DestNumber:"+ deliverMsg.Dest_terminal_Id+" ServiceID:"  
	                            +deliverMsg.Service_Id + " SrcUserType:"+deliverMsg.Src_terminal_type+" MsgContent:"+deliverMsg.Msg_Content+ " MsgFmt:"+deliverMsg.Msg_Fmt +" LinkId:"  
	                            +deliverMsg.LinkID );  
	            }  
	  
	            //回应Deliver消息   
	            CMPP   deliverRespMsg = new CMPP(CMPP.CMPP_DELIVER_RESP);  
	            deliverRespMsg.Msg_Id = deliverMsg.Msg_Id;  
	            deliverRespMsg.Result = 0;  
	            PublicFunction.send(outputStream, deliverRespMsg.pack());  
	            break;  
	  
	          case CMPP.CMPP_ACTIVE_TEST_RESP:  
	            //链路测试响应,无处理   
	            break;  
	  
	          case CMPP.CMPP_SUBMIT_RESP:  
	            //处理成功下发的响应信息   
	            //CMPPDBAccess.getInstance().dealProccessMsg(deliverMsg);   
	        	  
	            //2005-08-29修改，提高回应消息的保存速度   
	            synchronized(CMPPMaster.vctRespMsg){  
	            	CMPPMaster.vctRespMsg.addElement(deliverMsg);  
	            }  
	  
	            break;  
	        }
        }  
      }  
      catch(Exception ex){  
        destory(ex);  
      }  
  
    } // end while   
  }  
  
  /** 销毁线程 **/  
  public void destory(Exception ex){  
    if(ex !=null){  
      ex.printStackTrace();  
      Debug.outInfo("[CMPP]MO连接(收)线程异常停止,异常原因:" + ex.getMessage());  
    }  
    else{  
      Debug.outInfo("[CMPP]MO连接(收)线程异常停止");  
    }  
  
    CMPPMOSocketProcess.getInstance().disclose();  
    CMPPMOReceiveThread.isAvail = false;  
  }  
  
} 
