package scripts.tanner.runnables;

import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Timing;
import scripts.api.util.Logging;
import scripts.tanner.data.Vars;

import static scripts.tanner.data.Constants.COINS;

public class BankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Banking";
    }

    @Override
    public void run() {
        if (!Banking.isBankScreenOpen()) {
            openBank();
        }
        if (Banking.isBankScreenOpen()) {
            handleBanking();
        }
    }

    private void openBank() {
        if (Banking.openBankBooth()) {
            Timing.waitCondition(Banking::isBankScreenOpen, 1200);
        }
    }

    private void handleBanking() {
        if (Inventory.contains(Vars.get().hide.getTannedId())) {
            depositLeather();
        }
        if (!Inventory.contains(Vars.get().hide.getTannedId())) {
            withdrawHides();
        }
        checkForCoins();
    }

    private void depositLeather() {
        if (Banking.depositAll(Vars.get().hide.getTannedId())) {
            Timing.waitCondition(() -> !Inventory.contains(Vars.get().hide.getTannedId()), 1200);
        }
    }

    private void withdrawHides() {
        if (Banking.withdrawAll(Vars.get().hide.getUntannedId())) {
            Timing.waitCondition(() -> Inventory.contains(Vars.get().hide.getUntannedId()), 1200);
        } else if (Banking.isBankLoaded() && !Banking.contains(Vars.get().hide.getUntannedId())
            && !Timing.waitCondition(() -> Banking.contains(Vars.get().hide.getUntannedId()), 1200)) {
            Logging.critical("Out of hides to tan.");
            Vars.get().script.shutdown();
        }
    }

    private void checkForCoins() {
        if (Inventory.getCount(COINS) < Vars.get().hide.getCost()) {
            Logging.critical("Out of coins.");
            Vars.get().script.shutdown();
        }
    }

}
