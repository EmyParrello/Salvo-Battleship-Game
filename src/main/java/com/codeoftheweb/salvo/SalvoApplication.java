package com.codeoftheweb.salvo;

//Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;


//Application (CommandLineRunner) - run/create data when the application is started.
@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {

        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //encrypts passwords. If the database is hacked, it won't show the real passwords, but a 60-character encoded string,
                                              //prefixed by the encryption algorithm used.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean //Bean annotation: calls methods at startup time, saves instances for autowired injection.
    public CommandLineRunner initData(PasswordEncoder passwordEncoder, PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> { //CommandLineRunner: functional interface with just one method.
                          // initData: creates and returns a lambda expression.

            //---Create Players---
            Player p1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
            playerRepository.save(p1);
            Player p2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
            playerRepository.save(p2);
            Player p3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
            playerRepository.save(p3);
            Player p4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
            playerRepository.save(p4);


            //---Create Games---
            Game g1 = new Game();
            gameRepository.save(g1);
            Game g2 = new Game();
            gameRepository.save(g2);
            Game g3 = new Game();
            gameRepository.save(g3);
            Game g4 = new Game();
            gameRepository.save(g4);
            Game g5 = new Game();
            gameRepository.save(g5);
            Game g6 = new Game();
            gameRepository.save(g6);
            Game g7 = new Game();
            gameRepository.save(g7);
            Game g8 = new Game();
            gameRepository.save(g8);


            //---Create GamePlayers---
            //Game 1
            GamePlayer gp1 = new GamePlayer(g1, p1);
            gamePlayerRepository.save(gp1);
            GamePlayer gp2 = new GamePlayer(g1, p2);
            gamePlayerRepository.save(gp2);

            //Game 2
            GamePlayer gp3 = new GamePlayer(g2, p1);
            gamePlayerRepository.save(gp3);
            GamePlayer gp4 = new GamePlayer(g2, p2);
            gamePlayerRepository.save(gp4);

            //Game 3
            GamePlayer gp5 = new GamePlayer(g3, p2);
            gamePlayerRepository.save(gp5);
            GamePlayer gp6 = new GamePlayer(g3, p4);
            gamePlayerRepository.save(gp6);

            //Game 4
            GamePlayer gp7 = new GamePlayer(g4, p2);
            gamePlayerRepository.save(gp7);
            GamePlayer gp8 = new GamePlayer(g4, p1);
            gamePlayerRepository.save(gp8);

            //Game 5
            GamePlayer gp9 = new GamePlayer(g5, p4);
            gamePlayerRepository.save(gp9);
            GamePlayer gp10 = new GamePlayer(g5, p1);
            gamePlayerRepository.save(gp10);

            //Game 6
            GamePlayer gp11 = new GamePlayer(g6, p3);
            gamePlayerRepository.save(gp11);

            //Game 7
            GamePlayer gp12 = new GamePlayer(g7, p4);
            gamePlayerRepository.save(gp12);

            //Game 8
            GamePlayer gp13 = new GamePlayer(g8, p3);
            gamePlayerRepository.save(gp13);
            GamePlayer gp14 = new GamePlayer(g8, p4);
            gamePlayerRepository.save(gp14);


            //---Create Ships + Add Ships to GamePlayers---
            //Game 1
            Ship s1 = new Ship("Destroyer", Arrays.asList("H2", "H3", "H4"));
            gp1.addShips(s1);
            shipRepository.save(s1);
            Ship s2 = new Ship("Submarine", Arrays.asList("E1", "F1", "G1"));
            gp1.addShips(s2);
            shipRepository.save(s2);
            Ship s3 = new Ship("Patrol Boat", Arrays.asList("B4", "B5"));
            gp1.addShips(s3);
            shipRepository.save(s3);

            Ship s4 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp2.addShips(s4);
            shipRepository.save(s4);
            Ship s5 = new Ship("Patrol Boat", Arrays.asList("F1", "F2"));
            gp2.addShips(s5);
            shipRepository.save(s5);

            //Game 2
            Ship s6 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp3.addShips(s6);
            shipRepository.save(s6);
            Ship s7 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gp3.addShips(s7);
            shipRepository.save(s7);

            Ship s8 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            gp4.addShips(s8);
            shipRepository.save(s8);
            Ship s9 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gp4.addShips(s9);
            shipRepository.save(s9);

            //Game 3
            Ship s10 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp5.addShips(s10);
            shipRepository.save(s10);
            Ship s11 = new Ship ("Patrol Boat", Arrays.asList("C6", "C7"));
            gp5.addShips(s11);
            shipRepository.save(s11);

            Ship s12 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            gp6.addShips(s12);
            shipRepository.save(s12);
            Ship s13 = new Ship ("Patrol Boat", Arrays.asList("G6", "H6"));
            gp6.addShips(s13);
            shipRepository.save(s13);

            //Game 4
            Ship s14 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp7.addShips(s14);
            shipRepository.save(s14);
            Ship s15 = new Ship ("Patrol Boat", Arrays.asList("C6", "C7"));
            gp7.addShips(s15);
            shipRepository.save(s15);

            Ship s16 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            gp8.addShips(s16);
            shipRepository.save(s16);
            Ship s17 = new Ship ("Patrol Boat", Arrays.asList("G6", "H6"));
            gp8.addShips(s17);
            shipRepository.save(s17);

            //Game 5
            Ship s18 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp9.addShips(s18);
            shipRepository.save(s18);
            Ship s19 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gp9.addShips(s19);
            shipRepository.save(s19);

            Ship s20 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            gp10.addShips(s20);
            shipRepository.save(s20);
            Ship s21 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gp10.addShips(s21);
            shipRepository.save(s21);

            //Game 6
            Ship s22 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp11.addShips(s22);
            shipRepository.save(s22);
            Ship s23 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gp11.addShips(s23);
            shipRepository.save(s23);

            //Game 8
            Ship s24 = new Ship ("Destroyer", Arrays.asList("B5", "C5", "D5"));
            gp13.addShips(s24);
            shipRepository.save(s24);
            Ship s25 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gp13.addShips(s25);
            shipRepository.save(s25);

            Ship s26 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            gp14.addShips(s26);
            shipRepository.save(s26);
            Ship s27 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gp14.addShips(s27);
            shipRepository.save(s27);


            //Create Salvos + Add Salvos to Games
            //Game 1
            Salvo sv1 = new Salvo(1, Arrays.asList("B5", "C5", "F1"));
            gp1.addSalvoes(sv1);
            salvoRepository.save(sv1);
            Salvo sv2 = new Salvo(1, Arrays.asList("B4", "B5", "B6"));
            gp2.addSalvoes(sv2);
            salvoRepository.save(sv2);
            Salvo sv3 = new Salvo (2, Arrays.asList("F2", "D5"));
            gp1.addSalvoes(sv3);
            salvoRepository.save(sv3);
            Salvo sv4 = new Salvo (2, Arrays.asList("E1", "H3", "A2"));
            gp2.addSalvoes(sv4);
            salvoRepository.save(sv4);

            //Game 2
            Salvo sv5 = new Salvo (1, Arrays.asList("A2", "A4", "G6"));
            gp3.addSalvoes(sv5);
            salvoRepository.save(sv5);
            Salvo sv6 = new Salvo (1, Arrays.asList("B5", "D5", "C7"));
            gp4.addSalvoes(sv6);
            salvoRepository.save(sv6);
            Salvo sv7 = new Salvo (2, Arrays.asList("A3", "H6"));
            gp3.addSalvoes(sv7);
            salvoRepository.save(sv7);
            Salvo sv8 = new Salvo (2, Arrays.asList("C5", "C6"));
            gp4.addSalvoes(sv8);
            salvoRepository.save(sv8);

            //Game 3
            Salvo sv9 = new Salvo (1, Arrays.asList("G6", "H6", "A4"));
            gp5.addSalvoes(sv9);
            salvoRepository.save(sv9);
            Salvo sv10 = new Salvo (1, Arrays.asList("H1", "H2", "H3"));
            gp6.addSalvoes(sv10);
            salvoRepository.save(sv10);
            Salvo sv11 = new Salvo (2, Arrays.asList("A2", "A3", "D8"));
            gp5.addSalvoes(sv11);
            salvoRepository.save(sv11);
            Salvo sv12 = new Salvo (2, Arrays.asList("E1", "F2", "G3"));
            gp6.addSalvoes(sv12);
            salvoRepository.save(sv12);

            //Game 4
            Salvo sv13 = new Salvo (1, Arrays.asList("A3", "A4", "F7"));
            gp7.addSalvoes(sv13);
            salvoRepository.save(sv13);
            Salvo sv14 = new Salvo (1, Arrays.asList("B5", "C6", "H1"));
            gp8.addSalvoes(sv14);
            salvoRepository.save(sv14);
            Salvo sv15 = new Salvo (2, Arrays.asList("A2", "G6", "H6"));
            gp7.addSalvoes(sv15);
            salvoRepository.save(sv15);
            Salvo sv16 = new Salvo (2, Arrays.asList("C5", "C7", "D5"));
            gp8.addSalvoes(sv16);
            salvoRepository.save(sv16);

            //Game 5
            Salvo sv17 = new Salvo (1, Arrays.asList("A1", "A2", "A3"));
            gp9.addSalvoes(sv17);
            salvoRepository.save(sv17);
            Salvo sv18 = new Salvo (1, Arrays.asList("B5", "B6", "C7"));
            gp10.addSalvoes(sv18);
            salvoRepository.save(sv18);
            Salvo sv19 = new Salvo (2, Arrays.asList("G6", "G7", "G8"));
            gp9.addSalvoes(sv19);
            salvoRepository.save(sv19);
            Salvo sv20 = new Salvo (2, Arrays.asList("C6", "D6", "E6"));
            gp10.addSalvoes(sv20);
            salvoRepository.save(sv20);
            Salvo sv21 = new Salvo (3, Arrays.asList("H1", "H8"));
            gp10.addSalvoes(sv21);
            salvoRepository.save(sv21);


            //---Create Scores---
            //Game 1
            Score sc1 = new Score(g1, p1, 1.0);
            scoreRepository.save(sc1);
            Score sc2 = new Score(g1, p2, 0.0);
            scoreRepository.save(sc2);

            //Game 2
            Score sc3 = new Score(g2, p1, 0.5);
            scoreRepository.save(sc3);
            Score sc4 = new Score(g2, p2, 0.5);
            scoreRepository.save(sc4);

            //Game 3
            Score sc5 = new Score (g3, p2, 1.0);
            scoreRepository.save(sc5);
            Score sc6 = new Score (g3, p4, 0.0);
            scoreRepository.save(sc6);

            //Game 4
            Score sc7 = new Score (g4, p2, 0.5);
            scoreRepository.save(sc7);
            Score sc8 = new Score(g4, p1, 0.5);
            scoreRepository.save(sc8);
        };
    }
}

