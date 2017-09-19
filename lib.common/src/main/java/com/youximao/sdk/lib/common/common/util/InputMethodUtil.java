package com.youximao.sdk.lib.common.common.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;


public class InputMethodUtil {

    public static void showInputMethod(Context ctx) {
        onHideShowInputMethod(ctx);
    }

    public static void hideInputMethod(Context ctx) {
        onHideShowInputMethod(ctx);
    }

    private static void onHideShowInputMethod(Context ctx) {
        if (ctx == null) {
            return;
        }
        InputMethodManager sInputMethodMgr = (InputMethodManager)
                ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        sInputMethodMgr.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isInputMethodShowing(Context ctx) {
        if (ctx == null) {
            return false;
        }
        InputMethodManager sInputMethodMgr = (InputMethodManager)
                ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        return sInputMethodMgr.isActive();
    }

    /**
     * @param context 关闭输入法，需要一个activity
     */
    public static void closeInputMethod(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
            // TODO: handle exception
            Log.d("", "关闭输入法异常");
        }
    }
}
