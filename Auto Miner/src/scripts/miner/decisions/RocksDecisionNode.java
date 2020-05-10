package scripts.miner.decisions;

import scripts.api.Player;
import scripts.api.script.frameworks.tree.DecisionNode;
import scripts.miner.data.Option;
import scripts.miner.data.Vars;

public class RocksDecisionNode extends DecisionNode {
    @Override
    public boolean isValid() {
        return (!Vars.get().isStationary || Vars.get().option == Option.BANK) && Player.isInArea(Vars.get().miningArea)
          || Vars.get().isStationary && Player.getPosition().equals(Vars.get().stationaryTile);
    }
}