//Authentication (determines who the User is and its role): This subclass takes the name someone has entered for log in, search the
//database with that name, and return a UserDetails object with name, password, and role information for that user if it exists.
//If not, it returns an error message. It doesn't check if the password is correct; That's handled by Spring Security's User class internally.
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userName-> {
            Player player = playerRepository.findByUserName(userName);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(userName),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + userName);
            }
        });
    }
}

//Authorization (what the User can do): This subclass configures the authorization rules. What different roles can do.
//The matchers are checked in order, so it's important to know what we want to authorize and whom, to put them in the correct order.
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/games.html").permitAll() //everyone can see the Games page.
                .antMatchers("/web/game.html").hasAuthority("USER")//only authenticated USERS can see the Game page.
                .and() //starts a new section of rules. Every section has to succeed, so one of the matchers has to grant access
                      // and form login has to pass, which will be true if the user is already logged in, or successfully logs in.
                .formLogin(); //creates a login controller.

        http.formLogin() //If a user tries to access a URL that requires authentication, a GET for the URL /login is automatically issued.
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginPage("/api/login"); //An HTML login form is generated and returned when GET /login occurs.
                                         //When a user submits that form, it does a POST to /login with the request parameters username and password.
                                        //The user authentication code is used to verify that data. If successful, the user is redirected
                                       //to the original URL. If the data is not valid, a failure HTML page is returned.

        http.logout().logoutUrl("/api/logout"); //logs the user out. Clears any authentication.

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }


}
