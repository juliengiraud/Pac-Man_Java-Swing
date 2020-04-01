/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** La classe Jeu a deux fonctions 
 *  (1) Gérer les aspects du jeu : condition de défaite, victoire, nombre de vies
 *  (2) Gérer les coordonnées des entités du monde : déplacements, collisions, perception des entités, ... 
 *
 * @author freder
 */
public class Jeu extends Observable implements Runnable {

    //public static final int SIZE_X = 26;
    //public static final int SIZE_Y = 29;
    public static final int SIZE = 25;

    private Point point_;
    private Pacman pm;
    private Bonus bonus;
    private Fantome[] fantome = new Fantome[4];
    private Mur mur;

    private Entite[][] grilleEntites = new Entite[SIZE][SIZE]; // permet de récupérer une entité à partir de ses coordonnées
    private ArrayList<Entite> entites = new ArrayList<>();
    
    // TODO : ajouter les murs, couloir, PacGums, et adapter l'ensemble des fonctions (prévoir le raffraichissement également du côté de la vue)
    
    
    public Jeu() {
        initialisationDesEntites();
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Pacman getPacman() {
        return pm;
    }
    
    public Mur getMur() {
        return mur;
    }
    
    public Bonus getBonus() {
        return bonus;
    }
    
    private void initialisationDesEntites() {
        
        pm = new Pacman(this, new Point(0, 12));
        grilleEntites[0][12] = pm;
        entites.add(pm);
        
        fantome[0] = new Fantome(this, new Point(12, 12));
        grilleEntites[12][12] = fantome[0];
        entites.add(fantome[0]);
        
        bonus = new Bonus(this, new Point(5, 5), 200);
        grilleEntites[5][5]= bonus;
        entites.add(bonus);
        
        initialisationMap();
        
    }
    
    private void initialisationMap(){
        //Initialisation des 4 coins
        point_ = new Point(0,0);
        mur = new Mur(this,point_);
        
        
        initialiserMur(0,0);
        initialiserMur(SIZE-1,0);
        initialiserMur(0,SIZE-1);
        initialiserMur(SIZE-1,SIZE-1);
        
        
        //initialiser les cotés
        for (int x = 1; x < SIZE-1; x++)  initialiserMur(x,0);
        for (int y = 1; y < SIZE-1; y++) if(y!=11 && y!=12)  initialiserMur(0,y);
        for (int x = 1; x < SIZE-1; x++)  initialiserMur(x,SIZE-1);
        for (int y = 1; y < SIZE-1; y++)  if(y!=11 && y!=12) initialiserMur(SIZE-1,y);

        //ajout des murs comme opstacle a la map
            //Mur millieu
        initialiserMur(13,11);
        initialiserMur(13,12);
        initialiserMur(9,11);
        initialiserMur(9,12);
        initialiserMur(9,13);
        initialiserMur(10,13);
        initialiserMur(11,13);
        initialiserMur(12,13);
        initialiserMur(13,13);
        
        
            //mur entree bas
        initialiserMur(3,13);
        initialiserMur(3,14);
        initialiserMur(3,15);
        initialiserMur(3,16);
        
            //obstacle 1
        initialiserMur(2,18);
        initialiserMur(3,18);
        initialiserMur(4,18);
        initialiserMur(5,18);
        initialiserMur(5,17);
        initialiserMur(5,16);
        initialiserMur(5,15);
        initialiserMur(6,15);
        initialiserMur(7,15);
        initialiserMur(7,14);
        initialiserMur(7,13);
        initialiserMur(8,15);
        initialiserMur(9,15);
        initialiserMur(10,15);
        
            //Petit uniMur
        initialiserMur(7,17); //faut changer l'icone 
        initialiserMur(2,22);
        initialiserMur(12,15);
        initialiserMur(22,22);
        initialiserMur(22,16);
        initialiserMur(7,11);
        
        //obstacle bas gauche
        initialiserMur(2,20);
        initialiserMur(3,20);
        initialiserMur(4,20);
        initialiserMur(4,21);
        initialiserMur(4,22);
        
        //obstacle milieu gauche
        initialiserMur(9,17); //debut down
        initialiserMur(9,18);
        initialiserMur(9,19);
        initialiserMur(9,20);
        initialiserMur(10,20);
        initialiserMur(11,20);
        initialiserMur(12,20);
        initialiserMur(13,20);//coin mont
        initialiserMur(13,18);
        initialiserMur(13,19); //fin monte
        initialiserMur(13,17);
            //coin milieu 2 cases
        initialiserMur(11,17);
        initialiserMur(11,18);
        
        //millieu bas gauche
        initialiserMur(7,19);
        initialiserMur(7,20);
        initialiserMur(7,21);//TODO: coin 6
        initialiserMur(6,21);//fin 3
        initialiserMur(7,22);//COIN 2
        initialiserMur(8,22);
        initialiserMur(9,22);
        initialiserMur(10,22);
        initialiserMur(11,22);
        initialiserMur(12,22);
        
        //long mur 1
        initialiserMur(15,15); //fin 
        initialiserMur(15,14);
        initialiserMur(15,13);
        initialiserMur(15,12);
        initialiserMur(15,11);
        initialiserMur(15,10);
        initialiserMur(15,9); //debut
        
        //long mur 2
        initialiserMur(15,17); //fin 
        initialiserMur(15,18);
        initialiserMur(15,19);
        initialiserMur(15,20);
        initialiserMur(15,21);
        initialiserMur(15,22);//debut
        
        //fond droit
        initialiserMur(20,22); //fin
        initialiserMur(20,21);
        initialiserMur(20,20); //Coin 0
        initialiserMur(21,20);
        initialiserMur(22,20); //fin mkesel
        
        //fond millieu droit
        initialiserMur(18,22); //fin mksel
        initialiserMur(17,22); //coin 2
        initialiserMur(17,21);
        initialiserMur(17,20); //coin 0
        initialiserMur(18,20); //fin mkesel
        
        //long mur mkesel
        initialiserMur(17,18);
        initialiserMur(18,18);
        initialiserMur(19,18);
        initialiserMur(20,18);
        initialiserMur(21,18);
        initialiserMur(22,18);
        
        //droit millieu
        initialiserMur(20,16);
        initialiserMur(20,15);
        initialiserMur(20,14);//coin 0
        initialiserMur(21,14);
        initialiserMur(22,14);
        
        //rectangle
            //1
        initialiserMur(17,16); //end
        initialiserMur(18,16);
        initialiserMur(17,15); //mill
        initialiserMur(18,15);
        initialiserMur(17,14); //deb 
        initialiserMur(18,14);
            //2
        initialiserMur(2,11);//fin rec
        initialiserMur(3,11);
        initialiserMur(2,10);
        initialiserMur(3,10);
        initialiserMur(2,9);
        initialiserMur(3,9);
            //3
        for(int i=2; i<8;i++)
            initialiserMur(15,i);
        for(int i=2; i<8;i++)
            initialiserMur(14,i);
        
        //carre
            //1
        initialiserMur(17,12); //mill
        initialiserMur(18,12);
        initialiserMur(17,11); //deb 
        initialiserMur(18,11);
            //2
        initialiserMur(17,9); //mill
        initialiserMur(18,9);
        initialiserMur(17,8); //deb 
        initialiserMur(18,8);
            //3
        initialiserMur(22,3); //mill
        initialiserMur(21,3);
        initialiserMur(22,2); //deb 
        initialiserMur(21,2);
        
        //long mur
            //1er
        for(int i=12; i>6;i--)
            initialiserMur(20,i);
            //2eme
        for(int i=12; i>6;i--)
            initialiserMur(22,i);
          //3eme
        for(int i=9; i<14;i++)
            initialiserMur(5,i);
            //4eme
        for(int i=2; i<7;i++)
            initialiserMur(17,i);
            //5eme
        for(int i=2; i<5;i++)
            initialiserMur(12,i);
            //6eme
        for(int i=2; i<4;i++)
            initialiserMur(2,i);
        
        
        //Un J pour Julien
        for(int i=2; i<7;i++)
            initialiserMur(4,i);
        initialiserMur(4,7);//coin 3
        initialiserMur(3,7);
        initialiserMur(2,7);//coin 2
        for(int i=6; i>4;i--)
            initialiserMur(2,i);
        
        //Un M pour Meriem
        for(int i=2; i<8;i++)
            initialiserMur(6,i);
        initialiserMur(7,3);
        initialiserMur(9,3);
        initialiserMur(8,4);
        for(int i=2; i<8;i++)
            initialiserMur(10,i);
        
        //obstacle bizarre
        for(int i=7; i<14;i++)
            initialiserMur(i,9);
        for(int i=6; i<10;i++)
            initialiserMur(8,i);
        for(int i=6; i<10;i++)
            initialiserMur(12,i);
        
        //obstacle droit up
        for(int i=2; i<6;i++)
            initialiserMur(19,i);
        for(int i=19; i<23;i++)
            initialiserMur(i,5);
        
    }
    
    private void initialiserMur(int x,int y)
    {
        point_.setLocation(x,y);
        mur.setMur(point_);
        grilleEntites[x][y]= mur;
        
        // pas possible mur 
       // entites.add(mur);
       // a savoir pourquoi ? 
    }
    
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = e.coo;
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déclacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        
        boolean retour;
        
        Point pCourant = e.coo;
        Point pCible = calculerPointCible(pCourant, d);
        
        if (!contenuDansGrille(pCible)) {
            return false;
        }
        
        if (objetALaPosition(pCible) == null) { // Si la case est vide on a rien à faire
            deplacerEntite(pCourant, pCible, e);
            return true;
        }
        
        Entite new_e = gestionCollision(e, objetALaPosition(pCible));
        if (new_e != null) {
            deplacerEntite(pCourant, pCible, new_e);
            return true;
        }
        
        return false;
    }
    
