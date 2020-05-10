package scripts.agility.courses.canifis.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class MeatGap extends Obstacle {

    private static final RSArea MEAT_SHOP_AREA = new RSArea(
      new RSTile(3503, 3489, 2),
      new RSTile(3512, 3499, 2)
    );

    public MeatGap() {
        super(10820, new RSTile(3505, 3498, 2), "Gap", "Jump", MEAT_SHOP_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() || Player.isAnimating(), 1500)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(MEAT_SHOP_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 8000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 34 || rotation > 46) {
            Camera.setCameraRotation(General.random(34, 46));
        }
        return true;
    }

    @Override
    public String toString() {
        return "Meat shop gap";
    }
}
