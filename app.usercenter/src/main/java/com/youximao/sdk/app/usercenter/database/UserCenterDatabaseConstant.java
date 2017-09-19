package com.youximao.sdk.app.usercenter.database;

import com.youximao.sdk.lib.database.annotation._DB_TABLE;

/**
 * Created by admin on 2017/3/13.
 */

public class UserCenterDatabaseConstant {
    public interface Tables {
        String USER_INFORMATION = "tb_user_info";
    }

    @_DB_TABLE(name = Tables.USER_INFORMATION, version = 1)
    public interface UserInformationColumns {
        String TOKEN = "_token";
        String USER_ID = "_user_id";
        String USER_NAME = "_user_name";
        String LOGIN_TYPE = "_login_type";
        String PHONE = "_phone";
        String ACCOUNT = "_account";
        String TIME = "_time";
        String TOKEN_TYPE = "_token_type";
        String GAME_ID = "_game_id";
        String OPEN_ID = "_open_id";
    }
}
