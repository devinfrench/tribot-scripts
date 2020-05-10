package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class LedgeTwo extends Obstacle {

    private static final RSArea LEDGE_TWO_AREA = new RSArea(
      new RSTile(3014, 3346, 3),
      new RSTile(3011, 3344, 3)
    );

    public LedgeTwo() {
        super(11367, new RSTile(3011, 3343, 3), "Ledge", "Jump", LEDGE_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(LEDGE_TWO_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
