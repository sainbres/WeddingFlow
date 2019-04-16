package com.sainbres.shu.weddingflow.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sainbres.shu.weddingflow.WeddingFlowDB;

@Table(database = WeddingFlowDB.class, name = "Users")
public class User extends BaseModel {

    public User() {
    }

    @PrimaryKey(autoincrement = true)
    int UserId;

    @Column
    String UserName;

    @Column
    String Password;

    @Column
    int ThemeSetting;

    @Column
    String FirstName;

    @Column
    String Surname;

    public int getUserId() {
        return UserId;
    }
    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }

    public int getThemeSetting() { return ThemeSetting; }
    public void setThemeSetting(int themeSetting) { ThemeSetting = themeSetting; }

    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSurname() {
        return Surname;
    }
    public void setSurname(String surname) {
        Surname = surname;
    }
}
