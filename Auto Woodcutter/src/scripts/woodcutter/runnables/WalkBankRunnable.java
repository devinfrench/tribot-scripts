package scripts.woodcutter.runnables;

import org.tribot.api.General;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.ABCUtil;
import scripts.api.Banking;
import scripts.api.Interact;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.woodcutter.data.Location;
import scripts.woodcutter.data.Vars;

import static scripts.woodcutter.data.Constants.EDGEVILLE_CLOSED_DOOR_TILE;

public class WalkBankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Bank";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof CuttingRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
                ? Vars.get().tree.getReactionTime()
                : General.randomSD(120, 60);
            General.sleep(sleep);
        }
        ABCUtil.performRunActivation();
        if (Vars.get().location == Location.EDGEVILLE_YEW && Walking.canWalkPath(Vars.get().location.getPath())) {
            walkToEdgevilleBank();
        } else if (Vars.get().location != null && Vars.get().location.getPath() != null) {
            walkToBank();
        } else if (WebWalking.walkTo(getClosestBank().getArea().getCenterTile())) {
            Timing.waitCondition(Banking::isInBank, 2000);
        }
    }

    private void walkToBank() {
        if (Walking.canWalkPath(Vars.get().location.getPath())
            && Walking.walkPath(Walking.randomizePath(Vars.get().location.getPath()))
            || !Walking.canWalkPath(Vars.get().location.getPath())
            && WebWalking.walkTo(Vars.get().location.getPath()[Vars.get().location.getPath().length - 1])) {
            Timing.waitCondition(() -> Vars.get().location.getBank().containsPlayer(), 2000);
        }
    }

    private void walkToEdgevilleBank() {
        RSObject door = Objects.find().nameEquals("Door").atPosition(EDGEVILLE_CLOSED_DOOR_TILE).getFirst();
        RSTile playerPos = Player.getPosition();
        if (door != null && playerPos.getX() <= EDGEVILLE_CLOSED_DOOR_TILE.getX()) {
            if (Interact.with(door).walkTo().cameraTurn().click("Open")) {
                Timing.waitCondition(() -> !Objects.find().nameEquals("Door").atPosition(EDGEVILLE_CLOSED_DOOR_TILE).exists(),
                    playerPos.distanceTo(door) * 600 + 1200);
            }
        } else {
            walkToBank();
        }
    }

    public static Banking.Bank getClosestBank() {
        RSTile playerPos = Player.getPosition();
        Banking.Bank closestBank = Banking.Bank.GRAND_EXCHANGE;
        int closestDistance = Integer.MAX_VALUE;
        for (Banking.Bank bank : Banking.Bank.values()) {
            int distance = playerPos.distanceTo(bank.getArea().getCenterTile());
            if (distance < closestDistance) {
                closestBank = bank;
                closestDistance = distance;
            }
        }
        return closestBank;
    }
}
