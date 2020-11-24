package uhk.fim.fileWork;

import com.google.gson.Gson;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import java.io.*;
import java.net.URL;

public class FileWork {



    private static String pathToStorage = "D:\\Project\\Pro2\\UHK_PRO2_B_ShoppingCart\\src\\uhk\\fim\\storage.csv";


    public static void saveFileXmlDom4j(){

    }

    public static void loadJson() {
        Gson gson =  new Gson();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                }catch (Exception e){

                }

                try{
                    //simulace dlouhé odpovědi

                    ShoppingCart cart = gson.fromJson(new InputStreamReader(
                            new URL("https://lide.uhk.cz/fim/student/benesja4/shoppingCart.json").openStream()
                    ), ShoppingCart.class);

                }
                catch (Exception e){

                }
            }
        });

        thread.start();
    }



    public static ShoppingCart loadFileCsvStorage(){
        String filename = pathToStorage;
        ShoppingCart shoppingCart = new ShoppingCart();
        String thisLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){

            while ((thisLine = br.readLine()) != null) {
                shoppingCart.addItem(new ShoppingCartItem(thisLine.split(";")[0],
                        Double.parseDouble(thisLine.split(";")[1]),
                        Integer.parseInt(thisLine.split(";")[2] )));
            }
            br.close();


        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);

        }
        return shoppingCart;
    }

    public static void saveFileCsvStorage(ShoppingCart shoppingCart) {
        //JFileChooser fc = new JFileChooser();

       // if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
           // String fileName = fc.getSelectedFile().getAbsolutePath();

        String fileName = pathToStorage ;
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false));) {

                for (ShoppingCartItem item : shoppingCart.getItems()
                ) {
                    bw.write(item.getName() + ";" + item.getPricePerPiece() + ";" + item.getPieces());
                    bw.newLine();
                }
                bw.close();
                JOptionPane.showMessageDialog(null, "Úspěšně uloženo", "Úspěch", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);
            }


     //   }


    }


}
