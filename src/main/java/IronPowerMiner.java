import jdk.nashorn.internal.ir.annotations.Ignore;
import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.*;
import org.rev317.min.api.wrappers.Character;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

@ScriptManifest(author = "Blade", category = Category.MINING, description = "PowerMines Iron and drops it", name = "IronPowerMiner", servers = { "2006Scape" }, version = 1.1)
public class IronPowerMiner extends Script {
    public ArrayList<Strategy> strategies = new ArrayList<Strategy>();
    private static final int[] ORE_ID = {441};
    private static final int[] ROCK_IDS = {2092, 2093};
    private static final int[] GEM_IDS = {1620, 1622, 1618, 1624};
    public final Tile[] BankSpot = {
            new Tile(3295, 3310)
            , new Tile(3301, 3296)
            , new Tile(3299, 3288)
            , new Tile(3300, 3279)
            , new Tile(3302, 3270)
            , new Tile(3305, 3262)
            , new Tile(3303, 3252)
            , new Tile(3305, 3245)
            , new Tile(3311, 3235)//tile in front of gate at duel arena
    };//this will bring you infront of gate at duel arena from mining area
    public final Tile[] BankSpot2 = {
            new Tile(3313, 3235)
            , new Tile(3319, 3236)
            , new Tile(3324, 3243)
            , new Tile(3325, 3253)
            , new Tile(3333, 3265)
            ,new Tile(3340,3265)
            ,new Tile(3345,3265)
            , new Tile(3350, 3265)
            ,new Tile(3355,3265)
            ,new Tile(3360,3266)
            , new Tile(3367, 3266)
            , new Tile(3382, 3266)
            , new Tile(3381, 3268)
    };//this will bring you to duel bank
    TilePath path3 = new TilePath(BankSpot2);
    TilePath path = new TilePath((BankSpot));
    public final Tile[] MiningSpot = {
            new Tile(3381, 3268)
            , new Tile(3382, 3266)
            , new Tile(3367, 3266)
            , new Tile(3356, 3265)
            , new Tile(3350, 3265)
            , new Tile(3338, 3265)
            , new Tile(3333, 3265)
            , new Tile(3325, 3253)
            , new Tile(3321,3245)
            , new Tile(3313, 3235)
            , new Tile(3305, 3245)
            , new Tile(3305, 3255)
            , new Tile(3305, 3262)
            , new Tile(3300, 3279)
            , new Tile(3305, 3273)
            , new Tile(3301, 3296)
            , new Tile(3300, 3285)
            , new Tile(3298, 3300)
            , new Tile(3295, 3310)
    };
    TilePath path2 = new TilePath(MiningSpot);

    @Override
    public boolean onExecute() {
        strategies.add(new Mining());
        provide(strategies);
        return true;
    }

    @Override
    public void onFinish() {
    }

    private class Mining implements Strategy {
        boolean rock; // local variable to store the tree.
        boolean Drop;
        boolean Bank;

        @Override
        public boolean activate() {
            if (path2.hasReached() && !Inventory.isFull()) {
                rock = rock();
            } else {
                if (!path2.hasReached() && !Inventory.isFull() && Players.getMyPlayer().getAnimation() == -1) {
                    path2.traverse();
                    Time.sleep(2000);
                    path2.getNextTile();
                    Time.sleep(2000);
                    if(Players.getMyPlayer().getLocation().getX()==3313 && Players.getMyPlayer().getLocation().getY()==3235) {
                        for(SceneObject gate : SceneObjects.getNearest(3197,3198)){
                            if(gate.getLocation().getX() == 3312 || gate.getLocation().getY() ==3235 || gate.getLocation().getX() == 3311 || gate.getLocation().getY() ==3234){
                                gate.interact(SceneObjects.Option.OPEN);
                            } break;
                        }
                        }
                }
                if (Inventory.isFull() && Players.getMyPlayer().getAnimation() == -1) {
                    path.traverse();
                    path.getNextTile();
                    for(SceneObject gate : SceneObjects.getNearest(3197,3198)){
                        if(Players.getMyPlayer().getLocation().getX() ==3311 &&Players.getMyPlayer().getLocation().getY() ==3235){
                            if(gate.getLocation().getX() == 3312 || gate.getLocation().getY() ==3235 || gate.getLocation().getX() == 3311 || gate.getLocation().getY() ==3234){
                                gate.interact(SceneObjects.Option.OPEN);
                            }
                        } break;
                    }
                    Bank = Bank();
                }
            }
            return Players.getMyPlayer().getAnimation() == -1;
        }

