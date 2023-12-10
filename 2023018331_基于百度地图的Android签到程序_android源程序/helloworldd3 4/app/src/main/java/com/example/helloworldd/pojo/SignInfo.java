package com.example.helloworldd.pojo;

public class SignInfo {

    String signid;
    String groupid;
    String signtext;
    String signstarttime;
    String signmins;
    String signnums;
    String jingdu;
    String weidu;

    @Override
    public String toString() {
        return "SignInfo{" +
                "signid='" + signid + '\'' +
                ", groupid='" + groupid + '\'' +
                ", signtext='" + signtext + '\'' +
                ", signstarttime='" + signstarttime + '\'' +
                ", signmins='" + signmins + '\'' +
                ", signnums='" + signnums + '\'' +
                ", jingdu='" + jingdu + '\'' +
                ", weidu='" + weidu + '\'' +
                '}';
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

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getSigntext() {
        return signtext;
    }

    public void setSigntext(String signtext) {
        this.signtext = signtext;
    }

    public String getSignstarttime() {
        return signstarttime;
    }

    public void setSignstarttime(String signstarttime) {
        this.signstarttime = signstarttime;
    }

    public String getSignmins() {
        return signmins;
    }

    public void setSignmins(String signmins) {
        this.signmins = signmins;
    }

    public String getSignnums() {
        return signnums;
    }

    public void setSignnums(String signnums) {
        this.signnums = signnums;
    }
}
