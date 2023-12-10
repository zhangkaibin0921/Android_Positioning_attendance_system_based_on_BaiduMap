package com.example.pojo;

public class SignQueue {
    public int groupId;
    public String startTime;
    public String endTime;
    public String type;
    public String timetype;
    public String howLong;
    public String nums;
    public String signTexts;
    public String signTextsType;
    public String timeHour;

    @Override
    public String toString() {
        return "SignQueue{" +
                "groupId=" + groupId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type='" + type + '\'' +
                ", timetype='" + timetype + '\'' +
                ", howLong='" + howLong + '\'' +
                ", nums='" + nums + '\'' +
                ", signTexts='" + signTexts + '\'' +
                ", signTextsType='" + signTextsType + '\'' +
                ", timeHour='" + timeHour + '\'' +
                '}';
    }

    public String getTimetype() {
        return timetype;
    }

    public void setTimetype(String timetype) {
        this.timetype = timetype;
    }

    public String getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(String timeHour) {
        this.timeHour = timeHour;
    }




    public String getSignTextsType() {
        return signTextsType;
    }

    public void setSignTextsType(String signTextsType) {
        this.signTextsType = signTextsType;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHowLong() {
        return howLong;
    }

    public void setHowLong(String howLong) {
        this.howLong = howLong;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getSignTexts() {
        return signTexts;
    }

    public void setSignTexts(String signTexts) {
        this.signTexts = signTexts;
    }
}
