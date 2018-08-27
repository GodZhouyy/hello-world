package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPPMTReceiveThread 双端口连接方式-MT的发送线程</P>  
 * <P>Description: 中国移动短信网关通讯程序</P>  
 * <P>发送CMPP_SUBMIT, CMPP_ACTIVE_TEST,CMPP_QUERY等包</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */   
   
import java.io.*;     
   
public class CMPPMTSendThread extends Thread{   
   
  /** 该线程运行标志 true 在运行,false停止 **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
   
  /**  初始化构造器  
   *   使用CMPPMTSocketProcess唯一的实例的输入流和输出流**/   
  public CMPPMTSendThread() {   
    inputStream  = CMPPMTSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMTSocketProcess.getInstance().outputStream;   
    isAvail = true;   
   
  }   
   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT连接("+CMPParameter.MTServerPort+")->发送线程启动 ...");   
    /** 最后一次链路测试时间 **/   
    long LastActiveTime = System.currentTimeMillis();   
   
    try{   
      while (isAvail && CMPPMTSocketProcess.getInstance().isAvail) {     
   
        int sendFlag = sendDataFromBuffer();   
   
        if (sendFlag == 1) { //如果缓冲中无发送数据    
   
          //System.out.println("System.currentTimeMillis() = "+System.currentTimeMillis());    
   
          //如果在10秒内无数据交换，则发送链路测试包    
          if((System.currentTimeMillis() - LastActiveTime) > (CMPParameter.ActiveTestTime *1000)) //毫秒发一次测试包    
          {   
   
            PublicFunction.send(outputStream,new CMPP(CMPP.CMPP_ACTIVE_TEST).pack());   
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
     System.out.println("数据条数："+CMPPMaster.vctMTData.size());
      //当发送缓冲队列中有数据时，发送MT信息    
      while (CMPPMaster.vctMTData.size() > 0) {   
   
        //取缓冲中的第一条信息    
        synchronized (CMPPMaster.vctMTData) {   
          submitMsg = (CMPP) CMPPMaster.vctMTData.elementAt(0);   
        }   
   
        //判断是否发送成功，成功返回0,失败返回2    
        PublicFunction.send(outputStream,submitMsg.pack());   
   
        synchronized (CMPPMaster.vctMTData) {   
        	CMPPMaster.vctMTData.removeElementAt(0);   
        }   
        int iSleep = (1000 / CMPParameter.MTSpeed);   
        PublicFunction.sleep(iSleep);   
      } // end-while    
    }   
    catch (Exception ex) {   
      throw ex;   
    } 
    System.out.println("已发送数据！");
    CMPPMaster.getInstance().distroy();
    return 0;   
  }   
   
   
   
  /** 销毁线程 **/   
  public void destory(Exception ex){   
    Debug.outInfo("[CMPP]MT连接(发)线程异常停止,异常原因:" + ex.getMessage());   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMTSendThread.isAvail = false;   
  }   
   
}  
