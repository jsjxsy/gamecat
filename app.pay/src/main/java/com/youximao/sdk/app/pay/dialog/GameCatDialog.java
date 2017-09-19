package com.youximao.sdk.app.pay.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.youximao.sdk.app.pay.R;


/**
 * Created by yulinsheng on 16-10-22.
 * Dialog对话框
 */
public class GameCatDialog {
    AlertDialog dialog;
    TextView title_name;
    TextView tv_cancel;
    TextView tv_continue;

    public GameCatDialog(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.gamecat_dialog, null);
        title_name = (TextView) layout.findViewById(R.id.title_name);
        tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        tv_continue = (TextView) layout.findViewById(R.id.tv_continue);

        dialog = new AlertDialog.Builder(activity).create();
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setContentView(layout);
    }

    public void setTitle(int resId) {
        title_name.setText(resId);
    }

    public void setTitle(String title) {
        title_name.setText(title);
    }

    public void setCancer(String text, View.OnClickListener listener) {
        tv_cancel.setOnClickListener(listener);
        tv_cancel.setText(text);
    }

    public void setContinue(String text, View.OnClickListener listener) {
        tv_continue.setOnClickListener(listener);
        tv_continue.setText(text);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}