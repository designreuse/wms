<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<div id="sidebar-left" class="col-lg-2 col-sm-1 ">
	<div class="sidebar-nav nav-collapse collapse navbar-collapse">
		<ul class="nav main-menu">
			<li><a href="<c:url value="/home"/>"><i class="fa fa-home"></i><span
					class="hidden-sm text"> Home</span> </a>
			</li>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_WAREHOUSE')">
				<li><a href="<c:url value="/RtvRule/view"/>"><i
						class="fa fa-th-list"></i><span class="hidden-sm text">
							Post RTV Rules</span> </a>
				</li>
			</security:authorize>
			<security:authorize access="hasAnyRole('ROLE_ADMIN')">
				<li><a class="dropmenu" href="#"><i class="fa fa-user"></i><span
						class="hidden-sm text"> Users</span> <span class="chevron closed"></span>
				</a>
					<ul>
						<li><a class="submenu" href="<c:url value="/User/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu" href="<c:url value="/User/view" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize access="hasAnyRole('ROLE_ADMIN')">
				<li><a class="dropmenu" href="#"><i class="fa fa-inbox"></i><span
						class="hidden-sm text"> Warehouses</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Warehouse/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Warehouse/view" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-bullhorn"></i><span
						class="hidden-sm text"> Warehouse Boys</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/WarehouseBoy/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/WarehouseBoy/view" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_RULE','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-book"></i><span
						class="hidden-sm text"> Rules</span> <span class="chevron closed"></span>
				</a>
					<ul>
						<li><a class="submenu" href="<c:url value="/Rule/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu" href="<c:url value="/Rule/view" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_RULE','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-briefcase"></i><span
						class="hidden-sm text"> Bulk Rules</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Rule/bulk/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Rule/bulk/view" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
						<li><a class="submenu" href="<c:url value="/Upload/bulk" />"><i
								class="fa fa-upload"></i><span class="hidden-sm text">
									Bulk Upload</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_PUTAWAY','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-eject"></i><span
						class="hidden-sm text">Putaway</span> <span class="chevron closed"></span>
				</a>
					<ul>
						<li><a class="submenu" href="<c:url value="/Putaway/home" />"><i
								class="fa fa-barcode"></i><span class="hidden-sm text">
									Start Putaway</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Putaway/confirmPutaway" />"><i
								class="fa fa-pencil"></i><span class="hidden-sm text">
									Confirm Putaway</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Putaway/searchPutaway" />"><i
								class="fa fa-search"></i><span class="hidden-sm text">
									Search Putaway</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_PUTAWAY','ROLE_WAREHOUSE')">
				<li><a href="<c:url value="/PutawayDirect/home" />"><i class="fa fa-forward"></i><span
						class="hidden-sm text">Direct Dispatch</span> 
				</a>
				</li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_PICKLIST','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-gift"></i><span
						class="hidden-sm text">Picklist</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Picklist/home" />"><i
								class="fa fa-shopping-cart"></i><span class="hidden-sm text">
									Generate Picklist</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Picklist/searchPicklist" />"><i
								class="fa fa-search"></i><span class="hidden-sm text">
									Search Picklist</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_RTV','ROLE_WAREHOUSE')">
				<li><a class="dropmenu" href="#"><i class="fa fa-share"></i><span
						class="hidden-sm text">Returns</span> <span class="chevron closed"></span>
				</a>
					<ul>
						<li><a class="submenu" href="<c:url value="/Rtv/home" />"><i
								class="fa fa-check"></i><span class="hidden-sm text"> By
									Picklist</span> </a>
						</li>
						<li><a class="dropmenu" href="#"><i class="fa fa-user"></i><span
								class="hidden-sm text">Return Manually</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Rtv/homeWarehouse" />"><i
										class="fa fa-retweet"></i><span class="hidden-sm text">
											To Centre</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Rtv/homeVendor" />"><i
										class="fa fa-random"></i><span class="hidden-sm text">
											To Vendor</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Rtv/homeLiquidation" />"><i
										class="fa fa-tint"></i><span class="hidden-sm text">
											To Liquidation</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Rtv/homeCustomer" />"><i
										class="fa fa-user"></i><span class="hidden-sm text">
											To Customer</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Rtv/home3PL" />"><i
										class="fa fa-plane"></i><span class="hidden-sm text">
											To 3PL</span> </a>
								</li>
							</ul></li>
						<li><a class="dropmenu" href="#"><i class="fa fa-search"></i><span
								class="hidden-sm text">Search Sheet</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Rtv/searchSheet" />"><i
										class="fa fa-calendar"></i><span class="hidden-sm text">
											By Date</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Rtv/searchByCRI" />"><i
										class="fa fa-barcode"></i><span class="hidden-sm text">
											By CRI code</span> </a>
								</li>

							</ul></li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_MANIFEST','ROLE_WAREHOUSE')">
					<li><a class="dropmenu" href="#"><i class="fa fa-file"></i><span
						class="hidden-sm text">Manifest</span> <span class="chevron closed"></span></a>
						<ul>
							<li><a href="<c:url value="/Manifest/printByCourier"/>"><i
								class="fa fa-print"></i><span class="hidden-sm text">
									Print</span> </a>
							</li>
							<li><a href="<c:url value="/Manifest/search"/>"><i
								class="fa fa-search"></i><span class="hidden-sm text">
									Search</span> </a>
							</li>
						</ul>
					</li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_PUTAWAY','ROLE_WAREHOUSE')">
				<li><a href="<c:url value="/Relocate/home"/>"><i
						class="fa fa-refresh"></i><span class="hidden-sm text">
							Relocate</span> </a>
				</li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_GATEPASS','ROLE_WAREHOUSE')">
				<li><a href="<c:url value="/Gatepass/home"/>"><i
						class="fa fa-flag"></i><span class="hidden-sm text"> Gate
							Pass</span> </a>
				</li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN')">
				<li><a class="dropmenu" href="#"><i class="fa fa-tags"></i><span
						class="hidden-sm text"> Gate Pass Status</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Gatepass/create" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Gatepass/view" />"><i class="fa fa-edit"></i><span
								class="hidden-sm text"> View/Edit</span> </a>
						</li>
					</ul></li>
			</security:authorize>
			<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_WAREHOUSE')">
						<li><a class="dropmenu" href="#"><i class="fa fa-tint"></i><span
								class="hidden-sm text"> Liquidation Center</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Liquidation/create" />"><i
										class="fa fa-plus"></i><span class="hidden-sm text"> Create</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Liquidation/view" />"><i
										class="fa fa-pencil"></i><span class="hidden-sm text">
											View/Edit</span> </a>
								</li>
							</ul></li>
					</security:authorize>
					
					<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_WAREHOUSE')">
						<li><a class="dropmenu" href="#"><i class="fa fa-plane"></i><span
								class="hidden-sm text"> 3PL Center</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Centre3PL/create" />"><i
										class="fa fa-plus"></i><span class="hidden-sm text"> Create</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Centre3PL/view" />"><i
										class="fa fa-pencil"></i><span class="hidden-sm text">
											View/Edit</span> </a>
								</li>
							</ul></li>
					</security:authorize>
										
			<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_COURIER')">
				<li><a class="dropmenu" href="#"><i class="fa fa-tasks"></i><span
						class="hidden-sm text"> Courier</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Courier/create" />"><i
								class="fa fa-pencil"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Courier/search" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
					</ul></li>
				<li><a class="dropmenu" href="#"><i class="fa fa-envelope"></i><span
						class="hidden-sm text"> Shipping Location</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Courier/shippingLocation" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text"> Add</span>
						</a>
						</li>
					</ul></li>
				<li><a class="dropmenu" href="#"><i class="fa fa-list-alt"></i><span
						class="hidden-sm text"> Tracking Numbers</span> <span
						class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/Courier/trackingNumbers" />"><i
								class="fa fa-plus"></i><span class="hidden-sm text"> Add</span>
						</a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/Courier/trackingNumbers/remove" />"><i
								class="fa fa-times"></i><span class="hidden-sm text">
									Remove</span> </a>
						</li>
					</ul></li>
				<li><a class="dropmenu" href="#"><i
						class="fa fa-map-marker"></i><span class="hidden-sm text">
							ICA Rules</span> <span class="chevron closed"></span> </a>
					<ul>
						<li><a class="submenu"
							href="<c:url value="/ICARules/create" />"><i
								class="fa fa-pencil"></i><span class="hidden-sm text">
									Create</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/ICARules/search" />"><i
								class="fa fa-edit"></i><span class="hidden-sm text">
									View/Edit</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/ICARules/downloadList" />"><i
								class="fa fa-download"></i><span class="hidden-sm text">
									Download List</span> </a>
						</li>
						<li><a class="submenu"
							href="<c:url value="/ICARules/bulkUpload" />"><i
								class="fa fa-upload"></i><span class="hidden-sm text">
									Bulk Upload</span> </a>
						</li>
					</ul></li>
			</security:authorize>
				<li><a class="dropmenu" href="#"><i class="fa fa-envelope"></i><span
						class="hidden-sm text"> Email</span> <span
						class="chevron closed"></span> </a>
					<ul>					
					<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_WAREHOUSE')">
						<li><a class="dropmenu" href="#"><i class="fa fa-user"></i><span
								class="hidden-sm text"> Receiver</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Receiver/add" />"><i
										class="fa fa-plus"></i><span class="hidden-sm text"> Add</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Receiver/search" />"><i
										class="fa fa-pencil"></i><span class="hidden-sm text">
											View/Edit</span> </a>
								</li>
							</ul></li>
					</security:authorize>					
					<security:authorize access="hasAnyRole('ROLE_ADMIN')">
						<li><a class="dropmenu" href="#"><i class="fa fa-file"></i><span
								class="hidden-sm text"> Reports</span> <span
								class="chevron closed"></span> </a>
							<ul>
								<li><a class="submenu"
									href="<c:url value="/Reports/addReportName" />"><i
										class="fa fa-plus"></i><span class="hidden-sm text"> Add
											Report Name</span> </a>
								</li>
								<li><a class="submenu"
									href="<c:url value="/Reports/addEmail" />"><i
										class="fa fa-pencil"></i><span class="hidden-sm text">
											Add/Edit Email</span> </a>
								</li>
							</ul></li>
					</security:authorize>
					</ul>
				</li>
				<security:authorize access="hasAnyRole('ROLE_ADMIN')">
					<li><a class="dropmenu" href="#"><i class="fa fa-cog"></i><span
							class="hidden-sm text"> Property</span> <span
							class="chevron closed"></span> </a>
						<ul>
							<li><a class="submenu"
								href="<c:url value="/Property/addProperty" />"><i
									class="fa fa-plus"></i><span class="hidden-sm text"> Add
										Property</span> </a>
							</li>
							<li><a class="submenu"
								href="<c:url value="/Property/editProperty" />"><i
									class="fa fa-pencil"></i><span class="hidden-sm text">
										Add/Edit Property</span> </a>
							</li>
						</ul></li>
				</security:authorize>
		</ul>
	</div>
	<a href="#" id="main-menu-min" class="full visible-md visible-lg"><i
		class="fa fa-angle-double-left"></i> </a>
</div>
<!-- end: Main Menu -->