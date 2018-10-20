<#include "header.ftl">
<div class="row">
    <div class="col-md-12 mt-1">
        <h1>Import a CSV file</h1>
        <p>
            Select a file and click "Import"
        </p>
        <form class="form-inline" action="/api/import" method="post">
            <div class="form-group">
                <input type="file" class="form-control-file" id="csv" accept=".csv">
            </div>
            <button type="submit" class="btn btn-primary">Import</button>
        </form>
    </div>
</div>

<#include "footer.ftl">