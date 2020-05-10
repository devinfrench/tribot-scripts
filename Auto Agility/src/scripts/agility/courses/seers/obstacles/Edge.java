package scripts.agility.courses.seers.obstacles;

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
      new RSTile(2698, 3465, 2),
      new RSTile(2702, 3460, 2)
    );

    public Edge() {
        super(11377, new RSTile(2703, 3461, 2), "Edge", "Jump", EDGE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() || Player.isAnimating(), 1500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(EDGE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 8000);
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
