package com.woaiwangpai.iwb.utils;

import android.util.Log;

import com.woaiwangpai.iwb.MyApplication;

import java.util.Collection;
import java.util.Iterator;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:31
 * @Email : yiyajing8023@163.com
 * @Description : log定位相关类、行
 */

public class LogUtils {
    private static final boolean DEBUG = MyApplication.getInstance().isDebug();
    private static final int D = 745;
    private static final int E = 421;
    private static final int V = 674;
    private static final String CUT_OFF = "------------------------";
    private static final String CUT_OFF_END = "--------------------------------------------------------";
    private static final String SPACE_9 = " ";

    public static void d(String tag, String... values) {
        printf(D, tag, values);
    }

    public static void e(String tag, String... values) {
        printf(E, tag, values);
    }
    public static void v(String tag, String... values) {
        printf(V, tag, values);
    }


    /**
     * //String...可变长度数组 、固定长度数组String[]strs={};
     * @param mark
     * @param tag
     * @param values
     */
    private static void printf(int mark, String tag, String... values) {
        if (!DEBUG) {
            return;
        }
        //需要打印的内容
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            value.append(values[i]);
            if (i == values.length - 1) {
                break;
            }
            value.append(", ");
        }
        // 打印
        switch (mark) {
            case D:
                printfLine(D, tag);
                Log.d(tag, SPACE_9 + value.toString());
                Log.d(tag, CUT_OFF_END);
                break;
            case E:
                printfLine(E, tag);
                Log.e(tag, SPACE_9 + value.toString());
                Log.e(tag, CUT_OFF_END);
                break;
            case V:
                printfLine(V, tag);
                Log.v(tag, SPACE_9 + value.toString());
                Log.v(tag, CUT_OFF_END);
                break;
        }
    }

    private static void printfLine(int mark, String tag) {
        String startLine = createLog() + CUT_OFF;
        switch (mark) {
            case D:
                Log.d(tag, " ");
                Log.d(tag, startLine);
                break;
            case E:
                Log.e(tag, " ");
                Log.e(tag, startLine);
                break;
            case V:
                Log.v(tag, " ");
                Log.v(tag, startLine);
                break;
        }
    }
//
    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void i(String TAG, String msg) {
//        if (DEBUG) {
//            int strLength = msg.length();
//            int start = 0;
//            int end = LOG_MAXLENGTH;
//            String startLine = createLog() + CUT_OFF;
//            Log.i(TAG, startLine);
//            for (int i = 0; i < 100; i++) {
//                //剩下的文本还是大于规定长度则继续重复截取并输出
//                if (strLength > end) {
//                    Log.i(TAG, msg.substring(start, end));
////                    Log.i(TAG + i, msg.substring(start, end));
//                    start = end;
//                    end = end + LOG_MAXLENGTH;
//                } else {
//                    Log.i(TAG, msg.substring(start, strLength));
////                    Log.i(TAG, msg.substring(start, strLength));
//                    break;
//                }
//            }
//            Log.i(TAG, CUT_OFF_END);
//        }
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(TAG, createLog());
        Log.i(TAG, msg);
    }
    //
    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }
    private static String createLog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        return buffer.toString();
    }
    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!DEBUG)
            return;
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog()+CUT_OFF);
        Log.e(className, ""+message);
        Log.e(className, CUT_OFF_END);
    }
    public static void e(String tag, String values,Throwable tr) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag, createLog()+CUT_OFF);
        Log.e(tag, ""+values,tr);
        Log.e(tag, CUT_OFF_END);
    }
    public static void i(String message) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }
//    public static void i(String msg) {
//        if (DEBUG) {
//            getMethodNames(new Throwable().getStackTrace());
//            int strLength = msg.length();
//            int start = 0;
//            int end = LOG_MAXLENGTH;
//            for (int i = 0; i < 100; i++) {
//                //剩下的文本还是大于规定长度则继续重复截取并输出
//                if (strLength > end) {
//                    Log.i(className, createLog(msg.substring(start, end)));
//                    start = end;
//                    end = end + LOG_MAXLENGTH;
//                } else {
//                    Log.i(className, createLog(msg.substring(start, strLength)));
//                    break;//结束循环
//                }
//            }
//        }
//    }
    public static void d(String message) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }
//
    public static void log(Object message) {
        StackTraceElement element = new Throwable().getStackTrace()[1];
        print(element, message, null);
    }
    public static void log(Object message, Throwable error) {
        StackTraceElement element = new Throwable().getStackTrace()[1];
        print(element, message, error);
    }

    public static void debug(Object message) {
        if (DEBUG) {
            StackTraceElement element = new Throwable().getStackTrace()[1];
            print(element, message, null);
        }
    }
    public static void debug(Object message, Throwable error) {
        if (DEBUG) {
            StackTraceElement element = new Throwable().getStackTrace()[1];
            print(element, message, error);
        }
    }
    private static void print(StackTraceElement element, Object message, Throwable error) {
        String className = element.getClassName();
        className = className.substring(className.lastIndexOf('.') + 1);
        String tag = className+'.'+element.getMethodName()+'('+element.getFileName()+':'+element.getLineNumber()+')';
        String text = toString(message);

        if (error != null) {
            Log.e("[myLog]", tag + "\n\t" + text, error);
        } else {
            Log.e("[myLog]", tag + "\n\t" + text);
        }
    }

    private static String toString(Object message) {
        if (message == null) {
            return "[null]";
        }
        if (message instanceof Throwable) {
            return Log.getStackTraceString((Throwable) message);
        }
        if (message instanceof Collection) {
            return toString((Collection) message);
        }
        return String.valueOf(message);
    }
    private static String toString(Collection message) {
        Iterator it = message.iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            Object e = it.next();
            sb.append(e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(",\n ");
        }
    }

}
