package com.example.service;

import com.example.pojo.*;

import java.util.List;

public interface UserService {
    //注册用户
    int reigisterUser(String name, String phone, String password,String sex,String qianming);

    //用户登录
    User login(String number,String password);

    // 消息列表
    List<WeixinList> informationUser(String number);

    // 通讯录
    ContactList contact(String number);

    int forgetPassword(String phone,String password);

    public int addFriend(String Host, String Guest);
    public List<User> selectPhoneNumber(String phone);

    //修改个人信息
    int ModifyInfo(User user);
    //修改密码
    int ModifyPassword(User user);


    boolean createGroup(String groupname, List<User> chooses, User createuser);

    public List<SignInfo> getSignInfo(String grouid);

    public int addASign(String grouid, String signtext, String signstarttime, String signmins,String jingdu,String weidu);

    public SignInfo getASign(String grouid);

    List<SignRecord> getDetailSign(String signid);

    public int insertsign(String name, String number, String signinfoid, String signsuccess);
    int getNum(String signid);
    int addsigntime(String signid,int time);

    int signtitle(String signid, String title);

    int signteacher(String signid, String phone);

    int signend(String signid);

    int signdelete(String signid);
}
