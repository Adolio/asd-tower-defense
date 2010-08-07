package vues.reseau;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class Dialog_Message extends JDialog
{
    private final Insets MARGES = new Insets(30, 30, 30, 30);
    
    public Dialog_Message(Frame parent, String titre, String message)
    {
        super(parent,false);
        setTitle(titre);

        JLabel lblMessage = new JLabel(message);
        
        lblMessage.setBorder(new EmptyBorder(MARGES));
        add(lblMessage,BorderLayout.CENTER);
        
        JButton fermer = new JButton("OK");
        add(fermer,BorderLayout.SOUTH);
        
        fermer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
