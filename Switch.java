import java.awt.*;

/*A switch controls whether the door is open or not. You must be within the range of the switch to open the door.*/

public class Switch
{
    private boolean closed; //State of the door (true = closed, false = open)
    private static int size, range; //The size and range at which the switch can be accessed respectively
    private Color colour; //Colour of the switch (red = closed, green = open)
    private int x, y; //x and y coordinates of the top left corner of the switch respectively

    public Switch(int x, int y) //Receives x and y coordinates of door
    {
        closed = true; //Door starts closed
        size = 10;
        colour = Color.red;
        range = 70;
        this.x = x - range; //Switch is the range of the door for opening
        this.y = y + 55; //Switch is 55 pixels below the door
    }

    //accessor methods
    public boolean isClosed()
    {
        return closed;
    }

    public int getSize()
    {
        return size;
    }

    public Color getCol()
    {
        return colour;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getRange()
    {
        return range;
    }

    //modifier methods
    public void open()//Opens door
    {
        closed = false;
        colour = Color.green;
    }
}