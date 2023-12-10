package com.example.service;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.pojo.*;
import com.example.tool.MD5;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao ud = new UserDaoImpl();

    @Override
    public int reigisterUser(String name, String phone, String password,String sex,String qianming) {
        int i = ud.insertUser(name, phone, password,sex,qianming);
        return i;
    }

    public int forgetPassword(String phone, String password) {
        int i = ud.updateUser(phone, password);
        return i;
    }


    @Override
    public User login(String number, String password) {
        //调用dao层完成数据查询操作
        User user = ud.findByUsername(number);
        System.out.println(user);
        if (user != null) {
            //比较密码
            if (MD5.getMD5(password).equals(user.getPassword())) {
                //登录成功
                return user;
            }
        }
        return null;
    }

    public List<WeixinList> informationUser(String number) {

        //调用dao层完成数据查询操作
        List<WeixinList> information = ud.findInformation(number);
        //System.out.println("us拿到" + information);

        return information;
    }

    public ContactList contact(String number) {

        //调用dao层完成数据查询操作
        ContactList contactList = ud.findContact(number);

        return contactList;
    }


    @Override
    public int addFriend(String Host, String Guest) {
        User host = ud.findByUsername(Host);
        User guest = ud.findByUsername(Guest);
        int i = ud.addFriend(host, guest);
        return i;
    }

    @Override
    public List<User> selectPhoneNumber(String phone) {
        List<User> listfriend = ud.selectPhoneNumber(phone);
        return listfriend;
    }

    @Override
    public int ModifyInfo(User user) {
        int i = ud.ModifyInfo(user);
        return i;
    }

    @Override
    public int ModifyPassword(User user) {
        int i = ud.ModifyPassword(user);
        return i;
    }

    @Override
    public boolean createGroup(String groupname, List<User> chooses, User createuser) {

        boolean flag = ud.createGroup(groupname, chooses, createuser);
        return flag;
    }


    @Override
    public List<SignInfo> getSignInfo(String grouid) {
        List<SignInfo> signInfos = ud.getSignInfo(grouid);
        //System.out.println("拿到" + signInfos);
        return signInfos;
    }

    @Override
    public int addASign(String grouid, String signtext, String signstarttime, String signmins,String jingdu,String weidu) {
        int i = ud.addASign(grouid, signtext, signstarttime, signmins,jingdu,weidu);
        return i;
    }

    public SignInfo getASign(String grouid) {

        SignInfo signInfo = ud.getASign(grouid);
        return signInfo;
    }

    public List<SignRecord> getDetailSign(String signid) {
        List<SignRecord> signRecords = ud.getDetailSign(signid);
        return signRecords;
    }

    @Override
    public int insertsign(String name, String number, String signinfoid, String signsuccess) {
        int i =ud.insertSign(name, number, signinfoid, signsuccess);
        return i;

    }

    @Override
    public int getNum(String signid) {
        int i =ud.getNum(signid);
        return i;
    }

    @Override
    public int addsigntime(String signid, int time) {
        int i =ud.addsigntime(signid,time);
        return i;
    }

    @Override
    public int signtitle(String signid, String title) {
        int i =ud.signtitle(signid,title);
        return i;
    }

    @Override
    public int signteacher(String signid, String phone) {
        int i =ud.signteacher(signid,phone);
        return i;
    }

    @Override
    public int signend(String signid) {
        int i =ud.signend(signid);
        return i;
    }

    @Override
    public int signdelete(String signid) {
        int i =ud.signdelete(signid);
        return i;
    }
}
