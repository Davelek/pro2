package uhk.fim.gui;

import com.google.gson.Gson;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;

public class MainFrame extends JFrame implements ActionListener {
    MainFrame mainFrame;

    // Tlačítka deklarujeme zde, abychom k nim měli přístup v metodě actionPerformed
    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel shoppingCartTableModel;
    JLabel lblTotalPrice = new JLabel();

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Vytvoříme košík (data)
        shoppingCart = new ShoppingCart();
        // Vytvoříme model
        shoppingCartTableModel = new ShoppingCartTableModel();
        // Propojíme model s košíkem (data)
        shoppingCartTableModel.setShoppingCart(shoppingCart);

        initGUI();
    }

    public void initGUI() {
        // Vytvoříme hlavní panel, do kterého budeme přidávat další (pod)panely.
        // Naším cílem při tvorbě GUI, je snaha jednotlivé komponenty zanořovat.
        JPanel panelMain = new JPanel(new BorderLayout());

        //menubar

        createMenuBar();

        // Vytvoříme další 3 panely. Panel pro prvky formuláře pro přidání položky.
        // Panel pro tabulku a panel pro patičku.
        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout LEFT - komponenty chceme zarovnat zleva doprava.
        JPanel panelTable = new JPanel(new BorderLayout());
        JPanel panelFooter = new JPanel(new BorderLayout());

        // *** Formulář pro přidání položky ***
        // Název
        JLabel lblInputName = new JLabel("Název: ");
        txtInputName = new JTextField("", 15);
        // Cena za 1 kus
        JLabel lblInputPricePerPiece = new JLabel("Cena 1 kus: ");
        txtInputPricePerPiece = new JTextField("", 5);
        // Počet kusů
        JLabel lblInputPieces = new JLabel("Počet kusů: ");
        spInputPieces = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        // Tlačítka
        btnInputAdd = new JButton("Přidat");
        btnInputAdd.addActionListener(this); // Nastavení ActionListeneru - kdo obslouží kliknutí na tlačítko.

        // Přidání komponent do horního panelu pro formulář na přidání položky
        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);
        panelInputs.add(lblInputPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);
        panelInputs.add(lblInputPieces);
        panelInputs.add(spInputPieces);
        panelInputs.add(btnInputAdd);

        // *** Patička ***
        updateFooter();
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);

        // *** Tabulka ***
        JTable table = new JTable();
        // Tabulku propojíme s naším modelem
        table.setModel(shoppingCartTableModel);
        // Tabulku přidáme do panelu a obalíme ji komponentou JScrollPane
        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        // Přidání (pod)panelů do panelu hlavního
        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);

        // Přidání hlavního panelu do MainFrame (JFrame)
        add(panelMain);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Soubor");
        fileMenu.add(new AbstractAction("Nový nákupní seznam") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(new AbstractAction("Otevřít") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loafFileXmlSax
                loadFileXmlDom4j();
            }
        });
        fileMenu.add(new AbstractAction("načti json") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loafFileXmlSax
                loadJson();
            }
        });
        fileMenu.add(new AbstractAction("Uložit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileCsv();
            }
        });

        menuBar.add(fileMenu);

        JMenu aboutMenu = new JMenu("O programu");
        menuBar.add(aboutMenu);


        setJMenuBar(menuBar);

    }




    // Při kliknutí na jakékoliv tlačítko se zavolá tato metoda.
    // Toho jsme docílili implementování rozhraní ActionListener a nastavením tlačítek např. btnInputAdd.addActionListener(this);
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Metoda se volá pro každé tlačítko, musíme tedy rozhodnout, co se má skutečně stát pro konkrétní tlačítka
        if (actionEvent.getSource() == btnInputAdd) {
            addProduct();
        }
    }

    private void addProduct() {
        boolean nazev = txtInputName.getText().trim().isEmpty();
        if (nazev) {
            JOptionPane.showMessageDialog(mainFrame, "Nezadal jste název", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Double doubleZText = 0.0;

        try {
            doubleZText = Double.parseDouble(txtInputPricePerPiece.getText().replace(",", "."));


            // System.out.println(doubleZText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Nezadal jste správně cena za kus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Vytvořit novou položku
        ShoppingCartItem item = new ShoppingCartItem(txtInputName.getText(), doubleZText, (int) spInputPieces.getValue());
        // Přidat položku do košíku
        // if (!shoppingCart.updateDuplicity(item)){
        shoppingCart.addItem(item);
        // }
        updateFooter();
        // Refreshnout tabulku
        shoppingCartTableModel.fireTableDataChanged();
        JOptionPane.showMessageDialog(mainFrame, "Super! Přidáno.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateFooter() {
        //double roundedPrize =Math.round((shoppingCart.getTotalPrice() * 100)/100);

        // DecimalFormat f = new DecimalFormat("##.00");
        // f.format(shoppingCart.getTotalPrice())
        lblTotalPrice.setText("Celková cena: " + String.format("%.2f", shoppingCart.getTotalPrice()) + " Kč");
    }

    private void saveFileCsv() {
        JFileChooser fc = new JFileChooser();

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));) {

                for (ShoppingCartItem item : shoppingCart.getItems()
                ) {
                    bw.write(item.getName() + ";" + item.getPricePerPiece() + ";" + item.getPieces());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "došlo k chybě", "Error", JOptionPane.ERROR_MESSAGE);
            }


        }


    }

    private void loafFileXmlSax() {
        try {
            CharArrayWriter content = new CharArrayWriter();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("src/uhk/fim/save.xml"), new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    //  System.out.println("start element" + qName);
                    content.reset();
                }


                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    //  System.out.println("end element" + qName);

                }


                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    content.write(ch, start, length);
                }
            });

            System.out.println(content);


        } catch (Exception e) {

        }
    }

    private void loafFileXmlDom() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File("src/uhk/fim/save.xml"));
            Node root = document.getFirstChild();
            short nodeType = root.getNodeType();
            if (root.hasChildNodes()){
                NodeList list = root.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    Node nextNode = list.item(i);
                    if (root.hasChildNodes()){

                    }
                    
                }
            }




        } catch (Exception e) {

        }


    }
    private void loadFileXmlDom4j() {
        DocumentFactory df = DocumentFactory.getInstance();
        SAXReader reader = new SAXReader(df);


        try {
            org.dom4j.Document doc = reader.read(new File("src/save,xml"));
            System.out.println(doc.asXML());

        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private void loadJson() {
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
                    String test= "";
                }
                catch (Exception e){

                }
            }
        });

        thread.start();
    }
}
