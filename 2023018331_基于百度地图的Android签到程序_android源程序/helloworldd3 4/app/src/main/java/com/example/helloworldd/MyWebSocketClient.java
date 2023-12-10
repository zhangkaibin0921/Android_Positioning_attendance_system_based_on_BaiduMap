package com.example.helloworldd;

import static com.example.helloworldd.pojo.Message.ADD_FRIEND;
import static com.example.helloworldd.pojo.Message.LOGIN;

import com.example.helloworldd.pojo.Message;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketClient extends WebSocketListener {
    private WebSocket webSocket;
    private String url;

    public MyWebSocketClient(String url) {
        this.url = url;
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();
        client.newWebSocket(request, this);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        // 连接成功
        System.out.println("WebSocket连接成功！");
        this.webSocket = webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        // 接收到文本消息
        System.out.println("收到消息：" + message);

        Gson gson = new Gson();
        Message message1 = gson.fromJson(message,Message.class);
        Object content= message1.getContent();
        String type= message1.getType();
        String sender= message1.getSender();
        String recipient= message1.getRecipient();
        processMessage(type,content,sender,recipient);
        System.out.println("Message received: " + message);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        // 接收到二进制消息
        System.out.println("收到二进制消息：" + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        // 关闭中
        System.out.println("WebSocket关闭中：" + code + "，" + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        // 已关闭
        System.out.println("WebSocket已关闭：" + code + "，" + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        // 连接失败
        System.out.println("WebSocket连接失败：" + t.getMessage());
    }

    public void send(String message) {
        while (webSocket==null){
        }
        if (webSocket != null) {
            webSocket.send(message);
            System.out.println("定位1");
        }
        System.out.println(webSocket+"定位2");

    }
    private void processMessage(String messageType, Object messageContent, String sender, String recipient) {
        switch (messageType) {
            case LOGIN:

                break;
            case ADD_FRIEND:
//                User user =Json2Str.fromJsonString(messageContent,User.class);
//                ContactListFragment.listfriend.add(user);
//                ContactListFragment.listpin.add(new UserPin(user.getName()));
//                Collections.sort(ContactListFragment.listpin);
//                ContactListFragment.adapter.setData(ContactListFragment.listpin);
//                System.out.println("定位3");
//                System.out.println("定位3");
//                System.out.println("定位3");
            default:
                // 不处理未知消息类型
                break;
        }
    }

}
