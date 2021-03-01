package com.rookiefly.test.commons.poi;

public class User {

    private int id;
    private String username;
    private String nickname;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(int id, String username, String nickname, int age) {
        super();
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.age = age;
    }

    public User() {
        super();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", nickname="
                + nickname + ", age=" + age + "]";
    }


}
