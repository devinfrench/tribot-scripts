package scripts.tanner.runnables;

import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;

import static scripts.tanner.data.Constants.*;

public class WalkBankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Bank";
    }

    @Override
    public void run() {
        if (TANNER_AREA.contains(Player.getPosition()) && !PathFinding.canReach(OPEN_DOOR_TILE)) {
            openDoor();
        } else {
            walkToBank();
        }
    }

    private void openDoor() {
        RSObject door = Objects.find().actionEquals("Open").atPosition(CLOSED_DOOR_TILE).getFirst();
        Interact.with(door).cameraTurn().click("Open");
    }

    private void walkToBank() {
        ABCUtil.performRunActivation();
        RSTile[] path = Walking.randomizePath(Walking.invertPath(PATH_TO_TANNER));
        Walking.walkPath(path);
    }

}
