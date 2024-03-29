package com.uws.jupiter.common;

import java.io.Serializable;

// Representa um agente na rede e sua localização 
public class AgentLookup implements Serializable {
    private String name; 
    private String type;
    private Host originAgency;
    private Host currentAgency;


    public AgentLookup(String name, String type, Host originAgency) {
        this.name = name;
        this.originAgency = originAgency;
        this.currentAgency = originAgency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) 
            return true;

        if (obj == null)
            return false;

        if (this.getClass() == obj.getClass()){
            AgentLookup al = (AgentLookup) obj;
            return this.originAgency.equals(al.getOriginAgency()) &&
                    this.name.equals(al.getName());
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Host getOriginAgency() {
        return originAgency;
    }

    public Host getCurrentAgency() {
        return currentAgency;
    }

    public void setCurrentAgency(Host currentAgency) {
        this.currentAgency = currentAgency;
    }
}
