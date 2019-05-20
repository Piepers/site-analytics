<#include "header.ftl">
<div class="row">
    <div class="col-md-12 mt-1">
        <#assign importing=context.session().get("importing")!false>
        <#if importing != true>
            <h1>Import a CSV file</h1>
            <p>
                Select a file and click "Import"
            </p>
            <form class="form-inline" action="/import" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="file" class="form-control-file" id="csv" accept=".csv" name="file"/>
                </div>
                <div class="button">
                    <button type="submit" class="btn btn-primary">Send</button>
                </div>
            </form>
        <#else>
            <p>Processing your file.</p>
        </#if>
    </div>
    <div class="col-md-12 mt-1">
        <canvas id="site-chart"></canvas>
        <script>
            var ctx = document.getElementById('site-chart').getContext('2d');
            var chart = new Chart(ctx, {
                // The type of chart we want to create
                type: 'line',

                // The data for our dataset
                data: {
                    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                    datasets: [{
                        label: 'Visits',
                        fill: false,
                        backgroundColor: 'rgb(255, 99, 132)',
                        borderColor: 'rgb(255, 99, 132)',
                        data: [0, 10, 5, 2, 20, 30, 45]
                    },
                        {
                            label: 'Temperature',
                            fill: false,
                            backgroundColor: 'rgb(79, 114, 255)',
                            borderColor: 'rgb(30, 45, 255)',
                            data: [10, 11, 9, 15, 18, 19, 15]
                        }]
                },

                // Configuration options go here
                options: {}
            });
        </script>
    </div>
</div>

<#include "footer.ftl">