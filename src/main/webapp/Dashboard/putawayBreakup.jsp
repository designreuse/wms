<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<script>
$(document).ready(function () {
			  $('#putaway').highcharts({
			        chart: {
			            type: 'column',
			            zoomType: 'xy'
			        },
			        title: {
			            text:  'Putaway Breakup',
			        },
			        subtitle: {
			            text: '<c:out value="${warehouse.name}"></c:out>'
			        },
			        xAxis:{
			        	categories: [<c:forEach var="pb" items="${putawayBreakup}">
			            '<c:out value="${ pb.date}"></c:out>',
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
			            name: 'RTV Eligible',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.rtvEligible}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Soi Non Faulty',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.soiNonFaulty}"></c:out>,
									</c:forEach>]
			        }, {
			            name: '42 Days Hold',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.daysHold}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Courier Debit',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
					            <c:out value="${ pb.courierDebit}"></c:out>,
								</c:forEach>]
			        }, {
			            name: '3PL',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.pl}"></c:out>,
									</c:forEach>]
			        }, {
			            name: 'QC Rejected',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.qcRejected}"></c:out>,
									</c:forEach>]
			        }, {
			            name: 'Others',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.others}"></c:out>,
									</c:forEach>]
			        }]
			    }); 
			  
			  $('#putawayModal').highcharts({
			        chart: {
			            type: 'column',
			            zoomType: 'xy'
			        },
			        title: {
			            text:'Putaway Breakup',
			        },
			        subtitle: {
			            text: '<c:out value="${warehouse.name}"></c:out>'
			        },
			        xAxis:  {
			        	categories: [<c:forEach var="pb" items="${putawayBreakup}">
			            '<c:out value="${ pb.date}"></c:out>',
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
			            name: 'RTV Eligible',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.rtvEligible}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Soi Non Faulty',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.soiNonFaulty}"></c:out>,
									</c:forEach>]
			        }, {
			            name: '42 Days Hold',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.daysHold}"></c:out>,
									</c:forEach>]
			        } , {
			            name: 'Courier Debit',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
					            <c:out value="${ pb.courierDebit}"></c:out>,
								</c:forEach>]
			        }, {
			            name: '3PL',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.pl}"></c:out>,
									</c:forEach>]
			        }, {
			            name: 'QC Rejected',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.qcRejected}"></c:out>,
									</c:forEach>]
			        }, {
			            name: 'Others',
			            data: [<c:forEach var="pb" items="${putawayBreakup}">
						            <c:out value="${ pb.others}"></c:out>,
									</c:forEach>]
			        }]
			    });
		});
</script>





	
