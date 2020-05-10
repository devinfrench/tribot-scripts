package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TightRopeThree extends Obstacle {

    private static final RSArea TIGHT_ROPE_THREE_AREA = new RSArea(
      new RSTile(3029, 3355, 3),
      new RSTile(3026, 3352, 3)
    );

    public TightRopeThree() {
        super(11364, new RSTile(3026, 3353, 3), "Tightrope", "Cross", TIGHT_ROPE_THREE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(TIGHT_ROPE_THREE_AREA), 5500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(TIGHT_ROPE_THREE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 12000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }

}
