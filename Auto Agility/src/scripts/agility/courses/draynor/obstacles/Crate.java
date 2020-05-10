package scripts.agility.courses.draynor.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Crate extends Obstacle {

    private static final RSArea CRATE_AREA = new RSArea(
      new RSTile(3096, 3256, 3),
      new RSTile(3101, 3261, 3)
    );

    public Crate() {
        super(10086, new RSTile(3102, 3261, 3), "Crate", "Climb-down", CRATE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() || Player.isAnimating(), 1500)
          && Timing.waitCondition(Player::isAnimating, 4500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && Player.getPlane() == 0
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 55 || rotation > 80) {
            Camera.setCameraRotation(General.random(55, 80));
        }
        return true;
    }
}
