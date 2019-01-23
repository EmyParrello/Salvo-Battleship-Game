package com.codeoftheweb.salvo;

//Imports

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



// Rest Controller - defines methods that get (or modify) data from a repository, and return an object (JSON).
@RestController
@RequestMapping("/api")
public class SalvoController {



    //Attributes - (Autowired annotation: gets the instance needed, in this case, the Repositories where the instances are saved)
    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private ShipRepository shipRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    //Methods
    //Function that validates the email address.
    private boolean EmailValidator (String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    //Function that creates and returns a map.
    private Map<String, Object> makeMap(String Key, Object Value){
        Map<String, Object> map=new HashMap<>();
        map.put(Key, Value);
        return map;
    }



    //Request Methods - (RequestMapping: specifies the URL where the data is displayed)
    @RequestMapping("/games") //initial page (games.html)
    public Map<String, Object> getCurrentUser(Authentication authentication) {
       Map<String, Object> currentUser = new LinkedHashMap<>();
       if (userLoggedIn(authentication)) {
           currentUser.put("player", makePlayerDTO(playerRepo.findByUserName(authentication.getName())));
       } else {
           currentUser.put("player", null);
       }
       currentUser.put("games", getGamesList());
       return currentUser;
    }
    //This function returns true (if user is logged in) or false (if not) --> called in "RequestMapping ("/games")"
    private boolean userLoggedIn(Authentication authentication) {
        return !(authentication == null || authentication instanceof AnonymousAuthenticationToken);
    }
    //This function returns a list of Games --> called in "RequestMapping ("/games")"
    private List<Object> getGamesList() {
        return gameRepo.findAll().stream().map(game -> makeGameDTO(game)).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{ID}") //Game pages (game.html)
    public ResponseEntity<Map<String, Object>> getCurrentGamePlayer(@PathVariable long ID, Authentication authentication) {
        GamePlayer currentGamePlayer = gamePlayerRepo.getOne(ID);
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());
        Game currentGame = currentGamePlayer.getGame();
        if (currentGamePlayer.getPlayer().getID() == currentPlayer.getID()){
            Map<String, Object> gameView = makeGameDTO(currentGame);
            gameView.put("ships", currentGamePlayer.getShips().stream().map(ships -> makeShipDTO(ships)).collect(Collectors.toList()));
            gameView.put("salvoes", makeSalvoesDTO(currentGame.getGamePlayers()));
            return new ResponseEntity<>(gameView, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(makeMap("Error", "You are not authorized to see this game!"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST) //creates new Player
    public ResponseEntity<Map<String, Object>> createPlayer(@RequestParam String userName, @RequestParam String password) {
        String usernameTrim = userName.trim();

        if (usernameTrim.contains(" ")) {
            return new ResponseEntity<>(makeMap("Error", "Spaces are not allowed"), HttpStatus.FORBIDDEN);
        }

        if (!usernameTrim.contains("@")) {
            return new ResponseEntity<>(makeMap("Error", "'@' symbol required"), HttpStatus.FORBIDDEN);
        }

        if (StringUtils.countOccurrencesOf(usernameTrim, "@")>1) {
            return new ResponseEntity<>(makeMap("Error", "Only one '@' symbol allowed"), HttpStatus.FORBIDDEN);
        }

        if (!EmailValidator(usernameTrim)) {
            return new ResponseEntity<>(makeMap("Error", "Email address not valid"), HttpStatus.FORBIDDEN);
        }

        if (usernameTrim.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("Error", "No username or password given"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepo.findByUserName(usernameTrim);
        if (player != null) {
            return new ResponseEntity<>(makeMap("Error", "Username already in use"), HttpStatus.CONFLICT);
        }

        playerRepo.save(new Player(usernameTrim, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("Success", "User created"), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST) //creates new Game
    public  ResponseEntity <Map<String, Object>> createGame(Authentication authentication){
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());
        if (userLoggedIn(authentication)){
            Game newGame = gameRepo.save(new Game());
            GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(newGame, currentPlayer));
            return new ResponseEntity<>(makeMap("Success", newGamePlayer.getID()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("Error", "Log In required to create a new Game"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/game/{ID}/players", method = RequestMethod.POST) //creates new Game Player in existing Game (join Game)
    public  ResponseEntity <Map<String, Object>> joinGame(@PathVariable long ID, Authentication authentication){
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());
        Game currentGame = gameRepo.getOne(ID);
        if (userLoggedIn(authentication)){
            if (currentGame == null){
                return new ResponseEntity<>(makeMap("Error", "Game doesn't exist"), HttpStatus.FORBIDDEN);
            } else if (currentGame.getGamePlayers().size() > 1) {
                return new ResponseEntity<>(makeMap("Error", "Game is full"), HttpStatus.FORBIDDEN);
            } else if (currentGame.getGamePlayers().stream().anyMatch(gamePlayer -> gamePlayer.getPlayer().getID() == currentPlayer.getID())){
                return new ResponseEntity<>(makeMap("Error", "You already joined this Game"), HttpStatus.FORBIDDEN);
            } else {
                GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(currentGame, currentPlayer));
                return new ResponseEntity<>(makeMap("Success", newGamePlayer.getID()), HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(makeMap("Error", "Log In required to join a Game"), HttpStatus.UNAUTHORIZED);
        }
    }

@RequestMapping(path = "/games/players/{ID}/ships", method = RequestMethod.POST) //send ships and locations
public ResponseEntity<Map<String, Object>> addShips (@PathVariable long ID, Authentication authentication, @RequestBody List<Ship> ships) {
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());
        GamePlayer currentGamePlayer = gamePlayerRepo.getOne(ID);
        if (userLoggedIn(authentication)) {
            if (currentGamePlayer == null) {
                return new ResponseEntity<>(makeMap("Error", "GamePlayer doesn't exist"), HttpStatus.UNAUTHORIZED);
            } else if (currentGamePlayer.getPlayer().getID() != currentPlayer.getID()) {
                return new ResponseEntity<>(makeMap("Error", "You are not authorized to see this GamePlayer"), HttpStatus.UNAUTHORIZED);
            } else if (currentGamePlayer.getShips().size() > 0){
                return new ResponseEntity<>(makeMap("Error", "You have already placed ships"), HttpStatus.FORBIDDEN);
            } else {
                for (Ship ship : ships){
                    currentGamePlayer.addShips(ship);
                    shipRepo.save(ship);
                }
                return new ResponseEntity<>(makeMap("Success", "Ships placed"), HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(makeMap("Error", "Log In required to add Ships"), HttpStatus.UNAUTHORIZED);
        }
    }




    //DTO Methods - (Data Transfer Object (DTO): Java structure created to organize data for transfer to another system)
    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getID());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> makeGamePlayerDTO(gamePlayers)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayers) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayers.getID());
        dto.put("player", makePlayerDTO(gamePlayers.getPlayer()));
        if (gamePlayers.getScore() == null){
            dto.put("score", gamePlayers.getScore());
        }else{
        dto.put("score", gamePlayers.getScore().getScore());}
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getID());
        dto.put("email", player.getUserName());
        return dto;
    }

    private Map<String, Object> makeShipDTO (Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", ship.getType());
        dto.put("location", ship.getLocations());
        return dto;
    }

    private Map<Long, Object> makeSalvoesDTO (Set<GamePlayer> gamePlayers) {
        Map<Long, Object> dto = new LinkedHashMap<>();
        for (GamePlayer gamePlayer : gamePlayers){ //(loop for gamePlayer in gamePlayers
            dto.put(gamePlayer.getID(), makeSalvoDTO(gamePlayer.getSalvoes()));
        }
        return dto;
    }

    private Map<String, Object> makeSalvoDTO (Set<Salvo> salvoes) {
        Map<String, Object> dto = new LinkedHashMap<>();
        for (Salvo salvo : salvoes) { //loop for salvo in salvoes
            dto.put(salvo.getTurn().toString(), salvo.getLocations());
        }
        return dto;
    }
}



