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
        let statistics = JSON.parse(frame.body);
        processData(statistics);
    };

    client.connect({}, function () {
        var subscription = client.subscribe("weather-data-enriched", callback);
    })

    function processData(statistics) {
        statisticsData = statistics;
        let pages = statisticsData.page;
        let labelData = [];
        let tempData = [];
        let vititorData = [];
        pages.forEach(page => {
            let labels = page.labels;
            labels.forEach(lbl => labelData.push(lbl))
            let temps = page.tempData;
            temps.forEach(tmp => tempData.push(tmp));
            let users = page.usersData;
            users.forEach(usrs => vititorData.push(usrs));
        });

        var ctx = document.getElementById('site-chart').getContext('2d');
        var chart = new Chart(ctx, {
            // The type of chart we want to create
            type: 'line',

            // The data for our dataset
            data: {
                labels: labelData,
                datasets: [{
                    label: 'Visits',
                    fill: false,
                    backgroundColor: 'rgb(255, 99, 132)',
                    borderColor: 'rgb(255, 99, 132)',
                    data: vititorData
                },
                    {
                        label: 'Temperature',
                        fill: false,
                        backgroundColor: 'rgb(79, 114, 255)',
                        borderColor: 'rgb(30, 45, 255)',
                        data: tempData
                    }]
            },

            options: {
            }
        });
        document.getElementById("processing").innerText = "Processing complete.";
    }

</script>
<div class="container">