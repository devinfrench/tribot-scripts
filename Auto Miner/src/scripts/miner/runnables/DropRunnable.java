package scripts.miner.runnables;

import org.tribot.api.General;
import org.tribot.api2007.types.RSItem;
import scripts.api.Inventory;
import scripts.miner.data.Vars;

import java.util.HashSet;
import java.util.Set;

public class DropRunnable implements Runnable {

    @Override
    public String toString() {
        return "Dropping";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof MiningRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
              ? General.randomSD(500, 150)
              : General.randomSD(150, 50);
            General.sleep(sleep);
        }
        switch (Vars.get().option) {
            case SHIFT_DROP:
                Inventory.shiftDropAllExcept(getKeptItemIds());
                break;
            case MOUSE_KEYS_DROP:
                Inventory.mouseKeysDropAllExcept(getKeptItemIds());
                break;
            default:
                Inventory.dropAllExcept(getKeptItemIds());
                break;
        }
        if (!Inventory.isFull()) {
            General.sleep(General.randomSD(450, 100));
        }
    }

    private int[] getKeptItemIds() {
        Set<Integer> itemIds = new HashSet<>();
        RSItem[] items = Inventory.find().nameContains("pickaxe", "minerals").getAll();
        for (RSItem item : items) {
            itemIds.add(item.getID());
        }
        return itemIds.stream().mapToInt(Number::intValue).toArray();
    }
}
