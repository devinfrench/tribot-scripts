package scripts.miner.decisions;

import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;

public class MineDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return !isMining();
    }

    private boolean isMining() {
        return Player.isAnimating()
          && Objects.find().atPosition(Player.getFacingTile()).nameEquals("Rocks").hasModifiedColors().getAll().length > 0;
    }
}
