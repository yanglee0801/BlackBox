// IBPackageManagerService.aidl
package top.niunaijun.blackbox.server.pm;

// Declare any non-default types here with import statements
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.Intent;
import android.content.ComponentName;
import java.util.List;

interface IBPackageManagerService {
    ResolveInfo resolveService(in Intent intent, int flags, String resolvedType, int userId);
    ResolveInfo resolveActivity(in Intent intent, int flags, String resolvedType, int userId);
    ProviderInfo resolveContentProvider(String authority, int flag, int userId);
    ResolveInfo resolveIntent(in Intent intent, String resolvedType, int flags, int userId);

    ApplicationInfo getApplicationInfo(String packageName, int flags, int userId);
    PackageInfo getPackageInfo(String packageName, int flags, int userId);
    ServiceInfo getServiceInfo(in ComponentName component, int flags, int userId);
    ActivityInfo getReceiverInfo(in ComponentName componentName, int flags, int userId);
    ActivityInfo getActivityInfo(in ComponentName component, int flags, int userId);
    ProviderInfo getProviderInfo(in ComponentName component, int flags, int userId);

    List<ResolveInfo> queryIntentActivities(in Intent intent, int flags, String resolvedType, int userId);
    List<ResolveInfo> queryBroadcastReceivers(in Intent intent, int flags, String resolvedType, int userId);
    List<ProviderInfo> queryContentProviders(String processName, int uid, int flags, int userId);
    PackageInfo loadPackage(String file, int userId);
}
