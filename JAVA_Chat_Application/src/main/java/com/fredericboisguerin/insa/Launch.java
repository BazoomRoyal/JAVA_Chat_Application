package com.fredericboisguerin.insa;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Launch {

    public static ListUtilisateur list;
    public static String pseudo ;
    public static Integer portTCP = 3900;
    public static String myIP ;
    private Integer portUDP ;

    private boolean finish;
    private UDPMessageSenderService udpMessageSenderService ;
    private TCPMessageSenderService tcpMessageSenderService ;
    private TCPEcouteContinue tcpEcouteContinue ;


    private ReceiveUDP receiveUDP ;


    public Launch(ListUtilisateur list) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().toString() ;
        this.myIP = ip.substring(ip.indexOf("/")+1, ip.length()) ;
        this.list = list;
        this.finish = false ;
        this.udpMessageSenderService = new UDPMessageSenderService() ;
        this.tcpMessageSenderService = new TCPMessageSenderService(this.portTCP) ;
        this.receiveUDP = new ReceiveUDP();
        this.portUDP = 4504 ;
        this.tcpEcouteContinue = new TCPEcouteContinue() ;
    }

    public void mainLoop() throws Exception {

        while(!this.finish){
            this.afficherMenu();

        }

    }

    public void afficherMenu() throws Exception {
        System.out.println();
        System.out.println("MENU");
        System.out.println();
        System.out.println("0 - Quitter");
        System.out.println("1 - Connexion");
        System.out.println("2 - Choisir avec qui chatter");
        System.out.println("3 - Message de test pour simuler une réponse de Jesuisuntest");
        System.out.println("4 - Message de test pour simuler une réponse de Jesus");
        System.out.println("5 - Message de test pour simuler le départ du réseau de Jesuisuntest");
        System.out.println("6 - Envoyer un message tcp pour test");


        System.out.println();


        Scanner scanner = new Scanner(System.in);
        Integer o = Integer.valueOf(scanner.nextLine());

        switch(o){
            case 0 : {
                this.finish = true ;
                break ;
            }

            case 1 : {
                this.udpMessageSenderService.sendMessageOn("127.255.255.255" , this.portUDP, "requete/3958/"+this.myIP+"/&" );
                this.receiveUDP.lancerecoute(this.receiveUDP, 4504);
                demanderPseudo(scanner);
                lancerEcouterTCP();
                break ;
            }


            case 2 : {
                choisirUtilisateur(scanner);
                break ;
            }

            case 3 : {
                udpMessageSenderService.sendMessageOn("127.255.255.255", this.portUDP, "reponse/Jesuisuntest/127.0.0.1/2222/&" );
                break ;
            }

            case 4 : {
                udpMessageSenderService.sendMessageOn("127.255.255.255", this.portUDP, "reponse/Jesus/127.0.0.1/2244/&" );

                break ;

            }
            case 5 : {
                this.udpMessageSenderService.sendMessageOn("127.255.255.255", this.portUDP, "quit/Jesuisuntest/&");
                break ;
            }

            case 6 : {
                this.tcpMessageSenderService.sendMessageOn("127.0.0.1", 2222, "Bien le bonjour");
                break ;
            }
        }
    }

    /**Lancer l'écoute TCP qui permet de créer des conversations**/
    private void lancerEcouterTCP() throws IOException {
        this.tcpEcouteContinue.lancerecoute(this.tcpEcouteContinue, this.portTCP);
    }

    /**Méthode qui permet de choisir son pseudo pour se connecter**/
    public void demanderPseudo(Scanner scanner) throws Exception {

        System.out.println("Veuillez entrer votre pseudo");

        String s = scanner.nextLine();
        while (!list.pseudo_unique(s)){

            s = scanner.nextLine();
        }
        this.pseudo = s;
        this.udpMessageSenderService.sendMessageOn("127.255.255.255" , this.portUDP, "presente/"+this.pseudo + "/"+ this.myIP+"/"+ this.portTCP+"/&");
    }

    /**Affiche les utilisateurs et attend qu'on écrive avec lequel on veut chatter**/
    public void choisirUtilisateur (Scanner scanner) throws Exception {
        Integer port ;
        this.list.afficherUtilisateurs();
        String s = scanner.nextLine() ;
        String ip = this.list.retournerIP(s) ;
        port = this.list.retournerPort(s) ;
        if (port==0){
            System.out.println("Ton pseudo il est pas dans la liste là !! Tu dis n'importe quoi !");
        }
        else{
            etablirConnection(port, s, ip);
        }

    }

    /**Permet d'ouvrir un fenetre et d'informer un utilisateur que nous voulons chatter avec lui**/
    private void etablirConnection(Integer port, String s, String ip) throws Exception {
        Integer monPort = getRandomNumberInRange(3000,9000);
        Integer tonPorc = monPort + 1 ;
        this.tcpMessageSenderService.sendMessageOn(ip,port,"chattons/"+ this.pseudo +"/" + monPort + "/" + tonPorc + "/&");
        Interface chat = new Interface(s, ip, tonPorc, monPort) ;
    }

    /**Choisi des ports pour établir une communication entre 2 utilisateurs**/
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



}
