package scripts.agility.courses.canifis.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Game;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class TallTree extends Obstacle {

    public static final RSTile TALL_TREE_TILE = new RSTile(3507, 3487, 0);

    public TallTree() {
        super(10819, new RSTile(3505, 3489, 0), "Tall tree", "Climb", null);
    }

    @Override
    public boolean validate() {
        return Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        if (Player.isInCombat()) {
            Timing.waitCondition(() -> !Player.isAnimating(), 2400);
        }
        RSObject obj = getRSObject();
        if (Interact.with(obj).walkTo(true).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitValue(Game::getDestination, 1500) != null) {
                Timing.waitCondition(() -> performHumanActions()
                  && adjustCameraForNextObstacle()
                  && Player.getPlane() == 2
                  && !Player.isMoving()
                  && !Player.isAnimating(), 5000 + distance * 400);
            }
        } else if (Player.distanceTo(TALL_TREE_TILE) > 5) {
            Walking.walkStraightPath(TALL_TREE_TILE);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 34 || rotation > 46) {
            Camera.setCameraRotation(General.random(34, 46));
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tall tree";
    }

}
