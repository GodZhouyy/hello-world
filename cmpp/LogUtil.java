package com.bettersoft.util.cmpp;

/** 
 * <P>Title: 写日志</P> 
 * <P>Description: useage: Log.getInstance().info/error</P> 
 * 所有错误日志使用Log.getInstance().error()方法，写入yyyymmdd.log； 
 * 调试信息使用Log.getInstance().info(,,)方法，写入yyyymmdd.log； 
 * 打印16进制报文使用Log.getInstance().packDebug()方法，写入packyyyymmdd.log； 
 * <P>Copyright: Copyright (c) 2002</P> 
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0 
 */  
  
import java.io.*;  
import java.util.*;  
  
public class LogUtil {  
  private static LogUtil LogInst = null;  
  private static String pathStr;  
  private Calendar now;  
  private FileWriter fw;  
  private String nameStr;  
  
  private static int debugLevel = 3;  
  private boolean fileOpen = false;  
  
  public static LogUtil getInstance(){  
    if (LogInst == null )  
      LogInst  = new LogUtil(CMPParameter.LogFilePath);  
    return LogInst ;  
  }  
  
  public static void setLogPath(String logpath){  
    pathStr = logpath;  
  }  
  
  public static void setDebugLevel(int i){  
    debugLevel =i ;  
  }  
  
    /** 
     * 构造方法. 
     * @param logPath 日志路径 
     */  
    public LogUtil(String logPath) {  
      pathStr = logPath;  
      try{  
        File p = new File(pathStr);  
        if (!p.exists())  
          p.mkdirs();  
  
        // Get current time parameter   
        now = Calendar.getInstance();  
        String yyyy = String.valueOf(now.get(Calendar.YEAR));  
        String mm = String.valueOf(now.get(Calendar.MONTH)+1);  
        String dd = String.valueOf(now.get(Calendar.DAY_OF_MONTH));  
        mm = (1==mm.length()) ? ("0"+mm) : mm;  
        dd = (1==dd.length()) ? ("0"+dd) : dd;  
        nameStr = yyyy + mm + dd;  
  
        // Make today's log file full name.   
        String fileName = pathStr + "/"+ nameStr + ".log";  
        fw = new FileWriter(fileName,true);  
        fileOpen = true;  
      }catch (Exception e){  
        System.out.println("Create Log :"+e.toString() );  
      }  
      finally{  
        fwClose();  
        fileOpen = false;  
      }  
    }  
  
    /** 
     * 将数据包msg以16进制写入日志文件. 
     * @param note 说明串(说明从数据流向) 
     * @param msg  数据包 
     */  
    public void packDebug(String note, byte[] msg) {  
            writePackLog(note, msg);  
    }  
    /** 
     * 写普通日志信息. 不判断调试标志 
     * @param msg 写入内容 
     * @param addtimeflag 是否在内容前添加时间 
     */  
    public void info(String msg,boolean addtimeflag) {  
          writeToTodayLog("", msg, addtimeflag);  
    }  
  
    /** 
     * 写普通日志信息. 判断调试标志 
     * @param msg 写内容 
     * @param addtimeflag 是否在内容前添加时间 
     * @param debug 调试等级，当debug <= debugLevel(常量)输出信息 
     * debug =0 :无调试信息 
     * debug =1 :简单调试信息(显示处理过程) 
     * debug =2 :详细调试信息(显示处理数据) 
     */  
    public void info(String msg,boolean addtimeflag, int debug) {  
        if(debug<=debugLevel) {  
              writeToTodayLog("", msg,addtimeflag);  
        }  
    }  
    /** 
     * 写普通日志信息. 判断调试标志 
     * @param msg 写内容 
     * @param debug 调试等级，当debug <= debugLevel输出 
     */  
    public void info(String msg,int debug) {  
        if(debug<=debugLevel) {  
              writeToTodayLog("", msg,true);  
        }  
    }  
  
    /** 
     * 写警告信息. 不判断调试标志 
     * @param msg 写内容 
     */  
    public void warning(String msg) {  
            writeToTodayLog("WARNING", msg,true);  
    }  
  
    /** 
     * 写错误信息. 不判断调试标志 
     * @param msg 写内容 
     */  
    public void error(String msg) {  
            writeToTodayLog("ERROR", msg,true);  
    }  
  
