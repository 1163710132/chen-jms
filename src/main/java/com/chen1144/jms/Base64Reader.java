package com.chen1144.jms;

import java.io.Reader;
import java.util.Base64;
import java.util.Scanner;

public class Base64Reader {
    private Scanner scanner;

    public Base64Reader(Reader reader){
        this.scanner = new Scanner(reader);
    }

    public String read(){
        String base64 = scanner.nextLine();
        return new String(Base64.getDecoder().decode(base64));
    }
}
