package com.zhuoyi.fauction;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.yintai.CookieManager;
import com.yintai.app_common.database.table.UserTable;
import com.yintai.app_common.model.User;
import com.yintai.common.interfase.StaticClassReleace;


/**
 * Created by Chendj on 2015/8/11.
 */
public class UserManager implements StaticClassReleace {

    private static final long LONGIN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;         //一周的时间如果用户没有重新登入强迫重新登入

    private String PHPSESSIONID;

    public String getPHPSESSIONID() {
        return PHPSESSIONID;
    }

    public void setPHPSESSIONID(String PHPSESSIONID) {
        this.PHPSESSIONID = PHPSESSIONID;
    }

    @Override
    public void release() {
        if (user != null) {
            try {
                db.saveOrUpdate(user);
                user = null;
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        if (db != null) {
            db.close();
            db = null;
        }
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public interface OnLoginListener {
        void loginSuccess();

        void loginFail();
    }

    private OnLoginListener listener;
    private User user;
    private DbUtils db;
    private static UserManager ourInstance;

    public static UserManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserManager();
        }
        return ourInstance;
    }

    public boolean isLogin() {
        if (user != null && !TextUtils.isEmpty(user.getId())) {
            return this.user.islogin();
        }
        return false;
    }

    private UserManager() {
    }

    public User getUser() {
        if (user == null) {
            try {
                user = db.findFirst(Selector.from(User.class)
                        .where(WhereBuilder
                                .b()
                                .expr(UserTable.ISLOGIN, "=", true))
                        .orderBy(UserTable.LONGINTIME, true));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    //操作用户的信息的方法

    /**
     * 登入完成后，返回的用户信息
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        setUserLogin();
    }

    /**
     * 用户的一些登入信息
     *
     * @param user_auth
     * @param sign
     * @param token
     */
    public void setUserCookie(String user_auth, String sign, String token) {
        CookieManager.getInstance().setAuth(this.user.getId(), user_auth, sign, token);
    }

    /**
     * 返回的数组中第一项是user_auth,第二项是sign
     *
     * @return
     * @deprecated
     */
    public String[] getUserCookie() {
        String user_auth = CookieManager.getInstance().getUserAuth(this.user.getId());
        String auth_sign = CookieManager.getInstance().getUserAuthSign(this.user.getId());
        if (user_auth == null || auth_sign == null)
            return null;
        return new String[]{user_auth, auth_sign};
    }

    /**
     * 获取用户的user_auth信息
     * @return
     */
    public String getUserAuth() {
        String user_auth = CookieManager.getInstance().getUserAuth(this.user.getId());
        if (TextUtils.isEmpty(user_auth)) {
            return null;
        }
        return user_auth;
    }

    /**
     * 获取用户的user_auth_sign信息
     * @return
     */
    public String getUserAuthSign() {
        String auth_sign = CookieManager.getInstance().getUserAuthSign(this.user.getId());
        if (TextUtils.isEmpty(auth_sign)) {
            return null;
        }
        return auth_sign;
    }

    /**
     * 获取用户的user_auth_token信息
     * @return
     */
    public String getUserAuthToken() {
        String auth_token = CookieManager.getInstance().getUserAuthToken(this.user.getId());
        if (TextUtils.isEmpty(auth_token)) {
            return null;
        }
        return auth_token;
    }

    /**
     * 设置用户登入
     */
    private void setUserLogin() {
        this.user.setIslogin(true);
        this.user.setLoginTime(System.currentTimeMillis());
//        this.user.setLogoutTime(System.currentTimeMillis() + LONGIN_TIMEOUT);
        updateUser();
    }

    private void updateUser() {
        try {
            db.saveOrUpdate(this.user);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void setUserName(String name) {
        if (TextUtils.isEmpty(name))
            return;
        this.user.setName(name);
        updateUser();
    }

    public void setUserHeadImg(String url) {
        if (TextUtils.isEmpty(url))
            return;
        this.user.setHeadimg(url);
        updateUser();
    }

    /**
     * 设置用户登出
     */
    public void setUserLogout() {
        this.user.setIslogin(false);
        this.user.setLogoutTime(System.currentTimeMillis());
        updateUser();
        this.user = null;
    }

    public void init(DbUtils db, OnLoginListener listener) {
        //用户信息变动
        this.listener = listener;
        this.db = db;
        //初始化一下用户信息
        //1，从数据库初始化
        //3，获取之前登入的用户
        if (user == null) {
            try {
                if (db.tableIsExist(User.class)) {
                    user = getUser();
                }
            } catch (DbException e) {
                listener.loginFail();
                e.printStackTrace();
            }
        }

        if (user == null) {
            if (listener != null) {
                listener.loginFail();
            }
        } else {
            if (listener != null) {
                listener.loginSuccess();
            }
        }
        CookieManager.getInstance().init(db);
    }
}
