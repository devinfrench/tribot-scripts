package scripts.cooker.decisions.wine;

import scripts.api.Inventory;
import scripts.api.script.frameworks.tree.DecisionNode;

import static scripts.cooker.data.Constants.GRAPES;
import static scripts.cooker.data.Constants.JUG_OF_WATER;

public class SuppliesDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Inventory.containsAll(GRAPES, JUG_OF_WATER);
    }

}
