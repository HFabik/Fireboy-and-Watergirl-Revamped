/*A lake is a platform that deals passive damage when stood on*/

public class Lake extends CourseObject
{
    private int dmg;

    Lake(int xPos, int yPos, int length)
    {
        super(xPos, yPos, 5, length);
        dmg = 7;
    }

    public int getHitAction(int side)
    {
        if (side != CourseObject.NO_SIDE) //if any side is hit
        {
            return HIT_ACTION_HEALTH + dmg; //health decreased by damage
        }
        return HIT_ACTION_NOTHING;
    }
}