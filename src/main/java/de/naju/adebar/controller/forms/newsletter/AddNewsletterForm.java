package de.naju.adebar.controller.forms.newsletter;

/**
 * @author Rico Bergmann
 */
public class AddNewsletterForm {
    public enum Belonging {NONE, CHAPTER, EVENT, PROJECT}

    private String name;
    private Belonging belonging;
    private long localGroup;

    public AddNewsletterForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelonging() {
        return belonging.toString();
    }

    public void setBelonging(String belonging) {
        this.belonging = Belonging.valueOf(belonging);
    }

    public long getLocalGroup() {
        return localGroup;
    }

    public void setLocalGroup(long localGroup) {
        this.localGroup = localGroup;
    }

    public boolean belongsToChapter() {
        return belonging == Belonging.CHAPTER;
    }
}
