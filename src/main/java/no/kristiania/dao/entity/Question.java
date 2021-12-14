package no.kristiania.dao.entity;

import java.util.ArrayList;
import java.util.Objects;

public class Question implements Comparable<Question> {
    private String questionText;
    private long id;
    private ArrayList<Alternative> alternatives = new ArrayList<>();
    private long questionnaireId;
    private boolean editable = false;

    public Question() {
    }

    public Question(String questionText, long questionnaireId) {
        this.questionText = questionText;
        this.questionnaireId = questionnaireId;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        this.alternatives.forEach(e -> e.setEditable(true));
    }

    public void setQuestionnaireId(long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlternatives(ArrayList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (!isEditable()) {
            getAnswerHtml(buffer);
        } else {
            getEditHtml(buffer);
        }

        return buffer.toString();
    }

    private void getAnswerHtml(StringBuilder buffer) {
        buffer.append("<label for=\"question" + id + "\">");
        buffer.append(questionText);

        if (!(alternatives.size() == 0)) {
            buffer.append("<select name=\"question" + id + "\" >");
            alternatives.forEach(alternative -> {
                buffer.append("<option value=\"" + alternative + "\">");
                buffer.append(alternative + "</option>\n");
            });
            buffer.append("</select>");
        } else {
            buffer.append("<br><textarea name=\"question" + id + "\" required></textarea><br>");
        }

        buffer.append("</label><br>");
    }

    private void getEditHtml(StringBuilder buffer) {
        buffer.append("<br>");
        buffer.append("<form action=\"/api/question\" method=\"post\">");
        buffer.append("<input name=\"text\" value=\"" + this.questionText + "\">");
        buffer.append("<input name=\"type\" value=\"update\" hidden>");
        buffer.append("<input name=\"id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input type=\"submit\" value=\"Change text\">");
        buffer.append("</form>");
        alternatives.forEach(e -> buffer.append(e.toString()));

        buffer.append("<form action=\"/api/alternative\" method=\"post\">");
        buffer.append("<input name=\"question_id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input name=\"text\" placeholder=\"New alternative..\" required>");
        buffer.append("<input type=\"submit\" value=\"Add alternative\">");
        buffer.append("</form>");
        buffer.append("<form action=\"/api/question\" method=\"post\">");
        buffer.append("<input name=\"type\" value=\"delete\" hidden>");
        buffer.append("<input name=\"id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input type=\"submit\" value=\"Delete question\" class=\"red\">");
        buffer.append("</form>");
        buffer.append("<br>");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionnaireId == question.questionnaireId && questionText.equals(question.questionText) && Objects.equals(alternatives, question.alternatives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, alternatives, questionnaireId);
    }

    @Override
    public int compareTo(Question o) {
        if (id == o.getId()) {
            return 0;
        } else if (id > o.getId()) {
            return 1;
        } else return -1;
    }

    public long getQuestionnaireId() {
        return this.questionnaireId;
    }
}
