package it.polimi.ingsw.LM34.Controller.GameContexts;

import it.polimi.ingsw.LM34.Enums.Controller.ContextStatus;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Model.Player;

import java.util.ArrayList;

/**
 * Created by GiulioComi on 16/05/2017.
 */
public class CurchReportContext  extends AbstractGameContext {


    public void initContext(ArrayList<Player> players) {
        //TODO:implementation
        setChanged(); //trigger sisto IV if is an observer
        notifyObservers(ContextStatus.ENTERED);
    }

    private void interactWithPlayer(Player player) {
        //TODO: implement what player can do here and modify the model in this controller class
        //let the player choice if they wants to be excommunicated and assigned the negative effect to them
    }

    @Override
    public void initContext() {}

    @Override
    public ContextType getType() {
        return ContextType.CURCH_REPORT_CONTEXT;
    }

    @Override
    public void endContext() {
        setChanged();
        notifyObservers(ContextStatus.FINISHED);
    }



}


