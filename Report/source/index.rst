.. Rapport documentation master file, created by
   sphinx-quickstart on Mon May 22 09:06:27 2017.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Rapport du travail de bachelor
==============================

.. raw:: latex
    \begin{abstract}

    Ce rapport présente la mise en place d'un pipeline de deeplearning dédié à la classification
    d'image IRM du cerveau. Ce travail est une collaboration entre la HES de Neuchâtel et le Laboratoire de Recherche En Neuro-imagerie
    qui gère la Plateforme d'Informatique Médicale du Human Brain Project. Ce rapport retrace la réalisation de ce pipeline. Ce dernier est
    capable de classifier des images IRM du cerveau, selon si ils ont ou non la maladie d'Alzheimer, avec un taux de précision de près de 70%.
    Ce projet pourra être repris et améliorer par le Laboratoire de Recherche En Neuro-imagerie et intégrer à leur plateforme. Il pourrait
    ouvrir de nouvelle perspective dans la recherche neurologique et le traitement de maladies liées au cerveau.

    \begin{center}
    \textbf{Abstract}
    \end{center}

    This report presents the implementation of a deeplearning pipeline dedicated to classification
    image of the MRI of the brain. This work is a collaboration between the Neuchâtel HES and the Neuroimaging Research Laboratory
    which manages the Medical Informatics Plateform of the Human Brain Project. This report traces the completion of this pipeline. This last one is
    capable of classifying MRI images of the brain, depending on whether or not they have Alzheimer's disease, with an accuracy rate of nearly 70%.
    This project can be taken up and improved by the Neuroimaging Research Laboratory and integrated into their platform. He could
    Open new perspective in neurological research and treatment of brain related diseases.

    \end{abstract}

Introduction
==================
Ce rapport présente un projet développé dans le cadre du Travail de Bachelor, au sein de la HES de Neuchâtel
et en collaboration avec le Human Brain Project et le Laboratoire de Recherche en Neuro-imagerie (LREN)
du CHUV de Lausanne. Il a été développé par Monsieur Nicolas Sommer sous la supervision de Monsieur
Fabrizio Albertetti. Ce travail est un mandat de la Medical Informatics Platform du Human Brain Project,
la personne de contact et mandant est Arnaud Jutzeler.

Ce travail portait sur une durée de 450h et c'est déroulé sur 10 semaines durant le semestre de 
printemps 2017 à raison de 5h par semaine puis sur une durée de 10 semaines a raison de 8h par
jour.

Les premières semaines furent donc consacrées à l'analyse préliminaire du projet, aux choix et à
l'apprentissage des technologies qui seront employés. Le seconde partie fut consacré à l'implémentation
et aux expériences.

Le reste de ce rapport présente les contraintes et les analyses préliminaires effectuées pour préparer
la suite du projet, la conception et l'implémentation du programme lié au projet ainsi qu'une expérience
qui a été réalisé à l'aide de ce dernier. Il se concluera par un rapide survole de ce qui est fonctionnelle,
de ce qu'il reste à faire et des améliorations possibles.

La suite de ce chapitre expose le contexte du travail, les buts de ce projet et les objectifs qui ont été fixés pour
celui-ci et une note sur la confidentialité liée à ce projet.

Contexte du travail
--------------------
Le Human Brain Project (HBP) a pour but d'enrichir les connaissances humaines en matière de neuro-science
en cherchant à mieux comprendre les mécanismes du cerveau humain. Ce projet s'inscrit dans le cadre du
programme européen pour la recherche et l'innonvation Horizon 2020 et vise à accélerer les domaines des neuro-
sciences, de l'informatique et de la médecine liée au cerveau. La première étape du Human Brain Project veut
mettre à disposition des chercheurs un portail web. Ce portail web sera constitué d'un total de 6 plateformes
de recherche. Celles-ci porteront sur la neuro-informatique, la simulation du cerveau, le calcul à haute performance,
l'informatique médicale, l'informatique neuromorphique et la neuro-robotique.

.. figure:: images/mip4forweb.png
   :width: 350px
   :align: center
   :alt: Vue d'ensemble du Human Brain Project

   Vue d'ensemble du Human Brain Project

Le département des Neuro-sciences Cliniques du CHUV est chargé de la plateforme d'Informatique Médicale. Celle-ci
est une plateforme open-source permettant aux hôpitaux et aux centre de recherche de partager des données médicales.
Elle permettra aux utilisateurs d'avoir accès à des informations précises et pertinentes sur les maladies liées au
cerveau en préservant la confidentialité des patients. Cette plateforme servira donc de pont entre la recherche en
neuro-sciences, la recherche clinique et les soins aux patients. Elle pourrait également permettre la découverte de
mécanisme à différentes échelles qui expliquerait l'apparition et le développement de maladies cérébrales.

C'est dans ce cadre que l'équipe du Laboratoire de Recherche En Neuro-imagerie cherche à développer un ensemble d'
outils pour l'acquisition, le traitement et l'analyse des données. Elle cherche, entre autre chose, à pouvoir automatiser
aussi efficacement que possible les diagnostiques de maladie pouvant atteindre le cerveau, tel que les maladies
d'Alzheimer ou de Parkinson par exemple. Dans l'état actuel, cette analyse peut se faire avec des méthodes de machine learning.
Toutefois, il n'existe pas encore de méthode d'apprentissage profond disponible sur la plateforme et le LREN aimerait
pouvoir proposé cette option aux utilisateurs de la plateforme.

.. figure:: images/mip_dashboard.png
   :width: 350px
   :align: center
   :alt: Dashboard de la Plateforme d'Informatiques Médical

   Dashboard de la Plateforme d'Informatiques Médical

C'est dans ce but que la Haute-Ecole Arc de Neuchâtel a été contacté et ce projet proposé comme travail de diplôme à
un étudiant de troisième année.

Problème à résoudre et but du projet
------------------------------------
Actuellement, la plateforme rassemble un certain nombre d'images d'IRM. Celles-ci sont stockés sous la forme de DICOM ou
de fichier au format NIFTI. Le DICOM est une norme standard pour la gestion informatique des données issues de l'imagerie
médicale. N'étant pas employé dans le reste du projet, il ne sera pas plus détaillé ici. Le format NIFTI est un format d'
image IRM mis en place par quelques uns des acteurs les plus influents de la neuro-imagerie. Etant le format principalement
employé dans ce projet, il fera l'objet d'une description détaillé dans la partie consacré aux analyses préliminaires.

.. figure:: images/Brain_IRM.png
   :width: 350px
   :align: center
   :alt: Illustration d'une IRM du cerveau au format NIFTI

   Illustration d'une IRM du cerveau au format NIFTI

Afin d'être employé par les outils d'automatisation de diagnostique mis en place par la plateforme d'informatique médicale,
les images ont besoin d'être pré-traité. En effet, les outils de machine learning utilisé pour le diagnostique fonctionne
en se basant sur un certain nombre de caractéristiques du cerveau. Ces caractéristiques peuvent être le volume de matière
grise ou de matière blanche d'une zone spécifique du cerveau, la quantité de liquide cérébro-spinal, le volume du cerveau, etc.
Il faut donc extraire ces informations des images à disposition. Ceci se fait à l'aide du framework SPM. Ce framework permet
de récupérer des images d'une de ces caractéristiques séparées des autres. Il permet, par exemple, de récupérer une image IRM de la matière grise du
cerveau au format NIFTI. SPM permet également de créer un atlas des caractéristiques. Ainsi l'atlas fait correspondre,
sous la forme de tableau, un certain nombre de quantité caractéristique à chaque région du cerveau. Ce prétraitement fonctionne
à l'intérieur de la "data factory". Cette dernière est connecté à ce que le LREN appelle l'"algorithm factory".

.. figure:: images/IMP_ActualDataFlow.png
   :width: 350px
   :align: center
   :alt: Flux des données actuellement en place dans la plateforme d'informatique médicale

   Flux des données actuellement en place dans la plateforme d'informatique médicale

Une fois ce pré-traitement effectué, les données sont prêtes pour être utilisé par l'"algorithm factory". Cette dernière correspond
à l'ensemble des outils de diagnostique de la plateforme. Ces outils sont un ensemble d'algorithme de machine learning.

Le problème principal de cette manière de faire est qu'il existe une quantité non-négligeable d'information qui sont perdu au cours
du pré-traitement. L'idée du LREN est donc de trouver une solution pour traiter directement les images entières avec des outils de
diagnostique automatique.

Pour se faire, ils proposent de mettre en place une extension de l'"algorithm factory". Cette extension permettra d'appliquer des algorithmes
pour l'apprentissage de modèles de deeplearning et de faire valider ces derniers directement sur les images d'IRM.

.. figure:: images/Extension_MIP.png
   :width: 350px
   :align: center
   :alt: Flux des données avec extension imaginé dans la plateforme d'informatique médicale

   Flux des données avec extension imaginé dans la plateforme d'informatique médicale

Ce projet vise donc à explorer la possibilité de mettre en place cette extension. Il mettra en place un workflow alternatif à celui
existant dans l'"algorithm factory". Cette alternative a pour contrainte de permettre de lancer de nouveaux algorithmes travaillant
directement sur les images en utilisant le framework de calcul distribué Apache-Spark. Cette nouvelle fonctionnalitée sera illustrée
par l'intégration d'une bibliothèque de deep-learning et fera l'objet d'une expérience avec de véritable image d'IRM.

Rappel des objectifs du projet
------------------------------
Les premières semaines du projet ont été utilisé afin de fixer les objectifs principaux et secondaires de ce projet. Ainsi, les objectifs
principaux de ce travail sont: 

1. L'installation et la prise en main d'Apache-Spark
2. L'intégration d'Apache-Spark à l'"algorithm factory"
3. L'interfaçage des bases de données d'image de la plateforme à Apache-Spark. Ces bases de données sont a créer et a améliorer si besoin
   durant le projet.
4. Faire un état de l'art technique sur les différentes bibliothèques de deeplearning compatible avec Apache-Spark pour obtenir suffisamment
   d'information pour permettre le choix de l'une d'entre elles à intégrer au-dessus de Spark.
5. Traduire la partie prédictive de l'algorithme au format PFA (Portable Format for Analytics).
6. Tester les nouvelles fonctionnalités avec une expérience concrète fournit par le LREN. Cette expérience utilisera des images d'IRM utilisé
   par le laboratoire. Elle consistera en une classification de ces images. 


En plus de ces objectifs principaux, s'ajoute un objectif optionnel. Celui-ci consiste a étendre le portail web de la plateforme pour
permettre l'utilisation des nouvelles fonctionnalités de l'*algorithm factory*.

Il faut tout même signalé que ces objectifs ont évolué au cours du projet. Ces changements seront expliqué durant les chapitres concernant
la conception et l'implémentation du projet. (Attention a bien les expliquer)

Note sur la confidentialité au cours du projet
-----------------------------------------------
Comme déjà rappelé dans le cahier des charges de ce travail, l’aspect de l’utilisation d’image extraite d’IRM est un aspect sensible du point
de vue de la confidentialité.

Pour pallier tous soucis de confidentialité, les images employées durant la phase de développement étaient des images totalement ouvertes même
si ces dernières ne sont pas des images issues d’IRM. 

Ainsi, certaines images sont générées par le programme et lorsque des images d'IRM ont été nécessaire, elles ont été téléchargées sur le site
"https://openfmri.org/". Ce site répertorie un certain nombre de dataset totalement libre de scanner IRM du cerveau.

Pour l'expérience effectué avec les données du CHUV, le LREN a fournit un certain nombre d'image. Toutefois, ces images n'ont pas le droit d'être
diffusé. Ce rapport et la présentation ne contiendra alors aucune de ces images, pour des raisons de respects du secret médical.

Analyses préliminaires
======================
Le format NIFTI
----------------
Ce travail est un projet de neuro-imagerie, il est donc naturel de devoir travailler avec des
images IRM du cerveau. Le format utilisé par le CHUV pour les images est le format NIFTI
(Neuroimaging Informatics Technology Initiative), un format d'image très spécialisé mais
également très répandu dans ce domaine. 

.. figure:: images/brain_IRM.png
   :width: 350px
   :align: center
   :alt: Illustration d'une image IRM d'un cerveau

   Illustration d'une image IRM d'un cerveau

Ce chapitre présente donc ce format afin de mieux le comprendre. Pour faire celà, nous
allons voir l'origine du format, une vue d'ensemble des principales caractéristiques du format
et quelques outils qui ont été utiles à la réalisation de ce travail.

Origine du format NIFTI
***********************
NIFTI est un format de fichier pour sauvegarder des données d'IRM. Il fonctionne
sur le principe des voxels et est multidimensionnel.

Ce format a été imaginé il y a une dizaine d'année pour remplacer le format ANALYZE 7.5.
ANALYZE 7.5 était très utilisé mais était également très problématique. Le soucis principal de
ce format étant le manque d'information sur l'orientation dans l'espace de l'élément scanné.
Les données enregistrées ne pouvaient donc pas être lu et interprêté sans ambiguité. A cause
de ce manque d'information, il existait principalement une confusion entre le côté droit et le
côté gauche du cerveau. 

Deux conférences furent alors mises en place par quelques-uns des concepteurs des plus grands
logiciels de neuroimagerie. Ces deux conférences, le Data Format Working Group (DFWG), se sont
réunit au "National Institue of Health" (NIH) pour trouver un format de remplacement. De ces
réunions naquit le format NIFTI. Celui-ci intégre de nouvelles informations et est devenu
le nouveau standard de neuroimagerie.

Vue d'ensemble du format NIFTI
******************************
Le format ANALYZE 7.5 avait besoin de deux fichiers pour fonctionner. Un fichier "\*.hdr" contenant
le header pour stocker les méta-données et un fichier "\*.img" contenant les données de l'image.
Le format NIFTI a conservé l'idée d'avoir un header et des données afin de préserver la compatibilité
avec les systèmes déjà en place. Toutefois, des améliorations ont été apportés et pour évité de faire
l'erreur d'oublier l'un des deux fichiers du format, il a été décidé de permettre le stockage de ces
informations dans un seul fichier avec l'extension "\*.nii". Ces images contenant de grandes zones d'image
noires, elles sont donc parfaites pour être compressées avec gzip. Il n'est donc absolument
pas rare de trouver des fichiers NIFTI au format "\*.nii.gz". Pour ce travail nous avons utilisé
les formats "\*.nii" et "\*.nii.gz".

Le format NIFTI est un format de fichier que l'on peut représenter par une matrice multidimensionnel.
Au total, elle peut compter jusqu'à 7 dimensions. Dans tous les cas, les 3 premières dimensions sont des
dimensions spatiales (x, y, z) et la quatrième est une dimension temporelle. Les dimensions suivantes
(5-7) sont des dimensions réservées à d'autres usages et sont plus ou moins libre. Dans le cadre de ce
projet, les images utilisées ne possèdent que 3 dimensions (les 3 dimensions spatiales). On peut donc voir
les images comme étant un instantané du cerveau en 3 dimensions et chaque case de la matrice de données
représente un voxel de cette image.

Les dimensions et d'autres informations importantes sur le fichier sont stocké dans un fichier
header. Ce dernier est d'une taille de 348 octets. Il existe un tableau de tous les paramètres du header [*]_.

.. [*] https://brainder.org/2012/09/23/the-nifti-file-format/

Le champs principalement utilisé lors de ce projet est le champs short dim[8]. Ce champs est un
tableau contenant les données sur les dimensions du fichier. Ce tableau contient pour: 

* Dim[0]: Le nombre de dimensions
* Dim[1 -7]: Un nombre positif contenant la longueur de la dimension en question.


Pour ce travail deux types de NIFTI ont été employé. Le premier type de NIFTI a avoir été utilisé sont des
images générés très simple. Ces images correspondent à des sphères et des cubes. La dimension de ces images
générées peut être choisi. Au début du projet, de manière a facilité les tests, la taille de ces images étaient
de 100x100x100. Puis lorsque le projet eut une forme plus concrète la taille fut changer pour correspondre à la
taille standard utilisé par le CHUV (190x190x160). Le second type de données correspond aux images fournient par
le LREN. A savoir des images de la matière grise du cerveau avec une taille standard de 190x190x160. 

