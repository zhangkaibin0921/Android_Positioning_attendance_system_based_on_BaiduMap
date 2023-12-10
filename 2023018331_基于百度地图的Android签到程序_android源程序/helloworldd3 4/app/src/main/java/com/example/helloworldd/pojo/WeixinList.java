package com.example.helloworldd.pojo;

public class WeixinList {
    private int id;
    private String title;
    private String content;
    private String time;
    private String number;

    String createusernumber;
    String groupid;


    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getCreateusernumber() {
        return createusernumber;
    }

    public void setCreateusernumber(String createusernumber) {
        this.createusernumber = createusernumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "WeixinList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", number='" + number + '\'' +
                ", createusernumber='" + createusernumber + '\'' +
                ", groupid='" + groupid + '\'' +
                '}';
    }
}
