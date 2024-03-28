package com.example.hanseohelper_muso;

public class ChattingInfo {
    public int chatImage;
    public String chatName;
    public String chatMessage;
    public String chatTime;

    public ChattingInfo(){
    }

    public ChattingInfo(int chatImage, String chatName, String chatMessage, String chatTime){
        this.chatImage = chatImage;
        this.chatName = chatName;
        this.chatMessage = chatMessage;
        this.chatTime = chatTime;
    }

    public String getChatName() {
        return chatName;
    }
    public int getChatImage() {
        return chatImage;
    }
    public String getChatMessage() {
        return chatMessage;
    }
    public String getChatTime() {
        return chatTime;
    }

    public void setChatName(String chatName){ this.chatName = chatName; }

    public void setChatImage(int chatImage) {
        this.chatImage = chatImage;
    }

    public void setChatMessage(String chatMessage) { this.chatMessage = chatMessage; }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

}