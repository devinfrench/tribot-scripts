package scripts.agility.courses.seers.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class GapOne extends Obstacle {

    private static final RSArea GAP_AREA = new RSArea(
      new RSTile(2721, 3497, 3),
      new RSTile(2730, 3490, 3)
    );

    public GapOne() {
        super(11374, new RSTile(2720, 3492, 3), "Gap", "Jump", GAP_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() || Player.isAnimating(), 1500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_AREA)
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
