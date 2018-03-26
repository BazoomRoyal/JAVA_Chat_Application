package com.fredericboisguerin.insa;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        Launch launch = new Launch(new ListUtilisateur());
        launch.mainLoop();
    }
}
