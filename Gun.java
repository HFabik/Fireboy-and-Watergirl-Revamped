public class Gun
{
    private int x, y, dmg, r, l = 12, w = 5; //x position, y position, damage, range, length, and width respectively
    private boolean right = true; //true if player is travelling right
    private int numBullets = 0; //number of bullets fired so far
    private int mag; //magazine size
    private Bullet[] bullets; //contains bullets (bullets are assigned when fire() is called)
    private Audio audio; //gunfire

    Gun(int damage, int range, int magSize)
    {
        dmg = damage;
        r = range;
        mag = magSize;
        bullets = new Bullet[mag];
        //audio for bullets
        audio = new Audio();
    }

    //getters
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getL()
    {
        return l;
    }

    public int getW()
    {
        return w;
    }

    public Bullet getBullet(int i)
    {
        return bullets[i];
    }

    public int getNumBullets()
    {
        return numBullets;
    }

    public int getDmg()
    {
        return dmg;
    }

    public int getMag()
    {
        return mag;
    }

    //setters
    public void setDir(boolean direction)
    {
        right = direction;
    }

    public void setX(int xPos)
    {
        x = xPos;
    }

    public void setY(int yPos)
    {
        y = yPos;
    }

    //sets if the bullet is drawn or not
    public void setBulletInRange(int i, boolean inR)
    {
        bullets[i].setInRange(inR);
    }

    public void fire()
    {
        //new bullet created
        bullets[numBullets] = new Bullet(right, r, x + l, y, dmg);
        //numBullets updated
        numBullets++;
        audio.play();
    }

    public void reload()
    {
        numBullets = 0;
    }

}
