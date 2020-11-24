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

    public void addItem(ShoppingCartItem newItem) {

        for (ShoppingCartItem item: items
        ) {
            if (newItem.getName().equals(item.getName()) && newItem.getPricePerPiece() == item.getPricePerPiece()) {
                item.setPieces(item.getPieces() + newItem.getPieces());
               return;
            }
        }

        this.items.add(newItem);
    }


    public double getTotalPrice(){

        double suma = 0.00;
        for (ShoppingCartItem item: items
        ) {
            suma += item.getTotalPrize();
        }
        return suma;
    }
/*    public boolean updateDuplicity(ShoppingCartItem newItem){

        return false;
    }*/
    public void clear(){
        items = new ArrayList<ShoppingCartItem>();
    }



}