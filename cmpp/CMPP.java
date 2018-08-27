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

import com.thoughtworks.xstream.XStream;

   
   
public class CMPP {   
   
  /**4  Unsigned  Integer   ��Ϣ�ܳ���(����Ϣͷ����Ϣ��)**/   
  private int Total_Length;   
   
  /**4  Unsigned Integer    �������Ӧ����**/   
  public int Command_Id;   
   
  /**4  Unsigned Integer  
   * ��Ϣ��ˮ��,˳���ۼ�,����Ϊ1,ѭ��ʹ�ã�һ�������Ӧ����Ϣ����ˮ�ű�����ͬ��**/   
  public int Sequence_Id;   
   
  /************************CMPP_CONNECT*********************/   
  /**6  Octet String    Դ��ַ���˴�ΪSP_Id����SP����ҵ���롣 **/   
  public String Source_Addr = CMPParameter.SP_Id;;   
   
  /**16 Octet String    ���ڼ���Դ��ַ����ֵͨ������MD5 hash����ó�����ʾ���£�  
   * AuthenticatorSource =MD5��Source_Addr+9 �ֽڵ�0 +shared secret+timestamp��  
   * Shared secret ���й��ƶ���Դ��ַʵ�������̶���  
   * timestamp��ʽΪ��MMDDHHMMSS��������ʱ���룬10λ��   **/   
  public String AuthenticatorSource = "";   
  public byte[] AuthenticatorSource1 ; 
   
  /** 4 Unsigned Integer  
   * ʱ���������,�ɿͻ��˲���,��ʽΪMMDDHHMMSS��������ʱ���룬10λ���ֵ����ͣ��Ҷ��� ��**/   
  private int Timestamp ;   
   
  /************************CMPP_CONNECT_RESP*********************/   
  /**1  Unsigned Integer  
   * ״̬0����ȷ1����Ϣ�ṹ�� 2���Ƿ�Դ��ַ 3����֤�� 4���汾̫��  5~ ����������**/   
  public int Status = 0;   
   
  /**16 Octet String    ISMG��֤�룬���ڼ���ISMG��  
   * ��ֵͨ������MD5 hash����ó�����ʾ���£�AuthenticatorISMG =MD5��Status+AuthenticatorSource+shared secret����Shared secret ���й��ƶ���Դ��ַʵ�������̶���AuthenticatorSourceΪԴ��ַʵ�巢�͸�ISMG�Ķ�Ӧ��ϢCMPP_Connect�е�ֵ�� ��֤����ʱ������Ϊ�ա�**/   
  public String AuthenticatorISMG;   
   
  /** ��Ϣ��ʶ�����㷨���£�����64λ��8�ֽڣ���������  
   * ��1��ʱ�䣨��ʽΪMMDDHHMMSS��������ʱ���룩��bit64~bit39������  
   *     bit64~bit61���·ݵĶ����Ʊ�ʾ��bit60~bit56���յĶ����Ʊ�ʾ��  
   *     bit55~bit51��Сʱ�Ķ����Ʊ�ʾ��bit50~bit45���ֵĶ����Ʊ�ʾ��  
   *     bit44~bit39����Ķ����Ʊ�ʾ��  
   * ��2���������ش��룺  
   *     bit38~bit17���Ѷ������صĴ���ת��Ϊ������д�����ֶ��С�  
   * ��3�����кţ�bit16~bit1��˳�����ӣ�����Ϊ1��ѭ��ʹ�á��������粻�����������㣬�Ҷ��롣 **/   
  public long Msg_Id = 0;   
   
  /** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.9  **/   
  public byte TP_pid  = 0;   
   
  /** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.23����ʹ��1λ���Ҷ��� **/   
  public byte TP_udhi = 0;   
   
  /**��Ϣ��ʽ  0��ASCII��  3������д������  4����������Ϣ  8��UCS2����15����GB����    **/   
  public byte Msg_Fmt = 15;   
   
  /**CMPP_DELIVER: �Ƿ�Ϊ״̬����0����״̬����1��״̬����  1  Unsigned Integer  
   * CMPP_SUBMIT : �Ƿ�Ҫ�󷵻�״̬ȷ�ϱ��棺0������Ҫ��1����Ҫ��**/   
  public byte Registered_Delivery = 1;   
   
  /**��Ϣ���� 1 Unsigned Integer**/   
  public byte Msg_Length = 4;   
   
