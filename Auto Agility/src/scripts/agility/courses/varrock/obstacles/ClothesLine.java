package scripts.agility.courses.varrock.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class ClothesLine extends Obstacle {

    private static final RSArea CLOTHES_LINE_AREA = new RSArea(
      new RSTile(3214, 3410, 3),
      new RSTile(3219, 3419, 3)
    );

    public ClothesLine() {
        super(10587, new RSTile(3213, 3414, 3), "Clothes line", "Cross", CLOTHES_LINE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && (Timing.waitCondition(Player::isMoving, 2000) || Timing.waitCondition(Player::isAnimating, 9000))) {
            if (Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(CLOTHES_LINE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 10000)) {
                General.sleep(1000, 1250);
            }
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 55 || rotation > 105) {
            Camera.setCameraRotation(General.random(55, 105));
        }
        return true;
    }
}
