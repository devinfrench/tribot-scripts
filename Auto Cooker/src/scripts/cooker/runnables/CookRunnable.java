package scripts.cooker.runnables;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.api.*;
import scripts.cooker.data.Location;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.COOKING_PARENT;
import static scripts.cooker.data.Constants.RAW_KARAMBWAN;

public class CookRunnable implements Runnable {

    @Override
    public String toString() {
        return "Attempting to Cook";
    }

    @Override
    public void run() {
        if (Banking.isBankScreenOpen() && Banking.close(true)) {
            Timing.waitCondition(() -> !Banking.isBankScreenOpen(), 1200);
        }
        if (Inventory.getCount(Vars.get().rawId) < 28
          && Timing.waitCondition(Player::isAnimating, 900)) {
            return;
        }
        if (!Banking.isBankScreenOpen()) {
            if (!Interfaces.isValid(COOKING_PARENT)) {
                if (Vars.get().location == Location.ROGUES_DEN) {
                    if (useRaw()) {
                        clickHeatSource("Use");
                    }
                } else {
                    clickHeatSource("Cook");
                }
            }
            if (Interfaces.isValid(COOKING_PARENT)) {
                cookAll();
            }
        }
    }

    private boolean useRaw() {
        if (!Inventory.isItemSelected()) {
            RSItem raw = Inventory.find().idEquals(Vars.get().rawId).getFirst();
            if (Interact.with(raw).click("Use")) {
                Timing.waitCondition(Inventory::isItemSelected, 900);
            }
        }
        return Inventory.isItemSelected();
    }

    private void clickHeatSource(String action) {
        RSObject heatSource = Vars.get().location.getHeatSource();
        int multiplier = Game.isRunOn() && Game.getRunEnergy() >= 15 ? 400 : 600;
        int distance = (int) PathFinding.distanceTo(heatSource, false);
        if (Interact.with(heatSource).walkTo().cameraTurn().click(action)
          && Timing.waitCondition(() -> Interfaces.isValid(COOKING_PARENT), distance * multiplier + 1200)) {
            General.sleep(General.randomSD(600, 300));
        }
    }

    private void cookAll() {
        int child = 14;
        if (Vars.get().rawId == RAW_KARAMBWAN) {
            child = 15;
        }
        RSInterface cookAllWidget = Interfaces.find().parentEquals(COOKING_PARENT).childEquals(child).getFirst();
        if (cookAllWidget != null) {
            if (Vars.get().rawId != RAW_KARAMBWAN) {
                Keyboard.pressSpace();
            } else {
                Keyboard.typeString(Integer.toString(2));
            }
            if (Timing.waitCondition(Player::isAnimating, 1800)) {
                ABCUtil.generateTrackers(Inventory.getCount(Vars.get().rawId) * 2400, true);
            }
        }
//        if (Interact.with(cookAllWidget).click("Cook") && Timing.waitCondition(Player::isAnimating, 1800)) {
//            ABCUtil.generateTrackers(Inventory.getCount(Vars.get().rawId) * 2400, true);
//        }
    }

}
