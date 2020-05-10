package scripts.cooker.runnables.wine;

import org.tribot.api.General;
import scripts.api.ABCUtil;
import scripts.api.Inventory;
import scripts.api.Player;
import scripts.api.Timing;

import static scripts.cooker.data.Constants.GRAPES;
import static scripts.cooker.data.Constants.JUG_OF_WATER;

public class MixingRunnable implements Runnable {

    @Override
    public String toString() {
        return "Mixing";
    }

    @Override
    public void run() {
        long lastAnimationTime = System.currentTimeMillis();
        long timeout = General.random(4800, 6000);
        while (Inventory.containsAll(GRAPES, JUG_OF_WATER) && Timing.timeFromMark(lastAnimationTime) < timeout) {
            General.sleep(900, 1500);
            timedActions();
            if (Timing.waitCondition(Player::isAnimating, 1200)) {
                lastAnimationTime = System.currentTimeMillis();
            }
        }
        if (!Inventory.containsAll(GRAPES, JUG_OF_WATER)) {
            General.sleep(General.randomSD(800, 300));
        }
    }

    private void timedActions() {
        ABCUtil.performXPCheck();
        ABCUtil.performCheckTabs();
        ABCUtil.performExamineEntity();
        ABCUtil.performMoveMouse();
        ABCUtil.performRightClick();
        ABCUtil.performLeaveGame();
    }

}
