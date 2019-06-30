<#include "header.ftl">
<div class="row">
    <div id="form-upload" class="col align-self-center">
        <h1>Import a CSV file</h1>
        <p>
            Select a file and click "Import"
        </p>
        <form id="upload" class="form-inline">
            <div class="form-group">
                <input type="file" class="form-control-file" id="csv" accept=".csv" name="file"/>
            </div>
            <div class="button">
                <input type="submit" class="btn btn-primary" value="Send"/>
            </div>
        </form>
    </div>
    <div id="status" hidden>
        <span id="processing"></span>
    </div>
    <div class="col-md-12 mt-1">
        <canvas id="site-chart" hidden></canvas>
    </div>
</div>

<div id="button-row" class="row" hidden>
    <div class="col text-center">
        <div class="btn-group" role="group" aria-label="Pagination">
            <button id="begin-button" type="button" class="btn btn-primary" onclick="handleBegin()"><< Begin</button>
            <button id="previous-button" type="button" class="btn btn-primary" onclick="handlePrevious()">< Previous
            </button>
            <button id="back-button" type="button" class="btn btn-primary" onclick="reset()">Back</button>
            <button id="next-button" type="button" class="btn btn-primary" onclick="handleNext()">Next ></button>
            <button id="end-button" type="button" class="btn btn-primary" onclick="handleEnd()">End >></button>
        </div>
    </div>
</div>

<script>
    document.getElementById('begin-button').addEventListener('click', handleBegin);
    document.getElementById('previous-button').addEventListener('click', handlePrevious);
    document.getElementById('back-button').addEventListener('click', reset);
    document.getElementById('next-button').addEventListener('click', handleNext);
    document.getElementById('end-button').addEventListener('click', handleEnd);
    document.getElementById('upload').addEventListener('submit', handleSubmit);


    let statisticsData;
    let chart;

    // let callback = function (frame) {
    //     let statistics = JSON.parse(frame.body);
    //     processData(statistics);
    //     document.getElementById("processing").innerText = "";
    //     document.getElementById("button-row").hidden = false;
    //     document.getElementById("site-chart").hidden = false;
    // };


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

    function handleSubmit(e) {
        e.preventDefault();
        document.getElementById("status").hidden = false;
        document.getElementById("processing").innerText = "Processing your data."
        document.getElementById("form-upload").hidden = true;


        const input = document.getElementById('csv');

        const formData = new FormData();

        formData.append('file', input.files[0]);
        fetch("http://localhost:8080/import", {
            method: 'POST',
            body: formData
        })
            .then((response) => response.json())
            .then((body) => {
                processData(body);
                document.getElementById("processing").innerText = "";
                document.getElementById("status").hidden=true;
                document.getElementById("button-row").hidden = false;
                document.getElementById("site-chart").hidden = false;
            })

    }

    window.onload = init;
</script>
<#include "footer.ftl">