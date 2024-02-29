import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;


public class TheGame extends JPanel implements ActionListener
{
    private Timer t = new Timer(20, this); //animation timer
    private Player p;
    private Door[] d;
    private Platform[] pf;
    private Enemy[] enemies;
    private Lake[] lakes;
    private int nd, npf, ne = 1, nl, place = 0;
    private final static int ay = 2; //vertical acceleration due to gravity (scalar)
    private EndSquare ens;
    private KeyListener keyListener;
    private ImageIcon end, backg; //endsquare and background images
    private ArrayList<CourseObject> obstacles = new ArrayList<>();

    public TheGame(JFrame frame, int level)
    {
        //sets Background
        backg = new ImageIcon(".\\resources\\Background.png");

        //sets player
        p = new Player(10, 400);

        //sets ImageIcon for end square
        end = new ImageIcon(".\\resources\\EndSquare.png");

        //sets doors, lakes, platforms, and enemies
        makeObstacles(level);

        //sets KeyListener
        keyListener = new KeyListener()
        {
            public void keyTyped(KeyEvent e)
            {
                char k = e.getKeyChar();
                if (k == 'd') //finds closest door and opens it if in range
                {
                    int close = findDoor();
                    if (close <= d[place].getSwitch().getRange() && p.getY() > d[place].getY() && p.getY() + 20 < d[place].getY() + d[place].getHeight())
                    {
                        d[place] = open(d[place]);
                        repaint();
                    }
                } else if (k == 'w' && onPlatform(p)) //jumps if on a platform
                {
                    p.setVy(-18);
                } else if (k == 'r') //reloads
                {
                    Gun temp = p.getGun();
                    temp.reload();
                    p.setGun(temp);
                } else if (k == 'f' && p.getGun().getNumBullets() < p.getGun().getMag()) //fire if bullets left in magazine
                {
                    p.getGun().fire();
                }
            }

            public void keyPressed(KeyEvent e)
            {
            }

            public void keyReleased(KeyEvent e)
            {
            }
        };


        frame.setFocusable(true);
        frame.addKeyListener(keyListener);
    }

    public static int getAy()
    {
        return ay;
    }

    public Player getPlayer()
    {
        return p;
    }

