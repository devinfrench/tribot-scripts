package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Ledge extends Obstacle {

    private static final RSArea LEDGE_AREA = new RSArea(
      new RSTile(3236, 3408, 3),
      new RSTile(3240, 3403, 3)
    );

    public Ledge() {
        super(10781, new RSTile(3236, 3409, 3), "Ledge", "Hurdle", LEDGE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 6000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(LEDGE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
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
