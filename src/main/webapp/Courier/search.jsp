<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="myfn" uri="/WEB-INF/tlds/custom-function.tld"%>
<tags:page title="Search Courier">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    function cancel(){
				window.location = "/Wms/Courier/info?code=${courier.code}";
				return false;
			}
		    
		    $(document).ready(function() {
		    	  
			    // From validation
		        $("#formUpdate").validate({
		        	onkeyup: false,
		        
		            // Specify the validation rules
		            rules: {
		            	name: "required",
		            	primaryEmail: {
		                    required: true,
		                    email: true
		                },
		                maxLoad: "number"
		            },
		            
		            // Specify the validation error messages
		            messages: {
		                name: "Please enter name",
		            	primaryEmail: "Please enter a valid email address",
		            	maxLoad: "Please enter numbers only"
		            }/*,
		            
		            submitHandler: function(form) {
		                form.submit();
		            }*/
		        });
			    
		        $("#formUpdate").submit(function(e){		
                	var shippingMode1 = $('#shippingMode1').is(':checked');
    				var shippingMode2 = $('#shippingMode2').is(':checked');
    				
    				$('#shippingMode1').attr('checked', false);
    				$('#shippingMode2').attr('checked', false);
    				
    				if(shippingMode1 && shippingMode2){
    					$('#shippingMode0').val("Both");
    				}
    				else if(shippingMode1)
    					$('#shippingMode0').val("Air");
    				else if(shippingMode2)
    					$('#shippingMode0').val("Surface");
    				else{
    					alert("Please select a shipping mode");
    					e.preventDefault();
    				}
                });
		        
		     	// activate Nestable for list 1
		        $('#nestable').nestable({
		            group: 1,
		            maxDepth: 1
		        });

		        // activate Nestable for list 2
		        $('#nestable2').nestable({
		            group: 1,
		            maxDepth: 1
		        });
		      });
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Search Courier</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-danger">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${message}
								</div>
							</c:if>
							<form name="courierSearch" action='<c:url value="/Courier/info" />' method="get" id="formSearch">
								<div class="form-group">
								  <label class="col-sm-2 control-label">Courier Code</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="code" name="code" class="form-control">
											<option value="--Select--">--Select--</option>
											<c:forEach var="item" items="${courierCodeList}">
	    		                    		 	<option value=<c:out value="${item}"/>><c:out value="${item}"/></option>
	    		                    		</c:forEach>
										</select>
									</div>	
								  </div>
								</div>
								<div class="form-actions">
									<button type="submit" class="col-sm-offset-2 btn btn-primary">Search</button>
								</div>
							</form>
						</div>
					</div>
					<c:if test="${not empty courier}">
						<div class="box">
							<div class="box-header">
								<h2>Courier Details</h2>
							</div>
							<div class="box-content">
								<c:if test="${not empty messageUpdate}">
									<div class="alert alert-success">
										<button type="button" class="close" data-dismiss="alert">×</button>
										${messageUpdate}
									</div>
								</c:if>
								<form name="courierUpdate" action='<c:url value="/Courier/update" />' method="post" id="formUpdate">
									<div class="form-group hidden">
									  <label class="col-sm-2 control-label">Id</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="id" id="id" value="${courier.id}" readonly>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Name</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="name" id="name" value="${courier.name}" readonly>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Primary Email</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="primaryEmail" id="primaryEmail" value="${courier.primaryEmail}" readonly>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Secondary Email</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="secondaryEmail" id="secondaryEmail" value="${courier.secondaryEmail}">
										</div>	
									  </div>
									</div>									
									<div class="form-group">
									  <label class="col-sm-2 control-label">Score Courier Code</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="scoreCourierCode" id="scoreCourierCode" value="${courier.scoreCourierCode}">
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Soft Data Subject</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="softDataSubject" id="softDataSubject" placeholder="Soft Data Subject" value="${courier.softDataSubject}">
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Soft Data Template</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<textarea class="form-control" rows="3" name="softDataTemplate" id="softDataTemplate" placeholder="Data template">${courier.softDataTemplate}</textarea>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Soft Data Header</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<textarea class="form-control" rows="3" class="form-control" name="softDataHeader" id="softDataHeader" placeholder="Data Header">${courier.softDataHeader}</textarea>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Tracking Link</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="trackingLink" id="trackingLink" value="${courier.trackingLink}">
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Maximum Load</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<c:set var="INT_MAX" value="2147483647"/>
											<c:choose>
												<c:when test="${courier.maxLoad != INT_MAX}">
													<input type="text" class="form-control" name="maxLoad" id="maxLoad" value="${courier.maxLoad}">
												</c:when>
												<c:otherwise>
													<input type="text" class="form-control" name="maxLoad" id="maxLoad" value="">
												</c:otherwise>
											</c:choose>
										</div>	
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-2 control-label">Warehouse</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<select name="warehouse[]" class="form-control" multiple="multiple" data-rel="chosen">
												<c:forEach var="warehouse" items="${warehouseList}">
													<c:set var="found" value="false" />
													<c:forEach var="savedWarehouse" items="${courier.warehouseList}">
														<c:if test="${savedWarehouse.id eq warehouse.id}">
															<option value="${warehouse.id}" selected="selected">${warehouse.name}</option>
															<c:set var="found" value="true"/>
														</c:if>
													</c:forEach>
													<c:if test="${not found}">
															<option value="${warehouse.id}">${warehouse.name}</option>
													</c:if>
													<%--<c:choose>
														<c:when test="${myfn:contains(courier.warehouseList, warehouse)}">
															<option value="${warehouse.id}" selected="selected">${warehouse.name}</option>
														</c:when>
														<c:otherwise>
															<option value="${warehouse.id}">${warehouse.name}</option>
														</c:otherwise>
													</c:choose> --%>
												</c:forEach>
											</select>
											<label>
												(Leave it blank to select <code>All</code>)
											</label>	
										</div>	
									  </div>								
									</div>
									<div class="form-group">
									<label class="col-sm-2 control-label">Advance AWBs</label>
									  	<div class="input-group col-sm-6">
								         	<c:choose>
								         		<c:when test="${courier.advanceAWBs == true }">
								               		<input type="checkbox" name="advanceAWBs" id="advanceAWBs" checked="checked">
								               	</c:when>
								               	<c:otherwise>
								               		<input type="checkbox" name="advanceAWBs" id="advanceAWBs">
								               	</c:otherwise>
								          	</c:choose>
								          </div>
							   </div>
							   <sec:authorize access="hasRole('ROLE_ADMIN')">
									<div class="form-group">
										<label class="col-sm-2 control-label">Enabled</label>
										  	<div class="input-group col-sm-6">
									         	<c:choose>
									         		<c:when test="${courier.enabled == true }">
									               		<input type="checkbox" name="enabled" id="enabled" checked="checked">
									               	</c:when>
									               	<c:otherwise>
									               		<input type="checkbox" name="enabled" id="enabled">
									               	</c:otherwise>
									           	</c:choose>
								             </div>
								   </div>					
								</sec:authorize>
								<div class="form-group">
									<label class="col-sm-2 control-label">Shipping Mode</label>
									  	<div class="input-group col-sm-6">
									  		 <input type="hidden" name="shippingMode" id="shippingMode0">
									  		 <c:choose>
									         	<c:when test="${courier.shippingMode == 'Air' || courier.shippingMode == 'Both' }">
										     		<input type="checkbox" name="shippingMode" id="shippingMode1" checked="checked">Air &nbsp;&nbsp;
										     	</c:when>
										     	<c:otherwise>
										     		<input type="checkbox" name="shippingMode" id="shippingMode1">Air &nbsp;&nbsp;
										     	</c:otherwise>
										     </c:choose>
										     <c:choose>
									         	<c:when test="${courier.shippingMode == 'Surface' || courier.shippingMode == 'Both' }">
										     		<input type="checkbox" name="shippingMode" id="shippingMode2" checked="checked">Surface
										     	</c:when>
										     	<c:otherwise>
										     		<input type="checkbox" name="shippingMode" id="shippingMode2">Surface
										     	</c:otherwise>
										     </c:choose>
								      </div>
							   </div>
									<div class="form-actions" id="btnSubmit">
										<button class="col-sm-offset-2 btn btn-primary">Submit</button>
										<button class="btn btn-primary" onClick="return cancel()">Cancel</button>
									</div>
								</form>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	<!-- Modal -->
	    <div id="button-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	        <div class="modal-dialog modal-lg">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h2 class="modal-title" id="exampleModalLabel">Soft Data Template</h2>
	                    <div class="clearfix">
	                    	<span class="pull-right"><a href="javascript:void(0)" id="next">Skip To Xml&#8594;</a></span>
	                    </div>
	                </div>
	                <div class="modal-body">	
	                	<div class="clearfix">
		                	<div class="dd" id="nestable">
					        	<header>
					        		<strong>Included</strong>
					        		<button id="addColumn" class="btn btn-sm btn-primary pull-right"><i class="fa fa-plus"></i></button>
					        	</header>
					        	<c:choose>
						        	<c:when test="${fn:length(templateMapList) eq 0}">
						        		<div class="dd-empty"></div> 
						        	</c:when>
						        	<c:otherwise>
							            <ol class="dd-list">
								        	<c:forEach var="templateMap" items="${templateMapList}">
												<li class="dd-item dd3-item">
										           	<div class="dd-handle dd3-handle"><i class="fa fa-bars"></i></div>
										           	<div class="dd3-content" data-title="${templateMap.title}" data-map="${templateMap.map}" data-value="${templateMap.value}">${templateMap.title}</div>
										       	</li>
											</c:forEach>
										</ol>	
									</c:otherwise>							
					        	</c:choose>
						    </div>
					        <div class="dd" id="nestable2">
					        	<header>Not Included</header>
					        	<c:if test="${fn:length(templateList) eq 0}">
					        		<div class="dd-empty"></div> 
					        	</c:if>
					            <ol class="dd-list">
									<c:forEach var="template" items="${templateList}">
										<c:set var="found" value="false" />
										<c:forEach var="savedTemplate" items="${templateMapList}">
											<c:if test="${savedTemplate.map eq template}">
												<c:set var="found" value="true"/>
											</c:if>
										</c:forEach>
										<c:if test="${not found}">
											<li class="dd-item dd3-item">
									           	<div class="dd-handle dd3-handle"><i class="fa fa-bars"></i></div>
									           	<div class="dd3-content" data-title="${template.title}" data-map="${template.map}" data-value="${template.value}">${template.title}</div>
									       	</li>
										</c:if>
									</c:forEach>
					            </ol>
					        </div>
				        </div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                    <button type="button" id="generateXmlSubmit" class="btn btn-primary">Generate Xml</button>
	                </div>
	            </div>
	        </div>
	    </div>	    
	    
	    <!-- Add Column Modal -->
	    <div id="add-column-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	        <div class="modal-dialog modal-lg">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h2 class="modal-title" id="exampleModalLabel">Add Column</h2>
	                </div>
	                <div class="modal-body">
	                	<div class="form-group">
	                		<label>Title :</label>
		                	<input type="text" id="columnName" name="columnName" class="form-control" />
		                	<br>
		                	<label>Value :</label>
		                	<input type="text" id="columnValue" name="columnValue" class="form-control" />
	                	</div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" id="addColumnSubmit" class="btn btn-primary" data-dismiss="modal">Submit</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    
	    <!-- Title Modal -->
	    <div id="title-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	        <div class="modal-dialog modal-lg">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h2 class="modal-title" id="exampleModalLabel">Edit Title</h2>
	                </div>
	                <div class="modal-body">
	                	<div class="form-group">
	                		<label id="map">Title:</label>
		                	<input type="text" id="titleOfColumn" name="titleOfColumn" class="form-control" />
		                	<input type="hidden" id="mapHtml"/>
	                	</div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" id="titleSubmit" class="btn btn-primary" data-dismiss="modal">Submit</button>
	                </div>
	            </div>
	        </div>
	    </div>
		
		<!-- Modal -->
	    <div id="editor-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	        <div class="modal-dialog modal-lg">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h2 class="modal-title" id="exampleModalLabel">Soft Data Template</h2>
	                    <a href="javascript:void(0)" id="back">&#8592;Back</a>
	                </div>
	                <div class="modal-body">
	                    <pre>
	                        <div id="editor"></div>
	                    </pre>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                    <button type="button" id="xmlSubmit" class="btn btn-primary" data-dismiss="modal">Submit</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    <script src="<c:url value="/js/src-min-noconflict/ace.js"/>"></script>
	    <script src="<c:url value="/js/src-min-noconflict/ext-language_tools.js"/>"></script>
	    <script>
	        ace.require("ace/ext/language_tools");
	        var editor = ace.edit("editor");
	        editor.setTheme("ace/theme/textmate");
	        editor.getSession().setMode("ace/mode/xml");
	        editor.setReadOnly(true);
	        
	     	// To stop warning message at console log.
	        editor.$blockScrolling = Infinity;
	
	        // enable autocompletion and snippets
	        editor.setOptions({
	            enableBasicAutocompletion: true,
	            enableSnippets: true,
	            enableLiveAutocompletion: true
	        });
	        
	        function modalToggle(){
        	 	$("#editor-modal").addClass('right');
	        	$("#button-modal").addClass('left');
	        	
	        	$("#editor-modal").modal('toggle');
	        	$("#button-modal").modal('toggle');	  
	        	
	        	setTimeout(function(){
	        	 	$("#editor-modal").removeClass('right');
		        	$("#button-modal").removeClass('left');
                }, 200);
	        }
	        
	        $("#softDataTemplate").on("focus",function(){
	        	setTimeout(function(){
                    editor.setValue($('#softDataTemplate').val());
                }, 200);
	        	$("#button-modal").modal('toggle');
	        });
	        
	        $("#next").on("click",function(){
	        	modalToggle();
	        });
	        
	        $("#addColumn").on("click",function(){
	        	if($("#nestable").find(".dd3-content").size() > 0){
		        	$("#button-modal").fadeTo( "slow", 0.70 );
		        	$("#add-column-modal").modal('toggle');
	        	}
	        	else{
	        		alert("Please include at least one field from existing columns.");
	        	}
	        });
	        
	        $("#addColumnSubmit").on("click",function(){
	        	var title = $("#columnName").val().trim();
	        	var value = $("#columnValue").val().trim();
	        	if(title.trim() != ''){
	        		$("#nestable ol").append('<li class="dd-item dd3-item">'
	        		+ '<div class="dd-handle dd3-handle"><i class="fa fa-bars"></i></div>'
	        		+ '<div class="dd3-content" data-title="'+title+'" data-map="'+title+'" data-value="'+value+'">'+title+'</div>'
	        		+ '</li>');
	        	}
	        	$("#columnName").val('');
	        	$("#columnValue").val('');
	        });
	        
	        $("#add-column-modal").on('hidden.bs.modal',function(){
	        	$("#button-modal").fadeTo( "fast", 1.0 );
	        });
	        
	        $("#nestable").on("click",".dd3-item",function(){
	        	var title = $(this).find(".dd3-content").attr("data-title");
	        	var mapHtml = $(this).find(".dd3-content").attr("data-map");
	        	$("#titleOfColumn").val(title);
	        	$("#map").html(mapHtml + " :");
	        	$("#mapHtml").val(mapHtml);
	        	$("#button-modal").fadeTo( "slow", 0.70 );
	            $("#title-modal").modal('toggle');
	        });
	        
	        $("#titleSubmit").on("click",function(){
	        	var title = $("#titleOfColumn").val();
	        	var mapHtml = $("#mapHtml").val();
	        	if(title.trim() != '')
	        		$("div[data-map='"+mapHtml+"']").attr("data-title",title);
	        });
	        
	        $("#title-modal").on('hidden.bs.modal',function(){
	        	$("#button-modal").fadeTo( "fast", 1.0 );
	        });
	        
	        function addTab(count){
	        	var tabSpace = "";
	        	while(count-- > 0)
	        		tabSpace += "    ";
	        	return tabSpace;
	        }
	        
	        $("#generateXmlSubmit").on("click",function(){
	        	var xml = "<Template>\n";
	        	$("#nestable").find(".dd3-content").each(function() {
	        		xml += addTab(1) + "<Column>\n";
	        		xml += addTab(2) + "<Title>" + $( this ).attr("data-title") + "</Title>\n";
	        		xml += addTab(2) + "<Map>" + $( this ).attr("data-map") + "</Map>\n";
	        		xml += addTab(2) + "<Value>" + $( this ).attr("data-value") + "</Value>\n";
	        		xml += addTab(1) + "</Column>\n";
	        	});
	        	xml += "</Template>";
	        	setTimeout(function(){
                    editor.setValue(xml);
                }, 200);
	        	modalToggle();
	        });
	        
	        $("#back").on("click",function(){
	        	modalToggle();
	        });
	        
	        $( "#xmlSubmit" ).click(function() {
                $("#softDataTemplate").val(editor.getValue());
            });
	    </script>
	</jsp:body>
</tags:page>