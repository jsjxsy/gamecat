package com.youximao.sdk.lib.common.common.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.youximao.sdk.lib.common.R;

/**
 * Created by zhan on 17-3-16.
 */
public class PhoneUtils {

    /**
     * 复制到剪切板
     *
     * @param context
     * @param string
     */
    public static void copyTextToBoard(Context context,String string) {
        if (TextUtils.isEmpty(string))
            return;
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
        Toast.makeText(context, R.string.gamecat_copy_success,Toast.LENGTH_SHORT).show();
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param number
     */
    public static void callPhone(Context context,String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
