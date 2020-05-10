package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class Tunnel extends Obstacle {

    private static final RSArea TUNNEL_AREA = new RSArea(
      new RSTile(2490, 3427, 0),
      new RSTile(2481, 3431, 0)
    );

    public Tunnel() {
        super(23138, new RSTile(2484, 3431, 0), "Obstacle pipe", "Squeeze-through", TUNNEL_AREA);
    }

    @Override
    public boolean validate() {
        return Player.isInArea(getArea()) && Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 4000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && Player.getPosition().getY() >= 3436
              && !Player.isMoving()
              && !Player.isAnimating(), 12000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 38 || rotation > 55) {
            Camera.setCameraRotation(General.random(38, 55));
        }
        return true;
    }
}
