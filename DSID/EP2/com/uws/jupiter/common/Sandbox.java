package com.uws.jupiter.common;

import com.uws.jupiter.common.agents.Agent;

// Uma Sandbox represente uma thread de execução criada para um agente 
public class Sandbox implements Runnable {
   private Agent agent;
   private Thread controlThread;

   public Sandbox(Agent agent) {
      this.agent = agent;

      controlThread = new Thread(this);
   }

   @Override
   public void run() {
      agent.run();
   }

   public void start() {
      controlThread.start();
   }

}
