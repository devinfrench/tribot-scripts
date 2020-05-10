package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class RoughWall extends Obstacle {

    public static final RSTile ROUGH_WALL_TILE = new RSTile(3222, 3414, 0);
    private static final RSArea ROUGH_WALL_AREA = new RSArea(
      new RSTile(3221, 3413, 0),
      new RSTile(3223, 3415, 0)
    );
    private static final RSTile[] ROUGH_WALL_PATH = {
      new RSTile(3197, 3409, 0),
      new RSTile(3208, 3409, 0),
      new RSTile(3219, 3409, 0),
      new RSTile(3222, 3414, 0)
    };

    public RoughWall() {
        super(10586, new RSTile(3221, 3414, 0), "Rough wall", "Climb", null);
    }

    @Override
    public boolean validate() {
        return Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        RSObject obj = getRSObject();
        if (Interact.with(obj).walkTo(true).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitCondition(Player::isAnimating, 2000 + distance * 300)) {
                Timing.waitCondition(() -> performHumanActions()
                  && adjustCameraForNextObstacle()
                  && Player.getPlane() == 3
                  && !Player.isMoving()
                  && !Player.isAnimating(), 6000);
            }
        } else if (Player.distanceTo(ROUGH_WALL_TILE) > 5) {
            if (Walking.canWalkPath(ROUGH_WALL_PATH)) {
                Walking.walkPath(ROUGH_WALL_PATH);
            } else {
                Walking.walkStraightPath(ROUGH_WALL_TILE);
            }
            adjustCameraForNextObstacle();
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 55 || rotation > 105) {
            Camera.setCameraRotation(General.random(55, 105));
        }
        return true;
    }
}
