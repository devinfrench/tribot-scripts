package scripts.woodcutter.runnables;

import org.tribot.api.General;
import org.tribot.api2007.types.RSItem;
import scripts.api.Inventory;
import scripts.woodcutter.data.Vars;

import java.util.HashSet;
import java.util.Set;

public class DropRunnable implements Runnable {

    @Override
    public String toString() {
        return "Dropping";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof CuttingRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
                ? Vars.get().tree.getReactionTime()
                : General.randomSD(120, 60);
            General.sleep(sleep);
        }
        switch (Vars.get().option) {
            case SHIFT_DROP:
                Inventory.shiftDropAllExcept(getAxeIds());
                break;
            case MOUSE_KEYS_DROP:
                Inventory.mouseKeysDropAllExcept(getAxeIds());
                break;
            default:
                Inventory.dropAllExcept(getAxeIds());
                break;
        }
        if (!Inventory.isFull()) {
            General.sleep(General.randomSD(450, 100));
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
