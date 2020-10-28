package server.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Hashing {

    private static Argon2 argon;
    private static int memory = 1024;

    public Argon2Hashing(){
        argon = Argon2Factory.create();
    }

    public static String encrypt(String txt) {
        return argon.hash(10, memory, 1, txt.toCharArray());
    }
    /*
        Entschlüsselung eines Byte-Arrays mit Rückgabe eines Strings
     */
    public static boolean verify(String hash, String txt) {
        return argon.verify(hash, txt.toCharArray());
    }


}
