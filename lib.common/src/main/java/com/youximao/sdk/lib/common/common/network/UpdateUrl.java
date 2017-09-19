package com.youximao.sdk.lib.common.common.network;

import com.youximao.sdk.lib.common.common.config.Config;

/**
 * Created by admin on 2017/2/27.
 */

public class UpdateUrl {

    public final static String UPDATE_DEV_URL = "http://gamecatsdk.dm.com";
    public final static String UPDATE_TEST_URL = "http://testgamecatsdk.youximao.cn";
    public final static String UPDATE_LIANTIAO_URL = "http://testgamecatsdk-lt.youximao.cn";
    public final static String UPDATE_PRE_URL = "http://sdk-pre.youximao.tv";
    public final static String UPDATE_RELEASE_URL = "http://sdk.youximao.tv";

    private final static String UPDATE_URL = "/sdk/hotswap/getUpdateInfo";

//    private final static String UPDATE_URL = "http://mock.youximao.cn/mockjsdata/49/sdk/hotswap/getUpdateInfo";
    public static String getUpdateUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            case 0:
                //联调环境
                url.append(UPDATE_LIANTIAO_URL);
                break;
            case 1:
                //正式环境
                url.append(UPDATE_RELEASE_URL);
                break;
            case 2:
                //测试环境
                url.append(UPDATE_TEST_URL);
                break;
            case 3:
                //预发布
                url.append(UPDATE_PRE_URL);
                break;
            case 4:
                url.append(UPDATE_DEV_URL);
                break;
        }
        url.append(UPDATE_URL);
        return url.toString();
    }
}
