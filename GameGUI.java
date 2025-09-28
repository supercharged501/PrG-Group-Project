import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

class LoadingScreen extends JFrame {
    public LoadingScreen() {
        setUndecorated(true); // no title bar
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("  African Legends  ", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 30));
        panel.add(label, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // loading animation
        panel.add(progressBar, BorderLayout.CENTER);

        JLabel loading = new JLabel("Loading African Legends...", JLabel.CENTER);
        panel.add(loading, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);

        // Close after 4 seconds and open game
        new javax.swing.Timer(4000, e -> {
            ((javax.swing.Timer) e.getSource()).stop(); // stop repeating
            dispose();
            new GameGUI();
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoadingScreen::new);
    }
}

public class GameGUI extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;

    public GameGUI() {
        setTitle("⚔️ African Legends ⚔️");
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Change Window Icon to AFL text ---
        setIconImage(createTextIcon("AFL", new Font("Serif", Font.BOLD, 24), Color.BLACK));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenu(), "MainMenu");
        mainPanel.add(createInstructionsMenu(), "Instructions");
        mainPanel.add(createCharacterMenu(), "CharacterMenu");
        mainPanel.add(createCreditsMenu(), "Credits");

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createMainMenu() {
        BackgroundPanel panel = new BackgroundPanel("/images/main_bg.jpg", "AFL");
        panel.setLayout(new BorderLayout(10,10));

        JLabel title = new JLabel("⚔️ African Legends ⚔️", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(Color.black);
        title.setOpaque(false);
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4,1,10,10));
        buttonPanel.setOpaque(false);

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

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(buttonPanel);

        panel.add(centerWrap, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInstructionsMenu() {
        BackgroundPanel panel = new BackgroundPanel("/images/instr_bg.jpg", "AFL");
        panel.setLayout(new BorderLayout());

        JLabel instructions = new JLabel(
                "<html><div style='text-align:center'><h2>Instructions</h2>"
                        + "1. Select a character.<br>"
                        + "2. Fight opponents.<br>"
                        + "3. Win by reducing HP to zero!</div></html>",
                SwingConstants.CENTER);
        instructions.setForeground(Color.blue);
        instructions.setOpaque(false);

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        panel.add(instructions, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCharacterMenu() {
        BackgroundPanel panel = new BackgroundPanel("/images/char_bg.jpg", "AFL");
        panel.setLayout(new BorderLayout(10,10));

        JLabel label = new JLabel("Choose Your Fighter", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 26));
        label.setForeground(Color.blue);
        label.setOpaque(false);
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3,1,10,10));
        buttonPanel.setOpaque(false);

        JButton w = makeMenuButton("Warrior");
        JButton m = makeMenuButton("Mage");
        JButton a = makeMenuButton("Archer");

        ActionListener pick = e -> JOptionPane.showMessageDialog(panel,
                "You selected: " + ((JButton) e.getSource()).getText(),
                "Character Selected",
                JOptionPane.INFORMATION_MESSAGE);

        w.addActionListener(pick);
        m.addActionListener(pick);
        a.addActionListener(pick);

        buttonPanel.add(w);
        buttonPanel.add(m);
        buttonPanel.add(a);

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(buttonPanel);

        panel.add(centerWrap, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCreditsMenu() {
        BackgroundPanel panel = new BackgroundPanel("/images/credits_bg.jpg", "AFL");
        panel.setLayout(new BorderLayout());

        JLabel credits = new JLabel(
                """
                        <html><div style='text-align:center'><h2>Credits</h2>\
                        Game Concept:<br>\
                        Programming: <br>\
                        Design: <br>""",
                SwingConstants.CENTER);
        credits.setForeground(Color.yellow);
        credits.setOpaque(false);

        JButton back = makeMenuButton("Back to Menu");
        back.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        panel.add(credits, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    // Utility to create visually consistent transparent buttons
    private JButton makeMenuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setForeground(Color.BLACK);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return b;
    }

    // Utility: create text-based icon (AFL)
    private Image createTextIcon(String text, Font font, Color color) {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        int x = (img.getWidth() - fm.stringWidth(text)) / 2;
        int y = (img.getHeight() + fm.getAscent()) / 2 - 5;
        g2.drawString(text, x, y);
        g2.dispose();
        return img;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }

    // -------- BackgroundPanel inner class -----------
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private String bigText;

        public BackgroundPanel(String resourcePath, String bigText) {
            this.bigText = bigText;
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                backgroundImage = new ImageIcon(url).getImage();
            } else {
                backgroundImage = new ImageIcon(resourcePath).getImage();
                System.err.println("Warning: resource not found on classpath: " + resourcePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0,0,getWidth(),getHeight());
            }

            // Draw large AFL letters
            if (bigText != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0,0,0,40)); // transparent black
                g2.setFont(new Font("Serif", Font.BOLD, getHeight()/2));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(bigText)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 50;
                g2.drawString(bigText, x, y);
                g2.dispose();
            }
        }
    }
}
