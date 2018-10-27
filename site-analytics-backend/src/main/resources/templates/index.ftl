<#include "header.ftl">
<div class="row">
    <div class="col-md-12 mt-1">
        <#assign importing=context.importing!false>
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
</div>

<#include "footer.ftl">