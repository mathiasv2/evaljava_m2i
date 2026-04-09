package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.dao.TagDao;
import edu.ban7.chatbotmsnmsii2527.dto.Question;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import edu.ban7.chatbotmsnmsii2527.model.Tag;
import edu.ban7.chatbotmsnmsii2527.security.AppUserDetails;
import edu.ban7.chatbotmsnmsii2527.security.IsAdmin;
import edu.ban7.chatbotmsnmsii2527.security.IsUser;
import edu.ban7.chatbotmsnmsii2527.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    protected final AiService aiService;
    protected final QuestionDao questionDao;

    @PostMapping("/ask")
    @IsUser
    public ResponseEntity<String> ask(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestBody Question question) {

        System.out.println(userDetails.getUser().getPseudo());

        return new ResponseEntity<>(
                aiService.askGemini(question.content(), userDetails.getUser()),
                HttpStatus.OK);
    }


    @GetMapping("/get-my-questions")
    @IsUser
    public ResponseEntity<List<String>> getMyQuestions(
            @AuthenticationPrincipal AppUserDetails userDetails) {

        List<String> questions = questionDao
                .listeQuestionsByUser(userDetails.getUsername());


        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/get-all-questions")
    @IsAdmin
    public ResponseEntity<List<String>> getAllHistory() {
        return new ResponseEntity<>(questionDao.toutesLesQuestion(), HttpStatus.OK);
    }




}
