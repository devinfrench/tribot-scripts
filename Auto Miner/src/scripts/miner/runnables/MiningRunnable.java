package scripts.miner.runnables;

import org.tribot.api.General;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.miner.data.Vars;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class MiningRunnable implements Runnable {

    private long startMiningTime = System.currentTimeMillis();
    private long totalWaitingTime = 0;
    private int totalWaitTimes = 0;
    private long waitingTime = 0;
    private boolean hover;

    @Override
    public String toString() {
        return "Mining";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() != this) {
            startMiningTime = System.currentTimeMillis();
            totalWaitingTime += waitingTime;
            totalWaitTimes++;
            long avg = totalWaitingTime / totalWaitTimes;
            ABCUtil.generateTrackers(avg, false);
            hover = ABCUtil.shouldHoverNext();
        }

        if (Vars.get().isTimedActions) {
            ABCUtil.performActions();
        }
        RSObject rockToHover = getHoverRock();
        if (rockToHover != null
          && hover
          && Mouse.isInBounds()
          && rockToHover.isOnScreen()
          && Inventory.getAll().length < 27) {
            RSModel model = rockToHover.getModel();
            if (model != null && !model.getEnclosedArea().contains(Mouse.getPos()) && Mouse.hover(rockToHover)) {
                General.sleep(60, 120);
            }
        }
        if (Inventory.getAll().length >= 27 && Timing.waitCondition(() -> !Player.isAnimating(), 1200)) {
            Timing.waitCondition(Inventory::isFull, 800);
        }
        waitingTime = System.currentTimeMillis() - startMiningTime;
    }

    private RSObject getHoverRock() {
        RSTile[] positions = Vars.get().rockTileList.toArray(new RSTile[Vars.get().rockTileList.size()]);
        RSObject[] rocks = Objects.find().nameEquals("Rocks").atPosition(positions).hasModifiedColors().getAll();
        if (Vars.get().nextRock != null && Arrays.asList(rocks).contains(Vars.get().nextRock)) {
            return Vars.get().nextRock;
        }
        Optional<RSObject> hoverRock = Arrays.stream(rocks)
          .sorted(Comparator.comparing((RSObject rock) -> !Player.getFacingTile().equals(rock.getPosition()))
            .thenComparingDouble(Player::distanceToDouble)
            .thenComparing(rock -> !Player.isAdjacent(rock)))
          .filter(rock -> !Player.getFacingTile().equals(rock.getPosition())).findFirst();
        if (hoverRock.isPresent() && !hoverRock.get().equals(Vars.get().nextRock)) {
            Vars.get().nextRock = hoverRock.get();
        }
        return hoverRock.orElse(Vars.get().anticipatedLocations.getAnticipatedRock());
    }
}
