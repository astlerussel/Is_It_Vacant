package com.example.isitvacant;

public class ModelReviews  {

    public String name,image,review,rating;

    public ModelReviews() {
    }

    public ModelReviews(String name, String image, String review, String rating) {
        this.name = name;
        this.image = image;
        this.review = review;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

