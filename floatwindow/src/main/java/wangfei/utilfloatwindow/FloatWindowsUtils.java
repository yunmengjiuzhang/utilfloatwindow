package wangfei.utilfloatwindow;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class FloatWindowsUtils {
    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        String mName;
        for (int i = 0; i < myList.size(); i++) {
            mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public void startService(Context context, String serviceName, String action) {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(context.getPackageName());
        if (isServiceWork(context, serviceName)) {
            context.stopService(intent);
        } else {
            if (SettingsCompat.canDrawOverlays(context)) {
                context.startService(intent);
            } else {
                SettingsCompat.manageDrawOverlays(context);
            }
        }
    }

    public void stopService(Context context, String serviceName, String action) {
        if (isServiceWork(context, serviceName)) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.setPackage(context.getPackageName());
            context.stopService(intent);
        }
    }
}
