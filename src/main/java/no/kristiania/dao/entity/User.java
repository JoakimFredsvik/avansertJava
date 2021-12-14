package no.kristiania.dao.entity;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String name;
    private ArrayList<Questionnaire> answers;
    private long id;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Questionnaire> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Questionnaire> answers) {
        this.answers = answers;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(answers, user.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, answers);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
