package scripts.woodcutter.runnables;

import org.tribot.api.General;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.api.entities.RSObjectEntity;
import scripts.woodcutter.data.Vars;

import java.util.Arrays;

public class CuttingRunnable implements Runnable {

    private long startCuttingTime = System.currentTimeMillis();
    private long totalWaitingTime = 0;
    private int totalWaitTimes = 0;
    private long waitingTime = 0;
    private boolean hover;

    @Override
    public String toString() {
        return "Cutting";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() != this) {
            startCuttingTime = System.currentTimeMillis();
            totalWaitingTime += waitingTime;
            totalWaitTimes++;
            long avg = totalWaitingTime / totalWaitTimes;
            ABCUtil.generateTrackers(avg, false);
            hover = ABCUtil.shouldHoverNext();
        }

        if (Vars.get().isTimedActions) {
            ABCUtil.performActions();
        }
        RSObject treeToHover = getHoverTree();
        if (treeToHover != null
            && hover
            && Mouse.isInBounds()
            && treeToHover.isOnScreen()
            && Inventory.getAll().length < 27) {
            RSModel model = treeToHover.getModel();
            if (model != null && !model.getEnclosedArea().contains(Mouse.getPos()) && Mouse.hover(treeToHover)) {
                General.sleep(60, 120);
            }
        }
        if (Inventory.getAll().length >= 27 && Timing.waitCondition(() -> !Player.isAnimating(), 1200)) {
            Timing.waitCondition(Inventory::isFull, 800);
        }
        waitingTime = System.currentTimeMillis() - startCuttingTime;
    }

    private RSObject getHoverTree() {
        RSTile[] positions = Vars.get().treeTileList.toArray(new RSTile[Vars.get().treeTileList.size()]);
        RSObjectEntity selector = Objects.find().nameEquals(Vars.get().tree.getObjectName());
        selector = Vars.get().location != null
            ? selector.inArea(Vars.get().location.getTreeArea())
            : selector.atPosition(positions);
        selector = selector.filter(tree -> !Arrays.asList(tree.getAllTiles()).contains(Player.getFacingTile()));
        RSObject hoverTree = selector.getFirst();
        return hoverTree != null ? hoverTree : Vars.get().anticipatedLocations.getAnticipatedTree();
    }
}
