package scripts.agility.courses.seers.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class Wall extends Obstacle {

    public static final RSTile WALL_TILE = new RSTile(2729, 3489, 0);
    private static final RSTile[] WALL_PATH = {
      new RSTile(2704, 3464, 0),
      new RSTile(2718, 3468, 0),
      new RSTile(2726, 3476, 0),
      new RSTile(2728, 3483, 0),
      new RSTile(2729, 3489, 0)
    };

    public Wall() {
        super(11373, new RSTile(2729, 3489, 0), "Wall", "Climb-up", null);
    }

    @Override
    public boolean validate() {
        return Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        RSObject obj = getRSObject();
        if (Interact.with(obj).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitCondition(Player::isAnimating, 2000 + distance * 300)) {
                Timing.waitCondition(() -> performHumanActions()
                  && adjustCameraForNextObstacle()
                  && Player.getPlane() == 3
                  && !Player.isMoving()
                  && !Player.isAnimating(), 6000);
            }
        } else if (Player.distanceTo(WALL_TILE) > 6) {
            // TODO add teleport
            if (Walking.canWalkPath(WALL_PATH)) {
                Walking.walkPath(WALL_PATH);
            } else {
                Walking.walkStraightPath(WALL_TILE);
            }
            Timing.waitCondition(() -> adjustCameraForNextObstacle() && !Player.isMoving(), 1500);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 125 || rotation > 135) {
            Camera.setCameraRotation(General.random(125, 135));
        }
        return true;
    }
}
