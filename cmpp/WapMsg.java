package com.bettersoft.util.cmpp;

public class WapMsg {   
	  public WapMsg() {   
	  }   
	   
	  /**  
	   Reference <<>>  
	   A.3 Combined Use of Headers  
	   The figures below illustrate the use of the User Data Header framework and the various Information Elements  
	   defined for WDP. A datagram always contains the port numbers for application level routing, and optionally (if  
	   segmentation and reassembly is needed) contains also the adaptation layer.  
	   0 1 2 3 4 5 6 7  
	   Length of total User Data Header (all Information Elements)  
	   UDH IE identifier: Port numbers (5)  
	   UDH port number IE length (4)  
	   Destination Port (High)  
	   Destination Port (Low)  
	   Originator Port (High)  
	   Originator Port (Low)  
	   UDH IE identifier: SAR (0)  
	   UDH SAR IE length (3)  
	   Datagram Reference number  
	   Total number of segments in Datagram  
	   Segment count  
	   Padding Bits if User Data uses 7 bit alphabet  
	   1 - n bytes of User Data  
	   Figure A.3: A complete datagram header with 8 bit reference and 16 bit addressing scheme for  
	   WDP in GSM SMS  
	  
	  
	   0 1 2 3 4 5 6 7  
	   Length of total User Data Header (all Information Elements)  
	   UDH IE identifier: Port numbers (5)  
	   UDH port number IE length (4)  
	   Destination Port (High)  
	   Destination Port (Low)  
	   Originator Port (High)  
	   Originator Port (Low)  
	   Padding Bits if User Data uses 7 bit alphabet  
	   1 - n bytes of User Data  
	   Figure A.4: A datagram header without SAR for WDP in GSM SMS  
	  
	   本程序使用Figure A.3 with 8 bit reference and 16 bit addressing scheme for  
	   WDP in GSM SMS  
	   */   
	  public static String getSMSPush(String complexContent) throws Exception{   
	     String url = "";   
	     String subject = "";   
	   
	     if(complexContent.indexOf("$wappush")>-1){   
	       String urlFlag = "$url=";   
	       String subjectFlag = "$subject=";   
	   
	       int iWapIdIndex    = complexContent.indexOf("$wappush");   
	       int iUrlStartIndex = complexContent.indexOf(urlFlag,iWapIdIndex);   
	       int iSubjectStartIndex = complexContent.indexOf(subjectFlag,iUrlStartIndex);   
	   
	       url = complexContent.substring(iUrlStartIndex+urlFlag.length(),iSubjectStartIndex);   
	       subject  = complexContent.substring(iSubjectStartIndex +subjectFlag.length());   
	   
	       return getSMSPush(url,subject);   
	     }   
	   
	     throw new Exception("MsgContent Format Error......");   
	  }   
	   
	  public static String getSMSPush(String url, String subject) {   
	    String pushString = "";   
	    String body = "";   
	    body += "02";   
	    body += "05"; //-//WAPFORUM//DTD SI 1.0//EN    
	    body += "6A"; //UTF-8    
	    body += "00"; //字符串结束    
	    body += "45"; //<SI>    
	    body += "C6"; //<INDICATION <action="signal-high" ; +="08" body>    
	    body += "0C"; //href="http://    
	    body += "03"; //字符串开始    
	    body += byteArrayToHexString(url.getBytes()); //实际地址    
	   
	    body += "00"; //字符串结束    
//	    body += "0A"; //created=    
//	    body += "C3"; //'时间    
//	    body += "07"; //时间字节数    
//	    body += startTime; //YYYYMMDDHHMMSS    
//	    body += "10"; //si_expires=    
//	    body += "C3"; //时间    
//	    body += "07"; //时间字节数    
//	    body += endTime; //YYYYMMDDHHMMSS    
	    body += "01"; //>    
	    body += "03"; //字符串开始    
	    try {   
	      body += byteArrayToHexString(subject.getBytes("UTF-8")); //显示给用户的内容，用utf-8编码。utf-8编码，英文字符直接用ascii码；中文如果unicode是（二进制）    
	                        //System.out.println(subject.getBytes("UTF-8"));    
	    } catch (Exception ex) {   
	    }   
	   
	    body += "00"; //字符串结束    
	    body += "01"; //</INDICATION>"    
	    body += "01"; //'</SI>    
	    body.length();   
	    String pud = "";   
	    pud += "29"; //transaction id (connectionless WSP) （reference <<WAP-203-WSP>> p.69）    
	    pud += "06"; //pdu type (06=push)    
	    pud += "04"; //Headers len    
	    pud += "03";   
	    pud += "AE";   
	    pud += "81";   
	    pud += "EA"; //content type: application/vnd.wap.sic; charset=utf-8    
//	    pud += "8D";    
//	    pud += Integer.toHexString(length).toUpperCase(); //content-length    
	    //pud += "CA"; //    
	    String udh = "";   
	    udh += "0B"; //User Data Header Length (11 bytes(with sar) or 6 bytes(without sar))    
	    udh += "05"; //UDH Item Element id (Port Numbers)    
	    udh += "04"; //UDH IE length (4 bytes)    
	    udh += "0B"; //destination port number ,high    
	    udh += "84"; //destination port number ,low    
	    udh += "23"; //origin port number ,high    
	    udh += "F0"; //origin port number ,low    
	    udh += "00"; //UDH IE identifier: SAR (0)    
	    udh += "03"; //UDH SAR IE length (3)    
	    udh += "03"; //Datagram Reference number    
	    udh += "01"; //Total number of segments in Datagram    
	    udh += "01"; //Segment count    
	    pushString = udh + pud + body;   
	    return pushString;   
	  }   
	   
	  public static String byteArrayToHexString(byte[] b) {   
	    String result = "";   
	    StringBuffer sba = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {   
	      sba.append(byteToHexString(b[i]));   
	    }   
	    result += sba;
	    return result;   
	  }   
	   
	  public static String byteToString(byte[] b) {   
	    String result = ""; 
	    StringBuffer sba = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {   
	    	sba.append(b[i]);   
	    }   
	    result += sba;
	    return result;   
	  }   
	   
	  public static String byteToHexString(byte b) {   
	    int n = b;   
	    if (n < 0) {   
	      n = 256 + n;   
	    }   
	    int d1 = n / 16;   
	    int d2 = n % 16;   
	    return HexCode[d1] + HexCode[d2];   
	  }   
	   
	  private static String[] HexCode = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",   
	                                      "A", "B", "C", "D", "E", "F"};   
	   
	  public static String toByteString(String str){   
	    return str;
	  }
	  
}
	   
//	    for(int i=0;i<STR.LENGTH() + System.out.print((0x0B System.out.println(); } ?); ? System.out.print(Integer.parseInt(str.substring(i*2,i*2+2),16) i="0;" for(int 0xff) & (cc[2*i+1] ) 0x0f)<<4 (cc[2*i] System.out.print(( +? cc[2*i+1]) cc[2*i]) 2]; byte[cc.length bb="new" byte[] cc="str.toCharArray();" char[] ex){} catch(Exception str.getBytes()[2*i+1]); 4 << 0x0f) ((str.getBytes()[2*i] System.out.print( { i++) 2; str.getBytes().length < (int for try{ str.charAt(2*i+1)+? System.out.print(str.charAt(2*i)+??+ 2;i++){>   









