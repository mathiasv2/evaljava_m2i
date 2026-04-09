package edu.ban7.chatbotmsnmsii2527.mock;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockQuestionDao implements QuestionDao {

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<String> listeAdminPseudo() {
        return List.of();
    }

    @Override
    public List<String> listeQuestionsByUser(String author) {
        if (author.equals("a@a")) {
            return List.of("Question 1", "Question 2");
        }
        return List.of();
    }

    @Override
    public List<String> toutesLesQuestion() {
        return List.of("Question 1", "Question 2", "Question 3");
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends AppUser> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppUser> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppUser> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AppUser getOne(Integer integer) {
        return null;
    }

    @Override
    public AppUser getById(Integer integer) {
        return null;
    }

    @Override
    public AppUser getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends AppUser> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppUser> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppUser> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppUser> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppUser> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppUser> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppUser, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends AppUser> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AppUser> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<AppUser> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<AppUser> findAll() {
        return List.of();
    }

    @Override
    public List<AppUser> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(AppUser entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppUser> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<AppUser> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppUser> findAll(Pageable pageable) {
        return null;
    }
}