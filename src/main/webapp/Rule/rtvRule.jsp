<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page>
	<jsp:attribute name="title">Post RTV Rules</jsp:attribute>
	<jsp:attribute name="script">
	<script type="text/javascript">
		$(document).ready(function() {
			if ($("#message").val() != "") {
				noty({
					text : $("#message").val(),
					type : "success",
					layout : "topRight"
				});
			}
		});
	</script>
	</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
		Existing RTV Check Rules
		</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>Rule Name</th>
									<th>Rule Value</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="rule" items="${rules}">
									<tr>
										<td>${rule.name}</td>
										<td>${rule.value}</td>
										<td><c:if test="${rule.priority == 1 || rule.priority == 3}">
												<a href='<c:url value="/RtvRule/edit/${rule.id}"></c:url>'
														class="btn btn-primary">Edit</a>
											</c:if> <c:choose>
												<c:when test="${rule.enabled}">
													<a
															href='<c:url value="/RtvRule/disable/${rule.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a
															href='<c:url value="/RtvRule/enable/${rule.id}"></c:url>'
															class="btn btn-success" style="margin-left: 20px">Enable</a>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</jsp:body>
</tags:page>