package com.lots.travel.store.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/12/15.
 */
@Entity
public class Area {
    @Id(autoincrement = true)
    private Long id;
    private int aid;
    private int pid;
    private String name;
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPid() {
        return this.pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public int getAid() {
        return this.aid;
    }
    public void setAid(int aid) {
        this.aid = aid;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1442768751)
    public Area(Long id, int aid, int pid, String name) {
        this.id = id;
        this.aid = aid;
        this.pid = pid;
        this.name = name;
    }
    @Generated(hash = 179626505)
    public Area() {
    }
    
}
