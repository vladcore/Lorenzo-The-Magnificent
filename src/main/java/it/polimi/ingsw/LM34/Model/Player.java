package it.polimi.ingsw.LM34.Model;

import it.polimi.ingsw.LM34.Enums.Model.DiceColor;
import it.polimi.ingsw.LM34.Enums.Model.PawnColor;
import it.polimi.ingsw.LM34.Enums.Model.ResourceType;
import it.polimi.ingsw.LM34.Model.Boards.PlayerBoard.PersonalBoard;
import it.polimi.ingsw.LM34.Model.Cards.ExcommunicationCard;
import it.polimi.ingsw.LM34.Model.Cards.LeaderCard;
import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio Comi on 03/05/2017.
 */
public class Player implements Serializable {
    private final PawnColor pawnColor; //COLOR OF THE PAWN ASSOCIATED TO THE PLAYER
    private ArrayList<FamilyMember>  familyMembers;
    private PersonalBoard personalBoard;
    private ArrayList <LeaderCard> leadercards;
    private ArrayList<ExcommunicationCard> excommunicationCards;
    private AbstractEffect permanentBonus;
    //TODO: initialize the resources elsewhere
    private Resources resources;
    private Integer councilPrivileges;

    private ArrayList<AbstractEffect> abstractEffects;

    //VARIABLE FOR COMMUNICATION TO CLIENT
    //TODO: evalueate if this network connection is correct

    public Player(PawnColor pawnColor, PersonalBoard personalBoard) {
        this.pawnColor= pawnColor;
        this.personalBoard= personalBoard;
        resources = new Resources();

        familyMembers= new ArrayList<FamilyMember>();

        for ( DiceColor diceColor : DiceColor.values())
            familyMembers.add(new FamilyMember(pawnColor, diceColor));
    }


    //the controller updates the resources and bonuses of the player directly in the personalBoard;
    public PersonalBoard getPersonalBoard() {
        return this.personalBoard;
    }

    public PawnColor getPawnColor() {
        return pawnColor;
    }

    public void addResources(Resources res)    {
        this.resources.sumResources(res);
    }

    public Resources getResources() { return this.resources; }

    public ArrayList<FamilyMember> getFamilyMembers() {
        return this.familyMembers;
    }

    public ArrayList<AbstractEffect> getObservers() {
        return this.abstractEffects;
    }

    public void addObserver(AbstractEffect a) {
        abstractEffects.add(a);
    }

    public void unSubscribeObservers() {
        abstractEffects.clear();
    }

    public Integer getCouncilPrivileges() {
        return councilPrivileges;
    }

    public void addCouncilPrivileges(Integer councilPrivileges) {
        this.councilPrivileges += councilPrivileges;
    }

    public void subResources(Resources res) {
        this.resources.subResources(res);
    }

    public ArrayList<ExcommunicationCard> getExcommunicationCards() {
        return excommunicationCards;
    }

    public void addExcommunicationCards(ExcommunicationCard excommunicationCard) {
        this.excommunicationCards.add(excommunicationCard);
    }

    public Boolean hasEnoughResources (Resources resourcesRequired) {
        Resources resourcesAvailable =this.getResources();
        for(ResourceType resType : ResourceType.values())
            if(!(resourcesAvailable.getResourceByType(resType) >= resourcesRequired.getResourceByType(resType)))
                return false;

        return true;
    }
}
