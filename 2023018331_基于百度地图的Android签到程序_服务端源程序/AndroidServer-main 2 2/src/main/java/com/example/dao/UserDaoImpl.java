package com.example.dao;

import com.example.pojo.*;
import com.example.tool.MD5;
import com.example.util.JDBCUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserDaoImpl implements UserDao {
    ResultSet rs;

    @Override
    public int insertUser(String name, String phone, String password,String sex,String qianming) {
        String sql = "insert into user(name, phone, password,sex,qianming) values(?,?,?,?,?);";
        //i如果操作成功，就是操作成功的条数
        int i = JDBCUtil.executeUpdate(sql, name, phone, MD5.getMD5(password),sex,qianming);
        return i;
    }
    public int updateUser(String phone,String password){
        String sql = "update user set password=? where phone=?;";
        int i = JDBCUtil.executeUpdate(sql,MD5.getMD5(password),phone );
        return i;
    }

    @Override
    public User findByUsername(String number) {
            String sql = "select * from user where phone=?";
            rs = JDBCUtil.executeQuery(sql, number);

        try {
            if (rs.next()) {
                //如果查询到用户，将用户封装到User对象中
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                //将查询到的用户封装到一个User对象中
                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setPassword(password);
                user.setPhone(phone);
                user.setQianming(rs.getString("qianming"));
                user.setSex(rs.getString("sex"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WeixinList> findInformation(String number) {
        //sql
        String sql = "select * from weixinlist where number=?;";
        ResultSet rs = JDBCUtil.executeQuery(sql, number);
        //判断是否查询到用户
        List<WeixinList> list = new ArrayList<>();
        try {
            while (rs.next()) {
                //如果查询到用户，将用户封装到User对象中
                int id = rs.getInt("id");
                String title1 = rs.getString("title");
                String content = rs.getString("content");
                String time = rs.getString("time");
                String number1 = rs.getString("number");
                //将查询到的用户封装到一个weixinList对象中
                WeixinList weixinList = new WeixinList();
                weixinList.setId(id);
                weixinList.setTitle(title1);
                weixinList.setContent(content);
                weixinList.setTime(time);
                weixinList.setNumber(number1);
                weixinList.setCreateusernumber(rs.getString("createusernumber"));
                weixinList.setGroupid(rs.getString("groupid"));
                System.out.println("findInformation 查询到的消息" + weixinList);

                list.add(weixinList);

            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("findInformation ud返回"+list);
        return list;
    }

    @Override
    public ContactList findContact(String number) {

        String sql = "select * from contact where number=?;";
        ResultSet rs = JDBCUtil.executeQuery(sql, number);
        //判断是否查询到用户
        try {
            if (rs.next()) {
                //如果查询到用户，将用户封装到User对象中
                int id = rs.getInt("id");
                String img = rs.getString("img");
                String name = rs.getString("name");
                String number1 = rs.getString("number");
                //将查询到的用户封装到一个User对象中
                ContactList contactList = new ContactList();
                contactList .setId(id);
                contactList .setImg(img);
                contactList .setName(name);
                contactList .setNumber(number1);
                System.out.println("查询到的用户" + contactList);
                return contactList;
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @Override
    public int addFriend(User Host, User Guest) {
        String sql = "insert into contactlist (hostid, guestid, hostname,guestname,hostphone,guestphone) values(?,?,?,?,?,?);";
        //i如果操作成功，就是操作成功的条数
        int i = JDBCUtil.executeUpdate(sql, Host.getId(), Guest.getId(), Host.getName(),Guest.getName(),Host.getPhone(),Guest.getPhone());
        int j = JDBCUtil.executeUpdate(sql, Guest.getId(), Host.getId(), Guest.getName(),Host.getName(),Guest.getPhone(),Host.getPhone());
        return i+j;
    }

    @Override
    public List<User> selectPhoneNumber(String phone) {

        List<User> listfriend=new ArrayList<>();

//        ArrayList<HashMap<String,String>> strings = new ArrayList<>();
//        HashMap hashMap = new HashMap();
//

        String sql = "select * from contactlist where  hostphone= ?;";
        ResultSet rs = JDBCUtil.executeQuery(sql,phone);
        try {
            while (rs.next())
            {
                User temp=new User();
                temp.setName(rs.getString("guestname"));
                temp.setNumber(rs.getString("guestphone"));
                listfriend.add(temp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listfriend;
    }

    @Override
    public int ModifyInfo(User user) {
        String sql="update user set name=? , sex=? , qianming=? where phone= ?;";
        int i =JDBCUtil.executeUpdate(sql,user.getName(),user.getSex(),user.getQianming(),user.getPhone());
        sql="update contactlist set hostname =? where hostphone =?";
        JDBCUtil.executeUpdate(sql,user.getName(),user.getPhone());
        sql="update contactlist set guestname =? where guestphone =?";
        JDBCUtil.executeUpdate(sql,user.getName(),user.getPhone());
        return i;
    }
    @Override
    public int ModifyPassword(User user) {
        String sql="update user set password=? where phone=?;";
        int i =JDBCUtil.executeUpdate(sql,MD5.getMD5(user.getPassword()),user.getPhone());
        System.out.println(user);
        return i;
    }


    public boolean createGroup(String groupname, List<User> chooses, User createuser){
        Random random = new Random();
        int randomNumber = random.nextInt();
        createuser.setNumber(createuser.getPhone());
        System.out.println(chooses);
        String sql="insert into weixinlist(title,number,createusernumber,groupid) values (?,?,?,?)";
        for(int i=0;i<chooses.size();i++){
            System.out.println(chooses.size()+" "+i+chooses.get(i));
            JDBCUtil.executeUpdate(sql,groupname,chooses.get(i).getNumber(),createuser.getPhone(),randomNumber+"");
        }
        return true;
    }

    @Override
    public List<SignInfo> getSignInfo(String grouid) {
        String sql="select * from signinfo where groupid = ?;";

        ResultSet rs = JDBCUtil.executeQuery(sql, grouid);
        //判断是否查询到用户
        List<SignInfo> signInfos = new ArrayList<>();
        try {
            while (rs.next()) {
                //将查询到的用户封装到一个weixinList对象中
                SignInfo signInfo = new SignInfo();
                signInfo.setSigntext(rs.getString("signtext"));
                signInfo.setSignstarttime(rs.getString("signstarttime"));
                signInfo.setSignmins(rs.getString("signmins"));
                signInfo.setSignnums(rs.getString("signnums"));
                signInfo.setSignid(rs.getString("signid"));
                System.out.println("getSignInfo 查询到的消息" + signInfo);
                signInfos.add(signInfo);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("getSignInfo ud返回"+signInfos);
        return signInfos;
    }

    @Override
    public int addASign(String grouid, String signtext, String signstarttime, String signmins,String jingdu,String weidu) {
        int j=0;
        String signid="";
        try {
            String sql="insert into signinfo (groupid,signtext,signstarttime,signmins,signnums,jingdu,weidu) values(?,?,?,?,?,?,?);";
            int i = JDBCUtil.executeUpdate(sql,grouid,signtext,signstarttime,signmins,0,jingdu,weidu);

            sql="select signid from signinfo where groupid=? and signstarttime=?;";
            ResultSet resultSet=JDBCUtil.executeQuery(sql,grouid,signstarttime);
            resultSet.next();
            signid=resultSet.getString("signid");

            sql="select * from weixinlist where groupid =?;";
            ResultSet rs = JDBCUtil.executeQuery(sql, grouid);
            ArrayList<String>userphone=new ArrayList<>();
            while (rs.next()) {
                String str1=rs.getString("number");
                String str2=rs.getString("createusernumber");
                System.out.println("str1="+str1);
                System.out.println("str2="+str2);
                System.out.println("=="+str1.equals(str2));
                if (!str1.equals(str2)){userphone.add(rs.getString("number"));}
                }
            System.out.println("电话 "+userphone);
            ArrayList<String>username=new ArrayList<>();
            for (int i1 = 0; i1 < userphone.size(); i1++) {
                sql="select name from user where phone = ?";
                ResultSet resultSet1 = JDBCUtil.executeQuery(sql,userphone.get(i1));
                resultSet1.next();
                username.add(resultSet1.getString("name"));
            }
            System.out.println("用户名"+username);
            for (int i1 = 0; i1 < userphone.size(); i1++) {
                sql="insert into signrecord (signid,username,usernumber,signsuccess,signtime,text) values(?,?,?,?,?,?)";
                j=JDBCUtil.executeUpdate(sql,signid,username.get(i1),userphone.get(i1),"未签到","2022-12-10 00:00:00",signtext);
                System.out.println("插入signrecord "+j);
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Integer.valueOf(signid);
    }
    public SignInfo getASign(String grouid)
    {
        String sql="select * from signinfo where groupid = ?;";

        ResultSet rs = JDBCUtil.executeQuery(sql, grouid);
        //判断是否查询到用户

        SignInfo signInfo = new SignInfo();
        try {
            while (rs.next()) {
                //将查询到的用户封装到一个weixinList对象中
                signInfo.setSigntext(rs.getString("signtext"));
                signInfo.setSignstarttime(rs.getString("signstarttime"));
                signInfo.setSignmins(rs.getString("signmins"));
                signInfo.setSignnums(rs.getString("signnums"));
                signInfo.setSignid(rs.getString("signid"));
                signInfo.setJingdu(rs.getString("jingdu"));
                signInfo.setWeidu(rs.getString("weidu"));
                System.out.println("查询到的消息" + signInfo);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("ud返回"+signInfo);
        return signInfo;
    }

    public List<SignRecord> getDetailSign(String signid){
        String sql="select * from signrecord where signid = ?;";

        ResultSet rs = JDBCUtil.executeQuery(sql, signid);
        //判断是否查询到用户
        List<SignRecord> signRecords = new ArrayList<>();

        try {
            while (rs.next()) {
                SignRecord signRecord = new SignRecord();
                //将查询到的用户封装到一个weixinList对象中
                signRecord.setUsername(rs.getString("username"));
                signRecord.setSignsuccess(rs.getString("signsuccess"));
                signRecord.setSigntime(rs.getString("signtime"));
                System.out.println("查询到的消息" + signRecord);
                signRecords.add(signRecord);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("ud返回"+signRecords);
        return signRecords;



    }
// update
    @Override
    public int insertSign(String name, String number, String signinfoid, String signsuccess) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timee = formatter.format(date);
        String sql="update signrecord set signsuccess= ? ,signtime=? where signid=? and usernumber=?";
        int i = JDBCUtil.executeUpdate(sql,signsuccess,timee , signinfoid, number);
         sql ="UPDATE signinfo SET signnums = (SELECT COUNT(*) FROM signrecord WHERE signid = ? and signsuccess!=?);";
         int j =JDBCUtil.executeUpdate(sql,signinfoid,"未签到");
        System.out.println("j="+j);
        return i;
    }

    @Override
    public int getNum(String signid) {
        String sql="select signnums from signinfo where signid=?;";
        String nums="1";
        ResultSet resultSet =JDBCUtil.executeQuery(sql,signid);
        try {
            resultSet.next();
            nums = resultSet.getString(1);
        }catch (Exception e){e.printStackTrace();}
        System.out.println("NUMS "+nums);
        return Integer.valueOf(nums);
    }

    @Override
    public int addsigntime(String signid, int time) {
        String sql="update signinfo set signmins=signinfo.signmins+? where signid =?";
        int i =JDBCUtil.executeUpdate(sql,time,signid);
        return i;

    }

    @Override
    public int signtitle(String signid, String title) {
        String sql="update signinfo set signtext=? where signid =?";
        int i =JDBCUtil.executeUpdate(sql,title,signid);
        return i;
    }

    @Override
    public int signteacher(String signid, String phone) {
        String sql="update signrecord set signsuccess = ?where usernumber=? and signid=?";
        int i =JDBCUtil.executeUpdate(sql,"学生请假",phone,signid);
        sql ="UPDATE signinfo SET signnums = (SELECT COUNT(*) FROM signrecord WHERE signid = ?);";
        int j =JDBCUtil.executeUpdate(sql,signid);
        return i;
    }

    @Override
    public int signend(String signid) {
        long j= System.currentTimeMillis();
        String sql ="select signstarttime from signinfo where signid =?";
        ResultSet resultSet=JDBCUtil.executeQuery(sql,signid);
        String da = "1";
        try {
            resultSet.next();
             da =resultSet.getString("signstarttime");
        }catch (Exception e)
        {e.printStackTrace();}
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date = df.parse(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timee=date.getTime();
        long ii=(System.currentTimeMillis()-timee)/(60*1000);
        sql="update signinfo set signmins=? where signid =?";
        int i =JDBCUtil.executeUpdate(sql,ii,signid);
        System.out.println("ii"+ii+"  "+timee);
        return i;
    }

    @Override
    public int signdelete(String signid) {
        String sql="delete from signinfo where signid =?";
        int i =JDBCUtil.executeUpdate(sql,signid);
        sql="delete from signrecord where signid =?";
        int j =JDBCUtil.executeUpdate(sql,signid);
        return j;
    }
}
