package com.youximao.sdk.app.pay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youximao.sdk.app.pay.R;
import com.youximao.sdk.app.pay.model.CashCoupon;
import com.youximao.sdk.lib.common.base.AdapterParentBase;
import com.youximao.sdk.lib.common.base.TipsDialogFragment;
import com.youximao.sdk.lib.common.common.util.StringUtils;

/**
 * Created by zhan on 17-3-20.
 * <p/>
 * 代金券列表
 */
public class CashCouponAdapter extends AdapterParentBase<CashCoupon> {
    //1.代金券金额超过应付金额
    //2.满减代金券和直减代金券不可合并使用,请重新选择

    private SelectCashCouponInter inter;
    private double needMoney;
    private double selectMoney;
    private Context mContext;
    private PromptMessage promptMessage;

    /**
     * @param context
     * @param inter
     * @param needMoney
     * @param promptMessage
     * @param havaSelectMoney 二次打开代金券选择界面传入上次已经选择的代金券的总金额
     */
    public CashCouponAdapter(Context context, SelectCashCouponInter inter, double needMoney, PromptMessage promptMessage, double havaSelectMoney) {
        super(context);
        this.mContext = context;
        this.inter = inter;
        this.needMoney = needMoney;
        this.promptMessage = promptMessage;
        this.selectMoney = havaSelectMoney;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.gamecat_cash_coupon_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mContent.setText(StringUtils.setSpecifiedTextsColor("", "不可合并", 0Xfff6b29));
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashCoupon cashCoupon = mArrayList.get(position);
                if (cashCoupon.isSelect()) {//已经处于勾选状态
                    cashCoupon.setSelect(false);
                    holder.mRbSelectCash.setChecked(false);
                } else {//未勾选状态
                    if (selectMoney <= needMoney) {//未超出
                        cashCoupon.setSelect(true);
                        holder.mRbSelectCash.setChecked(true);
                    } else {//已经超出提示用户
                        if (null != promptMessage) {
                            promptMessage.toPromptMessage(TipsDialogFragment.TIPS_WARNING, "代金券金额超过应付金额");
                        }
                    }
                }
                inter.selectCash(cashCoupon, selectMoney);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView mMoney;
        RadioButton mRbSelectCash;
        TextView mContent;
        TextView mEndTime;
        RelativeLayout mContainer;

        public ViewHolder(View view) {
            mMoney = (TextView) view.findViewById(R.id.tv_money);
            mEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            mRbSelectCash = (RadioButton) view.findViewById(R.id.rb_select_cash);
            mContent = (TextView) view.findViewById(R.id.tv_content);
            mContainer = (RelativeLayout) view.findViewById(R.id.rl_container);
        }
    }

    public interface SelectCashCouponInter {
        void selectCash(CashCoupon cashCoupon, double haveSelectMoney);
    }

    public interface PromptMessage {
        void toPromptMessage(int type, String tips);
    }
}
