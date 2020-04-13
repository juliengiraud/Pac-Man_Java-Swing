package VueControleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modele.Bonus;
import modele.Boule;
import modele.Direction;
import modele.Entite;
import modele.Fantome;
import modele.Jeu;
import modele.Pacman;
import modele.Mur;
import modele.SuperBoule;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer {

    // En rapport avec la classe Jeu
    private Jeu jeu;
    private final int size;
    private boolean startgood; // permet de savoir si le jeu est en cours
    
    // En rapport avec l'affichage du jeu
    private final Entite[][] lastGrille; // Pour optimiser le nombre de mises à jour d'affichage
    private JLabel[][] tabJLabel; // Cases à afficher pour représenter le jeu
    JPanel jeu_;
    JLabel score;
    JLabel niveau;
    JLabel vies;

    // En rapport avec les icones
    private final ImageIcon[] icoPacMan; // icones affichées dans la grille
    private final ImageIcon[] icoFantome;
    private final ImageIcon[] icoMur;
    private final ImageIcon[] icoFood;
    private final ImageIcon[] icoBonus;
    private ImageIcon icoCouloir;
    

    // En rapport avec le menu
    JPanel menu;


    public VueControleurPacMan() {

        size = Jeu.SIZE;
        startgood = false;

        lastGrille = new Entite[size][size];

        icoPacMan = new ImageIcon[8];
        icoFantome = new ImageIcon[14];
        icoMur = new ImageIcon[19];
        icoFood = new ImageIcon[2];
        icoBonus = new ImageIcon[3];

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();

        setVisible(true);

    }

    private void ajouterEcouteurClavier() {

        setFocusable(true);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                
                if (startgood) {
                    switch(e.getKeyCode()) {  // on écoute les flèches de direction du clavier

                        case KeyEvent.VK_LEFT:
                            jeu.getPacman().setFutureDirection(Direction.gauche);
                            break;

                        case KeyEvent.VK_RIGHT:
                            jeu.getPacman().setFutureDirection(Direction.droite);
                            break;

                        case KeyEvent.VK_DOWN:
                            jeu.getPacman().setFutureDirection(Direction.bas);
                            break;

                        case KeyEvent.VK_UP:
                            jeu.getPacman().setFutureDirection(Direction.haut);
                            break;
                    }
                }
            }
        });
    }

    public void chargerJeu() {
        jeu = new Jeu();
        jeu.addObserver(this);
    }

    private void chargerLesIcones() {
        
        icoPacMan[0] = chargerIcone("Images/Up.png");
        icoPacMan[1] = chargerIcone("Images/Down.png");
        icoPacMan[2] = chargerIcone("Images/Left.png");
        icoPacMan[3] = chargerIcone("Images/Right.png");
        icoPacMan[4] = chargerIcone("Images/BigUp.png");
        icoPacMan[5] = chargerIcone("Images/BigDown.png");
        icoPacMan[6] = chargerIcone("Images/BigLeft.png");
        icoPacMan[7] = chargerIcone("Images/BigRight.png");

        icoCouloir = chargerIcone("Images/Couloir.png");

        icoBonus[0] = chargerIcone("Images/bonus1.png");
        icoBonus[1] = chargerIcone("Images/bonus2.png");
        icoBonus[2] = chargerIcone("Images/bonus3.png");

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
        icoFantome[0] = chargerIcone("Images/FonBleuDown.png");
        icoFantome[1] = chargerIcone("Images/FonBleuLeft.png");
        icoFantome[2] = chargerIcone("Images/FonBleuRight.png");
        icoFantome[3] = chargerIcone("Images/FonBleuUP.png");
        icoFantome[4] = chargerIcone("Images/FonRedDown.png");
        icoFantome[5] = chargerIcone("Images/FonRedLeft.png");
        icoFantome[6] = chargerIcone("Images/FonRedRight.png");
        icoFantome[7] = chargerIcone("Images/FonRedUp.png");
        icoFantome[8] = chargerIcone("Images/FonPinkDown.png");
        icoFantome[9] = chargerIcone("Images/FonPinkLeft.png");
        icoFantome[10] = chargerIcone("Images/FonPinkRight.png");
        icoFantome[11] = chargerIcone("Images/FonPinkUp.png");
        icoFantome[12] = chargerIcone("Images/End.png");
        icoFantome[13] = chargerIcone("Images/eaten.png");
        
        // Food
        icoFood[0] = chargerIcone("Images/food.png");
        icoFood[1] = chargerIcone("Images/SuperFood.png");

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

        // Partie fenêtre
        setTitle("PacMan");
        setSize(18*size, 21*size+30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Partie jeu -> map
        jeu_ = new JPanel();
        JPanel grilleJLabels = new JPanel(new GridLayout(size, size));
        grilleJLabels.setPreferredSize(new Dimension(this.getWidth()+1, 19*size));
        grilleJLabels.setBackground(Color.white);
        tabJLabel = new JLabel[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JLabel jlab = new JLabel();
                tabJLabel[y][x] = jlab;
                grilleJLabels.add(jlab);
            }
        }
        jeu_.add(grilleJLabels);
        
        // Partie jeu -> barre d'infos
        JPanel jp_barre_jeu = new JPanel(new BorderLayout());
        jp_barre_jeu.setPreferredSize(new Dimension(this.getWidth()-50, 32));
        score = new JLabel();
        score.setFont(score.getFont().deriveFont(20.0f));
        niveau = new JLabel();
        niveau.setFont(niveau.getFont().deriveFont(20.0f));
        niveau.setHorizontalAlignment(JLabel.CENTER);
        vies = new JLabel();
        vies.setFont(vies.getFont().deriveFont(20.0f));
        jp_barre_jeu.add(score, BorderLayout.WEST);
        jp_barre_jeu.add(niveau, BorderLayout.CENTER);
        jp_barre_jeu.add(vies, BorderLayout.EAST);
        jeu_.add(jp_barre_jeu);

        // Partie menu -> création des conteneurs
        JPanel jp_image_menu = new JPanel(); // Image du haut/milieu
        JPanel jp_lancer_jeu = new JPanel(); // Bouton "Lancer le jeu"
        JPanel jp_afficher_score = new JPanel(); // Bouton "Afficher les scores"
        menu = new JPanel(); // Tous les éléments

        // Partie menu -> espace du milieu
        JLabel jl_fond = new JLabel();
        jl_fond.setIcon(chargerIcone("Images/accueil.png"));
        jp_image_menu.setPreferredSize(new Dimension(this.getWidth()+1, 300));
        jp_image_menu.add(jl_fond);
        jp_image_menu.setBackground(Color.black);

        // Partie menu -> lancer le jeu
        JButton jb_lancer_jeu = new JButton("Lancer le jeu");
        jp_lancer_jeu.setMaximumSize(new Dimension(this.getWidth()+1, 0));
        jp_lancer_jeu.setPreferredSize(new Dimension(this.getWidth()+1, 42));
        jp_lancer_jeu.add(jb_lancer_jeu);
        jp_lancer_jeu.setBackground(Color.black);
        jb_lancer_jeu.addActionListener((ActionEvent ae) -> {
            lancer_jeu();
        });

        // Partie menu -> afficher les scores
        JButton jb_afficher_score = new JButton("Afficher les scores");
        jp_afficher_score.add(jb_afficher_score);
        jp_afficher_score.setBackground(Color.black);
        jb_afficher_score.addActionListener((ActionEvent ae) -> {
            afficher_scores();
        });

        // Partie menu -> ajout des conteneurs au menu
        BoxLayout bl_menu = new BoxLayout(menu, BoxLayout.Y_AXIS);
        menu.setLayout(bl_menu);
        menu.setAlignmentX(JComponent.BOTTOM_ALIGNMENT);
        menu.setBackground(Color.black);
        menu.add(jp_image_menu);
        menu.add(jp_lancer_jeu);
        menu.add(jp_afficher_score);

        // Ajout du menu à la vue
        add(menu);

    }

    private void mise_a_jour_barre_jeu() {
        score.setText("Score : " + jeu.getPacman().getScore());
        niveau.setText("Niveau : " + jeu.getNiveau() + "/3");
        vies.setText("Vies : " + jeu.getPacman().getVies());
    }

    private void lancer_jeu() {
        System.out.println("Lancer le jeu");

        menu.setVisible(false);
        remove(menu);
        add(jeu_);
        jeu_.setVisible(true);
        
        chargerJeu();
        jeu.start();

    }

    private void retour_menu() {
        System.out.println("Retourner au menu");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
        }

        jeu_.setVisible(false);
        remove(jeu_);
        add(menu);
        menu.setVisible(true);
        

    }

    private void afficher_scores() {
        System.out.println("Afficher le tableau des scores");
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!startgood || lastGrille[x][y] != jeu.getGrille()[x][y] || jeu.getGrille()[x][y] instanceof Fantome) {
                    //System.out.println("Changement en " + x + ", " + y);
                    if (jeu.getGrille()[x][y] instanceof Bonus) {
                        tabJLabel[x][y].setIcon(icoBonus[jeu.getNiveau()-1]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Pacman) {
                        tabJLabel[x][y].setIcon(
                            icoPacMan[ ((Pacman) jeu.getGrille()[x][y]).getState() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof Fantome) {
                        tabJLabel[x][y].setIcon(
                            icoFantome[ ((Fantome) jeu.getGrille()[x][y]).getState() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof Mur) {
                        tabJLabel[x][y].setIcon(
                            icoMur[ ((Mur) jeu.getGrille()[x][y]).getTypeMur() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof SuperBoule) {
                        tabJLabel[x][y].setIcon(icoFood[1]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Boule) {
                        tabJLabel[x][y].setIcon(icoFood[0]);
                    }
                    else {
                        tabJLabel[x][y].setIcon(icoCouloir);
                    }
                }
                lastGrille[x][y] = jeu.getGrille()[x][y];
            }
        }
        startgood = true;
        //System.out.println("\n");
        mise_a_jour_barre_jeu();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (jeu.getPartieOn())
            mettreAJourAffichage();
        else
            retour_menu();
    }
}
