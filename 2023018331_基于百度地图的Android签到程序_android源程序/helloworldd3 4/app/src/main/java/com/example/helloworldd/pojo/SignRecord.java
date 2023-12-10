package com.example.helloworldd.pojo;

public class SignRecord {

    String id;
    String signid;
    String username;
    String usernumber;
    String signtime;
    String jingdu;
    String weidu;
    String signsuccess;
    String text;
    @Override
    public String toString() {
        return "SignRecord{" +
                "id='" + id + '\'' +
                ", signid='" + signid + '\'' +
                ", username='" + username + '\'' +
                ", usernumber='" + usernumber + '\'' +
                ", signtime='" + signtime + '\'' +
                ", jingdu='" + jingdu + '\'' +
                ", weidu='" + weidu + '\'' +
                ", signsuccess='" + signsuccess + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getSigntime() {
        return signtime;
    }

    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public String getSignsuccess() {
        return signsuccess;
    }

    public void setSignsuccess(String signsuccess) {
        this.signsuccess = signsuccess;
    }
}
