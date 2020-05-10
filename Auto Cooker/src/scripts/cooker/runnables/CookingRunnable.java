package scripts.cooker.runnables;

import org.tribot.api.General;
import org.tribot.api2007.Walking;
import scripts.api.ABCUtil;
import scripts.api.Inventory;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.cooker.data.Location;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.ROGUES_DEN_FIRE_TILE;

public class CookingRunnable implements Runnable {

    @Override
    public String toString() {
        return "Cooking";
    }

    @Override
    public void run() {
        if (Vars.get().location == Location.ROGUES_DEN && Player.getPosition().equals(ROGUES_DEN_FIRE_TILE)) {
            if (Walking.clickTileMS(ROGUES_DEN_FIRE_TILE.translate(1, 0), 1)) {
                Timing.waitCondition(() -> !Player.getPosition().equals(ROGUES_DEN_FIRE_TILE), 1200);
            }
        } else {
            if (Timing.waitCondition(() -> !Player.isAnimating() && timedActions(), 3000)) {
                Timing.waitCondition(() -> Player.isAnimating() || !Inventory.contains(Vars.get().rawId), 1500);
            }
        }
        if (!Inventory.contains(Vars.get().rawId)) {
            General.sleep(General.randomSD(800, 300));
        }
    }

    private boolean timedActions() {
        ABCUtil.performXPCheck();
        ABCUtil.performCheckTabs();
        ABCUtil.performExamineEntity();
        ABCUtil.performMoveMouse();
        ABCUtil.performRightClick();
        ABCUtil.performLeaveGame();
        return true;
    }

}
