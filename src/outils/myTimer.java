package outils;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class myTimer extends Timer
{
    private static final long serialVersionUID = 1L;
    private long temp;
    private long timeStart;
    private boolean pause = false; 

    public myTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
    }

    @Override
    public void start()
    {
        super.start();
        
        timeStart = System.currentTimeMillis();
    }
    
    public void pause()
    {
        pause = true;
        temp += System.currentTimeMillis() - timeStart;
    }
    
    public void play()
    {
        pause = false;
        timeStart = System.currentTimeMillis();
    }
    
    public long getTime()
    {
        if(pause)
            return temp;
        else
            return temp + System.currentTimeMillis() - timeStart;
    }
    
    public int getSeconds()
    {
        return (int) (getTime() / 1000) % 60;
    }
    
    public int getMinutes()
    {
        return (int) (getTime() / 60000) % 60;
    }
    
    public int getHours()
    {
        return (int) (getTime() / 3600000) % 24;
    }
    
    public String toString()
    {
        return String.format("%02d:%02d:%02d", getHours(), 
                                               getMinutes(), 
                                               getSeconds());
    }

    public boolean isPaused()
    {
        return pause;
    }
}
