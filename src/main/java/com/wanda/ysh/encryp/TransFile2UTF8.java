package com.wanda.ysh.encryp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class TransFile2UTF8 {

    public static void main(String[] args) {
        File file = new File("D:/wp/yangshh/LearnPro/src/main/java/com/wanda/encryp");
        transAll(file);
    }

    private static void transAll(File file) {
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                transOne(f);
            }
            if (f.isDirectory()) {
                transAll(f);
            }
        }
    }

    private static void transOne(File file) {
        File outFile = new File(file.getAbsolutePath() + ".tmp");
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            String ln = br.readLine();
            while (ln != null) {
                bw.write(ln);
                bw.newLine();
                ln = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (file.delete()) {
            outFile.renameTo(file);
        }
    }
}
