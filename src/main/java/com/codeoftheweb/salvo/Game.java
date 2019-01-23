package com.codeoftheweb.salvo;

//Imports
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long ID;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch= FetchType.EAGER)
    Set<Score> scores;

    //Constructors
    public Game() {
        this.creationDate = LocalDateTime.now();
    }

    //Getters and Setters
    public long getID(){return this.ID;}

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return this.gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    //Other Methods
    public List<Player> getPlayers() {
        return this.gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }
}