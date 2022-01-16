package fr.amgone.animaquizz.app.gui.component;

import org.jdesktop.swingx.JXTextField;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class ATextField extends JXTextField {
    private Shape shape;
    private final int arch;

    public ATextField(int arch) {
        this.arch = arch;
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, arch, arch);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-2, getHeight()-2, arch, arch);
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-2, arch, arch);
        }

        return shape.contains(x, y);
    }
}
