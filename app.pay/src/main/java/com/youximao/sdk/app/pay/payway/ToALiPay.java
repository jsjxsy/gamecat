package com.youximao.sdk.app.pay.payway;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.youximao.sdk.app.pay.model.PayResult;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.common.util.SignUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by davy on 16/7/29.
 */
public class ToALiPay {
    public static final int BASE_ID = 0;
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALMyOXThDV71CyImUFqQ9eQceVmeZnBh21JEBKu71ZZjjRwFhXtLK9u09j9juWEiWoAGoGuXKf97/ipyjG2QXXC6nKYhirdvKNj7u2TiioI6g46z7Y4kIeqLFW9EqMCvqG2kxvG1kEWnHgUMUmXVHtHt9CASkUna0M0ruyoQsMu5AgMBAAECgYAyXNQ/IB006ePGJkSBH6qsAMCuSlF6FlagqVMyvOjLipEXJgoc28SozBRFPo7UWOZcgwBFHMgF8OA/J9CF3oQT+NPGAnI3SrkCjCqJslr3k143L+MVQe2Ylqq87nGMsTfCBjgX0RdY0odAcBjvfg9nmfvv3WfpQCsZEc6yDCqBLQJBAOnJhCSQm8VZD8mylgFFL1bJLzXZK/CHzPHNqThB5yooRITQ6ixoRpOxY+pP0PMOwztV9eIuIgjCmXKnsP9KIdMCQQDEON+a4HuGASLsRpNosL9rdy3Uv4QkW3Bx+GdeN3eYM7bn3ULvq7ObkyvEd3mrYv7gKAcsZsmyjmLpCzD6NdjDAkEAxxpOJe3aG9pbrcUgJ3jKdjjkshGAcFrXWd9zDGxj+O1OXWam6Kbt5H6gvz62jym1b2UoATaU2a0RjinufZye7QJAdZ82Aovv35sMyR9hO48/n3a4ZWBMINMjK15g8CxFp1nQmVqbxjlhl693u/R2XZMg/1NfQXF4FG2fwtxvhn0CqwJAaNsVqRV6d4mu7x+k33/hkhhTy7v24HyF/fXZRExEJ6sHwDgYwUM4weMdVTKWHMT/4/8rhtIv7odRumItorv/Ig==";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    String sign = "";
    String _input_charset = "";
    String total_fee = "";
    String subject = "";
    String notify_url = "";
    String service = "";
    String seller_id = "";
    String partner = "";
    String out_trade_no = "";
    String payment_type = "";
    String body = "";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        CallBackUtil.onSuccess();
                        Collect.getInstance().custom(CustomId.id_332000);


                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            JSONObject resultJson = new JSONObject();
                            try {
                                resultJson.put("status", resultStatus);
                                resultJson.put("message", "支付结果确认中");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            CallBackUtil.onFail();
                            Collect.getInstance().custom(CustomId.id_333000);
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    JSONObject resultJson = new JSONObject();
                    try {
                        resultJson.put("status", "-1");
                        resultJson.put("message", "检查结果为：" + msg.obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public void action(final Context context, JSONObject jsonString) {
        try {
            sign = jsonString.getString("sign");
            _input_charset = jsonString.getString("_input_charset");
            total_fee = jsonString.getString("total_fee");
            subject = jsonString.getString("subject");
            notify_url = jsonString.getString("notify_url");
            service = jsonString.getString("service");
            seller_id = jsonString.getString("seller_id");
            partner = jsonString.getString("partner");
            out_trade_no = jsonString.getString("out_trade_no");
            payment_type = jsonString.getString("payment_type");
            body = jsonString.getString("body");
            String orderInfo = getOrderInfo(subject, body, total_fee, partner, seller_id,
                    out_trade_no, notify_url, _input_charset, payment_type);
            // 对订单做RSA 签名
            String sign1 = sign;
            try {
                // 仅需对sign 做URL编码
                sign1 = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 完整的符合支付宝参数规范的订单信息
           /* final String payInfo = orderInfo + "&sign=\"" + sign1+ "\"&"
                    + getSignType();*/
            final String payInfo = orderInfo + "&sign=\"" + sign1 + "\"&"
                    + getSignType();

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象

                    PayTask alipay = new PayTask((Activity) context);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);


                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();


        } catch (JSONException e) {
            CallBackUtil.onFail();
            e.printStackTrace();
        }


    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price, String partner,
                               String seller, String outTradeNo, String notifyUrl, String _input_charset
            , String payment_type) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notifyUrl
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=" + "\"" + payment_type
                + "\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=" + "\"" + _input_charset
                + "\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";


        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
