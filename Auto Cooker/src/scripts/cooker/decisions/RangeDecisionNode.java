package scripts.cooker.decisions;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.cooker.data.Location;
import scripts.cooker.data.Vars;

import static scripts.cooker.data.Constants.AL_KHARID_RANGE_AREA;

public class RangeDecisionNode extends DecisionNode {

    @Override
    public boolean isValid() {
        return Vars.get().location != Location.AL_KHARID || AL_KHARID_RANGE_AREA.contains(Player.getPosition());
    }

}
