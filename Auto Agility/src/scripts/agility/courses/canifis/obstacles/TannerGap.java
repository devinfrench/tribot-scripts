package scripts.agility.courses.canifis.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TannerGap extends Obstacle {

    private static final RSArea TANNER_AREA = new RSArea(
      new RSTile(3493, 3505, 2),
      new RSTile(3485, 3498, 2)
    );

    public TannerGap() {
        super(10828, new RSTile(3485, 3499, 2), "Gap", "Jump", TANNER_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() || Player.isAnimating(), 1500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(TANNER_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 8000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 34 || rotation > 46) {
            Camera.setCameraRotation(General.random(34, 46));
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tanner gap";
    }
}
