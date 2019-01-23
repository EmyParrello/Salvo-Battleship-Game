package com.codeoftheweb.salvo;

//Imports
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private Integer Turn;

    @ElementCollection
    @Column(name="cell")
    private List<String> Locations = new ArrayList<>();

    //Constructors
    public Salvo() {}

    public Salvo(Integer turn, List<String> locations) {
        this.Turn = turn;
        this.Locations = locations;
    }

    //Getters and Setters

    public long getID() {
        return this.ID;
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {

        this.gamePlayer = gamePlayer;
    }

    public Integer getTurn() {
        return this.Turn;
    }

    public void setTurn(Integer turn) {
        this.Turn = turn;
    }

    public List<String> getLocations() {
        return this.Locations;
    }

    public void setLocations(List<String> locations) {
        this.Locations = locations;
    }

    //Other Methods
}
