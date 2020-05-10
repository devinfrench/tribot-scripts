package scripts.agility.courses.draynor.obstacles;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TightRopeTwo extends Obstacle {

    private static final RSArea TIGHT_ROPE_TWO_AREA = new RSArea(new RSTile[]{
      new RSTile(3091, 3277, 3),
      new RSTile(3093, 3275, 3),
      new RSTile(3090, 3272, 3),
      new RSTile(3087, 3274, 3),
      new RSTile(3089, 3276, 3)
    });

    public TightRopeTwo() {
        super(10075, new RSTile(3092, 3276, 3), "Tightrope", "Cross", TIGHT_ROPE_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(TIGHT_ROPE_TWO_AREA), 3500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(TIGHT_ROPE_TWO_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 12000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
