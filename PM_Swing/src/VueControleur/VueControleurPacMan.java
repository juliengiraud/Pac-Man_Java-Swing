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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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

@SuppressWarnings("serial")
public class VueControleurPacMan extends JFrame implements Observer {
    
    private final boolean debug;

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
    
    // En rapport avec le scoreboard
    JPanel scoreboard_;
    JPanel scoreboard_container;
    ArrayList<int[]> scoreboard;
    int[] scoreboard_tmp;

    // En rapport avec les icones
    private final ImageIcon[] icoPacMan; // icones affichées dans la grille
    private final ImageIcon[] icoFantome;
    private final ImageIcon[] icoMur;
    private final ImageIcon[] icoFood;
    private final ImageIcon[] icoBonus;
    private ImageIcon icoCouloir;

    // En rapport avec le menu
    JPanel menu;
     
    //Pour le Son  
    private AudioInputStream pacmanBoule;
    private Clip clipPacmanBoule;
    private FloatControl controlPacmanBoule;
    
    private AudioInputStream intro;
    private Clip clipIntro;
    private FloatControl controlIntro;
    
    private AudioInputStream superBoule;
    private Clip clipSuperBoule;
    private FloatControl controlSuperBoule;
    
    private AudioInputStream bonus;
    private Clip clipBonus;
    private FloatControl controlBonus;
    
    private AudioInputStream pacmanFantome;
    private Clip clipPacmanFantome;
    private FloatControl controlPacmanFantome;
    
    private AudioInputStream fantomePacman;
    private Clip clipFantomePacman;
    private FloatControl controlFantomePacman;
    
    private AudioInputStream fin;
    private Clip clipFin;
    private FloatControl controlFin;

    public VueControleurPacMan(boolean d) {
        
        debug = d;

        size = Jeu.SIZE;
        startgood = false; // Indique si la partie est lancée

        lastGrille = new Entite[size][size]; // Pour minimiser les mises à jour graphiques

        scoreboard = new ArrayList<>();
        scoreboard_tmp = new int[2];
        
        icoPacMan = new ImageIcon[8];
        icoFantome = new ImageIcon[15];
        icoMur = new ImageIcon[19];
        icoFood = new ImageIcon[2];
        icoBonus = new ImageIcon[3];

        chargerLesIcones();
        initialisationSon();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();

        setVisible(true);

    }

    private void ajouterEcouteurClavier() {

        setFocusable(true); // À cause des changements de focus
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
        jeu = new Jeu(debug);
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
        icoFantome[14] = chargerIcone("Images/corona.png");
        
        // Food
        icoFood[0] = chargerIcone("Images/food.png");
        icoFood[1] = chargerIcone("Images/SuperFood.png");

    }

