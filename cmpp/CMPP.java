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

import com.thoughtworks.xstream.XStream;

   
   
public class CMPP {   
   
  /**4  Unsigned  Integer   消息总长度(含消息头及消息体)**/   
  private int Total_Length;   
   
  /**4  Unsigned Integer    命令或响应类型**/   
  public int Command_Id;   
   
  /**4  Unsigned Integer  
   * 消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）**/   
  public int Sequence_Id;   
   
  /************************CMPP_CONNECT*********************/   
  /**6  Octet String    源地址，此处为SP_Id，即SP的企业代码。 **/   
  public String Source_Addr = CMPParameter.SP_Id;;   
   
  /**16 Octet String    用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下：  
   * AuthenticatorSource =MD5（Source_Addr+9 字节的0 +shared secret+timestamp）  
   * Shared secret 由中国移动与源地址实体事先商定，  
   * timestamp格式为：MMDDHHMMSS，即月日时分秒，10位。   **/   
  public String AuthenticatorSource = "";   
  public byte[] AuthenticatorSource1 ; 
   
  /** 4 Unsigned Integer  
   * 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐 。**/   
  private int Timestamp ;   
   
  /************************CMPP_CONNECT_RESP*********************/   
  /**1  Unsigned Integer  
   * 状态0：正确1：消息结构错 2：非法源地址 3：认证错 4：版本太高  5~ ：其他错误**/   
  public int Status = 0;   
   
  /**16 Octet String    ISMG认证码，用于鉴别ISMG。  
   * 其值通过单向MD5 hash计算得出，表示如下：AuthenticatorISMG =MD5（Status+AuthenticatorSource+shared secret），Shared secret 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。 认证出错时，此项为空。**/   
  public String AuthenticatorISMG;   
   
  /** 信息标识生成算法如下：采用64位（8字节）的整数：  
   * （1）时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中  
   *     bit64~bit61：月份的二进制表示；bit60~bit56：日的二进制表示；  
   *     bit55~bit51：小时的二进制表示；bit50~bit45：分的二进制表示；  
   *     bit44~bit39：秒的二进制表示；  
   * （2）短信网关代码：  
   *     bit38~bit17，把短信网关的代码转换为整数填写到该字段中。  
   * （3）序列号：bit16~bit1，顺序增加，步长为1，循环使用。各部分如不能填满，左补零，右对齐。 **/   
  public long Msg_Id = 0;   
   
  /** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9  **/   
  public byte TP_pid  = 0;   
   
  /** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23，仅使用1位，右对齐 **/   
  public byte TP_udhi = 0;   
   
  /**信息格式  0：ASCII串  3：短信写卡操作  4：二进制信息  8：UCS2编码15：含GB汉字    **/   
  public byte Msg_Fmt = 15;   
   
  /**CMPP_DELIVER: 是否为状态报告0：非状态报告1：状态报告  1  Unsigned Integer  
   * CMPP_SUBMIT : 是否要求返回状态确认报告：0：不需要；1：需要。**/   
  public byte Registered_Delivery = 1;   
   
  /**消息长度 1 Unsigned Integer**/   
  public byte Msg_Length = 4;   
   
  /**消息内容 Msg_length    Octet String**/   
  public String Msg_Content = "测试数据";   
   
  /**保留项   8    Octet String**/   
  public String Reserved    = "";   
   
  /************************CMPP_DELIVER******************  
   * CMPP_DELIVER操作的目的是ISMG把从短信中心或其它ISMG转发来的  
   * 短信送交SP，SP以CMPP_DELIVER_RESP消息回应。***/   
   
  public String Service_Id = CMPParameter.Service_Id;   
   
  /** 源终端号码类型，0：真实号码；1：伪码。 **/   
  public byte Src_terminal_type = 0;   
   
  /**源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）**/   
  public String Src_terminal_Id = CMPParameter.Service_Id;
  
   
  /***************************CMPP_SUBMIT********************  
   * CMPP_SUBMIT操作的目的是SP在与ISMG建立应用层连接后向ISMG提交短信。  
   * ISMG以CMPP_SUBMIT_RESP消息响应。**/   
  /** 相同Msg_Id的信息总条数，从1开始。 **/   
  public byte Pk_total = 1;   
   
