package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPPMTReceiveThread 双端口连接方式-MT的收线程</P>  
 * <P>Description: 中国移动短信网关通讯程序</P>  
 * <P>接收CMPP_SUBMIT_RESP, CMPP_ACTIVE_TEST_RESP,CMPP_QUERY_RESP等包</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */   
   
import java.io.*;     
   
public class CMPPMTReceiveThread extends Thread{   
   
  /** 该线程运行标志 true 在运行,false停止 **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
  /**  初始化构造器  
   *   使用CMPPMTSocketProcess唯一的实例的输入流和输出流**/   
  public CMPPMTReceiveThread() {   
    inputStream  = CMPPMTSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMTSocketProcess.getInstance().outputStream;   
    isAvail = true;   
  }   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT连接("+CMPParameter.MTServerPort+")->接收线程启动 ...");   
   
    while(CMPPMTSocketProcess.getInstance().isAvail && isAvail){   
      try{ 
    	  //System.out.println("输入流的状态："+inputStream.available());
    	  if(inputStream.available()>0){ 
	    	byte[] recByte    = PublicFunction.recv(inputStream);   
	        CMPP   deliverMsg = new CMPP();   
	        deliverMsg.parsePack(recByte);   
	        System.out.println("接受数据包的命令ID："+Integer.toHexString(deliverMsg.Command_Id));
	        switch(deliverMsg.Command_Id){   
	          case CMPP.CMPP_DELIVER:    
	            
	        	CMPP   deliverRespMsg = new CMPP(CMPP.CMPP_DELIVER_RESP);   
	            deliverRespMsg.Msg_Id = deliverMsg.Msg_Id;   
	            deliverRespMsg.Result = 0; 
	            
	            PublicFunction.send(outputStream, deliverRespMsg.pack());
	            break;   
	   
	          case CMPP.CMPP_ACTIVE_TEST_RESP:   
	            //链路测试回应包    
	            break;   
	   
	          case CMPP.CMPP_SUBMIT_RESP:   
	   
	            if(deliverMsg.Result == 0){   
	              //发送成功的,只打印消息,不做日志    
	              System.out.println("[CMPPMTReceiveThread]"+PublicFunction.getFormatTime()+" <MT Resp>"+" MsgId="+deliverMsg.Msg_Id+" SequenceID="+deliverMsg.Sequence_Id+" 发送成功");   
	            }   
	            else{   
	              Debug.outInfo("[CMPPMTReceiveThread]"+PublicFunction.getFormatTime()+" <MT Resp>"+" MsgId="+deliverMsg.Msg_Id+" SequenceID="+deliverMsg.Sequence_Id+" 发送失败,status="+deliverMsg.Result);   
	            }     
	            break;  
	        }
        }   
      }   
      catch(Exception ex){   
        destory(ex);   
      }   
   
    }   
   
    //退出线程后    
  }   
   
  public static String byteHEX(byte ib) {  
      char[] Digit = { '0','1','2','3','4','5','6','7','8','9',  
      'A','B','C','D','E','F' };  
      char [] ob = new char[2];  
      ob[0] = Digit[(ib >>> 4) & 0X0F];  
      ob[1] = Digit[ib & 0X0F];  
      String s = new String(ob);  
      return s;  
  } 
   
  /** 销毁线程 **/   
  public void destory(Exception ex){ 
	Debug.outInfo("发现异常，关闭连接！" );
    Debug.outInfo("[CMPP]MT连接(收)线程异常停止,异常原因:" + ex.getMessage());   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMTReceiveThread.isAvail = false;   
  }   
   
}   
