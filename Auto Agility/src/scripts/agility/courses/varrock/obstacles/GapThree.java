package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class GapThree extends Obstacle {

    private static final RSArea GAP_THREE_AREA = new RSArea(new RSTile[]{
      new RSTile(3182, 3382, 3),
      new RSTile(3182, 3399, 3),
      new RSTile(3201, 3399, 3),
      new RSTile(3202, 3404, 3),
      new RSTile(3208, 3404, 3),
      new RSTile(3209, 3395, 3),
      new RSTile(3201, 3394, 3),
      new RSTile(3195, 3387, 3),
      new RSTile(3189, 3381, 3)
    });
    private static final RSTile GAP_TILE = new RSTile(3208, 3396, 3);

    public GapThree() {
        super(10779, new RSTile(3209, 3397, 3), "Gap", "Leap", GAP_THREE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 6000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_THREE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 8000);
        } else if (Player.distanceTo(GAP_TILE) > 5) {
            Walking.walkStraightPath(GAP_TILE);
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

    @Override
    public String toString() {
        return "Gap three";
    }
}
