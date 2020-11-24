package uhk.fim.fileWork;

import com.google.gson.Gson;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URL;

public class FileWork {


    private static String pathToStorage = "D:\\Project\\Pro2\\UHK_PRO2_B_ShoppingCart\\src\\uhk\\fim\\storage.csv";
    private static String startintPath = "D:\\Project\\Pro2\\UHK_PRO2_B_ShoppingCart\\src\\uhk\\fim";

    public static void saveFileXmlDom4j() {

    }

    /*public static void loadJson() {
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
    }*/


    public static ShoppingCart loadFileCsvStorage() {
        return loadFileCsv(pathToStorage);
    }


    public static ShoppingCart loadFileCsv(String path) {
        String filename = path;
        ShoppingCart shoppingCart = new ShoppingCart();
        String thisLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((thisLine = br.readLine()) != null) {
                shoppingCart.addItem(new ShoppingCartItem(thisLine.split(";")[0],
                        Double.parseDouble(thisLine.split(";")[1]),
                        Integer.parseInt(thisLine.split(";")[2])));
            }
            br.close();


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);

        }
        return shoppingCart;
    }

    public static void saveFileCsvStorage(ShoppingCart shoppingCart) {
        saveFileCsv(shoppingCart, pathToStorage);
    }

    public static void saveFileCsv(ShoppingCart shoppingCart, String path) {
        //JFileChooser fc = new JFileChooser();

        // if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        // String fileName = fc.getSelectedFile().getAbsolutePath();

        String fileName = path;
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


    public static void saveFileDialog(ShoppingCart shoppingCart) {
        JFileChooser fc = new JFileChooser(startintPath);
        fc.setDialogTitle("Ulož do souboru");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("csv a json", "csv", "json");

        fc.addChoosableFileFilter(filter1);

        int returnValue = fc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fc.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                String extension = path.substring(path.lastIndexOf("."), path.length());
                // System.out.println(extension);
                switch (extension) {
                    case ".csv":
                        saveFileCsv(shoppingCart, path);
                        break;
                    case ".json":
                        saveFileJson(shoppingCart, path);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Zadal jste neplatný typ souboru \n" +
                                "" +
                                "Zadejte buď \".csv\" nebo \"json\"", "Error", JOptionPane.ERROR_MESSAGE);

                }


            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Musíte napsat soubor ve tvaru\n" +
                        " \"nazevSouboru.csv\" nebo \"nazevSoubour.json\"", "Error", JOptionPane.ERROR_MESSAGE);

            }


        }


    }


    public static ShoppingCart openFileDialog() {
        ShoppingCart cart = new ShoppingCart();

        JFileChooser fc = new JFileChooser(startintPath);
        fc.setDialogTitle("Otevři ze souboru");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("csv a json", "csv", "json");

        fc.addChoosableFileFilter(filter1);

        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fc.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                String extension = path.substring(path.lastIndexOf("."), path.length());
                // System.out.println(extension);
                switch (extension) {
                    case ".csv":
                        cart = loadFileCsv(path);
                        break;
                    case ".json":
                        cart = loadFileJson(path);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Zadal jste neplatný typ souboru \n" +
                                "" +
                                "Zadejte buď \".csv\" nebo \"json\"", "Error", JOptionPane.ERROR_MESSAGE);

                }


            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Musíte napsat soubor ve tvaru\n" +
                        " \"nazevSouboru.csv\" nebo \"nazevSoubour.json\"", "Error", JOptionPane.ERROR_MESSAGE);

            }


        }

        return cart;
    }

    private static ShoppingCart loadFileJson(String path) {

        Gson gson = new Gson();
        ShoppingCart cart = new ShoppingCart();


        try {


            cart = gson.fromJson(new FileReader(path), ShoppingCart.class);
            // System.out.println(cart);
            //  JOptionPane.showMessageDialog(null, "Úspěšně uloženo", "Úspěch", JOptionPane.INFORMATION_MESSAGE);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);

        }
        return cart;


    }

    public static void saveFileJson(ShoppingCart shoppingCart, String path) {
        Gson gson = new Gson();
        String cart = gson.toJson(shoppingCart);
        try (FileWriter file = new FileWriter(path)) {

            file.write(cart);
            file.flush();
            JOptionPane.showMessageDialog(null, "Úspěšně uloženo", "Úspěch", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
}
