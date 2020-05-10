package scripts.cooker.runnables.wine;

import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.api.*;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.*;

public class MixRunnable implements Runnable {

    @Override
    public String toString() {
        return "Attempting to Mix";
    }

    @Override
    public void run() {
        if (Banking.isBankScreenOpen() && Banking.close(false)) {
            Timing.waitCondition(() -> !Banking.isBankScreenOpen(), 1200);
        }
        if (!Banking.isBankScreenOpen()) {
            if (!Interfaces.isValid(COOKING_PARENT)) {
                mix();
            }
            if (Interfaces.isValid(COOKING_PARENT)) {
                makeAll();
            }
        }
    }

    private void mix() {
        RSItem[] grapes = Inventory.find().idEquals(GRAPES).getAll();
        RSItem[] jugsOfWater = Inventory.find().idEquals(JUG_OF_WATER).getAll();
        RSItem grape = null;
        RSItem jug = null;
        if (grapes.length > 0) {
            grape = grapes[0].getIndex() == 0 ? grapes[grapes.length - 1] : grapes[0];
        }
        if (jugsOfWater.length > 0) {
            jug = jugsOfWater[0].getIndex() == 0 ? jugsOfWater[jugsOfWater.length - 1] : jugsOfWater[0];
        }
        if (Inventory.combine(grape, jug)) {
            Timing.waitCondition(() -> Interfaces.isValid(COOKING_PARENT), 1200);
        }
    }

    private void makeAll() {
        RSInterface cookAllWidget = Interfaces.get(COOKING_PARENT, 14);
        if (Interact.with(cookAllWidget).click("Make") && Timing.waitCondition(Player::isAnimating, 1200)) {
            ABCUtil.generateTrackers(Inventory.getCount(Vars.get().rawId) * 2400, true);
        }
    }

}
