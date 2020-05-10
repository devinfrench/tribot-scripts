package scripts.agility.courses.rellekka.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Game;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;

public class GapTwo extends Obstacle {

    private static final RSArea GAP_AREA = new RSArea(
        new RSTile(2626, 3655, 3),
        new RSTile(2630, 3651, 3)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(2639, 3653, 3);

    public GapTwo() {
        super(14990, new RSTile(2629, 3656, 3), "Gap", "Leap", GAP_AREA);
    }


    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 8000);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 205 || rotation > 230) {
            Camera.setCameraRotation(General.random(205, 230));
        }
        return true;
    }
}
