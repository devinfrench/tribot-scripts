package scripts.agility.courses.draynor.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Wall extends Obstacle {

    private static final RSArea WALL_AREA = new RSArea(
      new RSTile(3088, 3261, 3),
      new RSTile(3087, 3257, 3)
    );

    public Wall() {
        super(10084, new RSTile(3088, 3256, 3), "Wall", "Jump-up", WALL_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 3000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(WALL_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 180 || rotation > 210) {
            Camera.setCameraRotation(General.random(180, 210));
        }
        return true;
    }
}
