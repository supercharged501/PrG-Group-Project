import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class AfricanLegends extends JFrame {

    // ---------------- Fighters Class ----------------
    static class Fighter {
        String name;
        int health;
        int maxHealth;
        String attackMove;
        String defenceMove;
        String specialAbility;
        int abilityCooldown;        // number of turns before special can be reused
        int cooldownRemaining = 0;  // turns left until ability ready
        boolean specialBuffActive = false; // e.g. Shaka double damage or Nzinga buff
        int buffDuration = 0;      // how many attacks buff lasts
        int curseTurns = 0;        // DOT turns if cursed
        int curseDamage = 0;       // DOT amount
        int rushDamage = 0;        // e.g. Hannibal

        Fighter(String name, int health, String attackMove, String defenceMove,
                String specialAbility, int abilityCooldown) {
            this.name = name;
            this.health = this.maxHealth = health;
            this.attackMove = attackMove;
            this.defenceMove = defenceMove;
            this.specialAbility = specialAbility;
            this.abilityCooldown = abilityCooldown;
        }

        boolean isAlive() { return health > 0; }
    }

    // Game State
    private Fighter[] fighters;
    private Fighter player;
    private Fighter computer;
    private boolean playerTurn = true;
    private boolean gameActive = false;
    private Random rnd = new Random();

    // Overall UI Components
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Sprite / Background Requirements
    private JLabel playerSprite;
    private JLabel computerSprite;
    private JLabel backgroundLabel; // for background images (optional)

    // Selection screen components
    private JComboBox<String> fighterDropdown;

    // Combat screen components
    private JProgressBar playerHealthBar;
    private JProgressBar computerHealthBar;
    private JTextArea battleLog;
    private JTextField commandInput;

    public AfricanLegends() {
        setTitle("African Legends");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initFighters();
        initUI();
    }

    private void initFighters() {
        fighters = new Fighter[] {
                new Fighter("Menelik II", 100, "Mighty Blow", "Absorb", "Legendary Counterattack", 3),
                new Fighter("Cleopatra", 95, "Poison Strike", "Enemy Hesitation", "Inescapable Attack", 3),
                new Fighter("Mansa Musa", 100, "Wealth Punch", "Golden Shield", "Wealth Heal", 3),
                new Fighter("Shaka Zulu", 95, "Spear Thrust", "Raise Defences", "Double Damage", 2),
                new Fighter("Hannibal Barca", 90, "Crushing Blow", "War Elephant Defence", "Elephant Rush", 3),
                new Fighter("Queen Nzinga", 95, "Punishing Slash", "Cunning Tactics", "Inspire Buff", 2)
        };

        for (Fighter f : fighters) {
            if (f.name.equals("Hannibal Barca")) f.rushDamage = 40;
        }
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenu(), "MainMenu");
        mainPanel.add(createInstructions(), "Instructions");
        mainPanel.add(createCharacterSelect(), "CharacterSelect");
        mainPanel.add(createCombatPanel(), "Combat");

        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    // ---------------- Main Menu ----------------
    private JPanel createMainMenu() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("AFRICAN LEGENDS", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(new Color(200, 140, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 12, 12));
        JButton start = new JButton("Start Game");
        JButton instructions = new JButton("Instructions");
        JButton credits = new JButton("Credits");
        JButton exit = new JButton("Exit");

        start.addActionListener(e -> cardLayout.show(mainPanel, "CharacterSelect"));
        instructions.addActionListener(e -> cardLayout.show(mainPanel, "Instructions"));
        credits.addActionListener(e -> showCreditsDialog());
        exit.addActionListener(e -> System.exit(0));

        center.add(start);
        center.add(instructions);
        center.add(credits);
        center.add(exit);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void showCreditsDialog() {
        JOptionPane.showMessageDialog(this,
                "African Legends\n\nConcept & Programming: James W. Nangombe & Gregory-Shawn M. Masule\nGUI: Justina Kathindi, Edward P. Michael \nSprites and Backgrounds: Anna Toivo K.T \nArrays and Character Assets: Lyile K. Paulus",
                "Credits", JOptionPane.INFORMATION_MESSAGE);
    }

    //  Instructions (Updated for Text Input)
    private JPanel createInstructions() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea t = new JTextArea();
        t.setEditable(false);
        t.setText(
                "Instructions:\n\n\n" +
                        "1) Choose a fighter then start the battle.\n" +
                        "2) **Input Commands** in the text box below the Battle Log:\n" +
                        "    Type --1-- for PUNCH\n" +
                        "    Type --2-- for KICK\n" +
                        "    Type --9-- for BLOCK\n" +
                        "    Type --0-- for SPECIAL\n" +
                        "    Blocks half of the incoming damage for that enemy's attack.\n" +
                        "    Special is unique to each fighter and has cooldown.\n" +
                        "3) Players take turns: you act first, then computer.\n" +
                        "4) First to reduce opponent HP to 0 wins.\n\n" +
                        "Controls: Type your command (1, 2, 9, or 0) and press Enter. Enjoy!"
        );
        t.setFont(new Font("Monospaced", Font.PLAIN, 16));
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        p.add(back, BorderLayout.SOUTH);
        return p;
    }

    // Character Selection
    private JPanel createCharacterSelect() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Select Your Fighter", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        p.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        fighterDropdown = new JComboBox<>();
        for (Fighter f : fighters) fighterDropdown.addItem(f.name);
        fighterDropdown.setMaximumSize(new Dimension(400, 40));
        fighterDropdown.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton confirm = new JButton("Confirm Selection (Player vs Computer)");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirm.addActionListener(e -> {
            String selected = (String) fighterDropdown.getSelectedItem();
            if (selected != null) {
                setupBattleFor(selected);
                cardLayout.show(mainPanel, "Combat");
            }
        });

        center.add(Box.createVerticalGlue());
        center.add(fighterDropdown);
        center.add(Box.createRigidArea(new Dimension(0, 18)));
        center.add(confirm);
        center.add(Box.createVerticalGlue());

        p.add(center, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        p.add(back, BorderLayout.SOUTH);

        return p;
    }

    // The Combat Panel (Contains GridBagLayout and Text Input)
    private JPanel createCombatPanel() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900, 600));

        // Background label (bottom layer)
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 900, 600);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // the actual combat UI
        JPanel combatUI = new JPanel(new GridBagLayout());
        combatUI.setOpaque(false);
        combatUI.setBounds(0, 0, 900, 600);
        combatUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JPanel healthBarsPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        healthBarsPanel.setOpaque(false);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setOpaque(false);
        JLabel playerName = new JLabel("Player", SwingConstants.CENTER);
        playerName.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerPanel.add(playerName, BorderLayout.NORTH);
        playerHealthBar = new JProgressBar();
        playerHealthBar.setStringPainted(true);
        playerPanel.add(playerHealthBar, BorderLayout.CENTER);

        JPanel computerPanel = new JPanel(new BorderLayout());
        computerPanel.setOpaque(false);
        JLabel compName = new JLabel("Computer", SwingConstants.CENTER);
        compName.setFont(new Font("SansSerif", Font.BOLD, 18));
        computerPanel.add(compName, BorderLayout.NORTH);
        computerHealthBar = new JProgressBar();
        computerHealthBar.setStringPainted(true);
        computerPanel.add(computerHealthBar, BorderLayout.CENTER);

        healthBarsPanel.add(playerPanel);
        healthBarsPanel.add(computerPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        combatUI.add(healthBarsPanel, gbc);


        JPanel spritesPlaceholder = new JPanel(new GridLayout(1, 2));
        spritesPlaceholder.setOpaque(false);
        spritesPlaceholder.setPreferredSize(new Dimension(880, 200));

        playerSprite = new JLabel("Player", SwingConstants.CENTER);
        computerSprite = new JLabel("Computer", SwingConstants.CENTER);
        playerSprite.setVerticalTextPosition(SwingConstants.BOTTOM);
        playerSprite.setHorizontalTextPosition(SwingConstants.CENTER);
        computerSprite.setVerticalTextPosition(SwingConstants.BOTTOM);
        computerSprite.setHorizontalTextPosition(SwingConstants.CENTER);

        spritesPlaceholder.add(playerSprite);
        spritesPlaceholder.add(computerSprite);

        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.BOTH;
        combatUI.add(spritesPlaceholder, gbc);



        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setPreferredSize(new Dimension(880, 150));
        logScroll.setOpaque(false);
        logScroll.getViewport().setOpaque(false);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        combatUI.add(logScroll, gbc);


        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel commandLabel = new JLabel("Enter command (1-Punch, 2-Kick, 9-Block, 0-Special): ");
        commandInput = new JTextField(10);

        // Add the action listener to process the command on Enter
        commandInput.addActionListener(e -> handlePlayerInput(commandInput.getText().trim()));

        actionsPanel.add(commandLabel, BorderLayout.WEST);
        actionsPanel.add(commandInput, BorderLayout.CENTER);

        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        combatUI.add(actionsPanel, gbc);


        layeredPane.add(combatUI, Integer.valueOf(1));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(layeredPane, BorderLayout.CENTER);
        return wrapper;
    }

    // handles user input
    private void handlePlayerInput(String input) {
        if (!playerTurn || !gameActive) {
            commandInput.setText("");
            return;
        }

        boolean actionTaken = false;
        switch (input) {
            case "1":
                playerActionPunch();
                actionTaken = true;
                break;
            case "2":
                playerActionKick();
                actionTaken = true;
                break;
            case "9":
                playerActionBlock();
                actionTaken = true;
                break;
            case "0":
                // playerActionSpecial handles the end of turn if successful
                playerActionSpecial();
                return;
            default:
                log("Invalid command. Use 1 (Punch), 2 (Kick), 9 (Block), or 0 (Special).");
                break;
        }

        if (actionTaken) {
            commandInput.setText(""); // Clear input after successful action
        }
    }

