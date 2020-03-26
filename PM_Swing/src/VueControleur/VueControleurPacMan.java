package VueControleur;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import modele.Bonus;
import modele.Direction;
import modele.Fantome;
import modele.Jeu;
import modele.Pacman;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer/*, ActionListener*/ {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    //private int sizeX; // taille de la grille affichée
    //private int sizeY;
    private int size;

    private ImageIcon icoPacMan; // icones affichées dans la grille
    private ImageIcon icoFantome;
    private ImageIcon icoCouloir;
    private ImageIcon icoBonus;

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
                    case KeyEvent.VK_LEFT : jeu.getPacman().setDirection(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.getPacman().setDirection(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.getPacman().setDirection(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.getPacman().setDirection(Direction.haut); break;
                }
                
            }

        });

    }

    public void setJeu(Jeu _jeu) {
        jeu = _jeu;
    }

    private void chargerLesIcones() {
        icoPacMan = chargerIcone("Images/Pacman.png");
        icoCouloir = chargerIcone("Images/Couloir.png");
        icoFantome = chargerIcone("Images/Fantome.png");
        icoBonus = chargerIcone("Images/bonus1.png");
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
        setSize(25*size, 25*size);
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
                if (jeu.getGrille()[x][y] instanceof Bonus) {
                    tabJLabel[x][y].setIcon(icoBonus);
                }
                else if (jeu.getGrille()[x][y] instanceof Pacman) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                    tabJLabel[x][y].setIcon(icoPacMan);
                }
                else if (jeu.getGrille()[x][y] instanceof Fantome) {
                    tabJLabel[x][y].setIcon(icoFantome);
                }
                else {
                    tabJLabel[x][y].setIcon(icoCouloir);
                }
            }
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        
        mettreAJourAffichage();
        
        
        
        /*SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); */
       
        
    }

}
