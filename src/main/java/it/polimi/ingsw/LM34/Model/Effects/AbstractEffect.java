package it.polimi.ingsw.LM34.Model.Effects;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Model.Player;

import java.util.ArrayList;

/**
 * Created by vladc on 5/13/2017.
 */
public abstract class AbstractEffect {
    private ArrayList<AbstractGameContext> observableContexts;
    /**
     *
     * @return true if the observer is activable once per round (e.g. SkipTurn, PerRoundLeaderReward)
     * Default is false
     */
    public boolean isOncePerRound() {
        return false;
    }


    public void subscribeObserverToContext(ArrayList<AbstractGameContext> contexts) {
        this.observableContexts = contexts;
    }

    public abstract void applyEffect( Player player);

}
