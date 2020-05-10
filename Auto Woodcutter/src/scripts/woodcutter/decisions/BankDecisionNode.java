package scripts.woodcutter.decisions;

import scripts.api.Banking;
import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.woodcutter.data.Vars;

public class BankDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return Vars.get().location.getBank().contains(Player.getPosition())
            || Vars.get().location == null && Banking.isInBank();
    }
}
