package me.search.concept.model;

import javafx.beans.property.SimpleStringProperty;

public final class ConceptTableViewItem {

    private final SimpleStringProperty conceptName = new SimpleStringProperty();
    private final SimpleStringProperty  updatedTime = new SimpleStringProperty();

    public ConceptTableViewItem(String conceptName, String updatedTime) {
        setConceptName(conceptName);
        setUpdatedTime(updatedTime);
    }

    public String getConceptName() {
        return conceptName.get();
    }

    public void setConceptName(String conceptName) {
        this.conceptName.set(conceptName);
    }

    public String getUpdatedTime() {
        return updatedTime.get();
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime.set(updatedTime);

    }
}
