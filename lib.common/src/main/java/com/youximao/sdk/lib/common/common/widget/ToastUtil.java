package com.youximao.sdk.lib.common.common.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youximao.sdk.lib.common.R;


/**
 * Created by davy on 16/3/18.
 */
public class ToastUtil {

    public static void makeText(Activity activity, String message, boolean isRight) {
        if (activity != null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View layout = inflater.inflate(R.layout.gamecat_toast, null);
            ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
            if (isRight) {
                image.setImageResource(R.drawable.gamecat_toast_right);
            } else {
                image.setImageResource(R.drawable.gamecat_toast_wrong);
            }
            TextView text = (TextView) layout.findViewById(R.id.toast_textview);
            text.setText(message);
            Toast toast = new Toast(activity.getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    public static void makeTextSuccess(Activity activity, String message) {
        if (activity != null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View layout = inflater.inflate(R.layout.gamecat_new_toast, null);
            TextView text = (TextView) layout.findViewById(R.id.tv_toast_message);
            text.setText(message);
            Toast toast = new Toast(activity.getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

}
