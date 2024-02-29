
public class Door extends CourseObject
{
    private Switch s; //opens the door

    public Door(int x, int y)
    {
        super(x, y, 74, 5); //standard door size, super from CourseObject
        s = new Switch(x, y); //all doors have switches
    }

    public Switch getSwitch()
    {
        return s;
    }

    public int getHitAction(int side)
    {
        if (s.isClosed())
        {
            if (side == CourseObject.RIGHT || side == CourseObject.LEFT) //if player hits side of door
            {
                return HIT_ACTION_HEALTH; //player dies
            }

            return HIT_ACTION_BOUNCE; //if player hits door anywhere else they bounce
        }
        return HIT_ACTION_NOTHING;  //else nothing happens
    }
}