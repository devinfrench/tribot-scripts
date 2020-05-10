package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class GapTwo extends Obstacle {

    private static final RSArea GAP_TWO_AREA = new RSArea(
      new RSTile(3048, 3361, 3),
      new RSTile(3045, 3367, 3)
    );

    public GapTwo() {
        super(11360, new RSTile(3044, 3361, 3), "Gap", "Jump", GAP_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_TWO_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
