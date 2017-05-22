package it.polimi.ingsw.LM34.Model.Effects.GameSpaceRelatedBonus;

import it.polimi.ingsw.LM34.Controller.GameContexts.AbstractGameContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Enums.Model.DiceColor;
import it.polimi.ingsw.LM34.Exceptions.Controller.NoSuchContextException;
import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Utils.Utilities;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vladc on 5/13/2017.
 *
 * This class aggregates instant and permanent effects from different development card types, leader cards and
 * excomunication tiles, all of which are related to the context where a player places a family member in a game space so that
 * the dice values associated to his pawns has to be incremented or decreased by the effects of the cards mentioned above
 */

//TODO: merge this type of bonuses in a single observer instance
public class FamilyMemberValueEffect extends AbstractEffect implements Observer {

    /**
     * if null then the value is related to the neutral family member
     * if MULTICOLOR, the value is applied to all the dices
     */
    private DiceColor diceColor; //keep track on what dice the effect is applied to

    //TODO: handle federico da montefeltro

    private Integer value;

    /**
     * indicates if the value is:
     *      absolute (substitutes the 'old' value)
     *      relative (adds/subs to 'old' value)
     */
    private Boolean relative;

    public FamilyMemberValueEffect(DiceColor color, Integer value, Boolean relative) {

        this.diceColor = color;
        this.value = value;
        this.relative = relative;
    }

    public DiceColor getDiceColor() {
        return this.diceColor;
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean isRelative() {
        return this.relative;
    }

    @Override
    public void update(Observable o, Object arg) {

        Player player = (Player) arg;
        //increase the values of the family members in this context
        ArrayList<FamilyMember> familyMembers = player.getFamilyMembers();
        for(FamilyMember member : familyMembers)
            if(member.getDiceColorAssociated() == this.diceColor)
                member.setValue(member.getValue() + this.value);
    }

    public void subscribeObserverToContext(ArrayList<AbstractGameContext> contexts) throws NoSuchContextException {
        Utilities.getContextByType(contexts, ContextType.ACTION_SLOT_CONTEXT).addObserver(this);
        Utilities.getContextByType(contexts, ContextType.DEVELOPMENT_CARD_ACQUIRE_CONTEXT).addObserver(this);
    }
}
