package scripts.agility.courses.seers.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TightRope extends Obstacle {

    private static final RSArea TIGHT_ROPE_AREA = new RSArea(new RSTile[]{
      new RSTile(2714, 3495, 2),
      new RSTile(2711, 3495, 2),
      new RSTile(2710, 3496, 2),
      new RSTile(2704, 3496, 2),
      new RSTile(2704, 3492, 2),
      new RSTile(2706, 3492, 2),
      new RSTile(2706, 3488, 2),
      new RSTile(2711, 3488, 2),
      new RSTile(2711, 3492, 2),
      new RSTile(2714, 3493, 2)
    });

    public TightRope() {
        super(11378, new RSTile(2710, 3489, 2), "Tightrope", "Cross", TIGHT_ROPE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(TIGHT_ROPE_AREA), 5500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(TIGHT_ROPE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 12000);
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
