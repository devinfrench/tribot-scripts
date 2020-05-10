package scripts.tanner.runnables;

import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;

import static scripts.tanner.data.Constants.*;

public class WalkTannerRunnable implements Runnable {

    private static final RSTile TANNER_TILE = new RSTile(3276, 3191, 0);

    @Override
    public String toString() {
        return "Walking to Tanner";
    }

    @Override
    public void run() {
        if (PathFinding.canReach(TANNER_TILE)) {
            walkToTanner();
        } else if (Player.distanceTo(CLOSED_DOOR_TILE) > 6) {
            walkToDoor();
        } else if (openDoor()) {
            tradeTanner();
        }
    }

    private void walkToTanner() {
        ABCUtil.performRunActivation();
        RSTile[] path = Walking.randomizePath(PATH_TO_TANNER);
        if (Walking.walkPath(path)) {
            Timing.waitCondition(() -> TANNER_AREA.contains(Player.getPosition()), 1200);
        }
    }

    private void walkToDoor() {
        ABCUtil.performRunActivation();
        RSTile[] path = Walking.randomizePath(PATH_TO_DOOR);
        Walking.walkPath(path);
    }

    private boolean openDoor() {
        RSObject door = Objects.find().actionEquals("Open").atPosition(CLOSED_DOOR_TILE).getFirst();
        return Interact.with(door).cameraTurn().click("Open")
            && Timing.waitCondition(() -> PathFinding.canReach(TANNER_TILE), 1200);
    }

    private void tradeTanner() {
        RSNPC ellis = NPCs.find().nameEquals("Ellis").getFirst();
        if (Interact.with(ellis).walkTo().cameraTurn().click("Trade")
            && Timing.waitValue(Player::getInteractingCharacter, 1200) != null) {
            Timing.waitCondition(() -> Interfaces.isValid(TANNING_INTERFACE), 2100);
        }
    }

}
