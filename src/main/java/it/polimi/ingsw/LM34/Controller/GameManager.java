package it.polimi.ingsw.LM34.Controller;

import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.DiceDependentContexts.CouncilPalaceContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts.ChurchReportContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts.TurnContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts.UseCouncilPrivilegeContext;
import it.polimi.ingsw.LM34.Controller.NonInteractiveContexts.EndGameContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Enums.Controller.PlayerSelectableContexts;
import it.polimi.ingsw.LM34.Enums.Model.DiceColor;
import it.polimi.ingsw.LM34.Enums.Model.PawnColor;
import it.polimi.ingsw.LM34.Exceptions.Controller.NoSuchContextException;
import it.polimi.ingsw.LM34.Exceptions.Validation.IncorrectInputException;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.CouncilPalace;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.Market;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.Tower;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.WorkingArea;
import it.polimi.ingsw.LM34.Model.Boards.PlayerBoard.BonusTile;
import it.polimi.ingsw.LM34.Model.Boards.PlayerBoard.PersonalBoard;
import it.polimi.ingsw.LM34.Model.Cards.*;
import it.polimi.ingsw.LM34.Model.Dice;
import it.polimi.ingsw.LM34.Model.Effects.AbstractOncePerRoundEffect;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Network.GameRoom;
import it.polimi.ingsw.LM34.Network.Server.ServerNetworkController;
import it.polimi.ingsw.LM34.Utils.Configurator;
import it.polimi.ingsw.LM34.Utils.Validator;

import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.*;
import static it.polimi.ingsw.LM34.Utils.Configurator.MAX_LEADER_PER_PLAYER;
import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

public class GameManager {
    private GameRoom gameRoom;

    /*TURNS*/
    private Integer period; //3 in a game
    private Integer round; //3*2 in a game
    /**
     * Equals to the number of {@link Player s}
     */
    private Integer phase;
    /**
     * When a player places 1 {@link FamilyMember}(and in addition does special actions)
     */
    private Integer turn;

    private List<Dice> dices;
    private List<Player> players;

    /*GAMEBOARD COMPONENTS*/
    private Market market;
    private List<Tower> towers;
    private CouncilPalace councilPalace;
    private WorkingArea harvestArea;
    private WorkingArea productionArea;
    private Map<Integer, Integer> faithPath;
    private Map<Integer, Integer> mapCharactersToVictoryPoints;
    private Map<Integer, Integer> mapTerritoriesToVictoryPoints;

    /*DECKS*/
    private DevelopmentCardDeck<TerritoryCard> territoryCardDeck;
    private DevelopmentCardDeck<CharacterCard> characterCardDeck;
    private DevelopmentCardDeck<VentureCard> ventureCardDeck;
    private DevelopmentCardDeck<BuildingCard> buildingCardDeck;
    private List<LeaderCard> leaderCardsDeck;
    private List<ExcommunicationCard> excommunicationCards;

    /*GAME CONTEXTS*/
    private Map<ContextType, AbstractGameContext> contexts;
    private TurnContext turnContext = new TurnContext();
    private CouncilPalaceContext palaceContext;

    public GameManager(GameRoom gameRoom, List<String> players) {
        this.gameRoom = gameRoom;

        this.players = new ArrayList<>();
        for (int i = 0; i < players.size(); i++)
            this.players.add(new Player(players.get(i), PawnColor.values()[i], new PersonalBoard()));

        this.period = 0; //when cards of the new period are stored on towers
        this.round = 0; //when all players have placed all their pawns
        this.phase = 0; //when all players have placed 1 of their pawn
        this.turn = 0; //when the current player places his pawn

        setupGameContexts();
        setUpGameSpaces();
        setUpDecks();
        replaceCards();
        setExcommunicationCards();

        //Instantiate and roll dices
        this.dices = new ArrayList<>();
        for(DiceColor color : DiceColor.values())
            this.dices.add(new Dice(color));
        rollDices();

        /**
         * Randomly set the initial play order
         */
        Collections.shuffle(players);
        setupPlayersResources();
    }

    public ServerNetworkController getActivePlayerNetworkController() {
        return this.gameRoom.getPlayerNetworkController(players.get(this.turn).getPlayerName());
    }

    public ServerNetworkController getPlayerNetworkController(Player player) {
        return this.gameRoom.getPlayerNetworkController(player.getPlayerName());
    }

    /**
     * At game startup show to the each client the info about the players
     */
    public void startGame() {
        bonusTileSelectionPhase();
        //leaderSelectionPhase(); //TODO: uncomment
        //players.forEach(player -> this.getPlayerNetworkController(player).setExcommunicationCards(this.excommunicationCards));
        players.forEach(player -> this.getPlayerNetworkController(player).updateDiceValues(this.dices));
        players.forEach(player -> this.getPlayerNetworkController(player).updatePlayersData(this.players));
        players.forEach(player -> this.getPlayerNetworkController(player).updateTowers(this.towers));
        players.forEach(player -> this.getPlayerNetworkController(player).updateProductionArea(this.productionArea));
        players.forEach(player -> this.getPlayerNetworkController(player).updateHarvestArea(this.harvestArea));
        players.forEach(player -> this.getPlayerNetworkController(player).updateMarket(this.market));
        players.forEach(player -> this.getPlayerNetworkController(player).updateCouncilPalace(this.councilPalace));

        ((TurnContext) getContextByType(TURN_CONTEXT)).initContext(); //first player start first round of the game
    }

