package com.bettersoft.util.cmpp;

/** 
 * <P>Title: CMPPMOSocketProcess ˫���ӷ�ʽ-MO��Socket����</P> 
 * <P>Description: �й��ƶ���������ͨѶ����</P> 
 * <P>���ڽ������ƶ��������ص�Socket����,ͬʱ�շ���½��,��½��Ӧ��</P> 
 * ��SOCKET�����ڵ�����ʱ��,�����շ�����(Submit,SubmitResp,Deliver,DeliverResp��) 
 * ��˫����ʱ��,��������������(Deliver,DeliverResp��)  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0
 */  
  
import java.io.*;  
import java.net.*;  
import java.util.Vector;   
  
public class CMPPMOSocketProcess {  
  
  public Socket socket ;  
  
  public DataInputStream  inputStream;  
  
  public DataOutputStream outputStream;  
  
  public boolean isAvail  = false;  
  
  /** CMPPMOSocketProcess �ĵ��� **/  
  private static CMPPMOSocketProcess moSocketProcess = null;  
  
  /** �������ݻ���,��CMPPMOReceiveThread���ܵ��������ݺ�,�����ݱ����ڸû�����,�� 
   *  CMPPMODataSaveThread�߳̽������ٱ��浽���ݿ��� **/  
  public Vector vctMOData = new Vector(1,1);  
  
  public CMPPMOSocketProcess() {  
  }  
  
  
  /**ȡ��CMPPMOSocketProcess��Ψһʵ�� **/  
  public static CMPPMOSocketProcess getInstance(){  
    if(moSocketProcess == null){  
      moSocketProcess = new CMPPMOSocketProcess();  
    }  
    return moSocketProcess;  
  }  
  
  
  /** 
   * ��������ؽ������� 
   * @throws java.lang.Exception 
   */  
  public void connectSMSG() throws Exception{  
    try{  
      Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" "+CMPParameter.ServerIp +":"+ CMPParameter.MOServerPort);  
      socket = new Socket(CMPParameter.ServerIp,CMPParameter.MOServerPort); 
      socket.setSoTimeout(0);
      inputStream  = new DataInputStream(socket.getInputStream());  
      outputStream = new DataOutputStream(socket.getOutputStream());  
  
      loginSMSG();  
    }  
    catch(Exception ex){  
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
  
      if(loginRespMsg.Status == 0 ){  
        Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO�߳�����ISMG�ɹ�");  
        this.isAvail = true;  
      }  
      else{  
        this.socket.close();  
        this.socket = null;  
        Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO�߳�����ISMGʧ��,ʧ��״̬:"+loginRespMsg.Status);  
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

