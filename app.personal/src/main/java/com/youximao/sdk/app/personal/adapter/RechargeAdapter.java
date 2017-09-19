package com.youximao.sdk.app.personal.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.model.GoodsItem;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.AdapterParentBase;

/**
 * Created by yulinsheng on 16-11-7.
 * 充值item
 */
public class RechargeAdapter extends AdapterParentBase<GoodsItem> implements SectionIndexer {

    public int cunt;
    public int potion;
    public EditText money;
    private Button mButtonConfirmPay;


    public RechargeAdapter(Context mContext, Button mButtonConfirmPay) {
        super(mContext);
        this.mButtonConfirmPay = mButtonConfirmPay;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        final RechargeAdapter.ItemViewHolder holder;
        if (view == null) {
            int layoutId = R.layout.gamecat_recharge_item;
            view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            holder = new RechargeAdapter.ItemViewHolder();
            holder.mTextViewMeow = (TextView) view.findViewById(R.id.tv_meow);
            holder.mTextViewMoney = (TextView) view.findViewById(R.id.tv_money);
            holder.mLinearLayoutGv = (RelativeLayout) view.findViewById(R.id.ll_gv);
            holder.mEditTextMoney = (EditText) view.findViewById(R.id.et_money);
            holder.mLinearLayoutMoney = (LinearLayout) view.findViewById(R.id.ll_money);
            holder.mImageConfirm = (ImageView) view.findViewById(R.id.iv_confirm);
            view.setTag(holder);
        } else {
            holder = (RechargeAdapter.ItemViewHolder) view.getTag();
        }

        if (getCount() - position == 1) {
            holder.mLinearLayoutMoney.setVisibility(View.VISIBLE);
            holder.mLinearLayoutGv.setVisibility(View.GONE);
            holder.mEditTextMoney.setText("");
            holder.mEditTextMoney.setHint("自定义金额");
            holder.mEditTextMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (getPotion() == 6) {
                        if (TextUtils.isEmpty(holder.mEditTextMoney.getText().toString())) {
                            mButtonConfirmPay.setEnabled(false);
                            mButtonConfirmPay.setText("立即支付");
                        } else {
                            if (Double.parseDouble(holder.mEditTextMoney.getText().toString()) * 10 >= 1) {
                                mButtonConfirmPay.setEnabled(true);
                                mButtonConfirmPay.setText("立即支付￥" + holder.mEditTextMoney.getText().toString());
                            } else {
                                mButtonConfirmPay.setEnabled(false);
                                mButtonConfirmPay.setText("立即支付");
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.mEditTextMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Collect.getInstance().custom(CustomId.id_216000);
                        holder.mEditTextMoney.setHint("");
                        setCunt(-1);
                        setMoney(holder.mEditTextMoney);
                        setPotion(getCount());
                        notifyDataSetChanged();
                    }
                }
            });

        } else {
            holder.mLinearLayoutMoney.setVisibility(View.GONE);
            holder.mLinearLayoutGv.setVisibility(View.VISIBLE);
            if (position == getCunt()) {
                holder.mImageConfirm.setVisibility(View.VISIBLE);
                holder.mLinearLayoutGv.setBackgroundResource(R.drawable.gamecat_recharge_black);
            } else {
                holder.mImageConfirm.setVisibility(View.GONE);
                holder.mLinearLayoutGv.setBackgroundResource(R.drawable.gamecat_recharge_white);
            }
            holder.mTextViewMoney.getPaint().setFakeBoldText(true);
            holder.mTextViewMoney.setText((int) getItem(position).getPrice() + "元");
            holder.mTextViewMeow.setText(getItem(position).getPoint() + "喵点");
            holder.mLinearLayoutGv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custom(position);
                    ((InputMethodManager) mContext.getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            holder.mLinearLayoutGv.getWindowToken(), 0);
                    holder.mEditTextMoney.setFocusable(true);
                    holder.mEditTextMoney.setFocusableInTouchMode(true);
                    holder.mEditTextMoney.requestFocus();
                    holder.mEditTextMoney.requestFocusFromTouch();
                    setCunt(position);
                    setPotion(position);
                    notifyDataSetChanged();
                    mButtonConfirmPay.setEnabled(true);
                    mButtonConfirmPay.setText("立即支付￥" + getItem(position).getPrice());
                }
            });
        }
        return view;
    }

    public Object[] getSections() {
        return null;
    }

    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public int getPositionForSection(int arg0) {
        return 0;
    }

    public int getCunt() {
        return cunt;
    }

    public void setCunt(int cunt) {
        this.cunt = cunt;
    }

    public int getPotion() {
        return potion;
    }

    public void setPotion(int potion) {
        this.potion = potion;
    }

    public EditText getMoney() {
        return money;
    }

    public void setMoney(EditText money) {
        this.money = money;
    }

    public void custom(int potion) {
        if (potion == 0) {
            Collect.getInstance().custom(CustomId.id_211000);
        } else if (potion == 1) {
            Collect.getInstance().custom(CustomId.id_212000);
        } else if (potion == 2) {
            Collect.getInstance().custom(CustomId.id_213000);
        } else if (potion == 3) {
            Collect.getInstance().custom(CustomId.id_214000);
        } else if (potion == 4) {
            Collect.getInstance().custom(CustomId.id_215000);
        }
    }

    public static class ItemViewHolder {
        private TextView mTextViewMoney;
        private TextView mTextViewMeow;
        private RelativeLayout mLinearLayoutGv;
        private EditText mEditTextMoney;
        private LinearLayout mLinearLayoutMoney;
        private ImageView mImageConfirm;
    }
}