    /** 
     * 写信息入分为今天的日志文件. 
     * 
     * @param flag 写入标记 
     * @param msg  写入内容 
     */  
    private synchronized void writeToTodayLog(String flag,String msg,boolean timeflag) {  
            try {  
                // Get current time parameter   
                now = Calendar.getInstance();  
                String yyyy = String.valueOf(now.get(Calendar.YEAR));  
                String mm = String.valueOf(now.get(Calendar.MONTH)+1);  
                String dd = String.valueOf(now.get(Calendar.DAY_OF_MONTH));  
                String hh = String.valueOf(now.get(Calendar.HOUR_OF_DAY));  
                String ff = String.valueOf(now.get(Calendar.MINUTE));  
                String ss = String.valueOf(now.get(Calendar.SECOND));  
                mm = (1==mm.length()) ? ("0"+mm) : mm;  
                dd = (1==dd.length()) ? ("0"+dd) : dd;  
  
                String yyyymmdd = yyyy + mm + dd;  
                if ( ( ! yyyymmdd.equals(nameStr) )||(fileOpen==false) ) {  
                        fwClose();  
                        fileOpen = false;  
                        nameStr = yyyymmdd;  
                        String fileName = pathStr + nameStr + ".log";  
  
  
                        fw = new FileWriter(fileName,true);  
                        fileOpen = true;  
                }  
  
                String hhffss =  hh + ":" + ff + ":" + ss;  
                if( flag != "" )  
                        hhffss = flag + ": " + hhffss ;  
                if(timeflag)  
                  fw.write("\n\n"+ hhffss+" :  "+msg) ;  
                else  
                  fw.write("\n"+msg);  
                  fw.flush() ;  
            } catch(Exception ioe) {  
                System.out.println("write To Today Log: "+ioe);  
                fwClose();  
                fileOpen = false;  
            }  
            finally{  
              fwClose();  
              fileOpen = false;  
            }  
    }  
    /** 
       * 写数据包入包日志文件. 
       * 
       * @param note 说明串 
       * @param pack 数据包 
       */  
    private synchronized void writePackLog(String note,byte[] pack) {  
  
            FileWriter fp = null;  
            try {  
              // Get current time parameter   
              now = Calendar.getInstance();  
              String yyyy = String.valueOf(now.get(Calendar.YEAR));  
              String mm = String.valueOf(now.get(Calendar.MONTH)+1);  
              String dd = String.valueOf(now.get(Calendar.DAY_OF_MONTH));  
              String hh = String.valueOf(now.get(Calendar.HOUR_OF_DAY));  
              String ff = String.valueOf(now.get(Calendar.MINUTE));  
              String ss = String.valueOf(now.get(Calendar.SECOND));  
              mm = (1==mm.length()) ? ("0"+mm) : mm;  
              dd = (1==dd.length()) ? ("0"+dd) : dd;  
  
              String yyyymmdd = yyyy + mm + dd;  
              String pnameStr = "pack"+yyyymmdd;  
              String fileName = pathStr + pnameStr + ".log";  
              fp = new FileWriter(fileName,true);  
  
              String hhffss =  hh + ":" + ff + ":" + ss;  
              if( !"".equals(note) )  
                      hhffss = note + " \t" + hhffss ;  
              fp.write("\n"+hhffss +"\n");  
  
              for( int i =0; i<= (pack.length/16) ; i++ ) {  
                  for( int j =0; j<16 ; j++ ) {  
                      fp.write(" ");  
                      if( i*16+j < pack.length ) {  
                              String sss = Integer.toHexString(pack[i*16+j]);  
                              if ( sss.length()==1 )  
                                      sss = " "+sss;  
                              else if ( sss.length() == 8 )  
                                      sss = sss.substring(6,8) ;  
                              fp.write(sss);  
                              //fp.write(Integer.toHexString(pack[i*8+j]) );   
                      }  
                      else  
                              break;  
                  }  
                  fp.write("\n");  
              }  
              fp.write("\n");  
              fp.close() ;  
  
            } catch(IOException ioe) {  
                System.out.println("writePackLog: "+ioe);  
                try {  
                        fp.close();  
                } catch (Exception e) {}  
            }  
    }  
  
    /** 
     * 关闭日志文件 
     */  
    private void fwClose() {  
            try {  
                    fw.close();  
            } catch (IOException e) {
            	e.printStackTrace();
            }  
    }  
  
  
    /** 
     * main funcation used to be tested writing log file 
     * @param args 
     */  
    public static void main(String[] args)  
    {  
      LogUtil  test=new LogUtil("d:/LogTest");  
      test.info("test",true);  
    }  
}  
