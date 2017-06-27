package it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Exceptions.Validation.IncorrectInputException;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

/*Called by DiceDependentContext classes*/
public class FamilyMemberSelectionContext extends AbstractGameContext {
    private Integer tempValue = 0; //if we not initialized tempValue to 0, when we will try to run increaseTempValue we will get NullPointerException

    @Override
    public void interactWithPlayer() {
        /*VOID*/
    }


    public FamilyMember familyMemberSelection(List<FamilyMember> pawnsCloned) {
        FamilyMember selectedPawn = null;
        List<FamilyMember> playerPawns = new ArrayList();
        Integer selected = this.gameManager.getActivePlayerNetworkController().familyMemberSelection(pawnsCloned);

        try {
            Validator.checkValidity(selected.toString(),playerPawns);
            selectedPawn = playerPawns.get(selected);
            tempValue = selectedPawn.getValue();

                //gameManager.getContextByType(INCREASE_PAWNS_VALUE_BY_SERVANTS_CONTEXT).interactWithPlayer(player);

        }
        /*If input mismatches expected informations... the player is able to try again*/
        catch(IncorrectInputException ide){
            LOGGER.log(Level.INFO, "Invalid input");
            familyMemberSelection(pawnsCloned);
        }
      //TODO: handle if player has not enough dice value...
        return selectedPawn;
    }


    public void increaseTempValue(Integer servantsConsumed) {
        tempValue += servantsConsumed;
    }

}
