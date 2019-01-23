var app = new Vue({
    el: "#app",
    data: {
        currentPlayer: null,
        emailInput: "",
        passwordInput: "",
        gamesData: [],
        gamePlayers: [],
        players: []
    },

    methods: {
        getData: function () {
            fetch("/api/games")
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    app.currentPlayer = data.player;
                    app.gamesData = data.games;
                    console.log(app.currentPlayer);
                    console.log(app.gamesData);

                    // app.createGameList();
                    app.getGamePlayers();
                    app.getPlayers();

                })
        },

        logIn: function () {
            fetch("/api/login", {
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                method: 'POST',
                body: 'userName=' + this.emailInput + '&password=' + this.passwordInput,
            }).then(function (response) {
                console.log('Request success: ', response);
                if (response.status == 401) {
                    alert("Username or password not valid");
                } else {
                    location.reload();
                }
            }).catch(function (error) {
                console.log('Request failure: ', error);
            });
        },

        signUp: function () {
            fetch("/api/players", {
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                method: 'POST',
                body: 'userName=' + this.emailInput + '&password=' + this.passwordInput,
            }).then(function (response) {
                console.log('Request success: ', response);
                return response.json();
            }).then(function (data) {
                console.log('Request success: ', data);
                if (data.Success) {
                    app.logIn();
                } else {
                    alert(data.Error);
                }
            }).catch(function (error) {
                console.log('Request failure: ', error);
            });
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
                location.reload();
            }).catch(function (error) {
                console.log('Request failure: ', error);
            });
        },

        // createGameList: function () {
        //     for (x = 0; x < this.gamesData.length; x++) {
        //         var gameList = document.createElement("li");
        //         var gameDate = this.gamesData[x].created;

        //         var gamePlayersArray = this.gamesData[x].gamePlayers;
        //         var gameInfo = [gameDate];

        //         for (y = 0; y < gamePlayersArray.length; y++) {
        //             playerEmail = gamePlayersArray[y].player.email;
        //             gameInfo.push(playerEmail);
        //         }
        //         gameInfo = gameInfo.join(", ");
        //         gameList.append(gameInfo);
        //         document.getElementById("gameList").append(gameList);
        //     }
        // },

        getGameCreationDate: function (game) {
            var date = new Date(game.created);
            return date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
        },

        getGamePlayers: function () {
            for (x = 0; x < this.gamesData.length; x++) {
                for (y = 0; y < this.gamesData[x].gamePlayers.length; y++) {
                    this.gamePlayers.push(this.gamesData[x].gamePlayers[y]);
                }
            }
        },

        getPlayers: function () {
            var usernames = [];
            for (x = 0; x < this.gamePlayers.length; x++) {
                if (!usernames.includes(this.gamePlayers[x].player.email)) {
                    usernames.push(this.gamePlayers[x].player.email);
                }
            }
            for (y = 0; y < usernames.length; y++) {
                var player = {
                    username: usernames[y],
                    totalScore: this.totalScore(usernames[y]),
                    totalWins: this.totalWins(usernames[y]),
                    totalLosses: this.totalLosses(usernames[y]),
                    totalTies: this.totalTies(usernames[y]),
                    totalGames: this.totalGames(usernames[y])
                }
                this.players.push(player);
                this.players.sort(function (a, b) {
                    if (a.totalScore > b.totalScore) {
                        return -1;
                    } else if (a.totalScore < b.totalScore) {
                        return 1;
                    } else {
                        if (a.totalWins > b.totalWins) {
                            return -1;
                        } else if (a.totalWins < b.totalWins) {
                            return 1;
                        } else {
                            if (a.totalGames > b.totalGames) {
                                return -1;
                            } else if (a.totalGames < b.totalGames) {
                                return 1;
                            } else {
                                if (a.totalLosses > b.totalLosses) {
                                    return 1;
                                } else if (a.totalLosses < b.totalLosses) {
                                    return -1;
                                }
                            }
                        }
                    }
                });
            }
        },

        enterGameButton: function (game) {
            if (this.currentPlayer !== null) {
                for (var x = 0; x < game.gamePlayers.length; x++) {
                    if (game.gamePlayers[x].player.id == this.currentPlayer.id) {
                        return true;
                    }
                }
            }
        },

        enterGame: function (game) {
            if (this.currentPlayer !== null) {
                for (var x = 0; x < game.gamePlayers.length; x++) {
                    if (this.currentPlayer.id == game.gamePlayers[x].player.id) {
                        return window.location.replace("game.html?gp=" + game.gamePlayers[x].id);
                    }
                }
            }
        },

        newGame: function () {
            if (this.currentPlayer !== null) {
                fetch("/api/games", {
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    method: 'POST',
                }).then(function (response) {
                    return response.json();

                }).then(function (data) {
                    console.log('Request success: ', data);
                    if (data.Success) {
                        var gamePlayerID = data.Success;
                        window.location = 'game.html?gp=' + gamePlayerID;
                    } else {
                        alert(data.Error);
                    }
                }).catch(function (error) {
                    console.log('Request failure: ', error);
                });
            }
        },

        joinGameButton: function (game) {
            if (this.currentPlayer !== null) {
                for (var x = 0; x < game.gamePlayers.length; x++) {
                    if (game.gamePlayers[x].player.id !== this.currentPlayer.id && game.gamePlayers.length < 2) {
                        return true;
                    }
                }
            }
        },

        joinGame: function (game) {
            if (this.currentPlayer !== null) {
                fetch("/api/game/" + game.id + "/players", {
                    credentials: 'include',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    method: 'POST',
                }).then(function (response) {
                    return response.json();

                }).then(function (data) {
                    console.log('Request success: ', data);
                    if (data.Success) {
                        window.location = 'game.html?gp=' + data.Success;
                    } else {
                        alert(data.Error);
                    }
                }).catch(function (error) {
                    console.log('Request failure: ', error);
                });
            }
        },

        totalScore: function (player) {
            var totalScore = 0;
            for (x = 0; x < this.gamePlayers.length; x++) {
                if (player == this.gamePlayers[x].player.email) {
                    totalScore += this.gamePlayers[x].score;
                }
            }
            return totalScore;
        },

        totalWins: function (player) {
            var wins = 0;
            for (x = 0; x < this.gamePlayers.length; x++) {
                if (player == this.gamePlayers[x].player.email) {
                    if (this.gamePlayers[x].score == 1.0) {
                        wins++;
                    }
                }
            }
            return wins;
        },

        totalLosses: function (player) {
            var losses = 0;
            for (x = 0; x < this.gamePlayers.length; x++) {
                if (player == this.gamePlayers[x].player.email) {
                    if (this.gamePlayers[x].score == 0.0) {
                        losses++;
                    }
                }
            }
            return losses;
        },

        totalTies: function (player) {
            var ties = 0;
            for (x = 0; x < this.gamePlayers.length; x++) {
                if (player == this.gamePlayers[x].player.email) {
                    if (this.gamePlayers[x].score == 0.5) {
                        ties++;
                    }
                }
            }
            return ties;
        },

        totalGames: function (player) {
            var games = this.totalWins(player) + this.totalLosses(player) + this.totalTies(player);
            return games;
        }
    },

    created() {
        this.getData();
    }
})