package it.polimi.ingsw.LM34.Model.ResourceRelatedBonus;

import it.polimi.ingsw.LM34.Controller.GameContexts.AbstractGameContext;
import it.polimi.ingsw.LM34.Controller.GameContexts.LeaderDiscardContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;
import it.polimi.ingsw.LM34.Model.Effects.GameSpaceRelatedBonus.WorkingAreaValueEffect;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Utils.Utilities;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by GiulioComi on 18/05/2017.
 */


//TODO: remember to activate these rewards in the controller at the beginning of the phase of each player
public class PerRoundLeaderReward extends AbstractEffect implements Observer {
    private Resources resources;
    private Integer councilPrivilege;
    private WorkingAreaValueEffect workingAreaValueEffect; //"francesco sforza, leonardo da vinci"

    public PerRoundLeaderReward(Resources resources, Integer councilPrivilege) {
        this.resources = resources;
        this.councilPrivilege = councilPrivilege;
    }

    //TODO: "francesco sforza, leonardo da vinci"
    public PerRoundLeaderReward(WorkingAreaValueEffect valueEffect) {
        this.workingAreaValueEffect = valueEffect;
    }

    public PerRoundLeaderReward() {
        resources = null;
        councilPrivilege = null;
        workingAreaValueEffect = null;
    }

    public Resources getResources() {
        return this.resources;
    }

    public Integer getCouncilPrivilege() {
        return this.councilPrivilege;
    }

    public WorkingAreaValueEffect getWorkingAreaValueEffect() {
        return this.workingAreaValueEffect;
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO: remove this testing call
        LeaderDiscardContext callerContext = (LeaderDiscardContext) o;
        Integer numberOfDiscardedCards = (Integer) arg;
        numberOfDiscardedCards = 7;
        callerContext.setTotalLeadersDiscarded(numberOfDiscardedCards);
        /*try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/


        System.out.println("Carte scartate: " + numberOfDiscardedCards);


        /*if (arg instanceof Player) {
            Player player = (Player) arg;
            player.addResources(resources);
            player.addCouncilPrivileges(councilPrivilege);
        }*/
    }


    @Override
    public void subscribeObserverToContext(ArrayList<AbstractGameContext> contexts)  {
        Utilities.getContextByType(contexts, ContextType.TURN_CONTEXT).addObserver(this);

    }

    public AbstractEffect subscribeObserverToContext(Object bypass, ArrayList<AbstractGameContext> contexts)  {
        /*//"francesco sforza, leonardo da vinci"
        if(workingAreaValueEffect != null)
            workingAreaValueEffect.subscribeObserverToContext(contexts);
        else*/
        //Utilities.getContextByType(contexts, ContextType.TURN_CONTEXT).addObserver(this);
        //TODO: remove this testing call



        System.out.println("mi sono iscritto al contesto");
        return this;
    }
    @Override
    public void applyEffect(ArrayList<AbstractGameContext> contexts, Player player) {

    }


    public void applyEffect(ArrayList<AbstractGameContext> contexts) {
        subscribeObserverToContext(contexts);
    }

    @Override
    public boolean isOncePerRound() {
        return true; //all these leader bonuses are activable once per round
    }





}
