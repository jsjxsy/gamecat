package com.youximao.sdk.app.personal.fragment;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.util.ImageUtils;
import com.youximao.sdk.lib.common.common.util.PhoneUtils;
import com.youximao.sdk.lib.common.common.widget.DialogUtil;

/**
 * Created by zhan on 17-3-13.
 * <p/>
 * 帮助
 */
public class HelpFragment extends BaseFragment implements View.OnLongClickListener {
    private TextView mTvServicePhone;
    private TextView mTvAccount;
    private ImageView mImgQrCode;
    private Bitmap mBitmap;

    public static HelpFragment getInstance() {
        HelpFragment helpFragment = new HelpFragment();
        return helpFragment;
    }

    @Override
    public void init(View view) {
        mTvServicePhone = (TextView) view.findViewById(R.id.tv_service_phone);
        mTvAccount = (TextView) view.findViewById(R.id.tv_account);
        mImgQrCode = (ImageView) view.findViewById(R.id.img_qr_code);
        mImgQrCode.setOnLongClickListener(this);
        mTvAccount.setOnLongClickListener(this);
        mTvServicePhone.setOnClickListener(this);
        String url1 = "http://disimg.youximao.tv/2017011711233818071285259011.jpg";
//        Glide.with(mContext).load(url1).into(mImgQrCode);
        Glide.with(mContext).load(url1).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (bitmap != null){
                    mImgQrCode.setImageBitmap(bitmap);
                    mBitmap = bitmap;
                }
            }
        });
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_help;
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_qr_code:
                if (mBitmap != null){
                    ImageUtils.saveImageToGallery(mContext,mBitmap);
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_account:
                PhoneUtils.copyTextToBoard(mContext,""+mTvAccount.getText().toString());
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_service_phone:
                if (!TextUtils.isEmpty(mTvServicePhone.getText().toString())){
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.gamecat_call_phone_tips, null);
                    TextView mPhoneNum = (TextView) view.findViewById(R.id.tv_phone_num);
                    TextView mCancel = (TextView) view.findViewById(R.id.cancel);
                    TextView mCallNum = (TextView) view.findViewById(R.id.call_num);
                    mPhoneNum.setText(mTvServicePhone.getText().toString());
                    final DialogUtil mDialog = new DialogUtil(mContext,view);
                    mDialog.setCancelable(true);
                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                        }
                    });

                    mCallNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneUtils.callPhone(mContext,mTvServicePhone.getText().toString());
                            mDialog.cancel();
                        }
                    });
                    mDialog.show();
                }
                break;
        }
    }
}
