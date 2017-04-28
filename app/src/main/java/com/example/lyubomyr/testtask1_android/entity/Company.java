package com.example.lyubomyr.testtask1_android.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Company extends RealmObject{
    @PrimaryKey
    private Long id;

    private String name;

    private Company parent;

    private RealmList<Company> children;

    public Company() {}

    public Company(Long id, String name, Company parent, RealmList<Company> children) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.children = children;
    }

    public Company(Long id, String name, Company parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getParent() {
        return parent;
    }

    public void setParent(Company parent) {
        this.parent = parent;
    }

    public RealmList<Company> getChildren() {
        return children;
    }

    public void setChildren(RealmList<Company> children) {
        this.children = children;
    }
}
