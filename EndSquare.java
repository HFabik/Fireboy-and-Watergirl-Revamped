/* When you touch this square, you win!*/

public class EndSquare extends CourseObject
{
    public EndSquare(int x, int y)
    {
        super(x, y, 20, 20);
    }

    public int getHitAction(int side)
    {
        if (side != CourseObject.NO_SIDE) //if the player has hit any side
        {
            return HIT_ACTION_WIN;
        }
        return HIT_ACTION_NOTHING;
    }
}