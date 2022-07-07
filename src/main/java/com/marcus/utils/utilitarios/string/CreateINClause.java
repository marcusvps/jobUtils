package com.marcus.utils.utilitarios.string;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CreateINClause {


    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

        System.out.println(create(data));
    }


    public static String create(String values){
        StringBuilder inClause = new StringBuilder("IN (");
        String[] split;
        if(values.contains("\n")){
            split = values.split("\n");
        }else{
            split = values.split(" ");
        }
        System.out.println("Total de elementos identificados: " + split.length);
        formatValues(inClause, split);
        return inClause.toString();
    }

    private static void formatValues(StringBuilder inClause, String[] split) {
        for (String val: split) {
            inClause.append(val).append(",");
        }

        inClause.replace(inClause.length() - 1, inClause.length(), "");
        inClause.append(")");
    }
}
