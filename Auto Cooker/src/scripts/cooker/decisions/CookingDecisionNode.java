package scripts.cooker.decisions;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;

public class CookingDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Player.isAnimating();
    }

}
