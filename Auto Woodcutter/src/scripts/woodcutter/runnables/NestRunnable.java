package scripts.woodcutter.runnables;

import org.tribot.api.General;
import scripts.api.GroundItems;
import scripts.api.Interact;
import scripts.api.Timing;
import scripts.woodcutter.data.Vars;

public class NestRunnable implements Runnable {

    @Override
    public String toString() {
        return "Picking Up Nest";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof CuttingRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
                ? Vars.get().tree.getReactionTime()
                : General.randomSD(120, 60);
            General.sleep(sleep);
        }
        if (Interact.with(GroundItems.find().nameContains("nest").getFirst()).walkTo().cameraTurn().click("Take")) {
            Timing.waitCondition(() -> GroundItems.find().nameContains("nest").getAll().length == 0, 2400);
        }
    }
}
