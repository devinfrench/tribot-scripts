package scripts.agility.courses.falador.obstacles;

import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class HandHolds extends Obstacle {

    private static final RSArea HAND_HOLDS_AREA = new RSArea(
      new RSTile(3051, 3349, 3),
      new RSTile(3044, 3341, 3)
    );

    public HandHolds() {
        super(10836, new RSTile(3050, 3350, 3), "Hand holds", "Cross", HAND_HOLDS_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(HAND_HOLDS_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 10000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        return true;
    }
}