        @Override
        public void execute() {
        }

        private boolean Drop() {
            for (Item ore : Inventory.getItems(ORE_ID)) {
                if (ore != null) {
                    ore.drop();
                    Time.sleep(1000);
                }
            }
            for (Item gem : Inventory.getItems(GEM_IDS)) {
                if (gem != null) {
                    gem.drop();
                    Time.sleep(1000);
                }
            }
            return Inventory.isFull();
        }

        private boolean rock() {
            if (!path2.hasReached() && Players.getMyPlayer().getAnimation() == -1 && !Inventory.isFull()) {
                path2.traverse();
            } else {
                if (path2.hasReached()) {
                    for (SceneObject rock : SceneObjects.getNearest(ROCK_IDS)) {
                        if (rock != null && Players.getMyPlayer().getAnimation() != 625 && rock.distanceTo() <= 1) {
                            rock.interact(SceneObjects.Option.MINE);
                            Time.sleep(1000);
                        }
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        private boolean Bank() {
            if (path3.hasReached()) {
                for (SceneObject Bank_Chest : SceneObjects.getNearest(3194)) {
                    Time.sleep(1250);
                    if (Bank_Chest != null && path3.hasReached() && Players.getMyPlayer().distanceTo() <= 1) {
                        Bank_Chest.interact(SceneObjects.Option.FIRST);
                        Time.sleep(1500);
                       // Menu.sendAction(679,0,0,4886);
                        //Time.sleep(5500);
                        //Menu.sendAction(315,0,0,2461);
                        //Time.sleep(5500);
                        //Menu.sendAction(679,47136768,215,972);
                        //Time.sleep(5500);
                        org.rev317.min.api.methods.Bank.depositAllExcept();
                        Time.sleep(2250);
                        org.rev317.min.api.methods.Bank.close();
                        Time.sleep(5000);
                    }
                    break;
                } for(SceneObject Bankchest1: SceneObjects.getNearest(2693)){
                    Time.sleep(1500);
                    if(Bankchest1 != null && path3.hasReached() && Players.getMyPlayer().distanceTo() <= 1){
                        Bankchest1.interact(SceneObjects.Option.FIRST);
                        Time.sleep(1500,5000);
                    } break;
                }
            }
                if (!path3.hasReached() && Inventory.isFull()
                        && Players.getMyPlayer().getAnimation() == -1) {
                    path3.traverse();
                    Time.sleep(1000);
                    path3.getNextTile();
                    Time.sleep(1250);
                    if(Players.getMyPlayer().getLocation().getX() != 3311 && (Players.getMyPlayer().getLocation().getY()!=3234) &&!path.hasReached()){
                        Time.sleep(1500,2000);
                    } else {
                        if(path.hasReached()&&Players.getMyPlayer().getLocation().getX() == 3311 && (Players.getMyPlayer().getLocation().getY()==3234 || Players.getMyPlayer().getLocation().getY()==3235)){
                            path3.traverse();
                            path3.getNextTile();
                            System.out.println("Path 3 has started");
                        }
                                if(!path3.hasReached() && Players.getMyPlayer().getLocation().getX() ==3311 && Players.getMyPlayer().getLocation().getY() ==3234
                                || Players.getMyPlayer().getLocation().getX() == 3311 || Players.getMyPlayer().getLocation().getY() ==3235){
                                    path3.traverse();
                                    path3.getNextTile();
                                }
                            }
                            Time.sleep(5000, 15000);


                        }

            if (Players.getMyPlayer().getAnimation() == -1
                    && !Inventory.isFull()
                    && !Inventory.contains(ORE_ID)) {
                return rock();
            } else {
                if (Inventory.isFull()) {
                    return Bank;

                }
                return true;
            }
        }
    }
}