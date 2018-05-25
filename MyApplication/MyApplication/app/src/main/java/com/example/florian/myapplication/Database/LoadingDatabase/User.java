package com.example.florian.myapplication.Database.LoadingDatabase;

/**
 * Représente une ligne de la table User
 */
public class User {

    private long _id;
    private String login;

    public User(long _id, String login){
        this._id = _id;
        this.login = login;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
