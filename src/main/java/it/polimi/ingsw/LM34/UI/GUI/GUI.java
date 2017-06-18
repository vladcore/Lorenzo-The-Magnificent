package it.polimi.ingsw.LM34.UI.GUI;


import it.polimi.ingsw.LM34.Enums.Controller.LeaderCardsAction;
import it.polimi.ingsw.LM34.Enums.Model.DevelopmentCardColor;
import it.polimi.ingsw.LM34.Enums.Model.PawnColor;
import it.polimi.ingsw.LM34.Enums.Model.ResourceType;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.*;
import it.polimi.ingsw.LM34.Model.Cards.AbstractDevelopmentCard;
import it.polimi.ingsw.LM34.Model.Cards.ExcommunicationCard;
import it.polimi.ingsw.LM34.Model.Cards.LeaderCard;
import it.polimi.ingsw.LM34.Model.Dice;
import it.polimi.ingsw.LM34.Model.Effects.ResourceRelatedBonus.ResourcesBonus;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Network.Client.AbstractClient;
import it.polimi.ingsw.LM34.Network.Client.ClientNetworkController;
import it.polimi.ingsw.LM34.Network.Client.RMI.RMIClient;
import it.polimi.ingsw.LM34.Network.Client.Socket.SocketClient;
import it.polimi.ingsw.LM34.Network.PlayerAction;
import it.polimi.ingsw.LM34.UI.GUI.GuiControllers.PersonalBoardController;
import it.polimi.ingsw.LM34.UI.GUI.GuiViews.*;
import it.polimi.ingsw.LM34.UI.UIInterface;
import it.polimi.ingsw.LM34.Utils.Configurator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by vladc on 6/6/2017.
 */
public class GUI extends Application implements UIInterface {
    private AbstractClient networkClient;
    private ClientNetworkController networkController;
    private PersonalBoardController personalBoardController;

    private Parent root;
    private Stage primaryStage;
    private LoginDialog loginDialog;
    private PopupSlotBonus popupSlotBonus;
    private Scene guiScene;

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private RadioButton rmiChoice;
    @FXML
    private RadioButton socketChoice;
    @FXML
    private AnchorPane login;
    @FXML
    private Group towers;
    @FXML
    private Group slots;
    @FXML
    private VBox playersInfoList;

