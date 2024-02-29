/*Health bars show the user how much health is left for players/enemies*/

public class HealthBar
{
    static final int LENGTH = 25, WIDTH = 5; //standard size
    protected int x, y, health;

    public HealthBar(int xPos, int yPos, int h)
    {
        health = h / 4; //Health bar is one quarter of a player's health (100)
        x = xPos;
        y = yPos;
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

    public int getHealth()
    {
        return health;
    }

    //modifiers
    public void setX(int xPos)
    {
        x = xPos;
    }

    public void setY(int yPos)
    {
        y = yPos;
    }

    public void setHealth(int h)
    {
        health = h / 4;
    }
}
