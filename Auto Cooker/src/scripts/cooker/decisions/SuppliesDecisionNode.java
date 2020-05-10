package scripts.cooker.decisions;

import scripts.api.Inventory;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.cooker.data.Vars;

public class SuppliesDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Inventory.contains(Vars.get().rawId);
    }

}
