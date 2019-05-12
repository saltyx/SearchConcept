package me.search.concept.model;

public class BaseConcept {

    private String conceptCode;
    private String conceptName;
    private String updatedTime;

    public BaseConcept(String conceptCode, String conceptName, String updatedTime) {
        this.conceptCode = conceptCode;
        this.conceptName = conceptName;
        this.updatedTime = updatedTime;
    }

    public String getConceptCode() {
        return conceptCode;
    }

    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
