/*An obstacle that a player can interact with. Deafult is a health action.*/

public class CourseObject
{
    //location and size
    protected int xPos, yPos, height, length;
    //sides are assigned integers as identification
    public static final int LEFT = 0,
            UP = 1,
            RIGHT = 2,
            DOWN = 3,
            NO_SIDE = -1;
    //action-on-hits are assigned integers as identification
    public static final int HIT_ACTION_NOTHING = 0,
            HIT_ACTION_BLOCK = 1,
            HIT_ACTION_BOUNCE = 2,
            HIT_ACTION_WIN = 3,
            HIT_ACTION_HEALTH = 100;

    public CourseObject(int x, int y, int h, int l) //default construtor
    {
        xPos = x;
        yPos = y;
        height = h;
        length = l;
    }

    //getters
    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }

    public int getHeight()
    {
        return height;
    }

    public int getLength()
    {
        return length;
    }

    public int getHitAction(int side)
    {
        return HIT_ACTION_HEALTH;
    } //gets overridden depending on type of CourseObject
}
