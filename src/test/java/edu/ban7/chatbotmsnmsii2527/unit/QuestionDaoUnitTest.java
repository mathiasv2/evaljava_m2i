package edu.ban7.chatbotmsnmsii2527.unit;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.mock.MockQuestionDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class QuestionDaoUnitTest {

    @Test
    public void listeQuestionsByUser_existingUser_shouldReturnQuestions() {
        QuestionDao dao = new MockQuestionDao();

        List<String> result = dao.listeQuestionsByUser("a@a");

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Question 1", result.get(0));
    }

    @Test
    public void listeQuestionsByUser_unknownUser_shouldReturnEmptyList() {
        QuestionDao dao = new MockQuestionDao();

        List<String> result = dao.listeQuestionsByUser("unknown");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void toutesLesQuestion_shouldReturnAllQuestions() {
        QuestionDao dao = new MockQuestionDao();

        List<String> result = dao.toutesLesQuestion();

        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void toutesLesQuestion_shouldContainExpectedValues() {
        QuestionDao dao = new MockQuestionDao();

        List<String> result = dao.toutesLesQuestion();

        Assertions.assertTrue(result.contains("Question 2"));
    }
}
