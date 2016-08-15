<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function () {
    $('#daily').highcharts({
        title: {
            text: 'Daily Activity',
            x: -20 //center
        },
        subtitle: {
            text: '<c:out value="${warehouse.name}"></c:out>',
            x: -20
        },
        xAxis: {
            categories: [<c:forEach var="dailyNumber" items="${dailyNumber}">
            '<c:out value="${ dailyNumber.date}"></c:out>',
            </c:forEach>]
        },
        yAxis: {
            title: {
                text: 'Count'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: ''
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Total Returns Received',color:'red',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                    <c:out value="${ dailyNumber.totalReceived}"></c:out>,
                    </c:forEach>]
        }, {
            name: 'Total QC',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalQc}"></c:out>,
                        </c:forEach>]
        } , {
            name: 'Total Putaway',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalPutaway}"></c:out>,
                        </c:forEach>]
        } , {
            name: 'Total Dispatch',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                    <c:out value="${ dailyNumber.totalDispatch}"></c:out>,
                    </c:forEach>]
        } , {
            name: 'Total Gatepass',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalGatepass}"></c:out>,
                        </c:forEach>]
        }]
    });
    $('#dailyModal').highcharts({
         title: {
             text: 'Daily Activity',
             x: -20 //center
         },
         subtitle: {
             text: '<c:out value="${warehouse.name}"></c:out>',
             x: -20
         },
        xAxis: {
            categories: [<c:forEach var="dailyNumber" items="${dailyNumber}">
            '<c:out value="${ dailyNumber.date}"></c:out>',
            </c:forEach>]
        },
        yAxis: {
            title: {
                text: 'Count'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: ''
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Total Returns Received',color:'red',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                    <c:out value="${ dailyNumber.totalReceived}"></c:out>,
                    </c:forEach>]
        }, {
            name: 'Total QC',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalQc}"></c:out>,
                        </c:forEach>]
        } , {
            name: 'Total Putaway',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalPutaway}"></c:out>,
                        </c:forEach>]
        } , {
            name: 'Total Dispatch',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                    <c:out value="${ dailyNumber.totalDispatch}"></c:out>,
                    </c:forEach>]
        } , {
            name: 'Total Gatepass',
            data: [<c:forEach var="dailyNumber" items="${dailyNumber}">
                        <c:out value="${ dailyNumber.totalGatepass}"></c:out>,
                        </c:forEach>]
        }]
    });
});

</script>