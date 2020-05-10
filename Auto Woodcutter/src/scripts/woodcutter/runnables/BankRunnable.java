package scripts.woodcutter.runnables;

import org.tribot.api.interfaces.Clickable07;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.api.ABCUtil;
import scripts.api.Banking;
import scripts.api.Interact;
import scripts.api.Inventory;
import scripts.api.NPCs;
import scripts.api.Objects;
import scripts.api.Timing;

import java.util.HashSet;
import java.util.Set;

public class BankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Banking";
    }

    @Override
    public void run() {
        if (!Banking.isBankScreenOpen() && !Banking.isDepositBoxOpen()) {
            openBank();
        }
        if (Banking.isBankScreenOpen() || Banking.isDepositBoxOpen()) {
            if (Banking.depositAllExcept(getAxeIds()) > 0) {
                Timing.waitCondition(() -> !Inventory.isFull(), 1200);
            }
        }
    }

    private void openBank() {
        RSObject bankObject = Objects.find().actionEquals("Bank").getNearest();
        RSObject bankChest = Objects.find().actionEquals("Use", "Collect").nameContains("Bank").getNearest();
        RSNPC bankNPC = NPCs.find().actionEquals("Bank").getNearest();
        if (bankObject != null && bankNPC != null) {
            Clickable07 bank = ABCUtil.getOpenBankPreference() == OpenBankPreference.BANKER ? bankNPC : bankObject;
            if (Interact.with(bank).walkTo().cameraTurn().click("Bank")) {
                Timing.waitCondition(Banking::isBankScreenOpen, 2400);
            }
        } else if (bankObject != null) {
            if (Interact.with(bankObject).walkTo().cameraTurn().click("Bank")) {
                Timing.waitCondition(Banking::isBankScreenOpen, 2400);
            }
        } else if (bankNPC != null) {
            if (Interact.with(bankNPC).walkTo().cameraTurn().click("Bank")) {
                Timing.waitCondition(Banking::isBankScreenOpen, 2400);
            }
        } else if (bankChest != null) {
            if (Interact.with(bankChest).walkTo().cameraTurn().click("Use")) {
                Timing.waitCondition(Banking::isBankScreenOpen, 2400);
            }
        } else {
            RSObject depositBox = Objects.find().actionEquals("Deposit").getNearest();
            if (Interact.with(depositBox).walkTo().cameraTurn().click("Deposit")) {
                Timing.waitCondition(Banking::isDepositBoxOpen, 2400);
            }
        }
    }

    private int[] getAxeIds() {
        Set<Integer> pickaxeIds = new HashSet<>();
        RSItem[] items = Inventory.find().nameContains(" axe").getAll();
        for (RSItem item : items) {
            pickaxeIds.add(item.getID());
        }
        return pickaxeIds.stream().mapToInt(Number::intValue).toArray();
    }
}
