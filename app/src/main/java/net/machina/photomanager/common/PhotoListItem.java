package net.machina.photomanager.common;

public class PhotoListItem {

    private String imageName;
    private String imageCreationDate;

    public PhotoListItem(String imageName, String imageCreationDate) {
        this.imageName = imageName;
        this.imageCreationDate = imageCreationDate;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageCreationDate() {
        return imageCreationDate;
    }

    public void setImageCreationDate(String imageCreationDate) {
        this.imageCreationDate = imageCreationDate;
    }
}