Outils pratique
***************
Le format NIFTI est un format très spécifique au domaine de la neuro-imagerie. Il est donc nécessaire afin de
se familiariser avec ce format d'utiliser un certain nombre d'outils de visualisation. Une gamme d'outils en ligne
de commande existe. Cette dernière s'appelle Fslutils. Il fournit un set complet de ligne de commande utiles pour convertir
et manipuler les fichiers NIFTI. Une liste complète des outils fournit par Fslutils est fournit sur leur pages Internet.

.. figure:: images/fsl.jpg
   :width: 350px
   :align: center
   :alt: Logo de FSL

   Logo de FSL

De toute cette gamme d'outils, deux ont principalement été employé:

* fslinfo: affiche les informations du header du fichier NIFTI ou ANALYZE lu
* fslsplit: découpe une image IRM 4D en un lot de fichier 3D


Une fois le format appréhendé, il faut un outil permettant de les lire dans un programme. La plateforme du CHUV et notre projet
devant pouvoir tourner sur une JVM, une librairie JAVA a été trouvé. Cet outil est le projet niftijio. Cet outil permet de lire
le header et les données d'un fichier NIFTI et de les récupérer sous la forme d'un tableau dans un programme en Java ou Scala.  

Le calcul distribué
-------------------
Le nombre d'image et la taille de ces dernières font qu'il y a un nombre très important de données et de calcul a effectué.
Pour le confort de l'utilisateur, le temps de traitement de ces données doit être le plus court possible. La plateforme
actuellement en place au CHUV tourne donc sur un cluster de machine afin de permettre à l'utilisateur d'obtenir le plus rapidement
possible les résultats des analyses qu'il demande.

Ce projet doit donc pouvoir se porter sur l'infrastructure en place. De plus, le Laboratoire de Recherche En Neuro-imagerie désire
intégrer la technologie Spark pour effectuer leur calcul. Ces deux contraintes ont donc fait l'objet d'une analyse et sont exposé
dans ce chapitre.

Qu'est ce que le calcul distribué ?
***********************************
Ces dernières années la quantité de données disponibles a explosé. Rapidement, les technologies ont du s'adapter à cette quantité
d'information toujours plus importante à traiter. L'une des solutions trouvé pour résoudre se problème consiste à répartir les tâches
de traitement (de calcul) sur plusieurs unité de travail. Ainsi, on répartit le besoin en puissance de calcul, pour un projet, en
petite entités sur autant d'ordinateurs disponible qu'il y en a dans notre réseau distribué.

Celà permet d'exploiter les ressources de chaques machines au profit d'un projet commun. Ce projet dispose alors d'une puissance de
calcul correspondant à la somme de tous les ordinateurs individuels.

Le calcul distribué s'effectue donc au sein d'un cluster de machine. C'est à dire, au sein d'un groupe de machines indépendantes fonctionnant
comme une seule et même entité. Chacune de ces entités correspond à un noeud. Si une machine est ajouté au cluster, la puissance de calcul est
directement augmenté contrairement à une machine seule, où si l'on veut augmenter la puissance de calcul, il faut augmenter la puissance des
processeurs.

.. figure:: images/SparkRDD.png
   :width: 350px
   :align: center
   :alt: Illustration du calcul distribué

   Illustration du calcul distribué

Pour le calcul distribué, les noeuds sur lesquels les calculs sont exécuté sont donc distant, autonome et ne partage pas de ressources. Il
faut donc que chaques noeuds communiquent avec les autres au travers de message qu'il s'envoie au travers du cluster.

Pour pouvoir distribuer son projet, il faut donc diviser le problème initial en sous-problème et assigner à chaque noeud l'un de ces sous-problèmes.
Chaque noeud effectue la tâche qui lui est assigné. On récupère alors le résultat de chacun des sous-problèmes et on les combine pour obtenir le
résultat finale du projet initial.

Afin de gérer tout celà il est possible d'utiliser des framework de calcul distribué. Ces framework fournissent un ensemble d'outils pour faciliter
la création d'application distribuées. Le CHUV à choisi pour ce projet d'utiliser le framework Apache-Spark. La suite de ce chapitre présentera donc
ce framework et son fonctionnement.

Spark
*****
Spark est un framework open-source de calcul distribué écrit en Scala. Il a été conçu en 2009 par Matei Zaharia lors de son doctorat au sein de l'université de Californie
à Berkley. L'objectif de Matei Zaharia lors de la conception de Spark était de trouver une solution pour accélerer le traitement des systèmes Hadoop. Spark
est transmis a Apache en 2013 et devient l'un des projets les plus actifs de la firme. Le framework a le vent en poupe (à l'instar de Docker que nous verrons
plus loin dans ce rapport) et est en train de remplacer Hadoop. En effet, il a été démontré que Spark permet des temps d'exécution jusqu'à 100 fois plus courts
qu'Hadoop pour les mêmes tâches. La dernière version de Spark est Spark 2.2.0 et est disponible depuis le 11 juillet 2017. Spark fournit une API haut-niveau en
Java, Scala, Python et R.

.. figure:: images/spark-logo.png
   :width: 350px
   :align: center
   :alt: Logo d'Apache-Spark

   Logo d'Apache-Spark

Afin de fonctionner aussi rapidement Spark fonctionne directement en mémoire et cherche a avoir un traitement proche du temps-réel. Lorsque Spark execute des tâches,
il cherche à maintenir les résultats intermédiaires en mémoire plutôt que sur le disque. Cette manière de faire permet de facilement pouvoir travailler à plusieurs
reprises sur le même jeu de données. Toutefois Spark n'est pas restreint au travail en mémoire. Il peut aussi bien travailler sur le disque. Les opérateurs réalisent
des opérations externes lorsque les données ne tiennent pas en mémoire. Par défaut, Spark essaie de stocker le plus d'info en mémoire avant de basculer sur le disque.
Cependant, ce comportement est configurable. Il est possible de demander a Spark de ne travailler que sur le disque ou uniquement en mémoire mais également avec une
partie des données en mémoire et l'autre partie sur le disque.

Spark possède un écosystème contenant des bibliothèques additionnelles qui permettent de travailler dans les dommaines du "big data" et du machine learning.
Dans cet écosystème, on trouve notamment: 

* Spark Streaming: Permet le traitement temps-réel des données de flux.
* Spark SQL: Permet d'exécuter des requêtes SQL pour charger et transformer les données et ce quel que soit le format d'origine de celles-ci.
* Spark GraphX: Permet le traitement et la parallélisation de graphes. 
* Spark MLlib: Est une bibliothèque d'apprentissage automatique qui contient tous les algorithmes et utilitaires d'apprentissage classiques, tel que la classification,
  la régression, le clustering, le filtrage collaboratif et la réduction de dimension, en plus des primitives d'optimisation nécessaires à ces tâches.

.. figure:: images/Spark-screen-shot-3.png
   :width: 350px
   :align: center
   :alt: Illustration de l'écosystème Spark

   Illustration de l'écosystème Spark

L'architecture de Spark comprend les trois composants principaux suivants: 

* Un composant de stockage des données qui utilise le système de fichier HDFS pour le stockage.
* Une API haut-niveau
* Un composant de gestion des ressources. Ce composant permet a Spark d'être déployé comme un serveur autonome ou sur un framework de traitements distribués comme Apache-Mesos
  ou Apache-YARN.

.. figure:: images/2.png
   :width: 350px
   :align: center
   :alt: Composant principaux de Spark

   Composant principaux de Spark


L'élément de base principal au coeur de Spark est le "Resilient Distributed Dataset" ou RDD. Un RDD est une abstraction de collection sur laquelle les opérations sont effectué
de manière distribué et en étant tolérante aux pannes matérielles. On peut donc les voir comme une table dans une base de données. Un RDD peut contenir n'importe quel type de donné
et est stocké par Spark sur différentes partitions. Ainsi, le traitement que l'on écrit pour un RDD semble s'exécuter sur une JVM mais il sera en fait découpé pour s'exécuter sur plusieurs
noueds. Si le cluster de machine perd un noeud, le sous-traitement sera automatiquement relancé sur un autre noeud par le framework. Ceci est possible car un RDD sait recréer et recalculer
son ensemble de données. Les RDD supportent deux types d'opérations:

* Les transformations(map, filter, flatMap, groupByKey, reducebyKey, etc...): Celles-ci retourne un nouvel RDD. 
* Les actions(reduce, collect, count, first, take, foreach, etc...): Celles-ci évaluent et retournent une nouvelle valeur. 


L'exécution de Spark peut se faire de plusieurs manière différente. Pour celà il suffit de donner le bon paramètre de connexion au moteur de Spark (Master, chef d'orchestre). Ainsi, la connexion
au moteur peut se faire de manière local (sur un ou K "worker"), en se connectant à un cluster Spark, Mesos ou Yarn.

.. figure:: images/23.png
   :width: 350px
   :align: center
   :alt: Mode de connection à Spark

   Mode de connection à Spark

Spark fournit également une interface web. Pour joindre cette interface, il suffit, une fois Spark en cours d'exécution, de se connecter sur le port 4040 du localhost. Cette interface permet de
surveiller le stockage, l'environnement, les exécuteurs et les étapes effectué par Spark.

Spark possèdent encore bien des caractéristiques qui font de lui l'un des leaders du domaine. Toutefois, nous avons vu ici ces principales caractéristiques et les principaux outils utilisé durant
l'élaboration de ce travail de Bachelor. L'utilisation de Spark dans le projet sera détaillé plus loin dans la rédaction de ce rapport. 

Le deeplearning et choix d'une bibliotheque
--------------------------------------------
La plateforme d'informatique médicale tenue par le LREN aimerait pouvoir donner à ces utilisateurs la possibilité de lancer des expériences
de deeplearning. Ce projet a donc pour objectif d'ouvrir la voie à ce procédé.

Il est donc important de faire le point sur cette technologie. Cette partie va donc permettre de voir ce que sont les réseaux de neurones
et le deeplearning. Puis dans un second temps, les réseaux de convolution seront abordé. Dans une troisième partie, ce rapport abordera
les différentes manières de mélanger calcul distribué et deeplearning. Ces trois premières parties permettront de se faire une idée de ce
concept et d'aborder plus sereinement l'état de l'art des bibliothèques de deeplearning et le choix de l'une d'entre elle pour ce travail.

Considération générale
**********************
Cette partie du rapport présente les généralitées concernant le deeplearning. Nous verrons dans quel domaine de l'informatique se situe le deeplearning, le principe de neurone formel,
les réseaux de neurones et la manière d'entrainer un réseau de neurones.

Machine learning et bases
+++++++++++++++++++++++++
Le deeplearning est un ensemble de méthodes de machine learning. Le machine learning est l'un des champs d'étude de l'intelligence artificielle
et cherche à permettre à une machine de modéliser des phénomènes dans le but de prendre des décisions et de résoudre des problèmes concrets.
Cette capacité à prendre des déscisions se fait sans être explicitement programmé par le développeur.

Un problème concret peut, par exemple, être d'identifier des fraudes, d'aider aux diagnostiques médicaux, de recommander un article
personnalisé à un client, de prédire le prix d'un produit, etc. L'idée derrière le machine learning est alors de permettre à la machine de
se construire une représentation interne du problème sans que le développeur n'ait besoin de la modéliser pour elle.

A l'aide de cette modélisation, la machine pourra alors effectuer la tâche qui lui est demandé. La tâche demandé au cours de ce projet est
une tâche de classification. La classification sert à pouvoir ranger une donnée (une image par exemple) dans une classe spécifique.
Pouvoir dire d'une image qu'elle représente un chat ou un chien par exemple. Etant la tâche sur laquelle ce travail se base la
classification sera utilisé comme exemple dans la suite de ce rapport.

Pour que l'algorithme de machine learning puisse se construire une représentation du problème, il faut lui fournir un jeu de données
d'exemple. Grâce à ce jeu de données, l'algorithme va pouvoir s'entraîner et s'améliorer dans la tâche qui lui a été confié. Nous pourrons
par la suite lui fournir des données réels et obtenir un résultat aux problèmes posés.

Il existe différent algorithme de machine learning. Parmis eux nous pouvons noté: 

