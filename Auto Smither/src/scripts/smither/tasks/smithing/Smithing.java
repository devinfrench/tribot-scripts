package scripts.smither.tasks.smithing;

import org.tribot.api2007.Inventory;
import scripts.api.ABCUtil;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.data.Vars;

public class Smithing implements Task {

    private boolean isSmithing;

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Player.isAnimating() && Inventory.getCount(Vars.get().bar.getId()) >= Vars.get().item.getBarsRequired() || isSmithing;
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() != this) {
            ABCUtil.generateTrackers((Inventory.getCount(Vars.get().bar.getId()) / Vars.get().item.getBarsRequired()) * 2500, true);
        }
        int beforeCount = Inventory.getCount(Vars.get().bar.getId());

        if (Timing.waitCondition(() -> {
            ABCUtil.performActions();
            return !Player.isAnimating();
        }, 3100)) {
            Timing.waitCondition(() -> {
                ABCUtil.performActions();
                return Player.isAnimating()
                  || Inventory.getCount(Vars.get().bar.getId()) < Vars.get().item.getBarsRequired();
            }, 3100);
        }

        int afterCount = Inventory.getCount(Vars.get().bar.getId());
        isSmithing = afterCount < beforeCount && afterCount != 0;
    }

    @Override
    public String toString() {
        return "Smithing";
    }
}
