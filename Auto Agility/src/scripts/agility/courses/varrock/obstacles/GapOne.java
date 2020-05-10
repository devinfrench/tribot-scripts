package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class GapOne extends Obstacle {

    private static final RSArea GAP_ONE_AREA = new RSArea(
      new RSTile(3208, 3413, 3),
      new RSTile(3201, 3417, 3)
    );

    public GapOne() {
        super(10642, new RSTile(3200, 3416, 3), "Gap", "Leap", GAP_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo(6).cameraTurn(5).click(getAction())
          && (Timing.waitCondition(Player::isMoving, 2000) || Timing.waitCondition(Player::isAnimating, 6000))) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_ONE_AREA)
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

    @Override
    public String toString() {
        return "Gap one";
    }
}
