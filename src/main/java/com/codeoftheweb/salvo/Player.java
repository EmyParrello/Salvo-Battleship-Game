package com.codeoftheweb.salvo;

//Imports
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import sun.security.util.Password;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Entity
public class Player {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long ID;

    private String userName;

    private String password;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch= FetchType.EAGER)
    Set<Score> scores;

    //Constructors
    public Player() {
    }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //Getters and Setters
    public long getID(){return this.ID;}

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword(String password) {return this.password;}

    public void setPassword(String password) {this.password = password;}

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    //Other Methods
    @JsonIgnore
    public List<Game> getGames() {
        return this.gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    public Score getScore(Game game){
    return this.scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }
}
