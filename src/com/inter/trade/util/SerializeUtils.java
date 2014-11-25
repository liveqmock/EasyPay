package com.inter.trade.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-14
 */
public class SerializeUtils {

    /**
     * deserialization from file
     * 
     * @param filePath
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch(Exception e) {
           e.printStackTrace();
           return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
    }

    /**
     * serialize to file
     * 
     * @param filePath
     * @param obj
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
    }
}
