package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LayoutLignes implements LayoutManager {
    private final boolean largeurMax;
    private int maxWidth;


    public LayoutLignes() {
	this(true);
    }

    public LayoutLignes(boolean largeurMax) {
	this.largeurMax = largeurMax;
	maxWidth = Integer.MAX_VALUE;
    }

    public LayoutLignes setMaxWidth(int maxWitdh) {
	this.maxWidth = maxWitdh;
	return this;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void layoutContainer(Container parent) {
	int y = 0;
	for(final Component c : parent.getComponents()) {
	    int w, h;
	    if(c.isVisible()) {
		w = Math.min(maxWidth, largeurMax ? parent.getWidth() : Math.max(c.getMinimumSize().width, c.getPreferredSize().width));
		h = Math.max(c.getMinimumSize().height, c.getPreferredSize().height);
	    } else {
		w = 0;
		h = 0;
	    }
	    c.setBounds((parent.getWidth() - w)/2, y, w, h);
	    y += h;
	}
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
	Dimension d = null;
	for(final Component c : parent.getComponents())
	    if(d == null)
		d = c.getMinimumSize();
	    else {
		if(c.getMinimumSize().width > d.width)
		    d.width = Math.min(maxWidth, c.getMinimumSize().width);
		d.height += c.getMinimumSize().height;
	    }
	return d == null ? new Dimension() : d;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
	Dimension d = null;
	for(final Component c : parent.getComponents())
	    if(d == null)
		d = c.getPreferredSize();
	    else {
		if(c.getPreferredSize().width > d.width)
		    d.width = Math.min(maxWidth, c.getPreferredSize().width);
		if(c.isVisible())
		    d.height += c.getPreferredSize().height;
	    }
	return d == null ? new Dimension(150, 25 * parent.getComponentCount() + 30) : d;
    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }



}
