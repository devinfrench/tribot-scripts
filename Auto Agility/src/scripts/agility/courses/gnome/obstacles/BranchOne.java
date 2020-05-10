package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class BranchOne extends Obstacle {

    private static final RSArea BRANCH_ONE_AREA = new RSArea(
      new RSTile(2471, 3424, 1),
      new RSTile(2476, 3422, 1)
    );

    public BranchOne() {
        super(23559, new RSTile(2473, 3422, 1), "Tree branch", "Climb", BRANCH_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 3000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && Player.getPlane() != 1
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
