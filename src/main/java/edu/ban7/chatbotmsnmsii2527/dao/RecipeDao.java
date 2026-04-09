package edu.ban7.chatbotmsnmsii2527.dao;

import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import edu.ban7.chatbotmsnmsii2527.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecipeDao extends JpaRepository<Recipe,Integer> {

    /*
    @Query("SELECT new edu.ban7.chatbotmsnmsii2527.dao.RecipeDao.RecipeCount(r.name, count(*)) " +
            "FROM Recipe r " +
            "JOIN r.tags t")
    RecipeCount[] countTagByRecipe();

    @Query("SELECT count(*) " +
            "FROM Recipe r " +
            "JOIN r.tags t " +
            "WHERE r.name = :name")
    int countTagByRecipeName(@Param("name") String name);

    record RecipeCount (String name, long count){}
    */

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE t.id IN :tagIds")
    List<Recipe> findByAnyTag(@Param("tagIds") List<Integer> tagIds);

    @Query("""
            SELECT r FROM Recipe r
            JOIN r.tags t
            WHERE t.id IN :tagIds
            GROUP BY r
            HAVING COUNT(DISTINCT t.id) = :tagCount
            """)
    List<Recipe> findByAllTags(@Param("tagIds") List<Integer> tagIds,
                               @Param("tagCount") long tagCount);

    @Query("""
            SELECT r FROM Recipe r
            WHERE r NOT IN (
                SELECT r2 FROM Recipe r2 JOIN r2.tags t WHERE t.id IN :tagIds
            )
            """)
    List<Recipe> findExcludingAnyTag(@Param("tagIds") List<Integer> tagIds);
}
