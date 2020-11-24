package uhk.fim.fileWork;

import com.google.gson.Gson;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import uhk.fim.model.ShoppingCart;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public class FileWork {



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


}
