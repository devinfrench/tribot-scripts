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

public class NetOne extends Obstacle {

    private static final RSArea NET_ONE_AREA = new RSArea(
      new RSTile(2470, 3429, 0),
      new RSTile(2477, 3425, 0)
    );

    public NetOne() {
        super(23134, new RSTile(2473, 3425, 0), "Obstacle net", "Climb-over", NET_ONE_AREA);
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
              && !Player.isInArea(NET_ONE_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 6000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 145 || rotation > 225) {
            Camera.setCameraRotation(General.random(145, 225));
        }
        return true;
    }
}
