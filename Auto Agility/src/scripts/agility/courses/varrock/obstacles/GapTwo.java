package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class GapTwo extends Obstacle {

    private static final RSArea GAP_TWO_AREA = new RSArea(
      new RSTile(3192, 3406, 3),
      new RSTile(3198, 3402, 3)
    );

    public GapTwo() {
        super(10778, new RSTile(3193, 3401, 3), "Gap", "Leap", GAP_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 6000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_TWO_AREA)
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

    @Override
    public String toString() {
        return "Gap two";
    }
}