    private Entite gestionCollision(Entite src, Entite dest) { // Gère le trantement lorsque deux entités se supperposent
        
        if (dest == bonus) { // On va arriver aux coordonnées du bonus
            if (src == pm) { // Si on est le joueur
                pm.augmenterScore(bonus.getScore()); // Gestion "pacman mange le bonus"
                bonus.reinitialiser();
                return pm;
            }
            else { // Un fantome arrive sur le bonus
                return dest; // Gestion "fantome sur le bonus"
            }
        }
        return null;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        switch(d) {
            case haut: pCible = new Point(pCourant.x, (pCourant.y + SIZE - 1) % SIZE); break;
            case bas: pCible = new Point(pCourant.x, (pCourant.y + 1) % SIZE); break;
            case gauche: pCible = new Point((pCourant.x + SIZE - 1) % SIZE, pCourant.y); break;
            case droite: pCible = new Point((pCourant.x + 1) % SIZE, pCourant.y); break;
            case neutre: pCible = pCourant; break;
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        e.coo = pCible;
    }
    
    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        boolean sortie = p.x >= 0 && p.x < SIZE && p.y >= 0 && p.y < SIZE;
        if (!sortie)
            System.out.println("Erreur grille : " + p.toString());
        return sortie;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }
    
    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {

        new Thread(this).start();

    }

    @Override
    public void run() {
        
        while (true) {
            for (Entite e : entites) {
                e.run();
            }

            setChanged();
            notifyObservers(); // notification de l'observer pour le raffraichisssement graphique
            
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
