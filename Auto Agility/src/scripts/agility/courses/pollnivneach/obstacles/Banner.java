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

public class Banner extends Obstacle {

    private static final RSArea BANNER_AREA = new RSArea(
        new RSTile(3355, 2973, 1),
        new RSTile(3352, 2976, 1)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(3360, 2977, 1);

    public Banner() {
        super(14937, new RSTile(3356, 2978, 1), "Banner", "Grab", BANNER_AREA);
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
