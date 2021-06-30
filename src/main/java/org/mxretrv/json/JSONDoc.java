package org.mxretrv.json;

import java.io.Serializable;

public class JSONDoc implements Serializable {

    /**
     * JSON content
     */
    private String jsonContent;

    public JSONDoc(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    @Override
    public String toString() {
        return "JSONDoc{" +
                "jsonContent='" + jsonContent + '\'' +
                '}';
    }
}
