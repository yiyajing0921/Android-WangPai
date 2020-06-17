package com.woaiwangpai.iwb.constant.crash;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 *
 */

public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * Debug״Release
	 * */
	public static final boolean DEBUG = true;
	/** UncaughtException */
	/** Properties */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** */
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	// CrashHandler 实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的 Context 对象
	private Context mContext;
	// 系统默认的 UncaughtException 处理类
	private UncaughtExceptionHandler mDefaultHandler;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	// 用来显示Toast中的信息获取到
	private static String error = "程序异常，请稍后再试";//还没获取到具体的crash
	private static final Map<String, String> regexMap = new HashMap<String, String>();
	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
	/** 保证只有一个 CrashH 实例 */
	private CrashHandler() {
//
	}
	/** 获取 CrashHandler 实例 ,单例模式 */
	public static CrashHandler getInstance() {
		initMap();
		return INSTANCE;
	}
	/**
	 * 初始化
	 *
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
// 设置该 CrashH 为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		LogUtils.d("TEST", "Crash:init");
	}
	/**
	 * 当 UncaughtException 发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
			LogUtils.d("TEST", "defalut");
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				LogUtils.e(TAG,  e.getMessage());
			}
// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
// mDefaultHandler.uncaughtException(thread, ex);
			System.exit(1);
		}
	}
	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 *
	 * @param ex
	 * @return true：如果处理了该异常信息；否则返回 false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
// 收集设备参数信息
// collectDeviceInfo(mContext);
// 保存日志文件
		saveCrashInfoFile(ex);
// 使用 Toast 来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				ToastUtils.show( error);
				Looper.loop();
			}
		}.start();
		return true;
	}
	/**
	 * 收集设备参数信息
	 *
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			LogUtils.e(TAG,  e.getMessage());
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				LogUtils.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				LogUtils.e(TAG, e.getMessage());
			}
		}
	}
	/**
	 * 保存错误信息到文件中 *
	 *
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfoFile(Throwable ex) {
		StringBuffer sb = getTraceInfo(ex);
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-"+time+"-"+timestamp+".log";//time拼接
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory() + "/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			LogUtils.e(TAG,  e.getMessage());
		}
		return null;
	}
	/**
	 * 整理异常信息
	 * @param e
	 * @return
	 */
	public static StringBuffer getTraceInfo(Throwable e) {
		StringBuffer sb = new StringBuffer();
		Throwable ex = e.getCause() == null ? e : e.getCause();
		StackTraceElement[] stacks = ex.getStackTrace();
		for (int i =0; i < stacks.length; i++) {
			if (i ==0) {
				setError(ex.toString());
			}
			sb.append("class: ").append(stacks[i].getClassName())
					.append("; method: ").append(stacks[i].getMethodName())
					.append("; line: ").append(stacks[i].getLineNumber())
					.append("; Exception: ").append(ex.toString() + "\n");
		}
		LogUtils.d(TAG, sb.toString());
		return sb;
	}
	/**
	 * 设置错误的提示语
	 * @param e
	 */
	public static void setError(String e) {
		Pattern pattern;
		Matcher matcher;
		for (Entry<String, String> m : regexMap.entrySet()) {
			LogUtils.d(TAG, e+"key:" + m.getKey() + "; value:" + m.getValue());
			pattern = Pattern.compile(m.getKey());
			matcher = pattern.matcher(e);
			if(matcher.matches()){
				error = m.getValue();//捕捉异常
				break;
			}
		}
	}
	/**
	 * 初始化错误的提示语
	 */
	private static void initMap() {
		regexMap.put(".*NullPointerException.*", "空指针异常：NullPointerException!");
		regexMap.put(".*ClassNotFoundException.*", "类未找到异常ClassNotFoundException");
		regexMap.put(".*NoSuchMethodException.*", "方法未找到异常：NoSuchMethodException");
		regexMap.put(".*FileNotFoundException.*", "文件未找到异常：java.lang.FileNotFoundException");
		regexMap.put(".*ClassCastException.*", "类型强制转换异常:ClassCastException ");
		regexMap.put(".*ArithmeticException.*", "算术异常:ArithmeticException被除数为整型时（short、int、long）不可为零");
		regexMap.put(".*ArrayIndexOutOfBoundsException.*", "数组下标越界异常:java.lang.ArrayIndexOutOfBoundsException");
		regexMap.put(".*NegativeArrayException.*", "数组负下标异常:java.lang.NegativeArrayException");
		regexMap.put(".*IllegalArgumentException.*", "参数不合法IllegalArgumentException");
		regexMap.put(".*IllegalAccessException.*", "访问权限异常IllegalAccessException是否调用了private方法");
		regexMap.put(".*SecturityException.*", "违背安全原则异常SecturityException");
		regexMap.put(".*NumberFormatException.*", "字符串转换为数字异常：NumberFormatException");
		regexMap.put(".*OutOfMemoryError.*", "内存不足异常OutOfMemoryError");
		regexMap.put(".*IOException.*", "输入输出异常：IOException");
		regexMap.put(".*StackOverflowError.*", "堆栈溢出错误StackOverflowError！");
		regexMap.put(".*RuntimeException.*", "运行时异常RuntimeException");
		regexMap.put(".*EOFException.*", "文件已结束异常：java.lang.EOFException");
		regexMap.put(".*SQLException.*", "操作数据库异常：SQLException");
		regexMap.put(".*UnknownError.*", "未知错误：java.lang.UnknownError");

	}