* La régression linéaire
* La classification naïve de Bayes
* Machine à vecteurs de support (SVM: support vector machine)
* K-nn
* Random Forest (Forêt d'arbres décisionnnels)
* Réseau de neurones


Les neurones formels
++++++++++++++++++++
Le deeplearning est une technique qui fonctionne sur la base des réseaux de neurones. Les réseaux de neurones sont construit à partir d'un
paradigme biologique. Ce paradigme est celui du neurone formel. Un neurone formel est une représentation mathématique et informatique d'un
neurone biologique. Le neurone formel possède généralement plusieurs entrées et une sortie. Les entrées correspondent ainsi aux dendrites
d'un neurone, tandis que la sortie correspond à l'axone de ce dernier. Pour fonctionner, un neurone biologique reçoit des signaux
excitateurs et inhibiteurs grâce aux synapses (lien entre deux neurones). Ces signaux sont simulés dans un réseau de neurones informatiques
par des coefficients numériques associés aux entrées des neurones. Ces coefficients sont appelés les biais. Les valeurs numériques de ces
coefficients sont ajustées durant la phase d'apprentissage dans un réseau. Le neurone formel fait alors des calculs avec les poids pondérés
des entrées reçues, puis applique au résultat de ce calcul une fonction d'activation. La valeurs finale obtenue se retrouve alors sur la
sortie du neurone. Ces neurones formels peuvent ensuite être assemblé entre eux pour former des réseaux et réaliser des tâches plus
complexe. Ainsi le neurone formel est l'unité élémentaire des réseaux de neurones artificiels.

.. figure:: images/neurone_bio.jpg
   :width: 350px
   :align: center
   :alt: Illustration d'un neurone biologique

   Illustration d'un neurone biologique

L'un des éléments capitaux d'un neurones formels est la valeur de ces biais. Un biais est la pondération d'une des entrées. La plupart des
neurones informatiques effectue une somme pondéré de leur entrée. Ainsi, plus une entrée à une valeur de biais importante, plus la "force"
de la connexions est grande. Cela simule le comportement d'un neurone biologique, dans lequel plus une connexion est importante, plus
celle-ci est sensible aux stimulis chimiques. Dans un reseau de neurones, la valeur des biais est mis à jour durant la phase d'apprentissage
que nous avons évoqué plus haut. Durant cette phase, on va chercher à faire converger les valeurs des biais pour s'assurer une classification
aussi proche que possible de l'optimal.

.. figure:: images/neurone.png
   :width: 350px
   :align: center
   :alt: Illustration d'un neurone formel

   Illustration d'un neurone formel

L'autre éléments important d'un neurones formel est sa fonction d'activation. Dans un premier temps, le neurone va récupérer ces entrées
et les agrégers avec une fonction d'agrégation. La fonction d'agrégation la plus simple consiste en une somme pondéré par les biais des
valeurs en entrée. Toutefois, cette fonction peut être plus complexe. Le but de cette fonction est d'obtenir une valeur agrégé des entrées
du neurones. Ce dernier utilise cet agrégat comme paramètre d'une fonction d'activation. Cette fonction a pour rôle de décider si le
neurone est actif ou non. Elle permet également de donner la valeur a fournir en sortie du neurone. Il est donc important de bien choisir
cette dernière. Il existe en effet plusieurs fonctions d'activations typiques:

* La fonction sigmoïde
* La fonction de Heaviside
* La fonction tangente hyperbolique
* La fonction de base radiale
* La fonction sigma-pi
* La fonction RELU
* La fonction SOFTMAX  
* etc...

.. figure:: images/activation_fonction.png
   :width: 350px
   :align: center
   :alt: Illustration des principales fonctions d'activation d'un neurone formel

   Illustration des principales fonctions d'activation d'un neurone formel

L'objectif de la fonction d'activation est d'introduire de la non-linéarité dans le fonctionnement du neurone. Les fonctions d'activation
présente, en règle général, trois intervalles: 

* Sous le seuil d'activation, le neurone est inactif
* Au alentour du seuil, le neurone est dans une phase de transition
* Au dessus du seuil, le neurone est actif

Le neurone formel est la brique de base des réseaux de neurones que nous allons voir dans la partie suivante.

Les réseaux de neurones
+++++++++++++++++++++++
Un réseau de neurones est simplement un ensemble de neurones liés entre eux, la sortie d'un premier neurone devenant l'entrée d'un second
neurone.

Il existe plusieurs types de réseaux de neurones qui vont chacun dépendre de la manière d'organiser les neurones. Le modèle de réseau le
plus simple est le perceptron simple. Un perceptron est un réseau de neurones monocouche et permet une classification linéaire.
Nous allons prendre ce modèle pour expliquer quelques notions importante. Un perceptron possède n informations sur ces
entrées et p neurones formels formant une couche. Chacun des p neurones est connecté aux n informations d'entrée et a sa propre sortie.

Un perceptron peut avoir une représentation matricielle. Ainsi on peut considérer le n information d'entrée comme un vecteur à n composantes.
Et la sortie du perceptron est un vecteur de p composantes. Et finalement le perceptron est une matrice de n lignes et p colonnes. Cette
matrice est rempli avec les différents poids de chacunes des connexions avec le vecteur d'entrée. Elle est régulièrement appelé matrice de
poids. En étendant ce principe nous pouvons en déduire que les réseaux de neurones même plus complexe, sont en réalité de longues chaines
de calcul matriciel.

.. figure:: images/perceptron.jpeg
   :width: 350px
   :align: center
   :alt: Illustration d'un perceptron simple

   Illustration d'un perceptron simple

Un perceptron tel que décrit au dessus est également un réseau feed-forward. Il existe, en effet, manière de "nourrir" un réseau de neurones.
Un réseau de neurones peut donc être "feed-forward" ou récurrent. Un modèle récurrent peut alimenter ses entrées avec ses sorties. Tandis que
les réseaux "feed-forward" ne le peuvent pas.

Nous pouvons pouvons étendre le concept du perceptron en lui ajoutant des couches. Le perceptron de l'exemple précédent devient alors un
perceptron multi-couches. Une couche est un ensemble de neurones connectés aux mêmes entrées mais pas relié entre eux. Dans le modèle
multi-couches, les réseaux récurrents peuvent utiliser leurs sorties pour alimenter des entrées des couches précédentes.

.. figure:: images/PMC.png
   :width: 350px
   :align: center
   :alt: Illustration d'un perceptron multicouche

   Illustration d'un perceptron multicouche

Les modèles de deeplearning sont bâtis sur le même principe que les perceptrons multi-couche. Dans ce genre de modèle, le nombre de couche
du perceptron sont plus nombreuses. Chacune des couches intermédiaires étant chargé de résoudre ou de découper un sous-problème. Ainsi si
l'on imagine en entrée du réseau une image de voiture, les couches pourraient hierarchisé cette image de cette façon: 

1. Couche 1: Cette image représente un véhicule
2. Couche 2: Ce véhicule est motorisé
3. Couche 3: Ce véhicule motorisé a 4 roues
4. Couche 4: Ce véhicules motorisé à 4 roues est une voiture


Ainsi chaques couches ajoute un contexte de plus en plus précis aux données passées en entrée.

Note sur l'apprentissage d'un réseau de neurones
++++++++++++++++++++++++++++++++++++++++++++++++
Comme déjà souligné, un réseau de neurones ne peut effectué la tâche pour laquelle il est conçu qu'après avoir subi une phase d'apprentissage.
Cette phase d'apprentissage consiste à mettre à jour les biais de chaques neurones pour les faire converger vers une meilleure précision
de l'algorithme.

Il existe différents type d'apprentissage. Les apprentissage supervisé et ceux qui ne le sont pas (non-supervisé). Un apprentissage supervisé
se fait en donnant des labels aux données d'entrée, en les étiquetant. Le modèle peut alors apprendre en se basant sur ces labels. Dans
l'apprentissage non-supervisée les données ne sont pas étiquetté. Pour le projet qui nous intéresse la méthode d'apprentissage utilisé
est une méthode supervisée.

De manière générale pour s'entraîner le réseau va lire des données d'entrainement, tenté de les classifier, calculer l'erreur qu'il fait
à chaque itération et modifier les poids de manière à diminuer l'erreur calculé. 

Cette manière de faire est une méthode que l'on appelle algorithme de rétro-propagation. On peut ensuite utilisé la technique de la descente de
gradient qui consiste a calculer la direction, dans l'espaces des poids, dans laquelle la décroissance de l'erreur est maximal et mettre à
jour nos poids.

Il est a noté que le temps nécessaire à l'apprentissage augmente très rapidement avec le nombre de couche du réseau et que les réseaux de
neurones peuvent subir un surapprentissage. 

Le surapprentissage (overfitting) est un problème qui empêche le réseau de généraliser les caractéristiques des données. Le réseau
perd alors sa capacité à prédire. Il existe des manières simple d'éviter le surapprentissage:

* Cross-validation: consiste a créer deux sous-ensembles de données. On créer un sous-ensemble d'entraînement et un sous-ensemble de validation.
  On entraine ensuite le réseau avec le sous-ensemble d'apprentissage et on test son pouvoir prédictif avec l'ensemble de validation. Ainsi si l'erreur lors
  de l'apprentissage diminue alors qu'elle augmente sur les données de validation le réseau est sur-entrainé. On essaie donc d'arrêter l'entraînement
  lorsque l'on constate cette divergence. 
* Régularisation: consiste a pénaliser les valeurs extrêmes des paramètres.


Au terme de l'entraînement, il est possible d'élaguer notre réseau. Cette technique consiste a supprimer les connexions ayant peu d'influence
sur le reste du réseau. Cela permet de faire gagner du temps pour la tâche a effectuer ensuite.

Réseaux de convolution
**********************
Dans cette partie, le rapport se concentrera sur ce qu'est un réseau de convolution. Ces réseaux sont très courant dans le domaine de la vision par ordinateur.
C'est pourquoi, nous allons voir quelques notions générales sur les réseaux de convolution, 

Ce projet vise a traiter des images d'IRM. L'un des type de réseau de neurones dont la spécialité est de traiter des images est le réseau de neurones
convolution (CNN).

.. figure:: images/FirstLayers.png
   :width: 350px
   :align: center
   :alt: Illustration du résultat du filtrage par convolution

   Illustration du résultat du filtrage par convolution

Les réseaux de convolution sont un type de réseau de neurones acyclique ("feed-forward") dans lequel le motif de connexion entre les neurones
est inspiré par le cortex visuel des animaux. On peut donc lui passer en entrée une image et lui demander de la classifier.

Ce genre de réseau de neurones est, en général, conçu en deux parties distinctes. La première partie est la partie convolutive du réseau.
Cette dernière agit comme un extracteur de caractéristiques. On passe l'image à travers un certain nombre de filtre (noyau de convolution)
pour créer de nouvelles images (carte de convolution). Les couches de convolution sont en principe suivi d'une couche de correction
utilisant la fonction d'activation RELU. On peut placer, entre les opérations de convolution, des filtres chargé de réduire la résolution
de l'image par une opération de maximum local (Pooling). Au terme de cette étape, les cartes de convolutions sont concaténé en un vecteur
de caractéristiques. Ce vecteur est souvent appelé le code CNN.

.. figure:: images/CNN.png
   :width: 350px
   :align: center
   :alt: Illustration d'un réseau de neurones de convolution

   Illustration d'un réseau de neurones de convolution

Ce code est alors utilisé en entrée de la seconde partie du réseau convolutif. Cette partie est en règle général constitué de couches
entièrement connectées entre elle. Le but de cette partie est de combiner les caractéristiques du code CNN pour classifier l'image. 

La sortie de ce genre de réseau est en principe une dernière couche contenant autant de neurones qu'il n'y a de classe possible. La sortie est
en principe un vecteur dont le nombre de composant est le nombre de classe disponible. La valeur de ces composantes est compris entre 0 et 1.
Et la somme des composantes vaut 1. En lisant ce vecteur on obtient la distribution de probabilité que l'image appartiennent a une catégorie
ou une autre. 

Pour concevoire des couches de convolution, il existe trois paramêtres particulièrement important a géré. Ces paramêtres sont: 

* La profondeur: le nombre de filtre que l'on va utiliser
* Le pas: Le pas contrôle le chevauchement des noyaux de convolution
* La marge: La marge contrôle la dimension spatiale du volume de sortie. Elle ajoute des 0 à la frontières de l'image en entrée.


Après la couche de convolution, on peut trouver une couche de correction. Cette couche semble permettre d'accélerer la vitesse de traitement
du réseau sans affecté la précision. La fonction d'activation de cette couche peut être: 

* Correction RELU: permet d'augmenter les propriétés non-linéaire du réseau
* Correction par tangente hyperbolique
* Correction par la fonction sigmoide
* etc...


La correction la plus utilisé est la correction RELU.

Une fois la correction effectué, on peut trouver une couche de pooling. Elle permet de sous-échantillonner les données d'entrée (l'image).
Elle permet de réduire le risque de sur-entraînement du réseau. Toutefois, il faut faire attention à utiliser de petit filtre afin de ne
pas perdre trop d'information. Pour cette couche on va en effet passer sur l'image des filtres chargés d'extraire une valeur de l'image.
L'une des méthodes les plus utilisé est d'employer un filtre de taille 2x2 chargé d'extraire la valeur maximum des pixels contenu dans le
filtre. Le pooling permet d'augmenter l'efficacité du traitement. Toutefois, il casse le lien entre une image et son contenu (par exemple
entre le nez et l'image d'un visage). Cette relation peut être intéressante a conservé. En faisant débordé les filtres du pooling les unes
sur les autres, il est possible de définir une position pour un élément. Il devient alors possible de dire que le nez est au milieu du
visage par exemple. Cependant, il faut noter que faire ceci empechera tout autres formes d'extrapolation (changement d'angle de vue ou d'échelle),
contrairement à ce que le cerveau humain peut faire. Pour améliorer ce problème, on peut passer des données d'entraînement plus variées à
notre réseau. Ces images peuvent être plus variées en terme de luminosité, angle de vue ou taille.

La toute dernière couche d'un réseau de convolution est une couche de LOSS. Cette couche est chargé de définir la classe dans laquelle l'image
se situe. Elle peut être activé par différentes fonction d'activation: 

* Fonction SOFTMAX: Une fonction idéal lorsque l'on doit ranger les images en 2 classes. C'est une fonction mutuellement exclusive.
* Fonction d'entropie sigmoide croisé: prédis des valeurs de probabilité indépendante dans [0, 1]
* Fonction euclidienne: Regression vers des valeurs réelles (contenu entre moins l'infini et plus l'infini)


Il existe plusieurs modèles de réseau convolutif devenus des standards. Ces architectures sont les suivantes: 

* INPUT + CONVOLUTION + RELU + FULLY CONNECTED + LOSS
* INPUT + (CONVOLUTION + RELU +POOLING)\*2 + FULLY CONNECTED + RELU + FULLY CONNECTED + LOSS
* Input + (CONVOLUTION + RELU + CONVOLUTION + RELU + POOLING)\*3 + (FULLY CONNECTED + RELU)\* 2 + FULLY CONNECTED + LOSS


Deeplearning et calcul distribué
********************************
Ce projet devra pouvoir fonctionner avec du calcul distribué. Comme vu plus tôt dans ce rapport, le calcul distribué permet de répartir des
tâches entre plusieurs machines connecté à un même cluster de machines. Il existe différentes techniques pour distribuer des tâches dans un
réseau de deeplearning. Il existe deux modèles principaux:

* La parallélisation des données
* La parallélisation du modèle


Dans la parallélisation du modèle, les différentes machines sur le réseau distribué sont en charge d'une partie du réseau. Par exemple,
chaque machine peut se voir assigné la gestion d'une couche du réseau de neurones.

.. figure:: images/ModelDataParallelism.png
   :width: 350px
   :align: center
   :alt: Illustration des modes de parallélisation

   Illustration des modes de parallélisation

Dans la parallélisation des données, les différentes machines sur le réseau distribué ont une copie complète du modèle de réseau. Chaque
machine reçoit alors une partie des données et entraîne son modèle. Au terme de l'entraînement, les résultats sont combinés entre eux.

Les approches d'entraînement en utilisant la parallélisation des données nécessitent toutes une méthode de combinaison des résultats et de
synchronisation des paramètres du modèle entre chaque machine.

L'implémentation actuel de la bibliothèque de deeplearning choisi pour ce projet permet de faire de la parallélisation des données. Pour faire
cela, la bibliothèque utilise les techniques de moyennes des paramètres synchrones. 

L'approche de la moyenne des paramètres est l'approche la plus simple de la parallélisation des données. En utilisant cette technique,
l'apprentissage fonctionne ainsi:

1. Les paramètres du réseau sont initialisés de manière aléatoire en fonction de la configuration du modèle
2. Une copie des paramètres actuels est distribué sur chaque machine 
3. Chaque machine entraîne son modèle avec les données en sa possession
4. De nouveaux paramètres globaux sont calculés en fonction de la moyenne des paramètres de chaque machine
5. Tant qu'il y a des données à traiter, on retourne a l'étape 2


.. figure:: images/UpdateBased.png
   :width: 350px
   :align: center
   :alt: Illustration de l'approche de la moyenne des paramètres

   Illustration de l'approche de la moyenne des paramètres


Bibliothèque disponible et choix
********************************
Cette partie du chapitre va faire un état des biblitothèques de deeplearning actuellement disponible. Puis en ce basant sur les contraintes
fourni par le CHUV et la plateforme d'Informatique Médical du Human Brain Project, elle défendra le choix de la bibliothèque choisi pour le
reste du projet. Il faut rappeler que ces contraintes sont:

* L'utilisation du calcul distribué avec Spark
* Une plateforme qui fonctionne en Scala


Liste de bibliothèques disponible
+++++++++++++++++++++++++++++++++
Ce rapport va ici faire une liste des bibliothèques vu pour ce projet. Chacune d'entre elle sera accompagné d'une brève description et de
ces caractéristiques principales.

TensorFlow
~~~~~~~~~~
TensorFlow est une bibliothèque de programme open-source développé par Google. Cette bibliothèque est utilisé dans de nombreux produit Google.
Ces principales caractéristiques sont:

* Qu'elle est utilisable en Python
* Qu'elle possède une API en C++
* Qu'elle possède une grosse documentation et est très utilisé
* Que c'est un projet très solide de Google


Toutefois, pour pouvoir être utilisé en scala il est nécessaire d'utiliser un outil comme ScalaPy. Cette biblitothèques a donc été rejeté
car on ne peut pas se passer de ScalaPy.

TensorFrames
~~~~~~~~~~~~
TensorFrames est un portage expérimentale en Scala de TensorFlow. Ce portage est fait par Databricks. Ces principales caractéristiques sont:

* Que c'est un portage expérimentale ne fonctionnant que sur des plateformes Linux 64 bits
* Qu'elle est utilisable directement en Scala et en Python


Cette bibliothèque étant expérimentale, cette bibliothèque a été écarté.

BigDL
~~~~~
BigDL est une bibliothèque conçu pour Spark et pouvant fonctionner sur les clusters Spark ou Hadoop existant. Elle a été crée par Intel.
Ces principales caractéristiques sont: 

* Qu'elle fonctionne nativement en Java
* Qu'elle est directement intégrable a Spark
* Qu'elle a été conçu pour supporter le calcul distribué
* Qu'elle ne fonctionne que sur les chips Intel


Le fait que cette bibliothèque ne fonctionne que sur les chips Intel a écarté cette bibliothèque.

Keras
~~~~~
Keras est une API de haut-niveau écrit en python et capable de fonctionner sur TensorFlow, CNTK ou Theano. Ces principales caractéristiques sont:

* Qu'elle fonctionne en Python et nécessite donc d'être binder à du Java/Scala
* Qu'elle supporte le CPU et le GPU
* Qu'elle est conçu pour faire du prototyping rapidement


Le fait qu'elle fonctionne en python a permis son élimination.

Caffe on Spark
~~~~~~~~~~~~~~
Caffe on Spark est une bibliothèque mêlant le framework Caffe et Spark ou Hadoop. Elle est géré par Yahoo. Ces principales caractéristiques sont:

* Qu'elle fonctionne sur GPU et CPU
* Qu'elle fonctionne sur les systèmes de fichier HDFS d'Hadoop
* Qu'elle permet de gérer le réseau de neurones depuis Spark ou Hadoop
* Qu'elle fonctionne en Java
* Qu'elle a besoin d'être installé sur chaque noeud du cluster


