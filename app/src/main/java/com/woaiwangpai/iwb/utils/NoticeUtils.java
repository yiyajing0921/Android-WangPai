package com.woaiwangpai.iwb.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:45
 * @Description:
 */
public class NoticeUtils {
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����JPush��TAG �� Alias
     *
     * @param TAG           : GUEST NORMAL VIP PRIME DIAGON
     * @param isAliasAction : true� false
     */
    public static void setTagAndAlias(Context c, String TAG, boolean isAliasAction) {
//		TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
//		if (isAliasAction)
//			tagAliasBean.action = 2;
//		else
//			tagAliasBean.action = 1;
//		int sequence = PreferenceHelper.readInt(c, Constant.FILE_NAME,
//				Constant.TAGSEQUENCE, 1);
//		if (TAG.equals("GUEST")) {
//			if (isAliasAction) {
//				tagAliasBean.alias = Constant.APPID;
//			} else {
//				Set<String> sets = new HashSet<String>();
//				sets.add(TAG);
//				sets.add(Constant.APPID);
//				tagAliasBean.tags = sets;
//			}
//			tagAliasBean.isAliasAction = isAliasAction;
//			TagAliasOperatorHelper.getInstance().handleAction(c, sequence,
//					tagAliasBean);
//		} else {
//			if (isAliasAction) {
//				tagAliasBean.alias = Constant.APPID;
//			} else {
//				Set<String> sets = new HashSet<String>();
//				sets.add(TAG);
//				sets.add(Constant.APPID);
//				sets.add(PreferenceHelper.readString(c, Constant.FILE_NAME,
//						Constant.PHONE));
//				tagAliasBean.tags = sets;
//			}
//			tagAliasBean.isAliasAction = isAliasAction;
//			TagAliasOperatorHelper.getInstance().handleAction(c, sequence,
//					tagAliasBean);
//		}
    }

    public static void setTagAndAlias2(Context c, String TAG) {

//		if (TAG.equals("GUEST")) {
//			Set<String> sets = new HashSet<String>();
//			sets.add(TAG);
//			sets.add(Constant.APPID);
//			JPushInterface.setTags(c, sets, new TagAliasCallback() {
//				@Override
//				public void gotResult(int i, String s, Set<String> set) {
//				}
//			});
//			// 设置别名Alia
//			JPushInterface.setAlias(c, Constant.APPID, new TagAliasCallback() {
//				@Override
//				public void gotResult(int i, String s, Set<String> set) {
//				}
//			});
//		} else {
//			Set<String> sets = new HashSet<String>();
//			sets.add(TAG);
//			sets.add(Constant.APPID);
//			sets.add(PreferenceHelper.readString(c, Constant.FILE_NAME,
//					Constant.PHONE));
//			JPushInterface.setTags(c, sets, new TagAliasCallback() {
//				@Override
//				public void gotResult(int i, String s, Set<String> set) {
//				}
//			});
//			// 设置别名Alia
//			JPushInterface.setAlias(c, Constant.APPID, new TagAliasCallback() {
//				@Override
//				public void gotResult(int i, String s, Set<String> set) {
//				}
//			});
//		}
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showUpdateNoticeDialog(final Activity activity) {
        final KyDialogBuilder builder = new KyDialogBuilder(activity);
        builder.setTitle("提示");
        builder.setMessage("未获得通知权限,可能会影响软件使用!");
        builder.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                builder.dismiss();
            }

        });
        builder.setPositiveButton("设置", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                builder.dismiss();
                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }

        });
        builder.show();
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

}
