package tuan.aprotrain.projectpetcare.entity;

public class Image {
    private long imageId;
    private String fileName;
    private String url;

    public Image(){}

    public Image(long imageId, String fileName, String url) {
        this.imageId = imageId;
        this.fileName = fileName;
        this.url = url;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
