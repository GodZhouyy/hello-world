package com.bettersoft.util.cmpp;

/** 
 * <P>Title: CMPPMTReceiveThread ˫�˿����ӷ�ʽ-MT��Socket����</P> 
 * <P>Description: �й��ƶ���������ͨѶ����</P> 
 * <P>���ڽ������ƶ��������ص�Socket����,ͬʱ�շ���½��,��½��Ӧ��</P> 
 * ��SOCKET�����ڵ�����ʱ��,����,������ 
 * ��˫����ʱ��,��������������(Submit,SubmitResp��) 
 * <P>Copyright: Copyright (c) 2004</P> 
  * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
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
  
  /** CMPPMTSocketProcess �ĵ��� **/  
  private static CMPPMTSocketProcess mtSocketProcess = null;  
  
  public Vector vctMTMsg = new Vector(1,1);  
  
  public CMPPMTSocketProcess() {  
//    try{   
//      System.out.println("new CMPPMTSocketProcess()" + isAvail);   
//      if(!isAvail){   
//        //����   
//        connectSMSG();   
//        //��½   
//        loginSMSG();   
//   
//        isAvail = true;   
//      }   
//    }   
//    catch(Exception ex){   
//      Debug.outError(ex);   
//    }   
  
  }  
  
  
    /**ȡ��CMPPMTSocketProcess��Ψһʵ�� **/  
  public static CMPPMTSocketProcess getInstance(){  
    if(mtSocketProcess == null){  
      mtSocketProcess = new CMPPMTSocketProcess();  
    }  
    return mtSocketProcess;  
  }  
  
  /** 
   * ��������ؽ������� 
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
      System.out.println("�����쳣�����ӹرգ�");
      disclose();  
      throw ex;  
    }  
  }  
  
  /** 
   * ���͵�½��,������Ӧ��,��½ 
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
        Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT�߳�����ISMG�ɹ�");  
        this.isAvail = true;  
      }  
      else{  
        Debug.outInfo("[CMPPMaster]MT�߳�����ISMGʧ��,ʧ��״̬:"+loginRespMsg.Status);  
      }  
    }  
    catch(Exception ex){  
      throw ex;  
    }  
  
  }  
  
  /** �Ͽ����� **/  
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


