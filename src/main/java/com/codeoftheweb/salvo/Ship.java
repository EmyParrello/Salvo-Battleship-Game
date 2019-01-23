package com.codeoftheweb.salvo;

//Imports
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private String Type;

    @ElementCollection
    @Column(name="cell")
    private List<String> Locations = new ArrayList<>();

    //Constructors
    public Ship(){}

    public Ship(String type, List<String> locations) {
        this.Type = type;
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

    public String getType() {
        return this.Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public List<String> getLocations() {
        return this.Locations;
    }

    public void setLocations(List<String> locations) {
        this.Locations = locations;
    }

    //Other Methods
}
