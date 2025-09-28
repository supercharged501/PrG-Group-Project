import javax.swing.*;
import java.awt.*;
import java.util.Random;

// ---------------- Fighters Class ----------------
class Fighters {
    private String name;
    private int health;
    private String attackMove;
    private String defenceMove;
    private String specialAbility;

    public Fighters(String name, int health, String attackMove, String defenceMove, String specialAbility) {
        this.name = name;
        this.health = health;
        this.attackMove = attackMove;
        this.defenceMove = defenceMove;
        this.specialAbility = specialAbility;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public String getAttackMove() { return attackMove; }
    public String getDefenceMove() { return defenceMove; }
    public String getSpecialAbility() { return specialAbility; }
}

// ---------------- Game GUI ----------------
public class GameGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Fighters[] fighters;
    private Fighters player1, player2;
    private boolean player1Selected = false;

    public GameGUI() {
        setTitle("⚔️ African Legends ⚔️");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load Fighters
        fighters = new Fighters[]{
                new Fighters("Menelik II", 100, "Mighty Blow", "Absorbs Damage", "Legendary Counterattack"),
                new Fighters("Cleopatra", 95, "Poison Strike", "Enemy Hesitates", "Inescapable Attack"),
                new Fighters("Mansa Musa", 100, "Wealth Punch", "Golden Shield", "Crushing Strike"),
                new Fighters("Shaka Zulu", 95, "Spear Thrust", "Raise Defences", "Devastating Attack"),
                new Fighters("Hannibal Barca", 90, "Crushing Blow", "War Elephant Defence", "Massive Attack"),
                new Fighters("Queen Nzinga", 95, "Punishing Slash", "Cunning Tactics", "No Mercy Strike")
        };

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenu(), "MainMenu");
        mainPanel.add(createInstructionsMenu(), "Instructions");
        mainPanel.add(createCharacterMenu(), "CharacterMenu");
        mainPanel.add(createCreditsMenu(), "Credits");

