package com.codeoftheweb.salvo;

//Imports

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GamePlayer {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_ID")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_ID")
    private Game game;

    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();


    //Constructors
    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = LocalDateTime.now();
    }

    //Getters and Setters
    public long getID() {
        return this.ID;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LocalDateTime getJoinDate() {
        return this.joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Set<Ship> getShips() {
        return this.ships;
    }

    public void addShips(Ship ship) {
        ship.setGamePlayer(this);
        this.ships.add(ship);
    }

    public Set<Salvo> getSalvoes() {
        return this.salvoes;
    }

    public void addSalvoes(Salvo salvo) {
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }

    //Other Methods
    public Score getScore(){
    return this.getPlayer().getScore(this.game);
    }
}

