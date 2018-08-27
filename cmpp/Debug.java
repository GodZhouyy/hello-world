package com.bettersoft.util.cmpp;

import java.util.*;   
import java.io.*;   
   
public class Debug {   
  private static short loger = 4; //定义日志级别    
  private static short level = 4; //定义控制级别    
   
  /*设置级别信息*/   
  public static void setLevel(short i) {   
    if (i >= 0 || i <= 4) {   
      outInfo("调试配置设置成功");   
      level = i;   
    }   
    else {   
      Exception e = new Exception("调试配置参数错误：超出范围");   
      outError(e);   
    }   
  }   
   
  /*输出错误信息*/   
  public static void outError(Exception e) {   
    StringBuffer strTemp = new StringBuffer(100);   
    loger = 1;   
    if (loger <= level) {   
      strTemp.append("Error:").append(e.getMessage());   
      System.out.println(strTemp.toString());   
      strTemp = null;   
    }   
  }   
   
  /*输出警告信息*/   
  public static void outWarn(String msg) {   
    StringBuffer strTemp = new StringBuffer(100);   
    loger = 2;   
    if (loger <= level) {   
      strTemp.append("Warning:").append(msg);   
      LogUtil.getInstance().info(strTemp.toString(),false);   
      System.out.println(strTemp.toString());   
      strTemp = null;   
    }   
  }   
   
  /*输出消息信息*/   
  public static void outInfo(String msg) {   
    StringBuffer strTemp = new StringBuffer(100);   
    loger = 3;   
    if (loger <= level) {   
      strTemp.append("").append(msg);//Information:    
      LogUtil.getInstance().info(strTemp.toString(),false);   
      System.out.println(strTemp.toString());   
      strTemp = null;   
    }   
  }   
   
   
  /*输出调试信息*/   
  public static void outDebug(String msg) {   
    StringBuffer strTemp = new StringBuffer(100);   
    loger = 5;   
    if (CMPParameter.IfDebug == 1) {   
      strTemp.append("[Debug]").append(msg);   
      System.out.println(strTemp.toString());   
      strTemp = null;   
    }   
   
  }   
   
   
  public static void log(String msg) throws Exception {   
    StringBuffer strTemp = new StringBuffer(100);   
    loger = 4;   
    if (loger <= level) {   
      File file = new File("c:/log.txt");   
      try {   
        if (!file.exists()) {   
          if (file.createNewFile()) {   
            BufferedWriter out = new BufferedWriter(new FileWriter("c:/log.txt", true));   
            strTemp.append("时间:").append(Calendar.getInstance().getTime()).   
                append("  ").append(msg).append("\n");   
            out.write(strTemp.toString());   
            out.close();   
          }   
        }   
        else {   
          BufferedWriter out = new BufferedWriter(new FileWriter("c:/log.txt", true));   
          strTemp.append("时间:").append(Calendar.getInstance().getTime()).append(   
              "  ").append(msg).append("\n");   
          out.write(strTemp.toString());   
          out.close();   
        }   
      }   
      catch (Exception ex) {   
        ex.printStackTrace();   
      }   
    }   
   
  }   
   
  /*****************以下是利用Log4J作的日志信息控制**********************/   
  /**  
   * 设定日志级别,1为最高级,必须调用。在TPMaster中已调用  
   * 利用Log4J的方法  
   */   
  /*  
     public static void setLevel(int i){  
    logger = Logger.getLogger(Debug.class.getName());  
    BasicConfigurator.configure ();  
    if (i==1) logger.setLevel(Level.ERROR);  
      else  
        if (i==2) logger.setLevel(Level.WARN);  
          else  
            if (i==3) logger.setLevel(Level.INFO);  
              else  
                logger.setLevel(Level.DEBUG);  
     }  
   */   
   
  /**  
   * 设定日志Level  
   */   
   
  /*  
     public static void ifDebug(){  
    //PropertyConfigurator.configure ( "Debug.properties" ) ;  
    Logger logger = Logger.getLogger(Debug.class.getName());  
    BasicConfigurator.configure ();  
    System.out.println("^^^^^^^^^^^^"+Level.DEBUG);  
    logger.debug("FFFFFFFFFFFFFFFFFFFFF") ;  
     }  
   */   
}   
