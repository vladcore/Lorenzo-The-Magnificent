package it.polimi.ingsw.LM34.Model.Effects;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;

import java.util.Observable;
import java.util.Observer;

import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.CURCH_REPORT_CONTEXT;

/**
 * Created by GiulioComi on 16/05/2017.
 *
 * This class represents Sisto IV peculiar effect and registers itself to CurchReportContext
 */
public class ChurchSupportBonus extends AbstractEffect implements Observer {
    private Resources reward;

    public ChurchSupportBonus(Resources reward) {
        this.reward = reward;
    }
    @Override
    public void update(Observable o, Object arg) {
        AbstractGameContext callerContext = (AbstractGameContext) o;
        Player player = (Player) arg;
        player.getResources().sumResources(reward);

    }

    @Override
    public void applyEffect(AbstractGameContext callerContext) {
        callerContext.getContextByType(CURCH_REPORT_CONTEXT).addObserver(this);
    }

}