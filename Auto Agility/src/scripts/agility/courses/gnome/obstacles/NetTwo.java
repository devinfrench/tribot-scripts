package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Entities;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.entities.RSObjectEntity;

public class NetTwo extends Obstacle {

    private static final RSArea NET_TWO_AREA = new RSArea(
      new RSTile(2490, 3418, 0),
      new RSTile(2481, 3426, 0)
    );

    public NetTwo() {
        super(23135, new RSTile(2485, 3426, 0), "Obstacle net", "Climb-over", NET_TWO_AREA);
    }

    @Override
    public boolean validate() {
        return Player.isInArea(getArea()) && Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isAnimating() || Player.isMoving(), 2000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(NET_TWO_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
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
