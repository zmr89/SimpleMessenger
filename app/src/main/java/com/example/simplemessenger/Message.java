package com.example.simplemessenger;

public class Message {
    private String textMessage;
    private String senderUserId;
    private String receiverUserId;

    public Message() {
    }

    public Message(String textMessage, String senderUserId, String receiverUserId) {
        this.textMessage = textMessage;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

}