    private ImageIcon chargerIcone(String urlIcone) {

        BufferedImage image;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPacMan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    private void creerFenetre() {
        setTitle("PacMan");
        setSize(18*size, 21*size+30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Pour la placer au centre
    }
    
    private void creerVueMap() {
        
        jeu_ = new JPanel(); // Conteneur de la grille du jeu et de la barre d'info
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
    }
    
    private void creerVueBarre() {
        
        JPanel barre = new JPanel(new BorderLayout()); // Barre d'info
        barre.setPreferredSize(new Dimension(this.getWidth()-50, 32)); // Largeur moins petit espace
        
        score = new JLabel();
        score.setFont(score.getFont().deriveFont(20.0f));
        niveau = new JLabel();
        niveau.setFont(niveau.getFont().deriveFont(20.0f));
        niveau.setHorizontalAlignment(JLabel.CENTER);
        vies = new JLabel();
        vies.setFont(vies.getFont().deriveFont(20.0f));
        
        barre.add(score, BorderLayout.WEST);
        barre.add(niveau, BorderLayout.CENTER);
        barre.add(vies, BorderLayout.EAST);
        jeu_.add(barre);
    }
    
    private void creerMenu() {
        
        // Création des conteneurs
        JPanel cimage_menu = new JPanel(); // Image du haut/milieu
        JPanel clancer_jeu = new JPanel(); // Bouton "Lancer le jeu"
        JPanel cafficher_score = new JPanel(); // Bouton "Afficher les scores"
        menu = new JPanel(); // Tous les éléments

        // Espace du milieu
        JLabel fond = new JLabel();
        fond.setIcon(chargerIcone("Images/accueil.png"));
        cimage_menu.setPreferredSize(new Dimension(this.getWidth()+1, 300));
        cimage_menu.add(fond);
        cimage_menu.setBackground(Color.black);

        // Lancer le jeu
        JButton lancer_jeu = new JButton("Lancer le jeu");
        clancer_jeu.setMaximumSize(new Dimension(this.getWidth()+1, 0));
        clancer_jeu.setPreferredSize(new Dimension(this.getWidth()+1, 42));
        clancer_jeu.add(lancer_jeu);
        clancer_jeu.setBackground(Color.black);
        lancer_jeu.addActionListener((ActionEvent ae) -> {
            lancer_jeu();
        });

        // Afficher les scores
        JButton afficher_score = new JButton("Afficher les scores");
        cafficher_score.add(afficher_score);
        cafficher_score.setBackground(Color.black);
        afficher_score.addActionListener((ActionEvent ae) -> {
            afficher_scores();
        });

        // Ajout des conteneurs au menu
        BoxLayout bmenu = new BoxLayout(menu, BoxLayout.Y_AXIS);
        menu.setLayout(bmenu);
        menu.setAlignmentX(JComponent.BOTTOM_ALIGNMENT);
        menu.setBackground(Color.black);
        menu.add(cimage_menu);
        menu.add(clancer_jeu);
        menu.add(cafficher_score);

        // Ajout du menu à la vue
        add(menu);
    }

    private void creerTableauDesScores() {
        
        scoreboard_container = new JPanel(); // Conteneur de toute la fenêtre
        scoreboard_container.setLayout(new BoxLayout(scoreboard_container, BoxLayout.Y_AXIS));
        scoreboard_container.setBackground(Color.black);
        
        scoreboard_ = new JPanel(); // Conteneur des lignes de score
        scoreboard_.setBackground(Color.black);
        JLabel label = new JLabel("Niveau    Score");
        label.setFont(score.getFont().deriveFont(15.0f));
        label.setBackground(Color.black);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setPreferredSize(new Dimension(this.getWidth()+1, 50));
        label.setForeground(Color.white);
        scoreboard_.add(label);
        scoreboard_container.add(scoreboard_);
        
        // Afficher les scores
        JButton jb_retour_menu = new JButton("Retour au menu");
        jb_retour_menu.setAlignmentX(CENTER_ALIGNMENT);
        scoreboard_container.setAlignmentX(CENTER_ALIGNMENT);
        jb_retour_menu.addActionListener((ActionEvent ae) -> {
            retour_menu_depuis_scoreboard();
        });
        scoreboard_container.add(jb_retour_menu);
        
        JLabel space = new JLabel("");
        space.setPreferredSize(new Dimension(this.getWidth(), 80));
        scoreboard_container.add(space);
    }
    
    private void retour_menu_depuis_scoreboard() {
        scoreboard_container.setVisible(false);
        remove(scoreboard_container);
        add(menu);
        menu.setVisible(true);
    }
    
    private void placerLesComposantsGraphiques() {

        creerFenetre();
        
        creerVueMap(); // Partie jeu -> map
        
        creerVueBarre(); // Partie jeu -> barre d'infos
        
        creerTableauDesScores();
        
        creerMenu();

    }

    private void mise_a_jour_barre_jeu() {
        
        score.setText("Score : " + jeu.getPacman().getScore());
        niveau.setText("Niveau : " + jeu.getNiveau() + "/3");
        vies.setText("Vies : " + jeu.getPacman().getVies());

        // Sauvegarde des scores de la partie
        scoreboard_tmp[0] = jeu.getNiveau();
        scoreboard_tmp[1] = jeu.getPacman().getScore();
    }
    
    private void enregistreScore() {
        int[] tmp = {scoreboard_tmp[0], scoreboard_tmp[1]};
        scoreboard.add(tmp);
    }
    
    private void chargerScores() {
        creerTableauDesScores();
        
        for (int[] scores : scoreboard) { // Pour chaque ligne de score
            JPanel l = new JPanel(); // Conteneur de la ligne
            l.setBackground(Color.black);
            l.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            
            JLabel l1 = new JLabel(scores[0] + "/3       "); // Partie gauche de la ligne
            JLabel l2 = new JLabel("   " + scores[1]); // Partie droite de la ligne
            
            l1.setPreferredSize(new Dimension(this.getWidth()/2, 15)); // Pour que la largeur tombe bien
            l2.setPreferredSize(new Dimension(this.getWidth()/2, 15)); // entre la gauche et la droite
            
            l1.setFont(score.getFont().deriveFont(15.0f)); // La police était un peu petite
            l2.setFont(score.getFont().deriveFont(15.0f));
            
            l1.setHorizontalAlignment(JLabel.RIGHT); // Pour avoir un effet "centré"
            l2.setHorizontalAlignment(JLabel.LEFT);
            
            l1.setForeground(Color.white); // Pour le "style"
            l2.setForeground(Color.white);
            
            l.add(l1); // Parce que sinon les dernières lignes ne servent à rien :-)
            l.add(l2);
            scoreboard_.add(l);
        }
    }

    private void lancer_jeu() {

        menu.setVisible(false);
        remove(menu);
        add(jeu_);
        jeu_.setVisible(true);
        
        chargerJeu();
        jeu.start();

    }

    private void retour_menu() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
        }

        jeu_.setVisible(false);
        remove(jeu_);
        add(menu);
        menu.setVisible(true);
        
        enregistreScore();

    }

