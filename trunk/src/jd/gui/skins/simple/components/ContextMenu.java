package jd.gui.skins.simple.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import jd.utils.JDUtilities;

public class ContextMenu extends JPopupMenu {

    protected Logger          logger = JDUtilities.getLogger();

    // public void mousePressed(MouseEvent e) {
    // logger.info("PRESSED");
    // if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
    // Point point = e.getPoint();
    // int x = e.getX();
    // int y = e.getY();
    // new InternalPopup(this, x, y);
    // }
    //
    // }

    private Vector<JMenuItem> menus;

    private int[]             indeces;

    public ContextMenu(Component parent, Point point, String[] options, ActionListener list) {
        super();
        this.menus= new Vector<JMenuItem>();
        int x = (int) point.getX();
        int y = (int) point.getY();
        for (int i = 0; i < options.length; i++) {
            JMenuItem menu;
            this.menus.add(menu = new JMenuItem(options[i]));
            menu.addActionListener(list);
            this.add(menu);
        }
        this.add(new JSeparator());

        this.show(parent, x, y);
    }

}
