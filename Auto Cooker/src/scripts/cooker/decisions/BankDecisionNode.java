package scripts.cooker.decisions;

import scripts.api.Banking;
import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.cooker.data.Location;
import scripts.cooker.data.Vars;

public class BankDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Vars.get().location != Location.AL_KHARID || Banking.Bank.AL_KHARID.contains(Player.getPosition());
    }

}
