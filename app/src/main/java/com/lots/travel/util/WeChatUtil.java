package com.lots.travel.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by nalanzi on 2017/11/4.
 */

public class WeChatUtil {

    /*      判断用户是否安装微信客户端     */
    public static boolean isWeChatAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取package manager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}
