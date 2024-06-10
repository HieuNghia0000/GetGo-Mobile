package com.application.getgoproject.models;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String userName;
    private String password;

    private boolean gender;
    private String email;
    private String phoneNumber;

    private String avatar;
    private String role;
    private boolean isActive;
    private List<String> friends;
    private List<String> favorites;

    public User() {
        this.id = "";
        this.userName = "";
        this.password = "";
        this.gender = false;
        this.email = "";
        this.phoneNumber = "";
        this.avatar = "";
        this.role = "";
        this.isActive = false;
        this.friends = new ArrayList<>();
        this.favorites = new ArrayList<>();
    }

    public User(String id, String userName, String password, boolean gender, String email, String phoneNumber, String avatar, String role, boolean isActive, List<String> friends, List<String> favorites) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.role = role;
        this.isActive = isActive;
        this.friends = friends;
        this.favorites = favorites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
}
