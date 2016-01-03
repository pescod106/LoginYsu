package com.pescod.loginysu.model;

/**
 * Created by Administrator on 1/1/2016.
 */
public class AccountInfo {
    private String strAccount;
    private String strPassword;
    private boolean isAvailable = false;
    private boolean isTest = false;

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    public boolean isTest() {
        return isTest;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public String getStrAccount() {
        return strAccount;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setStrAccount(String strAccount) {
        this.strAccount = strAccount;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }
}
