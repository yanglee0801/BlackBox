package top.niunaijun.blackbox.client.hook.proxies.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IInterface;
import android.os.Process;

import java.lang.reflect.Method;
import java.util.List;

import mirror.android.app.ActivityThread;
import mirror.android.app.ContextImpl;
import mirror.android.content.pm.ParceledListSlice;
import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.client.BClient;
import top.niunaijun.blackbox.client.hook.BinderInvocationStub;
import top.niunaijun.blackbox.client.hook.MethodHook;
import top.niunaijun.blackbox.utils.MethodParameterUtils;
import top.niunaijun.blackbox.utils.Reflector;
import top.niunaijun.blackbox.utils.compat.BuildCompat;

/**
 * Created by Milk on 3/30/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class PackageManagerStub extends BinderInvocationStub {
    public static final String TAG = "PackageManager";

    public PackageManagerStub() {
        super(ActivityThread.sPackageManager.get().asBinder());
    }

    @Override
    protected Object getWho() {
        return ActivityThread.sPackageManager.get();
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        ActivityThread.sPackageManager.set((IInterface) proxyInvocation);
        replaceSystemService("package");
        Object systemContext = ActivityThread.getSystemContext.call(BlackBoxCore.mainThread());
        PackageManager packageManager = ContextImpl.mPackageManager.get(systemContext);
        if (packageManager != null) {
            try {
                Reflector.on("android.app.ApplicationPackageManager")
                        .field("mPM")
                        .set(packageManager, proxyInvocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new ResolveIntent());
        addMethodHook(new SetComponentEnabledSetting());
        addMethodHook(new GetPackageInfo());
        addMethodHook(new GetProviderInfo());
        addMethodHook(new GetServiceInfo());
        addMethodHook(new GetActivityInfo());
        addMethodHook(new GetReceiverInfo());
        addMethodHook(new GetInstalledApplications());
        addMethodHook(new GetInstalledPackages());
        addMethodHook(new GetApplicationInfo());
        addMethodHook(new QueryContentProviders());
        addMethodHook(new ResolveContentProvider());
        addMethodHook(new CanRequestPackageInstalls());
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    static class ResolveIntent extends MethodHook {

        @Override
        protected String getMethodName() {
            return "resolveIntent";
        }

        @Override
        protected ResolveInfo hook(Object who, Method method, Object[] args) throws Throwable {
            Intent intent = (Intent) args[0];
            String resolvedType = (String) args[1];
            int flags = (int) args[2];
            return BlackBoxCore.getVPackageManager().resolveIntent(intent, resolvedType, flags, BClient.getUserId());
        }
    }

    static class SetComponentEnabledSetting extends MethodHook {

        @Override
        protected String getMethodName() {
            return "setComponentEnabledSetting";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            // todo
            return 0;
        }
    }

    static class GetPackageInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getPackageInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            String packageName = (String) args[0];
            int flag = (int) args[1];
            PackageInfo packageInfo = BlackBoxCore.getVPackageManager().getPackageInfo(packageName, flag, BClient.getUserId());
            if (packageInfo != null) {
                return packageInfo;
            }
            return method.invoke(who, args);
        }
    }

    static class GetProviderInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getProviderInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ComponentName componentName = (ComponentName) args[0];
            int flags = (int) args[1];
            ProviderInfo providerInfo = BlackBoxCore.getVPackageManager().getProviderInfo(componentName, flags, BClient.getUserId());
            if (providerInfo != null)
                return providerInfo;
            return method.invoke(who, args);
        }
    }

    static class GetReceiverInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getReceiverInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ComponentName componentName = (ComponentName) args[0];
            int flags = (int) args[1];
            ActivityInfo receiverInfo = BlackBoxCore.getVPackageManager().getReceiverInfo(componentName, flags, BClient.getUserId());
            if (receiverInfo != null)
                return receiverInfo;
            return method.invoke(who, args);
        }
    }
    static class GetActivityInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getActivityInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ComponentName componentName = (ComponentName) args[0];
            int flags = (int) args[1];
            ActivityInfo activityInfo = BlackBoxCore.getVPackageManager().getActivityInfo(componentName, flags, BClient.getUserId());
            if (activityInfo != null)
                return activityInfo;
            return method.invoke(who, args);
        }
    }


    static class GetServiceInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getServiceInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            ComponentName componentName = (ComponentName) args[0];
            int flags = (int) args[1];
            ServiceInfo serviceInfo = BlackBoxCore.getVPackageManager().getServiceInfo(componentName, flags, BClient.getUserId());
            if (serviceInfo != null)
                return serviceInfo;
            return method.invoke(who, args);
        }
    }

    static class GetInstalledApplications extends MethodHook {

        @Override
        protected String getMethodName() {
            return "GetInstalledApplications";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return method.invoke(who, args);
        }
    }

    static class GetInstalledPackages extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getInstalledPackages";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return method.invoke(who, args);
        }
    }

    static class GetApplicationInfo extends MethodHook {

        @Override
        protected String getMethodName() {
            return "getApplicationInfo";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            String packageName = (String) args[0];
            int flags = (int) args[1];
            ApplicationInfo applicationInfo = BlackBoxCore.getVPackageManager().getApplicationInfo(packageName, flags, BClient.getUserId());
            if (applicationInfo != null) {
                return applicationInfo;
            }
            return method.invoke(who, args);
        }
    }

    static class QueryContentProviders extends MethodHook {

        @Override
        protected String getMethodName() {
            return "queryContentProviders";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            List<ProviderInfo> providers = BlackBoxCore.getVPackageManager().
                    queryContentProviders(BClient.getVProcessName(), Process.myUid(), 0, BClient.getUserId());

            if (BuildCompat.isQ()) {
                return ParceledListSlice.ctorQ.newInstance(providers);
            } else {
                Object list = ParceledListSlice.ctor.newInstance();
                for (ProviderInfo provider : providers) {
                    ParceledListSlice.append.call(list, provider);
                }
                return list;
            }
        }
    }

    static class ResolveContentProvider extends MethodHook {

        @Override
        protected String getMethodName() {
            return "resolveContentProvider";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            String authority = (String) args[0];
            int flags = (int) args[1];
            ProviderInfo providerInfo = BlackBoxCore.getVPackageManager().resolveContentProvider(authority, flags, BClient.getUserId());
            if (providerInfo == null) {
                return method.invoke(who, args);
            }
            return providerInfo;
        }
    }

    static class CanRequestPackageInstalls extends MethodHook {

        @Override
        protected String getMethodName() {
            return "canRequestPackageInstalls";
        }

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }
}
