package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class TightRopeOne extends Obstacle {

    private static final RSArea TIGHT_ROPE_ONE_AREA = new RSArea(
      new RSTile(3036, 3343, 3),
      new RSTile(3040, 3342, 3)
    );

    public TightRopeOne() {
        super(10834, new RSTile(3040, 3343, 3), "Tightrope", "Cross", TIGHT_ROPE_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(TIGHT_ROPE_ONE_AREA), 5500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(TIGHT_ROPE_ONE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 12000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }

}
