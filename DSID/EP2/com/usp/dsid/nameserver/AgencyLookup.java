package com.usp.dsid.nameserver;

import java.util.HashMap;

public class AgencyLookup {
    private String name;
    private String host;
    private int port;

    public AgencyLookup(String name){
        this.name = name;
    }
    
    public AgencyLookup(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public HashMap<String, String> getAddress() {
        HashMap<String, String> address = new HashMap<>();

        address.put("name", name);
        address.put("host", host);
        address.put("port", String.valueOf(port));

        return address;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) 
            return true;

        if (obj == null)
            return false;

        if (this.getClass() == obj.getClass()){
            AgencyLookup al = (AgencyLookup) obj;
            return this.name.equals(al.getName()) &&
                    this.host == al.getHost() &&
                    this.port == al.getPort();
        }

        return false;
    }
 
    // private int id;
    // private String operationalSystem;
    // private String processor;
    // private String ram;
    // private String videoCard;
    
    // public int getId() {
    //     return id;
    // }

    // public String getOperationalSystem() {
    //     return operationalSystem;
    // }

    // public String getProcessor() {
    //     return processor;
    // }

    // public String getRam() {
    //     return ram;
    // }
    
    // public String getVideoCard() {
    //     return videoCard;
    // }
}
