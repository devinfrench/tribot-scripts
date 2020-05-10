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

public class DryingLine extends Obstacle {

    private static final RSArea DRYING_LINE_AREA = new RSArea(
        new RSTile(3365, 3000, 2),
        new RSTile(3356, 3004, 2)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(3363, 2998, 0);

    public DryingLine() {
        super(14945, new RSTile(3363, 3000, 2), "Drying line", "Jump-to", DRYING_LINE_AREA);
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
        if (rotation < 345) {
            Camera.setCameraRotation(General.random(345, 359));
        }
        return true;
    }
}
