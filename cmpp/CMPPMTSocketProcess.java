package com.bettersoft.util.cmpp;

/** 
 * <P>Title: CMPPMTReceiveThread 双端口连接方式-MT的Socket连接</P> 
 * <P>Description: 中国移动短信网关通讯程序</P> 
 * <P>用于建立与移动短信网关的Socket连接,同时收发登陆包,登陆响应包</P> 
 * 该SOCKET连接在单连接时候,无用,不启动 
 * 在双连接时候,仅用来做法数据(Submit,SubmitResp等) 
 * <P>Copyright: Copyright (c) 2004</P> 
  * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */  
  
import java.io.*;  
import java.net.*;  
import java.util.Vector;  
  
public class CMPPMTSocketProcess {  
  
  public Socket socket ;  
  
  public DataInputStream  inputStream;  
  
  public DataOutputStream outputStream;  
  
  public boolean isAvail  = false;  
  
  /** CMPPMTSocketProcess 的单例 **/  
  private static CMPPMTSocketProcess mtSocketProcess = null;  
  
  public Vector vctMTMsg = new Vector(1,1);  
  
  public CMPPMTSocketProcess() {  
//    try{   
//      System.out.println("new CMPPMTSocketProcess()" + isAvail);   
//      if(!isAvail){   
//        //连接   
//        connectSMSG();   
//        //登陆   
//        loginSMSG();   
//   
//        isAvail = true;   
//      }   
//    }   
//    catch(Exception ex){   
//      Debug.outError(ex);   
//    }   
  
  }  
  
  
    /**取得CMPPMTSocketProcess的唯一实例 **/  
  public static CMPPMTSocketProcess getInstance(){  
    if(mtSocketProcess == null){  
      mtSocketProcess = new CMPPMTSocketProcess();  
    }  
    return mtSocketProcess;  
  }  
  
  /** 
   * 与短信网关建立连接 
   * @throws java.lang.Exception 
   */  
  public void connectSMSG() throws Exception{  
    try{  
      Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" "+CMPParameter.ServerIp +":"+ CMPParameter.MTServerPort);  
      socket = new Socket(CMPParameter.ServerIp,CMPParameter.MTServerPort); 
      socket.setSoTimeout(0);
      inputStream  = new DataInputStream(socket.getInputStream());  
      outputStream = new DataOutputStream(socket.getOutputStream());  
  
      loginSMSG();  
    }  
    catch(Exception ex){  
      System.out.println("出现异常，连接关闭！");
      disclose();  
      throw ex;  
    }  
  }  
  
  /** 
   * 发送登陆包,接收响应包,登陆 
   * @throws java.lang.Exception 
   */  
  public void loginSMSG() throws Exception{  
    try{  
      CMPP loginMsg = new CMPP(CMPP.CMPP_CONNECT);  
      PublicFunction.send(outputStream,loginMsg.pack());  
      byte[] loginRespByte = PublicFunction.recv(inputStream);  
      CMPP loginRespMsg = new CMPP();  
      loginRespMsg.parsePack(loginRespByte);  
      System.out.println("[CMPPMTSocketProcess]CMPP.loginSMSG " + loginRespMsg.Status); 
      if(loginRespMsg.Status == 0){  
        Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT线程连接ISMG成功");  
        this.isAvail = true;  
      }  
      else{  
        Debug.outInfo("[CMPPMaster]MT线程连接ISMG失败,失败状态:"+loginRespMsg.Status);  
      }  
    }  
    catch(Exception ex){  
      throw ex;  
    }  
  
  }  
  
  /** 断开连接 **/  
  public void disclose(){  
    try{  
      if(this.inputStream !=null){  
        this.inputStream.close();  
        this.inputStream = null;  
      }  
      if(this.outputStream !=null){  
        this.outputStream.close();  
        this.outputStream = null;  
      }  
      if(this.socket !=null){  
        this.socket.close();  
        this.socket = null;  
      }  
    }  
    catch(Exception ex){  
      Debug.outError(ex);  
    }  
    this.isAvail = false;  
  }  
  
  
  
  
}  


