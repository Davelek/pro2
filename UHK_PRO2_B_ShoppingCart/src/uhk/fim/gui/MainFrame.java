package uhk.fim.gui;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uhk.fim.fileWork.FileWork;
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

public class MainFrame extends JFrame implements ActionListener {
    MainFrame mainFrame;

    // Tlačítka deklarujeme zde, abychom k nim měli přístup v metodě actionPerformed
    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel shoppingCartTableModel;
    JLabel lblTotalPrice = new JLabel();
    JLabel lbBoughtPrize = new JLabel();
    JLabel lbDifference = new JLabel();

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Vytvoříme košík (data)
        shoppingCart = new ShoppingCart();
        ShoppingCart storage = FileWork.loadFileCsvStorage();
        if (storage.hasItems()) {
            shoppingCart = storage;
        }
        // Vytvoříme model
        shoppingCartTableModel = new ShoppingCartTableModel();
        // Propojíme model s košíkem (data)
        shoppingCartTableModel.setShoppingCart(shoppingCart);

        initGUI();
    }

    public void initGUI() {
        // Vytvoříme hlavní panel, do kterého budeme přidávat další (pod)panely.
        // Naším cílem při tvorbě GUI, je snaha jednotlivé komponenty zanořovat.
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/uhk/fim/img/cart.png")));


        JPanel panelMain = new JPanel(new BorderLayout());


        //menubar

        createMenuBar();

        // Vytvoříme další 3 panely. Panel pro prvky formuláře pro přidání položky.
        // Panel pro tabulku a panel pro patičku.
        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout LEFT - komponenty chceme zarovnat zleva doprava.
        JPanel panelTable = new JPanel(new BorderLayout());
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelInputs.setBackground(new Color(0xFFF2C6));

        panelFooter.setBackground(new Color(0x66FF66));

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
        btnInputAdd = new JButton("Přidat" , new ImageIcon(getClass().getResource("/uhk/fim/img/add.gif")));

        btnInputAdd.addActionListener(this); // Nastavení ActionListeneru - kdo obslouží kliknutí na tlačítko.

        // Přidání komponent do horního panelu pro formulář na přidání položky
        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);
        panelInputs.add(lblInputPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);
        panelInputs.add(lblInputPieces);
        panelInputs.add(spInputPieces);
        panelInputs.add(btnInputAdd);
        lbBoughtPrize.setHorizontalAlignment(JLabel.CENTER);

        // *** Patička ***
        updateFooter();
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);
        panelFooter.add(lbBoughtPrize, BorderLayout.CENTER);
        panelFooter.add(lbDifference, BorderLayout.EAST);

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        //asi to není hezký řešení, ale nějak mi nenapadá jak to udělat jinak, než kontrolovat změnu isBought
                        updateFooter();
                        deleteItem();


                    }catch (Exception e){

                    }

                }
            }
        });
        thread.start();
    }

    private void reset() {
        shoppingCart.clear();
        shoppingCartTableModel.fireTableDataChanged();


    }


    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Soubor");
        fileMenu.add(new AbstractAction("Nový nákupní seznam", new ImageIcon(getClass().getResource("/uhk/fim/img/new.gif"))) {
            @Override
            public void actionPerformed(ActionEvent e) {

                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Všechny neuložený věci budou ztaceny. Chcete pokrařovat",
                        "Warning", dialogButton);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    reset();
                }


            }
        });
        fileMenu.add(new AbstractAction("Otevřít" , new ImageIcon(getClass().getResource("/uhk/fim/img/open.gif"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loafFileXmlSax
                ShoppingCart cart = FileWork.openFileDialog();
                if (cart.hasItems()) {
                    //    System.out.println("jsme tu");
                    shoppingCart = cart;
                    shoppingCartTableModel.setShoppingCart(shoppingCart);
                    shoppingCartTableModel.fireTableDataChanged();

                }
            }
        });
        fileMenu.add(new AbstractAction("Uložit jako" , new ImageIcon(getClass().getResource("/uhk/fim/img/save.gif"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loafFileXmlSax
                //   loadJson();
                FileWork.saveFileDialog(shoppingCart);
            }
        });
        fileMenu.add(new AbstractAction("Uložit" , new ImageIcon(getClass().getResource("/uhk/fim/img/save.gif"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWork.saveFileCsvStorage(shoppingCart);
            }
        });

        menuBar.add(fileMenu);

        JMenu aboutMenu = new JMenu("Other");
        aboutMenu.add(new AbstractAction("O pragramu" , new ImageIcon(getClass().getResource("/uhk/fim/img/about.gif"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "K O Š Í K\n(C)David Mašek, FIM UHK",
                        "O programu",
                        JOptionPane.INFORMATION_MESSAGE + JOptionPane.OK_OPTION,
                        new ImageIcon(getClass().getResource("/uhk/fim/img/uhk-fim-logo.png"))

                );
            }
        });

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
    private void deleteItem(){
        for (int i = 0; i < shoppingCart.getItems().size(); i++) {
            if (shoppingCart.getItems().get(i).isDelete()){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Chcete odstranit položku?",
                        "Warning", dialogButton);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    shoppingCart.getItems().remove(i);
                }
                if (dialogResult == JOptionPane.NO_OPTION){
                    shoppingCart.getItems().get(i).setDelete(false);
                }

            }
        }

    }

    private void updateFooter() {
        //double roundedPrize =Math.round((shoppingCart.getTotalPrice() * 100)/100);

        // DecimalFormat f = new DecimalFormat("##.00");
        // f.format(shoppingCart.getTotalPrice())
        lblTotalPrice.setText("Celková cena: " + String.format("%.2f", shoppingCart.getTotalPrice()) + " Kč");
        double boughtPrize = 0;
        for (ShoppingCartItem item: shoppingCart.getItems()
             ) {
            if (item.isBought()){
                boughtPrize += item.getTotalPrize();
            }
        }
        lbBoughtPrize.setText("Cena zakoupených: " + String.format("%.2f", boughtPrize) + " Kč");
        lbDifference.setText("Rozdíl: " + String.format("%.2f",shoppingCart.getTotalPrice() - boughtPrize) + " kč");
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
            if (root.hasChildNodes()) {
                NodeList list = root.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    Node nextNode = list.item(i);
                    if (root.hasChildNodes()) {

                    }

                }
            }


        } catch (Exception e) {

        }


    }

    public static void loadFileXmlDom4j() {
        DocumentFactory df = DocumentFactory.getInstance();
        SAXReader reader = new SAXReader(df);
        try {
            org.dom4j.Document doc = reader.read(new File("src/save,xml"));
            System.out.println(doc.asXML());

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}
