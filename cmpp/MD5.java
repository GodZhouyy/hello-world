package com.bettersoft.util.cmpp;

/**  
 * <P>Title: MD5 数据加密</P>  
 * <P>Description: 湖南移动短信网关通讯程序</P>  
 * <P>Copyright: Copyright (c) 2004</P>  
 * <P>Company: 深圳拜特科技有限公司.</P>  
 * <P>Reference Cmpp 2.0 protocol</P>  
 * @author cuiww  
 * @version 1.0  
 */   
/**  
 *  使用说明  
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
//传入要加密的字符串，此函数反回此串的16个字节的摘要密码字符串    
  public static String encrypt(String key){   
   String m = key ;   
   return computeDigest(m.getBytes()) ;   
  }   
   
  private static String computeDigest(byte[] b){   
    alg.reset();   
    alg.update(b);   
    byte[] hash = alg.digest(); //得到摘要 
    System.out.println("哈希长度"+hash.length);
    
    String d = new String(hash, 0, 0, hash.length);   
    System.out.println("哈希字符串"+d);
    return d;   
  }
//传入要加密的字符串，此函数反回此串的16个字节的摘要密码字符串    
  public static byte[] encrypt_byte(String key){   
   String m = key ;   
   alg.reset();   
   alg.update(m.getBytes());   
   return alg.digest(); //得到摘要    
  }
//传入要加密的字符串，此函数反回此串的16个字节的摘要密码字符串    
  public static byte[] encrypt1(byte[] b){     
   alg.reset();   
   alg.update(b);   
   return alg.digest(); //得到摘要    
  } 

}
   

