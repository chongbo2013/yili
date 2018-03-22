package com.lots.travel.store.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/9/16.
 */
@Entity
public class HttpInterface {
    @Id
    private String name;
    private String action;
    private String control;
    public String getControl() {
        return this.control;
    }
    public void setControl(String control) {
        this.control = control;
    }
    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Generated(hash = 187895804)
    public HttpInterface(String name, String action, String control) {
        this.name = name;
        this.action = action;
        this.control = control;
    }
    @Generated(hash = 209383314)
    public HttpInterface() {
    }
}
