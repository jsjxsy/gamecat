package com.youximao.sdk.lib.common.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * SDK对话框，主容器
 */
public class DialogUtil extends Dialog {


    public DialogUtil(Context context, View view, boolean needBackground) {
        super(context, android.R.style.Theme_NoTitleBar);

        init(view, needBackground);
    }

    public DialogUtil(Context context, View view) {

        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        init(view, false);
        setOwnerActivity((Activity) context);
    }

    private void init(View view, boolean needBackground) {


        //Dialog无边框
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        //设置对话框Dialog透明
        if (needBackground) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f8fa")));
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(0));

        }

        setContentView(view);

    }


}
