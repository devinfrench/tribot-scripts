package scripts.cooker.runnables;

import org.tribot.api.interfaces.Clickable07;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Game;
import scripts.api.*;
import scripts.api.util.Logging;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.CATHERBY_BANK_BOOTH_TILE;
import static scripts.cooker.data.Constants.NARDAH_BANK_BOOTH_TILE;

public class BankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Banking";
    }

    @Override
    public void run() {
        if (!Banking.isBankScreenOpen() && openBank()) {
            Timing.waitCondition(Banking::isBankLoaded, 1200);
        }
        if (Banking.isBankScreenOpen()) {
            handleBanking();
        }
    }

    private boolean openBank() {
        Clickable07 bank = null;
        String action = "Bank";
        switch (Vars.get().location) {
            case ROGUES_DEN:
                bank = NPCs.find().nameEquals("Emerald Benedict").getFirst();
                break;
            case CATHERBY:
                bank = Objects.find().nameEquals("Bank booth").atPosition(CATHERBY_BANK_BOOTH_TILE).getFirst();
                break;
            case NARDAH:
                bank = Objects.find().nameEquals("Bank booth").atPosition(NARDAH_BANK_BOOTH_TILE).getFirst();
                break;
            case AL_KHARID:
                bank = Objects.find().nameEquals("Bank booth").getFirst();
                break;
            case HOSIDIUS:
                bank = Objects.find().nameEquals("Bank chest").getFirst();
                action = "Use";
                break;
        }
        int distance = 10;
        if (bank != null) {
            distance = (int) PathFinding.distanceTo((Positionable) bank, false);
        }
        int multiplier = Game.isRunOn() && Game.getRunEnergy() >= 15 ? 400 : 600;
        if (Interact.with(bank).click(action)) {
            Timing.waitCondition(Banking::isBankScreenOpen, distance * multiplier + 600);
        }
        return Banking.isBankScreenOpen();
    }

    private void handleBanking() {
        if (!Inventory.isEmpty() && !Inventory.contains(Vars.get().rawId)) {
            deposit();
        }
        if (Inventory.isEmpty() && !Inventory.contains(Vars.get().rawId)) {
            withdraw();
        }
    }

    private void deposit() {
        if (Banking.depositAll() > 0) {
            Timing.waitCondition(Inventory::isEmpty, 1200);
        }
    }

    private void withdraw() {
        if (Banking.withdrawAll(Vars.get().rawId)) {
            Timing.waitCondition(() -> Inventory.contains(Vars.get().rawId), 1200);
        } else if (Banking.isBankLoaded() && !Banking.contains(Vars.get().rawId)
          && !Timing.waitCondition(() -> Banking.contains(Vars.get().rawId), 1200)) {
            Logging.critical("Out of raw food to cook.");
            Vars.get().script.shutdown();
        }
    }

}
