public class Enemy extends Player
{
    private int xTravelled = 0; //used for pace()
    private int range = 100; //range within which enemy can pace()
    private int dir = 1; //1 if enemy is moving right, -1 if it is moving left
    private int dmg = 5; //passive damage dealt when touching player
    private boolean isSmart; //does the enemy approach the player?

    public Enemy(int x, int y, int d, boolean s)
    {
        super(x, y);
        isSmart = s;
        dmg = d;
    }

    public int getDmg()
    {
        return dmg;
    }

    public void move(Player p)
    {
        //smart enemies stop pacing if the player is at the same y location (+-5) to pursue them
        if (isSmart && (p.getY() > y - 5 && p.getY() < y + 5))
        {
            moveSmart(p); //enemy chases player
        } else
        {
            pace();
        }
        moveX(); //ensures enemy is within bounds
    }

    public void pace() //moves enemy back and forth within range
    {
        if (Math.abs(xTravelled) + 6 <= range) //if within range
        {
            vx = 6 * dir; //velocity based on direction
            xTravelled += vx; //xTravelled incremented
        } else //reset distanceTravelled to 0 and change direction
        {
            xTravelled = 0;
            dir *= -1;
        }
    }

    public void moveSmart(Player p)
    {
        //follows the player (based on x only)
        if (x > p.getX())
        {
            vx = -6;
        } else if (x < p.getX())
        {
            vx = 6;
        }
    }
}