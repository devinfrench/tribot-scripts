package scripts.tanner.decisions;

import scripts.api.Banking;
import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;

public class BankDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Banking.Bank.AL_KHARID.contains(Player.getPosition());
    }

}
