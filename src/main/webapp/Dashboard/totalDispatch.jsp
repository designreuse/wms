<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<script>
$(document).ready(function () {
			  $('#td').highcharts({
			        chart: {
			            type: 'column',
			            zoomType: 'xy'
			        },
			        title: {
			            text: 'Total Dispatch'
			        },
			        subtitle: {
			            text: '<c:out value="${warehouse.name}"></c:out>'
			        },
			        xAxis: {
			        	categories: [<c:forEach var="td" items="${td}">
			            '<c:out value="${ td.date}"></c:out>',
			            </c:forEach>]
			        },
			        yAxis: {
			            min: 0,
			            title: {
			                text: 'Count'
			            },
			            stackLabels: {
			                style: {
			                    color: 'black'
			                },
			                enabled: true,
			                formatter: function () {
			                    return this.total ;
			                }
			            }
			        },
			        tooltip: {
			            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			                '<td style="padding:0"><b>{point.y}</b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true,
			            
			            formatter: function() {
			                var s = '<span style="font-size:12px">'+this.x+'</span><table>',
			                    sum = 0;

			                $.each(this.points, function(i, point) {
			                    s += '<tr><td style="padding:1;font-size:12px">'+ point.series.name +': </td>' +
				                '<td style="padding:1;font-size:10px"><b>'+point.y+'</b></td></tr>';
			                    sum += point.y;
			                });
			                s += '<tr><td style="padding:1;font-size:12px">Total: </td>' +
			                '<td style="padding:1;font-size:12px"><b>'+sum+'</b></td></tr>';
			                return s;
			            }
			        },
			        plotOptions: {
			            column: {
			            	stacking: 'normal',
			                pointPadding: 0.2,
			                borderWidth: 0
			            }
			        },
			        series: [{
			            name: 'Delhivery',color:'red',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.delhivery}"></c:out>,
								</c:forEach>]
			        }, {
			            name: 'Dotzot',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.dotzot}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Red Express',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.redExpress}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Go Javas',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.goJavas}"></c:out>,
								</c:forEach>]
			        } , {
			            name: 'Gati',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.gati}"></c:out>,
								</c:forEach>]
			        } , {
			            name: 'Others',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.others}"></c:out>,
									</c:forEach>]
			        }]
			    }); 
			  
			  $('#tdModal').highcharts({
			        chart: {
			            type: 'column',
			            zoomType: 'xy'
			        },
			        title: {
			            text: 'Total Dispatch'
			        },
			        subtitle: {
			            text: '<c:out value="${warehouse.name}"></c:out>'
			        },
			        xAxis: {
			        	categories: [<c:forEach var="td" items="${td}">
			            '<c:out value="${ td.date}"></c:out>',
			            </c:forEach>]
			        },
			        yAxis: {
			            min: 0,
			            title: {
			                text: 'Count'
			            },
			            stackLabels: {
			                style: {
			                    color: 'black'
			                },
			                enabled: true,
			                formatter: function () {
			                    return this.total ;
			                }
			            }
			        },
			        tooltip: {
			            headerFormat: '<span style="font-size:12px">{point.key}</span><table>',
			            pointFormat: '<tr><td style="color:{series.color};padding:0;font-size:12px">{series.name}: </td>' +
			                '<td style="padding:0;font-size:12px"><b>{point.y}</b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true,
			            
			            formatter: function() {
			                var s = '<span style="font-size:12px">'+this.x+'</span><table>',
			                    sum = 0;

			                $.each(this.points, function(i, point) {
			                    s += '<tr><td style="padding:1;font-size:12px">'+ point.series.name +': </td>' +
				                '<td style="padding:1;font-size:12px"><b>'+point.y+'</b></td></tr>';
			                    sum += point.y;
			                });
			                s += '<tr><td style="padding:1;font-size:12px">Total: </td>' +
			                '<td style="padding:1;font-size:12px"><b>'+sum+'</b></td></tr>';
			                return s;
			            }
			        },
			        plotOptions: {
			            column: {
			            	stacking: 'normal',
			                pointPadding: 0.2,
			                borderWidth: 0
			            }
			        },
			        series: [{
			            name: 'Delhivery',color:'red',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.delhivery}"></c:out>,
								</c:forEach>]
			        }, {
			            name: 'Dotzot',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.dotzot}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Red Express',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.redExpress}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Go Javas',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.goJavas}"></c:out>,
								</c:forEach>]
			        } , {
			            name: 'Gati',
			            data: [<c:forEach var="td" items="${td}">
					            <c:out value="${ td.gati}"></c:out>,
								</c:forEach>]
			        } , {
			            name: 'Others',
			            data: [<c:forEach var="td" items="${td}">
						            <c:out value="${ td.others}"></c:out>,
									</c:forEach>]
			        }]
			    });
		});
</script>





	
