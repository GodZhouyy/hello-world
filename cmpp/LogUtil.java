package com.bettersoft.util.cmpp;

/** 
 * <P>Title: д��־</P> 
 * <P>Description: useage: Log.getInstance().info/error</P> 
 * ���д�����־ʹ��Log.getInstance().error()������д��yyyymmdd.log�� 
 * ������Ϣʹ��Log.getInstance().info(,,)������д��yyyymmdd.log�� 
 * ��ӡ16���Ʊ���ʹ��Log.getInstance().packDebug()������д��packyyyymmdd.log�� 
 * <P>Copyright: Copyright (c) 2002</P> 
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
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
     * ���췽��. 
     * @param logPath ��־·�� 
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
     * �����ݰ�msg��16����д����־�ļ�. 
     * @param note ˵����(˵������������) 
     * @param msg  ���ݰ� 
     */  
    public void packDebug(String note, byte[] msg) {  
            writePackLog(note, msg);  
    }  
    /** 
     * д��ͨ��־��Ϣ. ���жϵ��Ա�־ 
     * @param msg д������ 
     * @param addtimeflag �Ƿ�������ǰ���ʱ�� 
     */  
    public void info(String msg,boolean addtimeflag) {  
          writeToTodayLog("", msg, addtimeflag);  
    }  
  
    /** 
     * д��ͨ��־��Ϣ. �жϵ��Ա�־ 
     * @param msg д���� 
     * @param addtimeflag �Ƿ�������ǰ���ʱ�� 
     * @param debug ���Եȼ�����debug <= debugLevel(����)�����Ϣ 
     * debug =0 :�޵�����Ϣ 
     * debug =1 :�򵥵�����Ϣ(��ʾ�������) 
     * debug =2 :��ϸ������Ϣ(��ʾ��������) 
     */  
    public void info(String msg,boolean addtimeflag, int debug) {  
        if(debug<=debugLevel) {  
              writeToTodayLog("", msg,addtimeflag);  
        }  
    }  
    /** 
     * д��ͨ��־��Ϣ. �жϵ��Ա�־ 
     * @param msg д���� 
     * @param debug ���Եȼ�����debug <= debugLevel��� 
     */  
    public void info(String msg,int debug) {  
        if(debug<=debugLevel) {  
              writeToTodayLog("", msg,true);  
        }  
    }  
  
    /** 
     * д������Ϣ. ���жϵ��Ա�־ 
     * @param msg д���� 
     */  
    public void warning(String msg) {  
            writeToTodayLog("WARNING", msg,true);  
    }  
  
    /** 
     * д������Ϣ. ���жϵ��Ա�־ 
     * @param msg д���� 
     */  
    public void error(String msg) {  
            writeToTodayLog("ERROR", msg,true);  
    }  
  
    /** 
     * д��Ϣ���Ϊ�������־�ļ�. 
     * 
     * @param flag д���� 
     * @param msg  д������ 
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
       * д���ݰ������־�ļ�. 
       * 
       * @param note ˵���� 
       * @param pack ���ݰ� 
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
     * �ر���־�ļ� 
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
