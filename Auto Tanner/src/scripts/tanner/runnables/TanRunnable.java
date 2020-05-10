package scripts.tanner.runnables;

import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.api.*;
import scripts.tanner.data.Vars;

import static scripts.tanner.data.Constants.COINS;
import static scripts.tanner.data.Constants.TANNING_INTERFACE;

public class TanRunnable implements Runnable {

    @Override
    public String toString() {
        return "Tanning";
    }

    @Override
    public void run() {
        if (!Interfaces.isValid(TANNING_INTERFACE)) {
            tradeTanner();
        }
        if (Interfaces.isValid(TANNING_INTERFACE)) {
            tanHides();
        }
    }

    private void tradeTanner() {
        RSNPC ellis = NPCs.find().nameEquals("Ellis").getFirst();
        if (Interact.with(ellis).walkTo().cameraTurn().click("Trade")
            && Timing.waitValue(Player::getInteractingCharacter, 1200) != null) {
            Timing.waitCondition(() -> Interfaces.isValid(TANNING_INTERFACE), 2100);
        }
    }

    private void tanHides() {
        int coins = Inventory.getCount(COINS);
        int untanned = Inventory.getCount(Vars.get().hide.getUntannedId());
        RSInterface tannerWidget = Interfaces.find().parentEquals(TANNING_INTERFACE).childEquals(Vars.get().hide.getChildId()).getFirst();
        if (Interact.with(tannerWidget).click("Tan All")
            && Timing.waitCondition(() -> Inventory.getCount(Vars.get().hide.getUntannedId()) < untanned, 1200)) {
            int tanned = untanned - Inventory.getCount(Vars.get().hide.getUntannedId());
            if (tanned == (coins - Inventory.getCount(COINS)) / Vars.get().hide.getCost()) {
                Vars.get().stats.addCount("tanned", tanned);
            }
        }
    }

}
