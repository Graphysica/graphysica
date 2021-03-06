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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.AxeHorizontal;
import org.graphysica.espace2d.forme.AxeVertical;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.position.PositionReelle;
import org.graphysica.espace2d.position.PositionVirtuelle;
import org.graphysica.util.SetChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un espace permet d'afficher un ensemble de formes dans un repère. Le repère
 * de l'espace permet de décrire les positions de l'environnement d'édition
 * graphique. Les positions virtuelles sont exprimées en fonction de l'origine
 * virtuelle située au coin supérieur gauche de l'espace et selon un axe
 * vertical orienté négativement.
 * <p>
 * L'espace peut être navigué à l'aide du bouton du milieu de la souris. Le
 * défilement de la souris est associé à la translation du repère, et le
 * roulement de la molette est associée l'échelle du repère.
 * <p>
 * L'espace permet de récupérer l'ensemble des formes qui sont sélectionnées par
 * l'utilisateur selon la distance minimale du curseur à la forme en
 * considération avec l'ordre de rendu des formes. Un gestionnaire de sélections
 * devra s'occuper des sélections ponctuelles et multiples.
 * <p>
 * Les formes graphiques de repérage dans l'espace, qui comprennent les axes et
 * les grilles, sont propres à chaque espace. Il est possible de créer des
 * espaces à partir d'un autre espace qui s'actualise à l'ajout et le retrait de
 * formes dans chaque espace effectuant le rendu d'un même ensemble de formes.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Espace extends ToileRedimensionnable
        implements Actualisable {

    /**
     * L'utilitaire d'enregistrement de traces d'exécution.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Espace.class);

    /**
     * L'ensemble ordonné observable des formes dessinées dans l'espace.
     */
    private final ObservableSet<Forme> formes
            = FXCollections.observableSet(new LinkedHashSet<>());

    /**
     * L'ordre de rendu des formes dams l'espace.
     */
    private final OrdreRendu ordreRendu = new OrdreRendu();

    /**
     * Le repère de l'espace.
     */
    private final Repere repere = new Repere();

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
     * Le facteur de zoom utilisé pour zoomer la toile.
     */
    private static final double FACTEUR_ZOOM = 1.1;

    /**
     * La position précédente du curseur.
     */
    private Position positionPrecendenteCurseur;

    /**
     * La position virtuelle actuelle du curseur.
     */
    private final ObjectProperty<PositionVirtuelle> positionVirtuelleCurseur
            = new SimpleObjectProperty<>(new PositionVirtuelle(Vector2D.ZERO));

    /**
     * La position réelle actuelle du curseur.
     */
    private final ObjectProperty<PositionReelle> positionReelleCurseur
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    {
        repere.echelleProperty().addListener(evenementActualisation);
        repere.origineVirtuelleProperty().addListener(evenementActualisation);
        formes.addListener(new FormesListener());
        formes.addListener(evenementActualisation);
        positionVirtuelleCurseur.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                positionReelleCurseur.setValue(
                        new PositionReelle(getPositionVirtuelleCurseur()
                                .reelle(repere)));
            }
        });
        formes.add(grilleSecondaire);
        formes.add(grillePrincipale);
        formes.add(axeVertical);
        formes.add(axeHorizontal);
        definirInteractionCurseur();
    }
    
    /**
     * Définit les interactions entre l'espace et le curseur.
     */
    private void definirInteractionCurseur() {
        final Cursor curseurParDefaut = Cursor.CROSSHAIR;
        setOnMouseEntered((evenement) -> {
            requestFocus();
            setCursor(curseurParDefaut);
        });
        setOnMouseMoved((evenement) -> {
            actualiserPositionsCurseur(evenement);
            if (formesSurvolees().isEmpty()) {
                setCursor(curseurParDefaut);
            }
        });
        setOnScroll((evenement) -> {
            zoomer(evenement.getDeltaY());
        });
        setOnMousePressed((evenement) -> {
            if (evenement.isMiddleButtonDown()) {
                setCursor(Cursor.CLOSED_HAND);
            }
        });
        setOnMouseDragged((evenement) -> {
            actualiserPositionsCurseur(evenement);
            if (evenement.isMiddleButtonDown()) {
                defiler();
            }
        });
        setOnMouseDragReleased((evenement) -> {
            final Vector2D origineVirtuelle = repere.getOrigineVirtuelle();
            repere.setOrigineVirtuelle(new Vector2D(
                    (int) origineVirtuelle.getX(),
                    (int) origineVirtuelle.getY()));
        });
    }
    
    /**
     * Positionne l'origine de l'espace en son centre.
     */
    public void centrerOrigine() {
        repere.centrer(getWidth(), getHeight());
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
        dessinerFormes(formes);
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
     * Récupère les formes survolées par l'utilisateur en ordre croissant de
     * distance au curseur en considérant l'ordre de rendu des formes. Ce
     * faisant, la forme qui correspond le plus à une sélection ponctuelle de la
     * part de l'utilisateur est le premier élément de l'ensemble récupéré. Il
     * sera possible de déterminer l'intersection entre les premières formes de
     * cet ensemble pour l'outil d'intersection en traversant l'ensemble pour y
     * trouver les deux premières droites ou deux premières figures
     * d'intersection. Il est fort probable que cet ensemble soit vide.
     *
     * @return les formes survolées en ordre croissant de distance.
     */
    public LinkedHashSet<Forme> formesSurvolees() {
        // Ajouter les formes dans l'ordre inverse
        final List<Forme> formesSurvolees = new ArrayList<>();
        final Map<Forme, Double> distances = distancesFormes();
        ordreRendu.stream().map((classe) -> {
            // Retenir les formes de la classe
            final List<Map.Entry<Forme, Double>> formesRetenues
                    = new ArrayList<>();
            distances.entrySet().stream().filter((
                    entree) -> (classe.isInstance(entree.getKey())))
                    .forEach((entree) -> {
                        formesRetenues.add(entree);
                    });
            return formesRetenues;
        }).map((formesRetenues) -> {
            // Trier en ordre décroissant
            formesRetenues.sort((forme1, forme2) -> forme2.getValue()
                    .compareTo(forme1.getValue()));
            return formesRetenues;
        }).forEach((formesRetenues) -> {
            formesRetenues.stream().forEach((entree) -> {
                formesSurvolees.add(entree.getKey());
            });
        });
        Collections.reverse(formesSurvolees);
        return new LinkedHashSet<>(formesSurvolees);
    }

    /**
     * Récupère les distances entre la position actuelle du curseur et les
     * formes sélectionnées.
     *
     * @return l'association des distances aux formes.
     */
    private Map<Forme, Double> distancesFormes() {
        final LinkedHashSet<Forme> formesDistantes
                = new LinkedHashSet<>(formes);
        final Map<Forme, Double> distances = new HashMap<>();
        formesDistantes.stream().filter((forme) -> (forme.isSelectionne(
                getPositionVirtuelleCurseur(), repere))).forEach((forme) -> {
            distances.put(forme, forme.distance(
                    getPositionVirtuelleCurseur(), repere));
        });
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
    private void zoomer(final double defilementVertical) {
        if (defilementVertical < 0) {
            dezoomer();
        } else if (defilementVertical > 0) {
            zoomer();
        }
    }

    /**
     * Zoome l'espace vers la position du curseur.
     */
    public void zoomer() {
        zoomer(getPositionReelleCurseur());
    }

    /**
     * Zoome l'espace vers une position cible.
     *
     * @param cible la cible du zoom.
     */
    public void zoomer(@NotNull final Position cible) {
        zoomer(cible, FACTEUR_ZOOM);
    }

    /**
     * Dézoome l'espace à partir de la position du curseur.
     */
    public void dezoomer() {
        dezoomer(getPositionReelleCurseur());
    }

    /**
     * Dézoome l'espace vers une position cible.
     *
     * @param cible la cible du dézoom.
     */
    public void dezoomer(@NotNull final Position cible) {
        zoomer(cible, 1 / FACTEUR_ZOOM);
    }

    /**
     * Zoome l'espace vers une position cible selon un facteur de zoom. Le
     * facteur de zoom correspond au pourcentage d'agrandissement de l'espace,
     * de telle sorte qu'un facteur plus grand que 1 correspond à un zoom et un
     * facteur entre 0 et 1 exclusivement correspond à un dézoom.
     *
     * @param cible la position cible du zoom.
     * @param facteurZoom le facteur de zoom.
     */
    private void zoomer(@NotNull final Position cible,
            final double facteurZoom) {
        final Vector2D translationOrigine = cible.virtuelle(repere)
                .subtract(repere.getOrigineVirtuelle());
        repere.setOrigineVirtuelle(
                repere.getOrigineVirtuelle().add(translationOrigine));
        repere.setEchelle(repere.getEchelle().scalarMultiply(facteurZoom));
        final Vector2D nouvelleOrigine = repere.getOrigineVirtuelle()
                .subtract(translationOrigine.scalarMultiply(facteurZoom));
        repere.setOrigineVirtuelle(
                new Vector2D((int) nouvelleOrigine.getX(),
                        (int) nouvelleOrigine.getY()));
    }

    /**
     * Défile l'espace selon un défilement virtuel spécifié.
     *
     * @param defilement le défilement de l'espace, exprimé en pixels.
     */
    public void defiler(@NotNull final Vector2D defilement) {
        repere.setOrigineVirtuelle(repere.getOrigineVirtuelle()
                .add(defilement));
    }

    /**
     * Défile l'espace selon le déplacement virtuel du curseur.
     */
    public void defiler() {
        defiler(getDeplacementVirtuelCurseur());
    }

    /**
     * Actualise les positions virtuelles précédente et actuelle du curseur.
     *
     * @param evenement l'événement de la souris.
     */
    private void actualiserPositionsCurseur(
            @NotNull final MouseEvent evenement) {
        final PositionVirtuelle positionCaptee
                = capterPositionActuelleCurseur(evenement);
        if (getPositionVirtuelleCurseur() == null) {
            setPositionVirtuelleCurseur(positionCaptee);
        }
        setPositionPrecedenteCurseur(getPositionVirtuelleCurseur());
        setPositionVirtuelleCurseur(positionCaptee);
    }

    /**
     * Capte la position virtuelle et actuelle du curseur à partir d'un
     * événement de la souris.
     *
     * @param evenement l'événement de la souris.
     * @return la position virtuelle du curseur.
     */
    private PositionVirtuelle capterPositionActuelleCurseur(
            @NotNull final MouseEvent evenement) {
        return new PositionVirtuelle(
                new Vector2D(evenement.getX(), evenement.getY()));
    }

    /**
     * Récupère les formes affichées dans cet espace.
     *
     * @return les formes affichées dans cet espace.
     */
    public Collection<Forme> getFormes() {
        return formes;
    }

    private Position getPositionPrecedenteCurseur() {
        return positionPrecendenteCurseur;
    }

    private void setPositionPrecedenteCurseur(
            @NotNull final Position positionPrecedenteCurseur) {
        this.positionPrecendenteCurseur = positionPrecedenteCurseur;
    }

    /**
     * Récupère la propriété de position réelle du curseur dans l'espace. Permet
     * de lier des positions de forme à la position du curseur pour la
     * prévisualisation ou le déplacement de formes.
     *
     * @return la propriété de position réelle du curseur.
     */
    public ObjectProperty<PositionReelle>
            positionReelleCurseurProperty() {
        return positionReelleCurseur;
    }

    /**
     * Récupère la position actuelle réelle du curseur.
     *
     * @return la position réelle du curseur.
     */
    public PositionReelle getPositionReelleCurseur() {
        return new PositionReelle(getPositionVirtuelleCurseur().reelle(repere));
    }

    /**
     * Récupère la position actuelle virtuelle du curseur.
     *
     * @return la position virtuelle du curseur.
     */
    public PositionVirtuelle getPositionVirtuelleCurseur() {
        return positionVirtuelleCurseur.getValue();
    }

    private void setPositionVirtuelleCurseur(
            @NotNull final PositionVirtuelle positionVirtuelleCurseur) {
        this.positionVirtuelleCurseur.setValue(positionVirtuelleCurseur);
    }

    /**
     * Récupère le déplacement virtuel du curseur dans l'espace. Ce déplacement
     * correspond à la distance vectorielle virtuelle de la position précédente
     * du curseur vers la position actuelle du curseur.
     *
     * @return le déplacement virtuel du curseur dans l'espace.
     */
    private Vector2D getDeplacementVirtuelCurseur() {
        return getPositionVirtuelleCurseur().virtuelle(repere)
                .subtract(getPositionPrecedenteCurseur().virtuelle(repere));
    }

    /**
     * Récupère le déplacement réel du curseur dans l'espace. Ce déplacement
     * correspond à la distance vectorielle réelle de la position précédente du
     * curseur vers la position actuelle du curseur.
     *
     * @return le déplacement réel du curseur dans l'espace.
     */
    public Vector2D getDeplacementReelCurseur() {
        return getPositionVirtuelleCurseur().reelle(repere)
                .subtract(getPositionPrecedenteCurseur().reelle(repere));
    }
    
    /**
     * L'événement d'actualisation de l'ensemble des formes de l'espace. Lie
     * l'événement d'actualisation de l'espace aux formes ajoutées à la liste,
     * ou retire l'événement d'actualisation de l'espace aux formes retirées de
     * la liste.
     */
    private class FormesListener extends SetChangeListener<Forme> {

        @Override
        public void onAdd(@NotNull final Forme forme) {
            forme.getProprietes().stream().forEach((propriete) -> {
                propriete.addListener(evenementActualisation);
            });
        }

        @Override
        public void onRemove(@NotNull final Forme forme) {
            forme.getProprietes().stream().forEach((propriete) -> {
                propriete.removeListener(evenementActualisation);
            });
        }

    }

}
