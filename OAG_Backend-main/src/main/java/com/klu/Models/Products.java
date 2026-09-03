package com.klu.Models;

import java.util.List;

import com.klu.DTO.OrderDto;

import jakarta.persistence.*;

@Entity
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double price;          // make sure type is double
    private String category;
    private String subCategory;
    private String image;
    private int width;
    private int height;
    private String description;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
	public List<OrderDto> getOrders() {
		// TODO Auto-generated method stub
		return null;
	}
}
