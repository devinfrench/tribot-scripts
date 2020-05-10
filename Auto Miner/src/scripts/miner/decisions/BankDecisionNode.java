package scripts.miner.decisions;

import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.miner.data.Vars;

public class BankDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return Vars.get().location.getBank().containsPlayer();
    }
}
