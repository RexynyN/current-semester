package com.usp.dsid.prototype;

import java.io.Serializable;

public class Host implements Serializable {
    private String host = "localhost";
    private int port;
    private String name;
    private String id;1
    
    public Host(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public Host(int port, String name, String id) {
        this.port = port;
        this.name = name;
        this.id = id;
    }

    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) 
            return true;

        if (obj == null)
            return false;

        if (this.getClass() == obj.getClass()){
            Host ht = (Host) obj;
            return this.host.equals(ht.getHost()) &&
                    this.name.equals(ht.getName()) &&
                    this.port == ht.getPort();
        }

        return false;
    }



    
}
