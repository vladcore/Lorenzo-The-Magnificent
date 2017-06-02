package it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.DiceDependentContexts;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.WorkingArea;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Utils.Configurator;

import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.HARVEST_AREA_CONTEXT;
import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.INCREASE_PAWNS_VALUE_BY_SERVANTS_CONTEXT;

/**
 * Created by GiulioComi on 16/05/2017.
 */
public class HarvestAreaContext extends AbstractGameContext implements DiceDependentContextsInterface {
    private Integer tempValue;
    private WorkingArea harvestArea;


    public HarvestAreaContext() {
        contextType = HARVEST_AREA_CONTEXT;
        harvestArea = Configurator.getHarvestArea();
    }

    @Override
    public void interactWithPlayer(Player player) {
        tempValue = 0;
        //the player chooses the slot to occupy (highlight the difference
        // beetwen single slot and advanced slot)
        setChanged();
        //TODO: now values of dices are increased
        //TODO: player chooses the action slot
        //The reward of the slots are add to the player
        //TODO: interact with player in action slot is necessary?
        gameManager.getContextByType(ContextType.ACTION_SLOT_CONTEXT).interactWithPlayer(player);

        //TODO: now values of dices are increased
        FamilyMember memberChoosed = player.getFamilyMembers().get(1);
        FamilyMember tempMemberChoosed = memberChoosed.clone();
        //TODO: here we pass the family member chosed (only one)
        setChanged();
        notifyObservers(tempMemberChoosed); //observers do a setValue on it
        tempValue = tempMemberChoosed.getValue();
        gameManager.getContextByType(INCREASE_PAWNS_VALUE_BY_SERVANTS_CONTEXT).interactWithPlayer(player);
        harvestArea.getSingleSlot().getResourcesReward().applyEffect(this, player);
    }


    @Override
    public void sweep() {
        harvestArea.sweep();
    }

    @Override
    public void increaseTempValue(Integer servantsConsumed) { this.tempValue += servantsConsumed; }

}