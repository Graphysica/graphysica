/*
 * Copyright (C) 2018 Graphysica
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graphysica.espace2d;

import org.graphysica.espace2d.forme.OrdreRendu;
import org.graphysica.espace2d.forme.Grille;
import org.graphysica.espace2d.forme.Forme;
import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.AxeHorizontal;
import org.graphysica.espace2d.forme.AxeVertical;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.position.PositionReelle;
import org.graphysica.espace2d.position.PositionVirtuelle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un espace permet d'afficher un ensemble de formes dans un repère.
 *
 * @author Marc-Antoine Ouimet
 */
public class Espace extends ToileRedimensionnable implements Actualisable {

    /**
     * L'utilitaire d'enregistrement de traces d'exécution.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Espace.class);

    /**
     * L'ensemble ordonné observable des formes dessinées dans l'espace.
     */
    protected final Formes formes;

    /**
     * L'ordre de rendu des formes dams l'espace.
     */
    protected final OrdreRendu ordreRendu = new OrdreRendu();

    /**
     * Le repère de l'espace.
     */
    protected final Repere repere;

    /**
     * La grille principale de l'espace.
     */
    private final Grille grillePrincipale = new Grille(new Vector2D(100, 100),
            Color.gray(0.5));

    /**
     * La grille secondaire de l'espace.
     */
    private final Grille grilleSecondaire = new Grille(new Vector2D(25, 25),
            Color.gray(0.9));

    /**
     * L'axe vertical de l'espace.
     */
    private final AxeVertical axeVertical = new AxeVertical(100);

    /**
     * L'axe horizontal de l'espace.
     */
    private final AxeHorizontal axeHorizontal = new AxeHorizontal(100);

    /**
     * L'ensemble des formes de repérage de cet espace.
     */
    private final Set<Forme> reperage = new HashSet<>();

    /**
     * Le facteur de zoom utilisé pour zoomer la toile.
     */
    private static final double FACTEUR_ZOOM = 1.1;

    /**
     * La position précédente du curseur.
     */
    private Position positionPrecendenteCurseur;

    /**
     * La position actuelle du curseur.
     */
    private final ObjectProperty<Position> positionCurseur
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * Construit un espace dont les dimensions virtuelles et l'ensemble de
     * formes sont définis.
     *
     * @param largeur la largeur de l'espace exprimée en pixels.
     * @param hauteur la hauteur de l'espace exprimée en pixels.
     * @param formes l'ensemble des formes de l'espace.
     */
    private Espace(final double largeur, final double hauteur,
            @NotNull Formes formes) {
        super(largeur, hauteur);
        repere = new Repere(largeur, hauteur);
        repere.echelleProperty().addListener(evenementActualisation);
        repere.origineVirtuelleProperty().addListener(evenementActualisation);
        this.formes = formes;
        this.formes.ajouterEspace(this);
        definirInteractionCurseur();
    }

    /**
     * Construit un espace dont les dimensions virtuelles sont définies.
     *
     * @param largeur la largeur de l'espace exprimée en pixels.
     * @param hauteur la hauteur de l'espace exprimée en pixels.
     */
    public Espace(final double largeur, final double hauteur) {
        this(largeur, hauteur, new Formes());
    }

    /**
     * Construit un nouvel espace comprenant les formes d'un autre espace
     * défini.
     *
     * @param espace l'espace à dupliquer.
     */
    public Espace(@NotNull final Espace espace) {
        this(espace.getWidth(), espace.getHeight(), espace.formes);
    }

    {
        reperage.add(grilleSecondaire);
        reperage.add(grillePrincipale);
        reperage.add(axeVertical);
        reperage.add(axeHorizontal);
    }

