package scripts.smither.tasks.smelting;

import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.api.util.Logging;
import scripts.smither.data.Bar;
import scripts.smither.data.Vars;

public class Bank implements Task {

    private final int AMMO_MOULD = 4;

    @Override
    public String toString() {
        return "Banking";
    }

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return !Vars.get().bar.hasOreRequirements() && Banking.Bank.EDGEVILLE.containsPlayer();
    }

    @Override
    public void execute() {
        if (!Banking.isBankScreenOpen()) {
            Banking.openBank();
        }
        if (Banking.isBankScreenOpen()) {
            deposit();
            withdraw();
        }
    }

    private void deposit() {
        if (!Vars.get().bar.hasOreRequirements() && Inventory.contains(Vars.get().bar.getId())) {
            if (Vars.get().bar != Bar.CANNONBALL) {
                if (Banking.depositAll() > 0) {
                    Timing.waitCondition(Inventory::isEmpty, 1200);
                }
            } else {
                if (Banking.depositAllExcept(AMMO_MOULD) > 0) {
                    Timing.waitCondition(() -> Inventory.containsOnly(AMMO_MOULD), 1200);
                }
            }
        }
    }

    private void withdraw() {
        if (!Vars.get().bar.hasOreRequirements() && !Inventory.contains(Vars.get().bar.getId())) {
            for (int i = 0; i < Vars.get().bar.getOreRequirements().length; i++) {
                int index = i;
                if (Banking.withdraw(Vars.get().bar.getOreRequirements()[index][2],
                  Vars.get().bar.getOreRequirements()[index][1])) {
                    Timing.waitCondition(() -> Inventory.contains(Vars.get().bar.getOreRequirements()[index][1]), 1000);
                } else if (Banking.isBankLoaded() && !Banking.contains(Vars.get().bar.getOreRequirements()[index][1])) {
                    if (!Timing.waitCondition(() -> Banking.contains(Vars.get().bar.getOreRequirements()[index][1]), 1500)) {
                        Logging.critical("Out of required ore. Ending script.");
                        Vars.get().script.shutdown();
                    }
                }
            }
        }
    }
}
