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

        double suma = 0.00;
        for (ShoppingCartItem item: items
        ) {
            suma += item.getTotalPrize();
        }
        return suma;
    }
    public boolean updateDuplicity(ShoppingCartItem newItem){
        for (ShoppingCartItem item: items
             ) {
            if (newItem.getName().equals(item.getName()) && newItem.getPricePerPiece() == item.getPricePerPiece()) {
                item.setPieces(item.getPieces() + newItem.getPieces());
                return true;
            }
        }
        return false;
    }



}