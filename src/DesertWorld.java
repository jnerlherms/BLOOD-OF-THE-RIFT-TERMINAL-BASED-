import java.util.Scanner;
import java.util.Random;

public class DesertWorld {
    private Scanner input;
    private Character player;
    private ClearScreen screen;
    private GoToXY go;
    private DrawBox box;
    private Random random = new Random();
    private int mobsDefeated = 0;
    private final int MOBS_UNTIL_BOSS = 4;
    private SnowyIsland snowyIsland;

    public DesertWorld(Scanner input, Character player, ClearScreen screen, GoToXY go, DrawBox box, SnowyIsland snowyIsland) {
        this.input = input;
        this.player = player;
        this.screen = screen;
        this.go = go;
        this.box = box;
        this.snowyIsland = snowyIsland; 
    }

    public void explore() {
    
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg1 = "You step through the portal and are hit by a wave of searing, unnatural heat.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        screen.clear(1); // 4

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String msg2 = "This desert is wrong. The sand is gray with ash, and the sun is a hateful red eye.";
        String msg3 = "Legends speak of a corrupted tomb, a guardian... and a sacred blade lost in the sand.";
        go.move(105 - (msg2.length() / 2), 44);
        System.out.println(msg2);
        go.move(105 - (msg3.length() / 2), 46);
        System.out.println(msg3);
        screen.clear(1); // 8

        boolean inWorld = true;
        while (inWorld) {
            screen.clear(0);
            go.move(0, 37);
            box.draw(209, 18);
            displayPlayerStatus();

            go.move(95, 43);
            System.out.println("Where will you explore?");
            
            go.move(102, 47);
            System.out.println("[1]-North");
            go.move(111, 49);
            System.out.println("[2]-East");
            go.move(102, 51);
            System.out.println("[3]-South");
            go.move(94, 49);
            System.out.println("[4]-West");
            go.move(106, 49);

            int direction = input.nextInt();

            if (direction >= 1 && direction <= 4) {
                if (random.nextInt(100) < 75) {  // 75% encounter
                    triggerEncounter(direction); 
                    if (!player.isAlive()) {
                        inWorld = false; 
                    }
                } else {
                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String directionString = "";
                    switch (direction) {
                        case 1: directionString = "You climb over the buried ruins of an old city."; break;
                        case 2: directionString = "You walk east, the sun glinting off sharp, black glass."; break;
                        case 3: directionString = "You trudge south through endless, choking dunes of ash."; break;
                        case 4: directionString = "You head west into a field of colossal, sun-bleached bones."; break;
                    }

                    int centerXDir = 105 - (directionString.length() / 2);
                    go.move(centerXDir, 45);
                    System.out.println(directionString);
                    screen.clear(1); // 2

                    screen.clear(0);
                    go.move(0, 37);
                    box.draw(209, 18);
                    displayPlayerStatus();

                    String wanderString = "You wander the desert but find nothing of interest.";
                    int centerXWander = 105 - (wanderString.length() / 2);
                    go.move(centerXWander, 45);
                    System.out.println(wanderString);
                    screen.clear(1); // 3
                }
            } else {
                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();
                go.move(84, 45);
                System.out.println("Invalid direction. Please choose 1-4.");    
                screen.clear(1); // 3
            }
        }
    }

    private void triggerEncounter(int direction) {
        World1Mob mob; 

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String directionString = "";
        switch (direction) {
            case 1: directionString = "You climb over the buried ruins..."; break;
            case 2: directionString = "You walk east into the canyons..."; break;
            case 3: directionString = "You trudge south through the ash..."; break;
            case 4: directionString = "You head west into the bone fields..."; break;
        }

        int centerXDir = 105 - (directionString.length() / 2);
        go.move(centerXDir, 45);
        System.out.println(directionString);
        screen.clear(1); // 2

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        if (mobsDefeated >= MOBS_UNTIL_BOSS) {
            mob = new World2Mob.GiantWorm();
            String msg = "A sarcophagus bursts from the sand! The Desert Boss " + mob.name + " has appeared!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        } else {
            int mobType = random.nextInt(3);
            if (mobType == 0) {
                mob = new World2Mob.Spider();
            } else if (mobType == 1) {
                mob = new World2Mob.Snake();
            } else {
                mob = new World2Mob.Mummy(); // Changed from Mummy
            }

            String msg = "A " + mob.name + " ambushes you from the sand!";
            int centerXMsg = 105 - (msg.length() / 2);
            go.move(centerXMsg, 45);
            System.out.println(msg);
        }

        screen.clear(1); // 2

        Battle battle = new Battle(input, player, mob, screen, go, box);
        boolean playerWon = battle.start();

        if (playerWon) {
            if(mob instanceof World2Mob.GiantWorm) { 
                playWorld2Outro(); 

                screen.clear(0);
                go.move(0, 37);
                box.draw(209, 18);
                displayPlayerStatus();

                String msg1 = "The portal to the Snowy Island is open.";
                String msg2 = "What will you do?";
                String opt1 = "[1] Enter the portal";
                String opt2 = "[2] Return home to rest";
                
                go.move(105 - (msg1.length() / 2), 43);
                System.out.println(msg1);
                go.move(105 - (msg2.length() / 2), 45);
                System.out.println(msg2);

                go.move(105 - (opt1.length() / 2), 48);
                System.out.println(opt1);
                go.move(105 - (opt2.length() / 2), 49);
                System.out.println(opt2);

                go.move(106, 51);
                int choice = input.nextInt();

                if(choice == 1) {
                    snowyIsland.explore();
                } else {
                    boolean isResting = true;
                    while(isResting){
                        
                        screen.clear(0);
                        go.move(0, 37);
                        box.draw(209, 18);
                        displayPlayerStatus(); 

                        String restMsg1 = "You are resting at home.";
                        String restMsg2 = "Are you ready for your next adventure?";
                        String restOpt = "[1] Yes, proceed to the Snowy Island";

                        go.move(105 - (restMsg1.length() / 2), 44);
                        System.out.println(restMsg1);
                        go.move(105 - (restMsg2.length() / 2), 46);
                        System.out.println(restMsg2);
                        go.move(105 - (restOpt.length() / 2), 49);
                        System.out.println(restOpt);

                        go.move(106, 51); 
                        int restChoice = input.nextInt();

                        if (restChoice == 1) {
                            isResting = false; 
                            snowyIsland.explore(); 
                        } else {
                            screen.clear(0);
                            go.move(0, 37);
                            box.draw(209, 18);
                            displayPlayerStatus();
                            String waitMsg = "You take a little more time to rest...";
                            go.move(105 - (waitMsg.length() / 2), 45);
                            System.out.println(waitMsg);
                            screen.clear(1); // 3
                        }
                    }
                }
            } else {
                mobsDefeated++;
                openRewardChest();
            }
        } else {
            screen.clear(0);
            go.move(98, 27);
            System.out.println("You have been defeated...");
            screen.clear(1); // 5    
            System.exit(0);
        }
    }

