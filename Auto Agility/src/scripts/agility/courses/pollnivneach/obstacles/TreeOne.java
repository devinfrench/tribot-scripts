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

public class TreeOne extends Obstacle {

    private static final RSArea TREE_ONE_AREA = new RSArea(
        new RSTile(3366, 2976, 1),
        new RSTile(3370, 2974, 1)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(3368, 2982, 1);

    public TreeOne() {
        super(14939, new RSTile(3367, 2977, 1), "Tree", "Jump-to", TREE_ONE_AREA);
    }

    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 6000);
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