  /**��Ϣ���� Msg_length    Octet String**/   
  public String Msg_Content = "��������";   
   
  /**������   8    Octet String**/   
  public String Reserved    = "";   
   
  /************************CMPP_DELIVER******************  
   * CMPP_DELIVER������Ŀ����ISMG�ѴӶ������Ļ�����ISMGת������  
   * �����ͽ�SP��SP��CMPP_DELIVER_RESP��Ϣ��Ӧ��***/   
   
  public String Service_Id = CMPParameter.Service_Id;   
   
  /** Դ�ն˺������ͣ�0����ʵ���룻1��α�롣 **/   
  public byte Src_terminal_type = 0;   
   
  /**Դ�ն�MSISDN���루״̬����ʱ��ΪCMPP_SUBMIT��Ϣ��Ŀ���ն˺��룩**/   
  public String Src_terminal_Id = CMPParameter.Service_Id;
  
   
  /***************************CMPP_SUBMIT********************  
   * CMPP_SUBMIT������Ŀ����SP����ISMG����Ӧ�ò����Ӻ���ISMG�ύ���š�  
   * ISMG��CMPP_SUBMIT_RESP��Ϣ��Ӧ��**/   
  /** ��ͬMsg_Id����Ϣ����������1��ʼ�� **/   
  public byte Pk_total = 1;   
   
  /** ��ͬMsg_Id����Ϣ��ţ���1��ʼ�� **/   
  public byte Pk_number = 1;   
   
  /** ��Ϣ���� **/   
  public byte Msg_level = 9;   
   
  /** �Ʒ��û������ֶΣ�0����Ŀ���ն�MSISDN�Ʒѣ�1����Դ�ն�MSISDN�Ʒѣ�  
   *                  2����SP�Ʒѣ�3����ʾ���ֶ���Ч��  
   *                  ��˭�ƷѲμ�Fee_terminal_Id�ֶΡ�**/   
  public byte Fee_UserType = 2;   
   
   
  /** ���Ʒ��û��ĺ��룬��Fee_UserTypeΪ3ʱ��ֵ��Ч����Fee_UserTypeΪ0��1��2ʱ��ֵ�����塣**/   
  public String Fee_terminal_Id  = CMPParameter.Service_Id;   
   
  /** ���Ʒ��û��ĺ������ͣ�0����ʵ���룻1��α�롣**/   
  public byte Fee_terminal_type = 1;   
   
   
  /** ��Ϣ������Դ(SP_Id)�� **/   
  public String Msg_src = CMPParameter.SP_Id;   
   
  /** �ʷ����01���ԡ��Ʒ��û����롱��ѣ�  
   *           02���ԡ��Ʒ��û����롱��������Ϣ�ѣ�  
   *           03���ԡ��Ʒ��û����롱��������ȡ��Ϣ�ѡ� **/   
  public String FeeType ="01";   
   
  /** �ʷѴ��루�Է�Ϊ��λ���� **/   
  public String FeeCode = "000030";   
   
  /**  �����Ч�ڣ���ʽ��ѭSMPP3.3Э�顣 **/   
  public String ValId_Time = "";   
   
  /** ��ʱ����ʱ�䣬��ʽ��ѭSMPP3.3Э�顣 **/   
  public String At_Time    = "";   
   
  /** ������Ϣ���û�����(С��100���û�)�� **/   
  public byte DestUsr_tl   = 1;   
   
  /** ���ն��ŵ�MSISDN���롣 **/   
  public String Dest_terminal_Id = "13900001234";   
   
  /** ���ն��ŵ��û��ĺ������ͣ�0����ʵ���룻1��α�롣 **/   
  public byte Dest_terminal_type = 0;   
   
   
  /**  ���0����ȷ1����Ϣ�ṹ�� 2�������ִ�  
   *       3����Ϣ����ظ�4����Ϣ���ȴ�5���ʷѴ����  
   *       6�����������Ϣ��7��ҵ������  
   *       8: �������ƴ�9~ ���������� ***/   
  public byte Result = 0;   
   
  /**  ��Ϣ��  **/   
  public byte[] dataPack = null;   
   
  /**Reserved   1**/   
  private byte ActiveReserved  = 0x00;   
   
  /**�㲥ҵ��ʹ�õ�LinkID���ǵ㲥��ҵ���MT���̲�ʹ�ø��ֶΡ�**/   
  public String LinkID = "";   
   
   
  /************************CMPP_QUERY***********************************/   
  /** ʱ��YYYYMMDD(��ȷ����)�� **/   
  public String Time = "";   
   
