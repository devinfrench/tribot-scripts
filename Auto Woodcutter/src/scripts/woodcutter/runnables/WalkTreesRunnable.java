package scripts.woodcutter.runnables;

import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.ABCUtil;
import scripts.api.Interact;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.woodcutter.data.Location;
import scripts.woodcutter.data.Vars;

import static scripts.woodcutter.data.Constants.EDGEVILLE_CLOSED_DOOR_TILE;

public class WalkTreesRunnable implements Runnable {

    @Override
    public String toString() {
        return "Walking to Trees";
    }

    @Override
    public void run() {
        ABCUtil.performRunActivation();
        if (Vars.get().location == Location.EDGEVILLE_YEW && Walking.canWalkPath(Vars.get().location.getPath())) {
            walkToEdgevilleYews();
        } else if (Vars.get().location != null && Vars.get().location.getPath() != null) {
            walkToTrees();
        } else if (WebWalking.walkTo(Vars.get().location != null
            ? Vars.get().location.getTreeArea().getClosestTile()
            : Vars.get().customTreeArea.getClosestTile())) {
            Timing.waitCondition(this::atTrees, 2000);
        }
    }

    private boolean atTrees() {
        return Vars.get().location != null && Player.isInArea(Vars.get().location.getTreeArea())
            || Vars.get().customTreeArea != null && Player.isInArea(Vars.get().customTreeArea);
    }

    private void walkToTrees() {
        if (Walking.canWalkPath(Vars.get().location.getPath())
            && Walking.walkPath(Walking.randomizePath(Walking.invertPath(Vars.get().location.getPath())))
            || !Walking.canWalkPath(Vars.get().location.getPath())
            && WebWalking.walkTo(Vars.get().location.getPath()[0])) {
            Timing.waitCondition(() -> Player.isInArea(Vars.get().location.getTreeArea()), 2000);
        }
    }

    private void walkToEdgevilleYews() {
        RSObject door = Objects.find().nameEquals("Door").atPosition(EDGEVILLE_CLOSED_DOOR_TILE).getFirst();
        RSTile playerPos = Player.getPosition();
        if (door != null
            && playerPos.getX() > EDGEVILLE_CLOSED_DOOR_TILE.getX()) {
            if (Interact.with(door).walkTo().cameraTurn().click("Open")) {
                Timing.waitCondition(() -> !Objects.find().nameEquals("Door").atPosition(EDGEVILLE_CLOSED_DOOR_TILE).exists(),
                    playerPos.distanceTo(door) * 600 + 1200);
            }
        } else if (door == null && playerPos.distanceTo(EDGEVILLE_CLOSED_DOOR_TILE) > 12) {
            if (Walking.walkStraightPath(EDGEVILLE_CLOSED_DOOR_TILE.translate(1, 0))) {
                Timing.waitCondition(() -> Player.getPosition().distanceTo(EDGEVILLE_CLOSED_DOOR_TILE) < 3, 1200);
            }
        } else {
            walkToTrees();
        }
    }
}
