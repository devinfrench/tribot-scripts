package scripts.miner.runnables;

import org.tribot.api.General;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.miner.data.Vars;

import java.util.Arrays;
import java.util.Comparator;

public class MineRunnable implements Runnable {

    @Override
    public String toString() {
        return "Attempting to Mine";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof MiningRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
              ? General.randomSD(500, 150)
              : General.randomSD(120, 60);
            General.sleep(sleep);
        }
        Inventory.cancelSelectItem();
        RSObject rock = getRock();
        if (rock != null) {
            if (Interact.with(rock).walkTo().cameraTurn().click("Mine")) {
                Timing.waitCondition(this::isMining, Player.distanceTo(rock) * (Game.isRunOn() ? 600 : 900) + 900);
            }
        } else {
            rock = Vars.get().anticipatedLocations.getAnticipatedRock();
            if (rock != null) {
                if (Player.distanceTo(rock) > 2) {
                    RSTile closestTile = Objects.getClosestReachableTile(rock);
                    ABCUtil.performRunActivation();
                    if (closestTile.isOnScreen() && closestTile.isClickable() && Mouse.click("Walk here", closestTile)
                      || !closestTile.isOnScreen() && Walking.walkStraightPath(closestTile)) {
                        Timing.waitCondition(() -> Player.distanceTo(closestTile) <= 2 && !Player.isMoving(),
                          Player.distanceTo(closestTile) * 600 + 1200);
                    }
                } else {
                    RSModel model = rock.getModel();
                    if (model != null
                      && Mouse.isInBounds()
                      && rock.isOnScreen()
                      && !model.getEnclosedArea().contains(Mouse.getPos())
                      && Mouse.hover(rock)) {
                        General.sleep(100, 200);
                    }
                }
            }
        }
    }

    private RSObject getRock() {
        RSTile[] positions = Vars.get().rockTileList.toArray(new RSTile[Vars.get().rockTileList.size()]);
        RSObject[] rocks = Objects.find().nameEquals("Rocks").atPosition(positions).hasModifiedColors().getAll();
        Arrays.sort(rocks, Comparator.comparing((RSObject rock) -> !Player.getFacingTile().equals(rock.getPosition()))
          .thenComparingDouble(Player::distanceToDouble)
          .thenComparing(rock -> !Player.isAdjacent(rock)));
        if (Vars.get().nextRock != null && Arrays.asList(rocks).contains(Vars.get().nextRock)) {
            RSObject rock = Vars.get().nextRock;
            if (rocks.length > 1) {
                Vars.get().nextRock = rocks[0].equals(rock) ? rocks[1] : rocks[0];
            } else {
                Vars.get().nextRock = null;
            }
            return rock;
        }
        if (rocks.length > 1) {
            Vars.get().nextRock = rocks[1];
        }
        return rocks.length > 0 ? rocks[0] : null;
    }

    private boolean isMining() {
        return Player.isAnimating()
          && Objects.find().atPosition(Player.getFacingTile()).nameEquals("Rocks").hasModifiedColors().getAll().length > 0;
    }
}