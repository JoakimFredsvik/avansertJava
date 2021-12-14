package no.kristiania.dao.entity;

public class Answer implements Comparable<Answer> {
    private Question question;
    private long id;
    private long questionId;
    private long userId;
    private String username;
    private String questionnaireName;
    private String answerText;
    private String questionText;

    public Answer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<tr>");
        buffer.append("<td>").append(questionnaireName).append("</td>");
        buffer.append("<td>").append(questionText).append("</td>");
        buffer.append("<td>").append(answerText).append("</td>");
        buffer.append("<td>").append(username).append("</td>");
        buffer.append("</tr>");

        return buffer.toString();
    }

    @Override
    public int compareTo(Answer o) {
        if (id == o.getId()) {
            return 0;
        } else if (id > o.getId()) {
            return 1;
        } else return -1;
    }
}


