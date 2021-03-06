/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zipsms;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;
import miniblockcipher.MiniBlockCipher;
import utils.BitUtils;
import utils.IOFile;

/**
 *
 * @author zulvafachrina
 */
public class ZipSMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
       
        String input;
        do {
            System.out.println("Encrypt/Decrypt SMS");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("0. Exit");

            System.out.print("Pilihan: ");
            input = scanner.nextLine();
            IOFile iofile = new IOFile();
            if(input.equals("1")) {
                System.out.print("Nama File: ");
                String filename = scanner.nextLine();
                System.out.print("Kunci: ");
                String key = scanner.nextLine();
                
                String text = iofile.readText(filename);
                byte[] btext = text.getBytes("UTF-8");
                byte[] bkey = key.getBytes("UTF-8");
                                
                MiniBlockCipher myCipher = new MiniBlockCipher(bkey);
                byte[] bres = myCipher.encrypt(btext, bkey);
                
                String encrypted = BitUtils.byteToHex(bres);
		
		iofile.writeText("data/result_cipher.txt", encrypted);
                System.out.println();
                
                
            } else if(input.equals("2")){
                System.out.print("Nama File: ");
                String filename = scanner.nextLine();
                System.out.print("Kunci: ");
                String key = scanner.nextLine();
                
                String text = iofile.readText(filename);
                byte[] bkey = key.getBytes();

                MiniBlockCipher myCipher = new MiniBlockCipher(bkey);
                
                byte[] btext = BitUtils.hexToBytes(text);
                byte[] bres = myCipher.decrypt(btext, bkey);
                              
                String decrypted = new String(bres,"UTF-8");
                iofile.writeText("data/result_plain.txt", decrypted);
               
            }
        } while(!input.equals("0"));
      
    }
}
