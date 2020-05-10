package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Rope extends Obstacle {

    private static final RSArea ROPE_AREA = new RSArea(
      new RSTile(2472, 3421, 2),
      new RSTile(2477, 3418, 2)
    );

    public Rope() {
        super(23557, new RSTile(2478, 3420, 2), "Balancing rope", "Walk-on", ROPE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isAnimating() || Player.isMoving(), 2000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(ROPE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 10000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 335) {
            Camera.setCameraRotation(General.random(335, 359));
        }
        return true;
    }
}
