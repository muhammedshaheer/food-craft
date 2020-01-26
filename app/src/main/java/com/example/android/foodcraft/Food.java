package com.example.android.foodcraft;

public class Food {

    private String MenuId,description,image,name,type;

    private Long price;

    public Food()
    {

    }

    public Food(String MenuId, String description, String image, String name, Long price, String type) {
        this.MenuId = MenuId;
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
