package scripts.agility.courses.draynor.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class NarrowWall extends Obstacle {

    private static final RSArea NARROW_WALL_AREA = new RSArea(
      new RSTile(3089, 3265, 3),
      new RSTile(3094, 3268, 3)
    );

    public NarrowWall() {
        super(10077, new RSTile(3089, 3264, 3), "Narrow wall", "Balance", NARROW_WALL_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(() -> Player.isMoving() && !Player.isInArea(NARROW_WALL_AREA), 4200)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && !Player.isInArea(NARROW_WALL_AREA)
              && !Player.isMoving()
              && !Player.isAnimating(), 8000);
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
