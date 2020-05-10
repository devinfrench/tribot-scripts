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

public class GapThree extends Obstacle {

    private static final RSArea GAP_AREA = new RSArea(
        new RSTile(2639, 3653, 3),
        new RSTile(2644, 3649, 3)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(2643, 3657, 3);

    public GapThree() {
        super(14991, new RSTile(2643, 3654, 3), "Gap", "Hurdle", GAP_AREA);
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
        if (rotation < 205 || rotation > 230) {
            Camera.setCameraRotation(General.random(205, 230));
        }
        return true;
    }
}