    private void afficher_scores() {
        menu.setVisible(false);
        remove(menu);
        
        chargerScores();
        
        add(scoreboard_container);
        scoreboard_container.setVisible(true);
    }
    
    private void initialisationSon() {
        try {
            
            intro = AudioSystem.getAudioInputStream(new File("./Images/intro.wav"));
            AudioFormat formatIntro = intro.getFormat();
            DataLine.Info infoIntro = new DataLine.Info(Clip.class, formatIntro);
            clipIntro = (Clip) AudioSystem.getLine(infoIntro);
            clipIntro.open(intro);
            
            
            pacmanBoule = AudioSystem.getAudioInputStream(new File("./Images/insert_coin.wav"));
            AudioFormat formatPacmanBoule = pacmanBoule.getFormat();
            DataLine.Info infoPacmanBoule = new DataLine.Info(Clip.class, formatPacmanBoule);
            clipPacmanBoule = (Clip) AudioSystem.getLine(infoPacmanBoule);
            clipPacmanBoule.open(pacmanBoule);
            
            
            superBoule = AudioSystem.getAudioInputStream(new File("./Images/son.wav"));
            AudioFormat formatSuperBoule = superBoule.getFormat();
            DataLine.Info infoSuperBoule = new DataLine.Info(Clip.class, formatSuperBoule);
            clipSuperBoule = (Clip) AudioSystem.getLine(infoSuperBoule);
            clipSuperBoule.open(superBoule);
    
            
            bonus = AudioSystem.getAudioInputStream(new File("./Images/fruit.wav"));
            AudioFormat formatBonus = bonus.getFormat();
            DataLine.Info infoBonus = new DataLine.Info(Clip.class, formatBonus);
            clipBonus = (Clip) AudioSystem.getLine(infoBonus);
            clipBonus.open(bonus);
   
    
            pacmanFantome = AudioSystem.getAudioInputStream(new File("./Images/ghost_eat_1.wav"));
            AudioFormat formatPacmanFantome = pacmanFantome.getFormat();
            DataLine.Info infoPacmanFantome = new DataLine.Info(Clip.class, formatPacmanFantome);
            clipPacmanFantome = (Clip) AudioSystem.getLine(infoPacmanFantome);
            clipPacmanFantome.open(pacmanFantome);
    
    
            fantomePacman = AudioSystem.getAudioInputStream(new File("./Images/death_1.wav"));
            AudioFormat formatFantomePacman = fantomePacman.getFormat();
            DataLine.Info infoFantomePacman = new DataLine.Info(Clip.class, formatFantomePacman);
            clipFantomePacman = (Clip) AudioSystem.getLine(infoFantomePacman);
            clipFantomePacman.open(fantomePacman);
    
            fin = AudioSystem.getAudioInputStream(new File("./Images/corona.wav"));
            AudioFormat formatFin = fin.getFormat();
            DataLine.Info infoFin = new DataLine.Info(Clip.class, formatFin);
            clipFin = (Clip) AudioSystem.getLine(infoFin);
            clipFin.open(fin);


        } catch(IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void son(Clip clipMusic) {
        clipMusic.loop(0);
        clipMusic.setMicrosecondPosition(0L);
        clipMusic.start();
        
    }
    
    /**
     * 
     * 0 intro
     * 1 pacman mange une boule
     * 2 pacman mange un bonus
     * 3 pacman mange une superBoule
     * 4 pacman mange un fantôme
     * 5 pacman se fait manger par un fantôme
     * 6 fin de la partie
     */
    private void mettre_a_jour_son(){
       
       for (int i : jeu.getSon()) {
            switch(i){
               case 0:
                  son(clipIntro);
                  break;
               case 1:
                  son(clipPacmanBoule);
                  break;
               case 2:
                  son(clipBonus);
                  break;
               case 3:
                  son(clipSuperBoule);
                  break;
               case 4:
                  son(clipPacmanFantome);
                  break;
               case 5:
                  son(clipFantomePacman);
                  break;
               case 6:
                  son(clipFin);
                  break;
               
           }
       }
       jeu.getSon().clear();
    }
    
    private void mettreAJourAffichage() {

        for (int x = 0; x < size; x++) { // Pour chaque case de la grille des entités...
            for (int y = 0; y < size; y++) {
                if (
                    !startgood // Pour détecter le premier passage dans cette boucle
                    || lastGrille[x][y] != jeu.getGrille()[x][y] // Pour ne gérer que les changements
                    || jeu.getGrille()[x][y] instanceof Fantome // Sauf pour les fantômes qui changent quand même
                ) {
                    // Pour chaque entité de met à jour son icone
                    if (jeu.getGrille()[x][y] instanceof Bonus) {
                        // L'icone du bonus change selon le niveau
                        tabJLabel[x][y].setIcon(icoBonus[jeu.getNiveau()-1]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Pacman) {
                        tabJLabel[x][y].setIcon(
                            // L'icone du pacman dépend de sa direction
                            icoPacMan[ ((Pacman) jeu.getGrille()[x][y]).getState() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof Fantome) {
                        tabJLabel[x][y].setIcon(
                            // L'icone du fantôme dépend de pleins de choses
                            icoFantome[ ((Fantome) jeu.getGrille()[x][y]).getState() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof Mur) {
                        tabJLabel[x][y].setIcon(
                            // L'icone du mur dépend de son type
                            icoMur[ ((Mur) jeu.getGrille()[x][y]).getTypeMur() ]
                        );
                    }
                    else if (jeu.getGrille()[x][y] instanceof SuperBoule) {
                        // Là ça dépend de rien, enfin !
                        // Il faut cependant placer cette ligne avant "Boule"
                        tabJLabel[x][y].setIcon(icoFood[1]);
                    }
                    else if (jeu.getGrille()[x][y] instanceof Boule) { // Car cette condition fonctionne
                        tabJLabel[x][y].setIcon(icoFood[0]);           // aussi pour SuperBoule
                    }
                    else {
                        tabJLabel[x][y].setIcon(icoCouloir); // Le seul qui reste
                    }
                }
                lastGrille[x][y] = jeu.getGrille()[x][y]; // Pour mettre à jour la sauvegarde qu'on
                                                          // utilise pour détecter les changements
            }
        }
        startgood = true; // Pour indiquer qu'on a passé la première exécution
        mise_a_jour_barre_jeu();
        mettre_a_jour_son();
    }

    @Override
    public void update(Observable o, Object arg) {
        
        // Si on ne joue pas, on est dans le menu ou le scoreboard
        // Mais le scoreboard n'est pas géré là...
        if (jeu.getPartieOn())
            mettreAJourAffichage();
        else
            retour_menu();
    }
}
