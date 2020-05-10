package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class LedgeOne extends Obstacle {

    private static final RSArea LEDGE_ONE_AREA = new RSArea(
      new RSTile(3016, 3349, 3),
      new RSTile(3022, 3343, 3)
    );

    public LedgeOne() {
        super(11366, new RSTile(3015, 3345, 3), "Ledge", "Jump", LEDGE_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(LEDGE_ONE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
