package scripts.smither.tasks.smithing;

import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.api.util.Logging;
import scripts.smither.data.Vars;

public class Bank implements Task {

    private static final int HAMMER = 2347;

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
        return Banking.Bank.VARROCK_WEST.containsPlayer()
          && Inventory.getCount(Vars.get().bar.getId()) < Vars.get().item.getBarsRequired();
    }

    @Override
    public void execute() {
        if (!Banking.isBankScreenOpen()) {
            openBank();
        }
        if (Banking.isBankScreenOpen()) {
            deposit();
            withdraw();
        }
    }

    private void openBank() {
        if (Banking.openBank()) {
            Timing.waitCondition(Banking::isBankLoaded, 1200);
        }
    }

    private void deposit() {
        if (!Inventory.containsOnly(HAMMER, Vars.get().bar.getId()) && Banking.depositAllExcept(HAMMER, Vars.get().bar.getId()) > 0) {
            Timing.waitCondition(() -> Inventory.containsOnly(HAMMER, Vars.get().bar.getId()), 1200);
        }
    }

    private void withdraw() {
        if (Inventory.containsOnly(HAMMER, Vars.get().bar.getId())) {
            if (Banking.withdraw(0, Vars.get().bar.getId())) {
                Timing.waitCondition(() -> Inventory.getCount(Vars.get().bar.getId()) >= Vars.get().item.getBarsRequired(), 1200);
            } else if (!Timing.waitCondition(() -> Banking.contains(Vars.get().bar.getId()), 1500)) {
                Logging.critical("Out of required bar. Ending script.");
                Vars.get().script.shutdown();
            }
        }
    }
}
