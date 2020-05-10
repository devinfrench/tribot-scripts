package scripts.woodcutter.decisions;

import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.woodcutter.data.Vars;

import java.util.Arrays;

public class CutDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return !isCutting();
    }

    private boolean isCutting() {
        return Player.isAnimating()
            && Objects.find()
            .nameEquals(Vars.get().tree.getObjectName())
            .filter(tree -> Arrays.asList(tree.getAllTiles()).contains(Player.getFacingTile()))
            .getAll().length > 0;
    }
}
