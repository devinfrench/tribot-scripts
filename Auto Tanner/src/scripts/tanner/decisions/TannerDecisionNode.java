package scripts.tanner.decisions;

import static scripts.tanner.data.Constants.TANNER_AREA;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;

public class TannerDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return TANNER_AREA.contains(Player.getPosition());
    }

}
