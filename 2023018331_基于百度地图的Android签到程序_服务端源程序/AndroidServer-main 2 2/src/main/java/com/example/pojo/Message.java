package com.example.pojo;

public class Message {
    private String type;
    private String content;
    private String sender;
    private String recipient;
    // 1 登陆
    public static final String LOGIN = "LOGIN";
    // 2 添加好友
    public static final String ADD_FRIEND = "ADD_FRIEND";
    // 3 修改个人信息
    public static final String CHANGE_USER_INFORMATION = "CHANGE_USER_INFORMATION";
    // 4 获取list
    public static final String GET_GROUP_LIST = "GET_GROUP_LIST";
    // 5 最近的签到
    public static final String LAST_SIGN = "LAST_SIGN";
    // 6 签到
    public static final String FINISH_SIGN = "FINISH_SIGN";
    // 7 创建群聊
    public static final String CREATE_GROUP = "CREATE_GROUP";
    // 8 获取群聊的签到list
    public static final String GROUP_SIGN_LIST = "GROUP_SIGN_LIST";
    // 9 发起签到
    public static final String ADD_SIGN = "ADD_SIGN";
    // 10 查看签到详细信息
    public static final String DETAIL_SIGN = "DETAIL_SIGN";
    // 11 监控SIGNNUM
    public static final String SIGNNUM = "SIGNNUM";

    // 12 查看后台数据并导出





    public static final String INFORMATION = "INFORMATION";
    public static final String SIGN_NUMBER = "SIGN_NUMBER";
    public static final String LATEST_SIGN = "LATEST_SIGN";
    public static final String RECORD = "SIGN_RECORD";
    public static final String ADD_RECORD = "ADD_RECORD";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Message(String type, String content, String sender, String recipient) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