/**
 * 抽象方法错误，当应用试图调用抽象方法时抛出：java.lang.AbstractMethodError
 * 用来指示一个断言失败的问题：java.lang.AssertionError
 * 类循环依赖错误：java.lang.ClassCircularityError
 * 类格式错误：java.lang.ClassFormatError
 * 所有错误的基类，用于标识严重的程序运行问题：java.lang.Error
 * 初始化程序错误：java.lang.ExceptionInInitializerError
 * 违法访问错误：java.lang.IllegalAccessError
 * 不兼容的类变化错误：java.lang.IncompatibleClassChangeError
 * 实例化错误：java.lang.InstantiationError
 * 内部错误：java.langInternalError
 * 连接错误：java.lang.LinkageError
 * 未找到类定义错误：java.lang.NoClassDefFoundError
 * 域不存在错误：java.lang.NoSuchFieldError
 * 方法不存在错误：java.lang.NoSuchMethodError
 * 内存不足错误：java.lang.OutOfMemoryError
 * 堆栈溢出错误：java.lang.StackOverflowError
 * 线程结束：java.lang.ThreadDeath
 * 未知错误：java.lang.UnknownError
 * 未满足的链接错误：java.langUnsatisfiedLinkError
 * 不支持的类版本错误：java.lang.UnsupportedClassVersionError
 * 验证错误：java.lang.VerifyError
 * 虚拟机错误：java.lang.VirtualMechineError
 * 算术条件异常：java.lang.ArithmeticException
 * 数组索引越界异常：java.lang.ArrayIndexOutOfBoundsException
 * 数组存储异常：java.lang.ArrayStoreException
 * 类造型异常：java.lang.ClassCastException
 * 找不到类异常：java.lang.NotFoundException
 * 不支持克隆异常：Java.lang.CloneNotSupportedException
 * 枚举敞亮不存在异常：java.lang.EnumConstantNotPresentException
 * 根异常：java.lang.Exception
 * 违法的访问异常：java.lang.IllegalAccessExcetion
 * 违法的监控状态异常：java.lang.IllegalMonitorStateException
 * 违法的状态异常：java.lang.IllegalStateException
 * 违法的线程状态异常：java.lang.IllegalThreadStateException
 * 索引越界异常：java.lang.IndexOutOfBoundsException
 * 实例化异常：java.lang.InstantiationException
 * 被中止异常：java.lang.InterruptedException
 * 数组大小为负值异常：java.lang.NegativeArraySizeException
 * 属性不存在异常：java.lang.NoSuchFieldException
 * 运行时异常：java.lang.RuntimeException
 * 安全异常：java.lang.SecurityException
 * 类型不存在异常：java.lang.TypeNotPresentException
 */

	/**
	 * 发送异常log之日至
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		File path = Environment.getExternalStorageDirectory();
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(path, fileName);
				//postReport(cr); //发送到服务器
				//cr.delete();
			}
		}
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = Environment.getExternalStorageDirectory();
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();	
			SharedPreferences sp = mContext.getSharedPreferences("config", 0);
			String fileName = Environment.getDataDirectory()
					+ "/" + "crash-" +sp.getString("websrvpath", "wsatest")+ timestamp + CRASH_REPORTER_EXTENSION;
//			FileOutputStream trace = mContext.openFileOutput(fileName,
//					Context.MODE_PRIVATE);
//			mDeviceCrashInfo.store(trace, "");
			try {
				FileOutputStream trace = new FileOutputStream(fileName);
				trace.write(result.getBytes());
				trace.flush();
				trace.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileName;
		} catch (Exception e) {
			LogUtils.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}

	/**
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			LogUtils.e(TAG, "Error while collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					LogUtils.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				LogUtils.e(TAG, "Error while collect crash info", e);
			}

		}
	}

}
