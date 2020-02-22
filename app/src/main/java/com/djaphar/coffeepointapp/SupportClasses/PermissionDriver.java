package com.djaphar.coffeepointapp.SupportClasses;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.fragment.app.Fragment;

public class PermissionDriver {

    private static final int PERMISSION_REQUEST_CODE = 123;

    public static boolean hasPerms(String[] perms, Context context) {
        int res;

        for (String perm : perms) {
            res = context.checkCallingOrSelfPermission(perm);
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    public static void requestPerms(Fragment fragment, String[] perms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.requestPermissions(perms, PERMISSION_REQUEST_CODE);
        }
    }
}
