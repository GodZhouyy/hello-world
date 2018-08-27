package com.bettersoft.util.cmpp;

/**  
 * <P>Title: PublicFunction</P>  
 * <P>Description: 公用函数</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0  
 */   
import java.util.Calendar;   
   
public class PublicFunction {   
  public PublicFunction() {   
  }   
   
  public static String getTimeStamp(){   
    Calendar now = Calendar.getInstance();   
    String mm = totwo(String.valueOf(now.get(Calendar.MONTH) + 1));   
    String dd = totwo(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));   
    String hh = totwo(String.valueOf(now.get(Calendar.HOUR_OF_DAY)));   
    String ff = totwo(String.valueOf(now.get(Calendar.MINUTE)));   
    String ss = totwo(String.valueOf(now.get(Calendar.SECOND)));   
    return mm + dd + hh + ff + ss;   
  }   
   
   
   
  /**  
   * 公有方法，将当前时间格式化,格式化为12/12 06:50  
   * @return String  
   */   
  public static String getFormatTime() {   
    Calendar now = Calendar.getInstance();   
    Integer.toString(now.get(Calendar.YEAR));   
    String mon = Integer.toString(now.get(Calendar.MONTH) + 1);   
    String day = Integer.toString(now.get(Calendar.DAY_OF_MONTH));   
    String hour = Integer.toString(now.get(Calendar.HOUR_OF_DAY));   
    String min = Integer.toString(now.get(Calendar.MINUTE));   
    String sec = Integer.toString(now.get(Calendar.SECOND));   
   
    mon = (mon.length() == 1) ? "0" + mon : mon;   
    day = (day.length() == 1) ? "0" + day : day;   
    hour = (hour.length() == 1) ? "0" + hour : hour;   
    min = (min.length() == 1) ? "0" + min : min;   
    sec = (sec.length() == 1) ? "0" + sec : sec;   
    return (mon + "-" + day + " " + hour + ":" + min + ":" + sec);   
  }   
   
  /** 当一个字符时转换成两个字符 **/   
  private static String totwo(String s){   
    if (s.length() < 2) {   
      s = "0" + s;   
      return s;   
    }   
    return s;   
  }   
   
   
  /**  
   * 接收数据  
   * @return byte[]:接收的数据(不包含总共长度)  
   *  receivedData     
   *  receivedData[0]=ERROR_SOCKET SOCKET出错   
   *  receivedData[0]=ERROR_INTERRUPT SOCKET超时中断   
   */   
  public static byte[] recv(java.io.DataInputStream dataInStream) throws Exception//接受的数据    
  {   
    byte[] receivedData;   
    try {   
      synchronized(dataInStream){   
        
    	int dataSize = dataInStream.readInt();   
        if( (dataSize<12)||(dataSize > 900) )   
        {   
          receivedData = new byte[1];   
          receivedData[0] = -1;   
          return receivedData;   
        }   
        receivedData = new byte[dataSize - 4];   
        int dataTotalSizeToRead = dataSize - 4;   
        int dataToReadLeft      = dataSize - 4;   
        int dataThisTimeRead;   
        while(dataToReadLeft > 0)   
        {   
        	dataThisTimeRead = dataInStream.read(receivedData,   
                                               dataTotalSizeToRead - dataToReadLeft,   
                                               dataToReadLeft); 
          dataToReadLeft  -= dataThisTimeRead;   
        }   
      }   
    }   
   /** catch(NullPointerException e)    
    {    
      System.out.println("[PublicFunction]接收数据 NullPointerException:"+e.toString());    
      receivedData = new byte[1];    
      receivedData[0] = -1;    
    }    
    catch(java.io.InterruptedIOException e)    
    {    
      System.out.println("[PublicFunction]接收数据 InterruptedIOException:"+e.toString());    
      receivedData = new byte[1];    
      receivedData[0] = -1;    
    }    
    catch(java.io.EOFException e){    
      System.out.println("[PublicFunction]接收数据 EOFException:"+e.toString());    
      receivedData = new byte[1];    
      receivedData[0] = -1;        }   */ 
    catch(Exception e)   
    {   
      System.out.println("[PublicFunction]接收数据 Exception:"+e.toString());   
      throw e;   
    }   
   
    String strDebug = "[Debug]receivedData:size="+receivedData.length+",content=";  
    StringBuffer sba = new StringBuffer();
    for(int i = 0;i < receivedData.length;i++){   
      sba.append("["+receivedData[i]+"]");   
    }   
    strDebug += sba;
    Debug.outDebug(strDebug);   
   
    return receivedData;   
  }   
   
   
  /**  
   * 发送消息包  
   * @param sm  
   * @return true or false  
   */   
  public static void send(   
      java.io.DataOutputStream dataOutStream,byte[] msgpack) throws Exception{   
    try {   
      if (msgpack == null) {   
        throw new Exception("pack null error");   
      }   
      if ( (msgpack.length > 900) || (msgpack.length < 12)) {   
        throw new Exception("pack length error, length>900 or length<12");   
      }   
      //同步    
      synchronized(dataOutStream){   
        dataOutStream.write(msgpack);   
        dataOutStream.flush();   
      }   
      String strDebug = "[Debug]msgpack:size="+msgpack.length+",content=";   
      StringBuffer sba = new StringBuffer();
      for(int i = 0;i < msgpack.length;i++){   
        sba.append("["+msgpack[i]+"]");   
      }  
      strDebug += sba;
      Debug.outDebug("下发相应");  
      Debug.outDebug(strDebug);   
   
    }   
    catch (Exception ex) {   
      throw ex;   
    }   
  }   
   
  /**  
   * 公有方法，线程休眠（单位：微秒）  
   * @param miniSec 线程休眠  
   */   
  public static void sleep(int miniSec) {   
    try {   
      Thread.sleep(miniSec);   
    }   
    catch (InterruptedException e) {   
      System.out.println("sleep interrupt ." + e.toString());   
    }   
  }   
   
   
  /**释放连接池的连接closeconn();  //关闭connection(放回连接池)**/   
  public static void discloseconn(java.sql.Connection conn,   
                           java.sql.Statement stmt,   
                           java.sql.ResultSet rst){   
    try{   
      if(rst != null){   
        rst.close();   
      }   
       if(conn != null){   
        conn.close();   
      }   
      if(stmt != null){   
        stmt.close();   
      }   
   }   
    catch (Exception sqle){   
      System.out.println("Database disconnecting failed"+sqle);   
    }   
  }   
   
   
}   

