package outils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

public class TimerDemo extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private myTimer timer = new myTimer(1000, this); // call every 1000ms
    private JLabel lblTime  = new JLabel();
    private JButton btnPause = new JButton("PLAY");

    public TimerDemo()
    {
        super("Timer");
        setPreferredSize(new Dimension(200,200));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        getContentPane().add(lblTime,BorderLayout.NORTH);
        getContentPane().add(btnPause,BorderLayout.SOUTH);
        
        btnPause.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(timer.isPaused())
                {
                    timer.play();
                    btnPause.setText("PAUSE");
                }
                else
                {
                    timer.pause();
                    btnPause.setText("PLAY");
                }
            }
        });
        
        timer.start();

        pack();
        setVisible(true);
    }

    public static void main(String args[])
    {
        new TimerDemo();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == timer)
            lblTime.setText(timer.toString());
    }
}
