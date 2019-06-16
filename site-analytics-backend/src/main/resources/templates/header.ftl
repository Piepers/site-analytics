<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>
    <script src="js/stomp.min.js"></script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <title>${context.title}</title>

</head>
<body>
<script>
    const url = "ws://localhost:8080/stomp";
    const client = Stomp.client(url);
    let statisticsData;

    let callback = function (frame) {
        console.log("Received update from backend. Updating table...")
        let data = JSON.parse(frame.body);
        processData(data);
    };

    client.connect({}, function () {
        var subscription = client.subscribe("weather-data-enriched", callback);
        console.log("The subscription id is: " + subscription.id)
    })

    function processData(data) {
        statisticsData = data;
        let d = data.page;
        // let d = data.drafts;
        // document.getElementById("header-td").innerHTML = `<h1>Super 11 Uden Standings</h1>`;
        // Populate the table with the data from the fetch action.
        // let output = "";
        // d.forEach(draft => {
        <#--    output += `<tr><td>${draft.rank}</td><td>${draft.draftName}</td><td>${draft.points}</td><td>${draft.totalPoints}</td></tr>`;-->
        // });

        // document.getElementById("standings-body").innerHTML = output;
        console.log("Data is now: " + statisticsData);
    }

</script>
<div class="container">