    public GUI() {
        personalBoardController = new PersonalBoardController(); //TODO
    }

    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("views/gui.fxml"));

        this.primaryStage = new Stage();
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        prepareWindow();
        /*----------GAME SETUPS----------*/
        //loadTowersBonuses(); //TODO
        //loadCouncilPrivilegesBonuses(); //TODO
        //loadMarketBonuses(); //TODO
        //loadFaithPath(); //TODO

        /*----------TURN SETUPS--------*/
        //updatePlayersInfo();
        //updateDiceValues();

        /*----------ROUND SETUPS--------*/
        //sweepSlots();

        /*----------DIALOGS--------*/
        //servantsSelection(5,1);
        //churchReportDecision(4,2);
        //leaderCardAction(); //TODO
        //addPlayersInfo(root); //TODO
        //endGame();
        //familyMemberSelection();

        /*List<Pair<Resources, ResourcesBonus>> listPairExchange = new ArrayList<>();
        Resources tempRes = new Resources(1,2,9,0);
        ResourcesBonus tempResBon = new ResourcesBonus(new Resources(), 3);
        Resources tempRes2 = new Resources(0,0,72,5);
        ResourcesBonus tempResBon2 = new ResourcesBonus(new Resources(3,2,1,4), 1);
        listPairExchange.add(new ImmutablePair<>(tempRes, tempResBon));
        listPairExchange.add(new ImmutablePair<>(tempRes2, tempResBon2));
        new ResourceExchangeDialog().interactWithPlayer(listPairExchange);*/

        /*List<Resources> resList = new ArrayList<>();
        resList.add(tempRes);
        resList.add(new Resources(3,2,1,0));
        new UseCouncilPrivilegeDialog().interactWithPlayer(resList);*/

        /*Player giacomo = new Player("giacomo", BLUE, new PersonalBoard());
        giacomo.addResources(new Resources(4,5,1,2));
        Player antonio = new Player("antonio", PawnColor.RED, new PersonalBoard());
        antonio.addResources(new Resources(4,5,1,2, 7,4 ,9));
        List<Player> players = new ArrayList<>();
        players.add(giacomo); players.add(antonio);
        updatePlayersData(players);

        Dice orange = new Dice(ORANGE); orange.rollDice();
        Dice black = new Dice(BLACK); orange.rollDice();
        Dice white = new Dice(WHITE); orange.rollDice();
        List<Dice> dices = new ArrayList<>();
        dices.add(orange); dices.add(black); dices.add(white);
        updateDiceValues(dices);*/
    }

    @Override
    public void endGame(List<Player> players) {
        try {
            new EndGameDialog(players).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*TODO: public void leaderCardAction() {
        try {
            Parent root2 = FXMLLoader.load(getClass().getClassLoader().getResource("views/leaderCardAction.fxml"));
            primaryStage.setScene(new Scene(
                    root2, 500, 400));
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void prepareWindow() {

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.getIcons().add(new Image(Thread.currentThread().getContextClassLoader().getResource("images/icon.png").toExternalForm()));
        primaryStage.setTitle("Lorenzo il Magnifico by CranioCreations");
        guiScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(guiScene);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void loginResult(Boolean result) {
        if (result) {
            try {
                username.getScene().getWindow().hide();
                this.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setExcommunicationCards(List<ExcommunicationCard> excommunicationCards) {
        ImageView imageView = new ImageView();
        for (Integer index = 1; index <= Configurator.TOTAL_PERIODS; index++) {
            imageView = ((ImageView) root.lookup("#excommunicationCard" + excommunicationCards.get(index).getPeriod()));
            imageView.setImage(new Image(Thread.currentThread()
                    .getContextClassLoader().getResource("images/excommunicationTiles/excomm_" + index + "_" + excommunicationCards.get(index).getNumber() + ".png")
                    .toExternalForm()));
        }
    }

    @Override
    public void updateTowers(List<Tower> towers) {
        ArrayList<AbstractDevelopmentCard> tempCardsStored = new ArrayList<>();


        for (Tower tower : towers) {
            /***Load cards***/
            Integer indexCard = 0;
            for (AbstractDevelopmentCard card : tower.getCardsStored()) {
                ImageView imageView = ((ImageView) root.lookup("#tower" + card.getColor().toString() + "_level" + indexCard));
                imageView.setImage(new Image(Thread.currentThread()
                        .getContextClassLoader().getResource("images/developmentCards/" + card.getColor().getDevType() + card.getName() + ".png")
                        .toExternalForm()));
                indexCard++;
            }
            /***Load slots***/
            Integer indexSlot = 0;

            for (TowerSlot slot : tower.getSlotsStored()) {
                Resources resources = new Resources();
                ImageView imageView = ((ImageView) root.lookup("#tower" +
                        slot.getCardStored().getColor().toString() + "Bonus" + indexSlot));
                resources = slot.getResourcesReward().getResources();
                for (ResourceType resType : ResourceType.values())
                    if (resources.getResourceByType(resType) != 0)
                        imageView.setImage(new Image(Thread.currentThread()
                                .getContextClassLoader().getResource("images/resources/" + resType + ".png").toExternalForm()));
                indexSlot++;
            }
        }
    }

    @Override
    public void updateCouncilPalace(CouncilPalace councilPalace) {
        StackPane palacePane = (StackPane) root.lookup("#councilPalace");
        ImageView imageView;
        if(councilPalace.getOccupyingPawns().size() <= 0)
            palacePane.getChildren().removeAll(palacePane.getChildren());
        else
            for(FamilyMember pawn : councilPalace.getOccupyingPawns()) {
                imageView = new ImageView(new Image(Thread.currentThread().getContextClassLoader().getResource("images/pawns" + pawn.getFamilyMemberColor() + ".png").toExternalForm()));
                palacePane.getChildren().add(imageView);
            }
    }

    @Override
    public void updateMarket(Market market) {
        Integer index = 0;
        List<ActionSlot> marketSlots = market.getMarketSlots();
        for(index = 0; index< marketSlots.size(); index++) {
            ImageView imageView = ((ImageView) root.lookup("#marketActionSlot" + index));
            PawnColor pawnColor = marketSlots.get(index).getFamilyMember().getFamilyMemberColor();
            imageView.setImage(new Image(Thread.currentThread()
                    .getContextClassLoader().getResource("images/pawns/" + pawnColor.toString() + ".png").toExternalForm()));
        }
    }

    @Override
    public void updateProductionArea(WorkingArea productionArea) {
    }

    @Override
    public void updateHarvestArea(WorkingArea harvestArea) {
    }


    @Override
    public void updatePlayersData(List<Player> players) {
        Pane playerInfo = new Pane();
        Label playerName = new Label();
        Text value = new Text();
        DropShadow borderGlow;
        Integer numPlayer = 1;

        for (Player player : players) {
            playerInfo = (Pane) root.lookup("#playerInfo" + numPlayer);
            playerInfo.setBackground(new Background(new BackgroundFill(Color.valueOf(player.getPawnColor().toString()), CornerRadii.EMPTY, Insets.EMPTY)));
            playerInfo.setVisible(true);
            playerName = (Label) root.lookup("#player" + numPlayer);
            playerName.setText(player.getPlayerName());
            for (ResourceType resType : ResourceType.values()) {
                value = (Text) root.lookup("#" + resType.toString() + "_player" + numPlayer + "_value");
                value.setText(player.getResources().getResourceByType(resType).toString());

                borderGlow = new DropShadow();
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setSpread(0.4);
                borderGlow.setRadius(25.0);
                borderGlow.setColor(Color.WHITE);
                borderGlow.setWidth(35);
                borderGlow.setHeight(35);
                value.setEffect(borderGlow);
            }
            numPlayer++;
        }
    }

    @Override
    public void updateDiceValues(List<Dice> dicesValues) {
        Text diceSlot = new Text();

        for(Dice dice : dicesValues) {
            diceSlot = (Text) root.lookup("#diceSlot" + dice.getColor());
            diceSlot.setText(dice.getValue().toString());
        }
    }

    @Override
    public PlayerAction turnMainAction(Optional<Boolean> lastActionValid) {
        return null;
    }

    @Override
    public PlayerAction turnSecondaryAction(Optional<Boolean> lastActionValid) {
        return null;
    }

    @Override
    public Integer familyMemberSelection(List<FamilyMember> familyMembers) {
        return new FamilyMemberSelectDialog().interactWithPlayer(familyMembers);
    }

    @Override
    public Integer servantsSelection(Integer servantsAvailable, Integer minimumServantsRequested) {
        return new UseServantsDialog().interactWithPlayer(servantsAvailable, minimumServantsRequested);
    }

    @Override
    public Integer resourceExchangeSelection(List<Pair<Resources, ResourcesBonus>> choices) {
        return new ResourceExchangeDialog().interactWithPlayer(choices);
    }

    //TODO
    @Override
    public Integer leaderCardSelection(List<LeaderCard> leaderCards, LeaderCardsAction action) {
        return null;
    }

    @Override
    public Boolean churchSupport() {
        return new ChurchReportDialog().interactWithPlayer();
    }

    @Override
    public Integer selectCouncilPrivilegeBonus(List<Resources> availableBonuses) {
        return new UseCouncilPrivilegeDialog().interactWithPlayer(availableBonuses);
    }


    @Override
    public void show() {

        loginMenu();
    }

    public void doLogin() {
        if(rmiChoice.isSelected() && !socketChoice.isSelected())
            this.networkClient = new RMIClient(SERVER_IP, RMI_PORT, this);
        else
            this.networkClient = new SocketClient(SERVER_IP, SOCKET_PORT, this);

        this.networkController = this.networkClient.getNetworkController();
        this.networkClient.login(username.getText(), password.getText());
    }

    @Override
    public void loginMenu() {
        this.loginDialog = new LoginDialog();
        this.loginDialog.show();
    }

    public void sweepSlots() {
        sweepWorkingAreas();
        sweepTowersSlots();
    }

    //TODO
    @FXML
    public void placePawn(MouseEvent event) {
        Image image;

        ArrayList<FamilyMember> membersAvailable = new ArrayList<>();

        //TODO: get the list from the model instantiated in the client or sent by the controller

        String source = event.getPickResult().getIntersectedNode().getId();
        FamilyMemberSelectDialog dialog = new FamilyMemberSelectDialog();

        //TODO: the server confirm that the move is valid before removing the card from the towers

        List<Node> nodes = towers.getChildren();
        for (Node node : nodes)
            if (node.getId() == source) {
            //TODO: set image of the pawn getting it from Resource/images/..
                //((ImageView) node).setImage(....getResource("images/pawns/"+ choosedPawn +".png");
            }
    }

    @FXML
    public void popupBonus(MouseEvent event) {
        ActionSlot slot = new ActionSlot(true, 3, new ResourcesBonus(new Resources(3,4,4,3),1));
        //TODO: get reward from source String
        popupSlotBonus =  new PopupSlotBonus(event, new ResourcesBonus(new Resources(3,3,982,90),1));

        try {
            popupSlotBonus.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMarketBonuses() {
        Integer index = 0;
        for(index= 0; index<4; index++) {
            //TODO: load bonus from model view
        }
    }

    public void loadCouncilPrivilegesBonuses() {
        //TODO
    }

    public void loadFaithPath() {
        //TODO
    }

    @FXML
    public void buyCard(MouseEvent event) {
        Image image;
            Object source = event.getSource();
           ImageView imageView = (ImageView) source;
        imageView.setImage(new Image(Thread.currentThread()
                .getContextClassLoader().getResource("images/transparent.png").toExternalForm()));
    }

    private void sweepTowersSlots() {
        for(Integer index = 0; index < Configurator.MAX_TOWER_LEVELS; index++)
            for(DevelopmentCardColor color : DevelopmentCardColor.values())
                if(color.toString() != "MULTICOLOR") {
                    ImageView imageView = ((ImageView) root.lookup("#tower" + color.toString() + "Bonus" + index));
                    imageView.setImage(new Image(Thread.currentThread()
                            .getContextClassLoader().getResource("images/transparentSlot.png").toExternalForm()));
                }
    }

    private void sweepWorkingAreas() {
        Integer index = 0;
        for(index= 0; index<2; index++) {
            ImageView imageView = ((ImageView) root.lookup("#harvestArea" + index));
            imageView.setImage(new Image(Thread.currentThread()
                    .getContextClassLoader().getResource("images/transparentSlot.png").toExternalForm()));
        }
        index = 0;
        for(index= 0; index<2; index++) {
            ImageView imageView = ((ImageView) root.lookup("#productionArea" + index));
            imageView.setImage(new Image(Thread.currentThread()
                    .getContextClassLoader().getResource("images/transparentSlot.png").toExternalForm()));

        }
    }


    //TODO: remove just after tests
    public static void main(String [] args) {
        GUI gui = new GUI();
        gui.show();
    }

}