public class Player
{
    protected int x, y, vx = 10, vy = 0, health = 100; //x position, y position, x velocity, y velocity, health
    protected static int diameter = 20;
    protected HealthBar hb;
    protected boolean hasWon = false;
    private MagBar mb;
    private Gun gun;

    Player(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
        gun = new Gun(20, 250, 10); //parameters: damage, range, magSize
        int barPos = x + diameter / 2 - HealthBar.LENGTH / 2;
        hb = new HealthBar(barPos, y - 4, 100);
        mb = new MagBar(barPos, y - 10, 10, gun.getMag());
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

    public boolean getHasWon()
    {
        return hasWon;
    }

    public int getVx()
    {
        return vx;
    }

    public int getVy()
    {
        return vy;
    }

    public int getDiameter()
    {
        return diameter;
    }

    public int getHealth()
    {
        return health;
    }

    public Gun getGun()
    {
        return gun;
    }

    public HealthBar getHBar()
    {
        return hb;
    }

    public MagBar getMBar()
    {
        return mb;
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

    public void setVx(int xVel)
    {
        vx = xVel;
    }

    public void setVy(int yVel)
    {
        vy = yVel;
    }

    public void setGun(Gun g)
    {
        gun = g;
    }

    public void setHasWon(boolean hw)
    {
        hasWon = hw;
    }

    public void setGunBulletInRange(int i, boolean inR)
    {
        gun.setBulletInRange(i, inR);
    }

    public void decreaseHealth(int decrement)
    {
        health -= decrement;
    }

    public void moveX()
    {
        //FOR X
        //x direction is reversed if player is moving towards a wall
        if (x < 20 && vx < 0 || x > 780 && vx > 0)
        {
            vx *= -1;
        }
        //x is incremented by vx
        x += vx;
        //updates direction boolean of gun
        if (vx > 0)
        {
            gun.setDir(true);
        } else if (vx < 0)
        {
            gun.setDir(false);
        }
    }

    public void moveY()
    {
        //FOR Y
        //y is moved by vy
        y += vy;
        //vy is always incremented by vertical acceleration
        vy += TheGame.getAy();
    }

    public void moveGun()
    {
        //setting x (direction of gun affects drawing coordinates)
        if (vx > 0)
        {
            gun.setX(x + diameter);
        } else
        {
            gun.setX(x - gun.getL() + 1);
        }
        //setting y
        gun.setY(y + diameter / 2);
    }

    public void moveHBar()
    {
        hb.setX(x + diameter / 2 - HealthBar.LENGTH / 2);
        hb.setY(y - 4);
    }

    public void moveMBar()
    {
        mb.setX(x + diameter / 2 - HealthBar.LENGTH / 2);
        mb.setY(y - 10);
    }

    public void isDamaged(Enemy e) //checks if player has been damaged by any enemies
    {
        int ex = e.getX();
        int ey = e.getY();
        //if x == ex (+-5) and y == ey (+-5)
        if (x > ex - 5 && x < ex + 5 && y > ey - 5 && y < ey + 5)
        {
            decreaseHealth(e.getDmg());
        }
    }

    public boolean isShot(Bullet b) //checks if bullet has hit player
    {
        int bx = b.getX();
        int by = b.getY();
        int enemyX = x + diameter / 2;
        int enemyY = y + diameter / 2;
        int allowance = Math.abs(b.getVel() + vx) + diameter / 2 + 1; //approximation
        return (b.getInRange() && (bx >= enemyX - allowance && bx <= enemyX + allowance + b.getVel()) && (by > enemyY - diameter && by < enemyY + diameter));
    }

    public boolean checkIfAlive()
    {
        return (health > 0);
    }
}
