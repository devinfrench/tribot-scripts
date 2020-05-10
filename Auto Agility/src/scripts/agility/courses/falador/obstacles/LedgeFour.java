package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class LedgeFour extends Obstacle {

    private static final RSArea LEDGE_FOUR_AREA = new RSArea(
      new RSTile(3012, 3330, 3),
      new RSTile(3017, 3334, 3)
    );

    public LedgeFour() {
        super(11370, new RSTile(3018, 3332, 3), "Ledge", "Jump", LEDGE_FOUR_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(LEDGE_FOUR_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
