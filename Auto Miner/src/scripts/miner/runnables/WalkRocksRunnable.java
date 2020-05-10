package scripts.miner.runnables;

import scripts.api.ABCUtil;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.miner.data.Option;
import scripts.miner.data.Vars;

public class WalkRocksRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Rocks";
    }

    @Override
    public void run() {
        if (Vars.get().isStationary && Vars.get().option != Option.BANK && Player.isInArea(Vars.get().miningArea)) {
            if (Interact.with(Vars.get().stationaryTile).walkTo().cameraTurn().click("Walk here")) {
                Timing.waitCondition(() -> Player.getPosition().equals(Vars.get().stationaryTile),
                  Player.distanceTo(Vars.get().stationaryTile) * 600 + 1200);
            }
        } else {
            ABCUtil.performRunActivation();
            if (Vars.get().location != null && Vars.get().location.getPath() != null
              && Walking.canWalkPath(Vars.get().location.getPath())) {
                if (Walking.walkPath(Walking.randomizePath(Walking.invertPath(Vars.get().location.getPath())))) {
                    Timing.waitCondition(() -> Player.isInArea(Vars.get().miningArea), 2400);
                }
            } else {
                if (Walking.walkStraightPath(Vars.get().miningArea.getClosestTile())) {
                    Timing.waitCondition(() -> Player.isInArea(Vars.get().miningArea), 2400);
                }
            }
        }
    }
}
