package com.usp.dsid.common.agents;

import com.usp.dsid.common.apollo.CodeResult;

public class Worker implements Agent {
    private String name; 
    private String payload;
    private CodeResult result;
    
    public Worker(String name, String payload) {
        this.name = name;
        this.payload = payload;
    }

    public Worker(String name) {
        this.name = name;
    }

    public String getPayload() {
        return payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    public CodeResult getResult() {
        return result;
    }
    
    public void setResult(CodeResult result) {
        this.result = result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "worker";
    }

}
