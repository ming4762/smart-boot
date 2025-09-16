<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8" />
    <title>云付跳转</title>

    <script>
        function submitForm() {
            document.getElementById('payForm').submit();
        }
    </script>
</head>
<body onload="submitForm()">
    <form id="payForm" action="${submitUrl}" method="post">
        <#list parameterList as parameter>
        <input type="hidden" name="${parameter.name}" value="${parameter.value}" />
        </#list>
    </form>
</body>
</html>
