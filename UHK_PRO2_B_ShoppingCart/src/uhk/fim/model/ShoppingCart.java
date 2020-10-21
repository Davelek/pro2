package uhk.fim.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<ShoppingCartItem>();
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void addItem(ShoppingCartItem item) {
        this.items.add(item);
    }
    public double getTotalPrice(){

        double suma = 0;
        for (ShoppingCartItem item: items
        ) {
            suma += item.getPricePerPiece()*item.getPieces();
        }
        return suma;
    }
}