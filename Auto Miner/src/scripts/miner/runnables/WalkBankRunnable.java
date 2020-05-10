package scripts.miner.runnables;

import org.tribot.api.General;
import scripts.api.ABCUtil;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.miner.data.Vars;

public class WalkBankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Bank";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof MiningRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
              ? General.randomSD(500, 150)
              : General.randomSD(150, 50);
            General.sleep(sleep);
        }
        ABCUtil.performRunActivation();
        if (Vars.get().location.getPath() != null
          && Walking.walkPath(Walking.randomizePath(Vars.get().location.getPath()))
          || Vars.get().location.getPath() == null
          && Walking.walkStraightPath(Vars.get().location.getBank().getArea().getCenterTile())) {
            Timing.waitCondition(() -> Vars.get().location.getBank().containsPlayer(), 2400);
        }
    }
}