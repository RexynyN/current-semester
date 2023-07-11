package com.usp.dsid.nameserver;

import com.usp.dsid.common.Host;

public class AgentLookup {
    private String name; 
    private String type;
    private Host originAgency;
    private Host currentAgency;

    

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
