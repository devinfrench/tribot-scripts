package scripts.tanner.decisions;

import scripts.api.Inventory;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.tanner.data.Vars;

import static scripts.tanner.data.Constants.COINS;

public class SuppliesDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Inventory.contains(Vars.get().hide.getUntannedId())
            && !Inventory.contains(Vars.get().hide.getTannedId())
            && Inventory.getCount(COINS) >= Vars.get().hide.getCost();
    }

}
