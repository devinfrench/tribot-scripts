package scripts.woodcutter.decisions;

import scripts.api.Inventory;
import scripts.api.script.frameworks.tree.DecisionNode;

public class InventoryDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return Inventory.isFull();
    }
}
