## Librairie et technologies nécessaires

<br/>**Front-end:**
<br/> Angular et npm

<br/>**Back-end:**
<br/> Maven(facultatif) et spring-boot

## Commandes pour lancer le projet

<br/>**Front-end:**
<br/> *npm install* puis *npm start*)
<br/> Se connecter à *localhost:4200*
<br/> ATTENTION : Importer les fichiers, PUIS les sélectionner avec les checkboxes

<br/>**Back-end:**
<br/> *mvn -e spring-boot:run* (ou *./mvnw -e spring-boot:run* si Maven n'est pas installé)

## Fonctionnement de la page

<br/>Le premier *éditeur de text* permet de gérer les imports de
<br/>librairies qui ne sont pas installées dans python classique. Pour
<br/>importer une ou plusieurs librairies, il suffit de noter le nom et
<br/>d'appuyer sur le bouton *upload*.

<br/>Le bouton *Load file with requested imports* permet de charger un
<br/>ficher contenant la liste des librairies nécessaires.

<br/>Le deuxième *éditeur de text* permet d'éditer un script. Le
<br/>bouton *upload* permet d'uploader ce script.

<br/>En dessous, On peut cliquer sur les boutons
<br/>*import or drag script here* pour charger un script dans l'éditeur
<br/>de script, ou *import or drag image here* pour uploader un fichier
<br/>de donné. Pour utiliser une donné, il faut la cocher tout en bas.

<br/>A droite, le bouton *Run posted script* permet d'exécuter un script
<br/>uploadé.
