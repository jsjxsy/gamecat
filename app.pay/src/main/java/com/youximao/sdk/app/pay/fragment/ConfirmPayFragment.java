package com.youximao.sdk.app.pay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gamecat.sdk.proxy.HostInterface;
import com.gamecat.sdk.proxy.HostInterfaceManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youximao.sdk.app.pay.R;
import com.youximao.sdk.app.pay.adapter.CashCouponAdapter;
import com.youximao.sdk.app.pay.adapter.PayWayAdapter;
import com.youximao.sdk.app.pay.adapter.VoucherAdapter;
import com.youximao.sdk.app.pay.dialog.GameCatDialog;
import com.youximao.sdk.app.pay.model.CashCoupon;
import com.youximao.sdk.app.pay.model.ConfirmPayWay;
import com.youximao.sdk.app.pay.model.ConfirmPayWayResult;
import com.youximao.sdk.app.pay.model.Order;
import com.youximao.sdk.app.pay.model.PayWay;
import com.youximao.sdk.app.pay.payway.ToALiPay;
import com.youximao.sdk.app.pay.payway.ToLinKeaPay;
import com.youximao.sdk.app.pay.payway.ZwxH5Pay;
import com.youximao.sdk.app.pay.service.GameCatPayApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.base.TipsDialogFragment;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.model.MeowOrder;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.DialogUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by davy on 16/6/15.
 * 充值道具/喵点支付页面
 */
public class ConfirmPayFragment extends BaseFragment implements View.OnClickListener, CashCouponAdapter.PromptMessage {
    private static final String ORDER_AMOUNT = "orderAmount";
    private static final String ORDER_DESCRIPTION = "orderDescription";
    private static final String ORDER_PAY_WAY_LIST = "payWayList";
    private static final String ORDER_INFO = "order";
    private static final String MEOW_ORDER_INFO = "meowOrder";
    private static final String CASH_COUPON_NUM = "cashcouponnum";
    private static final String CASH_COUPON_LIST = "cashcouponlist";
    public static boolean mIsWeiXinPay = false;
    public static String mPrepayId;
    //关闭按钮
    ImageView mClose;
    //确定订单按钮
    Button mConfirmPayButton;
    //产品介绍
    TextView mDescriptionTextView;
    //产品价格
    TextView mAmountTextView;
    //喵点余额text
    int mBalanceAmount;
    //实际支付金额text
    String mPayAmount = "";
    //订单id
    String mOrderId = "";
    String mToPayInterfaceId = "3";
    private ArrayList<PayWay> mPayWayArrayList = new ArrayList<>();
    private GridView mPayWayGridView;
    private PayWayAdapter mPayWayAdapter;
    private int mPrePosition = -1;
    private int mSelectPosition = 0;
    private boolean isDialog = true;
    //    private PayWay.OnGridViewItemClickListener mOnGridViewItemClickListener = new PayWay.OnGridViewItemClickListener() {
//        @Override
//        public void onItemClickListener(View v, PayWay payWay) {
//            mToPayInterfaceId = payWay.payInterface;
//        }
//
//    };
    private double mAmount;
    private String mDescription;
    private String mAmounts;

    private RelativeLayout mRlCashCoupon;
    private TextView mTvCashCouponNum;
    private int mCashCouponNum;
    private DialogUtil mDialogUtil;
    private ListView mListView;
    private TextView mHadNoMore;
    private ImageView mCancel;
    private TextView mSelectVouchers;
    private ArrayList<String> mGroups = new ArrayList<>();
    private String cashCouponList;
    private Map<String, CashCoupon> map;//存放已经选中的代金券
    private double haveSelectMoney;//已经选中代金券的总金额

