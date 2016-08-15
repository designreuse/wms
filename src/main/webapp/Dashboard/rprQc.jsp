<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="box-body table-responsive">
<div class="text-center" style="font-size: 25px;margin-bottom: 5px">RPR to QC</div>
<div class="text-center" style="font-size: 12px;margin-bottom: 10px"><c:out value="${warehouse.name}"></c:out></div>
						<table id="dTable1" class="table table-bordered table-striped" >
							<thead>
								<tr>
									<th>RPR Date</th>
									<th>Total RPR</th>
									<th>Within 12 Hrs</th>
									<th>Within 24 Hrs</th>
									<th>Within 48 Hrs</th>
									<th>Within 72 Hrs</th>
									<th>>72 Hrs</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="rpr" items="${rpr}">
								<tr>
									<td>${rpr.date}</td>
									<td>${rpr.totalRpr}</td>
									<td>${rpr.hrs12}%</td>
									<td>${rpr.hrs24}%</td>
									<td>${rpr.hrs48}%</td>
									<td>${rpr.hrs72}%</td>
									<td>${rpr.hrs72Plus}%</td>
								</tr>
							</c:forEach>
								
							</tbody>
						</table>
</div>
			<script>
			
			 $(function() {
		            $("#dTable").dataTable({
		            	"bAutoWidth": false
		            });
		            
		        });
			
			
			</script>		
					