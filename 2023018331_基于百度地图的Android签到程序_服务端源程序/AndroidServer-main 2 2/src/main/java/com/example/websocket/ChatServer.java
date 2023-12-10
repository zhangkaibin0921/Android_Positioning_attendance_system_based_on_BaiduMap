package com.example.websocket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pojo.Message;
import com.example.pojo.User;
import com.example.util.Json2Str;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.pojo.Message.*;

@ServerEndpoint("/websocket/{userId}")
public class ChatServer {
    private static Map<String, Session> sessionMap = new HashMap<>();
    public static HashMap<String,User> users = new HashMap<>();
    public  static Jedis jedis;

    // 在对应的方法中根据消息类型处理消息，并发送给对应的客户端
    private void processMessage(String message) {
        JSONObject rjson = (JSONObject) JSONObject.parse(message);
        System.out.println(rjson);
//        switch (messageType) {
//            case LOGIN:
//                User user=new Gson().fromJson(messageContent,User.class);
//                users.put(user.getPhone()+"",user);
//                System.out.println(user+"测试");
//                System.out.println("User: " + user);
//                break;
//            case ADD_FRIEND:
//                if (users.containsKey(recipient))
//                {
//                    sendToClient(sender,recipient,ADD_FRIEND,Json2Str.toJsonString(users.get(sender),User.class));
//                    System.out.println("9967");
//                }
//                sendToClient(sender, recipient, ADD_FRIEND, messageContent);
//                break;
//            case CHANGE_USER_INFORMATION:
//                // 处理修改个人信息请求
//                sendToClient(sender, recipient, CHANGE_USER_INFORMATION, messageContent);
//                break;
//            case GET_GROUP_LIST:
//                // 处理获取群聊列表请求
//                sendToClient(sender, recipient, GET_GROUP_LIST, messageContent);
//                break;
//            case LAST_SIGN:
//                // 处理最近签到请求
//                sendToAllClients(LAST_SIGN, messageContent);
//                break;
//            case FINISH_SIGN:
//                // 处理签到请求
//                sendToClient(sender, recipient, FINISH_SIGN, messageContent);
//                sendToAllClients(GROUP_SIGN_LIST, messageContent);
//                break;
//            case CREATE_GROUP:
//                // 处理创建群聊请求
//                sendToClient(sender, recipient, CREATE_GROUP, messageContent);
//                sendToAllClients(GET_GROUP_LIST, messageContent);
//                break;
//            case GROUP_SIGN_LIST:
//                // 处理获取群聊签到列表请求
//                sendToClient(sender, recipient, GROUP_SIGN_LIST, messageContent);
//                break;
//            case ADD_SIGN:
//                // 处理发起签到请求
//                sendToClient(sender, recipient, ADD_SIGN, messageContent);
//                sendToAllClients(GROUP_SIGN_LIST, messageContent);
//                break;
//            case DETAIL_SIGN:
//                // 处理查看签到详细信息请求
//                sendToClient(sender, recipient, DETAIL_SIGN, messageContent);
//                break;
//            case SIGNNUM:
//                // 处理监控SIGNNUM请求
//                sendToClient(sender, recipient, SIGNNUM, messageContent);
//                break;
//            default:
//                // 不处理未知消息类型
//                break;
//        }
    }


    // 发送消息给所有客户端
    private void sendToAllClients(String messageType, String messageContent) {
        for (Session session : sessionMap.values()) {
            try {
                // 构造消息内容并发送
                String message = messageType + ":" + messageContent;
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                // 发送失败，忽略异常
            }
        }
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        System.out.println("WebSocket opened: " + session.getId());
        sessionMap.put(userId, session);
        session.getBasicRemote().sendText("欢迎 " + userId + " 进入聊天室！");
        System.out.println("欢迎 " + userId + " 进入聊天室！");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

//        Type listOfMyClassObject = new TypeToken<HashMap<String,String>>() {}.getType();
//        JSONObject rjson = (JSONObject) JSONObject.parse(message);
//        HashMap<String,String> hashMap= new Gson().fromJson(URLDecoder.decode(rjson.getString("chooses"), "utf-8"), listOfMyClassObject);

//        String content= message1.getContent();
//        String type= message1.getType();
//        String sender= message1.getSender();
//        String recipient= message1.getRecipient();
//        processMessage(type,content,sender,recipient);
//        System.out.println(hashMap);
//        System.out.println(rjson+"----");
        System.out.println("Message received: " + message);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                processMessage(message);
//            }
//        }).start();

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("WebSocket closed: " + session.getId());

        // 找到对应的userId并从map中移除
        String userId = null;
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                userId = entry.getKey();
                break;
            }
        }
        sessionMap.remove(userId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error: " + throwable.getMessage());
    }
    private void sendToClient(String sender,String recipient, String messageType, String messageContent) {
        Session session = sessionMap.get(recipient);
        if (session != null) {
            try {
                Message message1 = new Message(messageType,messageContent,sender,recipient);
                Gson gson = new Gson();
                String str=gson.toJson(message1,Message.class);
                session.getBasicRemote().sendText(str);
            } catch (IOException e) {
                // 发送失败，忽略异常
            }
        }
    }

}