        setContentPane(mainPanel);
        setVisible(true);
    }

    // ---------- MAIN MENU ----------
    private JPanel createMainMenu() {
        JPanel panel = new JPanel(new BorderLayout(10,10));

        JLabel title = new JLabel("⚔️ African Legends ⚔️", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 34));
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4,1,10,10));
        JButton startButton = makeMenuButton("Start Game");
        JButton instructionsButton = makeMenuButton("Instructions");
        JButton creditsButton = makeMenuButton("Credits");
        JButton exitButton = makeMenuButton("Exit");

        startButton.addActionListener(e -> cardLayout.show(mainPanel, "CharacterMenu"));
        instructionsButton.addActionListener(e -> cardLayout.show(mainPanel, "Instructions"));
        creditsButton.addActionListener(e -> cardLayout.show(mainPanel, "Credits"));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(creditsButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    // ---------- INSTRUCTIONS ----------
    private JPanel createInstructionsMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel instructions = new JLabel(
                "<html><div style='text-align:center'><h2>Instructions</h2>"
                        + "1. Select a fighter for Player 1.<br>"
                        + "2. Select a fighter for Player 2.<br>"
                        + "3. Take turns attacking until one fighter’s health reaches zero.</div></html>",
                SwingConstants.CENTER);

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        panel.add(instructions, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- CREDITS ----------
    private JPanel createCreditsMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel credits = new JLabel(
                "<html><div style='text-align:center'><h2>Credits</h2>"
                        + "Concept: You<br>"
                        + "Programming: You<br>"
                        + "Design: You</div></html>",
                SwingConstants.CENTER);

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        panel.add(credits, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- CHARACTER MENU ----------
    private JPanel createCharacterMenu() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JLabel label = new JLabel("Choose Your Fighter", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 26));
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3,2,10,10));
        for (Fighters f : fighters) {
            JButton btn = makeMenuButton(f.getName());
            btn.addActionListener(e -> selectFighter(f));
            buttonPanel.add(btn);
        }

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- FIGHTER SELECTION ----------
    private void selectFighter(Fighters fighter) {
        if (!player1Selected) {
            player1 = fighter;
            player1Selected = true;
            JOptionPane.showMessageDialog(this, "Player 1 selected: " + fighter.getName());
        } else {
            player2 = fighter;
            JOptionPane.showMessageDialog(this, "Player 2 selected: " + fighter.getName());
            startCombat();
        }
    }

    // ---------- START COMBAT ----------
    private void startCombat() {
        JPanel combatPanel = new CombatPanel(player1, player2);
        mainPanel.add(combatPanel, "Combat");
        cardLayout.show(mainPanel, "Combat");
    }

    // ---------- MENU BUTTON UTILITY ----------
    private JButton makeMenuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        return b;
    }

    // ---------- COMBAT PANEL ----------
    class CombatPanel extends JPanel {
        private Fighters p1, p2;
        private int p1Health, p2Health;
        private JProgressBar p1Bar, p2Bar;
        private JTextArea log;
        private Random rnd = new Random();
        private boolean p1Turn = true;

        public CombatPanel(Fighters p1, Fighters p2) {
            this.p1 = p1; this.p2 = p2;
            this.p1Health = p1.getHealth();
            this.p2Health = p2.getHealth();

            setLayout(new BorderLayout());

            // Top panel (fighters + health bars)
            JPanel top = new JPanel(new GridLayout(2,2));
            top.add(new JLabel(p1.getName(), SwingConstants.CENTER));
            p1Bar = new JProgressBar(0, p1.getHealth());
            p1Bar.setValue(p1Health);
            top.add(p1Bar);

            top.add(new JLabel(p2.getName(), SwingConstants.CENTER));
            p2Bar = new JProgressBar(0, p2.getHealth());
            p2Bar.setValue(p2Health);
            top.add(p2Bar);
            add(top, BorderLayout.NORTH);

            // Combat log
            log = new JTextArea();
            log.setEditable(false);
            add(new JScrollPane(log), BorderLayout.CENTER);

            // Buttons
            JPanel buttons = new JPanel();
            JButton punchBtn = new JButton("Punch");
            JButton kickBtn = new JButton("Kick");
            JButton blockBtn = new JButton("Block");

            punchBtn.addActionListener(e -> doMove("Punch"));
            kickBtn.addActionListener(e -> doMove("Kick"));
            blockBtn.addActionListener(e -> doMove("Block"));

            buttons.add(punchBtn);
            buttons.add(kickBtn);
            buttons.add(blockBtn);
            add(buttons, BorderLayout.SOUTH);
        }

        private void doMove(String type) {
            Fighters attacker = p1Turn ? p1 : p2;
            Fighters defender = p1Turn ? p2 : p1;

            int damage = 0;
            if (type.equals("Punch")) damage = rnd.nextInt(15) + 10;
            else if (type.equals("Kick")) damage = rnd.nextInt(20) + 15;
            else if (type.equals("Block")) {
                log.append(attacker.getName() + " blocked!\n");
                p1Turn = !p1Turn;
                return;
            }

            if (attacker == p1) {
                p2Health -= damage;
                if (p2Health < 0) p2Health = 0;
                p2Bar.setValue(p2Health);
            } else {
                p1Health -= damage;
                if (p1Health < 0) p1Health = 0;
                p1Bar.setValue(p1Health);
            }

            log.append(attacker.getName() + " used " + type + " and dealt " + damage + " damage!\n");

            if (p1Health <= 0 || p2Health <= 0) {
                log.append("🏆 " + (p1Health > 0 ? p1.getName() : p2.getName()) + " WINS!\n");
            } else {
                p1Turn = !p1Turn; // switch turn
                log.append("It is now " + (p1Turn ? p1.getName() : p2.getName()) + "'s turn.\n");
            }
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