  /** 相同Msg_Id的信息序号，从1开始。 **/   
  public byte Pk_number = 1;   
   
  /** 信息级别。 **/   
  public byte Msg_level = 9;   
   
  /** 计费用户类型字段：0：对目的终端MSISDN计费；1：对源终端MSISDN计费；  
   *                  2：对SP计费；3：表示本字段无效，  
   *                  对谁计费参见Fee_terminal_Id字段。**/   
  public byte Fee_UserType = 2;   
   
   
  /** 被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。**/   
  public String Fee_terminal_Id  = CMPParameter.Service_Id;   
   
  /** 被计费用户的号码类型，0：真实号码；1：伪码。**/   
  public byte Fee_terminal_type = 1;   
   
   
  /** 信息内容来源(SP_Id)。 **/   
  public String Msg_src = CMPParameter.SP_Id;   
   
  /** 资费类别：01：对“计费用户号码”免费；  
   *           02：对“计费用户号码”按条计信息费；  
   *           03：对“计费用户号码”按包月收取信息费。 **/   
  public String FeeType ="01";   
   
  /** 资费代码（以分为单位）。 **/   
  public String FeeCode = "000030";   
   
  /**  存活有效期，格式遵循SMPP3.3协议。 **/   
  public String ValId_Time = "";   
   
  /** 定时发送时间，格式遵循SMPP3.3协议。 **/   
  public String At_Time    = "";   
   
  /** 接收信息的用户数量(小于100个用户)。 **/   
  public byte DestUsr_tl   = 1;   
   
  /** 接收短信的MSISDN号码。 **/   
  public String Dest_terminal_Id = "13900001234";   
   
  /** 接收短信的用户的号码类型，0：真实号码；1：伪码。 **/   
  public byte Dest_terminal_type = 0;   
   
   
  /**  结果0：正确1：消息结构错 2：命令字错  
   *       3：消息序号重复4：消息长度错5：资费代码错  
   *       6：超过最大信息长7：业务代码错  
   *       8: 流量控制错9~ ：其他错误 ***/   
  public byte Result = 0;   
   
  /**  消息包  **/   
  public byte[] dataPack = null;   
   
  /**Reserved   1**/   
  private byte ActiveReserved  = 0x00;   
   
  /**点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。**/   
  public String LinkID = "";   
   
   
  /************************CMPP_QUERY***********************************/   
  /** 时间YYYYMMDD(精确至日)。 **/   
  public String Time = "";   
   
  /** 查询类别：0：总数查询；1：按业务类型查询。 **/   
  public byte Query_Type = 0;   
   
  /** 查询码。当Query_Type为0时，此项无效；当Query_Type为1时，此项填写业务类型Service_Id. **/   
  public String Query_Code = "";   
   
  /************************CMPP_QUERY_RESP*****************************/   
  /** 从SP接收信息总数。 **/   
  public int MT_TLMsg = 0;   
   
  /** 从SP接收用户总数。 **/   
  public int MT_Tlusr = 0;   
   
  /** 成功转发数量。  **/   
  public int MT_Scs = 0;   
   
  /** 待转发数量。 **/   
  public int MT_WT = 0;   
   
  /** 转发失败数量。 **/   
  public int MT_FL = 0;   
   
  /** 向SP成功送达数量。 **/   
  public int MO_Scs = 0;   
   
  /** 向SP待送达数量。 **/   
  public int MO_WT = 0;   
   
  /** 向SP送达失败数量。 **/   
  public int MO_FL = 0;   
   
   
  /*****当ISMG向SP送交状态报告时，信息内容字段（Msg_Content）格式定义如下：**/   
  public long Report_Msg_Id = 0;   
   
  public String Report_Dest_terminal_Id = "";   
   
  /**发送短信的应答结果，含义详见表一。SP根据该字段确定CMPP_SUBMIT消息的处理状态。**/   
  public String Report_Stat = "";   
   