  /** ��ѯ���0��������ѯ��1����ҵ�����Ͳ�ѯ�� **/   
  public byte Query_Type = 0;   
   
  /** ��ѯ�롣��Query_TypeΪ0ʱ��������Ч����Query_TypeΪ1ʱ��������дҵ������Service_Id. **/   
  public String Query_Code = "";   
   
  /************************CMPP_QUERY_RESP*****************************/   
  /** ��SP������Ϣ������ **/   
  public int MT_TLMsg = 0;   
   
  /** ��SP�����û������� **/   
  public int MT_Tlusr = 0;   
   
  /** �ɹ�ת��������  **/   
  public int MT_Scs = 0;   
   
  /** ��ת�������� **/   
  public int MT_WT = 0;   
   
  /** ת��ʧ�������� **/   
  public int MT_FL = 0;   
   
  /** ��SP�ɹ��ʹ������� **/   
  public int MO_Scs = 0;   
   
  /** ��SP���ʹ������� **/   
  public int MO_WT = 0;   
   
  /** ��SP�ʹ�ʧ�������� **/   
  public int MO_FL = 0;   
   
   
  /*****��ISMG��SP�ͽ�״̬����ʱ����Ϣ�����ֶΣ�Msg_Content����ʽ�������£�**/   
  public long Report_Msg_Id = 0;   
   
  public String Report_Dest_terminal_Id = "";   
   
  /**���Ͷ��ŵ�Ӧ���������������һ��SP���ݸ��ֶ�ȷ��CMPP_SUBMIT��Ϣ�Ĵ���״̬��**/   
  public String Report_Stat = "";   
   
  /**YYMMDDHHMM��YYΪ��ĺ���λ00-99��MM��01-12��DD��01-31��HH��00-23��MM��00-59���� **/   
  public String Report_Submit_time = "";   
   
  /** YYMMDDHHMM�� **/   
  public String Report_Done_time   = "";   
   
  /**ȡ��SMSC����״̬�������Ϣ���е���Ϣ��ʶ��**/   
  public int Report_SMSC_sequence  = 0;   
   
  /**********************CMPP CANSTANTS****************************/   
  public final static int CMPP_CONNECT         = 0x00000001 ;   //��������    
  public final static int CMPP_CONNECT_RESP    = 0x80000001 ;   //��������Ӧ��    
  public final static int CMPP_TERMINATE       = 0x00000002 ;   //��ֹ����    
  public final static int CMPP_TERMINATE_RESP  = 0x80000002 ;   //��ֹ����Ӧ��    
  public final static int CMPP_SUBMIT          = 0x00000004 ;   //�ύ����    
  public final static int CMPP_SUBMIT_RESP     = 0x80000004 ;   //�ύ����Ӧ��    
  public final static int CMPP_DELIVER         = 0x00000005 ;   //�����·�    
  public final static int CMPP_DELIVER_RESP    = 0x80000005 ;   //�·�����Ӧ��    
  public final static int CMPP_QUERY           = 0x00000006 ;   //���Ͷ���״̬��ѯ    
  public final static int CMPP_QUERY_RESP      = 0x80000006 ;   //���Ͷ���״̬��ѯӦ��    
  public final static int CMPP_CANCEL          = 0x00000007 ;   //ɾ������    
  public final static int CMPP_CANCEL_RESP     = 0x80000007 ;   //��������Ӧ��    
  public final static int CMPP_ACTIVE_TEST     = 0x00000008 ;   //�������    
  public final static int CMPP_ACTIVE_TEST_RESP= 0x80000008 ;   //�������Ӧ��    
   
   
  public final static String REPORT_DELIVRD = "Message is delivered to destination";   
  public final static String REPORT_EXPIRED = "Message validity period hasexpired";   
  public final static String REPORT_DELETED = "Message has been deleted";   
  public final static String REPORT_UNDELIV = "Message is undeliverable";   
  public final static String REPORT_ACCEPTD = "Message is in accepted state(i.e. has been manually read on behalf of the subscriber by customer service)";   
  public final static String REPORT_UNKNOWN = "Message is in invalid state";   
  public final static String REPORT_REJECTD = "Message is in a rejected state";   
   
