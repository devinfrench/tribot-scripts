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

public class TightRopeTwo extends Obstacle {

    private static final RSArea TIGHTROPE_AREA = new RSArea(
        new RSTile(2643, 3657, 3),
        new RSTile(2650, 3663, 3)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(2655, 3670, 3);

    public TightRopeTwo() {
        super(14992, new RSTile(2647, 3663, 3), "Tightrope", "Cross", TIGHTROPE_AREA);
    }


    @Override
    public void traverse() {
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 11000);
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
