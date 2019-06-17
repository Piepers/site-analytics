<#include "header.ftl">
<div class="row">
    <div class="col align-self-center">
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
        <canvas id="site-chart" hidden></canvas>
    </div>
</div>
<div id="button-row" class="row" hidden>
    <div class="col text-center">
        <div class="btn-group" role="group" aria-label="Pagination">
            <button id="begin-button" type="button" class="btn btn-primary" onclick="handleBegin()"><< Begin</button>
            <button id="previous-button" type="button" class="btn btn-primary" onclick="handlePrevious()">< Previous</button>
            <button id="back" type="button" class="btn btn-primary" onclick="reset()">Back</button>
            <button id="next-button" type="button" class="btn btn-primary" onclick="handleNext()">Next ></button>
            <button id="end-button" type="button" class="btn btn-primary" onclick="handleEnd()">End >></button>
        </div>
    </div>
</div>

<#include "footer.ftl">