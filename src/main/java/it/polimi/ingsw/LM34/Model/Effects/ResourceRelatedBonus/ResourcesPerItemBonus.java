package it.polimi.ingsw.LM34.Model.Effects.ResourceRelatedBonus;

import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;
import it.polimi.ingsw.LM34.Enums.Model.DevelopmentCardColor;
import it.polimi.ingsw.LM34.Model.Resources;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by vladc on 5/13/2017.
 */
public class ResourcesPerItemBonus extends AbstractEffect {
    private Resources bonusResources;
    private DevelopmentCardColor cardColor;
    private Resources requiredResources;

    public ResourcesPerItemBonus(Resources bonusResources, DevelopmentCardColor cardColor, Resources requiredResources) {
        this.bonusResources = bonusResources;
        this.cardColor = cardColor; //"nobile, araldo, cortigiana,governatore, zecca, teatro, esattoria,arco di triongo"
        this.requiredResources = requiredResources; //"generale" card
    }

    public Resources getBonusResources() {
        return this.bonusResources;
    }

    public DevelopmentCardColor getCardColor() {
        return this.cardColor;
    }

    public Resources getRequiredResources() {
        return this.requiredResources;
    }

}
