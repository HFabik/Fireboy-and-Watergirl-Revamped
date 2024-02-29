/*A platform is a general rectangle that a player cannot move through*/

public class Platform extends CourseObject
{

    Platform(int xPos, int yPos, int length)
    {
        super(xPos, yPos, 10, length);
    } //constructor

    //accessors
    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }

    public int getL()
    {
        return length;
    }

    public int getH()
    {
        return height;
    }

    public int getHitAction(int side)
    {
        //if player hits the top of a platform they don't fall through
        if (side == CourseObject.UP)
        {
            return HIT_ACTION_BLOCK;
        }//if player hits the bottom they are bounced back down
        else if (side == CourseObject.DOWN)
        {
            return HIT_ACTION_BOUNCE;
        }//if a player hits the sides they are bounced off
        else if (side == CourseObject.LEFT || side == CourseObject.RIGHT)
        {
            return HIT_ACTION_BOUNCE;
        }
        return HIT_ACTION_NOTHING;
    }
}