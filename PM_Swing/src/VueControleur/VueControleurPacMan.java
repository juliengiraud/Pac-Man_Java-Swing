package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modele.Bonus;
import modele.Direction;
import modele.Entite;
import modele.Fantome;
import modele.Jeu;
import modele.Pacman;
import modele.Mur;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer/*, ActionListener*/ {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int size;

    private final ImageIcon[] icoPacMan = new ImageIcon[8]; // icones affichées dans la grille
    private final ImageIcon[] icoFantome = new ImageIcon[3];
    private final ImageIcon[] icoMur= new ImageIcon[13];
    private final ImageIcon[] icoCoin = new ImageIcon[6];
    private  ImageIcon icoBonus;
    private ImageIcon icoCouloir;
    
    private Entite[][] lastGrille = new Entite[jeu.SIZE][jeu.SIZE];
    private boolean startgood = false;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)


    public VueControleurPacMan(int _size) {

        size = _size;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        ajouterEcouteurClavier();

    }

    private void ajouterEcouteurClavier() {

        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                
                switch(e.getKeyCode()) {  // on écoute les flèches de direction du clavier
                    
                    case KeyEvent.VK_LEFT:
                        jeu.getPacman().setDirection(Direction.gauche);
                        break;
                        
                    case KeyEvent.VK_RIGHT:
                        jeu.getPacman().setDirection(Direction.droite);
                        break;
                        
                    case KeyEvent.VK_DOWN:
                        jeu.getPacman().setDirection(Direction.bas);
                        break;
                        
                    case KeyEvent.VK_UP:
                        jeu.getPacman().setDirection(Direction.haut);
                        break;
                }
                
            }

        });

    }

    public void setJeu(Jeu _jeu) {
        jeu = _jeu;
    }

    private void chargerLesIcones() {
        
        icoPacMan[0]= chargerIcone("Images/Up.png");
        icoPacMan[1]= chargerIcone("Images/Down.png");
        icoPacMan[2]= chargerIcone("Images/Left.png");
        icoPacMan[3]= chargerIcone("Images/Right.png");
        icoPacMan[4]= chargerIcone("Images/BigUp.png");
        icoPacMan[5]= chargerIcone("Images/BigDown.png");
        icoPacMan[6]= chargerIcone("Images/BigLeft.png");
        icoPacMan[7]= chargerIcone("Images/BigRight.png");
        
        icoCouloir = chargerIcone("Images/Couloir.png");
        icoBonus = chargerIcone("Images/bonus.png");
        
        //Mur
        icoMur[0] = chargerIcone("Images/wall.png");
        icoMur[1] = chargerIcone("Images/debD.png");
        icoMur[2] = chargerIcone("Images/milD.png");
        icoMur[3] = chargerIcone("Images/finD.png");
        icoMur[4] = chargerIcone("Images/debutUp.png");
        icoMur[5] = chargerIcone("Images/milUp.png");
        icoMur[6] = chargerIcone("Images/finUp.png");
        icoMur[7] = chargerIcone("Images/debG.png");
        icoMur[8] = chargerIcone("Images/upG.png");
        icoMur[9] = chargerIcone("Images/finG.png");
        icoMur[10] = chargerIcone("Images/startD.png");
        icoMur[11] = chargerIcone("Images/upD.png");
        icoMur[12] = chargerIcone("Images/endD.png");
        
        //Coin Mur
        icoCoin[0] = chargerIcone("Images/coin1.png");
        icoCoin[1] = chargerIcone("Images/coin2.png");
        icoCoin[2] = chargerIcone("Images/coin3.png");
        icoCoin[3] = chargerIcone("Images/coin4.png");
        icoCoin[4] = chargerIcone("Images/coinUp.png");
        icoCoin[5] = chargerIcone("Images/coinG.png");
        
        //Fantome 
        icoFantome[0] = chargerIcone("Images/FonRedRight.png");
        icoFantome[1]= chargerIcone("Images/FonPinkRight.png");
        icoFantome[2]= chargerIcone("Images/FonBleuRight.png");
        
    }

    private ImageIcon chargerIcone(String urlIcone) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPacMan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {

        setTitle("PacMan");
        setSize(18*size, 21*size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(size, size)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[size][size];
        

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                JLabel jlab = new JLabel();
                tabJLabel[y][x] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
                
            }

        }

        add(grilleJLabels);

    }
    
    private void initialiserCoin(int x, int y, int icone)
    {
        tabJLabel[x][y].setIcon(icoCoin[icone]);     
    }
    
    private void initialiserMur(int x, int y, int icone)
    {
        tabJLabel[x][y].setIcon(icoMur[icone]); 
    }
    
    private void creeMap(){
           
        //Initialisation des 4 coins 
        initialiserCoin(0,0,0);
        initialiserCoin(size-1,0,1);
        initialiserCoin(0,size-1,2);
        initialiserCoin(size-1,size-1,3);
        
        
        //initialiser les cotés
        for (int x = 1; x < size-1; x++)  initialiserMur(x,0,5);
        for (int x = 1; x < size-1; x++)  initialiserMur(x,size-1,5);
                                                            //pour avoir un passage
        for (int y = 1; y < size-1; y++) if(y!= 13 && y!= 10 && y!=11 && y!=12) initialiserMur(size-1,y,2);
        for (int y = 1; y < size-1; y++) if(y!= 13 && y!= 10 && y!=11 && y!=12) initialiserMur(0,y,2);
        
        //ajout des murs comme opstacle a la map
            //passage Droite
        initialiserMur(size-1,10,3);
        initialiserMur(size-1,13,1);
                
            //passage Gauche
        initialiserMur(0,10,3);
        initialiserMur(0,13,1);
        
        //Mur Milieu
        initialiserMur(13,11,1);
        initialiserMur(13,12,2);
        initialiserMur(9,11,1);
        initialiserMur(9,12,2);
        initialiserCoin(9,13,2);
        initialiserMur(10,13,5);
        initialiserMur(12,13,5);
        initialiserMur(11,13,5);
        initialiserCoin(13,13,3);
        
        
        //Mur du bas 
        initialiserMur(3,13,1);
        initialiserMur(3,14,2);
        initialiserMur(3,15,2);
        initialiserMur(3,16,3);
        
        //obstacle du bas
        initialiserMur(2,18,4);
        initialiserMur(3,18,5);
        initialiserMur(4,18,5);
        initialiserCoin(5,18,3);
        initialiserMur(5,17,2);
        initialiserMur(5,16,2);
        initialiserCoin(5,15,0);
        initialiserMur(6,15,5);
        initialiserMur(7,14,2);
        initialiserCoin(7,15,4);
        initialiserMur(7,13,1);//a changer debut de la fin
        initialiserMur(8,15,5);
        initialiserMur(9,15,5);
        initialiserMur(10,15,6);
        
        //Petit UniMur
        initialiserMur(7,17,0);
        initialiserMur(2,22,0);
        initialiserMur(12,15,0);
        initialiserMur(22,22,0);
        initialiserMur(22,16,0);
        initialiserMur(7,11,0);
        
        //obstacle bas gauche
        initialiserMur(2,20,4);
        initialiserMur(3,20,5);
        initialiserCoin(4,20,1);
        initialiserMur(4,21,2);
        initialiserMur(4,22,3);
        
        //obstacle milieu gauche 
        initialiserMur(9,17,1);
        initialiserMur(9,18,2);
        initialiserMur(9,19,2);
        initialiserCoin(9,20,2);
        initialiserMur(10,20,5);
        initialiserMur(11,20,5);
        initialiserMur(12,20,5);
        initialiserCoin(13,20,3);//coin mont
        initialiserMur(13,18,2);
        initialiserMur(13,19,2); //fin monte
        initialiserMur(13,17,1);
            //coin milieu 2 cases
        initialiserMur(11,17,1);
        initialiserMur(11,18,3);
        
        initialiserMur(7,19,1);
        initialiserMur(7,20,2);
        initialiserCoin(7,21,5);//TODO: coin 6
        initialiserMur(6,21,4);
        initialiserCoin(7,22,2);
        initialiserMur(8,22,5);
        initialiserMur(9,22,5);
        initialiserMur(10,22,5);
        initialiserMur(11,22,5);
        initialiserMur(12,22,6);
        
        //long mur 1
        initialiserMur(15,15,3); //fin 
        initialiserMur(15,14,2);
        initialiserMur(15,13,2);
        initialiserMur(15,12,2);
        initialiserMur(15,11,2);
        initialiserMur(15,10,2);
        initialiserMur(15,9,1); //debut
        
        //long mur 2
        initialiserMur(15,17,1);  
        initialiserMur(15,18,2);
        initialiserMur(15,19,2);
        initialiserMur(15,20,2);
        initialiserMur(15,21,2);
        initialiserMur(15,22,3);
        
        //fond droit
        initialiserMur(20,22,3); //fin
        initialiserMur(20,21,2);
        initialiserCoin(20,20,0); //Coin 0
        initialiserMur(21,20,5);
        initialiserMur(22,20,6); //fin mkesel
        
        
        //fond millieu droit
        initialiserMur(18,22,6); //fin mksel
        initialiserCoin(17,22,2); //coin 2
        initialiserMur(17,21,2);
        initialiserCoin(17,20,0); 
        initialiserMur(18,20,6); 
        
        //long mur mkesel
        initialiserMur(17,18,4);
        initialiserMur(18,18,5);
        initialiserMur(19,18,5);
        initialiserMur(20,18,5);
        initialiserMur(21,18,5);
        initialiserMur(22,18,6);
        
        //droit millieu
        initialiserMur(20,16,3);
        initialiserMur(20,15,2);
        initialiserCoin(20,14,0);//coin 0
        initialiserMur(21,14,5);
        initialiserMur(22,14,6); 
        
        //rectangle
         //1
        initialiserMur(17,16,9); //end
        initialiserMur(18,16,12);
        initialiserMur(17,15,8); //mill
        initialiserMur(18,15,11);
        initialiserMur(17,14,7); //deb 
        initialiserMur(18,14,10);
          //2
        initialiserMur(2,11,9);//fin rec
        initialiserMur(3,11,12);
        initialiserMur(2,10,8);
        initialiserMur(3,10,11);
        initialiserMur(2,9,7);
        initialiserMur(3,9,10);
             //3
        initialiserMur(15,2,10);
        initialiserMur(15,7,12);
        for(int i=3; i<7;i++)
            initialiserMur(15,i,11);
        initialiserMur(14,2,7);
        initialiserMur(14,7,9);
        for(int i=3; i<7;i++)
            initialiserMur(14,i,8);
        
        //caree
            //1
        initialiserMur(17,12,9); //mill
        initialiserMur(18,12,12);
        initialiserMur(17,11,7); //deb 
        initialiserMur(18,11,10);
            //2
        initialiserMur(17,9,9); //mill
        initialiserMur(18,9,12);
        initialiserMur(17,8,7); //deb 
        initialiserMur(18,8,10);
            //3
        initialiserMur(22,3,12); //mill
        initialiserMur(21,3,9);
        initialiserMur(22,2,10); //deb 
        initialiserMur(21,2,7);
        
        //long mur
            //1er
        initialiserMur(20,12,3);
        initialiserMur(20,7,1);
        for(int i=11; i>7;i--)
            initialiserMur(20,i,2);
            //2eme
        initialiserMur(22,12,3);
        initialiserMur(22,7,1);
        for(int i=11; i>7;i--)
            initialiserMur(22,i,2);
            //3eme
        initialiserMur(5,9,1);
        initialiserMur(5,13,3);
        for(int i=10; i<13;i++)
            initialiserMur(5,i,2);
             //4eme
        initialiserMur(17,2,1);
        initialiserMur(17,6,3);
        for(int i=3; i<6;i++)
            initialiserMur(17,i,2);
              //5eme
        initialiserMur(12,2,1);
        initialiserMur(12,3,2);
        initialiserMur(12,4,3); 
              //6eme
        initialiserMur(2,2,1);
        initialiserMur(2,3,3);
        
         //Un J pour Julien
        initialiserMur(4,2,1);
        for(int i=3; i<7;i++)
            initialiserMur(4,i,2);
        initialiserCoin(4,7,3);//coin 3
        initialiserMur(3,7,5);
        initialiserCoin(2,7,2);//coin 2
        initialiserMur(2,5,1);
        initialiserMur(2,6,2);
        
         //Un M pour Meriem
        initialiserMur(6,2,1);
        initialiserMur(6,7,3);
        for(int i=3; i<7;i++)
            initialiserMur(6,i,2); 
        initialiserMur(7,3,0);
        initialiserMur(9,3,0);
        initialiserMur(8,4,0);
        initialiserMur(10,2,1);
        initialiserMur(10,7,3);
        for(int i=3; i<7;i++)
            initialiserMur(10,i,2);
        
        //obstacle bizarre
        
        initialiserMur(12,8,2);
        initialiserMur(8,8,2);
        initialiserMur(12,7,2);
        initialiserMur(8,7,2);
        initialiserMur(12,6,1);
        initialiserMur(8,6,1);
        
        initialiserMur(7,9,4);
        initialiserMur(13,9,6);
        initialiserCoin(12,9,4);
        initialiserCoin(8,9,4);
        for(int i=9; i<12;i++)
            initialiserMur(i,9,5);
        
        //droit up
        initialiserMur(19,2,1);
        initialiserMur(19,3,2);
        initialiserMur(19,4,2);
        initialiserCoin(19,5,2);
        initialiserMur(20,5,5);
        initialiserMur(21,5,5);
        initialiserMur(22,5,6);
               
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        
        if (!startgood)
            creeMap();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!startgood || lastGrille[x][y] != jeu.getGrille()[x][y]) {
                    System.out.println("Changement en " + x + ", " + y);
                    if (jeu.getGrille()[x][y] instanceof Bonus) {
                        tabJLabel[x][y].setIcon(icoBonus);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Pacman) {
                        tabJLabel[x][y].setIcon(icoPacMan[((Pacman)jeu.getGrille()[x][y]).getState()]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Fantome) {
                        tabJLabel[x][y].setIcon(icoFantome[0]);
                    }
                    else if (!(jeu.getGrille()[x][y] instanceof Mur)) {
                        tabJLabel[x][y].setIcon(icoCouloir);
                    }
                }
                lastGrille[x][y] = jeu.getGrille()[x][y];
            }
        }
        startgood = true;
        System.out.println("\n");
    }

    @Override
    public void update(Observable o, Object arg) {
        
        mettreAJourAffichage();
        
        
        
        /*SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                });*/
       
        
    }

}
