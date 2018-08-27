package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPPMTReceiveThread ˫�˿����ӷ�ʽ-MT�ķ����߳�</P>  
 * <P>Description: �й��ƶ���������ͨѶ����</P>  
 * <P>����CMPP_SUBMIT, CMPP_ACTIVE_TEST,CMPP_QUERY�Ȱ�</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */   
   
import java.io.*;     
   
public class CMPPMTSendThread extends Thread{   
   
  /** ���߳����б�־ true ������,falseֹͣ **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
   
  /**  ��ʼ��������  
   *   ʹ��CMPPMTSocketProcessΨһ��ʵ�����������������**/   
  public CMPPMTSendThread() {   
    inputStream  = CMPPMTSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMTSocketProcess.getInstance().outputStream;   
    isAvail = true;   
   
  }   
   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT����("+CMPParameter.MTServerPort+")->�����߳����� ...");   
    /** ���һ����·����ʱ�� **/   
    long LastActiveTime = System.currentTimeMillis();   
   
    try{   
      while (isAvail && CMPPMTSocketProcess.getInstance().isAvail) {     
   
        int sendFlag = sendDataFromBuffer();   
   
        if (sendFlag == 1) { //����������޷�������    
   
          //System.out.println("System.currentTimeMillis() = "+System.currentTimeMillis());    
   
          //�����10���������ݽ�����������·���԰�    
          if((System.currentTimeMillis() - LastActiveTime) > (CMPParameter.ActiveTestTime *1000)) //���뷢һ�β��԰�    
          {   
   
            PublicFunction.send(outputStream,new CMPP(CMPP.CMPP_ACTIVE_TEST).pack());   
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
     System.out.println("����������"+CMPPMaster.vctMTData.size());
      //�����ͻ��������������ʱ������MT��Ϣ    
      while (CMPPMaster.vctMTData.size() > 0) {   
   
        //ȡ�����еĵ�һ����Ϣ    
        synchronized (CMPPMaster.vctMTData) {   
          submitMsg = (CMPP) CMPPMaster.vctMTData.elementAt(0);   
        }   
   
        //�ж��Ƿ��ͳɹ����ɹ�����0,ʧ�ܷ���2    
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
    System.out.println("�ѷ������ݣ�");
    CMPPMaster.getInstance().distroy();
    return 0;   
  }   
   
   
   
  /** �����߳� **/   
  public void destory(Exception ex){   
    Debug.outInfo("[CMPP]MT����(��)�߳��쳣ֹͣ,�쳣ԭ��:" + ex.getMessage());   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMTSendThread.isAvail = false;   
  }   
   
}  