// Sprites and backgrounds

    private void loadSprites() {
        playerSprite.setIcon(loadSpriteFor(player.name));
        playerSprite.setText(player.name);
        computerSprite.setIcon(loadSpriteFor(computer.name));
        computerSprite.setText(computer.name);
    }

    private ImageIcon loadSpriteFor(String fighterName) {
        String path = "/sprites/" + fighterName.toLowerCase().replaceAll(" ", "") + ".png";
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.out.println("Sprite not found: " + path);
            return null;
        }
    }

    private void setBackgroundImage(String backgroundName) {
        String path = "/backgrounds/" + backgroundName.toLowerCase() + ".png";
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            backgroundLabel.setIcon(new ImageIcon(img));
        } else {
            backgroundLabel.setIcon(null);
            System.out.println("Background not found: " + path);
        }
    }

    // Sets up Battle
    private void setupBattleFor(String playerChoice) {
        player = cloneFighter(findByName(playerChoice));

        List<Fighter> options = new ArrayList<>();
        for (Fighter f : fighters) if (!f.name.equals(player.name)) options.add(f);
        int idx = rnd.nextInt(options.size());
        computer = cloneFighter(options.get(idx));

        playerTurn = true;
        gameActive = true;

        player.cooldownRemaining = 0; player.specialBuffActive = false; player.buffDuration = 0; player.curseTurns = 0; player.curseDamage = 0;
        computer.cooldownRemaining = 0; computer.specialBuffActive = false; computer.buffDuration = 0; computer.curseTurns = 0; computer.curseDamage = 0;

        String[] backgrounds = {"savannah","desert","jungle","mountains"};
        setBackgroundImage(backgrounds[rnd.nextInt(backgrounds.length)]);

        updateHealthBars();
        battleLog.setText("");
        log("*** BATTLE START: " + player.name + " vs " + computer.name + " ***");
        log("It is your turn. Enter command (1-Punch, 2-Kick, 9-Block, 0-Special).");
        updateButtons();
        loadSprites();

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private Fighter findByName(String name) {
        for (Fighter f : fighters) if (f.name.equals(name)) return f;
        return fighters[0];
    }

    private Fighter cloneFighter(Fighter src) {
        Fighter f = new Fighter(src.name, src.maxHealth, src.attackMove, src.defenceMove, src.specialAbility, src.abilityCooldown);
        f.rushDamage = src.rushDamage;
        f.curseDamage = src.curseDamage;
        return f;
    }

    // Player Actions and decisions

    private void playerActionPunch() {
        if (!playerTurn || !gameActive) return;
        int damage = computePunchDamage(true);
        log(player.name + " throws a PUNCH and deals " + damage + " damage!");
        applyDamageToComputer(damage);
        endPlayerTurn();
    }

    private void playerActionKick() {
        if (!playerTurn || !gameActive) return;
        int damage = computeKickDamage(true);
        log(player.name + " performs a KICK and deals " + damage + " damage!");
        applyDamageToComputer(damage);
        endPlayerTurn();
    }

    private void playerActionBlock() {
        if (!playerTurn || !gameActive) return;
        log(player.name + " blocks and will reduce next incoming damage by half.");
        player.buffDuration = -1; // -1 indicates block active for next incoming attack
        endPlayerTurn();
    }

    private void playerActionSpecial() {
        if (!playerTurn || !gameActive) return;
        if (player.cooldownRemaining > 0) {
            log(player.name + "'s special is on cooldown (" + player.cooldownRemaining + " turns left).");
            return;
        }
        boolean used = useSpecial(player, computer);
        if (used) {
            updateHealthBars();
            commandInput.setText(""); // Clear input on successful use
            endPlayerTurn();
        }
    }

    //  Damage calculations

    private int computePunchDamage(boolean isPlayer) {
        int damage = 0;
        int roll = rnd.nextInt(9) + 1; // 1..9
        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                for (int i = 1; i <= 15; i++) damage++;
                break;
            case 7:
                for (int i = 1; i <= 28; i++) damage++;
                break;
            case 8:
                for (int i = 1; i <= 30; i++) damage++;
                break;
            case 9:
                for (int i = 1; i <= 45; i++) damage++;
                break;
            default:
                break;
        }

        Fighter attacker = isPlayer ? player : computer;
        if (attacker.specialBuffActive) {
            damage *= 2;
            attacker.buffDuration--;
            if (attacker.buffDuration <= 0) {
                attacker.specialBuffActive = false;
                log(attacker.name + "'s buff faded.");
            }
        }
        return damage;
    }

    private int computeKickDamage(boolean isPlayer) {
        int damage = 0;
        int roll = rnd.nextInt(5) + 1; // 1..5
        switch (roll) {
            case 1:
            case 2:
                for (int i = 1; i <= 15; i++) damage++;
                break;
            case 3:
                for (int i = 1; i <= 28; i++) damage++;
                break;
            case 4:
                for (int i = 1; i <= 30; i++) damage++;
                break;
            case 5:
                for (int i = 1; i <= 35; i++) damage++;
                break;
            default:
                break;
        }

        Fighter attacker = isPlayer ? player : computer;
        if (attacker.specialBuffActive) {
            damage += 10;
            attacker.buffDuration--;
            if (attacker.buffDuration <= 0) {
                attacker.specialBuffActive = false;
                log(attacker.name + "'s buff faded.");
            }
        }
        return damage;
    }

    //  Applies damage & blocking
    private void applyDamageToComputer(int dmg) {
        if (computer.buffDuration == -1) {
            dmg = dmg / 2;
            computer.buffDuration = 0;
            log(computer.name + " blocked and reduced damage!");
        }
        if (computer.specialBuffActive && computer.buffDuration > 0 && computer.name.equals("Menelik II")) {
            int counter = dmg / 2;
            player.health = Math.max(0, player.health - counter);
            log(computer.name + " counters and returns " + counter + " damage to " + player.name + "!");
            computer.specialBuffActive = false;
            computer.buffDuration = 0;
        }
        computer.health = Math.max(0, computer.health - dmg);
        updateHealthBars();
        checkForEnd();
    }

    private void applyDamageToPlayer(int dmg) {
        if (player.buffDuration == -1) {
            dmg = dmg / 2;
            player.buffDuration = 0;
            log(player.name + " blocked and reduced damage!");
        }
        if (player.specialBuffActive && player.buffDuration > 0 && player.name.equals("Menelik II")) {
            int counter = dmg / 2;
            computer.health = Math.max(0, computer.health - counter);
            log(player.name + " counters and returns " + counter + " damage to " + computer.name + "!");
            player.specialBuffActive = false;
            player.buffDuration = 0;
        }
        player.health = Math.max(0, player.health - dmg);
        updateHealthBars();
        checkForEnd();
    }

    // Special moves
    private boolean useSpecial(Fighter user, Fighter opponent) {
        log(user.name + " attempts to use SPECIAL: " + user.specialAbility + "!");
        switch (user.name) {
            case "Mansa Musa": // Heal
                int heal = 40;
                user.health = Math.min(user.maxHealth, user.health + heal);
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " heals for " + heal + " HP. Now at " + user.health + ".");
                return true;
            case "Shaka Zulu": // Double damage next attack
                user.specialBuffActive = true;
                user.buffDuration = 1;
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " activates DOUBLE DAMAGE for the next attack!");
                return true;
            case "Hannibal Barca": // Elephant Rush instant damage
                int rush = user.rushDamage > 0 ? user.rushDamage : 30;
                opponent.health = Math.max(0, opponent.health - rush);
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " commands ELEPHANT RUSH for " + rush + " damage!");
                updateHealthBars();
                return true;
            case "Queen Nzinga": // Inspire buff (adds flat damage next attack)
                user.specialBuffActive = true;
                user.buffDuration = 1;
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " inspires her forces! Next attack stronger.");
                return true;
            case "Cleopatra": // Powerful single-strike: add large damage
                int cpDmg = 35;
                opponent.health = Math.max(0, opponent.health - cpDmg);
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " uses Inescapable Attack for " + cpDmg + " damage!");
                updateHealthBars();
                return true;
            case "Menelik II": // Counterattack style: next incoming damage returns
                user.specialBuffActive = true;
                user.buffDuration = 1;
                user.cooldownRemaining = user.abilityCooldown;
                log(user.name + " prepares Legendary Counterattack!");
                return true;
            default:
                log("Special not implemented for " + user.name);
                return false;
        }
    }

    // Ends turn and initiates computer AI ----------------
    private void endPlayerTurn() {
        playerTurn = false;
        decrementCooldowns();
        updateButtons();
        if (!gameActive) return;

        javax.swing.Timer t = new javax.swing.Timer(700, e -> {
            ((javax.swing.Timer)e.getSource()).stop();
            computerAct();
        });
        t.setRepeats(false);
        t.start();
    }

    private void decrementCooldowns() {
        if (player.cooldownRemaining > 0) player.cooldownRemaining--;
        if (computer.cooldownRemaining > 0) computer.cooldownRemaining--;
    }

    private void computerAct() {
        if (!gameActive) return;

        if (computer.curseTurns > 0) {
            computer.health = Math.max(0, computer.health - computer.curseDamage);
            computer.curseTurns--;
            log(computer.name + " suffers " + computer.curseDamage + " damage from a curse. (" + computer.curseTurns + " turns left)");
            updateHealthBars();
            if (checkForEnd()) return;
        }

        boolean usedAbility = false;
        if (computer.cooldownRemaining == 0) {
            if (computer.name.equals("Mansa Musa") && computer.health <= computer.maxHealth - 20) {
                useSpecial(computer, player);
                usedAbility = true;
            } else if (computer.name.equals("Hannibal Barca") && player.health > 0) {
                if (rnd.nextDouble() < 0.5) { useSpecial(computer, player); usedAbility = true; }
            } else if (rnd.nextDouble() < 0.25) {
                useSpecial(computer, player);
                usedAbility = true;
            }
        }

        if (!usedAbility) {
            int choice = rnd.nextInt(100);
            if (choice < 45) {
                int dmg = computePunchDamage(false);
                log(computer.name + " throws a PUNCH dealing " + dmg + " damage!");
                applyDamageToPlayer(dmg);
            } else if (choice < 80) {
                int dmg = computeKickDamage(false);
                log(computer.name + " performs a KICK dealing " + dmg + " damage!");
                applyDamageToPlayer(dmg);
            } else {
                computer.buffDuration = -1;
                log(computer.name + " blocks and will halve next incoming damage!");
            }
        }

        playerTurn = true;
        decrementCooldowns();
        updateButtons();
        if (gameActive) log("It is your turn. Enter command (1-Punch, 2-Kick, 9-Block, 0-Special).");
    }

    // Updates UI / buttons / health
    private void updateHealthBars() {
        if (player != null) {
            playerHealthBar.setMaximum(player.maxHealth);
            playerHealthBar.setValue(player.health);
            playerHealthBar.setString(player.name + " HP: " + player.health + "/" + player.maxHealth);
        }
        if (computer != null) {
            computerHealthBar.setMaximum(computer.maxHealth);
            computerHealthBar.setValue(computer.health);
            computerHealthBar.setString(computer.name + " HP: " + computer.health + "/" + computer.maxHealth);
        }
        updateButtons();
    }

    private void updateButtons() {
        if (gameActive && playerTurn) {
            commandInput.setEnabled(true);
            commandInput.requestFocusInWindow();
        } else {
            commandInput.setEnabled(false);
        }
    }

    // Checks end of game
    private boolean checkForEnd() {
        if (player.health <= 0 && computer.health <= 0) {
            log("*** DRAW! Both fighters fell! ***");
            showGameOver("DRAW");
            return true;
        } else if (computer.health <= 0) {
            log("*** " + player.name + " WINS! ***");
            showGameOver(player.name + " Wins!");
            return true;
        } else if (player.health <= 0) {
            log("*** " + computer.name + " WINS! ***");
            showGameOver(computer.name + " Wins!");
            return true;
        }
        updateHealthBars();
        return false;
    }

    // What Happens when the game ends
    private void showGameOver(String winnerText) {
        gameActive = false;
        updateButtons();

        JPanel gameOverPanel = new JPanel(new BorderLayout(12,12));
        JLabel msg = new JLabel(winnerText, SwingConstants.CENTER);
        msg.setFont(new Font("Serif", Font.BOLD, 36));
        gameOverPanel.add(msg, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton playAgain = new JButton("Play Again");
        JButton toMenu = new JButton("Main Menu");
        JButton exit = new JButton("Exit");

        playAgain.addActionListener(e -> {
            for (Fighter f : fighters) f.health = f.maxHealth;
            cardLayout.show(mainPanel, "CharacterSelect");
        });
        toMenu.addActionListener(e -> {
            for (Fighter f : fighters) f.health = f.maxHealth;
            cardLayout.show(mainPanel, "MainMenu");
        });
        exit.addActionListener(e -> System.exit(0));

        btns.add(playAgain);
        btns.add(toMenu);
        btns.add(exit);
        gameOverPanel.add(btns, BorderLayout.SOUTH);

        mainPanel.add(gameOverPanel, "GameOver");
        cardLayout.show(mainPanel, "GameOver");
    }

    // Utilities
    private void log(String s) {
        battleLog.append(s + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    // Main Class
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AfricanLegends game = new AfricanLegends();
            game.setVisible(true);
        });
    }
}
