package com.codeoftheweb.salvo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Rest Repository - sends instances of Java classes (stored in JpaRepository) to browsers and other web services in JSON strings,
// and defines URL links to get the JSON data.
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    //Repository Query Methods - finds information from the database and declares it on the repository interface.
    Player findByUserName(@Param("userName") String userName);
}