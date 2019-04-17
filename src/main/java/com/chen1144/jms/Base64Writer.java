package com.chen1144.jms;

import java.io.IOException;
import java.io.Writer;
import java.util.Base64;

public class Base64Writer {
    private Writer writer;

    public Base64Writer(Writer writer){
        this.writer = writer;
    }

    public void write(String string) throws IOException {
        writer.write(Base64.getEncoder().encodeToString(string.getBytes()));
        writer.write('\n');
    }

    public void flush() throws IOException{
        writer.flush();
    }
}
