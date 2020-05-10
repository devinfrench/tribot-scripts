package scripts.woodcutter.runnables;

import org.tribot.api.General;
import org.tribot.api.util.Sorting;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.api.entities.RSObjectEntity;
import scripts.woodcutter.data.Tree;
import scripts.woodcutter.data.Vars;

import java.util.Arrays;
import java.util.Comparator;

public class CutRunnable implements Runnable {

    @Override
    public String toString() {
        return "Attempting to Cut";
    }

    @Override
    public void run() {
        if (Vars.get().script.getLastTask() instanceof CuttingRunnable) {
            long sleep = Vars.get().isWaitingReactionTime
                ? Vars.get().tree.getReactionTime()
                : General.randomSD(120, 60);
            General.sleep(sleep);
        }
        Inventory.cancelSelectItem();
        RSObject tree = getTree();
        if (tree != null) {
            RSTile closestTile = Objects.getClosestReachableTile(tree);
            if ((!tree.isOnScreen() || !tree.isClickable()) && Player.getPosition().distanceTo(closestTile) > 6
                && Walking.walkStraightPath(closestTile)) {
                Timing.waitCondition(() -> Player.getPosition().distanceTo(closestTile) <= 2,
                    Player.distanceTo(closestTile) * 600 + 900);
            }
            if ((tree.isClickable() && tree.isOnScreen() || Player.getPosition().distanceTo(closestTile) <= 6)
                && Interact.with(tree).cameraTurn().click("Chop down")) {
                Timing.waitCondition(this::isCutting, (long) PathFinding.distanceTo(closestTile, false) * 600 + 900);
            }
        } else {
            RSObject anticipatedTree = Vars.get().anticipatedLocations.getAnticipatedTree();
            if (anticipatedTree != null) {
                RSTile closestTile = Objects.getClosestReachableTile(anticipatedTree);
                if (Player.distanceTo(closestTile) > 2) {
                    ABCUtil.performRunActivation();
                    if (closestTile.isOnScreen() && closestTile.isClickable() && Mouse.click("Walk here", closestTile)
                        || !closestTile.isOnScreen() && Walking.walkStraightPath(closestTile)) {
                        Timing.waitCondition(() -> Player.distanceTo(closestTile) <= 2 && !Player.isMoving(),
                            Player.distanceTo(closestTile) * 600 + 1200);
                    }
                } else {
                    RSModel model = anticipatedTree.getModel();
                    if (model != null
                        && Mouse.isInBounds()
                        && anticipatedTree.isOnScreen()
                        && !model.getEnclosedArea().contains(Mouse.getPos())
                        && Mouse.hover(anticipatedTree)) {
                        General.sleep(100, 200);
                    }
                }
            }
        }
    }

    private RSObject getTree() {
        RSTile[] positions = Vars.get().treeTileList.toArray(new RSTile[Vars.get().treeTileList.size()]);
        RSObjectEntity selector = Objects.find().nameEquals(Vars.get().tree.getObjectName());
        selector = Vars.get().location != null
            ? selector.inArea(Vars.get().location.getTreeArea())
            : selector.atPosition(positions);
        RSObject[] trees = selector.getAll();
        if (Vars.get().tree != Tree.TREE) {
            Arrays.sort(trees, Comparator.comparing((RSObject tree) -> !Arrays.asList(tree.getAllTiles()).contains(Player.getFacingTile()))
                .thenComparing(tree -> Player.distanceToDouble(Objects.getClosestReachableTile(tree))));
        } else {
            if (General.isLookingGlass()) {
                Sorting.sortByDistance(trees, Player.getPosition(), true);
            } else {
                Arrays.sort(trees, Comparator.comparing(tree -> PathFinding.distanceTo(Objects.getClosestReachableTile(tree), false)));
            }
        }
        return trees.length > 0 ? trees[0] : null;
    }

    private boolean isCutting() {
        return Player.isAnimating()
            && Objects.find()
            .nameEquals(Vars.get().tree.getObjectName())
            .filter(tree -> Arrays.asList(tree.getAllTiles()).contains(Player.getFacingTile()))
            .getAll().length > 0;
    }
}
