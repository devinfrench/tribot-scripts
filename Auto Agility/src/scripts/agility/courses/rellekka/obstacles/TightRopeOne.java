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

public class TightRopeOne extends Obstacle {

    private static final RSArea TIGHTROPE_AREA = new RSArea(
        new RSTile(2622, 3668, 3),
        new RSTile(2615, 3658, 3)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(2627, 3654, 3);

    public TightRopeOne() {
        super(14987, new RSTile(2623, 3658, 3), "Tightrope", "Cross", TIGHTROPE_AREA);
    }


    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 7000);
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
