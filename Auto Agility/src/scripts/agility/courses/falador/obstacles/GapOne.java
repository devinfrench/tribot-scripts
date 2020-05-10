package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class GapOne extends Obstacle {

    private static final RSArea GAP_ONE_AREA = new RSArea(
      new RSTile(3050, 3357, 3),
      new RSTile(3048, 3358, 3)
    );

    public GapOne() {
        super(11161, new RSTile(3048, 3359, 3), "Gap", "Jump", GAP_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_ONE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
