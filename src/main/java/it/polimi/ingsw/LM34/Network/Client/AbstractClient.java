package it.polimi.ingsw.LM34.Network.Client;

import it.polimi.ingsw.LM34.Enums.Controller.PlayerSelectableContexts;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.Market;
import it.polimi.ingsw.LM34.UI.UIInterface;
import java.util.List;

/**
 * Created by vladc on 5/23/2017.
 */
public abstract class AbstractClient {
    protected ClientNetworkController networkController;
    protected UIInterface ui;

    public final ClientNetworkController getNetworkController() {
        return this.networkController;
    }

    public final UIInterface getUI() {
        return this.ui;
    }

    public abstract void login(String username, String password);

    public abstract Integer contextSelection(List<PlayerSelectableContexts> contexts);

    public abstract Integer marketSlotSelection(Market market);

}
