package com.fredericboisguerin.insa;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.WEST;

/**Fenetre de chat **/
public class Interface extends JFrame{

    private TCPMessageSenderService  tcpMessageSenderService;
    private ReceiveTCP receiveTCP ;
    private static final int DEFAULT_TEXT_FIELD_WIDTH = 10;
    private final JTextField messageChatTextField = buildInputTextField();
    private Integer portDest ;
    private Integer portSource ;
    public String pseudo ;
    private String ip;
    public MyTextArea espaceTexte ;
    public ArrayList<String> historique ;
    private String nomFichierHistorique ;

    public Interface(String nom, String ip, Integer portD, Integer portS) throws IOException {
        this.pseudo = nom;
        this.ip = ip;
        this.portDest = portD;
        this.portSource = portS;
        this.receiveTCP = new ReceiveTCP(this);
        this.tcpMessageSenderService = new TCPMessageSenderService(this.portDest);
        /**Pour l'historique**/
        this.historique = new ArrayList<>() ;
        this.nomFichierHistorique = ("src/main/resources/"+ this.pseudo+ ".csv") ;
        this.historique = recupererHistorique(this.historique) ;

        this.espaceTexte = new MyTextArea() ;
        this.espaceTexte.setLineWrap(true);
        this.espaceTexte.setEditable(false);

        /**Scroll la conversation**/
        JScrollPane scroll = new JScrollPane(this.espaceTexte, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.setTitle(this.pseudo);
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);


        JLabel messageChatLabel = new JLabel("Chat : ");

        messageChatLabel.setLabelFor(messageChatTextField);

        /** Bouton pour envoyer le message**/
        JButton sendButton = new JButton("Envoyer");
        sendButton.addActionListener(e -> {
            try {
                onComputeButtonClicked();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        /** Bouton pour fermer le chat et la connexion**/
        JButton closeButton = new JButton("Fermer chat");
        closeButton.addActionListener(e -> {
            try {
                onCloseButtonClicked() ;
            } catch (Exception e1){
                e1.printStackTrace();
            }
        });

        /** Gestion du champ d'écriture **/
        KeyListener execution = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendButton.doClick(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        messageChatLabel.addKeyListener(execution);

        JPanel formPanel = new JPanel();
        addLabelAndFieldInPanel(messageChatLabel, messageChatTextField, formPanel);
        add(formPanel, NORTH) ;
        add(sendButton, SOUTH) ;
        add(closeButton, WEST) ;


        /**affichage message **/
        this.getContentPane().add(scroll, null);


        this.setVisible(true);
        afficherHistorique();
        this.receiveTCP.lancerecoute(this.receiveTCP, this.portSource);

    }

    private static JTextField buildInputTextField() {
        return new JTextField(DEFAULT_TEXT_FIELD_WIDTH);
    }

    private static void addLabelAndFieldInPanel(JComponent label, JComponent field, JPanel p) {
        p.add(label);
        p.add(field);
    }

    /**Envoie le message quand on clique sur le bouton, l'ajoute à l'historique et sur la fenêtre de chat**/
    private void onComputeButtonClicked() throws Exception {
        String message = messageChatTextField.getText();
        String newmess = (Launch.pseudo+ " : "+ message +"\n") ;
        this.espaceTexte.ajouterTexte(newmess);
        this.historique.add(newmess) ;
        this.tcpMessageSenderService.sendMessageOn(this.ip, this.portDest, message);
    }

    /**Ferme la fenêtre de chat quand on clique sur le bouton et envoie un message de fermeture à l'autre utilisateur**/
    private void onCloseButtonClicked() throws Exception {
        this.tcpMessageSenderService.sendMessageOn(this.ip, this.portDest, "fermeture/&");
        ecrireHistorique();
        this.dispose();
    }

    /**Ecrit l'historique dans le fichier associé à l'utilisateur**/
    public void ecrireHistorique() throws IOException {
        String[] entree = new String[this.historique.size()] ;
        String actuel ;
        Integer index = 0 ;

        for (String s : this.historique){
            actuel = s ;
            entree[index]= actuel ;
            index += 1 ;
        }
        try (CSVWriter writerHistorique = new CSVWriter(new FileWriter(this.nomFichierHistorique));) {
            writerHistorique.writeNext(entree);
        }
    }

    /**Récupère l'historique de conversation dans le fichier associé à l'utilisateur auquel on parle**/
    private ArrayList<String> recupererHistorique(ArrayList historique) throws IOException {
             File f = new File(this.nomFichierHistorique);
             if(f.isFile())
             {
                CSVReader readerHistorique = new CSVReader(new FileReader(this.nomFichierHistorique));
                String[] nextLine ;
                while ((nextLine = readerHistorique.readNext()) != null) {
                    for (int i = 0; i < nextLine.length; i++) {
                        historique.add(nextLine[i]);
                    }
                }
             }
        return historique;
    }

    /**Affiche l'historique dans la fenêtre de chat**/
    private void afficherHistorique(){
        for (String s : this.historique){
            this.espaceTexte.ajouterTexte(s);
        }
    }

}