  /**YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）。 **/   
  public String Report_Submit_time = "";   
   
  /** YYMMDDHHMM。 **/   
  public String Report_Done_time   = "";   
   
  /**取自SMSC发送状态报告的消息体中的消息标识。**/   
  public int Report_SMSC_sequence  = 0;   
   
  /**********************CMPP CANSTANTS****************************/   
  public final static int CMPP_CONNECT         = 0x00000001 ;   //请求连接    
  public final static int CMPP_CONNECT_RESP    = 0x80000001 ;   //请求连接应答    
  public final static int CMPP_TERMINATE       = 0x00000002 ;   //终止连接    
  public final static int CMPP_TERMINATE_RESP  = 0x80000002 ;   //终止连接应答    
  public final static int CMPP_SUBMIT          = 0x00000004 ;   //提交短信    
  public final static int CMPP_SUBMIT_RESP     = 0x80000004 ;   //提交短信应答    
  public final static int CMPP_DELIVER         = 0x00000005 ;   //短信下发    
  public final static int CMPP_DELIVER_RESP    = 0x80000005 ;   //下发短信应答    
  public final static int CMPP_QUERY           = 0x00000006 ;   //发送短信状态查询    
  public final static int CMPP_QUERY_RESP      = 0x80000006 ;   //发送短信状态查询应答    
  public final static int CMPP_CANCEL          = 0x00000007 ;   //删除短信    
  public final static int CMPP_CANCEL_RESP     = 0x80000007 ;   //请求连接应答    
  public final static int CMPP_ACTIVE_TEST     = 0x00000008 ;   //激活测试    
  public final static int CMPP_ACTIVE_TEST_RESP= 0x80000008 ;   //激活测试应答    
   
   
  public final static String REPORT_DELIVRD = "Message is delivered to destination";   
  public final static String REPORT_EXPIRED = "Message validity period hasexpired";   
  public final static String REPORT_DELETED = "Message has been deleted";   
  public final static String REPORT_UNDELIV = "Message is undeliverable";   
  public final static String REPORT_ACCEPTD = "Message is in accepted state(i.e. has been manually read on behalf of the subscriber by customer service)";   
  public final static String REPORT_UNKNOWN = "Message is in invalid state";   
  public final static String REPORT_REJECTD = "Message is in a rejected state";   
   
