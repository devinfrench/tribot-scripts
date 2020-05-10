package scripts.cooker.runnables.alkharid;

import org.tribot.api2007.types.RSObject;
import scripts.api.*;

import static scripts.cooker.data.Constants.*;

public class WalkRangeRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Range";
    }

    @Override
    public void run() {
        if (PathFinding.canReach(AL_KHARID_RANGE_TILE)) {
            walkToRange();
        } else if (Player.distanceTo(AL_KHARID_OPEN_DOOR_TILE) > 6) {
            walkToDoor();
        } else {
            openDoor();
        }
    }

    private void openDoor() {
        RSObject door = Objects.find().actionEquals("Open").atPosition(AL_KHARID_CLOSED_DOOR_TILE).getFirst();
        Interact.with(door).cameraTurn().click("Open");
    }

    private void walkToRange() {
        ABCUtil.performRunActivation();
        if (Walking.walkStraightPath(AL_KHARID_RANGE_TILE)) {
            Timing.waitCondition(() -> AL_KHARID_RANGE_AREA.contains(Player.getPosition()), 1200);
        }
    }

    private void walkToDoor() {
        ABCUtil.performRunActivation();
        Walking.walkStraightPath(AL_KHARID_OPEN_DOOR_TILE);
    }

}
