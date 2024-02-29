import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/*This class contains pathways to audio files (bullet shot) and methods to play them */

public class Audio
{
    Audio() //empty constructor
    {
    }

    public void play() //plays the file specified by filepath
    {
        final String filePaths = ".\\resources\\gunFire.wav";
        //for playing
        Clip clip;
        AudioInputStream stream;
        try //try to open audio stream
        {
            stream = AudioSystem.getAudioInputStream(new File(filePaths));
            clip = AudioSystem.getClip();
            clip.open(stream);
            //play the clip once
            clip.loop(0);
        } catch (Exception e)
        {
            System.out.println("Sound file could not be played. My sincerest apologies.");
        }
    }
}
