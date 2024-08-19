package com.example.flowerdemo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

/**
 * @author Run
 * @date 2022/3/8
 * @description
 */
public class ToastDialogUtil {
    @SuppressLint({"WrongConstant", "ShowToast"})
    public static void showToast(final Activity activity, final String msg) {
        // 判断是在子线程，还是主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(activity, msg, 0).show();
        } else {
            // 子线程
            activity.runOnUiThread(new Runnable() {
                @SuppressLint("WrongConstant")
                @Override
                public void run() {
                    Toast.makeText(activity, msg, 0).show();
                }
            });
        }
    }

    @SuppressLint("WrongConstant")
    public static void showDialog(final Activity activity, final String title, String msg, String button) {
        // 判断是在子线程，还是主线程
        if ("main".equals(Thread.currentThread().getName())) {
            new AlertDialog.Builder(
                    activity)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(button, null)
                    .show();
        } else {
            // 子线程
            activity.runOnUiThread(() -> new AlertDialog.Builder(
                    activity)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(button, null)
                    .show());
        }
    }
}