    /**
     * Définit les interactions entre l'espace et le curseur.
     */
    private void definirInteractionCurseur() {
        setOnMouseEntered((@NotNull final MouseEvent evenement) -> {
            requestFocus();
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseMoved((@NotNull final MouseEvent evenement) -> {
            actualiserPositionsCurseur(evenement);
        });
        setOnScroll((@NotNull final ScrollEvent evenement) -> {
            zoomer(evenement.getDeltaY());
        });
        setOnMousePressed((@NotNull final MouseEvent evenement) -> {
            if (evenement.isMiddleButtonDown()) {
                setCursor(Cursor.CLOSED_HAND);
            }
        });
        setOnMouseReleased((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseDragged((@NotNull final MouseEvent evenement) -> {
            actualiserPositionsCurseur(evenement);
            if (evenement.isMiddleButtonDown()) {
                defiler();
            }
        });
        setOnMouseDragReleased((@NotNull final MouseEvent evenement) -> {
            final Vector2D origineVirtuelle = repere.getOrigineVirtuelle();
            repere.setOrigineVirtuelle(new Vector2D(
                    (int) origineVirtuelle.getX(),
                    (int) origineVirtuelle.getY()));
        });
    }

    /**
     * Actualise l'affichage de cette toile en redessinant chacune de ses
     * formes. Si la classe d'une forme ne fait pas partie des définitions de
     * l'ordre de rendu, elle n'est pas dessinée.
     *
     * @see Espace#ordreRendu
     * @see OrdreRendu
     */
    @Override
    public void actualiser() {
        effacerAffichage();
        dessinerFormes(reperage);
        dessinerFormes(formes());
    }

    /**
     * Dessine une collection de formes sur l'espace.
     *
     * @param formes la collection des formes à dessiner.
     */
    private void dessinerFormes(@NotNull final Collection<Forme> formes) {
        int formesAffichables = 0;
        for (final Class classe : ordreRendu) {
            for (final Forme forme : formes) {
                if (classe.isInstance(forme)) {
                    formesAffichables++;
                    if (forme.isAffiche()) {
                        forme.dessiner(this, repere);
                    }
                }
            }
        }
        if (formesAffichables != formes.size()) {
            LOGGER.warn(String.format("Des classes de formes ne sont pas "
                    + "comprises dans l'ordre de rendu de la toile. "
                    + "%d formes sur %d sont affichables.", formesAffichables,
                    formes.size()));
        }
    }

    /**
     * Réinitialise l'image rendue par cette toile.
     */
    private void effacerAffichage() {
        getGraphicsContext2D().setFill(Color.WHITE);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Récupère les formes sélectionnées par l'utilisateur en ordre croissant de
     * distance au curseur en considérant l'ordre de rendu des formes. Ce
     * faisant, la forme qui correspond le plus à une sélection ponctuelle de la
     * part de l'utilisateur est le premier élément de l'ensemble récupéré. Il
     * sera possible de déterminer l'intersection entre les premières formes de
     * cet ensemble pour l'outil d'intersection en traversant l'ensemble pour y
     * trouver les deux premières droites ou deux premières figures
     * d'intersection. Il est fort probable que cet ensemble soit vide.
     *
     * @return les formes sélectionnées en ordre croissant de distance.
     */
    public Set<Forme> formesSelectionnees() {
        // Ajouter les formes dans l'ordre inverse
        final List<Forme> formesSelectionnees = new ArrayList<>();
        final Map<Forme, Double> distances = distancesFormes();
        for (final Class classe : ordreRendu) {
            // Retenir les formes de la classe
            final List<Map.Entry<Forme, Double>> formesRetenues
                    = new ArrayList<>();
            for (final Map.Entry<Forme, Double> entree : distances.entrySet()) {
                if (classe.isInstance(entree.getKey())) {
                    formesRetenues.add(entree);
                }
            }
            // Trier en ordre décroissant
            formesRetenues.sort((forme1, forme2)
                    -> forme2.getValue().compareTo(forme1.getValue()));
            for (final Map.Entry<Forme, Double> entree : formesRetenues) {
                formesSelectionnees.add(entree.getKey());
            }
        }
        Collections.reverse(formesSelectionnees);
        return new LinkedHashSet<>(formesSelectionnees);
    }

    /**
     * Récupère les distances entre la position actuelle du curseur et les
     * formes sélectionnées.
     *
     * @return l'association des distances aux formes.
     */
    private Map<Forme, Double> distancesFormes() {
        final LinkedHashSet<Forme> formesDistantes
                = new LinkedHashSet<>(reperage);
        formesDistantes.addAll(formes());
        final Map<Forme, Double> distances = new HashMap<>();
        for (final Forme forme : formesDistantes) {
            if (forme.isSelectionne(getPositionCurseur(), repere)) {
                distances.put(forme, forme.distance(
                        getPositionCurseur(), repere));
            }
        }
        return distances;
    }

    /**
     * Zoome l'espace de la toile vers la position définie du curseur. Un zoom a
     * lieu si le défilement vertical est positif, un dézoom a lieu si le
     * défilement vertical est négatif, et aucun zoom n'a lieu si le défilement
     * vertical est nul.
     *
     * @param defilementVertical le défilement vertical du zoom.
     */
    public void zoomer(final double defilementVertical) {
        if (defilementVertical != 0) {
            final Vector2D translationOrigine = getPositionCurseur()
                    .virtuelle(repere).subtract(repere.getOrigineVirtuelle());
            repere.setOrigineVirtuelle(
                    repere.getOrigineVirtuelle().add(translationOrigine));
            double facteurZoom = FACTEUR_ZOOM;
            if (defilementVertical < 0) {
                facteurZoom = 1 / FACTEUR_ZOOM;
            }
            repere.setEchelle(repere.getEchelle().scalarMultiply(
                    facteurZoom));
            final Vector2D nouvelleOrigine = repere.getOrigineVirtuelle()
                    .subtract(translationOrigine.scalarMultiply(facteurZoom));
            repere.setOrigineVirtuelle(
                    new Vector2D((int) nouvelleOrigine.getX(),
                            (int) nouvelleOrigine.getY()));
        }
    }

    /**
     * Défile l'espace de la toile selon la variation des positions du curseur.
     */
    private void defiler() {
        final Vector2D deplacement = getPositionCurseur().virtuelle(repere)
                .subtract(getPositionPrecedenteCurseur().virtuelle(repere));
        repere.setOrigineVirtuelle(repere.getOrigineVirtuelle()
                .add(deplacement));
    }

    /**
     * Actualise les positions virtuelles précédente et actuelle du curseur.
     *
     * @param evenement l'événement de la souris.
     */
    private void actualiserPositionsCurseur(
            @NotNull final MouseEvent evenement) {
        final Position positionCaptee
                = capterPositionActuelleCurseur(evenement);
        if (getPositionCurseur() == null) {
            setPositionCurseur(positionCaptee);
        }
        setPositionPrecedenteCurseur(getPositionCurseur());
        setPositionCurseur(positionCaptee);
    }

    /**
     * Capte la position virtuelle et actuelle du curseur à partir d'un
     * événement de la souris.
     *
     * @param evenement l'événement de la souris.
     * @return la position virtuelle du curseur.
     */
    private Position capterPositionActuelleCurseur(
            @NotNull final MouseEvent evenement) {
        return new PositionVirtuelle(
                new Vector2D(evenement.getX(), evenement.getY()));
    }

    /**
     * Récupère le module de gestion des formes de l'espace.
     *
     * @return le module de gestion des formes de l'espace.
     */
    public final Formes getFormes() {
        return formes;
    }

    /**
     * Récupère l'ensemble des formes affichées dans cet espace.
     *
     * @return l'ensemble des formes affichées dans cet espace.
     */
    private Set<Forme> formes() {
        return formes.get();
    }

    private Position getPositionPrecedenteCurseur() {
        return positionPrecendenteCurseur;
    }

    private void setPositionPrecedenteCurseur(
            @NotNull final Position positionPrecedenteCurseur) {
        this.positionPrecendenteCurseur = positionPrecedenteCurseur;
    }

    private Position getPositionCurseur() {
        return positionCurseur.getValue();
    }

    private void setPositionCurseur(@NotNull final Position positionCurseur) {
        this.positionCurseur.setValue(positionCurseur);
    }

    /**
     * Récupère la propriété de position du curseur dans l'espace. Permet de
     * lier des positions de forme à la position du curseur pour la
     * prévisualisation ou le déplacement de formes.
     * <p>
     * La position du curseur est virtuelle. Par conséquent, il faudra récupérer
     * la position réelle du curseur pour l'instantiation d'objets à sérialiser
     * dans la construction.
     *
     * @return la propriété de position virtuelle du curseur.
     * @see EspaceInteractif#positionReelleCurseur() la position réelle du
     * curseur à utiliser pour la création d'éléments de construction.
     */
    public final ObjectProperty<Position> positionCurseurProperty() {
        return positionCurseur;
    }

    /**
     * Récupère la position actuelle réelle du curseur.
     *
     * @return la position réelle du curseur.
     */
    public final Vector2D positionReelleCurseur() {
        return getPositionCurseur().reelle(repere);
    }

    /**
     * Récupère la position actuelle virtuelle du curseur.
     *
     * @return la position virtuelle du curseur.
     */
    public final Vector2D positionVirtuelleCurseur() {
        return getPositionCurseur().virtuelle(repere);
    }

}
