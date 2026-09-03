package com.klu.DTO;

import org.springframework.web.multipart.MultipartFile;

public class AddPhotoViewModel {

    private String name;
    private double price;
    private String category;
    private String subCategory;
    private MultipartFile image;
    private int width;
    private int height;
    private String description;

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getSubCategory() { return subCategory; }
    public MultipartFile getImage() { return image; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getDescription() { return description; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public void setImage(MultipartFile image) { this.image = image; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setDescription(String description) { this.description = description; }
}
