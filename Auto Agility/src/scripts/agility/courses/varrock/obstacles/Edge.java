package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Edge extends Obstacle {

    private static final RSArea EDGE_AREA = new RSArea(
      new RSTile(3236, 3415, 3),
      new RSTile(3240, 3410, 3)
    );

    public Edge() {
        super(10817, new RSTile(3236, 3416, 3), "Edge", "Jump-off", EDGE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 5000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(EDGE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
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
