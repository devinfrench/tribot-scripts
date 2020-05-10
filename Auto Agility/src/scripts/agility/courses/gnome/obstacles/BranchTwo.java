package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class BranchTwo extends Obstacle {

    private static final RSArea BRANCH_TWO_AREA = new RSArea(
      new RSTile(2483, 3421, 2),
      new RSTile(2488, 3418, 2)
    );

    public BranchTwo() {
        super(23560, new RSTile(2486, 3419, 2), "Tree branch", "Climb-down", BRANCH_TWO_AREA);
    }

    @Override
    public boolean validate() {
        return super.validate() && Player.getPlane() != 0;
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).cameraTurn().click(getAction())
          && Timing.waitCondition(Player::isAnimating, 3000)) {
            Timing.waitCondition(() -> performHumanActions()
              && adjustCameraForNextObstacle()
              && Player.getPlane() == 0
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
