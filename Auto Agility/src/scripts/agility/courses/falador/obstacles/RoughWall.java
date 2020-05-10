package scripts.agility.courses.falador.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class RoughWall extends Obstacle {

    public static final RSTile ROUGH_WALL_TILE = new RSTile(3036, 3341, 0);

    public RoughWall() {
        super(10833, new RSTile(3036, 3341, 0), "Rough wall", "Climb", null);
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
            Walking.walkStraightPath(ROUGH_WALL_TILE);
            adjustCameraForNextObstacle();
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation > 50 || rotation < 20) {
            Camera.setCameraRotation(General.random(20, 50));
        }
        return true;
    }
}
