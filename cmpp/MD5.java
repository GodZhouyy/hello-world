package com.bettersoft.util.cmpp;

/**  
 * <P>Title: MD5 ���ݼ���</P>  
 * <P>Description: �����ƶ���������ͨѶ����</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: ���ڰ��ؿƼ����޹�˾.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0  
 */   
/**  
 *  ʹ��˵��  
 * 1.   AuthenticatorICP  = MD5(Source_Addr+ 36(\0) + shared secret ) ;  
 * 2.   AuthenticatorISMG = MD5(Status+AuthenticatorICP+Tls_available+shared secret);  
 **/   
   
import java.security.*;   
   
public class MD5 {   
   
  private static MessageDigest alg ;   
   
   
  public MD5() {   
   try{   
     alg = MessageDigest.getInstance("MD5");   
   }   
   catch(NoSuchAlgorithmException e){   
      System.out.println("MD5 has error="+e);   
   }   
  } 
//����Ҫ���ܵ��ַ������˺������ش˴���16���ֽڵ�ժҪ�����ַ���    
  public static String encrypt(String key){   
   String m = key ;   
   return computeDigest(m.getBytes()) ;   
  }   
   
  private static String computeDigest(byte[] b){   
    alg.reset();   
    alg.update(b);   
    byte[] hash = alg.digest(); //�õ�ժҪ 
    System.out.println("��ϣ����"+hash.length);
    
    String d = new String(hash, 0, 0, hash.length);   
    System.out.println("��ϣ�ַ���"+d);
    return d;   
  }
//����Ҫ���ܵ��ַ������˺������ش˴���16���ֽڵ�ժҪ�����ַ���    
  public static byte[] encrypt_byte(String key){   
   String m = key ;   
   alg.reset();   
   alg.update(m.getBytes());   
   return alg.digest(); //�õ�ժҪ    
  }
//����Ҫ���ܵ��ַ������˺������ش˴���16���ֽڵ�ժҪ�����ַ���    
  public static byte[] encrypt1(byte[] b){     
   alg.reset();   
   alg.update(b);   
   return alg.digest(); //�õ�ժҪ    
  } 

}
   