    /**
     * Sets up the game spaces before game begins
     */
    private void setUpGameSpaces() {
        this.market = Configurator.getMarket();
        this.towers = Configurator.getTowers();
        this.councilPalace = Configurator.getPalace();
        this.harvestArea = Configurator.getHarvestArea();
        this.productionArea = Configurator.getProductionArea();
    }

    /**
     * Enter the EndGameContext during which final points are calculated and ranking is showed
     * With this {@link EndGameContext} the games end
     */
    private void endGame() {
        EndGameContext endGameContext = (EndGameContext) getContextByType(END_GAME_CONTEXT);
        endGameContext.interactWithPlayer();
    }

    /**
     * Prepare decks at the start of the game
     * {@link DevelopmentCardDeck}
     * {@link ExcommunicationCard deck}
     * {@link LeaderCard deck}
     */
    private void setUpDecks() {
        this.territoryCardDeck = Configurator.getTerritoryCards();
        this.buildingCardDeck = Configurator.getBuildingCards();
        this.characterCardDeck = Configurator.getCharactersCards();
        this.ventureCardDeck = Configurator.getVentureCards();
        this.leaderCardsDeck = Configurator.getLeaderCards(this.players.size());
        this.excommunicationCards = Configurator.getExcommunicationTiles();
    }

    public void nextTurn() {
        turn++;
        if (turn >= players.size()) { //all players have placed 1 pawn
            this.turn = 0;
            nextPhase();
        } else {
            ((TurnContext) getContextByType(TURN_CONTEXT)).initContext();
        }
    }

    private void nextPhase() {
        this.phase++;

        /*If all players have placed all of their pawns go to the next round*/
        if(phase >= (players.size() * players.get(0).getFamilyMembers().size()))
            nextRound();
    }

    private void nextRound() { //round = half period
        this.round++;

        this.players.forEach(player -> {
            player.getActivatedLeaderCards().forEach(card -> {
                if(card.getBonus() instanceof AbstractOncePerRoundEffect)
                    ((AbstractOncePerRoundEffect) card.getBonus()).newTurn();
            });
            player.getFamilyMembers().forEach(FamilyMember::freePawn);
        });

        if (this.round % 2 == 0)
            nextPeriod();

        this.phase = 0;

        setNewTurnOrder();
        rollDices();
        sweepActionSlots();  //sweeps all action and tower slots from pawns and cards
        replaceCards();      //Four development cards per type are moved from the decks into the towerslots
    }

    private void nextPeriod() {
        this.period++;

        /*Now it is Curch Report time*/
        ChurchReportContext churchContext = (ChurchReportContext) getContextByType(CHURCH_REPORT_CONTEXT);

        /*ChurchReportContext interact with a player at a time, based on turn order*/
        this.players.forEach(churchContext::interactWithPlayer);

        /*enter the endGame context in which final points are calculated*/
        if(this.period >= Configurator.TOTAL_PERIODS)
            endGame();
    }

    /**New cards are placed in the towers at the beginning of the new round**/
    private void replaceCards() {
        changeCards(towers, territoryCardDeck);
        changeCards(towers, buildingCardDeck);
        changeCards(towers, characterCardDeck);
        changeCards(towers, ventureCardDeck);
    }

    /**
     * Free all the action slots from the pawns and card stored during the previous round
     */
    private void sweepActionSlots() {
        this.towers.forEach(Tower::sweep);
        this.market.sweep();
        this.productionArea.sweep();
        this.harvestArea.sweep();
    }

    /**
     * Roll the dices at the beginning of a Round
     */
    private void rollDices() {
        dices.forEach(Dice::rollDice);
    }

