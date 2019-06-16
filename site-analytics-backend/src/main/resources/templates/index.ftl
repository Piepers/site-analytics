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
            <span id="processing">Processing your file.</span>
        </#if>
    </div>
    <div class="col-md-12 mt-1">
        <canvas id="site-chart"></canvas>
    </div>
</div>
<div class="row">
    <div class="col-md-12 mt-1">
        <div class="btn-group" role="group" aria-label="Basic example">
            <button type="button" class="btn btn-primary"><< Begin</button>
            <button type="button" class="btn btn-primary">< Previous</button>
            <button type="button" class="btn btn-primary">Next ></button>
            <button type="button" class="btn btn-primary">End >></button>
        </div>
    </div>
</div>

<#include "footer.ftl">