    public void discard(JFrame frame) //stop timer and KeyListener to start new game
    {
        t.stop();
        frame.removeKeyListener(keyListener);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == t) //when timer is fired
        {
            //checks if player is in bounds
            checkBounds();
            //checks if enemies are alive
            for (int i = 0; i < ne; i++)
            {
                if (enemies[i].checkIfAlive())
                {
                    enemies[i].move(p);
                    enemies[i].moveHBar();
                    enemies[i].getHBar().setHealth(enemies[i].getHealth()); //resets enemies health bar size if damaged
                    p.isDamaged(enemies[i]); //checks if enemy damages player
                    for (int j = 0; j < p.getGun().getNumBullets(); j++)
                    {
                        if (enemies[i].isShot(p.getGun().getBullet(j))) //if enemy is shot by a player's bullet
                        {
                            p.setGunBulletInRange(j, false); //stops drawing the bullet
                            enemies[i].decreaseHealth(p.getGun().getDmg()); //decreases halth
                        }
                    }
                }
            }
            //moves fired bullets
            for (int i = 0; i < p.getGun().getNumBullets(); i++)
            {
                p.getGun().getBullet(i).move();
            }
            //moves player
            int ogPX = p.getX();
            int ogPY = p.getY();
            p.moveX();
            p.moveY();
            //checks if player is touching a course object and performs appropriate action
            touchObject(ogPX, ogPY);
            //moves gun, magazine bar and health bar accordingly
            p.moveGun();
            p.moveHBar();
            p.moveMBar();
            p.getHBar().setHealth(p.getHealth()); //sets health
            Gun temp = p.getGun();
            p.getMBar().setNumBullets(temp.getMag() - temp.getNumBullets());//sets magazine

            repaint();
        }
    }

    public void paintComponent(Graphics g) //graphics
    {
        super.paintComponent(g);
        //paints background
        g.drawImage(backg.getImage(), 0, 0, null);
        //paints the end square
        g.drawImage(end.getImage(), ens.getX(), ens.getY(), null);
        //paints player
        g.setColor(Color.PINK);
        g.fillOval(p.getX(), p.getY(), p.getDiameter(), p.getDiameter());
        //paints gun
        g.setColor(Color.BLACK);
        drawGun(p.getGun(), g);
        //paints bullets
        for (int i = 0; i < p.getGun().getNumBullets(); i++)
        {
            Bullet bullet = p.getGun().getBullet(i);
            if (bullet.getInRange())
            {
                drawBullet(bullet, g);
            }
        }
        //paints player's health and mag bars
        drawHBar(p.getHBar(), g);
        drawHBar(p.getMBar(), g);
        //paint doors
        for (int i = 0; i < nd; i++)
        {
            if (d[i].getSwitch().isClosed()) //only paint doors if closed
            {
                g.setColor(Color.CYAN);
                g.fillRect(d[i].getX(), d[i].getY(), d[i].getLength(), d[i].getHeight());
            }
            //always paints switches
            g.setColor(d[i].getSwitch().getCol());
            g.fillOval(d[i].getSwitch().getX(), d[i].getSwitch().getY(), d[i].getSwitch().getSize(), d[i].getSwitch().getSize());
        }
        //paint platforms
        g.setColor(Color.BLACK);
        for (int i = 0; i < npf; i++)
        {
            g.fillRect(pf[i].getX(), pf[i].getY(), pf[i].getL(), pf[i].getH());
        }
        //paints lakes
        g.setColor(Color.MAGENTA);
        for (int i = 0; i < nl; i++)
        {
            g.fillRect(lakes[i].getX(), lakes[i].getY(), lakes[i].getLength(), lakes[i].getHeight());
        }
        //paint eneimes
        g.setColor(Color.BLUE);
        for (int i = 0; i < ne; i++)
        {
            Enemy temp = enemies[i];
            if (temp.checkIfAlive())
            {
                g.fillOval(temp.getX(), temp.getY(), temp.getDiameter(), temp.getDiameter());
                //paints enemies' health bar
                drawHBar(temp.getHBar(), g);
            }
        }
        //start the timer if it is not already running
        if (!t.isRunning())
        {
            t.start();
        }
    }

    //helper drawing methods
    private void drawGun(Gun gun, Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(gun.getX(), gun.getY(), gun.getL(), gun.getW());
    }

    private void drawBullet(Bullet b, Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillOval(b.getX(), b.getY(), Bullet.getDiam(), Bullet.getDiam());
    }

    private void drawHBar(HealthBar h, Graphics g)
    {
        //drawing healthbar
        g.setColor(Color.RED);
        g.fillRect(h.getX(), h.getY(), HealthBar.LENGTH, HealthBar.WIDTH);
        g.setColor(Color.GREEN);
        g.fillRect(h.getX(), h.getY(), h.getHealth(), HealthBar.WIDTH);
    }

    private void checkBounds() //checks if player is in bounds (above bottom)
    {
        if (p.getY() >= 500)
        {
            p.decreaseHealth(p.getHealth());
        }
    }

    public boolean onPlatform(Player actor) //checks if player is on platform (within 7px)
    {
        int range = 7; //vertical range within which player is considered to be on the platform
        for (int i = 0; i < pf.length; i++) //for each platform
        {
            //booleans for if player x and y are on platform or not
            boolean xMatch = false;
            boolean yMatch = false;
            //gets positions of player
            int playerY = actor.getY() + actor.getDiameter(); //bottom
            int playerX = actor.getX();
            //gets positions of platform
            int platX = pf[i].getX();
            int platY = pf[i].getY();
            //hands exception where playerX is past left edge but the player is not fully off platform
            if (playerX < platX && (playerX + actor.getDiameter()) > platX)
            {
                playerX += actor.getDiameter();
            }
            //if playerY == platY (+-10) OR the player would pass the platform on the next animation
            if ((playerY <= platY + range && playerY >= platY - range) || (playerY < platY && playerY + p.getVy() > platY))
            {
                yMatch = true;
            }
            //if playerX is between the left and right ends of the platform
            if (playerX >= platX && playerX <= (platX + pf[i].getL()))
            {
                xMatch = true;
            }
            //player is on platform for both x and y
            if (xMatch && yMatch)
            {
                return true;
            }
        }
        return false;
    }

    private Door open(Door dn) //opens door, remove it from obstacles
    {
        dn.getSwitch().open();
        obstacles.remove(dn);
        return dn;
    }

    private int findDoor() //finds the closest door to player
    {
        int px = p.getX();
        int closest = 800, dis;
        place = 0;
        for (int i = 0; i < nd; i++)
        {
            dis = Math.abs(d[i].getX() - px);
            if (dis < closest)
            {
                closest = dis;
                place = i;
            }
        }
        return closest; //returns distance to closest door
    }

    private void makeObstacles(int level) //sets obstacles depending on the level
    {
        try
        {
            FileReader r = new FileReader(".\\resources\\level" + level + ".txt"); //level is chosen in settings JPanel
            BufferedReader br = new BufferedReader(r);
            String s = br.readLine();
            Scanner in = new Scanner(s);
            //makes endsquare
            int ex = in.nextInt();
            int ey = in.nextInt();
            ens = new EndSquare(ex, ey);
            obstacles.add(ens);
            //makes enemies (not course obstacle so does not add to obstacles)
            ne = Integer.parseInt(br.readLine());
            enemies = new Enemy[ne];
            for (int i = 0; i < ne; i++)
            {
                String line = br.readLine();
                Scanner scan = new Scanner(line);
                int x = scan.nextInt();
                int y = scan.nextInt();
                int dmg = scan.nextInt();
                boolean smart = scan.nextBoolean();
                enemies[i] = new Enemy(x, y, dmg, smart);
            }
            //makes lakes
            nl = Integer.parseInt(br.readLine());
            lakes = new Lake[nl];
            for (int i = 0; i < nl; i++)
            {
                String line = br.readLine();
                Scanner scan = new Scanner(line);
                int x = scan.nextInt();
                int y = scan.nextInt();
                int l = scan.nextInt();
                lakes[i] = new Lake(x, y, l);
                obstacles.add(lakes[i]);
            }
            //makes doors
            nd = Integer.parseInt(br.readLine());
            d = new Door[nd];
            for (int i = 0; i < nd; i++)
            {
                String line = br.readLine();
                Scanner scan = new Scanner(line);
                int x = scan.nextInt();
                int y = scan.nextInt();
                d[i] = new Door(x, y);
                obstacles.add(d[i]);
            }
            //makes platforms
            npf = Integer.parseInt(br.readLine());
            pf = new Platform[npf];
            for (int i = 0; i < npf; i++)
            {
                String line = br.readLine();
                Scanner scan = new Scanner(line);
                int x = scan.nextInt();
                int y = scan.nextInt();
                int l = scan.nextInt();
                pf[i] = new Platform(x, y, l);
                obstacles.add(pf[i]);
            }
        } catch (Exception e)
        {
            System.out.println("File not found");
        }
    }

    private void touchObject(int ogX, int ogY) //finds which object is being toched on which side and performs the necessary actions
    {
        CourseObject cObjTouched = null; //which object is player touching
        int side = CourseObject.NO_SIDE; //which side is being touched
        double radius = p.getDiameter() / 2;
        double x = ogX + radius; //x center of player before move
        double y = ogY + radius; //y center of player before move
        double deltaX = p.getX() - ogX; //change in the x before and after player moved
        double deltaY = p.getY() - ogY; //change in the y before and after player moved
        double largest = Math.max(Math.abs(deltaX), Math.abs(deltaY)); //largest change
        double stepX = deltaX / largest; //increment by which x is checked for contact
        double stepY = deltaY / largest; //increment by which y is checked for contact

        for (int step = 0; step < largest && cObjTouched == null; step++) //exit when a touched object is found
        {
            x += stepX; //increase x and y by respective steps each time
            y += stepY;

            for (int objN = 0; objN < obstacles.size(); objN++) //for every obstacle
            {
                CourseObject co = obstacles.get(objN);
                //checks if is inside or on the obstacle
                if ((x + radius) > co.getX() && (x - radius) < (co.getX() + co.getLength()) &&
                        (y + radius) > co.getY() && (y - radius) < (co.getY() + co.getHeight()))
                {
                    // actor is inside or on the object, check which side is closer
                    cObjTouched = co;

                    double disToLeft = Math.abs(x + radius - co.getX()); //distance to the left side
                    double disToRight = Math.abs(co.getX() + co.getLength() - x + radius); //distance to the right side
                    double disToTop = Math.abs(y + radius - co.getY()); //distance to the top
                    double disToBottom = Math.abs(co.getY() + co.getLength() - y + radius); //distance to the bottom

                    //closest to bottom
                    if (disToBottom < disToTop && disToBottom < disToLeft && disToBottom < disToRight)
                    {
                        side = CourseObject.DOWN;
                    }
                    //closest to top
                    else if (disToTop < disToLeft && disToTop < disToRight && disToTop < disToBottom)
                    {
                        side = CourseObject.UP;
                    }
                    //closest to left side
                    else if (disToLeft < disToRight)
                    {
                        side = CourseObject.LEFT;
                    }
                    //closest to right side
                    else
                    {
                        side = CourseObject.RIGHT;
                    }
                    break;
                }
            }
        }

        if (cObjTouched != null) //if a course object is touched
        {

            int hitAction = cObjTouched.getHitAction(side);//gets the action taken when side is hit by player

            switch (hitAction)
            {
                case CourseObject.HIT_ACTION_NOTHING:
                {
                    // Continue, nothing to do
                    break;
                }
                case CourseObject.HIT_ACTION_BOUNCE:
                {

                    doBlockOrBounce(side, x, stepX, y, stepY, -p.getVx(), -p.getVy());//bounce
                    break;
                }
                case CourseObject.HIT_ACTION_BLOCK:
                {
                    doBlockOrBounce(side, x, stepX, y, stepY, 0, 0); //block
                    break;
                }
                case CourseObject.HIT_ACTION_WIN:
                {
                    p.setHasWon(true); //win
                    break;
                }
                default:
                {
                    // Must be a health action
                    doBlockOrBounce(side, x, stepX, y, stepY, 0, 0); //block to not fall through lakes
                    int damage = hitAction - CourseObject.HIT_ACTION_HEALTH;
                    p.decreaseHealth(damage == 0 ? p.getHealth() : damage);
                    break;
                }
            }
        }
    }

    //helper methods for touchObject
    private void doBlockOrBounce(int side, double x, double stepX, double y, double stepY, int vx, int vy)
    {
        int radius = p.getDiameter() / 2;
        if ((side == CourseObject.LEFT && p.getVx() > 0) || (side == CourseObject.RIGHT && p.getVx() < 0))
        {
            p.setVx(vx);
        } else
        {
            p.setVy(vy);
        }

        // Set the position of the player on the step before hitting the object
        p.setX((int) (x - stepX - radius));
        p.setY((int) (y - stepY - radius));
    }
}
