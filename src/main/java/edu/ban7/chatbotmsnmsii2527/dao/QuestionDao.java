package edu.ban7.chatbotmsnmsii2527.dao;

import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionDao extends JpaRepository<AppUser,Integer> {

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u.pseudo FROM AppUser u WHERE u.admin = true")
    List<String> listeAdminPseudo();


}
