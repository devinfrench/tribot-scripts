package scripts.cooker.runnables.alkharid;

import org.tribot.api2007.types.RSObject;
import scripts.api.*;
import scripts.api.Banking.Bank;

import static scripts.cooker.data.Constants.*;

public class WalkBankRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Bank";
    }

    @Override
    public void run() {
        if (PathFinding.canReach(AL_KHARID_OPEN_DOOR_TILE)) {
            walkToBank();
        } else {
            openDoor();
        }
    }

    private void openDoor() {
        RSObject door = Objects.find().actionEquals("Open").atPosition(AL_KHARID_CLOSED_DOOR_TILE).getFirst();
        Interact.with(door).cameraTurn().click("Open");
    }

    private void walkToBank() {
        ABCUtil.performRunActivation();
        if (Walking.walkStraightPath(AL_KHARID_BANK_TILE)) {
            Timing.waitCondition(() -> Bank.AL_KHARID.contains(Player.getPosition()), 1200);
        }
    }

}
