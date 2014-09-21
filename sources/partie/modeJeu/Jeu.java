package partie.modeJeu;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.Component;
import java.util.List;

import listeners.DestructibleListener;
import partie.PartieServeur;
import partie.modeJeu.scorable.Kill;
import partie.modeJeu.scorable.Mort;
import partie.modeJeu.scorable.TypeScorable;
import perso.InterfacePerso;
import perso.Perso;
import perso.Vivant;
import physique.PhysiqueDestructible;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourcesServeur;
import reseau.serveur.Serveur;
import temps.Evenement;
import temps.EvenementTempsPeriodique;
import temps.GestionnaireEvenements;
import divers.Listenable;

public abstract class Jeu extends Listenable implements Lancable, Fermable, DestructibleListener {
    private final Serveur serveur;


    public Jeu(Serveur serveur) {
	this.serveur = serveur;
    }

    public abstract TypeJeu getType();
    public abstract List<Component> getComposants(RessourcesServeur r);
    public abstract int nextIDEquipe(RessourcePerso r);
    public abstract int getValeur(TypeScorable type);

    public RessourcesServeur getRessources() {
	return serveur.getRessources();
    }

    public Serveur getServeur() {
	return serveur;
    }

    @Override
    public void vieChange(PhysiqueDestructible p) {}

    @Override
    public void meurt(final PhysiqueDestructible p, Vivant tueur) {
	PartieServeur ps = serveur.getPartie();
	int id = serveur.getRessources().getIDPerso(p);
	int idTueur = tueur == null ? id : serveur.getRessources().getIDPerso(tueur);
	if(idTueur != id)
	    ps.addScorable(idTueur, new Kill(getValeur(TypeScorable.KILL), id));
	ps.addScorable(id, new Mort(getValeur(TypeScorable.MORT), idTueur));
	ps.addEvenement(new Evenement(5000, (EvenementTempsPeriodique e, GestionnaireEvenements g) -> ps.spawn(id)));
    }

    public static Component getInterface(Perso p) {
	return new InterfacePerso(false, p);
    }

    public static Jeu getJeu(TypeJeu type, Serveur serveur) {
	switch(type) {
	case DEATHMATCH: return new DeathMatch(serveur);
	default: throw new IllegalArgumentException(type + " non implemente");
	}
    }

}
