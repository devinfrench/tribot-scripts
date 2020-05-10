package scripts.woodcutter.decisions;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.woodcutter.data.Vars;

public class TreesDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return atTrees();
    }

    private boolean atTrees() {
        return Vars.get().location != null && Player.isInArea(Vars.get().location.getTreeArea())
            || Vars.get().customTreeArea != null && Player.isInArea(Vars.get().customTreeArea);
    }
}