Le fait que cette bibliothèque ait besoin d'être installé sur chaque noeud l'a écarté. En effet, c'est une chose dont le LREN aimerait se passer.

SparkNet
~~~~~~~~
SparkNet est une bibliothèque de deeplearning conçu en Scala dont les pincipales caractéristiques sont:

* Qu'elle fonctionne sur Spark
* Qu'elle est nativement conçu en Scala
* Qu'elle est supportée que sur Ubuntu (CPU/GPU) et sur CentOS


Le nombre de plateforme sur laquelle elle est employable a éliminer cette bilbiothèque.

Deeplearning4j
~~~~~~~~~~~~~~
Deeplearning4j est une bibliothèque conçu pour la JVM et capable de fonctionner sur Spark. Elle est conçu pour tourner tant sur CPU que sur GPU.
Ces concepteurs la vende comme un outils pour le deeplearning à échelle industrielle. Ces principales caractéristiques sont:

* Qu'elle est conçu pour fonctionner avec la JVM, codé en Java
* Qu'elle fonctionne sur GPU et sur CPU
* Qu'il est possible de la faire fonctionner avec des modèles issu de Keras
* Qu'elle fournit des outils pour tourner sur Spark.
* Que sont intégration à un projet se veut simple en utilisant Maven, Graddle ou encore SBT
* Qu'elle possède une API Scala (ScalNet)
* Qu'elle a un support actif


Pour tous les avantages qu'elle donne cette librairie a été choisi en concertation avec le LREN. Cette bibliothèque fournit ainsi tous les outils
demandé pour réaliser le travail demandé par le CHUV.

Conception
===========
Au travers de ce chapitre, la conception de ce projet sera mise en avant. Il va permettre d'expliquer le workflow, l'architecture
du programme, ainsi que les différents paramètres de configuration de ce projet. Afin de donner les explications le plus clairement possible
ce chapitre contiendra les schémas utilisés durant la conception. Chacunes des classes créée durant la réalisation de ce projet est décrite
afin d'en expliquer le concept et l'utilité.

