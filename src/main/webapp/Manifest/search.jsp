<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:page title="Returns">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		//Date Picker
		 $('.date-picker').datepicker({
			 autoclose: true,
			 endDate: new Date()
		});
		 
		 //form validationr
		 $("#form").validate({
        
            // Specify the validation rules
            rules: {
            	startDate: "required",
            	endDate: { 
            		required: true,
            		greaterThanEqual: "#startDate",
            		differenceSevenDays: "#startDate"
            	}
            },
            
            // Specify the validation error messages
            messages: {
                startDate: "Please select a valid start date",
                endDate:{
                	required : "Please select a valid end date"
                }
            }/* ,
            
            submitHandler: function(form) {
               form.submit();
           } */
		 });
		 
		 $('#form').on('submit', function(form){
			 	var date = $("#endDate").val();
				if(date != "") {
					var dateTime = date.concat(" 23:59:59");
					$("#endDate").val(dateTime);	
				}
			 	form.submit();
		  });
		 
	});
	
	 //Check greater than validation for dates.
	 jQuery.validator.addMethod("greaterThanEqual", 
			 function(value, element, params) {
	    		 
				if(value != ""){
					
		    		 var arrStartDate = $(params).val().split("/");
		    		 var startDate = new Date(arrStartDate[2], arrStartDate[0], arrStartDate[1]);
		    		 var arrEndDate = value.split("/");
		    		 var endDate = new Date(arrEndDate[2], arrEndDate[0], arrEndDate[1]);
					return endDate >= startDate;
				}
				else
					return true;
			 },'End date must be greater than start date.');
	 
	//Check 7 days difference
	 jQuery.validator.addMethod("differenceSevenDays", 
			 function(value, element, params) {
	    		 
				if(value != ""){
		    		 var arrStartDate = $(params).val().split("/");
		    		 var startDate = new Date(arrStartDate[2],  arrStartDate[0], arrStartDate[1]);
		    		 var arrEndDate = value.split("/");
		    		 var endDate = new Date(arrEndDate[2], arrEndDate[0], arrEndDate[1]);
		    		 startDate.setDate(startDate.getDate() + 7);
					 
		    		 return endDate <= startDate;
				}
				else
					return true;
			 },'Difference Between start date and end Date can\'t be greater than 7 days.');	
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Search Manifest</div>
			<div class="box-content">
				<c:if test="${not empty message}">
							<div class="alert alert-danger">
								<button type="button" class="close" data-dismiss="alert">×</button>
								${message}
							</div>
				</c:if>
				<div class="row" style="margin-top: 20px; margin-bottom: 20px">
					<form action='<c:url value="/Manifest/searchManifest"/>' name="search" method="post" id="form">
						<div class="col-lg-12">
							<div class="col-lg-4">
								<label>Start Date : </label>
								<div class="input-group date col-lg-12">
									<span class="input-group-addon"><i
										class="fa fa-calendar"></i> </span>
										<input type="text" class="form-control date-picker" id="startDate" name="startDate" placeholder="Select start date">
								</div>
							</div>
							<div class="col-lg-4">
								<label>End Date : </label>
								<div class="input-group date col-lg-12">
									<span class="input-group-addon"><i
										class="fa fa-calendar"></i> </span> <input type="text"
										class="form-control date-picker" name="endDate"
										placeholder="Select end date" id="endDate">
								</div>
							</div>
							<div class="col-lg-4" style="margin-top: 25px; text-align: center;">
								<button id="submit" type="submit" class="btn btn-primary">Submit</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<c:if test="${fn:length(manifestList) gt 0}">
			<div class="box">
				<div class="box-content">
					<div class="row" style="margin-top: 20px; margin-bottom: 20px">
						<div class="col-lg-12">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Manifest Code</th>
										<th>Courier Code</th>
										<th>Created On</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${manifestList}" varStatus="i">
										<tr>
											<td>Manifest-${item.id}</td>
											<td>${item.courierCode}</td>
											<td><fmt:formatDate value="${item.created}" type="both"
													dateStyle="long" timeStyle="medium" /></td>
											<td><a href='<c:url value="/Manifest/reprint/${item.id}"/>'
												class="btn btn-primary" target="_blank">Reprint</a>
												<a href='<c:url value="/Manifest/reprintshare/${item.id}"/>'
												class="btn btn-success" target="_blank">Reprint & Share</a>
												</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	</jsp:body>
</tags:page>