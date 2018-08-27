package com.bettersoft.util.cmpp;

/**  
 * <P>Title: CMPPMTReceiveThread ˫�˿����ӷ�ʽ-MT�����߳�</P>  
 * <P>Description: �й��ƶ���������ͨѶ����</P>  
 * <P>����CMPP_SUBMIT_RESP, CMPP_ACTIVE_TEST_RESP,CMPP_QUERY_RESP�Ȱ�</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */   
   
import java.io.*;     
   
public class CMPPMTReceiveThread extends Thread{   
   
  /** ���߳����б�־ true ������,falseֹͣ **/   
  public static boolean isAvail = false;   
   
  private static DataInputStream  inputStream;   
   
  private static DataOutputStream outputStream;   
   
  /**  ��ʼ��������  
   *   ʹ��CMPPMTSocketProcessΨһ��ʵ�����������������**/   
  public CMPPMTReceiveThread() {   
    inputStream  = CMPPMTSocketProcess.getInstance().inputStream;   
    outputStream = CMPPMTSocketProcess.getInstance().outputStream;   
    isAvail = true;   
  }   
   
  public void run(){   
   
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MT����("+CMPParameter.MTServerPort+")->�����߳����� ...");   
   
    while(CMPPMTSocketProcess.getInstance().isAvail && isAvail){   
      try{ 
    	  //System.out.println("��������״̬��"+inputStream.available());
    	  if(inputStream.available()>0){ 
	    	byte[] recByte    = PublicFunction.recv(inputStream);   
	        CMPP   deliverMsg = new CMPP();   
	        deliverMsg.parsePack(recByte);   
	        System.out.println("�������ݰ�������ID��"+Integer.toHexString(deliverMsg.Command_Id));
	        switch(deliverMsg.Command_Id){   
	          case CMPP.CMPP_DELIVER:    
	            
	        	CMPP   deliverRespMsg = new CMPP(CMPP.CMPP_DELIVER_RESP);   
	            deliverRespMsg.Msg_Id = deliverMsg.Msg_Id;   
	            deliverRespMsg.Result = 0; 
	            
	            PublicFunction.send(outputStream, deliverRespMsg.pack());
	            break;   
	   
	          case CMPP.CMPP_ACTIVE_TEST_RESP:   
	            //��·���Ի�Ӧ��    
	            break;   
	   
	          case CMPP.CMPP_SUBMIT_RESP:   
	   
	            if(deliverMsg.Result == 0){   
	              //���ͳɹ���,ֻ��ӡ��Ϣ,������־    
	              System.out.println("[CMPPMTReceiveThread]"+PublicFunction.getFormatTime()+" <MT Resp>"+" MsgId="+deliverMsg.Msg_Id+" SequenceID="+deliverMsg.Sequence_Id+" ���ͳɹ�");   
	            }   
	            else{   
	              Debug.outInfo("[CMPPMTReceiveThread]"+PublicFunction.getFormatTime()+" <MT Resp>"+" MsgId="+deliverMsg.Msg_Id+" SequenceID="+deliverMsg.Sequence_Id+" ����ʧ��,status="+deliverMsg.Result);   
	            }     
	            break;  
	        }
        }   
      }   
      catch(Exception ex){   
        destory(ex);   
      }   
   
    }   
   
    //�˳��̺߳�    
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
   
  /** �����߳� **/   
  public void destory(Exception ex){ 
	Debug.outInfo("�����쳣���ر����ӣ�" );
    Debug.outInfo("[CMPP]MT����(��)�߳��쳣ֹͣ,�쳣ԭ��:" + ex.getMessage());   
    CMPPMTSocketProcess.getInstance().disclose();   
    CMPPMTReceiveThread.isAvail = false;   
  }   
   
}   
