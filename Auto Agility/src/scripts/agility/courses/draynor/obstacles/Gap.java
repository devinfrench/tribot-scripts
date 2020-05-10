package scripts.agility.courses.draynor.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Gap extends Obstacle {

    private static final RSArea GAP_AREA = new RSArea(
      new RSTile(3087, 3255, 3),
      new RSTile(3094, 3253, 3)
    );

    public Gap() {
        super(10085, new RSTile(3095, 3255, 3), "Gap", "Jump", GAP_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(GAP_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 180 || rotation > 210) {
            Camera.setCameraRotation(General.random(180, 210));
        }
        return true;
    }
}
