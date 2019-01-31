var app = new Vue({
    el: "#app",
    data: {
        url: new URL(window.location.href),
        gameView: {},
        columns: [],
        rows: [],
        currentPlayer: {},
        opponent: {},
        ships: [],
        salvoes: [],
        shipID: null
    },

    methods: {
        getData: function () {
            fetch("/api/game_view/" + this.url.searchParams.get("gp"))
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    if (data.Error) {
                        alert(data.Error);
                    } else {
                        app.gameView = data;
                        console.log(app.gameView);
                        
                        app.ships = data.ships;
                        app.salvoes = data.salvoes;

                        app.showPlayers();
                        app.showShips();
                        app.showSalvoes();
                    }
                })
        },

        createGrid: function () {
            for (var c = 0; c < 10; c++) {
                this.columns.push(1 + c);
            }
            for (var r = 0; r < 10; r++) {
                this.rows.push(String.fromCharCode(65 + r));
            }
        },

        showPlayers: function () {
            var gamePlayers = this.gameView.gamePlayers;
            if (gamePlayers.length == 2) {
                if (gamePlayers[0].id == this.url.searchParams.get("gp")) {
                    this.currentPlayer = gamePlayers[0].player;
                    this.opponent = gamePlayers[1].player;
                } else {
                    this.currentPlayer = gamePlayers[1].player;
                    this.opponent = gamePlayers[0].player;
                }
            } else {
                this.currentPlayer = gamePlayers[0].player;
                this.opponent.email = "(Waiting for an opponent!)";
                opp = document.getElementById("opponent").classList.add("red");
            }
        },

        showShips: function () {
            for (var s = 0; s < this.ships.length; s++) {
                var locations = this.ships[s].location;
                for (var l = 0; l < locations.length; l++) {
                    var shipLocation = document.getElementById(locations[l]);
                    shipLocation.setAttribute("class", "ship");
                }
            }
        },

        showSalvoes: function () {
            for (var gamePlayer in this.salvoes) {
                var turns = this.salvoes[gamePlayer];
                for (var turn in turns) {
                    var locations = turns[turn];
                    for (var l = 0; l < locations.length; l++) {
                        if (gamePlayer == this.url.searchParams.get("gp")) {
                            var salvoLocation = document.getElementById("s" + locations[l]);
                            salvoLocation.setAttribute("class", "salvo");
                            salvoLocation.innerHTML = turn;
                        } else {
                            var ship = document.getElementsByClassName("ship").namedItem(locations[l]);
                            var hitLocation = document.getElementById(locations[l]);
                            if (ship == hitLocation) {
                                hitLocation.setAttribute("class", "hit");
                                hitLocation.innerHTML = turn;
                            }
                        }
                    }
                }
            }
        },

        placeShips: function () {
            if (this.currentPlayer.id) {
                fetch("/api/games/players/" + this.url.searchParams.get("gp") + "/ships", {
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    method: 'POST',
                    body: JSON.stringify([{
                            "type": "destroyer",
                            "locations": ["A1", "B1", "C1"]
                        },
                        {
                            "type": "patrol boat",
                            "locations": ["H5", "H6"]
                        }
                    ])
                }).then(function (response) {
                    return response.json();

                }).then(function (data) {
                    console.log('Request success: ', data);
                    if (data.Success) {
                        location.reload();
                    } else {
                        alert(data.Error);
                    }

                }).catch(function (error) {
                    console.log('Request failure: ', error);
                });
            }
        },

        allowDrop: function (ev) {
            console.log("ondragover");
            ev.preventDefault();
        },

        drag: function (ev) {
            console.log("drag", ev.target);
            this.shipID = ev.target.id;
        },

        drop: function (ev) {
            console.log("drop", this.shipID);
            console.log(ev.target);
            console.log(ev);
            ev.preventDefault();
            ev.target.appendChild(document.getElementById(this.shipID));
        },

        logOut: function () {
            fetch("/api/logout", {
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                method: 'POST',
            }).then(function (data) {
                console.log('Request success: ', data);
                window.location.replace("games.html");
            }).catch(function (error) {
                console.log('Request failure: ', error);
            });
        }
    },

    created() {
        this.createGrid();
        this.getData();
    }
})