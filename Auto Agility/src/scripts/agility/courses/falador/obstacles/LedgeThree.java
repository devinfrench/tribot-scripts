package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class LedgeThree extends Obstacle {

    private static final RSArea LEDGE_THREE_AREA = new RSArea(
      new RSTile(3009, 3335, 3),
      new RSTile(3013, 3342, 3)
    );

    public LedgeThree() {
        super(11368, new RSTile(3012, 3334, 3), "Ledge", "Jump", LEDGE_THREE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(LEDGE_THREE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
