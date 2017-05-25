package it.polimi.ingsw.LM34.Model.Effects.ResourceRelatedBonus;

import it.polimi.ingsw.LM34.Controller.GameContexts.AbstractGameContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Utils.Utilities;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by vladc on 5/13/2017.
 */

/*Basic Instant effects are handled here*/
//This represents also the excomunication malus on resources
public class ResourcesBonus extends AbstractEffect implements Observer {
    private Resources resources;
    private Integer councilPrivilege;
    private Integer developmentCardsGoodsMultiplier;

    public ResourcesBonus(Resources resources, Integer councilPrivilege) {
        this.resources = resources;
        this.councilPrivilege = councilPrivilege;
        this.developmentCardsGoodsMultiplier = 1;
    }

    /*santa rita*/
    public ResourcesBonus(Integer developmentCardsGoodsMultiplier) {
        this.resources = null;
        this.councilPrivilege = 0;
        this.developmentCardsGoodsMultiplier = developmentCardsGoodsMultiplier; //santa rita, 2 di solito
    }

    public Resources getResources() {
        return this.resources;
    }

    public Integer getCouncilPrivilege() {
        return this.councilPrivilege;
    }

    public Integer getDevelopmentCardsGoodsMultiplier() {
        return this.developmentCardsGoodsMultiplier;
    }

    @Override
    public void update(Observable o, Object arg) {
        Player player = (Player) arg;
        player.addResources(this.resources);
    }

    /*santa rita*/
    @Override
    public void subscribeObserverToContext(ArrayList<AbstractGameContext> contexts) {
        Utilities.getContextByType(contexts, ContextType.RESOURCE_INCOME_CONTEXT).addObserver(this);
    }

    @Override
    public void applyEffect(Player player) {
        player.addResources(resources);
        player.addCouncilPrivileges(councilPrivilege);
    }


}
