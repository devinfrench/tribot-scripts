package scripts.cooker.decisions.wine;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;

public class MixingDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Player.isAnimating();
    }

}
