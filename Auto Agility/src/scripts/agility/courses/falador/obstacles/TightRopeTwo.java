package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TightRopeTwo extends Obstacle {

    private static final RSArea TIGHT_ROPE_TWO_AREA = new RSArea(
      new RSTile(3041, 3364, 3),
      new RSTile(3034, 3361, 3)
    );

    public TightRopeTwo() {
        super(11361, new RSTile(3034, 3361, 3), "Tightrope", "Cross", TIGHT_ROPE_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(TIGHT_ROPE_TWO_AREA), 5500)) {
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
