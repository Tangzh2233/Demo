name=${name}
year=${year}
class=${class}
http://www.${domain}.com
<#--list测试-->
<#list namelist as names>
    ${names}
    <#if (names=="yu")>
        哦！原来是老瓢啊！
    </#if>
</#list>
<#--map测试-->
<#list map?keys as key>
    ${map[key]}
</#list>
<#--实体类map属性测试-->
${report.name}
<#list report.map?keys as key>
    ${key}:${report.map[key]}
</#list>
<#--实体类map<String,map>属性测试-->
<#list report.metricMap?keys as key1>
    ${key1}:
    <#list report.metricMap[key1]?keys as key2>
        ${key2}:${report.metricMap[key1][key2]}
    </#list>
</#list>

<#--<table border="1px" width="400px" cellpadding="2" cellspacing="0" style="font-family: '楷体'">
    <tr><th>报警类型</th><th>报警总次数</th><th>报警指标</th><th>报警次数</th></tr>
    <tr><td align="center" rowspan="5">Transaction</td><td rowspan=${report.dtos.size+1} align="center">time</td></tr>
    <#list report.metricMap?keys as k>
        <tr>
            <td></td>
            <td>2</td>
        </tr>

    </#list>
    <tr>
        <td>33333</td>
        <td>1</td>
    </tr>
    <tr>
        <td>33333</td>
        <td>1</td>
    </tr>
    <tr>
        <td>33333</td>
        <td>1</td>
    </tr>
    <tr>
        <td align="center" rowspan="3">Event</td>
        <td align="center" rowspan="3">time</td>
    </tr>
    <tr><td>222222</td><td>1</td></tr>
    <tr><td>222222</td><td>1</td></tr>
    <tr>
        <td align="center" rowspan="1">Metric</td>
        <td align="center" rowspan="1">time</td>
        <!--<td >4444444</td>&ndash;&gt;
    </tr>
</table>-->
