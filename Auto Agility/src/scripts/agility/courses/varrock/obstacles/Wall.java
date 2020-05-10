package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Wall extends Obstacle {

    public static final RSArea WALL_AREA = new RSArea(
      new RSTile(3197, 3416, 1),
      new RSTile(3193, 3416, 1)
    );

    public Wall() {
        super(10777, new RSTile(3191, 3415, 1), "Wall", "Balance", WALL_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn(8).click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4000)) {
            if (Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(WALL_AREA)
              && !Player.isMoving()
              && !Player.isAnimating()
              && Player.getPlane() == 3
              || Player.getPlane() == 0, 12000)) {
                General.sleep(1000, 1250);
            }
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 250 || rotation > 290) {
            Camera.setCameraRotation(General.random(250, 290));
        }
        return true;
    }
}
