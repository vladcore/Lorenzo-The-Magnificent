package it.polimi.ingsw.LM34.Model.Effects.GameSpaceRelatedBonus;

import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by GiulioComi on 16/05/2017.
 */
public class MarketBan extends AbstractEffect implements Observer {


    public void applyEffect() {
      // marketContext.addObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {
        //TODO: disable the possibility to the player to place a pawn in a market slot

    }
}