    /**
     * provide the players the initial amount of resources
     */
    private void setupPlayersResources() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).addResources(new Resources(
                Configurator.BASE_COINS + i * Configurator.COINS_INCREMENT_PLAYER_ORDER,
                Configurator.BASE_WOODS + i * Configurator.WOODS_INCREMENT_PLAYER_ORDER,
                Configurator.BASE_STONES + i * Configurator.STONES_INCREMENT_PLAYER_ORDER,
                Configurator.BASE_SERVANTS + i * Configurator.SERVANTS_INCREMENT_PLAYER_ORDER
            ));
        }
    }

    /**
     * Instantiate all the game contexts at the start of the game
     */
    private void setupGameContexts() {
        contexts = new EnumMap<>(ContextType.class);
        for (ContextType context : ContextType.values())
            try {
                contexts.put(context, ContextFactory.getContext(context));
            } catch(NoSuchContextException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }

        ((UseCouncilPrivilegeContext) contexts.get(USE_COUNCIL_PRIVILEGE_CONTEXT)).setRewards(Configurator.getCouncilPrivilegeRewards());
        contexts.forEach((type, context) -> context.setGameManager(this));
    }

    public AbstractGameContext getContextByType(ContextType contextType) {
        return this.contexts.getOrDefault(contextType, null);
    }

    public AbstractGameContext getContextByType(PlayerSelectableContexts contextType) {
        return contexts.getOrDefault(contextType, null);
    }

    /**
     * Set the new players order for the new round about to start
     * @return the new {@link Player}s order
     */
    private List<Player>  setNewTurnOrder() {
        List<Player> oldPlayersOrder = players;
        List<Player> newPlayersOrder = new ArrayList<>();
        List<FamilyMember> membersInOrder = this.councilPalace.getActionSlot().getFamilyMembers();

        List<PawnColor> councilPalaceOrder = this.councilPalace.getPlayersOrder();
        for(int i = 0; i < councilPalaceOrder.size(); i++)
            for(int j = 0; j < oldPlayersOrder.size(); j++)
                if(oldPlayersOrder.get(j).getPawnColor().name() == councilPalaceOrder.get(i).name()) {
                    newPlayersOrder.add(oldPlayersOrder.get(j));
                    oldPlayersOrder.remove(j);
                }
        newPlayersOrder.addAll(oldPlayersOrder);

        return newPlayersOrder;
    }

    /**
     * Replace the cards at the beginning of the new round
     * @param towers from which choose the right tower by development card type
     * @param developmentDeck from which to extract and place in the tower the cards for the new round
     */
    private void changeCards(List<Tower> towers, DevelopmentCardDeck<?> developmentDeck) {
        Tower tower = null;
        Iterator iterator = developmentDeck.iterator();

        /**select the right tower...**/
        for (Tower t : towers)
            if (t.getDevelopmentTypeStored() == developmentDeck.getCardColor())
                tower = t;

        /**...and now place every card in the deck until the tower's slots are full**/
        if(tower != null)
            tower.sweep();
        while (iterator.hasNext() && tower.getCardsStored().size() < Configurator.CARD_PER_ROUND)
            tower.addCard((AbstractDevelopmentCard) iterator.next());
    }

    /**
     * Choose the 3 excommunication cards of the game
     */
    private void setExcommunicationCards() {
        this.excommunicationCards = Configurator.getExcommunicationTiles();
    }

    /**
     * The players have the opportunity to choose one {@link BonusTile}
     * he wants to have during the game
     *if timeout while user selects the card -> an arbitrary {@link  is selected automatically
     */
    private void bonusTileSelectionPhase() {
        List<BonusTile> bonusTiles;
        bonusTiles = Configurator.getBonusTiles();
        for(Integer playerIndex = 0; playerIndex < players.size(); playerIndex++) {
            Integer selected = getPlayerNetworkController(players.get(playerIndex)).bonusTileSelection(bonusTiles);
            players.get(playerIndex).getPersonalBoard().setPersonalBonusTile(bonusTiles.get(selected));
            bonusTiles.remove(selected.intValue());
        }
    }

    /**
     * To each player show 4,3,2 {@link LeaderCard} at each step, from which he chooses one
     * If timeout while user selects the card -> an arbitrary card is selected automatically
     */
    private void leaderSelectionPhase() {
        /**
         * The {@link LeaderCard s} are only 4*#players, the remaining cards are not considered in the game
         */
        leaderCardsDeck = Configurator.getLeaderCards(players.size());
        List<List<LeaderCard>> leaderCardsGroups = new ArrayList<>();

        for(int i = 0; i < players.size(); i++) {
            leaderCardsGroups.add(new ArrayList<>());
            for(int j = 0; j < MAX_LEADER_PER_PLAYER; j++)
                leaderCardsGroups.get(i).add(leaderCardsDeck.get(i * MAX_LEADER_PER_PLAYER + j));
        }

        for(int i = 0; i < players.size(); i++) {
            for(int j = 0; j < MAX_LEADER_PER_PLAYER; j++) {
                playerLeaderCardSelection(leaderCardsGroups.get(i), players.get(j % players.size()));
            }
            Collections.rotate(players, 1);
        }
    }

    private void playerLeaderCardSelection(List<LeaderCard> leaderCards, Player player) {
        try {
            Integer selectedCard = getPlayerNetworkController(player).leaderCardSelectionPhase(leaderCards);
            Validator.checkValidity(selectedCard, leaderCards);
            player.addLeaderCard(leaderCards.get(selectedCard));
            leaderCards.remove(selectedCard.intValue());
        } catch(IncorrectInputException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            playerLeaderCardSelection(leaderCards, player);
        }
    }

    public Integer getPeriod() {
        return period;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.turn);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Tower> getTowers() {
        return this.towers;
    }

    public Market getMarket() {
        return this.market;
    }

    public WorkingArea getProductionArea() {
        return this.productionArea;
    }

    public WorkingArea getHarvestArea() {
        return this.harvestArea;
    }

    public CouncilPalace getCouncilPalace() {
        return this.councilPalace;
    }

    public List<ExcommunicationCard> getExcommunicationCards() {
        return this.excommunicationCards;
    }

    public CouncilPalace getPalace() { return this.councilPalace; }
}







