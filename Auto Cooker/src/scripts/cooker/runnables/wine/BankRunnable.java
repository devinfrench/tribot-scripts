package scripts.cooker.runnables.wine;

import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Timing;
import scripts.api.util.Logging;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.*;

public class BankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Banking";
    }

    @Override
    public void run() {
        if (Inventory.isItemSelected()) {
            Inventory.cancelSelectItem();
        } else {
            if (!Banking.isBankScreenOpen() && Banking.openBank()) {
                Timing.waitCondition(Banking::isBankLoaded, 2400);
            }
            if (Banking.isBankScreenOpen()) {
                handleBanking();
            }
        }
    }

    private void handleBanking() {
        if (Inventory.contains(JUG_OF_WINE, UNFERMENTED_WINE)) {
            depositWine();
        }
        if (!Inventory.contains(JUG_OF_WINE, UNFERMENTED_WINE)) {
            if (!Inventory.contains(GRAPES)) {
                withdrawGrapes();
            }
            if (!Inventory.contains(JUG_OF_WATER)) {
                withdrawJugOfWater();
            }
        }
    }

    private void depositWine() {
        if (Banking.depositAllExcept(GRAPES, JUG_OF_WATER) > 0) {
            Timing.waitCondition(() -> !Inventory.contains(JUG_OF_WINE, UNFERMENTED_WINE), 1800);
        }
    }

    private void withdrawGrapes() {
        int amount = Inventory.getCount(JUG_OF_WATER) == 14 ? 0 : 14;
        if (Banking.withdraw(amount, GRAPES)) {
            Timing.waitCondition(() -> Inventory.contains(GRAPES), 1800);
        } else if (Banking.isBankLoaded() && !Banking.contains(GRAPES)
          && !Timing.waitCondition(() -> Banking.contains(GRAPES), 1800)) {
            Logging.critical("Out of grapes.");
            Vars.get().script.shutdown();
        }
    }

    private void withdrawJugOfWater() {
        int amount = Inventory.getCount(GRAPES) == 14 ? 0 : 14;
        if (Banking.withdraw(amount, JUG_OF_WATER)) {
            Timing.waitCondition(() -> Inventory.contains(JUG_OF_WATER), 1800);
        } else if (Banking.isBankLoaded() && !Banking.contains(JUG_OF_WATER)
          && !Timing.waitCondition(() -> Banking.contains(JUG_OF_WATER), 1800)) {
            Logging.critical("Out of jugs of water.");
            Vars.get().script.shutdown();
        }
    }

}
