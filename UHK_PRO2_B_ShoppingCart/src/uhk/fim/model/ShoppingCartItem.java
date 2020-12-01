package uhk.fim.model;

public class ShoppingCartItem {
    private String name;
    private double pricePerPiece;
    private int pieces;
    private boolean bought;
    private boolean delete = false;


    public ShoppingCartItem(String name, double pricePerPiece, int pieces) {
        this.name = name;
        this.pricePerPiece = pricePerPiece;
        this.pieces = pieces;
        this.bought = false;

    }

    public ShoppingCartItem(String name, double pricePerPiece, int pieces, boolean bought) {
        this.name = name;
        this.pricePerPiece = pricePerPiece;
        this.pieces = pieces;
        this.bought = bought;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public double getTotalPrize() {
        return pricePerPiece*pieces;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;

    }
}