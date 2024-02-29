import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;

/*this class contains all the pages necessary (game, menu, settings)*/

public class Cards extends JFrame implements ActionListener
{
    //cards displays JPanels using CardLayout
    private JPanel cards, menu, gOver, youWin, instruc, settings;

    //the game (extends JPanel)
    private TheGame game = null;

    //JButtons
    private JButton play, settingsButton, instructions, back, level1, level2, level3;
    //JLabels
    private JLabel gol, winL, deathl, instrucL, title;
    //new CardLayout
    private CardLayout cardLayout;
    //images for screens (game over, fireworks, you won!)
    private ImageIcon go, death, win;
    //timer to check if player is alive
    private Timer t;
    //default level for TheGame is level 1 (easy)
    private int level = 1;

    //card constraints
    private static final String MENU_CARD = "menu";
    private static final String GAMEOVER_CARD = "gameover";
    private static final String GAME_CARD = "game";
    private static final String WIN_CARD = "win";
    private static final String INSTRUC_CARD = "instructions";
    private static final String SETTINGS_CARD = "settings";

    public Cards()
    {
        //formats the JFrame
        JFrame.setDefaultLookAndFeelDecorated(true);
        setSize(800, 500);
        setResizable(false);
        setTitle("Fireboy and watergirl but it is actually geometry dash but it is actually surviv.io with guns");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //creates JPanel menu
        menu = new JPanel(); //contains most buttons
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
        //adds rigid areas, label, and buttons to menu
        menu.add(Box.createRigidArea(new Dimension(0, 100)));
        //setting title for menu
        title = new JLabel("Fireboy and watergirl but it's actually geometry dash but it's actually surviv.io with guns");
        title.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(title);
        play = addButton("Play!", menu, 20);
        settingsButton = addButton("Settings", menu, 20);
        instructions = addButton("Instructions", menu, 20);

        //displayed when player dies
        gOver = new JPanel();
        //images for game over screen
        go = new ImageIcon(".\\resources\\GameOver.png"); //background
        gol = new JLabel(go);
        //sets borderlayout for JLabel to add second image on top
        gol.setLayout(new BorderLayout());
        gOver.add(gol, BorderLayout.NORTH);
        ImageIcon death = new ImageIcon(".\\resources\\animated-fireworks-image-0087.gif"); //fireworks
        JLabel deathl = new JLabel(death);
        gol.add(deathl, BorderLayout.CENTER);

        //displayed when player wins
        youWin = new JPanel();
        //images for win screen
        win = new ImageIcon(".\\resources\\YouWin.png");
        winL = new JLabel(win);
        youWin.add(winL);

        //shows instructions for game
        instruc = new JPanel(); //contains instructions + back button
        instruc.setLayout(new BoxLayout(instruc, BoxLayout.PAGE_AXIS));
        instruc.add(Box.createRigidArea(new Dimension(0, 125)));
        instrucL = new JLabel();
        instruc.add(instrucL);
        back = addButton("Back", instruc, 100);

        //allows user to select levels
        settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.PAGE_AXIS));
        level1 = addButton("Easy", settings, 150);
        level2 = addButton("Medium", settings, 20);
        level3 = addButton("Hard", settings, 20);

        //setting up cardLayout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(menu, MENU_CARD);
        cards.add(gOver, GAMEOVER_CARD);
        cards.add(youWin, WIN_CARD);
        cards.add(instruc, INSTRUC_CARD);
        cards.add(settings, SETTINGS_CARD);
        //adding cards
        getContentPane().add(cards);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        //pressing play takes the player to TheGame
        if (e.getSource() == play)
        {
            executeInSwingThread(this::startGame);
        } else if (e.getSource() == t) //source is timer
        {
            //player died
            if (game != null && !game.getPlayer().checkIfAlive())
            {
                t.stop(); // Prevent repeated events from the timer
                game.discard(this);
                executeInSwingThread(this::doGameOver);
            } else if (game != null && game.getPlayer().getHasWon()) //player won
            {
                t.stop();
                executeInSwingThread(this::showWin);
            }
        } else if (e.getSource() == instructions)
        {
            executeInSwingThread(this::showInstructions);
        } else if (e.getSource() == back) //goes back to menu
        {
            executeInSwingThread(this::showMenu);
        } else if (e.getSource() == settingsButton)
        {
            executeInSwingThread(this::showSettings);
        } else if (e.getSource() == level1) //sets levels and goes back to menu
        {
            level = 1;
            executeInSwingThread(this::showMenu);
        } else if (e.getSource() == level2)
        {
            level = 2;
            executeInSwingThread(this::showMenu);
        } else if (e.getSource() == level3)
        {
            level = 3;
            executeInSwingThread(this::showMenu);
        }
    }

    private JButton addButton(String text, Container c, int height) //adds buttons to a box layout
    {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this);
        c.add(Box.createRigidArea(new Dimension(0, height)));
        c.add(button);
        return button;
    }

    //card showing helper methods
    private void showSettings()
    {
        cardLayout.show(cards, "settings");
        unfocus();
        level1.setFocusable(true);
        level2.setFocusable(true);
        level3.setFocusable(true);
    }

    private void showInstructions() //gets file into StringBuilder, prints it into JLabel
    {
        StringBuilder in = new StringBuilder("<html>");
        try
        {
            Scanner scFile = new Scanner(new File(".\\resources\\instructions.txt"));
            while (scFile.hasNext())
            {
                in.append(scFile.nextLine());
                in.append("<br/>");
            }
            in.append("<html>");
        } catch (Exception X)
        {
            in.append("Sorry, the file was not found.");
        }
        cardLayout.show(cards, "instructions");
        unfocus();
        back.setFocusable(true);
        String s = in.toString();
        instrucL.setText(s);
    }

    //displays gameover and then returns to menu
    private void doGameOver()
    {
        stopGame();
        cardLayout.show(cards, GAMEOVER_CARD);
        unfocus();
        gOver.setFocusable(true);
        timeRestart();
    }

    //displays win and then returns to menu
    private void showWin()
    {
        stopGame();
        cardLayout.show(cards, WIN_CARD);
        unfocus();
        youWin.setFocusable(true);
        timeRestart();
    }

    private void showMenu()
    {
        cardLayout.show(cards, "menu");
        unfocus();
        menu.setFocusable(true);
        repaint();
    }

    private void timeRestart()//timer that shows game over and you win for only 2 seconds
    {
        Timer timer = new Timer(2000, e -> executeInSwingThread(this::restartGame));
        timer.setRepeats(false);
        timer.start();
    }

    private void restartGame()
    {
        SwingUtilities.invokeLater(this::showMenu);
    }

    private void stopGame()
    {
        if (game != null)
        {
            t.stop();
            cardLayout.removeLayoutComponent(game);
            cards.remove(game);
            game = null;
        }
    }

    private void startGame()
    {

        stopGame(); //stops previous game

        game = new TheGame(this, level); //new game

        cards.add(game, "game");
        cardLayout.show(cards, "game");
        unfocus();
        game.setFocusable(true);
        requestFocusInWindow(); //for KeyListener
        t = new Timer(50, this);
        t.start();
        Component c = getFocusOwner();
    }

    private void unfocus() //unfocuses everything
    {
        //unfocuses JButtons
        play.setFocusable(false);
        settingsButton.setFocusable(false);
        instructions.setFocusable(false);
        back.setFocusable(false);
        level1.setFocusable(false);
        level2.setFocusable(false);
        level3.setFocusable(false);
        //unfocuses JLabel
        menu.setFocusable(false);
        gOver.setFocusable(false);
        youWin.setFocusable(false);
        instruc.setFocusable(false);
        settings.setFocusable(false);
        if (game != null)
        {
            game.setFocusable(false);
        }
    }

    private void executeInSwingThread(Runnable runnable) //thread handling
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            runnable.run();
        } else
        {
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            } catch (Exception ignored)
            {
            }
        }
    }

    public static void main(String[] args)
    {
        //run
        Cards c = new Cards();
        c.showMenu();
    }
}