    private void openRewardChest() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();
        
        String title = "You defeated the enemy! Choose your reward:";
        go.move(105 - (title.length() / 2), 43);
        System.out.println(title);

        String opt1 = "[1] Healing Potion (Restore all HP)";
        String opt2 = "[2] Mana Elixir (Restore all Mana)";
        String opt3 = "[3] Whetstone (Gain +15 Temp. Damage for 3 battles)";

        go.move(105 - (opt1.length() / 2), 46);
        System.out.println(opt1);
        go.move(105 - (opt2.length() / 2), 47);
        System.out.println(opt2);
        go.move(105 - (opt3.length() / 2), 48);
        System.out.println(opt3);

        go.move(106, 50);
        int choice = input.nextInt();

        screen.clearLine(go, 70, 43, 80); // Clear title
        screen.clearLine(go, 70, 46, 80); // Clear opt 1
        screen.clearLine(go, 70, 47, 80); // Clear opt 2
        screen.clearLine(go, 70, 48, 80); // Clear opt 3

        String msg = "";
        switch (choice) {
            case 1:
                player.hp = player.maxHp;
                msg = "You feel invigorated! HP restored to full!";
                break;
            case 2:
                player.mana = player.maxMana;
                msg = "Your mind clears! Mana restored to full!";
                break;
            case 3:
                player.addTemporaryDamage(15);
                msg = "Your weapon glows! Damage increased for the next 3 battles!";
                break;
            default:
                player.hp = player.maxHp;
                msg = "You fumbled but found a Healing Potion! HP restored to full!";
                break;
        }

        go.move(105 - (msg.length() / 2), 45);
        System.out.println(msg);
        screen.clear(1); // Pause for 4 seconds
    }

    private void displayPlayerStatus() {
        final String GREEN = "\u001B[1;92m";
        final String BLUE = "\u001B[36m";
        final String RESET = "\u001B[0m";

        go.move(93, 24);
        System.out.print("HP: ");
        System.out.print(GREEN + player.hp + "/" + player.maxHp + RESET);
        System.out.print("    MP:"); 
        System.out.println(BLUE + player.mana + "/" + player.maxMana + RESET);

        switch (player.className){
            case "Warrior":
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Warrior(102, 20);
                break;
            case "Paladin":
                go.move(103, 18);
                System.out.printf("%s", player.name);
                CharacterIcon.Paladin(105, 20);
                break;
            case "Mage":
                go.move(103, 17);
                System.out.printf("%s", player.name);
                CharacterIcon.Mage(104, 19);
                break;
        }
    }

    private void playWorld2Outro() {
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg1 = "The Mummy crumbles to dust, and the red haze over the desert lifts.";
        go.move(105 - (msg1.length() / 2), 44);
        System.out.println(msg1);
        screen.clear(1); // 4

        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg2 = "As the cursed sand settles, you see a glint from the Mummy's tomb.";
        String msg3 = "It's the Blade of Ashrock... but it's cold, its fire long dead.";
        go.move(105 - (msg2.length() / 2), 44);
        System.out.println(msg2);
        go.move(105 - (msg3.length() / 2), 46);
        System.out.println(msg3);
        screen.clear(1); // 6
        
        screen.clear(0);
        go.move(0, 37);
        box.draw(209, 18);
        displayPlayerStatus();

        String msg4 = "Suddenly, the air shimmers and freezes. A portal of frost and ice appears.";
        String msg5 = "You hear a cold, merciless voice echo from it... Silence. Stillness. Order.";
        go.move(105 - (msg4.length() / 2), 44);
        System.out.println(msg4);
        go.move(105 - (msg5.length() / 2), 46);
        System.out.println(msg5);
        screen.clear(1); // 7
    }
}