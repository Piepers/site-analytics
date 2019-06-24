<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>
    <script src="js/stomp.min.js"></script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="styles/bootstrap.min.css">
    <link rel="stylesheet" href="styles/bootstrap-grid.min.css">
    <title>${context.title}</title>

</head>
<body>
<script>
    const url = "ws://localhost:8080/stomp";
    const client = Stomp.client(url);

    let statisticsData;
    let chart;
    let clientId = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    let callback = function (frame) {
        console.log("Received: " + JSON.stringify(frame));
        let statistics = JSON.parse(frame.body);
        processData(statistics);
        document.getElementById("processing").innerText = "";
        document.getElementById("button-row").hidden = false;
        document.getElementById("site-chart").hidden = false;
    };

    client.connect({
        'client-id': clientId
    }, function () {
        var subscription = client.subscribe("weather-data-enriched", callback, {id: clientId,
        'selector': "subscription = '" + clientId + "'"});
    })

    function init() {
        let ctx = document.getElementById('site-chart').getContext('2d');
        this.chart = new Chart(ctx, {
            type: 'line',

            data: {},

            options: {}
        });
    }

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

        let chartData = {

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
        }

        this.chart.data = chartData;
        this.chart.update();

        if (statisticsData.sop == statisticsData.startKey) {
            this.toggleButton("begin-button", true);
            this.toggleButton("previous-button", true);
        } else {
            this.toggleButton("begin-button", false);
            this.toggleButton("previous-button", false);
        }

        if (statisticsData.eop == statisticsData.endKey) {
            this.toggleButton("end-button", true);
            this.toggleButton("next-button", true);
        } else {
            this.toggleButton("end-button", false);
            this.toggleButton("next-button", false);
        }
    }

    function reset() {
        this.chart.destroy();
        this.init();

        document.getElementById("button-row").hidden = true;
        document.getElementById("site-chart").hidden = true;

        window.location.href = '/';
    }

    function toggleButton(id, value) {
        if (value) {
            document.getElementById(id).setAttribute("disabled", value);
        } else {
            document.getElementById(id).removeAttribute("disabled");
        }
    }

    function handleBegin() {
        fetch("http://localhost:8080/api/statistics/first")
            .then(response => response.json())
            .then(processData);

    }

    function handleNext() {
        fetch("http://localhost:8080/api/statistics/next")
            .then(response => response.json())
            .then(processData);

    }

    function handlePrevious() {
        fetch("http://localhost:8080/api/statistics/previous")
            .then(response => response.json())
            .then(processData);

    }

    function handleEnd() {
        fetch("http://localhost:8080/api/statistics/last")
            .then(response => response.json())
            .then(processData);
    }

    window.onload = init;
</script>
<div class="container">