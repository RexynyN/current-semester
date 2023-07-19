package com.uws.jupiter.common;

import java.io.Serializable;

// Representa uma mensagem mandada entre agentes
public class Message implements Serializable{
    private AgentLookup sender;
    private String matter;
    private String [] body;
    
    public Message(AgentLookup sender, String matter, String [] body) {
        this.sender = sender;
        this.matter = matter;
        this.body = body;
    }

    public AgentLookup getSender() {
        return sender;
    }

    public String getMatter() {
        return matter;
    }

    public String [] getBody() {
        return body;
    }  
}
