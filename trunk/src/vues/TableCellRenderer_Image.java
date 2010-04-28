package vues;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableCellRenderer_Image implements TableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object image,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        Image img = (Image) image;
 
        return new JLabel(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
    }
}
