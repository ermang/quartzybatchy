package com.eg.quartzybatchy.entity;

import javax.persistence.Entity;

@Entity
public class ToTable extends BaseEntity {

    private String descr;

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
