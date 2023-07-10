package com.usp.dsid.nameserver;

public class AgentLookup {
    private String name; 
    private String type;
    private AgencyLookup originAgency;
    private AgencyLookup currentAgency;

    public AgentLookup(String name, String originAgency) {
        this.name = name;
        this.originAgency = originAgency;
    }

    
    

    public AgentLookup(String name, String originAgency, AgencyLookup currentAgency) {
        this.name = name;
        this.originAgency = originAgency;
        this.currentAgency = currentAgency;
    }

    public String getOriginAgency() {
        return originAgency;
    }

    public AgencyLookup getCurrentAgency() {
        return currentAgency;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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
                    this.name.equals(al.getName()) &&
                    this.type.equals(al.getType());
        }

        return false;
    }
}
