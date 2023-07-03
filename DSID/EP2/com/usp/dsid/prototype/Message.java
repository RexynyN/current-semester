package com.usp.dsid.prototype;

import java.io.Serializable;

public class Message implements Serializable{
    private Message sender;
    private String matter;
    private Object body;
    
    public Message(Message sender, String matter, String body) {
        this.sender = sender;
        this.matter = matter;
        this.body = body;
    }

    public Message getSender() {
        return sender;
    }

    public String getMatter() {
        return matter;
    }

    public Object getBody() {
        return body;
    }  
}