  /**1  Unsigned Integer  
   * ˫��Э�̵İ汾��(��λ4bit��ʾ���汾��,��λ4bit��ʾ�ΰ汾��)**/   
  private byte Version = (byte)CMPParameter.Version;   
   
   
  /**�չ�����������parse**/   
  public CMPP() {   
   
  }   
   
   
  /** ����pack�Ĺ�����**/   
  public CMPP(int CMPPMsgType) {   
    this.Command_Id  = CMPPMsgType;   
    this.Sequence_Id = getSeqNo();   
  }   
   
   
  /**  
   * ������������ش���������ֽ�����  
   * @return ����������ֽ�����  
   */   
  public byte[] pack(){   
    byte[] retBB = null;   
    switch(Command_Id){   
      case CMPP.CMPP_CONNECT:   
        retBB = this.makeConnectMsgPack();   
        break;   
      case CMPP.CMPP_CONNECT_RESP:   
        retBB = this.makeConnectRespMsgPack();   
        break;   
   
      case CMPP.CMPP_DELIVER:   
        break;   
      case CMPP.CMPP_DELIVER_RESP:   
        retBB = this.makeDeliverRespMsgPack();   
        break;   
   
      case CMPP.CMPP_SUBMIT:   
        retBB = this.makeSubmitMsgPack();   
        break;   
      case CMPP.CMPP_SUBMIT_RESP:   
        break;   
   
      case CMPP.CMPP_ACTIVE_TEST:   
        retBB = this.makeActiveMsgPack();   
        break;   
      case CMPP.CMPP_ACTIVE_TEST_RESP:   
        retBB = makeActiveRespMsgPack();   
        break;   
   
      case CMPP.CMPP_TERMINATE:   
        break;   
      case CMPP.CMPP_TERMINATE_RESP:   
        break;   
   
    }   
    return retBB;   
  }   
   
  /** �������к� **/   
  public static int fmo_number  = 0;   
  private synchronized static int getSeqNo(){   
    int temp_SeqNo = fmo_number++;   
    if (fmo_number >= 100000000)   
        fmo_number  = 1;   
     return temp_SeqNo;   
  }   
   
   
  /**ȡ��CMPP CMPP_ACTIVE_TEST����Ϣ��  
   * 7.4.7  ��·��⣨CMPP_ACTIVE_TEST������  
   **/   
  private byte[] makeActiveMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
      this.Total_Length = 12;   
   
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeActiveMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**ȡ��CMPP CMPP_ACTIVE_TEST_RESP����Ϣ��  
   * 7.4.7.1 CMPP_ACTIVE_TEST_RESP���壨SP -> ISMG��ISMG->SP��  
   **/   
  private byte[] makeActiveRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
      this.Total_Length = 12 + 1;   
   
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //�����Ϣ��    
      dataOutStream.writeByte(ActiveReserved);   
   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeActiveRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**ȡ��CMPP CMPP_CONNECT����Ϣ��  
   * 7.4.1.1 CMPP_CONNECT��Ϣ���壨SP->ISMG��  
   **/   
  private byte[] makeConnectMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
      this.Total_Length = 12 + 27;   
      this.Source_Addr  = CMPParameter.SP_Id;   
      String strTimestamp      = PublicFunction.getTimeStamp();   
      this.Timestamp           = Integer.parseInt(strTimestamp);  
      int psd_length = CMPParameter.SP_Pwd.length();
      this.AuthenticatorSource = Source_Addr +"000000000"+CMPParameter.SP_Pwd+strTimestamp; 
      System.out.println("��֤Դ��"+this.AuthenticatorSource);
      new MD5();
      byte[] addr= this.Source_Addr.getBytes();
  	  byte[] zero = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
  	  byte[] psw = CMPParameter.SP_Pwd.getBytes();
  	  byte[] time =strTimestamp.getBytes();
  	  this.AuthenticatorSource1 = new byte[25+psd_length];
  	  System.arraycopy(addr, 0, this.AuthenticatorSource1, 0, 6);
	  System.arraycopy(zero, 0, this.AuthenticatorSource1, 6, 9);
	  System.arraycopy(psw, 0, this.AuthenticatorSource1, 6+9, psd_length);
	  System.arraycopy(time, 0, this.AuthenticatorSource1, 6+9+psd_length, 10);
      //this.AuthenticatorSource1 = ;   
       
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //�����Ϣ��    
      writeString(dataOutStream,Source_Addr,6);  //д��Դ��ַ    
      //writeString(dataOutStream,AuthenticatorSource,16); //д�����ڼ���Դ��ַ������ 
      dataOutStream.write(MD5.encrypt1(this.AuthenticatorSource1)); //д�����ڼ���Դ��ַ������
      dataOutStream.writeByte(Version);  //д��˫��Э�̵İ汾��    
      dataOutStream.writeInt(Timestamp); //д��ʱ���������    
   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray(); 
      
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeConnectMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**ȡ��CMPP CMPP_CONNECT_RESP����Ϣ��  
   * 7.4.1.2 CMPP_CONNECT_RESP��Ϣ���壨ISMG -> SP��  
   **/   
  private byte[] makeConnectRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
      this.Total_Length = 12 + 27;   
   
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //�����Ϣ��    
      dataOutStream.writeByte(Status);  //д������״̬    
      writeString(dataOutStream,AuthenticatorISMG,16); //д�����ڼ���Դ��ַ������    
      dataOutStream.writeByte(Version);  //д��˫��Э�̵İ汾��    
   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeConnectRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**ȡ��CMPP CMPP_CONNECT_RESP����Ϣ��  
   * 7.4.1.2 CMPP_CONNECT_RESP��Ϣ���壨ISMG -> SP��  
   **/   
  private byte[] makeDeliverRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
   