    public static ConfirmPayFragment getInstance(double amount, String description, Order order, String payWayList, MeowOrder meowOrder, int num, String cashCouponList) {
        ConfirmPayFragment fragment = new ConfirmPayFragment();
        Bundle args = new Bundle();
        args.putDouble(ORDER_AMOUNT, amount);
        args.putString(ORDER_DESCRIPTION, description);
        if (order != null) {
            args.putSerializable(ORDER_INFO, order);
        }
        if (meowOrder != null) {
            args.putSerializable(MEOW_ORDER_INFO, meowOrder);
        }
        args.putInt(CASH_COUPON_NUM, num);
        args.putString(ORDER_PAY_WAY_LIST, payWayList);
        args.putString(CASH_COUPON_LIST, cashCouponList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        mClose = (ImageView) view.findViewById(R.id.close);
        mClose.setOnClickListener(this);

        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mAmountTextView = (TextView) view.findViewById(R.id.tv_money);

        mPayWayGridView = (GridView) view.findViewById(R.id.id_grid_view_pay_way);
        mRlCashCoupon = (RelativeLayout) view.findViewById(R.id.rl_cash_coupon);
        mRlCashCoupon.setOnClickListener(this);
        mTvCashCouponNum = (TextView) view.findViewById(R.id.tv_cash_coupon_num);


        mPayWayAdapter = new PayWayAdapter(getActivity());
        mPayWayGridView.setAdapter(mPayWayAdapter);
        mPayWayAdapter.setArrayList(mPayWayArrayList);
        mPayWayGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayWay payWay = (PayWay) parent.getItemAtPosition(position);

//                if (payWay.getOnClickListener() != null) {
//                    payWay.getOnClickListener().onItemClickListener(view, payWay);
//                }
                mToPayInterfaceId = payWay.payInterface;

                if (position != mPrePosition) {
                    mPayWayArrayList.get(mPrePosition).selected = false;
                    mPayWayArrayList.get(position).selected = true;
                    mPayWayAdapter.notifyDataSetChanged();
                }
                mPrePosition = position;
            }
        });

        mConfirmPayButton = (Button) view.findViewById(R.id.confirm_pay_button);
        mConfirmPayButton.setOnClickListener(this);
        mConfirmPayButton.setEnabled(true);
        initData();
        if (mCashCouponNum == 0) {
            mRlCashCoupon.setVisibility(View.GONE);
        } else {
            mRlCashCoupon.setVisibility(View.VISIBLE);
            mTvCashCouponNum.setText(mCashCouponNum + "张可用");
        }
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            cashCouponList = args.getString(CASH_COUPON_LIST);
            mCashCouponNum = args.getInt(CASH_COUPON_NUM, 0);
            mAmount = args.getDouble(ORDER_AMOUNT);
            mAmounts = mAmount + "";
            mDescription = args.getString(ORDER_DESCRIPTION);
            Order order = null;
            MeowOrder meowOrder = null;
            if (args.containsKey(ORDER_INFO)) {
                order = (Order) args.getSerializable(ORDER_INFO);
                mPayAmount = order.getPayAmount();
                mBalanceAmount = order.getSurplusPoint();
            }
            if (args.containsKey(MEOW_ORDER_INFO)) {
                meowOrder = (MeowOrder) args.getSerializable(MEOW_ORDER_INFO);
            }
            orderViewBindData(order, meowOrder);
            String payWayList = args.getString(ORDER_PAY_WAY_LIST);
            List<ConfirmPayWay> payWays = JSON.parseArray(payWayList, ConfirmPayWay.class);
            payWayBindData(payWays);
        }
    }


    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_pay_confirm_pay;
    }

    private void orderViewBindData(Order order, MeowOrder meowOrder) {
        if (order != null) {
            mOrderId = order.getOrderId();
        }

        if (meowOrder != null) {
            mOrderId = meowOrder.getOrderId();
        }
        mDescriptionTextView.setText(mDescription);
        //去掉科学计数法
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.setGroupingUsed(false);
        mAmountTextView.setText(df.format(mAmount));
        mAmountTextView.getPaint().setFakeBoldText(true);
        mConfirmPayButton.setText("立即支付￥" + df.format(mAmount));
    }


    private void payWayBindData(List<ConfirmPayWay> payWays) {
        if (payWays != null && !payWays.isEmpty()) {
            int size = payWays.size();
            for (int i = 0; i < size; i++) {
                ConfirmPayWay confirmPayWay = payWays.get(i);
                String payWay = confirmPayWay.getPayWay();
                PayWay payWayObject = new PayWay();
                payWayObject.payInterface = confirmPayWay.getInterfaceId();
                if (i == 0) {
                    payWayObject.selected = true;
                    mPrePosition = i;
                } else {
                    payWayObject.selected = false;
                }

                if (!TextUtils.isEmpty(payWay)) {
                    if (payWay.equals("1")) {
                        //支付宝
                        payWayObject.title = "支付宝";
                        if (confirmPayWay.getStatus() == 2) {
                            payWayObject.iconResId = R.drawable.game_cat_alipay_disabled;
                        } else {
                            payWayObject.iconResId = R.drawable.game_cat_alipay_normal;
                        }
                        if (mAmounts.equals("0.0")) {
                            payWayObject.clickEnable = false;
                            payWayObject.iconResId = R.drawable.game_cat_alipay_disabled;
                        } else {
                            payWayObject.clickEnable = true;
                        }
                    } else if (payWay.equals("2") || payWay.equals("6") || payWay.equals("8")) {
                        //微信支付
                        payWayObject.title = "微信";
                        if (confirmPayWay.getStatus() == 2) {
                            payWayObject.iconResId = R.drawable.game_cat_we_chat_disabled;
                        } else {
                            payWayObject.iconResId = R.drawable.game_cat_we_chat_normal;
                        }
                        if (mAmounts.equals("0.0")) {
                            payWayObject.clickEnable = false;
                            payWayObject.iconResId = R.drawable.game_cat_we_chat_disabled;
                        } else {
                            payWayObject.clickEnable = true;
                        }
                    } else if (payWay.equals("5")) {
                        //喵点支付
                        payWayObject.title = "喵点";
                        if (confirmPayWay.getStatus() == 2) {
                            payWayObject.iconResId = R.drawable.game_cat_cat_point_disabled;
                        } else {
                            payWayObject.iconResId = R.drawable.game_cat_cat_point_normal;
                        }

                        if (Integer.parseInt(mPayAmount) > mBalanceAmount) {
                            payWayObject.clickEnable = false;
                            payWayObject.iconResId = R.drawable.game_cat_cat_point_disabled;
                        } else {
                            payWayObject.clickEnable = true;
                            payWayObject.iconResId = R.drawable.game_cat_cat_point_normal;
                        }
                    }

                    //mPayAmount == 0 && mBalanceAmount==0 可以支付
                    if (i == 0 && payWay.equals("5") && Integer.parseInt(mPayAmount) > mBalanceAmount) {
                        mSelectPosition++;
                    }

                    if (i == mSelectPosition) {
                        payWayObject.selected = true;
                        mPrePosition = mSelectPosition;
                        mToPayInterfaceId = payWayObject.payInterface;
                    } else {
                        payWayObject.selected = false;
                    }

                }

//                payWayObject.setOnClickListener(mOnGridViewItemClickListener);
                mPayWayArrayList.add(payWayObject);
            }

            mPayWayAdapter.setArrayList(mPayWayArrayList);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_pay_button) {
            mConfirmPayButton.setEnabled(false);
            toSDKPay();
        } else if (v.getId() == R.id.close) {
            Dialog();
        } else if (v.getId() == R.id.rl_cash_coupon) {
            //可用代金券
            openDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && isDialog) {
                    isDialog = false;
                    Dialog();
                }
                return false;
            }
        });
    }


    public void Dialog() {
        final GameCatDialog dialog = new GameCatDialog(getActivity());
        dialog.setCancer("取消支付", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isDialog = true;
                Collect.getInstance().custom(CustomId.id_322000);
                CallBackUtil.onFail();
                getActivity().finish();
            }
        });

        dialog.setContinue("继续支付", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDialog = true;
                dialog.dismiss();
            }
        });
    }

    public void toSDKPay() {
        Collect.getInstance().custom(CustomId.id_321000);
        showCircleWaitingFragment("支付中，请稍等");
        GameCatPayApi.sdkPay(mToPayInterfaceId, mOrderId, new GameCatSDKListener() {
            @Override
            public void onSuccess(final JSONObject message) {
                closeWaitingFragment();
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        goToPay(message);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络错误", false);
                    closeWaitingFragment();
                }
            }
        }, mContext);

    }

    public void goToPay(JSONObject message) {
        try {
            String data = message.getString("data");
            String content = AESUtil.decryptAES(data, Config.getAESKey());
            ConfirmPayWayResult confirmPayWayResult = JSON.parseObject(content, ConfirmPayWayResult.class);
            String interfaceId = confirmPayWayResult.getInterfaceId();
            String metaData = confirmPayWayResult.getMetaData();
            switch (interfaceId) {
                case "1":
                    //1代表亿联家支付
                    openLinKeAPay(metaData);
                    break;
                case "2":
                    //2代表原生支付宝
                    openALiPayAction(metaData);
                    break;
                case "3":
                    //3代表喵点支付
                    openCatPointPayAction(message);
                    break;
                case "4":
                    //4代表亿联家微信
                    openToLinKeaPayAction(metaData);
                    break;
                case "5":
                    //4代表亿联家微信
                    openZwxH5PayAction(message);
                    break;
                case "6":
                    //梓微兴支付
                    openZwxH5PayAction(message);
                    break;
                default:
                    ToastUtil.makeText(getActivity(), interfaceId, false);
                    break;
            }
        } catch (JSONException e) {
            ToastUtil.makeText(getActivity(), "数据解析有误", false);
            getActivity().finish();
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        }

    }

    private void openLinKeAPay(String metaData) {
        Collect.getInstance().custom(CustomId.id_325000);
        showCircleWaitingFragment("支付初始化中...");
        ToLinKeaPay toLinKeaPay = new ToLinKeaPay();
        toLinKeaPay.actionAliPay(SDKManager.getContext(), metaData, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    if (message.getString("message").equals("success")) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                } catch (JSONException e) {
                    closeWaitingFragment();
                    ToastUtil.makeText(getActivity(), "数据解析失败", false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                ToastUtil.makeText(getActivity(), message, false);
                closeWaitingFragment();
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }


    private void openALiPayAction(String metaData) {
        Collect.getInstance().custom(CustomId.id_325000);
        ToALiPay toALiPay = new ToALiPay();
        try {
            toALiPay.action(SDKManager.getContext(), new JSONObject(metaData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getActivity().finish();
    }

    private void openCatPointPayAction(JSONObject message) {
        Collect.getInstance().custom(CustomId.id_324000);
        try {
            String code = message.getString("code");
            if (code.equals("000")) {
                Collect.getInstance().custom(CustomId.id_331000);
                ToastUtil.makeTextSuccess(getActivity(), message.getString("message"));
                CallBackUtil.onSuccess();
                getActivity().finish();
            } else {
                ToastUtil.makeText(getActivity(), message.getString("message"), false);
                CallBackUtil.onFail();
                getActivity().finish();
            }
        } catch (JSONException e) {
            ToastUtil.makeText(getActivity(), "数据解析有误", false);
            getActivity().finish();
            e.printStackTrace();
        }
    }

    private void openToLinKeaPayAction(String metaData) {
        showCircleWaitingFragment("支付初始化中...");
        ToLinKeaPay toLinKeaPay = new ToLinKeaPay();
        toLinKeaPay.actionWeChatPay(SDKManager.getContext(), metaData, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    if (message.getString("message").equals("success")) {
                        getActivity().finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), message, false);
                    closeWaitingFragment();
                }
            }
        });
    }

    private void openZwxH5PayAction(JSONObject message) {
        Collect.getInstance().custom(CustomId.id_327000);

        try {
            String code = message.getString("code");
            if (code.equals("000")) {
                Collect.getInstance().custom(CustomId.id_334000);
                String data = message.getString("data");
                String content = AESUtil.decryptAES(data, Config.getAESKey());
                JSONObject jsonObject = new JSONObject(content);
                JSONObject meData = jsonObject.getJSONObject("metaData");
                String prepay_url = meData.getString("prepay_url");

                mPrepayId = meData.getString("prepay_id");
                prepay_url = prepay_url + "&type=android";
                //activity需要拿游戏的activity


                if (isWXAppInstalledAndSupported(mContext)) {
                    HostInterface mInterface = HostInterfaceManager.getInstance().getHostInterface();
                    if (null != mInterface && null != mInterface.getActivity()) {
                        ZwxH5Pay.toPay(mInterface.getActivity(), prepay_url);
                    } else {
                        ZwxH5Pay.toPay(mContext, prepay_url);
                    }
                    mIsWeiXinPay = true;
                }
                getActivity().finish();
            } else {
                Collect.getInstance().custom(CustomId.id_335000);
                ToastUtil.makeText(getActivity(), message.getString("message"), false);
                CallBackUtil.onFail();
                getActivity().finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        }

    }

    private boolean isWXAppInstalledAndSupported(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            Toast.makeText(context, "未安装微信", Toast.LENGTH_SHORT).show();
        }
        return sIsWXAppInstalledAndSupported;
    }

    private void openDialog() {
        Collect.getInstance().custom(CustomId.id_313000);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gamecat_pay_confirm_order_dialog, null);
        mDialogUtil = new DialogUtil(getActivity(), view);

        mDialogUtil.show();
        mListView = (ListView) view.findViewById(R.id.listview);
        mHadNoMore = (TextView) view.findViewById(R.id.had_no_more);
        mHadNoMore.setVisibility(View.INVISIBLE);
        mCancel = (ImageView) view.findViewById(R.id.img_cancel);
        mSelectVouchers = (TextView) view.findViewById(R.id.select_vouchers);
//        final VoucherAdapter voucherAdapter = new VoucherAdapter(getActivity());
//        mListView.setAdapter(voucherAdapter);
        CashCouponAdapter cashCouponAdapter = new CashCouponAdapter(mContext, new CashCouponAdapter.SelectCashCouponInter() {
            @Override
            public void selectCash(CashCoupon cashCoupon, double haveSelectMoney) {
                ConfirmPayFragment.this.haveSelectMoney = haveSelectMoney;
                if (cashCoupon == null) return;
                if (map == null) {
                    map = new HashMap<String, CashCoupon>();
                }
                if (cashCoupon.isSelect()) {
                    map.put(cashCoupon.getmId(), cashCoupon);
                } else {
                    map.remove(cashCoupon.getmId());
                }
            }
        }, mAmount * 100, this, haveSelectMoney);
        mListView.setAdapter(cashCouponAdapter);
        try {
            JSONObject message = new JSONObject(cashCouponList);
            String size = message.getString("totalSize");
            if (!size.equals("0")) {
                JSONArray list = message.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    mGroups.add(list.get(i).toString());
                }
            }

        } catch (JSONException e) {
            getActivity().finish();
            e.printStackTrace();
        }
//        voucherAdapter.setArrayList(mGroups);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setDivider(null);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().custom(CustomId.id_314000);
                mDialogUtil.cancel();
            }
        });
        mSelectVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtil.cancel();
                Collect.getInstance().custom(CustomId.id_315000);
                if (map == null || map.size() == 0) return;
                //// TODO: 17-3-20   提交选中代金券
                //判断已选代金券的总金额　大于或等于要支付的金额则支付宝和微信支付不可选状态
            }
        });

        // 监听listview滚到最底部
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            mHadNoMore.setVisibility(View.VISIBLE);
                        } else {
                            mHadNoMore.setVisibility(View.INVISIBLE);
                        }
                        break;
                    //开始滚动
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        mHadNoMore.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

    }

    @Override
    public void toPromptMessage(int type, String tips) {
        TipsDialogFragment tipsDialogFragment = TipsDialogFragment.getInstance(type, tips, 3000);
        addFragment(tipsDialogFragment);
        FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), tipsDialogFragment);
    }
}
