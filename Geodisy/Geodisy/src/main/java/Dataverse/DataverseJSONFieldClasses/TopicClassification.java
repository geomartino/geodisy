package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class TopicClassification extends JSONField{
    private String topicClassValue, topicClassVocab, topicClassVocabURL;

    public TopicClassification() {
        this.topicClassValue = "";
        this.topicClassVocab = "";
        this.topicClassVocabURL = "";
    }

    public String getTopicClassValue() {
        return topicClassValue;
    }

    public void setTopicClassValue(String topicClassValue) {
        this.topicClassValue = topicClassValue;
    }

    public String getTopicClassVocab() {
        return topicClassVocab;
    }

    public void setTopicClassVocab(String topicClassVocab) {
        this.topicClassVocab = topicClassVocab;
    }

    public String getTopicClassVocabURL() {
        return topicClassVocabURL;
    }

    public void setTopicClassVocabURL(String topicClassVocabURL) {
        this.topicClassVocabURL = topicClassVocabURL;
    }

   //TODO
    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("topicClassValue"):
                this.topicClassValue = value;
                break;
            case("topicClassVocab"):
                this.topicClassVocab = value;
                break;
            case("topicClassVocabURL"):
                this.topicClassVocabURL = value;
                break;
            default:
                System.out.println("Something went wrong with Topic Class parsing. Could be URL vs URI vs URl issue");
        }
    }
}