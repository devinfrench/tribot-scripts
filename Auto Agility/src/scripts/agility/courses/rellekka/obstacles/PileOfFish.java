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

public class PileOfFish extends Obstacle {

    private static final RSArea PILE_OF_FISH_AREA = new RSArea(
new RSTile(2654, 3664, 3),
new RSTile(2666, 3685, 3)
);
    private static final RSTile SUCCESS_TILE = new RSTile(2652, 3676, 0);

    public PileOfFish() {
        super(14994, new RSTile(2654, 3676, 3), "Pile of fish", "Jump-in", PILE_OF_FISH_AREA);
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
