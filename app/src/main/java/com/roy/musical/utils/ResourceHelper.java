package com.roy.musical.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;

/**
 * 一个可以复用的用来获取主题颜色的方法
 * Created by roy on 16-1-19.
 */
public class ResourceHelper {

    /**
     * 从一个主题中获取相关属性值
     * @param context 上下文
     * @param attr 主题属性
     * @param defColor 默认使用的颜色
     * @return 返回颜色值
     */
    public static int getThemeColor(Context context,int attr, int defColor) {
        int themeColor = 0;


        String packageName = context.getPackageName();

        try {
            Context packageContext = context.createPackageContext(packageName,0);
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName,0);

            packageContext.setTheme(appInfo.theme);

            Resources.Theme theme = packageContext.getTheme();
            TypedArray ta = theme.obtainStyledAttributes(new int[] {attr});

            themeColor = ta.getColor(0,defColor);
            ta.recycle();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return themeColor;
    }
}
