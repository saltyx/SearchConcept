package me.search.concept;

public final class ApplicationContext {

    private transient String currentToken;

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }
}
