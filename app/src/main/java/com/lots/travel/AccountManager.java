package com.lots.travel;

import android.text.TextUtils;

import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.Account;
import com.lots.travel.store.db.AccountDao;
import com.lots.travel.store.db.GreenDaoManager;

import java.util.List;

/**
 * Created by nalanzi on 2017/10/14.
 */

public class AccountManager {
    private static final AccountManager mInstance = new AccountManager();

    public static AccountManager getInstance(){
        return mInstance;
    }

    private static final String USER_ID = "userId";

    private Account mAccount;

    private AccountManager(){
        mAccount = reload();
    }

    //为空表明当前没有已登录的用户
    public Account getAccount(){
        return mAccount;
    }

    public void login(Account account){
        save(account);
        mAccount = reload();
    }

    public void update(Account account){
        GreenDaoManager.getInstance()
                .getAccountDao()
                .update(account);
    }

    public void logout(){
        SpStore.set(USER_ID,-1L);
        mAccount = null;
    }

    private void save(Account account){
        //将userId存储到sp
        SpStore.set(USER_ID,account.getUserId());

        //将Account保存到数据库
        GreenDaoManager.getInstance()
                .getAccountDao()
                .insertOrReplace(account);
    }

    private Account reload(){
        //从sp中获取当前登录用户的userId
        long userId = SpStore.getLong(USER_ID,-1L);

        //根据userId获取数据库中存储的用户信息
        List<Account> accountList = GreenDaoManager.getInstance()
                .getAccountDao()
                .queryBuilder()
                .where(AccountDao.Properties.UserId.eq(userId))
                .list();

        //账号信息不存在 - 用户未登录
        if(accountList==null || accountList.size()==0)
            return null;
        else
            return accountList.get(0);
    }

    public long getUserId(){
        return mAccount==null ? -1:mAccount.getUserId();
    }

    public String getUsername(){
        return mAccount==null ? "":mAccount.getUsername();
    }

    public String getSecret(){
        return mAccount==null ? "":mAccount.getSecret();
    }

    public String getTags(){
        return mAccount==null ? "":mAccount.getTags();
    }

    public void setTags(String tags){
        if(mAccount!=null){
            mAccount.setTags(tags);
            //将Account保存到数据库
            GreenDaoManager.getInstance()
                    .getAccountDao()
                    .update(mAccount);
        }
    }

    public boolean isCurrentValidate() {
        if (mAccount == null)
            return false;

        Long userId = mAccount.getUserId();
        return !(userId == null || userId < 0)
                && !TextUtils.isEmpty(mAccount.getSecret());
    }

}
