package bruh;

/**
 * A Sandbox that an Agent can "play" in.
 *
 * Each Sandbox runs in its own thread of execution.
 * It runs its Agent in that thread.
 *
 * @version 1.0
 * @author Prof. David Bernstein, James Madison University
 */
public class Sandbox implements Runnable {
   private Agent agent;
   private Thread controlThread;

   /**
    * Explicit Value Constructor
    *
    * @param agent The Agent to execute
    */
   public Sandbox(Agent agent) {
      this.agent = agent;

      controlThread = new Thread(this);
   }

   /**
    * The code to execute in this Sandbox object's
    * thread of execution
    */
   @Override
   public void run() {
      agent.run();
   }

   /**
    * Start this Sandbox
    */
   public void start() {
      controlThread.start();
   }

}
