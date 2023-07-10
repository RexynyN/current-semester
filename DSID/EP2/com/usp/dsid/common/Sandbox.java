package com.usp.dsid.common;

import com.usp.dsid.common.agents.Agent;

/**
 * A Sandbox that an Agent can "play" in.
 *
 * Each Sandbox runs in its own thread of execution.
 * It runs its Agent in that thread.
 */
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