(A vérifier si toujours vrai + préciser si ca ne marche qu'avec IntelliJ ou aussi avec le jar)
Toutefois, il est a noté que le rendu de ce projet contient deux programmes excécutables. Ces exécutable sont nommés "LREN-Deeplearning.jar"
et "LREN_Deeplearning_DemoLocal.jar". Ceci est du à un problème de compatibilité de dépendance dans la bibliothèque Deeplearning4j. En
effet, les dépendances liées à l'emploie de Spark et à l'emploi d'une UI pour la visualisation de l'entraînement ne sont pas compatible
entre. Ce problème est plus précisément expliqué plus tard dans ce rapport. Cependant, ce qui est décrit dans ce rapport peut se porter
sur les deux executables fourni. La seule différence entre ces deux exécutables étant que l'exécutable "LREN_Deeplearning_DemoLocal"
fournit un outil de visualisation de l'entraînement mais ne fournit pas d'outil permettant de lancer l'apprentissage sur Spark.

Description du workflow et schéma de classe
-------------------------------------------
Ce projet peut fonctionner de plusieurs manières différentes. Les différents comportements du programme peuvent être configurer dans un
fichier "\*.properties". Un fichier de configuration détaillé avec une brève explication des paramètres est fournit en annexe de ce rapport.
La suite de ce rapport n'en reprendra que les grandes lignes.

.. figure:: images/Workflow_appli.png
   :width: 350px
   :align: center
   :alt: Illustration du workflow du programme

   Illustration du workflow du programme

La première chose que fait le programme est donc de créer un objet capabe de créer et de lire un fichier de configuration. Pour créer
le fichier de configuration il suffit de lancer le fichier jar du projet sans autre paramètre. Le programme générera alors un fichier de
configuration standard permettant de lancer une expérience de base puis se terminera.

Si l'on donne comme argument au programme un fichier de configuration, ce dernier sera lu et selon ce qu'il contient le programme adoptera
le comportement adéquat.

La première chose que le programme lira permettra de lui dire s'il doit ou non créer des données de test. Si oui, il pourra généré deux types
de NIFTI. Ces derniers permettent de lancer une expérience de classification en deux classes distinctes sans avoir à télécharger de données.

Une fois cette génération faite (ou non selon la configuration), le programme va lire le dossier qui lui est stipulé pour chercher les données et
demander à l'utilisateur d'entrer une chaine de caractère a recherché dans le nom du fichier afin de labeliser les données lues. Il faut donc que
les fichiers que cherche à lire le programme contiennent une chaîne de caractère unique et permettant d'identifier a quel classe il correspond.
De cette manière le programme peut généré les labels et étiquetté les images.

Le programme regarde ensuite s'il doit fonctionner sur Spark ou non. Dans les deux cas la marche a suivre ensuite reste très similaire.
Tout d'abord, le programme va récupérer les données d'entraînement et les données de test. Il charge ensuite la configuration du réseau selon
ce qui lui est spécifié dans le fichier de configuration. Il peut charger une configuration pré-enregistré dans le code du projet ou une configuration
sauvegardé plus tôt.

Une fois la configuration chargé et si la configuration demande l'emploi de Spark, le programme se charge d'initialiser le superviseur
d'entraînement de Spark. 

Puis dans le deux cas le programme termine la création d'un réseau avec la configuration chargé. Si Spark est demandé, le réseau est
fait pour fonctionner sur cet outil.

Suite à cela le réseau est entrainé avec les données d'apprentissage puis évaluer avec les données de test. Les résultats de l'évaluation
sont affiché à l'utilisateur et le réseau est sauvegarder dans un fichier selon la méthode demandée par le fichier de configuration, si la
sauvegarde est demandé.

Une fois le workflow défini, le schéma de classe a pu être conçu. Ce dernier a été imaginé en se focalisant sur les principales tâches
du programme. Ainsi chaque tâche du programme peut être représenté par une classe.

.. figure:: images/Schema_Classe.png
   :width: 350px
   :align: center
   :alt: Schéma de classe

   Schéma de classe

Les classes représentées sur ce schéma sont décrite dans la suite du rapport.

Description des classes
-------------------------
Dans la suite de ce chapitre, nous allons survolé les différents packages et les différentes classes créées pour ce projet. Une rapide
description du package expliquera les classes contenues dans le package et pourquoi elles ont été séparé ainsi. Puis, une description plus
précise des classes sera faîtes pour chaque package.

Package "Config"
****************
Le package "Config" contient tous les outils nécessaire pour créer et lire les fichiers de configuration dont le programme a besoin.
Il a été conçu à cause du besoin de pouvoir facilement modifier la configuration du programme pour le tester. Ce package ne contient
qu'une classe: La classe configuration.

La classe "Configuration"
+++++++++++++++++++++++++
La classe "Configuration" est une classe très simple. Elle répertorie la liste complète des paramètres dont le programme peut avoir besoin.
Ces paramètres, comme vu plus haut permettent de choisir le fonctionnement du programme mais également de modifier le comportement du réseau
de neurones.

Cette classe contient de nombreuses méthodes. Toutefois celle-ci peuvent être classer en trois types de méthode:

* Une méthode permettant de générer un fichier de configuration standard. Ce fichier permet de lancer une expérience de test fonctionnelle du programme.
* Une méthode de lecture d'un fichier de configuration. Cette méthode lit le fichier et stock les valeurs des paramètres dans des attributs de la classe.
* Des "getter" qui permettent l'accès à chaque paramètre lu par la classe.


Cette classe est une classe on ne peut plus standard utilisant les outils de Java standard.

Package "Generator"
*******************
Ce package fournit les outils nécessaires pour générer des fichiers NIFTI de test. Il a été conçu pour permettre de créer des données permettant
de tester les réseaux de neurones sans avoir besoin de télécharger un dataset complet. Ce package ne contient qu'une classe: la classe "DataTestGenerator".

La classe "DataTestGenerator"
+++++++++++++++++++++++++++++
Cette classe est chargé de créer des fichiers au format NIFTI. Elle permet la création de représentation de cube et de sphère dans ce format.
La génération de ces fichiers peut être supervisé par le fichier de configuration. Ainsi, il est possible de préciser la taille de la matrice
de données de ces fichiers. Il ne font que des NIFTI en trois dimensions tels que ceux qui sont utilisé par le CHUV.

Elle permet également de créer de très petit jeu de données totalement aléatoire. Cette fonctionnalitée a été utilisé au début du projet afin
de pouvoir comprendre le fonctionnement de la bibliothèque deeplearning4j. Toutefois, cette fonctionnalité n'est plus utilisé dans le programme
finale.

Cette classe possède ainsi:

* Des méthodes pour générer des cubes et des sphères dans des fichiers NIFTI selon des paramètres de taille que l'on peut lui spécifier.
* Une méthode pour générer un dataset complet de sphères et de cubes de tailles fixes dans des fichiers NIFTI de taille également fixe et ce à des positions prédéfinis.
* Une méthode pour générer un dataset complet de sphères et de cubes de tailles aléatoires dans des fichiers NIFTI de taille fixes et ce à des positions aléatoires.
* Un lot de méthode pour générer de très petits jeu de données de petites tailes et le tout aléatoirement.


Cette classe utilise les fonctionnalités fournit par la bibliothèque de gestion de NIFTI niftijio. 

Package "Core"
**************
Comme le nom de ce package l'indique ce dernier contient le coeur du projet. Il contient la classe principale du projet (la classe "Main") et
la classe chargée de lire les fichiers NIFTI.

La classe "Main"
++++++++++++++++
La classe "Main" centralise le workflow complet du programme. Cette dernière est chargé d'instancier tous les objets utiles au programme
et de les gérer. Ainsi chaque package est totalement indépendant des autres. Par exemple le package Configuration n'a pas besoin du package
Wrapper pour fonctionner.

Afin de rendre cette classe la plus lisible possible elle contient quelques fonctions:

* La fonction "Main" elle est le squelette du programme et gère le workflow.
* Une fonction chargée d'afficher une aide à l'utilisateur si l'utilisateur fait une erreur d'utilisation du fichier jar.
* Une fonction chargée de gérer la génération de données.
* Une fonction chargée de gérer la création des labels a assigner a chaque image.
* Une fonction chargée de gérer la lecture des données.
* Une fonction chargée de gérer la configuration, l'initialisation, l'entraînement et l'évaluation du réseau de neurones.


Elle instancie également toutes les autres classes créées pour ce projet.

La classe "DataReader"
++++++++++++++++++++++
La classe "DataReader" fournit les outils nécessaires à la lecture des données et à la préparation de ces dernières pour être comprise
par le réseau de neurones. 

Elle fournit un certain nombre de méthodes permettant: 

* de créer un iterateur sur le jeu de données lu. Chacunes des données lues se voit assigner le label nécessaire en fonction de son nom 
* d'obtenir les itérateurs des jeu de données d'apprentissage et de test sans autre modification (nécessaire si on ne se sert pas de Spark). 
* d'obtenir les itérateurs des jeu de données d'apprentissage et de test après que ceux-ci aient été normalisés (nécessaire si on ne se sert pas de Spark).
* d'obtenir les itérateurs des jeu de données d'apprentissage et de test sous forme de liste et sans avoir été normalisés (nécessaire si l'on se sert de Spark). 
* d'obtenir les itérateurs des jeu de données d'apprentissage et de test sous forme de liste et après avoir été normalisés (nécessaire si l'on se sert de Spark).


Cette classe permet de configuré la taille des minibatchs de chaque itérateurs et gère également de ratio de données d'entraînement et de test.
Ce ratio est fait de manière stratifié. Le même ratio est appliqué pour les données de chaques classes. Si l'on a 2 classes et un ratio de 80%
de données d'apprentissage et 20% de données de test, on retrouvera 80% des données de la classe 1 et 80% des données de la classe 2 dans les données
d'apprentissage. Et ce de la même manière pour les 20% de données de test.

Package "Wrapper"
*****************
Ce package réparti en trois classe la gestion de la configuration, de l'initialisation, de l'entrainement et de l'évaluation du réseau de neurones.
Une classe gère les parties communes de configuration du réseau, tandis que les autres se partage les tâches selon si ces dernières sont liées
à Spark ou non. 

La classe "WrapperDl4j"
+++++++++++++++++++++++
La classe "WrapperDl4j" est la classe mère de la gestion du réseau de neurones. Elle gère principalement la configuration du réseau de neurones.
En effet, la configuration du réseau est indépendante de la méthode employé ensuite pour l'utilisation (Utilisation de Spark ou non).

Elle fournit donc les méthodes qui permettent de: 

* créer une configuration de réseau. 
* charger une configuration de réseau depuis une configuration sauvegardé.
* sauvegarder le réseau. 


Elle est un wrapper autour de la bilbiothèque deeplearning4j afin d'en simplifier l'emploie. Elle sert également de classe de base pour
les deux classes que ce rapport va présenter ensuite.

La classe "LocalWrapperDl4j"
++++++++++++++++++++++++++++
Cette classe est une classe héritant de la classe "WrapperDl4j". Elle étend ainsi cette dernière en lui fournissant les méthodes pour
l'initialisation en local du projet. C'est à dire sans se servir de la plateforme Spark. Elle est contenu dans un sous package nommé
"local".

Les méthodes qu'elle fournit sont donc chargées:

* d'initialiser le projet pour fonctionner en local sur la machine.
* d'entraîner le réseau de neurones en local.
* d'évaluer le réseau de neurones en local.


Cette classe est elle aussi construite autour de la librairie deeplearning4j.

La classe "SparkWrapperDl4j"
++++++++++++++++++++++++++++
Cette classe est conçu pour permettre l'emploi d'un réseau de neurones sur Spark. Elle est contenu dans un sous package nommé "spark".
Elle étend la classe "WrapperDl4j" en lui fournissant les outils utiles a Spark.

Elle possède ainsi des méthodes pour: 

* spécifier la configuration de Spark (localisation du moteur de Spark, timeout, heartbeat)
* initialiser le reseau sur Spark
* initialiser le "training master" qui est en charge de gérer le processus de calcul des paramètres du réseau à travers Spark.
* d'entrainer le réseau au travers de Spark.
* d'évaluer le réseau au travers de Spark.

Cette classe est construite autour de la bibliothèque deeplearning4j.

Choix de la topologie du réseau de neurones
------------------------------------------------
La topologie du réseau de neurones retenu pour ce projet est une topologie on ne peut plus simple. 

Le réseau de neurones est constitué d'une couche de convolution, d'une couche de pooling, d'une couche de complètement connecté et d'une couche de sortie pour deux classes.

Cette topologie a finalement été choisie à cause de la quantité de mémoire disponible pour faire les tests. En effet, d'autre réseau plus complexe possédant par exemple plusieurs
couches de convolutions, de pooling ont été essayé. Toutefois, les 32Go de mémoire RAM de la machine qui a servi au développement ne suffisait plus. Il a donc été décidé de partir
sur un réseau bien plus simple mais fonctionnel sur la machine de développement. 

Les résultats et les performances du réseau s'en ressentiront probablement lors des tests et des expérimentations. Toutefois, ce projet
vise, avant tout, à vérifier que le traitement de fichiers au format NIFTI par un réseau de deeplearning est envisageable et peut donner des résultats
encourageant vers le développement de cette technologie pour la plateforme du LREN.

Implémentation
================
Ce chapitre décrit de manière technique le fonctionnement du programme réalisé dans le cadre du projet qui nous intéresse.
Les points les plus importants de l'implémentation sont décrit dans cette partie de rapport. Ainsi, la configuration d'une expérience,
la manière dont sont lu les données et la configuration d'un réseau sont abordé ici. Ce chapitre se terminera par une explication technique
des différentes manières d'entraîner et d'évaluer un réseau de neurones avec la bibliothèque Deeplearning4j.

Configuration d'une expérience
------------------------------
Une expérience se configure à l'aide d'un fichier "\*.properties". Ce fichier contient une série de couple "clé-valeur". Une clé est séparé
de sa valeur par un signe "=". La classe "Configuration" est chargé de lire ce type de fichier et de stocker les différentes valeurs obtenues
dans les attributs de la classe. Ces attributs sont tous accessibles de l'extérieur et permettent d'influencer le fonctionnement du programme.
Un fichier "\*.properties" par défaut peut être créer par le programme si ce dernier est lancé sans arguments.

Pour faire tout cela, la classe "Configuration" instancie un objet de la classe "Properties" contenu dans le package "java.util". Cette classe fournit
les outils adéquat pour le chargement d'un fichier "\*.properties", pour sa génération et pour sa lecture. Ainsi, le constructeur de la classe "Configuration",
dont voici la signature:

.. code-block:: java
   :linenos:

    public Configuration(String filename, boolean isGenerate)

prend comme paramètre le nom du fichier dont on va se servir et le paramètre booléen permet de dire si il doit généré ou lire le fichier spécifié. Cette variable booléenne
est a true si le programme est lancé sans nom de fichier passé en paramètre. Elle est à false si le programme a comme paramètre le nom d'un fichier de configuration. 

Si le programme doit généré un fichier de configuration, le nom de ce dernier sera "default.properties". Il sera généré par la méthode:

.. code-block:: java
   :linenos:

    public Object setProperty(String key,String value)

de la classe "Properties". Cette méthode prend en paramètre le couple "clé-valeur" sous la forme de chaîne de caractère. Une fois toutes les propriétés fixées, il suffit
d'écrire le fichier avec la méthode:

.. code-block:: java 
   :linenos:

    public void store(OutputStream out, String comment)

La classe "Configuration" possède une méthode

.. code-block:: java
   :linenos:

    public void generatePropertiesFile()

permettant d'encapsuler l'ensemble de la génération du fichier de configuration.

Si le programme doit lire un fichier de configuration, le nom de ce dernier est fournit comme paramètre au programme. La classe "Properties" de Java permet la lecture de ces
données avec la méthode: 

.. code-block:: java
   :linenos:

    public String getProperty(String key, String defaultValue)

Le paramètre "key" fournit doit correspondre à l'un des champs se trouvant à gauche du signe "=" dans le fichier lu. La valeur retourné par cette méthode peut alors être convertie dans
un format informatiquement compréhensible (int, boolean ou encore float). Cette valeur est ainsi stocké dans l'objet sous la forme d'un attribut. Chacun des attributs de la classe
"Configuration" possède un getter et permet ainsi la lecture pour le reste du programme des valeurs du fichier de configuration.

Génération de données
---------------------
La génération des données crée deux types de NIFTI. Ces NIFTI représentent des sphères ou des cubes. Pour générer des données ont utilise l'objet "DataTestGenerator". Celui-ci a
pour constructeur:

.. code-block:: java
   :linenos:

    public DataTestGenerator(int niftiSize, int cubeSize, String NIFTICubePrefix, int sphereSize, String NIFTISpherePrefix, int step)

Les paramètres numériques ne sont utiles que lorsque l'on demande une génération de NIFTI qui ne soit pas aléatoire. Le paramètre "niftiSize" fournit la taille des dimensions
spatiales x, y, et z. Elles auront toutes les trois la même tailles. "sphereSize" et "cubeSize" donne respectivement le diamètre d'une sphere et la longueur d'une face d'un cube
en nombre de voxel. Le paramètre "step" permet de donner au générateur un décalage pour la construction de la forme. Par exemple le premier cube se construit a partir du voxel
[0, 0, 0] et le second cube se construit à partir du voxel [step, step, step].

Les chaînes de caractères sont, dans tous les cas d'utilisation, le nom que portera le fichier avant un nombre. Si "NIFTICubePrefix" vaut cube, les fichiers contenant la représentation
d'un cube auront un nom sous la forme "cube_XXX.nii.gz" avec XXX un nombre correspondant a l'ordre de génération. 

L'objet "DataTestGenerator" fournit donc deux méthodes. L'un permettant de générer des sphères et des cubes de taille fixe à des positions définies (par le paramètre step). Sa
signature est la suivante: 

.. code-block:: java
   :linenos:

    public void generateSphereAndCube()

La seconde méthode de génération permet de créer des représentations de cubes et de sphères dont la taille et la position sont aléatoire. La signature de cette méthode est la suivante:

.. code-block:: java
   :linenos:

    public void generateSphereAndCubeSize(int batchSize, int nx, int ny, int nz)

Le paramètre batchSize permet de lui donner le nombre d'exemple de chaques formes à créer. Ainsi s'il vaut 300, 300 images de sphère et 300 images de cubes sont créées. Les paramètres
nx, ny, nz permettend de fixer une taille à chacunes des dimensions spatiales du NIFTI. Ainsi, on peut obtenir des NIFTI dont les dimensions vaudront celle couramment employé par le CHUV,
à savoir 190x190x160.

Pour ce qui est de la génération a proprement parlé, deux méthodes sont employées. Chacunes d'entre elle a pour but de créer une forme selon les informations qui lui sont données en argument.
Ces arguments sont les dimensions du NIFTI, la dimension du diamètre de la sphère ou la longueur du côté du cube, un offset pour la position de la forme et le nom du fichier à créer.
On parcours ensuite l'ensemble des voxels du NIFTI en imbriquant trois boucle "for". Pour chaque voxel, on calcul si il appartient ou non à la forme. S'il appartient à la forme on lui
assigne une valeur de 255 sinon une valeur de 0. 

Lecture des données
-------------------
La lecture des données nécessaires au fonctionnement du programme et la mise en forme de ces données dans un itérateur utilisable pour l'entraînement d'un réseau de neurones
se fait dans la classe "DataReader". Elle a pour but de pouvoir lire les données d'un fichier NIFTI et de pouvoir le convertir en INDArray. Le lot d'image pourra alors être
placé dans une collection itérable pour le réseau. Cette collection est un INDArrayDataSetIterator. Elle fournit ensuite les outils pour normalisé les données si besoin est.
Si le programme doit utiliser Spark comme plateforme de calcul, la classe "DataReader" est capable de fournir les listes qui doivent être placé dans les objets JavaRDD de Spark.
L'ensemble de ces outils sera détaillé dans la suite de ce chapitre.

Le constructeur de cette classe prend comme paramètre la localisation du dossier dans lequel sont contenus les NIFTI nécessaire à l'expérience et le ratio de données à mettre
dans le lot des images d'entraînement. Le ratio d'image à mettre dans le lot des images de validation est calculé à partir de ce dernier.

.. code-block:: java
   :linenos:

    public DataReader(String workFolder, int trainRatio)

Une fois l'objet instancié, il suffit de lui demander de créer les datasets. Pour faire cela, il faut faire appel à la méthode:

.. code-block:: java
   :linenos:

    public void createDataSet(int minibatchSize, Hashtable<String, INDArray> regLabel)

Cette méthode a comme paramètre une hashtable. Celle-ci fait correspondre à une chaîne de caractère un INDArray. Un INDArray est un tableau a N-dimension. Il est utilisé pour les
calcul matriciel dans la bibliothèque Deeplearning4j. Le INDArray ici présent représente une matrice d'une ligne et contenant un nombre de colonne égal au nombre de classe que l'on
désire. Cet INDArray représente un label. On fait correspondre ce label à une chaîne de caractère. Cette chaîne de caractère correspond à une chaîne de caractère que contient le nom
du fichier auquel on doit apposer le label lié. 

Prenons l'exemple d'un dossier rempli d'image de chien et de chat que l'on cherche à classifier. Les images de chiens portent toutes un nom sous la forme "chien_XXX.png" avec pour
XXX un nombre. Les images de chat portent un nom construit sur le même modèle ("chat_XXX.png"). Le label (INDArray) que l'on veut apposer aux images de chat correspond à {1, 0} et
le label pour les images de chien est {0, 1}. On lie donc la chaîne de caractère "chat" au label {1, 0} et "chien" au label {0, 1}. C'est pourquoi on fabrique une hashtable contenant
des chaînes de caractère et des INDArray. Ainsi, on peut parcourir la liste de tous les fichiers contenu dans le dossier et y chercher les chaînes de caractère contenu dans la table.
Si la chaîne est trouvé, on lit le fichier pour en extraire les données et on y appose le label correspondant.

Pour lire les données on se sert de la bibliothèque niftijio. Cette bibliothèque fournit une classe "NiftiVolume". Cette dernière permet de chargé le header et les données d'un NIFTI.
On peut ainsi facilement récupérer la taille des dimensions du fichier NIFTI. Dans notre cas seul les trois dimensions spatiales x, y et z. Une fois ces trois dimensions récupérées, on
instancie un tableau dont la taille vaut le produit de ces dernières. On imbrique ensuite trois boucle "for" afin de parcourir chacunes des dimensions et l'on ajoute au tableau que l'on
vient de créée la valeur du voxel que l'on est en train de lire à l'aide de nos boucles. 

Pour pouvoir être utilisable avec la bibliothèque Deeplearning4j, le tableau des données que l'on vient de construire doit être transformé en INDArray. Cette transformation se
fait à l'aide de la méthode: 

.. code-block:: java
   :linenos:

    INDArray array = Nd4j.create(tabFlat, tabShape)

Le paramètre tabFlat correspond au tableau contenant la valeur de chaque case de la matrice que l'on veut créer et le paramètre tabShape correspond a un tableau donnant la forme de
la matrice que l'on veut créer.

Une fois que les données sont lu et que les labels leurs sont liées, on peut créer un iterateur. Un itérateur est un objet dont les méthodes demandant l'entraînement du réseau se 
serve. La classe dataReader fabrique alors deux itérateurs. Un pour le lot de données d'entrainement et le second pour le lot de données de test. La taille de chacun de ceux-ci est
régis par le ratio fourni au constructeur de la classe. A la création de l'itérateur d'entraînement, nous lui fournissons le second paramètre de la méthode "createDataSet()". Celui-ci
correspond a la taille de minibatch de l'itérateur. C'est à dire le nombre d'image qui sont contenu dans chaque partition du dataset d'image complet.

Ces itérateurs sont alors stocké tel quel dans l'objet "DataReader" en tant qu'attribut. Il est alors possible, à l'aide de getter, d'y avoir accès. Cependant, un pré-traitement peut
être nécessaire pour se servir de ces données. Pour se faire, il existe des getter demandant la normalisation des données avant l'entraînement. La normalisation permet d'accélérer
l'apprentissage en utilisant des taux d'apprentissages plus élevé et de régulariser l'entraînement. La régularisation cherche a éviter l'"overfitting" (sur-apprentissage). La
normalisation se fait grâce à un objet appelé "NormalizerMinMaxScaler" fournit par Deeplearning4j. Il suffit ensuite de passer l'itérateur dans cet objet pour le normaliser. 

Lors de l'utilisation de Spark, les itérateurs ne sont pas utile. En effet, Spark se sert d'objet "JavaRDD". Ces objets ont besoin d'une liste de "Dataset" pour être créés. C'est pourquoi,
la classe "DataReader" permet la création et l'obtention de liste de "Dataset" à partir des itérateurs. Pour les fabriquer, il suffit de parcourir l'itérateur, d'en récupérer les "Dataset"
dans une liste et de la retourner. Pour créer une liste de "Dataset" normaliser, il suffit de normaliser l'itérateur comme vu dans le paragraphe précédent.

Configuration du réseau de neurones
-----------------------------------
Que l'on travaille sur Spark ou en local, la configuration d'un réseau se fait de la même manière. C'est pourquoi la configuration peut se mettre dans la classe mère "WrapperDl4j".
La bibliothèque Deeplearning4j fournit la classe "NeuralNetConfiguration". Cette classe permet de construire une configuration de réseau de neurones à l'aide d'un "Builder".
Cette classe sert à construire la quasi-totalitée des réseaux de neurones avec Deeplearning4j. Il offre une grande souplesse dans la construction des différents types de couches de 
réseau de neurones. 

Il existe dans la documentation de "Deeplearning4j", une liste complète des différents paramètres que le constructeur ("Builder") de cette classe peut prendre [#]_.
Ce rapport se contentera d'expliquer quels ont été les paramètres qui ont été utilisé. Ainsi la configuration du réseau utilisé dans ce programme correspond aux quelques lignes de
code ci-dessous: 

.. [#] https://deeplearning4j.org/neuralnet-configuration

.. code-block:: java
   :linenos:

    this.conf = new NeuralNetConfiguration.builder()
            .seed(seed)
            .iterations(iteration)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(learningRate)
            .regularization(true)
            .l2(0.0001)
            .update(Updater.RMSPROP)
            .list()
                .layer(0, convolutionLayer)
                .layer(1, subsamplingLayer)
                .layer(2, denseLayer)
                .layer(3, outputLayer)
            .pretrain(false)
            .backprop(true)
            .setInputType(InputType.convolutional(height, width, depth))
            .build();

Le paramètre "seed" permet de donner une graine au générateur de nombre pseudo-aléatoire contenu dans cette configuration. Ce générateur est employé pour chaque initialisation aléatoire
dans le réseau.

Le paramètre "itérations" permet de fixer le nombre d'itération a effectué durant l'apprentissage pour l'optimisation. Autrement dit, une itération est une mise à jour des paramètres
du modèles de réseau de neurones. Ce paramètre est une valeur configurable au travers du fichier de configuration du programme. Une valeur de 100 avec les données générées par le programme
donne de bon résultat.

Le paramètre "optimizationAlgo" permet de fixer un algorithme d'optimisation pour la mise à jour des biais. Il est possible de sélectionner différents algorithmes. Toutefois, pour ce
travail, l'algorithme le plus couramment employé a été choisi. Il s'agit de la descente de gradient stochastique. D'autres algorithmes sont plus puissants que la descente de gradient
stochastique. Toutefois, ils sont également plus coûteux en terme de temps.

Le "learningRate" est le taux d'apprentissage du réseau. Le "learningRate" correspond au pas des modifications sur le vecteur des paramètres. Imaginons un paramètre valant 1 avec
un "learningRate" de 0.5. Lors de la prochaine mise à jour des paramètres, ce paramètre prendra la valeur de 0.5 ou de 1.5. Une valeur faible aura tendance à allonger le temps nécessaire
à l'apprentissage du réseau. Cependant, un taux d'apprentissage trop grand réduira ce temps mais aura le risque de provoquer un dépassement de la valeur désiré. Ainsi, si la 
valeur cible est de 1.7, avec notre exemple précédent, nous risquons de passer à 2 et de ne pas atteindre le but.

Le paramètre "regularization" permet d'activer ou non la régularisation du réseau. Le but de la régularisation est de limiter le risque d'"overfitting" (sur-apprentissage)du réseau.
Il existe différent type de méthode de régularisation dans Deeplearning4j:

* La régularisation l1 et l2 sont des méthodes très communes. Elles cherchent a empêcher les poids dont les valeurs sont des extrêmes. Toutefois, il faut faire attention à ne pas
  donner de trop grande valeurs au paramètre de ces méthodes. En effet, cela peut avoir comme effet d'empêcher le réseau d'apprendre correctement. Pour ce travail, la méthode de régularisation
  l2 a été choisie. Cette méthode fonctionne communément bien avec des valeurs comprise entre 0.001 et 0.00001. Cette valeur est configurable dans le fichier de configuration du programme.
* La régularisation par "Dropout" est également utilisable. Cette méthode éteint des noeuds dans le réseau de neurones en les mettant à 0. Le réseau doit par conséquent compter sur
  d'autres parties du réseau.
* "DropConnect" fonctionne sur le même principe que la méthode précédente mais en éteignant des connexions et non pas des noeuds. Cette technique est moins usuelle.
* La régularisation peut également se faire en limitant le nombre de couche et la taille de ces dernières. Notre réseau est très simple et respecte donc par défaut cette règle.
* Il existe une dernière méthode de régularisation. Celle-ci est la méthode dites d'"early stopping". Avec cette technique, on cherche à arrêter l'entrainement du réseau lorsque le score
  du réseau lors des tests diminue alors que les scores du réseau lors de l'apprentissage continue d'augmenter.

Le paramètre "update" a pour but de fournir une méthode d'appréciation du learningRate. Elles permettent de modifier le learningRate durant l'entraînement pour le faire converger
plus vite. Pour ce travail, le choix de la méthode RMSPROP a été fait. Car c'est avec cette méthode que les meilleurs résultats ont été obtenus. Ce choix est, ainsi fait, par l'expérimentation.

Le paramètre "list" correspond à la liste des différentes couches du réseau. Les différentes couches sont décrite plus loin dans ce chapitre. En résumé, on crée ici un réseau de neurones
de convolution très simple. Ce réseau possède une couche de convolution suivi d'une couche de pooling puis une couche de réseau entièrement connecté et d'une couche de sortie.

Le paramètre pretrain permet d'activer ou non le pré-apprentissage du réseau. Le pré-apprentissage permet de fournir une idée au réseau des données qu'il va devoir traiter. Pour se faire, on passe
les données dans le réseau sans leur donner de label et de minimiser l'erreur sur la reconstruction des données. Ca permet au réseau de se souvenir des caracéristiques importantes des données. Durant 
la phase de pré-apprentissage, on essaie pas de classifier l'image. Le pre-apprentissage étant long le choix a été fait de désactiver cette phase.

Le paramètre "backprop" permet d'activer ou non la rétropropagation. Cette technique est très souvent utilisé. Elle est donc activée dans notre projet. Cette technique permet de calculer l'erreur faites
par chaques neurones et de la corriger selon l'importance des éléments ayant participé a cette erreur.

Le paramètre "setinputType" permet de spécifier le type d'entrée. Dans notre cas, on spécifie au réseau que l'entrée est une convolution sur une matrice de 2048x2880 se basant sur les niveaux de gris.

Une fois le comportement du réseau configuré, il faut préparé la topologies du réseau. Pour se faire on crée des layers et on les ajoutes après le paramètre "list". La topologie choisi pour ce travail
est la topologie la plus simple possible. Des essais ont été fait avec des topologies plus complexe, toutefois la mémoire de 32Go sur laquelle tournait les test était rapidement débordé. L'idée a donc
été de simplifier le réseau pour permettre des tests. 

Le réseau commence par une couche de convolution. Cette couche a un noyau de convolution d'une taille de 20x20. Cette taille a été choisie car la matrice a traiter est vraiment très importante. Pour des
images classiques il est recommander d'utiliser des noyaux de convolution compris entre 11x11 et 15x15. Il a été choisie de prendre plus grand également pour des raisons de mémoire. Le pas entre chaque
application d'un noyau de convolution est de 20x20 également celà empêche que les noyaux se chevauche. Il est en principe conseillé de permettre le chevauchement. Toutefois, pour des raisons de mémoire,
il était impossible de réaliser ce chevauchement. Cette couche fonctionne avec une activation RELU. Cette activation est conseillé dans les réseaux de convolution. La classe nécessaire à la création de
cette couche est la classe "ConvolutionLayer".

Le réseau se poursuit avec une couche de pooling. Le rôle de cette couche est de réduire la résolution de l'image. Le noyau de pooling est un noyau de taille 20x20. On devrait en principe prendre de très
petit noyau (par exemple 2x2). Cependant pour des raisons de mémoire et pour des images aussi grande, il a été choisie de grandement agrandir le noyau. La classe utile pour créer cette couche est la classe
"SubsamplingLayer". La méthode de pooling choisie est un pooling Max. C'est a dire que l'on prend la valeur maximum contenu dans le filtre.

La couche de pooling est suivi par une couche "DenseLayer". Cette couche est une couche de réseau de neurones entièrement connecté. Chaque valeur d'entrée est connecté à chaque neurone de la couche.
Sa fonction d'activation est une fonction RELU. Cette fonction a également été choisie car elle semble être la fonction standard. Le nombre de sortie de cette couche peut être configuré avec le fichier de
configuration. De bons résultats ont été obtenu avec un nombre de sortie égal à 50.

Après cette couche de réseau dense, la couche de sortie est ajouté. La couche de sortie est un objet de la classe "OutputLayer". Il prend comme paramètre une fonction de "loss" qui dans notre cas est la
fonction "NEGATIVELOGLIKELIHOOD". Cette fonction de "loss" est une des fonctions les plus régulièrement trouvé dans les différents exemples de réseau avec Deeplearning4j. C'est pourquoi elle a été
utilisé. Le nombre de sortie de cette couche doit correspondre au nombre de classe de notre "classifier". Ce nombre de sortie peut être configuré dans le fichier de configuration du programme. Pour
les expériences il a été utilisé avec une valeur de 2. La fonction d'activation de cette couche est la fonction "SOFTMAX" qui est très souvent conseillé lorsqu'il s'agit de faire un classifier avec 2
classes.

.. figure:: images/schemaReseau.png
   :width: 350px
   :align: center
   :alt: Topologie du réseau

   Topologie du réseau


Note sur l'apprentissage avec Deeplearning4j
--------------------------------------------
Comme nous le verrons dans la suite de ce chapitre, l'entraînement avec Deeplearning4j ne fait appelle qu'à une simple méthode "fit()". Cette méthode fonctionne plus ou moins de la même manière que
ce soit avec un réseau de neurones fonctionnant sur Spark ou sur une simple machine. Nous allons donc voir ici comment a été pensé l'entraînement dans la bibliothèque Deeplearning4j et avec notre exemple
où la rétropropagation est activée.

Dans un premier temps, les différentes entrées et les différentes sorties du réseau de neurones sont définies sous la forme d'INDArray. Un "Solver" est ensuite créée. Ce dernier est un objet chargé de faire
les calculs d'optimisation. Dans le cadre de notre implémentation le "Solver" créé cherche à optimiser la descente de gradient stochastique. Pour ce faire, il va, dans un premier temps, calculer le gradient
et dans un second temps mettre à jour les différents paramètres (poids) du réseau. C'est lors de la phase de mise à jour des paramètres que Deeplearning4j utilise le "learningRate", les méthodes d'update, 
etc.

Le calcul du gradient commence avec la couche de sortie du réseau qui calcul une erreur de base en fonction des prédictions faîtes, les labels des données et la fonction de "Loss" de la couche de sortie.
Puis cette erreur est propagé vers la couche précédente qui elle-même va la propager vers la couche suivante. Une "hashmap" est alors créé et contiendra les gradients de chaque couche du réseau. Cette
"hashmap" est utilisé par la suite pour mettre les poids à jour.

La mise à jour des paramètres se fait alors en utilisant la "hashmap" précédemment créé et en y appliquant les différentes méthodes configuré dans le réseau.

Ces différentes étapes sont répétés autant de fois que nécessaire durant la phase d'entrainement.

Entraînement et évaluation sans Spark
-------------------------------------
Notre réseau est à présent configuré et nous avons vu les différentes étapes d'un entrainement avec la librairie Deeplearning4j. La configuration est un objet de la classe "NeuralNetConfiguration". Et doit,
dans le cas ou l'on veut employer le réseau de neurones sans Spark, pour configurer un objet de la classe "MultiLayerNetwork". Pour faire celà, il suffit de passer l'objet "NeuralNetConfiguration" au
constructeur d'un "MultiLayerNetwork".

Une fois cette étape faites, il ne reste plus qu'à entrainer le réseau de neurones en faisant appel à la méthode "fit()" du "MulitLayernetwork". Cette méthode prend en argument un itérateur ou un dataset.
Dans notre cas nous avons fait le choix de l'itérateur et nous lui donnons l'itérateur contenant les données d'entraînements.

Il est ensuite possible d'effectuer une évaluation de l'entraînement avec la méthode "eval()" de l'objet "MultiLayerNetwork". Cette méthode prend en paramètre un  itérateur ou un dataset. Dans notre cas,
nous lui passons l'itérateur de test.

Il suffit ensuite d'écrire dans la console les résultats de l'évaluation pour voir les fruits de l'entraînement du réseau.

Entraînement et évaluation avec Spark local
-------------------------------------------
Avant de débuter ce chapitre, il est important de rappelé que dans l'implémentation actuel de Spark dans Deeplearning4j, chaque machine du cluster possède le réseau de neurones complet.
Comme précédemment, la préparation de la configuration du réseau se fait avant de choisir si l'on travaille sur Spark ou non. Une fois l'objet "NeuralNetConfiguration" préparé, il faut créer un
"TrainingMaster. Cet objet gère la distribution du travail sur Spark. Dans notre cas, nous avons un "TrainingMaster" configuré comme suit:

.. code-block:: java
   :linenos:

    TrainingMaster trainingMaster = new ParameterAveragingTrainingmaster.Builder(rddDataNbExemple)
        .averagingFrequency(averagingFrequency)
        .workerPrefetchNumBatches(workerNumBatch)
        .batchSizePerWorker(batchSizePerWorker)
        .build();

Le paramètre "rddDataNbExemple" placé dans le "Builder" correspond au nombre d'exemple contenu dans le dataset. Il est obligatoire. Dans notre cas, ce paramètre peut être configuré dans le fichier
de configuration, tout comme les autres paramètres de construction d'un "TrainingMaster".

Le paramètre "averagingFrequency" permet de choisir la fréquence à laquelle les poids sont mis à jour. Cette fréquence est un nombre de minibatch. Il faut faire attention a la valeur que l'on veut. Un
nombre trop élevé de minibatch risque de faire trop largement divergé les paramètres et l'entraînements risque d'être mauvais. Une taille trop faible et l'entraînement sera lent à cause du nombre de communication
nécessaire. Il est en principe conseillé par Deeplearning4j de choisir une valeur entre 5 et 10. Le choix de ce paramètre est fixé à 5, par défaut, dans ce projet.

Le paramètre "workerPrefetchNumBatches" permet de fixé le nombre de données que peuvent préchargé chacun des "workers" Spark. Une valeur de 0 désactive cette option. Il est conseillé par la documentation
de Deeplearning4j d'employé une valeur de 2.

Le paramètre "batchSizePerWorker" fixe la taille des minibatchs pour chaque "worker" du cluster. Celà fonctionne de la même manière que pour une seule machine. Toutefois, il faut noté que celà indique
également le nombre d'exemple utilisé avant une mise à jour des poids du réseau qui fonctionne sur une machine.

Il existe évidemment bien plus de paramètre possible pour la création du "TrainingMaster" [#]_.

.. [#] https://deeplearning4j.org/spark

On doit également configuré le contexte de Spark. Ceci se fait de cette manière:

.. code-block:: java
   :linenos:

    SparkConf sparkConf = new SparkConf();
    sparkConf.set("wrapper.spark.network.timeout", sparkTimeOut);
    sparkConf.set("wrapper.spark.executor.heartbeatInterval", sparkHeartBeatInterval);
    sparkConf.setMaster(sparkMaster);
    sparkConf.setAppName(appName);
    JavaSparkContext jsc = new JavaSparkContext(sparkConf);

De cette manière, nous récupérons un objet de configuration d'un contexte Spark. Dans notre cas, nous allons fixé quelques paramètres qui sont apparu comme problématique au cours des essais.
Ces paramètres sont le "timeout" du réseau de neurones fonctionnant sur Spark et le "heartbeatInterval". Le paramètre de "timeout" est le timeout général pour toutes les intéractions Spark, tandis
que le paramètre "heartbeatInterval" correspond à l'intervalle de temps entre deux signaux envoyés d'un exécuteur vers le chef d'orchestre de Spark. Ces signaux permettent au chef d'orchestre de
s'assurer que l'executeur est toujours en fonctionnement. Dans les faits le "heartbeatInterval" devrait être bien inférieur au "timeout".

Il existe bien d'autres paramètres modifiables et sont répertorié dans la documentation de Spark [#]_.

.. [#] https://spark.apache.org/docs/latest/configuration.html

La méthode "setmaster()" utilisé ensuite permet de donner le paramètre de connexion au moteur d'exécution de Spark. Pour une utilisation en local, sa valeur doit être la chaîne de caractère "local[\*]"
ou la chaîne "local[x]", avec X le nombre de coeur à utiliser.

Au terme de ces lignes de code, on obtient un contexte Spark utilisable en Java. Il ne reste donc plus qu'à créer un réseau de neurone compatible avec Spark. Deeplearning4j fournit alors un objet
qui s'instancie de cette manière:

.. code-block:: java
   :linenos:

    SparkDl4jMultiLayer sparkNet = new SparkDl4jMultiLayer(jsc, conf, trainingMaster);

Il prend en paramètre, le "TrainingMaster", le contexte Spark et la configuration du réseau de neurones. Il permet par la suite d'utiliser notre réseau de neurones de la même manière qu'un MutliLayerNetwork simple.
Ceci à un détail près. Le paramètre que prend la méthode "fit()" du réseau sur Spark n'est pas un itérateur ou un "DataSet". Il s'agit d'un objet "JavaRDD<Dataset>". Ce dernier s'instancie ainsi:

.. code-block:: java
   :linenos:

    JavaRDD<DataSet> trainData = jsc.parallelize(listTrainData);

Le paramètre "listTrainData" est une simple liste de "DataSet". Celle-ci peut être obtenu avec le "dataReader" du projet.

Entraînement et évaluation avec Spark sur un cluster
----------------------------------------------------
(Attention! Cette partie n'a pas pu être testé. En effet, l'accès à un cluster n'a pas été possible pendant la durée du projet.)

Pour faire fonctionner le "SparkDl4jMultiLayer" non plus en local mais sur un cluster, il faut faire quelques adaptions. 

Au moment de la configuration du "TrainingMaster", il est nécessaire de modifier la chaîne de caractère passée en paramètre. La chaîne de caractère à passer devient:

* "spark://HOST:PORT" si le réseau de neurones doit tourner sur un cluster fonctionnant sur Spark.
* "mesos://HOST:PORT" si le réseau de neurones doit fonctionner sur un cluster tournant sur Mesos.
* "yarn-client" si le réseau de neurones doit se connecter sur un cluster Yarn en tant que client. L'adresse du cluster doit se trouver dans le HADOOP_CONF_DIR.
* "yarn-cluster" si le réseau de neurones doit tourner sur un cluster Yarn en mode cluster. L'adresse du cluster se trouve dans le HADOOP_CONF_DIR.

Si on lance le réseau sur un cluster avec un fichier jar, il faut faire attention au paramètre de lancement. Ceux-ci dépendront du cluster sur lequel on veut le lancer.

Sauvegarde et chargement d'un réseau
------------------------------------
Le réseau de neurones du projet peut-être sauvegarder de 4 manières différentes. La manière de sauvegarder un réseau se configure dans le fichier de configuration du projet.
Ces modes de sauvegarde sont: 

* Sauvegarde du modèle dans un fichier binaire. Ce mode de sauvegarde ne sauve que la configuration du réseau.
* Sauvegarde du modèle dans un fichier JSON. Ce mode de sauvegarde ne sauve que la configuration du réseau.
* Sauvegarde du modèle dans un fihcier YAML. Ce mode de sauvegarde ne sauve que la configuration du réseau.
* Sauvegarde du modèle dans un dossier zip. Ce zip contient le modèle du réseau de neurones ainsi que l'état des différents paramètres.


Pour la sauvegarde dans un fichier binaire, il faut utiliser la méthode suivante:

.. code-block:: java
   :linenos:

    DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(modelName + ".bin")));
    Nd4j.write(network.params(), dos);

On écrit ainsi les différents paramètres du réseau dans le fichier binaire.

Pour les sauvegardes en JSON et YAML la manière de faire est quasiment la même. Il suffit de  d'écrire dans un fichier, à l'extension correspondante,
le retour de la méthode "getLayerWiseConfiguration()". Cette méthode peut être suivi d'une fonction "toYaml()" ou "toJson()".

Pour la sauvegarde complète, Deeplearning4j fournit un objet "ModelSerializer". Ce dernier permet via sa méthode "writeModel()" de sauvegarder la
configuration du réseau et les paramètres de ce dernier. Cette méthode prend en paramètre le réseau de neurones à sauvegarder, le chemin ou sauvegarder le réseau et
un paramètre booléen activant ou non la sauvegarder des paramètres du réseau. Cette sauvegarde permet de recharger le réseau dans son état final.

Pour ce qui est du chargement, celui-ci peut se faire également à l'aide de l'objet "ModelSerializer". Celui-ci offre en effet une méthode
"restoreMultiLayerNetwork()". Celle-ci permet de charger un "MultiLayerNetwork" avec le réseau sauvegardé.

Expérience réalisée avec le CHUV
=================================
Afin de permettre de vérifier la faisabilité du projet demandé, le LREN a demandé la réalisation d'une expérience de classification sur des images IRM au format NIFTI qu'il utilise
sur leur plateforme.

Ce chapitre décrit la données de l'expérience, la préparation, le déroulement, l'exécution de l'expérience et les résultats obtenus. Pour des raisons de confidentialité, aucune image
fournit par le LREN ne pourra être affiché ici en guise d'exemple.

Données de l'expérience
----------------------
L'expérience dont le LREN demande l'exécution a pour but de permettre la classification entre des cerveaux contrôlés et ne portant pas Alzheimer et des cerveaux ayant Alzheimer. 

Pour réaliser cette expérience, le CHUV a fournit un lot de  183 images. Sur ces 183 images, (nb images HC nb image AD). Les images fournies avaient déjà, en partie, été prétraitées.
Ainsi les images ne représentait que la partie contenant la matière grise du cerveau.

En se servant du réseau de neurones inclut dans le programme et du fichier de configuration, il fallait essayé de les classifier dans ces deux classes.

Préparation et exécution de l'expérience
----------------------------------------
Pour réaliser cette expérience, les images ont été mise dans un dossier IRM_Expérience. Ce dossier contenait en vrac toutes les images NIFTI. Cette expérience a été réalisé en local sur la machine sans Spark puis sur Spark
en local. Lancer l'expérience en local a permis de de visualiser l'expérience grâce la UI fournit par Deeplearning4j, puis sur Spark pour vérifier que les résultats soient les mêmes. 

.. figure:: images/DL4J_UI_Example.png
   :width: 350px
   :align: center
   :alt: Illustration d'exemple de l'interface utilisateur de Deeplearning4j

   Illustration d'exemple de l'interface utilisateur de Deeplearning4j

Cette UI a permis de faire un peu de tunning sur les paramètres du réseau. Afin d'utiliser au mieux cette interface utilisateur il est utile de
savoir que: 

* Le diagramme en haut à gauche représente le score en fonction des itérations. Il s'agit de la valeur
  de la focntion de "loss" durant le minibatch courant. Cette courbe devrait, de manière général, toujours être décroissante.
  Les caractéristiques de cette courbe peuvent aider à règler les paramètres de l'expérience:

  + Si le score est de manière général est croissant, le "learningRate" est probablement trop grand ou que les données sont mal normalisées.
  + Si le score est plat ou à décroissance lente:
    
    - Le "learningRate" est trop faible
    - L'optimisation choisi n'est pas la bonne

* Le tableau en haut à droite fournit des informations sur le réseau de neurones.
* Le diagramme en bas à gauche fournit des informations sur le ratio de paramètres à mettre à jour par couche. Elle représente les poids par rapport aux itérations.
  il fournit un outils pratique pour paramètrer le "learningRate".
* Le diagramme en bas à droite fournit des informations sur les déviations standards par rapport au temps de l'activation, des gradients et des mise à jours des poids.
  Il le fait pour chaque couche du réseau.

  + Pour l'activation une bonne déviation standard doit se situer et être stabilisé entre 0.5 et 2.0.
    Si ce n'est pas le cas, le problème peut venir d'une iniatilisation des poids trop faible, d'une régularisation trop importante, d'un manque de normalisation des données ou d'un "learningRate" trop haut.
  + Pour les paramètres, seul la plus récentes des itérations est affiché. Si cette courbe part vers une asymptote verticale, le "learningRate" est trop haute ou la régularisation est trop faible.
  + Pour les mise à jour, seul la plus récentes des itérations est affiché. Si cette courbe a de grandes valeurs, celà peut indiquer un problème de "learningRate" ou de normalisation.

C'est en se servant de ces outils que les paramètres ont été fixé, notamment du premier diagramme. 

Au terme de nombreuses tentatives, les meilleurs résultats ont été obtenus avec ces paramètres:

* Ratio d'images d'entraînement 80% et 20% d'images de validation. Ces ratios sont appliqués de manière stratifiée.
* Taille des minibatchs à 8
* Normalisation des données activée
* Nombre de sortie de la couche dense égal à 100
* Nombre de filtre à appliquer égal à 10
* Nombre d'itération égal à 100
* Nombre de label égal à 2
* Nombre de couleur égal à 1 (donc niveau de gris)
* LearningRate égal à 0.001
* Valeur de la régularisation l2 égal à 0.0005

Le réseau de neurones utilisé correspond à celui-implémenter dans le programme et n'a donc pas été chargé depuis un fichier précédemment sauvegardé.
Les résultats obtenus sont exposé dans la partie suivante.

Résultats
---------
Cette expérience nous a fournit comme les résultats suivants au terme de l'évaluation: 

(Insert printscreen résultat)

Les résultats affichés affichés ci-dessus sont plutôt réjouissant. On peut voir que le réseau permet de classifier les différents type des cerveaux avec un taux de réussite de près de 70%. 


Analyses du projet
==================
Ce chapitre a pour but de présenter une analyse du projet rendu. Le rapport présente dans la suite de ce chapitre, les fonctionnalités implémentés, les fonctionnalités manquantes, les difficultés rencontrées
seront abordées. Une explication aux deux projets rendus est également donnée.

Fonctionnalité implémenté
-------------------------
Ce projet implémente les fonctionnalités suivantes:

* Génération de données au format NIFTI. Ces fichiers représentent des sphères et des cubes.
* Lecture d'une base de données d'images. Cette base de données d'images est un simple dossier contenant en vrac les images nécessaires à l'entraînement et à la validation du réseau de neurones.
* Configuration d'un réseau de neurones.
* Déploiement du réseau de neurones sur une machine simple, sur Spark en local et sur un cluster.
* Sauvegarde du modèle d'un réseau de neurones sur un fichier (YAML, JSON, BIN)
* Sauvegarde et chargement du modèle et des poids d'un réseau de neurones.
* Entraînement et évaluation d'un réseau de neurones.
* Configuration du programme par un fichier de configuration afin de pouvoir aisément modifier de nombreux paramètres du programme.


Grâce à ces fonctionnalités des expériences ont pu être exécuté. La suite de ce chapitre présente les conclusions que permettent de faire les résultats de ces dernières.

Conclusion sur les résultats des expériences
--------------------------------------------
Grâce à ce projet, deux types d'expériences ont été exécutés. La première consiste en la classification de 300 images de sphères et de 300 images de cubes au format NIFTI. Ces fichiers sont des images noirs et blanches et respecte précisément le format des fichiers du CHUV.
La seconde expérience est celle fournit par le LREN et consiste en la classification d'image IRM de la matière grise du cerveau. Ces dernières sont en niveaux de gris et sont évidemment plus bruité que les images générées.

La classification des formes simples ont données d'excellents résultats malgré la simplicité du réseau de neurones utilisé. En effet, cette expérience a permis de classifier les sphères et les cubes avec un taux de succès de près de
97.5%. Ces résultats prouvent que la classification d'images au format NIFTI est possible avec un réseau de neurones. Toutefois, il ne faut pas perdre de vue le fait que ces images sont très simple. Le traitement
de NIFTI semblant être possible, il a été intéressant d'ensuite tenté l'expérience du CHUV.

.. figure:: images/Schema_Classe.png
   :width: 350px
   :align: center
   :alt: Schéma de classe

   Schéma de classe

Cette dernière a obtenu des performances bien moindre. Avec ces taux de classification proche de 69.4%, on ne peut pas dire que la fiabilité du résultat est suffisante pour dire que l'expérience est un succès
total. Toutefois, ces images sont beaucoup plus complexe que les précédentes et le réseau est toujours aussi simple. De plus le nombre d'images alloué à cette expérience est très faible (183). Avec plus de temps et une machine plus puissante,
il aurait été possible de mieux ajuster les paramètres du réseau (d'essayer plus de mélanges de paramètres), d'essayer d'autre configuration de réseau. De plus, afin de créer un réseau entraîner performant avoir un jeu d'images plus imposants
permettrait de meilleurs résultats.

De ces deux expériences, nous pouvons dire que la classification d'images IRM du cerveau et le diagnostique automatique de maladie neurologique avec un réseau de neurones est très prometteurs.

Fonctionnalités manquantes
--------------------------
Pour être un projet parfaitement complet, il manque un certain nombre de fonctionnalité. Ces fonctionnalités n'ont pas pu être réalisé faute de temps:

* Intégration du programme à la plateforme du CHUV
* Modification du portail Web pour permettre l'utilisation des algorithmes de deeplearning
* Intégration des bases de données du CHUV
* K-Fold Cross Validation

Difficultés rencontrées
-----------------------
Comme la plupart des projets, certaines difficultés ont été rencontrées durant sa réalisation. Cette partie de rapport soulignes ces dernières afin de mettre en garde sur ces points là, dans le cas où se projet serait
repris.

Ces difficultés sont: 

* la gestion des dépendances de deeplearning4j avec Maven. Notamment, un problème de dépendance concernant l'interface utilisateur et l'utilisation de Spark. Ceci rend l'utilisation de Spark et d'une interface utilisateur bien plus complexe.
* la lecture des données et sa mise en forme pour qu'elles puissent être gérées par le réseau de neurones.
* la compréhension des différents paramètres du réseau de neurones et leurs effets sur l'entraînement de ce dernier.
* la gestion de la mémoire. En effet, la taille et le nombre de données à traiter ont facilement tendance a remplir la mémoire. Il était donc très difficiles de tester des types et des configurations de réseau de neurones différents sans faire planter la mémoire.

Pourquoi deux projets ?
-----------------------
Les difficultés rencontrées sont la raison pour laquelle le rendu contient deux projets. Notamment la difficulté à faire cohabiter les dépendances de Spark avec les dépendances de l'interface utilisateur de deeplearning4j.
Afin de palier a ce problème, un projet ne comportant que la partie ne fonctionnant pas sur Spark et une interface a été réaliser pour permettre de visualiser l'entraînement du réseau pour règler les paramètres de ce dernier.

Le dossier de rendu du projet complet est nommé "Projet_complet". Ce dossier contient le programme complet contenant les deux manières d'entraîner un réseau (avec ou sans Spark).

Le dossier de rendu du projet ne fonctionnant pas sur Spark mais permettant de visualiser l'entraînement en direct est nommé "Projet_demo".

Note sur la gestion de projet
===============================
Une part non-négligeable du projet portait sur sa gestion. En effet, les premières semaines du projet était réservé à sa plannification et un journal de travail devait être tenu régulièrement par l'élève
réalisant ce travail. De plus, ce projet est une collaboration entre le LREN du CHUV et la HES de Neuchâtel et il fallait pouvoir tenir les deux parties au courant des évolutions du projet. Pour rappel, ce projet
devait être réalisé sur un total de 450h. La suite de ce chapitre présentera la manière dont ces heures ont été organisé, la tenue du journal de travail, l'organisation de la collaboration et la méthodologie de travail
sont présenté. 

Les 450 heures alloués à ce projet furent réparties de cette manière:

* 50 heures (5h de travail par semaine pendant 10 semaines) ont été données à l'analyses du projet, aux choix des outils et à la prise en main de ces derniers.
* 320 heures (8h de travail par jour pendant 8 semaines) ont été données à la conception, la réalisation et les tests du programme.
* 80 heures (8h de travail par jour pendant 2 semaines) ont été données aux expériences, à leur analyse et à la rédaction du rapport.

Ce nombre d'heures a régulièrement été un peu dépassé afin d'avoir un programme et un rendu convenable aux dates demandées.

Au terme de chaque journée de travail, une dizaine de minutes était alloué à la tenue d'un journal de travail. Ce dernier résume en quelques mots le travail effectué durant la journée.

Afin de pouvoir collaboré avec le LREN de nombreuses petites scéances ont été prévus et faites durant la période de travail. Au minimum une rencontre entre l'élève et le superviseur du projet était organisé
par semaine. Le but de ces rencontres était de tenir au courant le superviseur de la HES des avancées du travail et de ce qui a été décidé lors des réunions entre l'élèves et le mandant du LREN. Les réunions entre
le mandant du CHUV et l'élève de la HES aurait dû avoir lieu chaque semaine. Toutefois, il n'a pas été possible de respecté ce rythme sur toute la durée du projet. Les deux parties ont donc fait au mieux pour fonctionner
ensemble. Ainsi, un Trello et un channel de discussion Slack ont été mise en place afin de facilité la communication entre le LREN et l'exécutant de la HES.

La méthodologie de travail du CHUV a été adopté pour ce travail. Ces derniers travaillent en utilisant une méthodologie agile. Donc bien qu'une plannification du travail ait été faites au début du projet,
cette dernière n'a que très peu servi de guide au travail.

Conclusion
============
Ce projet demandait la réalisation d'un pipeline permettant d'exécuter des expériences de
classification Deeplearning sur des images IRM au format NIFTI pour le CHUV.

Ce projet avait pour contrainte de pouvoir fonctionner sur une plateforme fonctionnant sur
Java Virtual Machine. De plus, il doit pouvoir permettre la distribution du calcul avec la
technologie Apache-Spark. Les images a traiter sont des images au format NIFTI, un format
spécialisé pour les scanner IRM.

Cette tâche a été découpé en différentes étapes. Les premières semaines du projet furent
consacrées à l'analyse du problème, au choix des différentes technologies et à la prise en main
de ces dernières par des travaux d'exemples. La seconde partie a servi à la conception du projet et
à son implémentation. Puis une troisième partie importante fût consacré au test et à divers essaie
afin de tuner au mieux le réseau de neurones. Une fois ce dernier en place, une expérience donnée par
le CHUV fût réalisé.

Les outils choisi pour la réalisation de ce travail sont:

* niftijio pour permettre de lire les fichiers au format NIFTI et les utiliser dans un code Java
* Deeplearning4j, une bibliothèque de deeplearning pour Java et pouvant fonctionner avec Spark et aussi bien sur un CPU que sur un GPU

Le programme conçu peut tourner aussi bien sur une machine seule que sur un cluster Spark.
Cette possibilité est offerte par le programme grâce à un fichier de configuration. Ce dernier permet,
en plus de choisir le mode d'exécution, de modifier des paramètres inhérents au réseau de neurones.

En effet, ce programme fourni un réseau de neurones plutôt simple. Ce dernier est un réseau de convolution fonctionnant
avec 4 couches (Convolution + Pooling + Dense + couche de sortie). D'autres topologies de réseau ont été essayé, toutefois
pour des raisons de mémoire sur la machine de test, le réseau a dû être simplifié au maximum.

Ce projet permet également de généré des données d'expérimentation au format NIFTI. En effet,
au début du projet, il fallait un jeu de données classifiable et simple. Afin, de ne pas perdre trop
de temps avec la recherche d'image IRM open-source, le choix a été fait de créer un générateur. Ce dernier
permet de créer des représentations de sphères et de cubes dans des fichiers NIFTI. Ces fichiers ont la même
caractéristiques que les images du CHUV. 

Les résultats obtenues par les essais de classification des sphères et des cubes sont prometteurs.
En effet, ces données très simple et binaire peuvent être classifier avec des taux de succès de 97,5%.
Il est a noté que l'entrainement et les tests se sont fait sur un lot de 600 images. 300 d'entre elles représentant
des cubes et les 300 autres représentant les sphères.

Pour obtenir ces résultats le programme fonctionne en séparant le jeu de données en 2 lots, un lot de test et un lot
d'entrainement. La séparation dans ces deux lots des fichiers se fait de manière aléatoire.

Les résultats prometteurs sur les cubes et les sphères ont permis de lancer l'expérience proposé par le CHUV. Celle-ci
visait à classifier des images IRM de la matière grise de patient. Cette classification visait à séparer les images de
cerveau porteur d'Alzheimer des cerveaux sains. 

Cette expérience a été réalisé avec un lot de 183 images et le meilleur résultats obtenu pour la classification de ces images
est de 69.4% de classification réussi.

Ces résultats sont sans doute encore améliorable mais sont déjà plutôt prometteur. Il semble indiquer qu'il est possible de classifier
des cerveaux selon ces critères. Toutefois, il faudrait des réseaux de neurones plus complexes, un jeu de données plus grand et
des techniques d'entrainements plus spécifique (EarlyStop, k-fold Cross Validation). Ces améliorations sont mentionnée dans la suite
de ce chapitre.

Il est donc possible de conclure que le projet de traité des données au format NIFTI d'IRM du cerveau est totalement réalisable. Celà pourrait
même fournir des résultats de diagnostique automatique très fiable. Cependant, pour faire celà, il faudra des machines bien plus performantes que
la machine ayant servi au développement de ce projet. Ainsi les réseaux de neurones utilisable pourront être plus complexe et fournir de meilleurs
résultats. Un cluster serait idéal pour réaliser une version plus performantes de ce projet.

Améliorations futures
---------------------
Comme tout travail, ce projet est encore perfectible. Cette partie du chapitre est consacré à quelques améliorations que ce projet
pourrait encore subir. 

Afin d'améliorer la qualité de l'entrainement, deeplearning4j recommande d'utiliser ce qu'il appelle la technique "earlyStopping".
Le fonctionnement de la méthode "earlyStopping" est relativement simple. Il suffit de séparer les données en 2 datasets, test et training.
Ceci est déjà en place dans le projet. Ensuite on entraîne le réseau  et à chaque epoch (ou N epoch), on
évalue le réseau avec le set de données de test. Si les performances de sortie du réseau sont meilleurs que les performances de sortie du réseau
à l'epoch précédente, on sauvegarde le model du réseau à l'epoch courante. On arrête l'entrainement selon des critères que l'on choisit.
Par exemple, un nombre d'epoch maximum ou un temps maximum. Le modèle final est le réseau sauvegardé et
le meilleur des modèles qui a été entrainé. Le but de cette méthode étant d'éviter l'overfitting (sur-apprentissage). Il arrêterait en
effet, l'entraînement au moment ou la précision de prédiction de test diminue. Le graphique ci-dessous montre ce que cherche à faire cette technique.

Cette solution est presque déjà implémenté grâce aux outils fournit par deeplearning4j[*]_. Toutefois, en l'état, lorsqu'elle
est utilisé cette dernière lève une exception dû à un problème de lecture d'un fichier qui n'existe pas. Cependant, le temps
manquant cette option n'a pas été terminé.

.. [*] https://deeplearning4j.org/earlystopping

Une autre des améliorations possible est l'intégration de la technique de "K-Fold Cross Validation". Cette technique consiste à séparer
les données en K dossiers. Généralement K vaudra 10. On prend ensuite un des K dossier comme ensemble de validation et les (k-1) ensembles
restant comme dataset d'entraînement. On entraine le réseau et on calcul la performance du réseau. On recommence ensuite l'opération en prenant un autre
ensemble comme ensemble de validation et l'on fait celà jusqu'à ce que tout les ensembles aient servis d'ensemble de validation. On calcul enfin la moyenne des
erreurs pour estimer l'erreur de prédiction que fait le réseau. 

Deeplearning fournit des outils pour réaliser cette technique. Ils ont un objet "KFoldIterator" qui sert
à réaliser cette expérience. Il faudrait donc le remplir avec nos données et le donner comme itérateur à la méthode d'entraînement de notre réseau.

Une autre amélioration possible serait d'intégrer/créer des outils d'"Automated Machine Learning". Ce concept est un concept encore très jeune (millieu 2016).
Le but de de ce concept est de permettre le choix du modèle, des hyperparameter d'un réseau et même la manière de vectoriser les données en entrée d'un algorithme de
machine learning. On pourrait donc imaginer celà adapter à deeplearning4j et au deeplearning. Il pourrait ainsi permettre l'automatisation du choix du modèle et de ces paramètres
pour avoir une expérience la plus proche possible de l'optimal. Ce concept est encore très récent et les implémentations de ce dernier sont encore très peu nombreuses. Toutefois,
le concept est prometteur et pourrait être intéressant à ajouter à un projet semblable.

Ressenti personnel et remerciements
-----------------------------------
A titre personnel, j'ai trouvé ce projet réellement très enrichissant. Ce dernier m'a permis de me perfectionner dans une branche que je trouve captivante.
Nous voyons assez peu de deeplearning durant les cours données à la HES de Neuchâtel. Et j'ai donc trouvé ca intéressant de pouvoir m'y plonger un peu plus au travers de ce projet.
J'ai ainsi beaucoup appris sur ce domaine. 

De plus, le fait de travailler avec le LREN sur un projet de l'envergure du "Human Brain Project" était une réelle motivation supplémentaire. J'espère avoir pu apporter
une contribution, aussi infime soit-elle, intéressante pour ce projet. C'est le genre d'initiative que je trouve importante. Et je suis ravi d'avoir pu travailler un moment sur ce projet.

Je tiens également, à travers cette partie de rapport, remercier Messieurs Fabrizio Albertetti et Arnaud Jutzeler pour leurs aides, leurs soutiens et leurs conseils lors de la réalisation de ce projet.

Sources
========
Toutes les sources citées ci-dessous ont été vérifié à la relecture de ce rapport en date du 26.01.2017.

Human Brain Project et LREN
---------------------------

* Département des neurosciences cliniques, "Le Human Brain Project au CHUV": http://www.chuv.ch/neurosciences/dnc_home/dnc-recherche/dnc-hbp.htm
* Laboratoire de recherche en neuroimagerie, "The Human Brain Project": https://www.unil.ch/lren/en/home/menuinst/human-brain-project.html
* Laboratoire de recherche en neuroimagerie, "Home page": https://www.unil.ch/lren/en/home.html
* Human Brain Project, "Home page": https://www.humanbrainproject.eu/en/
* Human Brain Project, "The medical Informatics Platform": https://www.humanbrainproject.eu/en/medicine/medical-informatics-platform/

Le calcul distribué
-------------------

* Wikipédia, "Calcul distribué": https://fr.wikipedia.org/wiki/Calcul_distribu%C3%A9
* Céline Hudelot et Régis Behmo, "Réalisez des calculs distribués sur des données massives": https://openclassrooms.com/courses/realisez-des-calculs-distribues-sur-des-donnees-massives/initiez-vous-au-calcul-distribue
* Pierre Pinard, "Calcul distribué": http://assiste.com/Calcul_distribue.html
* Emmanuel Jeannot, "Calcul distribué: une introduction": http://cdsweb.u-strasbg.fr/meeting3/Calcul_distribue.pdf
* Salvatore Salamone, "Le calcul distribué part à la conquête des entreprises": http://www.zdnet.fr/actualites/le-calcul-distribue-part-a-la-conquete-des-entreprises-2110797.htm
* SOSPC, "Calcul distribué": https://www.sospc2424.ch/site/fr/lexique/-99-calcul-distribue/
* Wikipédia, "Hadoop": https://fr.wikipedia.org/wiki/Hadoop

Le Deeplearning et réseau de convolution
----------------------------------------

* SQLI et Manuel Alves, "Le Deeplearning pas à pas: Les concepts (1/2)": https://www.technologies-ebusiness.com/enjeux-et-tendances/le-deep-learning-pas-a-pas
* SQLI et Manuel Alves, "Le Deeplearning pas à pas: Les concepts (2/2)": https://www.technologies-ebusiness.com/langages/le-deep-learning-pas-a-pas-limplementation-22
* Philippe Beraud, "Une première introduction au Deep Learning": https://blogs.msdn.microsoft.com/mlfrance/2016/04/28/une-premiere-introduction-au-deep-learning/
* Alp Mestan, "Introduction aux Réseaux de Neurones Artificiels Feed Forward": http://alp.developpez.com/tutoriels/intelligence-artificielle/reseaux-de-neurones/
* Wikipédia, "Réseau neuronal convolutif": https://fr.wikipedia.org/wiki/R%C3%A9seau_neuronal_convolutif
* Wikipédia, "Apprentissage profond": https://fr.wikipedia.org/wiki/Apprentissage_profond
* LISA lab., "Convolutional Neural Networks (LeNet)": http://deeplearning.net/tutorial/lenet.html
* Michael Nielsen, "Deep learning": http://neuralnetworksanddeeplearning.com/chap6.html
* Tim Dettmers, "Understanding Convolution in Deep Learning": http://timdettmers.com/2015/03/26/convolution-deep-learning/
* Rutger Ruizendaal, "Deep Learning #2: Convolutional Neural Networks": https://medium.com/towards-data-science/deep-learning-2-f81ebe632d5c
* Adam Geitgey, "Machine Learning is Fun! Part 3: Deep Learning and Convolutional Neural Networks": https://medium.com/@ageitgey/machine-learning-is-fun-part-3-deep-learning-and-convolutional-neural-networks-f40359318721
* Stanford University CS231n, "Convolutional Neural Network": http://ufldl.stanford.edu/tutorial/supervised/ConvolutionalNeuralNetwork/
* Stanford University CS231n, "CS231n: Convolutional Neural Networks for Visual Recognition": http://cs231n.stanford.edu/

La technologie Spark
--------------------

* Databricks, "Intro to Apache Spark": http://training.databricks.com/workshop/itas_workshop.pdf
* Wikipédia, "Apache-Spark": https://fr.wikipedia.org/wiki/Apache_Spark
* Apache Spark, "Apache-Spark": https://spark.apache.org/
* HortonWorks, "Apache Spark": https://fr.hortonworks.com/apache/spark/
* Alexis Seigneurin, "Introduction à Apache Spark": https://blog.ippon.fr/2014/11/06/introduction-a-apache-spark/
* Srini Penchikala, "Traitements Big Data avec Apache Spark - 1ère partie : Introduction": https://www.infoq.com/fr/articles/apache-spark-introduction
* Semtech Solutions Ltd, "An introduction to Apache Spark": https://fr.slideshare.net/mikejf12/an-introduction-to-apache-spark
* Nitin Bandugula, "Spark": https://mapr.com/blog/5-minute-guide-understanding-significance-apache-spark/

Deeplearning4j
--------------

* Wikipédia, "Deeplearning4j": https://en.wikipedia.org/wiki/Deeplearning4j
* Deeplearning4j, "Deeplearning4j": https://deeplearning4j.org/
* Deeplearning4j, "Deeplearning4j doc": https://deeplearning4j.org/doc/
* Github, "Deeplearning4j github": https://github.com/deeplearning4j
* Nd4j, "Nd4j": http://nd4j.org/
* Github, "Nd4j github": https://github.com/deeplearning4j/nd4j
* Gitter, "Deeplearning4j": https://gitter.im/deeplearning4j/deeplearning4j

Java
----

* Oracle, "Java": https://www.java.com/fr/
* Oracle, "Java 8 doc": https://docs.oracle.com/javase/8/docs/api/

Etat de l'art des bibliothèques de deeplearning
-----------------------------------------------
TensorFlow
++++++++++

* TensorFlow, "Home page": https://www.tensorflow.org/
* Github, "TensorFlow": https://github.com/tensorflow/tensorflow
* Shadaj, "TensorFlow in Scala with ScalaPy": http://blog.shadaj.me/2017/01/04/tensorflow-in-scala-with-scalapy.html
* Tim Hunter, "Deep Learning with Apache Spark and TensorFlow": https://databricks.com/blog/2016/01/25/deep-learning-with-apache-spark-and-tensorflow.htm
* PhD. Christopher Nguyen - Chris Smith - Ushnish De - Vu Pham et Nanda Kishore, "Distributed TensorFlow: Scaling Google's Deep Learning Library on Spark": https://www.slideshare.net/arimoinc/distributed-tensorflow-scaling-googles-deep-learning-library-on-spark-58527889


TensorFrames
++++++++++++

* Github, "TensorFrames": https://github.com/databricks/tensorframes
* Tim Hunter, "TensorFrames: Google TensorFlow on Apache Spark": https://fr.slideshare.net/databricks/tensorframes-google-tensorflow-on-apache-spark
* DataBriks, "TensorFrames user guide": https://github.com/databricks/tensorframes/wiki/TensorFrames-user-guide

BigDL
+++++

* Alexandre Rodrigues, "Intel open source BigDL, une bibliothèque d'apprentissage en profondeur distribuée pour Apache Spark": https://www.infoq.com/fr/news/2017/01/bigdl-deep-learning-on-spark
* Intel, "BigDL: Distributed Deep Learning on Apache Spark": https://software.intel.com/en-us/articles/bigdl-distributed-deep-learning-on-apache-spark
* Sue Ann Hong et Joseph Bradley, "Intel’s BigDL on Databricks: Distributed deep learning on Apache Spark": https://databricks.com/blog/2017/02/09/intels-bigdl-databricks.html
* Serdar Yegulalp, "Intel's BigDL deep learning framework snubs GPUs for CPUs": http://www.infoworld.com/article/3158162/artificial-intelligence/intels-bigdl-deep-learning-framework-snubs-gpus-for-cpus.html
* Docker, "Docker: BigDL": https://hub.docker.com/r/intelanalytics/bigdl/

Keras
+++++

* Joeri Hermans, "Distributed Deep Learning with Apache Spark and Keras": https://db-blog.web.cern.ch/blog/joeri-hermans/2017-01-distributed-deep-learning-apache-spark-and-keras
* Github, "Github: Elephas": https://github.com/maxpumperla/elephas
* Joeri Hermans, "Distributed Keras": http://joerihermans.com/work/distributed-keras/
* Keras, "Home page": https://keras.io/

Caffe on Spark
++++++++++++++

* Andy Feng, "CaffeOnSpark Open Sourced for Distributed Deep Learning on Big Data Clusters": http://yahoohadoop.tumblr.com/post/139916563586/caffeonspark-open-sourced-for-distributed-deep
* Github, "CaffeOnSpark": https://github.com/yahoo/CaffeOnSpark
* Xiaoyong Zhu, "Distributed Deep Learning on HDInsight with Caffe on Spark": https://blogs.msdn.microsoft.com/azuredatalake/2017/02/02/distributed-deep-learning-on-hdinsight-with-caffe-on-spark/

SparkNet
++++++++

* Github, "SparkNet": https://github.com/amplab/SparkNet
* Matthew Mayo, "Spark + Deep Learning: Distributed Deep Neural Network Training with SparkNet": http://www.kdnuggets.com/2015/12/spark-deep-learning-training-with-sparknet.html

dllib
+++++

* dllib, "A Distributed Deep Learning framework running on Apache Spark": https://www.lewuathe.com/dllib/
* Github, "dllib": https://github.com/Lewuathe/dllib
* dllib, "Introduction of dllib": https://www.lewuathe.com/introduction-of-dllib.html

Comparaison
+++++++++++

* Zhe Dong et Dr. Yu Cao, "Which is deeper: Comparison of Deep Learning Frameworks Atop Spark": https://www.slideshare.net/SparkSummit/which-is-deeper-comparison-of-deep-learning-frameworks-on-spark
* John Murphy, "Deep Learning Frameworks: A Survey of TensorFlow, Torch, Theano, Caffe, Neon, and the IBM Machine Learning Stack": https://www.microway.com/hpc-tech-tips/deep-learning-frameworks-survey-tensorflow-torch-theano-caffe-neon-ibm-machine-learning-stack/

Forums
------

* Stackoverflow, "Home page": https://stackoverflow.com/$

K-Fold Cross validation avec Deeplearning4j
-------------------------------------------

* Github, "KFoldIterator": https://github.com/deeplearning4j/nd4j/blob/63d6e2520acec97bce5a052512d1a73163163fd1/nd4j-backends/nd4j-api-parent/nd4j-api/src/main/java/org/nd4j/linalg/dataset/api/iterator/KFoldIterator.java
* Wikipédia, "Validation croisé": https://fr.wikipedia.org/wiki/Validation_crois%C3%A9e
* Wikipédia, "Cross-validation (statistics)": https://en.wikipedia.org/wiki/Cross-validation_(statistics)


Automated Machine Learning
--------------------------

* charleslparker, "Machine Learning Automation: Beware of the Hype!": https://blog.bigml.com/2017/01/23/machine-learning-automation-beware-of-the-hype/
* Hamel Husain, "Automated Machine Learning — A Paradigm Shift That Accelerates Data Scientist Productivity @ Airbnb": https://medium.com/airbnb-engineering/automated-machine-learning-a-paradigm-shift-that-accelerates-data-scientist-productivity-airbnb-f1f8a10d61f8
* Matthew Mayo, "The Current State of Automated Machine Learning": http://www.kdnuggets.com/2017/01/current-state-automated-machine-learning.html


Annexes
========
En annexe de ce travail sont fournit:

* Le cahier des charges du projet
* Le journal de travail de l'étudiant ayant travaillé sur ce projet
* La plannification du projet faites au début du projet
* Le manuel utilisateur pour les tests
* Un fichier de configuration commenté
* La Java Doc du projet
* Schéma de classe
* Schéma du workFlow du programme