      if(Version == 48) this.Total_Length = 12 + 12;   
      if(Version != 48) this.Total_Length = 12 + 9;   
   
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //�����Ϣ��    
      dataOutStream.writeLong(this.Msg_Id); //д����Ϣ��ʶ    
      if(Version == 48) dataOutStream.writeInt(this.Result); //д����    
      if(Version != 48) dataOutStream.writeByte(this.Result); //д����    
   
   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP]makeDeliverRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
   
   
  /** ���CMPP_SUBMIT ��Ϣ  
   * 8.4.3.1    CMPP_SUBMIT��Ϣ���壨SP->ISMG��  
   **/   
  private byte[] makeSubmitMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT��Ϣ����    
   
      String str = Msg_Content;   
      int i = 0;   
   
//      if(this.Msg_Fmt == 15){    
//        i = new String(str.getBytes("gb2312")).getBytes().length;    
//        while(i>126){    
//          str = str.substring(0,str.length() -1);    
//          i = new String(str.getBytes("gb2312")).getBytes().length;    
//        }    
//        Msg_Content = new String(str.getBytes("gb2312"));    
//      }    
//      else    
//      if(this.Msg_Fmt == 24){    
//        i = new String(str.getBytes("UnicodeBigUnmarked")).getBytes().length;    
//        while(i>126){    
//          str = str.substring(0,str.length() -1);    
//          i = new String(str.getBytes("UnicodeBigUnmarked")).getBytes().length;    
//        }    
//        Msg_Content = new String(str.getBytes("UnicodeBigUnmarked"));    
//      }    
   
      //2006-10-23�޸ģ�����wap push����      
      if(this.Msg_Fmt == 15){   
        i = new String(str.getBytes("gb2312"),-4).getBytes().length;   
        while(i>126){   
          str = str.substring(0,str.length() -1);   
          i = new String(str.getBytes("gb2312"),-4).getBytes().length;   
        }   
        Msg_Content = new String(str.getBytes("gb2312"), -4);   
      }   
      else if(this.Msg_Fmt == 24){   
        i = new String(str.getBytes("UnicodeBigUnmarked"),-4).getBytes().length;   
        while(i>126){   
          str = str.substring(0,str.length() -1);   
          i = new String(str.getBytes("UnicodeBigUnmarked"),-4).getBytes().length;   
        }   
        Msg_Content = new String(str.getBytes("UnicodeBigUnmarked"), -4);   
      }   
   
      //Msg_Content.getBytes().length;    
      int iLength = 0;   
      if(Msg_Fmt == 4) {   
        iLength = Msg_Content.getBytes().length / 2;   
      }   
      else{   
        iLength = Msg_Content.getBytes().length ;   
      }   
      if(Version == 48) this.Total_Length = 12 + 183  + iLength;   
      if(Version != 48) this.Total_Length = 12 + 147  + iLength;   
      this.Sequence_Id = (int)Msg_Id;   
   
      //���MT��Ϣ��ͷ    
      packHead(dataOutStream);   
   
      //�����Ϣ��    
      Debug.outDebug("Msg_Id = " + Msg_Id + ",this.Total_Length ="+this.Total_Length   
                     +" MsgLength:"+iLength + "  SPID:"+ CMPParameter.SP_Id + "Fee_UserType:"   
                     +Fee_UserType + " Service_Id:"+Service_Id+" Fee_terminal_type:"+Fee_terminal_type   
                     +" Fee_terminal_Id:"+Fee_terminal_Id +" FeeType:"+FeeType+ " FeeCode:"+FeeCode   
                     +" Dest_terminal_Id:"+Dest_terminal_Id + " Dest_terminal_type:"+Dest_terminal_type);   
   
      //dataOutStream.writeLong(Msg_Id); 
      writeString(dataOutStream,null,8);
      dataOutStream.writeByte(Pk_total);   
      dataOutStream.writeByte(Pk_number);   
      dataOutStream.writeByte(Registered_Delivery);   
      dataOutStream.writeByte(Msg_level);   
   
      writeString(dataOutStream,Service_Id,10);   
   
      dataOutStream.writeByte(Fee_UserType);   
   
      writeString(dataOutStream,Fee_terminal_Id,21);   
   
      dataOutStream.writeByte(TP_pid);   
      dataOutStream.writeByte(TP_udhi);   
      dataOutStream.writeByte(Msg_Fmt);   
   
      writeString(dataOutStream,CMPParameter.SP_Id,6);   
      writeString(dataOutStream,FeeType,2);   
      writeString(dataOutStream,FeeCode,6);   
      writeString(dataOutStream,ValId_Time,17);   
      writeString(dataOutStream,At_Time,17);   
      writeString(dataOutStream,Src_terminal_Id,21);   
   
      dataOutStream.writeByte(DestUsr_tl);  
      if(Version == 48) {   
        writeString(dataOutStream, Dest_terminal_Id, 32);   
        dataOutStream.writeByte(Dest_terminal_type);   
      }   
      else   
      if(Version != 48) {   
        writeString(dataOutStream, Dest_terminal_Id, 21);   
      }   
   
      if(this.Msg_Fmt ==4 ){   
        dataOutStream.writeByte(Msg_Content.length()/2);   
        for (int g = 0; g < Msg_Content.length() / 2; g++) {   
          //System.out.print(Integer.parseInt(Msg_Content.substring(g * 2, g * 2 + 2), 16) + " ");    
          dataOutStream.write(Integer.parseInt(Msg_Content.substring(g * 2, g * 2 + 2), 16));   
        }   
      }   
      else{   
        dataOutStream.writeByte(iLength);   
        writeString(dataOutStream,Msg_Content,iLength);   
      }   
   
   
   
      if(Version == 48) writeString(dataOutStream,LinkID,20); //д����    
      if(Version != 48) writeString(dataOutStream,Reserved,8);   
      //������Ϣ���ֽ���    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP]makeSubmitMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**  
   * �����Ϣͷ  
   * @param dataOutStream �ɵ����ߴ����������������  
   */   
  private void packHead( DataOutputStream dataOutStream ) throws Exception   
  {   
    try {   
      dataOutStream.writeInt(Total_Length);  //д�������    
      dataOutStream.writeInt(Command_Id);    //д���������Ӧ����    
      dataOutStream.writeInt(Sequence_Id);   //д����Ϣ��ˮ��  
    } catch(IOException e) {   
      System.out.println("[CMPP] CMPP.packHead() thrown IOException"+e);   
      throw e;   
    }   
  }   
   
   
  /**  
   * �������ܵ������ݰ���ת����CMPP MSG  
   * @param recvPack  
   * @return  
   */   
  public void parsePack(byte[] recvPack) throws Exception{   
   
    ByteArrayInputStream byteStream = new ByteArrayInputStream(recvPack);   
    DataInputStream dataInStream = new DataInputStream(byteStream);   
   
    //��ȡ��Ϣͷ    
    try {   
      this.Command_Id  =   dataInStream.readInt();             //��Ϣ����    
      this.Sequence_Id =   dataInStream.readInt();             //��Ϣ��ˮ��(�������������Ϣ��ȷ��)    
    } catch(IOException e) {   
      System.out.println("[CMPP] CMPP.parseHead()  error : "+e);   
      throw e;   
    }   
   
    //��ȡ��Ϣ��    
    try{   
      switch(Command_Id){   
        case CMPP.CMPP_CONNECT:   
          break;   
        case CMPP.CMPP_CONNECT_RESP:   
          this.parseConnectRespPack(dataInStream);   
          break;   
   
        case CMPP.CMPP_DELIVER:   
          this.parseDeliverPack(dataInStream);   
          break;   
        case CMPP.CMPP_DELIVER_RESP:   
          break;   
   
        case CMPP.CMPP_SUBMIT:   
          break;   
        case CMPP.CMPP_SUBMIT_RESP:   
          this.parseSubmitRespPack(dataInStream);   
          break;   
   
        case CMPP.CMPP_ACTIVE_TEST:   
          break;   
        case CMPP.CMPP_ACTIVE_TEST_RESP:   
          break;   
   
        case CMPP.CMPP_QUERY:   
         break;   
       case CMPP.CMPP_QUERY_RESP:   
         break;   
   
      }   
    }   
    catch(Exception e){   
        throw e;   
    }   
  }   
   
   
   
  /**  
   * ����CMPP_CONNECT_RESP��Ϣ��  
   * 7.4.1.2 CMPP_CONNECT_RESP��Ϣ���壨ISMG -> SP��  
   * @param dataInputStream  
   */   
   private void parseConnectRespPack( DataInputStream dataInputStream ) throws Exception   
   {   
     try{   
    	System.out.println("�汾�ţ� " + this.Version); 
       this.Status           = dataInputStream.readByte();   
       System.out.println("״̬�� " + this.Status);
       this.AuthenticatorISMG = readString(dataInputStream,16);   
       this.Version           = dataInputStream.readByte(); 
       System.out.println("�汾�ţ� " + this.Version); 
   
     }catch(Exception e){   
       System.out.println("[CMPP]CMPP.parseConnectRespPack error " + e);   
       throw e;   
     }   
   }   
   
   
   /**  
    * ����CMPP_DELIVER��Ϣ��  
    * 8.4.5.1   CMPP_DELIVER��Ϣ���壨ISMG->SP��  
    * @param dataInputStream  
    */   
    private void parseDeliverPack( DataInputStream dataInputStream ) throws Exception   
    {   
      try{   
        this.Msg_Id     = dataInputStream.readLong();   
        //Ŀ�ĺ��롣SP�ķ�����룬һ��4--6λ��������ǰ׺Ϊ�������ĳ����룻�ú������ֻ��û�����Ϣ�ı��к��롣    
        this.Dest_terminal_Id   = CMPP.readString(dataInputStream,21);   
        this.Service_Id = CMPP.readString(dataInputStream,10);   
   
        this.TP_pid  = dataInputStream.readByte();   
        this.TP_udhi = dataInputStream.readByte();   
        this.Msg_Fmt = dataInputStream.readByte();   
   
   
        if(Version == 48){ //CMPP3.0    
          this.Src_terminal_Id   = CMPP.readString(dataInputStream,32);   
          this.Src_terminal_type = dataInputStream.readByte();   
        }   
        else{//CMPP2.0    
          this.Src_terminal_Id = CMPP.readString(dataInputStream, 21);   
        }   
   
        this.Registered_Delivery = dataInputStream.readByte();   
        this.Msg_Length  = dataInputStream.readByte();   
        //��Ϊ״̬����ʱ��    
        if(Registered_Delivery == 1){   
          this.Report_Msg_Id  = dataInputStream.readLong();   
          this.Report_Stat    = CMPP.readString(dataInputStream,7);   
          this.Report_Submit_time         = CMPP.readString(dataInputStream,10);   
          this.Report_Done_time           = CMPP.readString(dataInputStream,10);   
          this.Report_Dest_terminal_Id    = CMPP.readString(dataInputStream,21);   
          this.Report_SMSC_sequence       = dataInputStream.readInt();   
        }   
        else{   
          //���ֽ�������128ʱ��    
          this.Msg_Content = readDeliverString(dataInputStream,this.Msg_Length,this.Msg_Fmt);   
        }   
   
        if(Version == 48){ //CMPP3.0    
          this.LinkID = CMPP.readString(dataInputStream,20);   
        }   
        else  if(Version != 48){ //CMPP2.0    
          this.Reserved      = CMPP.readString(dataInputStream,8);   
        }   
   
   
    }catch(Exception e){   
        System.out.println("[CMPP]CMPP.parseDeliverPack error " + e);   
        throw e;   
      }   
    }   
   
   
   
   
    /**  
     * ����CMPP_SUBMIT_RESP��Ϣ��  
     * 8.4.3.2  CMPP_SUBMIT_RESP��Ϣ���壨ISMG �� SP��  
     * @param dataInputStream  
     */   
     private void parseSubmitRespPack( DataInputStream dataInputStream ) throws Exception   
     {   
       try{   
         this.Msg_Id     = dataInputStream.readLong();   
         //Ŀ�ĺ��롣SP�ķ�����룬һ��4--6λ��������ǰ׺Ϊ�������ĳ����룻�ú������ֻ��û�����Ϣ�ı��к��롣    
   
         if(Version == 48){ //CMPP3.0    
           this.Result   =  (byte)dataInputStream.readInt();   
         }   
         else { //CMPP2.0    
           this.Result   =  dataInputStream.readByte();   
         }   
       }catch(Exception e){   
         System.out.println("[CMPP]CMPP.parseSubmitRespPack error " + e);   
         throw e;   
       }   
     }   
   
   
     /**  
   * �����������д��ָ�������ַ����������Ҳ���0��  
   * @param dataOutStream �ɵ����ߴ����������������  
   * @param str д����ַ�  
   * @param StrLen ָ������  
   * @throws Exce  
   **/   
  private void writeString(DataOutputStream dataOutStream,String str,int StrLen)   
      throws Exception  {   
	  int i;   
	     try{   
	       if(str!=null)   
	       {   
	         dataOutStream.writeBytes(str) ;   
	         System.out.println("str len = "+str.length());   
	         for (i=1;i<=StrLen-str.length ();i++)   
	         {   
	           dataOutStream.writeByte(0x00);   
	         }   
	       }else{   
	         for (i=1;i<=StrLen;i++)   
	         {   
	           dataOutStream.writeByte(0x00);   
	         }   
	       }   
	     }catch(IOException e){   
	      throw e;   
	     }   
    }   
  private static String readString(DataInputStream inStream,int StrLen) throws Exception{   
	    byte[] readByte = new byte[1000];   
	    int i = 0;   
	    try{   
	        while(true){   
	           readByte[i] = inStream.readByte() ;   
	           i++;   
	          if (i>=1000)   
	              return "";   
	          if(i > StrLen-1)   
	              break;   
	        }   
	    }catch(IOException e){   
	    	return "erro";  
	   }   
	     String result = new String(readByte);  	     
	     return result.trim ();   
	}
   
    /**  
     * ������,�����ʽ���ֽ����ж�ȡָ�����ȵ��ַ�  
     * @param inStream :�ֽ���  
     * @param StrLen   :����  
     * @param fmt      :�����ʽ  
     * @return  
     */   
    private static String readDeliverString(DataInputStream inStream, int StrLen,   
                                            int fmt) {   
      String result = "";   
      byte[] readByte = new byte[1000];   
      int i = 0;   
      //�ж��ַ����Ƿ���128λ    
      if (StrLen < 0) {   
        StrLen = 256 + StrLen;   
      }   
   
      try {   
        while (true) {   
          readByte[i] = inStream.readByte();   
          i++;   
          if (i >= 1000)   
            return "";   
          if (i > StrLen - 1)   
            break;   
        }   
      }   
      catch (IOException e) {   
        return "readStringError->IOException";   
      }   
      //�������ʽ����ת��    
      try {   
        if (fmt == 8) {   
          result = new String(readByte, "UnicodeBigUnmarked");   
        }   
        if (fmt == 0) {   
          result = new String(readByte, 0, i);   
        }   
        if (fmt == 15) {   
          result = new String(readByte, "gb2312");   
        }   
      }   
      catch (Exception e) {   
        return "readStringError->FormatException";   
      }   
      return result.trim();   
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
    public static void main(String[] args){
    	
    	
    	byte[] addr= "901234".getBytes();
    	byte[] zero = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    	byte[] psw = "1234".getBytes();
    	byte[] time ="0829111423".getBytes();
    	byte[] authenticatorSource = new byte[29];
    	System.arraycopy(addr, 0, authenticatorSource, 0, 6);
    	System.arraycopy(zero, 0, authenticatorSource, 6, 9);
    	System.arraycopy(psw, 0, authenticatorSource, 6+9, 4);
    	System.arraycopy(time, 0, authenticatorSource, 6+9+4, 10);
    	 for(int i=0;i<authenticatorSource.length;i++){
        	 System.out.println("����ǰ��֤Դ��"+byteHEX(authenticatorSource[i]));
         }
         new MD5();   
         new XStream();
         byte[] test2 = MD5.encrypt1(authenticatorSource);   
         for(int i=0;i<test2.length;i++){
        	 System.out.println("���ܺ���֤Դ��"+byteHEX(test2[i]));
         }
    }
   
}   



