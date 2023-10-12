package com.example.shopdms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

   // 获得MD5摘要算法的 MessageDigest 对象
   private static MessageDigest _mdInst = null;
   private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static MessageDigest getMdInst() {
       if (_mdInst == null) {
           try {
               _mdInst = MessageDigest.getInstance("MD5");
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           }
       }
       return _mdInst;
   }

   private MD5Util() {
   }

   /**
    * Returns a MessageDigest for the given <code>algorithm</code>.
    *
    * @return An MD5 digest instance.
    * @throws RuntimeException when a {@link NoSuchAlgorithmException} is caught
    */

   static MessageDigest getDigest() {
       try {
           return MessageDigest.getInstance("MD5");
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e);
       }
   }

   /**
    * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
    *
    * @param data Data to digest
    * @return MD5 digest
    */
   public static byte[] md5(byte[] data) {
       return getDigest().digest(data);
   }

   /**
    * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
    *
    * @param data Data to digest
    * @return MD5 digest
    */
   public static byte[] md5(String data) {
       return md5(data.getBytes());
   }

   /**
    * Calculates the MD5 digest and returns the value as a 32 character hex string.
    *
    * @param data Data to digest
    * @return MD5 digest as a hex string
    */
   /*public static String md5Hex(byte[] data) {
       return HexUtil.toHexString(md5(data));
   }*/

   /**
    * Calculates the MD5 digest and returns the value as a 32 character hex string.
    *
    * @param data Data to digest
    * @return MD5 digest as a hex string
    */
   /*public static String md5Hex(String data) {
       return HexUtil.toHexString(md5(data));
   }*/

   /**
    * 对密码字符串进行MD5加密
    *
    * @param pass
    * @return
    */
   public static String encoding(String pass) {
       if (pass == null) {
           return "";
       } else {
           StringBuffer buf = null;
           MessageDigest md5 = null;
           try {
               // 获取md5摘要
               md5 = MessageDigest.getInstance("MD5");
               // 指定utf-8编码
               md5.update(pass.getBytes("utf-8"));
               byte[] b = md5.digest();
               int i;
               buf = new StringBuffer("");
               for (int offset = 0; offset < b.length; offset++) {
                   i = b[offset];
                   if (i < 0) {
                       i += 256;
                   }
                   if (i < 16) {
                       buf.append("0");
                   }
                   // 产生16进制保存
                   buf.append(Integer.toHexString(i));
               }
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           return buf.toString();
       }
   }

   public final static String encode(String s) {
       try {
           byte[] btInput = s.getBytes();
           // 使用指定的字节更新摘要
           getMdInst().update(btInput);
           // 获得密文
           byte[] md = getMdInst().digest();
           // 把密文转换成十六进制的字符串形式
           int j = md.length;
           char str[] = new char[j * 2];
           int k = 0;
           for (int i = 0; i < j; i++) {
               byte byte0 = md[i];
               str[k++] = hexDigits[byte0 >>> 4 & 0xf];
               str[k++] = hexDigits[byte0 & 0xf];
           }
           return new String(str);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }
}