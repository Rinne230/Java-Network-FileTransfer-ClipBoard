public class ClipboardData {
    private String clipboardContent;
    private String language;

    public ClipboardData(String clipboardContent, String language) {
        this.clipboardContent = clipboardContent;
        this.language = language;
    }

    public String getClipboardContent() {
        return clipboardContent;
    }

    public void setClipboardContent(String clipboardContent) {
        this.clipboardContent = clipboardContent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
}
