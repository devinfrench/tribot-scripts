package scripts.woodcutter.decisions;

import scripts.api.GroundItems;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.woodcutter.data.Option;
import scripts.woodcutter.data.Vars;

public class NestDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return Vars.get().option == Option.BANK && GroundItems.find().nameContains("nest").getAll().length > 0;
    }
}
