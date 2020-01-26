package com.example.android.foodcraft;

public class Order {

    private int ID;
    private String productId;
    private String ProductName;
    private String Quantity;
    private String price;
    private String image;

    public Order(){

    }

    public Order(String productId, String productName, String quantity, String price,String image) {
        this.productId = productId;
        ProductName = productName;
        Quantity = quantity;
        this.price = price;
        this.image=image;

    }

    public Order(int id, String productId, String productName, String quantity, String price, String image) {
        ID = id;
        this.productId = productId;
        ProductName = productName;
        Quantity = quantity;
        this.price = price;
        this.image = image;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
