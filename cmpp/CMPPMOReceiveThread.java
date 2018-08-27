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
  
public class CMPPMOReceiveThread extends Thread{  
  
  /** ���߳����б�־ true ������,falseֹͣ **/  
  public static boolean isAvail = false;  
  
  private static DataInputStream  inputStream;  
  
  private static DataOutputStream outputStream;  
  
  
  /**  ��ʼ�������� 
   *   ʹ��CMPPMOSocketProcessΨһ��ʵ�����������������**/  
  public CMPPMOReceiveThread() {  
    inputStream  = CMPPMOSocketProcess.getInstance().inputStream;  
    outputStream = CMPPMOSocketProcess.getInstance().outputStream;  
    isAvail = true;  
  }  
  
  public void run(){  
    Debug.outInfo("[CMPPMaster]"+PublicFunction.getFormatTime()+" MO����("+CMPParameter.MOServerPort+")->�����߳����� ...");  
  
    while(CMPPMOSocketProcess.getInstance().isAvail && isAvail){  
      try{ 
    	if(inputStream.available()>0){  
	        byte[] recByte    = PublicFunction.recv(inputStream);  
	        CMPP   deliverMsg = new CMPP();  
	        deliverMsg.parsePack(recByte);  
	  
	        switch(deliverMsg.Command_Id){  
	          case CMPP.CMPP_DELIVER:  
	  
	            //��Deliver��Ϣ�����ڽ��ջ�����,��CMPPMOSocketProcess��CMPPMODataSaveThread   
	            //�߳̽�MO���ݱ��������ݿ���   
	            synchronized(CMPPMaster.vctMOData){  
	            	CMPPMaster.vctMOData.addElement(deliverMsg);  
	            }  
	//            CMPPDBAccess.getInstance().saveMOData(deliverMsg);   
	  
	            //��ӡ������Ϣ   
	            if(deliverMsg.Registered_Delivery==1){  
	              Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+" <REPORT>SrcNumber:"+deliverMsg.Src_terminal_Id+" Report_Stat:"+deliverMsg.Report_Stat+  
	                            " Report_Dest_terminal_Id:"+deliverMsg.Report_Dest_terminal_Id +"  @"+deliverMsg.Dest_terminal_Id + " Report_ID:"+deliverMsg.Report_Msg_Id);  
	            }  
	            else{  
	              Debug.outInfo("\n[CMPPMaster]"+PublicFunction.getFormatTime()+" <MO>SrcNumber:"+deliverMsg.Src_terminal_Id+ " DestNumber:"+ deliverMsg.Dest_terminal_Id+" ServiceID:"  
	                            +deliverMsg.Service_Id + " SrcUserType:"+deliverMsg.Src_terminal_type+" MsgContent:"+deliverMsg.Msg_Content+ " MsgFmt:"+deliverMsg.Msg_Fmt +" LinkId:"  
	                            +deliverMsg.LinkID );  
	            }  
	  
	            //��ӦDeliver��Ϣ   
	            CMPP   deliverRespMsg = new CMPP(CMPP.CMPP_DELIVER_RESP);  
	            deliverRespMsg.Msg_Id = deliverMsg.Msg_Id;  
	            deliverRespMsg.Result = 0;  
	            PublicFunction.send(outputStream, deliverRespMsg.pack());  
	            break;  
	  
	          case CMPP.CMPP_ACTIVE_TEST_RESP:  
	            //��·������Ӧ,�޴���   
	            break;  
	  
	          case CMPP.CMPP_SUBMIT_RESP:  
	            //����ɹ��·�����Ӧ��Ϣ   
	            //CMPPDBAccess.getInstance().dealProccessMsg(deliverMsg);   
	        	  
	            //2005-08-29�޸ģ���߻�Ӧ��Ϣ�ı����ٶ�   
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
  
  /** �����߳� **/  
  public void destory(Exception ex){  
    if(ex !=null){  
      ex.printStackTrace();  
      Debug.outInfo("[CMPP]MO����(��)�߳��쳣ֹͣ,�쳣ԭ��:" + ex.getMessage());  
    }  
    else{  
      Debug.outInfo("[CMPP]MO����(��)�߳��쳣ֹͣ");  
    }  
  
    CMPPMOSocketProcess.getInstance().disclose();  
    CMPPMOReceiveThread.isAvail = false;  
  }  
  
} 
