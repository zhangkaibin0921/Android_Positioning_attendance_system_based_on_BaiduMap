package com.example.dao;

import com.example.pojo.*;

import java.util.List;

public interface UserDao {
    //添加用户
    int insertUser(String name, String phone, String password,String sex,String qianming);

    int updateUser(String phone,String password);

    //查询用户通过 号
    User findByUsername(String number);

    //查询 消息列表
    List<WeixinList> findInformation(String number);

    //查询 通信录列表
    ContactList findContact(String number);

    public int addFriend(User Host, User Guest);
    public List<User> selectPhoneNumber(String phone);

    //修改个人信息
    int ModifyInfo(User user);
    //修改密码
    int ModifyPassword(User user);

    boolean createGroup(String groupname, List<User> chooses, User createuser);

    //获取signinfo
    List<SignInfo> getSignInfo(String grouid);
    //发起签到
    int  addASign(String grouid,String signtext,String signstarttime,String signmins,String jingdu,String weidu);
    public SignInfo getASign(String grouid);
    public List<SignRecord> getDetailSign(String signid);

    int insertSign(String name, String number, String signinfoid, String signsuccess);

    int getNum(String signid);
    int addsigntime(String signid, int time);

    int signtitle(String signid, String title);

    int signteacher(String signid, String phone);

    int signend(String signid);

    int signdelete(String signid);
}
