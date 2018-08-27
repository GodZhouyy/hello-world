package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPP Interface</P>  
* <P>Description: �й��ƶ���������ͨѶ����</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */   
   
import java.io.*;   
   
   
public class CMPPMOSendThread extends Thread{   
   
  /** ���߳����б�־ true ������,falseֹͣ **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
   
  /**  ��ʼ��������  
   *   ʹ��CMPPMOSocketProcessΨһ��ʵ�����������������**/   
  public CMPPMOSendThread() {   
    inputStream  = CMPPMOSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMOSocketProcess.getInstance().outputStream;   
    isAvail = true;   
   
  }   
   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO����("+CMPParameter.MOServerPort+")->�����߳����� ...");   
    /** ���һ����·����ʱ�� **/   
    long LastActiveTime = System.currentTimeMillis();   
   
    try{   
      while (isAvail && CMPPMOSocketProcess.getInstance().isAvail) {   
   
        int sendFlag = 1;   
   
        sendFlag = sendDataFromBuffer();     
   
        //System.out.println("sendDataFromBuffer sendFlag = "+sendFlag);    
        if (sendFlag == 1) { //����������޷�������    
   
          //System.out.println("System.currentTimeMillis() = "+System.currentTimeMillis());    
   
          //�����10���������ݽ�����������·���԰�    
          if((System.currentTimeMillis() - LastActiveTime) > (CMPParameter.ActiveTestTime - 3000)) //���뷢һ�β��԰�    
          {   
   
            //PublicFunction.send(outputStream,new CMPP(CMPP.CMPP_ACTIVE_TEST).pack());   
            LastActiveTime = System.currentTimeMillis();   
          }   
   
          PublicFunction.sleep(500);  //�ʵ���ʱ���Լ���CMPPͨѶ�������˵�ѹ��������ȥ��    
        }   
        else {   
          //���ɹ��������ݳɹ��󣬸����ϴ���·����ʱ��    
            LastActiveTime = System.currentTimeMillis();   
        }   
      } //end while    
    }   
    catch(Exception ex){   
      this.destory(ex);   
    }   
   
  }   
   
   
   
  /**  
   * �ӷ��ͻ��������ȡ���ݷ���  
   * @return  
   * 0 �ɹ�����  
   * 1������,  
   * 2�����ݣ������͹����г����쳣  
   */   
  private int sendDataFromBuffer() throws Exception{   
   
    try {   
      CMPP submitMsg ; //Ҫ���͸�SP��submit message    
      if (CMPPMaster.vctMTData.size() > 0) {   
        System.currentTimeMillis();   
      }   
      else {   
        return 1;   
      }   
   
      //�����ͻ��������������ʱ������MT��Ϣ    
      while (CMPPMaster.vctMOData.size() > 0) {   
   
        //ȡ�����еĵ�һ����Ϣ    
        synchronized (CMPPMaster.vctMOData) {   
          submitMsg = (CMPP) CMPPMaster.vctMOData.elementAt(0);   
        }   
   
        //�ж��Ƿ��ͳɹ����ɹ�����0,ʧ�ܷ���2    
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
   
   
   
   
  /** �����߳� **/   
  public void destory(Exception ex){   
    if(ex !=null){   
      ex.printStackTrace();   
      Debug.outInfo("[CMPP]MO����(��)�߳��쳣ֹͣ,�쳣ԭ��:" + ex.getMessage());   
    }   
    else{   
      Debug.outInfo("[CMPP]MO����(��)�߳��쳣ֹͣ");   
    }   
   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMOSendThread.isAvail = false;   
  }   
   
}  
