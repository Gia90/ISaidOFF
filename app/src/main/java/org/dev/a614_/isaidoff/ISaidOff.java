package org.dev.a614_.isaidoff;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class ISaidOff implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui")) return;

        final Class<?> cellularTileClass = findClass("com.android.systemui.qs.tiles.CellularTile", lpparam.classLoader);

        // https://github.com/aosp-mirror/platform_frameworks_base/blob/d18ed49f9dba09b85782c83999a9103dec015bf2/packages/SystemUI/src/com/android/systemui/qs/tiles/CellularTile.java
        // Hooking method: protected void handleClick()
        findAndHookMethod(cellularTileClass, "handleClick", new XC_MethodReplacement() {

            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {

                Object mDataController = getObjectField(param.thisObject, "mDataController");

                if ( (Boolean)callMethod(mDataController, "isMobileDataEnabled") ) {
                    callMethod(mDataController, "setMobileDataEnabled", false);
                } else {
                    callMethod(mDataController, "setMobileDataEnabled", true);
                }
                return null;
            }
        });
    }
}