package no.kristiania.dao.entity;

import java.util.Objects;

public class Alternative implements Comparable<Alternative> {
    private String text;
    private long id;
    private long question_id;
    private boolean editable;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    @Override
    public String toString() {
        if (!isEditable()) {
            return this.text;
        } else {
            StringBuilder buffer = new StringBuilder();
            buffer.append("<form action=\"/api/alternative\" method=\"post\">");
            buffer.append("<input name=\"text\" value=\"" + this.text + "\">");
            buffer.append("<input name=\"type\" value=\"update\" hidden>");
            buffer.append("<input name=\"id\" value=\"" + this.id + "\" hidden>");
            buffer.append("<input type=\"submit\" value=\"Update alternative\">");
            buffer.append("</form>");
            return buffer.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alternative that = (Alternative) o;
        return question_id == that.question_id && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, question_id);
    }

    @Override
    public int compareTo(Alternative o) {
        if (id == o.getId()) {
            return 0;
        } else if (id > o.getId()) {
            return 1;
        } else return -1;
    }
}