  /**1  Unsigned Integer  
   * 双方协商的版本号(高位4bit表示主版本号,低位4bit表示次版本号)**/   
  private byte Version = (byte)CMPParameter.Version;   
   
   
  /**空构造器，用于parse**/   
  public CMPP() {   
   
  }   
   
   
  /** 用于pack的构造器**/   
  public CMPP(int CMPPMsgType) {   
    this.Command_Id  = CMPPMsgType;   
    this.Sequence_Id = getSeqNo();   
  }   
   
   
  /**  
   * 打包函数，返回打包后数据字节数组  
   * @return 打包后数据字节数组  
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
   
  /** 生成序列号 **/   
  public static int fmo_number  = 0;   
  private synchronized static int getSeqNo(){   
    int temp_SeqNo = fmo_number++;   
    if (fmo_number >= 100000000)   
        fmo_number  = 1;   
     return temp_SeqNo;   
  }   
   
   
  /**取得CMPP CMPP_ACTIVE_TEST的消息包  
   * 7.4.7  链路检测（CMPP_ACTIVE_TEST）操作  
   **/   
  private byte[] makeActiveMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
      this.Total_Length = 12;   
   
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeActiveMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**取得CMPP CMPP_ACTIVE_TEST_RESP的消息包  
   * 7.4.7.1 CMPP_ACTIVE_TEST_RESP定义（SP -> ISMG或ISMG->SP）  
   **/   
  private byte[] makeActiveRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
      this.Total_Length = 12 + 1;   
   
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //打包消息体    
      dataOutStream.writeByte(ActiveReserved);   
   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeActiveRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**取得CMPP CMPP_CONNECT的消息包  
   * 7.4.1.1 CMPP_CONNECT消息定义（SP->ISMG）  
   **/   
  private byte[] makeConnectMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
      this.Total_Length = 12 + 27;   
      this.Source_Addr  = CMPParameter.SP_Id;   
      String strTimestamp      = PublicFunction.getTimeStamp();   
      this.Timestamp           = Integer.parseInt(strTimestamp);  
      int psd_length = CMPParameter.SP_Pwd.length();
      this.AuthenticatorSource = Source_Addr +"000000000"+CMPParameter.SP_Pwd+strTimestamp; 
      System.out.println("认证源："+this.AuthenticatorSource);
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
       
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //打包消息体    
      writeString(dataOutStream,Source_Addr,6);  //写入源地址    
      //writeString(dataOutStream,AuthenticatorSource,16); //写入用于鉴别源地址的数据 
      dataOutStream.write(MD5.encrypt1(this.AuthenticatorSource1)); //写入用于鉴别源地址的数据
      dataOutStream.writeByte(Version);  //写入双方协商的版本号    
      dataOutStream.writeInt(Timestamp); //写入时间戳的明文    
   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray(); 
      
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeConnectMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**取得CMPP CMPP_CONNECT_RESP的消息包  
   * 7.4.1.2 CMPP_CONNECT_RESP消息定义（ISMG -> SP）  
   **/   
  private byte[] makeConnectRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
      this.Total_Length = 12 + 27;   
   
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //打包消息体    
      dataOutStream.writeByte(Status);  //写入连接状态    
      writeString(dataOutStream,AuthenticatorISMG,16); //写入用于鉴别源地址的数据    
      dataOutStream.writeByte(Version);  //写入双方协商的版本号    
   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP] makeConnectRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**取得CMPP CMPP_CONNECT_RESP的消息包  
   * 7.4.1.2 CMPP_CONNECT_RESP消息定义（ISMG -> SP）  
   **/   
  private byte[] makeDeliverRespMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
   
      if(Version == 48) this.Total_Length = 12 + 12;   
      if(Version != 48) this.Total_Length = 12 + 9;   
   
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //打包消息体    
      dataOutStream.writeLong(this.Msg_Id); //写入信息标识    
      if(Version == 48) dataOutStream.writeInt(this.Result); //写入结果    
      if(Version != 48) dataOutStream.writeByte(this.Result); //写入结果    
   
   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP]makeDeliverRespMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
   
   
  /** 打包CMPP_SUBMIT 消息  
   * 8.4.3.1    CMPP_SUBMIT消息定义（SP->ISMG）  
   **/   
  private byte[] makeSubmitMsgPack(){   
    ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();   
    DataOutputStream dataOutStream = new DataOutputStream(byteArrayOutStream);   
   
    try{   
      //MT消息长度    
   
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
   
      //2006-10-23修改，增加wap push功能      
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
   
      //打包MT消息报头    
      packHead(dataOutStream);   
   
      //打包消息体    
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
   
   
   
      if(Version == 48) writeString(dataOutStream,LinkID,20); //写入结果    
      if(Version != 48) writeString(dataOutStream,Reserved,8);   
      //返回消息的字节流    
      dataPack = byteArrayOutStream.toByteArray();   
      return this.dataPack;   
    }catch(Exception e){   
      e.printStackTrace();   
      System.out.println("[CMPP]makeSubmitMsgPack.pack() error : "+e.getMessage());   
      return null;   
    }   
  }   
   
   
  /**  
   * 打包消息头  
   * @param dataOutStream 由调用者传送来的数据输出流  
   */   
  private void packHead( DataOutputStream dataOutStream ) throws Exception   
  {   
    try {   
      dataOutStream.writeInt(Total_Length);  //写入包长度    
      dataOutStream.writeInt(Command_Id);    //写入命令或响应类型    
      dataOutStream.writeInt(Sequence_Id);   //写入消息流水号  
    } catch(IOException e) {   
      System.out.println("[CMPP] CMPP.packHead() thrown IOException"+e);   
      throw e;   
    }   
  }   
   
   
  /**  
   * 解析接受到的数据包，转换成CMPP MSG  
   * @param recvPack  
   * @return  
   */   
  public void parsePack(byte[] recvPack) throws Exception{   
   
    ByteArrayInputStream byteStream = new ByteArrayInputStream(recvPack);   
    DataInputStream dataInStream = new DataInputStream(byteStream);   
   
    //读取消息头    
    try {   
      this.Command_Id  =   dataInStream.readInt();             //消息类型    
      this.Sequence_Id =   dataInStream.readInt();             //消息流水号(可以用来完成消息的确认)    
    } catch(IOException e) {   
      System.out.println("[CMPP] CMPP.parseHead()  error : "+e);   
      throw e;   
    }   
   
    //读取消息体    
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
   * 解析CMPP_CONNECT_RESP信息包  
   * 7.4.1.2 CMPP_CONNECT_RESP消息定义（ISMG -> SP）  
   * @param dataInputStream  
   */   
   private void parseConnectRespPack( DataInputStream dataInputStream ) throws Exception   
   {   
     try{   
    	System.out.println("版本号： " + this.Version); 
       this.Status           = dataInputStream.readByte();   
       System.out.println("状态： " + this.Status);
       this.AuthenticatorISMG = readString(dataInputStream,16);   
       this.Version           = dataInputStream.readByte(); 
       System.out.println("版本号： " + this.Version); 
   
     }catch(Exception e){   
       System.out.println("[CMPP]CMPP.parseConnectRespPack error " + e);   
       throw e;   
     }   
   }   
   
   
   /**  
    * 解析CMPP_DELIVER信息包  
    * 8.4.5.1   CMPP_DELIVER消息定义（ISMG->SP）  
    * @param dataInputStream  
    */   
    private void parseDeliverPack( DataInputStream dataInputStream ) throws Exception   
    {   
      try{   
        this.Msg_Id     = dataInputStream.readLong();   
        //目的号码。SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。    
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
        //当为状态报告时候    
        if(Registered_Delivery == 1){   
          this.Report_Msg_Id  = dataInputStream.readLong();   
          this.Report_Stat    = CMPP.readString(dataInputStream,7);   
          this.Report_Submit_time         = CMPP.readString(dataInputStream,10);   
          this.Report_Done_time           = CMPP.readString(dataInputStream,10);   
          this.Report_Dest_terminal_Id    = CMPP.readString(dataInputStream,21);   
          this.Report_SMSC_sequence       = dataInputStream.readInt();   
        }   
        else{   
          //当字节数大于128时候    
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
     * 解析CMPP_SUBMIT_RESP信息包  
     * 8.4.3.2  CMPP_SUBMIT_RESP消息定义（ISMG à SP）  
     * @param dataInputStream  
     */   
     private void parseSubmitRespPack( DataInputStream dataInputStream ) throws Exception   
     {   
       try{   
         this.Msg_Id     = dataInputStream.readLong();   
         //目的号码。SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。    
   
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
   * 向数据输出流写入指定长度字符串，不足右补“0”  
   * @param dataOutStream 由调用者传送来的数据输出流  
   * @param str 写入的字符  
   * @param StrLen 指定长度  
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
     * 按长度,编码格式从字节流中读取指定长度的字符  
     * @param inStream :字节流  
     * @param StrLen   :长度  
     * @param fmt      :编码格式  
     * @return  
     */   
    private static String readDeliverString(DataInputStream inStream, int StrLen,   
                                            int fmt) {   
      String result = "";   
      byte[] readByte = new byte[1000];   
      int i = 0;   
      //判断字符串是否长于128位    
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
      //按编码格式进行转码    
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
        	 System.out.println("加密前认证源："+byteHEX(authenticatorSource[i]));
         }
         new MD5();   
         new XStream();
         byte[] test2 = MD5.encrypt1(authenticatorSource);   
         for(int i=0;i<test2.length;i++){
        	 System.out.println("加密后认证源："+byteHEX(test2[i]));
         }
    }
   
}   



