package ecrans;

import interfaces.Localise;
import interfaces.LocaliseDessinable;
import interfaces.LocaliseEquipe;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import listeners.SourisListener;
import listeners.ZoomListener;
import map.MapDessinable;
import statique.Style;
import vision.Camera;
import vision.ReticuleSelection;
import base.Ecran;
import divers.Outil;
import exceptions.ExceptionJeu;
import exceptions.HorsLimiteException;

public class ContainerMap<E extends LocaliseDessinable> extends Ecran {
    private static final long serialVersionUID = 1L;
    private final ZoomListener zoom;
    private final Camera cam;
    private MapDessinable<E> map;
    private ReticuleSelection r;
    private SourisListener l;
    private boolean stop;


    public ContainerMap(MapDessinable<E> map, Camera cam) {
	this(cam);
	this.map = map;
	setIgnoreRepaint(true);
    }

    public ContainerMap(LocaliseEquipe source) {
	this(new Camera());
	cam.setSource(source);
    }

    public ContainerMap(Camera cam) {
	setName("Map");
	this.cam = cam;
	cam.setEcran(this);
	setOpaque(false);
	addMouseWheelListener(zoom = new ZoomListener(cam));
    }

    public MapDessinable<E> getMap() {
	return map;
    }

    public void setMap(MapDessinable<E> map) {
	this.map = map;
	try {
	    centrer();
	} catch(ExceptionJeu e) {
	    e.printStackTrace();
	}
    }

    public void centrer() throws HorsLimiteException {
	cam.setX(Localise.UNITE.width * (map.getLargeur() - 2)/2);
	cam.setY(Localise.UNITE.height);
    }

    public Camera getCamera() {
	return cam;
    }

    public void setReticule(ReticuleSelection r) {
	this.r = r;
	l = new SourisListener(r, r);
	addMouseListener(l);
	addMouseMotionListener(l);
	if(map != null)
	    map.ajoutDessinable(r);
    }

    public void removeReticule(ReticuleSelection r) {
	removeMouseListener(l);
	removeMouseMotionListener(l);
	if(map != null)
	    map.removeDessinable(r);
	this.r = null;
    }

    public void removeSelectionListener() {
	removeMouseListener(l);
	removeMouseMotionListener(l);
	if(map != null)
	    map.removeDessinable(r);
    }

    public void stop() {
	stop = true;
    }

    @Override
    public void paintComponent(Graphics g) {
	g.setFont(Style.POLICE);
	if(map != null) {
	    if(MapDessinable.HD) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    }
	    try {
		map.dessiner(g, cam);
	    } catch(Exception err) {
		err.printStackTrace();
	    }
	    if(!stop) {
		Outil.wait(1);
		repaint();
	    }
	}
    }

    @Override
    public boolean fermer() {
	removeMouseWheelListener(zoom);
	return true;
    }

}
