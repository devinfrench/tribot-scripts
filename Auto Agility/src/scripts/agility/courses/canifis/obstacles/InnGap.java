package scripts.agility.courses.canifis.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class InnGap extends Obstacle {

    private static final RSArea INN_AREA = new RSArea(
      new RSTile(3486, 3467, 3),
      new RSTile(3505, 3479, 3)
    );

    public InnGap() {
        super(10823, new RSTile(3503, 3476, 3), "Gap", "Jump", INN_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, Player.distanceTo(getPosition()) * 600 + 1200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(INN_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
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
        return "Inn gap";
    }
}
