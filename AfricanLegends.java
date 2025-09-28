import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class AfricanLegends {

    // Define initial fighter stats (Name, Health, Base Damage, Ability Cooldown, Rush Damage, Buff Amount, Curse Damage)
    private static final Map<String, int[]> BASE_STATS = new HashMap<>();
    static {
        // Cooldown: Mansa(3), Shaka(2), Tut(2), Nzinga(2), Hannibal(3)
        // Note: Cooldown of 0 means the ability is always available on that turn if no active buff is present.
        BASE_STATS.put("Mansa Musa", new int[]{100, 15, 3, 0, 0, 0}); // Wealth Heal (30 heal)
        BASE_STATS.put("Shaka Zulu", new int[]{100, 20, 2, 0, 0, 0});  // Double Damage (Duration: 1 turn)
        BASE_STATS.put("Tutankhamun", new int[]{100, 15, 2, 0, 0, 5});  // Pharaoh's Curse (5 damage DOT, 2 turns)
        BASE_STATS.put("Queen Nzinga", new int[]{100, 10, 2, 0, 10, 0}); // Inspire Buff (+10 damage, Duration: 1 turn)
        BASE_STATS.put("Hannibal Barca", new int[]{100, 15, 3, 40, 0, 0}); // Elephant Rush (40 damage)
    }

    /**
     * Represents a fighter character in the African Legends game.
     * This class is now nested inside AfricanLegendsGame to prevent duplicate class errors.
     */
    private static class Fighter {
        public String name;
        public int health;
        public int maxHealth;
        public int baseDamage;
        public String specialAbility;

        // Ability-specific fields
        public int cooldownRemaining;
        public boolean specialActive; // Used for Shaka Zulu (Double Damage) and Nzinga (Inspire Buff)
        public int abilityDuration;   // Used for Shaka Zulu and Nzinga buffs
        public int curseTurns;        // Used for Tutankhamun's Curse (on the target)
        public int curseDamage;
        public int buffAmount;
        public int rushDamage;

        /**
         * Constructor to initialize a fighter based on the game's base stats.
         */
        public Fighter(String name, int health, int damage, String ability, int cooldown, int rushDmg, int buffAmt, int curseDmg) {
            this.name = name;
            this.health = health;
            this.maxHealth = health;
            this.baseDamage = damage;
            this.specialAbility = ability;
            this.cooldownRemaining = cooldown;
            this.specialActive = false;
            this.abilityDuration = 0;
            this.curseTurns = 0;
            this.curseDamage = curseDmg;
            this.buffAmount = buffAmt;
            this.rushDamage = rushDmg;
        }

        /**
         * Performs a standard attack, applying active buffs/debuffs.
         */
        public void attack(Fighter target) {
            int finalDamage = baseDamage;
            String message = this.name + " attacks " + target.name;

            // --- Apply Buffs before damage calculation ---
            if (this.name.equals("Shaka Zulu") && this.specialActive) {
                finalDamage *= 2;
                message += " with DOUBLE DAMAGE!";
                this.abilityDuration--;
                if (this.abilityDuration <= 0) {
                    this.specialActive = false;
                }
            } else if (this.name.equals("Queen Nzinga") && this.specialActive) {
                finalDamage += this.buffAmount;
                message += " with an Inspired attack (+" + this.buffAmount + " damage)!";
                this.abilityDuration--;
                if (this.abilityDuration <= 0) {
                    this.specialActive = false;
                }
            }

            // Apply final damage
            target.health -= finalDamage;
            target.health = Math.max(0, target.health);

            System.out.println(message + " dealing " + finalDamage + " damage. " + target.name + " Health: " + target.health);
        }

        /**
         * Executes the fighter's unique special ability.
         */
        public void useAbility(Fighter opponent) {
            if (cooldownRemaining > 0) {
                System.out.println(this.name + "'s " + this.specialAbility + " is on cooldown. (" + cooldownRemaining + " turns remaining)");
                return; // Cannot use ability
            }

            System.out.print(this.name + " uses " + this.specialAbility + "!");

            switch (this.name) {
                case "Mansa Musa":
                    int healAmount = 30;
                    this.health = Math.min(this.maxHealth, this.health + healAmount);
                    this.cooldownRemaining = 3;
                    System.out.println(" Heals for " + healAmount + ". Health is now " + this.health + ".");
                    break;
                case "Shaka Zulu":
                    if (!this.specialActive) {
                        this.specialActive = true;
                        this.abilityDuration = 1; // Double damage applies on the NEXT attack (which is this turn's attack)
                        this.cooldownRemaining = 2; // Shaka's cooldown starts after the effect
                        System.out.println(" Prepares for a devastating Double Damage attack!");
                    }
                    break;
                case "Tutankhamun":
                    opponent.curseTurns = 2;
                    opponent.curseDamage = 5;
                    this.cooldownRemaining = 2;
                    System.out.println(" Curses " + opponent.name + "! Takes " + opponent.curseDamage + " damage for " + opponent.curseTurns + " turns.");
                    break;
                case "Queen Nzinga":
                    if (!this.specialActive) {
                        this.specialActive = true;
                        this.abilityDuration = 1; // Buff applies on the NEXT attack
                        this.buffAmount = 10;
                        this.cooldownRemaining = 2;
                        System.out.println(" Inspires her forces! Attack power increased for the next turn.");
                    }
                    break;
                case "Hannibal Barca":
                    opponent.health -= this.rushDamage;
                    opponent.health = Math.max(0, opponent.health);
                    this.cooldownRemaining = 3;
                    System.out.println(" Commands an Elephant Rush, dealing a massive " + this.rushDamage + " damage! " + opponent.name + " Health: " + opponent.health);
                    break;
            }
        }

        /**
         * Applies damage over time (DOT) effects like Tutankhamun's Curse.
         */
        public void applyDOTs() {
            if (this.curseTurns > 0) {
                this.health -= this.curseDamage;
                this.health = Math.max(0, this.health);
                this.curseTurns--;
                System.out.println(this.name + " suffers " + this.curseDamage + " damage from Pharaoh's Curse! (" + this.curseTurns + " turns left)");
            }
        }

        /**
         * Decrements ability cooldowns at the start of a turn.
         */
        public void decrementCooldown() {
            if (this.cooldownRemaining > 0) {
                this.cooldownRemaining--;
            }
        }
    }


    private Fighter player;
    private Fighter computer;
    private final Scanner scanner;
    private final Random random;

    public AfricanLegends() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    /**
     * Initializes and starts the game.
     */
    public void startGame() {
        System.out.println("--- African Legends: Battle for Supremacy ---");

        // Player Selection
        System.out.println("\nAvailable Champions:");
        int index = 1;
        String[] fighterNames = BASE_STATS.keySet().toArray(new String[0]);
        for (String name : fighterNames) {
            System.out.println(index++ + ". " + name);
        }

        String playerName;
        while (true) {
            System.out.print("Select your champion (enter name or number): ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= fighterNames.length) {
                    playerName = fighterNames[choice - 1];
                    break;
                }
            } else {
                playerName = scanner.nextLine().trim();
                if (BASE_STATS.containsKey(playerName)) {
                    break;
                }
            }
            System.out.println("Invalid selection. Please try again.");
        }

        // Computer Selection
        String computerName;
        do {
            computerName = fighterNames[random.nextInt(fighterNames.length)];
        } while (computerName.equals(playerName));

        // Initialize Fighters
        this.player = createFighter(playerName);
        this.computer = createFighter(computerName);

        System.out.println("\n--- BATTLE START: " + player.name + " vs. " + computer.name + " ---\n");
        gameLoop();
    }

    /**
     * Factory method to create a fighter instance.
     */
    private Fighter createFighter(String name) {
        int[] stats = BASE_STATS.get(name);
        // Stats: {Health, Damage, Cooldown, RushDmg, BuffAmt, CurseDmg}
        return new Fighter(
                name,
                stats[0], // Health
                stats[1], // Damage
                getAbilityName(name), // Ability Name
                stats[2], // Cooldown
                stats[3], // Rush Damage
                stats[4], // Buff Amount
                stats[5]  // Curse Damage
        );
    }

    /**
     * Gets the ability name from the fighter's name.
     */
    private String getAbilityName(String name) {
        switch (name) {
            case "Mansa Musa": return "Wealth Heal";
            case "Shaka Zulu": return "Double Damage";
            case "Tutankhamun": return "Pharaoh's Curse";
            case "Queen Nzinga": return "Inspire Buff";
            case "Hannibal Barca": return "Elephant Rush";
            default: return "Unknown Ability";
        }
    }

    /**
     * The main loop of the turn-based game.
     */
    public void gameLoop() {
        boolean playerTurn = true;

        while (player.health > 0 && computer.health > 0) {
            System.out.println("\n==============================================");

            if (playerTurn) {
                System.out.println("--- YOUR TURN: " + player.name + " (HP: " + player.health + ") ---");
                playerTurn(player, computer);
            } else {
                System.out.println("--- COMPUTER TURN: " + computer.name + " (HP: " + computer.health + ") ---");
                computerTurn(computer, player);
            }

            // Check for winner immediately after a move
            if (player.health <= 0) {
                declareWinner(computer);
                break;
            } else if (computer.health <= 0) {
                declareWinner(player);
                break;
            }

            // Switch turn
            playerTurn = !playerTurn;
        }
    }

    /**
     * Handles the player's action choice.
     */
    private void playerTurn(Fighter current, Fighter opponent) {
        // 1. Apply DOTs and decrement cooldowns at the start of the turn
        current.applyDOTs();
        opponent.decrementCooldown(); // Decrement opponent's cooldown

        System.out.println("Choose action: 1. Attack | 2. Use " + current.specialAbility);
        int choice = 0;

        while (choice != 1 && choice != 2) {
            System.out.print("Enter choice (1 or 2): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input.");
            }
        }

        if (choice == 1) {
            current.attack(opponent);
        } else if (choice == 2) {
            current.useAbility(opponent);
            // Note: The logic for Shaka Zulu and Queen Nzinga's buffs is handled inside their respective methods (useAbility sets the buff, attack consumes it).
            // This is just a placeholder comment from the previous version:
            /*
            if (current.specialAbility.equals("Shaka Zulu") || current.specialAbility.equals("Queen Nzinga")) {
                 // If the ability is a self-buff like Shaka or Nzinga, the turn is spent setting up.
            } else if (current.specialAbility.equals("Mansa Musa") && current.cooldownRemaining == 0) {
                // If Mansa Musa successfully healed (cooldown was 0), their turn is over.
            }
            */
        }
    }

    /**
     * Handles the computer's action (simple AI).
     */
    private void computerTurn(Fighter current, Fighter opponent) {
        // 1. Apply DOTs and decrement cooldowns at the start of the turn
        current.applyDOTs();
        opponent.decrementCooldown(); // Decrement opponent's cooldown

        int action;

        // Simple AI logic:
        if (current.specialAbility.equals("Wealth Heal") && current.health < 50 && current.cooldownRemaining == 0) {
            // Mansa Musa: Heal if health is low
            action = 2;
        } else if (current.cooldownRemaining == 0 && random.nextBoolean()) {
            // General: 50% chance to use ability if available
            action = 2;
        } else {
            // Default: Attack
            action = 1;
        }

        if (action == 1) {
            current.attack(opponent);
        } else if (action == 2) {
            current.useAbility(opponent);
            // If the ability was on cooldown, the computer will effectively skip its turn (or attempt to use ability and fail).
        }
    }

    /**
     * Declares the winner and ends the simulation.
     */
    private void declareWinner(Fighter winner) {
        System.out.println("\n==============================================");
        System.out.println("!!! GAME OVER !!!");
        System.out.println("The champion is: " + winner.name + "!");
        System.out.println("==============================================");
    }

    public static void main(String[] args) {
        AfricanLegends game = new AfricanLegends();
        game.startGame();
    }
}
