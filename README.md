<p align='center'>
	<img height='150' alt='Graphysica' src='https://rawgit.com/Graphysica/graphysica/master/graphysica.svg' />
</p>
<p align='center'>
	<a alt='Problèmes du projet' href='https://github.com/Graphysica/graphysica/issues'><img alt='GitHub issues' src='https://img.shields.io/github/issues/Graphysica/graphysica.svg' /></a>
	<a alt='License GPL-3.0' href='https://github.com/Graphysica/graphysica/blob/master/LICENSE'><img alt='GitHub license' src='https://img.shields.io/github/license/Graphysica/graphysica.svg' /></a>
	<a alt='Statistiques du projet' href='http://githubstats.com/Graphysica/graphysica'><img alt='GitHub Stats' src='https://img.shields.io/badge/github-stats-ff5500.svg' /></a>
</p>

Graphysica est un logiciel de simulation de physique mécanique multiplateforme développé en Java. Il permet de modéliser des situations de physique classique en deux dimensions à l'aide d'outils mathématiques compréhensifs.

## Pour débuter

Clonez le projet puis ouvrez-le dans votre environnement de développement Java:

`git clone git://github.com/Graphysica/graphysica.git Graphysica`

### Prérequis

* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/)

## Déploiement

Exécutez la commande pour générer un `.jar` du logiciel dans le répertoire `./target/`:

`mvn install`

## Réalisé avec

* [Commons Math](http://commons.apache.org/proper/commons-math/) - Librairie de mathématiques, principalement utilisée pour ses modules d'algèbre linéaire
* [JBox2D](http://www.jbox2d.org/) - Librairie de simulation de physique
* [Apache POI](https://poi.apache.org/) - Interface de programmation avec des fichiers de la gamme MicrosoftⓇ Office
* [JLaTeXMath](https://github.com/opencollab/jlatexmath) - Librairie de rendu d'équations [LaTeX](https://www.latex-project.org/)
* [Scene Builder](http://gluonhq.com/products/scene-builder/) - Éditeur graphique de FXML
* [Maven](https://maven.apache.org/) - Gestionnaire de dépendances
* [JUnit](https://junit.org) - Librairie de test

## Auteurs

* **Marc-Antoine Ouimet** - [MartyO256](https://github.com/MartyO256)
* **Victor Babin** - [vicbab](https://github.com/vicbab)

## License

Graphysica est un logiciel libre; vous pouvez le redistribuer ou le modifier en accordance avec les termes de la 
GNU General Public License telle que publiée par la Free Software Foundation.

Graphysica est distribué librement, mais AUCUNE GARANTIE N'EST ÉMISE QUANT À SON FONCTIONNEMENT.
Consultez la [GNU General Public License](https://www.gnu.org/licenses/gpl-3.0.en.html) ou le fichier [LICENSE](https://github.com/Graphysica/graphysica/blob/master/LICENSE) pour plus de détails.

![license](https://www.gnu.org/graphics/gplv3-127x51.png)

## Remerciements

* [GeoGebra](https://www.geogebra.org)
