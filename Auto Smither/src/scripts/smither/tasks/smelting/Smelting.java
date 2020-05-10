package scripts.smither.tasks.smelting;

import scripts.api.ABCUtil;
import scripts.api.Inventory;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.data.Bar;
import scripts.smither.data.Vars;

public class Smelting implements Task {

    private boolean isSmelting;

    @Override
    public String toString() {
        return "Smelting";
    }

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Player.isAnimating() && Vars.get().bar.hasOreRequirements() || isSmelting;
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() != this) {
            ABCUtil.generateTrackers(Inventory.getCount(Vars.get().bar.getOreRequirements()[0][1]) * 2500, true);
        }
        int beforeCount = Inventory.getCount(Vars.get().bar.getOreRequirements()[0][1]);

        if (Vars.get().bar == Bar.CANNONBALL) {

            Timing.waitCondition(() -> {
                ABCUtil.performActions();
                return Inventory.getCount(Vars.get().bar.getOreRequirements()[0][1]) < beforeCount;
            }, 6200);

        } else {

            if (Timing.waitCondition(() -> {
                ABCUtil.performActions();
                return !Player.isAnimating();
            }, 3100)) {
                Timing.waitCondition(() -> {
                    ABCUtil.performActions();
                    return Player.isAnimating() || Inventory.getCount(Vars.get().bar.getOreRequirements()[0][1]) == 0;
                }, 3100);
            }

        }

        int afterCount = Inventory.getCount(Vars.get().bar.getOreRequirements()[0][1]);
        isSmelting = afterCount < beforeCount && afterCount != 0;
    }
}

