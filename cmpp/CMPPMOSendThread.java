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
   
   
public class CMPPMOSendThread extends Thread{   
   
  /** 该线程运行标志 true 在运行,false停止 **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
   
  /**  初始化构造器  
   *   使用CMPPMOSocketProcess唯一的实例的输入流和输出流**/   
  public CMPPMOSendThread() {   
    inputStream  = CMPPMOSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMOSocketProcess.getInstance().outputStream;   
    isAvail = true;   
   
  }   
   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO连接("+CMPParameter.MOServerPort+")->发送线程启动 ...");   
    /** 最后一次链路测试时间 **/   
    long LastActiveTime = System.currentTimeMillis();   
   
    try{   
      while (isAvail && CMPPMOSocketProcess.getInstance().isAvail) {   
   
        int sendFlag = 1;   
   
        sendFlag = sendDataFromBuffer();     
   
        //System.out.println("sendDataFromBuffer sendFlag = "+sendFlag);    
        if (sendFlag == 1) { //如果缓冲中无发送数据    
   
          //System.out.println("System.currentTimeMillis() = "+System.currentTimeMillis());    
   
          //如果在10秒内无数据交换，则发送链路测试包    
          if((System.currentTimeMillis() - LastActiveTime) > (CMPParameter.ActiveTestTime - 3000)) //毫秒发一次测试包    
          {   
   
            //PublicFunction.send(outputStream,new CMPP(CMPP.CMPP_ACTIVE_TEST).pack());   
            LastActiveTime = System.currentTimeMillis();   
          }   
   
          PublicFunction.sleep(500);  //适当延时。以减轻CMPP通讯服务器端的压力，可以去掉    
        }   
        else {   
          //当成功发送数据成功后，更新上次链路测试时间    
            LastActiveTime = System.currentTimeMillis();   
        }   
      } //end while    
    }   
    catch(Exception ex){   
      this.destory(ex);   
    }   
   
  }   
   
   
   
  /**  
   * 从发送缓冲队列中取数据发送  
   * @return  
   * 0 成功发送  
   * 1无数据,  
   * 2有数据，但发送过程中出现异常  
   */   
  private int sendDataFromBuffer() throws Exception{   
   
    try {   
      CMPP submitMsg ; //要发送给SP的submit message    
      if (CMPPMaster.vctMTData.size() > 0) {   
        System.currentTimeMillis();   
      }   
      else {   
        return 1;   
      }   
   
      //当发送缓冲队列中有数据时，发送MT信息    
      while (CMPPMaster.vctMOData.size() > 0) {   
   
        //取缓冲中的第一条信息    
        synchronized (CMPPMaster.vctMOData) {   
          submitMsg = (CMPP) CMPPMaster.vctMOData.elementAt(0);   
        }   
   
        //判断是否发送成功，成功返回0,失败返回2    
        PublicFunction.send(outputStream,submitMsg.pack());   
   
        synchronized (CMPPMaster.vctMOData) {   
        	CMPPMaster.vctMOData.removeElementAt(0);   
        }   
        int iSleep = (1000 / CMPParameter.MTSpeed);   
        PublicFunction.sleep(iSleep);   
      } // end-while    
    }   
    catch (Exception ex) {   
      throw ex;   
    }   
    return 0;   
  }   
   
   
   
   
  /** 销毁线程 **/   
  public void destory(Exception ex){   
    if(ex !=null){   
      ex.printStackTrace();   
      Debug.outInfo("[CMPP]MO连接(发)线程异常停止,异常原因:" + ex.getMessage());   
    }   
    else{   
      Debug.outInfo("[CMPP]MO连接(发)线程异常停止");   
    }   
   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMOSendThread.isAvail = false;   
  }   
   
}  
