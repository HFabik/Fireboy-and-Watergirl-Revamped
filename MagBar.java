public class MagBar extends HealthBar //shows remaining bullets in magazine
{
    private int m; //number of bullets and magazine size

    public MagBar(int xPos, int yPos, int b, int mag)
    {
        //"health" is equal to the fraction of remaining bullets * the total length of the health bar
        super(xPos, yPos, b * LENGTH / mag);
        m = mag;
    }

    public void setNumBullets(int b)
    {
        health = b * LENGTH / m;
    }
}
