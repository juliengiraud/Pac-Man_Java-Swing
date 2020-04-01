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
    private final ImageIcon[] icoMur = new ImageIcon[19];
    private  ImageIcon icoBonus;
    private ImageIcon icoCouloir;
    
    private final Entite[][] lastGrille = new Entite[jeu.SIZE][jeu.SIZE];
    private boolean startgood = false;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)


    public VueControleurPacMan(int _size) {

        size = _size;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        ajouterEcouteurClavier();

    }

    private void ajouterEcouteurClavier() {

        addKeyListener(new KeyAdapter() {
            
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
        icoMur[13] = chargerIcone("Images/coin1.png"); // coin[0] -> mur[13]
        icoMur[14] = chargerIcone("Images/coin2.png"); // coin[1] -> mur[14]
        icoMur[15] = chargerIcone("Images/coin3.png"); // coin[2] -> mur[15]
        icoMur[16] = chargerIcone("Images/coin4.png"); // coin[3] -> mur[16]
        icoMur[17] = chargerIcone("Images/coinUp.png"); // coin[4] -> mur[17]
        icoMur[18] = chargerIcone("Images/coinG.png"); // coin[5] -> mur[18]

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
    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!startgood || lastGrille[x][y] != jeu.getGrille()[x][y]) {
                    System.out.println("Changement en " + x + ", " + y);
                    if (jeu.getGrille()[x][y] instanceof Bonus) {
                        tabJLabel[x][y].setIcon(icoBonus);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Pacman) {
                        tabJLabel[x][y].setIcon(
                            icoPacMan[ ((Pacman) jeu.getGrille()[x][y]).getState() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof Fantome) {
                        tabJLabel[x][y].setIcon(icoFantome[0]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Mur) {
                        tabJLabel[x][y].setIcon(
                            icoMur[ ((Mur) jeu.getGrille()[x][y]).getTypeMur() ]
                        );
                    }
                    else {
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
    }
}
