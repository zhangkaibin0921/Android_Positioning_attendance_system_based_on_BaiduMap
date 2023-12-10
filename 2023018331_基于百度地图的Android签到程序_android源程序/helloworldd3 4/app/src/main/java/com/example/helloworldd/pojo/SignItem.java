package com.example.helloworldd.pojo;

public class SignItem {
    String SignCount;
    String SignTime;

    public SignItem() {

    }

    @Override
    public String toString() {
        return "SignItem{" +
                "SignCount='" + SignCount + '\'' +
                ", SignTime='" + SignTime + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    String Type;

    public SignItem(String signCount) {
        SignCount = signCount;
    }

    public String getSignCount() {
        return SignCount;
    }

    public void setSignCount(String signCount) {
        SignCount = signCount;
    }

    public String getSignTime() {
        return SignTime;
    }

    public void setSignTime(String signTime) {
        SignTime = signTime;
    }
}
