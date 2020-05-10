package scripts.agility.courses.pollnivneach.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Game;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class TreeTwo extends Obstacle {

    private static final RSArea TREE_TWO_AREA = new RSArea(
        new RSTile(3370, 2990, 2),
        new RSTile(3357, 2995, 2)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(3359, 3000, 2);

    public TreeTwo() {
        super(14944, new RSTile(3359, 2996, 2), "Tree", "Jump-on", TREE_TWO_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 5000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 345) {
            Camera.setCameraRotation(General.random(345, 359));
        }
        return true;
    }
}
