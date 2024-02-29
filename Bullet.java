/* Bullet fired by gun*/

public class Bullet
{
    private boolean dir; //true if bullet is moving right
    private int x, y, vel, dmg, distance = 0, r; //y position, x position, x velocity (bullets travel at point blank so no y velocity), damage dealt by bullet, distance bullet has travelled, max range bullet can travel
    private static int diameter = 5;
    private boolean inRange = true; //drawing does not occur if bullet out of range

    Bullet(boolean direction, int range, int xPos, int yPos, int damage)
    {
        dir = direction;
        x = xPos;
        y = yPos;
        r = range;
        dmg = damage;
        //if player is moving right, vel is positive, else, it is negative
        vel = direction ? 15 : -15;
    }

    //accessors
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getVel()
    {
        return vel;
    }

    public boolean getInRange()
    {
        return inRange;
    }

    public static int getDiam()
    {
        return diameter;
    }

    //modifiers
    public void setX(int xPos)
    {
        x = xPos;
    }

    public void setInRange(boolean inR)
    {
        inRange = inR;
    }

    public void move() //moves bullet (is called every set period of time)
    {
        //if bullet has not travelled past its range, x is incremented and distance is updated
        if (distance < r)
        {
            x += vel;
            distance += Math.abs(vel);
        } else
        {
            inRange = false; //used for drawing
        }
    }
}
