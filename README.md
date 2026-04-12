# Contrôle SSH Java

Ce projet est une application client-serveur permettant d'exécuter des commandes système à distance via une socket Java.

## Structure du Projet

- `src/main/java/client/` : Contient le code du client.
- `src/main/java/serveur/` : Contient le code du serveur et l'exécuteur de commandes.
- `src/main/java/commun/` : Contient les configurations partagées (port, commandes, etc.).
- `build.bat` : Script d'automatisation pour la compilation.

## Prérequis

- [Java JDK](https://www.oracle.com/java/technologies/downloads/) installé et configuré dans votre PATH.

## Utilisation du Script de Build (`build.bat`)

Le fichier `build.bat` à la racine du projet permet de nettoyer les anciennes compilations et de compiler tout le projet d'un coup.

### Compilation

Ouvrez un terminal à la racine du projet et lancez :
```cmd
.\build.bat
```

Ce script va :
1. Nettoyer les fichiers `.class` existants.
2. Compiler toutes les classes dans `src/main/java`.
3. Afficher les commandes pour lancer le serveur et le client.

### Option GUI (Interface Graphique) :
- **Serveur** : `java serveur.ServeurGUI`
- **Client** : `java client.ClientGUI`

## Commandes disponibles
- Vous pouvez taper n'importe quelle commande système (ex: `dir`, `echo hello`, `ls`).
- Pour quitter proprement (console), tapez : `exit`
- En mode GUI, vous pouvez simplement fermer la fenêtre ou cliquer sur "Arrêter".

