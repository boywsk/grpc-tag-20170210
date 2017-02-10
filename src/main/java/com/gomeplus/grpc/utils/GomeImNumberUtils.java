package com.gomeplus.grpc.utils;

/**
 *@Description:数字比较
 *@author liuzhenhuan
 *@date 2016年12月8日 上午10:19:10
 * 
 */
public class GomeImNumberUtils {

    public static boolean equals(Integer a, int b) {
        return a != null && a.intValue() == b;
    }
    public static boolean equals(int a, Integer b) {
    	return equals(b, a);
    }
    public static boolean equals(String a, Integer b) {
        return a == null ? b == null : String.valueOf(b).equals(a);
    }	
    public static boolean equals(Integer a, String b) {
    	return equals(b, a);
    }
    public static boolean equals(String a, Long b) {
    	return a == null ? b == null : String.valueOf(b).equals(a);
    }	
    public static boolean equals(Long a, String b) {
    	return equals(b, a);
    }
    
    public static boolean equals(Integer a, Integer b) {
    	return a == null ? b == null : a.equals(b);
    }
    
    public static String toString(Integer a, String defaultValue) {
        return a == null ? defaultValue : a.toString();
    }

}
