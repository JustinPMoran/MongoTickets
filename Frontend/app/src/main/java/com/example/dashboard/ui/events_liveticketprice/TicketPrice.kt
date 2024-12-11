package com.example.dashboard.ui.events_liveticketprice;

data class TicketPrice(
    val symbol: String,
    val price: Double,
    val timestamp: Long
)


//<script>
//        var as;
//function connect() {
//    var username = document.getElementsById("username").value;
//    var url = "ws://localhost:8080/chat/" + username;
//
//    ws = new WebSocket(url);
//
//    ws.onmessage = function(event) {
//        var log = document.getElementById("log");
//        log.innerHTML += event.data + "\n";
//    };
//
//    ws.onopen = function(event) {
//        var log = document.getElementById("log");
//        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
//    };
//}
//
//function send() {
//    var content = document.getElementById("msg").value;
//    ws.send(content);
//}
//</script>