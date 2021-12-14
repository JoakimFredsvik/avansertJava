package no.kristiania.dao.entity;

import java.util.ArrayList;
import java.util.Objects;

public class Questionnaire implements Comparable<Questionnaire> {
    private long id;
    private String name;
    private ArrayList<Question> questions = new ArrayList<>();
    private boolean editable = false;
    private boolean isShort = false;

    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean aShort) {
        this.isShort = aShort;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        this.questions.forEach(e -> e.setEditable(editable));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        if (isEditable()) {
            getEditHtml(buffer);
        } else if (isShort()) {
            getViewAllHtml(buffer);
        } else {
            getAnswerHtml(buffer);
        }

        return buffer.toString();
    }

    private void getEditHtml(StringBuilder buffer) {
        buffer.append("<form action=\"/api/questionnaire\" method=\"post\">");
        buffer.append("<label for=\"name\">Questionnaire name</label>");
        buffer.append("<input name=\"name\" value=\"" + this.name + "\">");
        buffer.append("<input name=\"id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input name=\"type\" value=\"update\" hidden>");
        buffer.append("<input type=\"submit\" value=\"Change name\">");
        buffer.append("</form>");
        buffer.append("<form action=\"/api/questionnaire\" method=\"post\">");
        buffer.append("<input type=\"text\" name=\"id\" value=\"" + id + "\" hidden>");
        buffer.append("<input type=\"text\" name=\"type\" value=\"delete\" hidden>");
        buffer.append("<input type=\"submit\" value=\"Delete questionnaire\" class=\"red\">");
        buffer.append("</form>");
        questions.forEach(buffer::append);
    }

    private void getAnswerHtml(StringBuilder buffer) {
        buffer.append("<div class=\"questionnaire\">\n");
        buffer.append("<p>").append(name).append("</p>");
        buffer.append("<form action=\"/api/answer\" method=\"post\">");
        buffer.append("<input name=\"user_id\" value=\"\" hidden>");
        questions.forEach(buffer::append);

        buffer.append("<input type=\"submit\" value=\"submit\">");
        buffer.append("</form>");

        buffer.append("</div>");
    }

    private void getViewAllHtml(StringBuilder buffer) {
        buffer.append("<div class=\"questionnaire\">\n");
        buffer.append("<p>").append(name).append("</p>");
        buffer.append("<form action=\"/answer-questionnaire.html?type=id&id=" + this.id + "\" method=\"get\">");
        buffer.append("<input type=\"text\" name=\"id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input type=\"submit\" value=\"answer\">");
        buffer.append("</form>");
        buffer.append("<form action=\"/edit-questionnaire.html?type=id&id=" + this.id + "\" method=\"get\">");
        buffer.append("<input type=\"text\" name=\"id\" value=\"" + this.id + "\" hidden>");
        buffer.append("<input type=\"submit\" value=\"edit\">");
        buffer.append("</form>");
        buffer.append("</div>");
    }

    @Override
    public int compareTo(Questionnaire o) {
        if (id == o.getId()) {
            return 0;
        } else if (id > o.getId()) {
            return 1;
        } else return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return name.equals(that.name) && Objects.equals(questions, that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, questions);
    }
}
