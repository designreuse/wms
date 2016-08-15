package com.snapdeal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.snapdeal.bean.CcStatusDto;
import com.snapdeal.bean.IssueCategoryDto;
import com.snapdeal.bean.ProductDetails;
import com.snapdeal.bean.SubCategoryDto;
import com.snapdeal.entity.ReceiverDetail;
import com.snapdeal.library.Util;

public class Dao {

	private DataSource dataSourceCamsDwh;
	private DataSource dataSourceRms;
	private DataSource dataSourceShipping;
	private DataSource dataSourceDwh; 
	private DataSource dataSourceScore;
	private DataSource dataSourceShippingLocal;

	public static final Logger LOGGER = Logger.getLogger(Dao.class);

	public DataSource getDataSourceDwh() {
		return dataSourceDwh;
	}

	public void setDataSourceDwh(DataSource dataSourceDwh) {
		this.dataSourceDwh = dataSourceDwh;
	}

	public DataSource getDataSourceShipping() {
		return dataSourceShipping;
	}

	public void setDataSourceShipping(DataSource dataSourceShipping) {
		this.dataSourceShipping = dataSourceShipping;
	}

	public DataSource getDataSourceRms() {
		return dataSourceRms;
	}

	public void setDataSourceRms(DataSource dataSourceRms) {
		this.dataSourceRms = dataSourceRms;
	}

	public DataSource getDataSourceCamsDwh() {
		return dataSourceCamsDwh;
	}

	public void setDataSourceCamsDwh(DataSource dataSourceCamsDwh) {
		this.dataSourceCamsDwh = dataSourceCamsDwh;
	}

	public void setDataSourceScore(DataSource dataSourceScore) {
		this.dataSourceScore = dataSourceScore;
	}

	public DataSource getDataSourceScore() {
		return dataSourceScore;
	}


	public DataSource getDataSourceShippingLocal() {
		return dataSourceShippingLocal;
	}

	public void setDataSourceShippingLocal(DataSource dataSourceShippingLocal) {
		this.dataSourceShippingLocal = dataSourceShippingLocal;
	}
	
	public List<SubCategoryDto> getSubCategoryList()
	{
		Connection connection = null;
		Statement statement = null;
		List<SubCategoryDto> subCategorieList = null;
		try{
			connection = (Connection) dataSourceCamsDwh.getConnection();
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select DISTINCT url, name from product_category where visible = 1 and parent_category_id IS NOT NULL");
			subCategorieList = new ArrayList<SubCategoryDto>();
			while(resultSet.next()){
				SubCategoryDto subCategory = new SubCategoryDto();
				subCategory.setSubcategoryUrl(resultSet.getString("url"));
				subCategory.setSubcategoryName(resultSet.getString("name"));
				subCategorieList.add(subCategory);
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return subCategorieList;
	}

	public List<CcStatusDto> getCcStatusList()
	{
		Connection connection = null;
		Statement statement = null;
		List<CcStatusDto> ccStatusList = null;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select code, description from cc_status where code not in('DNE','CCREQ')");
			ccStatusList = new ArrayList<CcStatusDto>();
			while(resultSet.next()){
				CcStatusDto ccStatusDto = new CcStatusDto();
				ccStatusDto.setCode(resultSet.getString("code"));
				ccStatusDto.setDescription(resultSet.getString("description"));
				ccStatusList.add(ccStatusDto);
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return ccStatusList;
	}

	public List<IssueCategoryDto> getIssueCategoryList()
	{
		Connection connection = null;
		Statement statement = null;
		List<IssueCategoryDto> issueCategoryList = null;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select code, description from issue_category");
			issueCategoryList = new ArrayList<IssueCategoryDto>();
			while(resultSet.next()){
				IssueCategoryDto issueCategoryDto = new IssueCategoryDto();
				issueCategoryDto.setCode(resultSet.getString("code"));
				issueCategoryDto.setDescription(resultSet.getString("description"));
				issueCategoryList.add(issueCategoryDto);
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			//Handle errors for Class.forName
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return issueCategoryList;
	}

	public String getSellerName(String barcode)
	{
		Connection connection = null;
		Statement statement = null;
		String sellerName = null;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			String query = "select seller_name as seller from customer_returned_item where code = '"+barcode+"'";
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null)
			{
				if(resultSet.next()){
					sellerName = Util.specialTrim(resultSet.getString("seller"));
				}
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return sellerName;
	}

	public ProductDetails getDetails(String barcode)
	{
		Connection connection = null;
		Statement statement = null;
		ProductDetails productDetails = null;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			String query = "select cri.vendor_code as vendor,cri.seller_name as seller,cri.product_name as productName, " +
			"cri.item_price as price, cri.sku_code as sku,cri.order_code, " +
			"cri.fulfillment_model_code as fulfillment_model,cri.physical_custom_details as remarks, " +
			"cri.suborder_code as suborder,cri.supc as supc,cri.code as barcode,cri.weight as weight," + 
			"cri.returns_center_code as rms_center, cri.status_code as cristatus, cc.status_code as ccstatus, " +
			"cc.issue_category_code as issuecategory, cc.zendesk_ticket_code as ticket_id, " + 
			"pd.name as customer_name , qc.qc_status ,qc.post_qc_item_status " +
			"from customer_returned_item cri JOIN customer_complaint cc On cri.cc_id = cc.id " + 
			"JOIN customer_returned_package crp on crp.id = cri.crp_id " +
			"JOIN pickup_detail pd ON pd.id = crp.pickup_detail_id " + 
			"JOIN qc_cri_status qc ON qc.cri_code = cri.code " +
			"where cri.code = '"+barcode+"' and qc.enabled = 1";
		
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null)
			{
				productDetails = new ProductDetails();
				if(resultSet.next()){
					productDetails.setBarcode(Util.specialTrim(resultSet.getString("barcode")));
					productDetails.setCriStatus(Util.specialTrim(resultSet.getString("cristatus")));
					productDetails.setCcStatus(Util.specialTrim(resultSet.getString("ccstatus")));
					productDetails.setVendorCode(Util.specialTrim(resultSet.getString("vendor")));
					productDetails.setSellerName(Util.specialTrim(resultSet.getString("seller")));
					productDetails.setPrice(resultSet.getLong("price"));
					productDetails.setWeight(Util.specialTrim(resultSet.getString("weight")));
					productDetails.setRmsCenter(Util.specialTrim(resultSet.getString("rms_center")));
					productDetails.setFulfillmentModel(Util.specialTrim(resultSet.getString("fulfillment_model")));
					productDetails.setQcRemarks(Util.specialTrim(resultSet.getString("remarks")));
					productDetails.setOrderCode(Util.specialTrim(resultSet.getString("order_code")));
					productDetails.setSuborderCode(Util.specialTrim(resultSet.getString("suborder")));
					productDetails.setSupc(Util.specialTrim(resultSet.getString("supc")));
					productDetails.setSku(Util.specialTrim(resultSet.getString("sku")));
					productDetails.setIssueCategory(Util.specialTrim(resultSet.getString("issuecategory")));
					productDetails.setTicketId(Util.specialTrim(resultSet.getString("ticket_id")));
					productDetails.setProductName(Util.specialTrim(resultSet.getString("productName")));
					productDetails.setCustomerName(Util.specialTrim(resultSet.getString("customer_name")));
					
					productDetails.setQcStatus(Util.specialTrim(resultSet.getString("qc_status")));
					productDetails.setPostQcStatus(Util.specialTrim(resultSet.getString("post_qc_item_status")));		
				}
			}
			resultSet.close();
			connection.close();
			connection = (Connection) dataSourceDwh.getConnection();
			statement = (Statement) connection.createStatement();
			query = "select subcategory_url as subcategory from dwh.d_product where supc= '"+productDetails.getSupc()+"'";
			resultSet = statement.executeQuery(query);
			if(resultSet != null) {
				if(resultSet.next()) {
					productDetails.setSubCategory(Util.specialTrim(resultSet.getString("subcategory")));
				}
			}
			resultSet.close();
			connection.close();
//			connection = (Connection) dataSourceShipping.getConnection();
//			statement = (Statement) connection.createStatement();
//			query = "select sp.created as manifest, sp.tracking_number as awb from shipping_package sp " +
//			"JOIN shipping_order_item soi ON soi.shipping_package_id = sp.id where " +
//			"soi.suborder_code = '"+productDetails.getSuborderCode()+"' "+
//			"and sp.created > '2014-01-01' and soi.created > '2014-01-01'";
//			resultSet = statement.executeQuery(query);
//			
//			if(resultSet != null) {
//			
//				if(resultSet.next()) {
					productDetails.setManifestDate(null);
					productDetails.setForwardAwbNumber(null);
//				}
//			}
//			resultSet.close();
//			connection.close();
			
			/** Get shipping Mode from Score DB **/
			productDetails.setShippingMode(getShippingMode(productDetails.getSupc() , productDetails.getSubCategory()));
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
	
		return productDetails;
	}

	public ReceiverDetail getReceiverDetails(String suborderCode, String vendorCode,String returnType)
	{
		Connection connection = null;
		Statement statement = null;
		ReceiverDetail receiverDetail = null;
		String query = null;
		String fcCode = null;
		
		try{
			
			if(returnType.equals("warehouse"))
			{	connection = (Connection) dataSourceShipping.getConnection();
				statement = (Statement) connection.createStatement();
//				
//				query = "select sd.name as name, sd.email as email, sd.address_line1 as line1, sd.address_line2 as line2, " +
//				"sd.city as city, sd.state as state, sd.pincode as pincode, sd.mobile as contact " +
//				"from shipping_order_item soi JOIN shipping_order so "+
//				"ON soi.shipping_order_id = so.id JOIN fulfillment_centre fc " +
//				"ON so.fp_code = fc.code " +
//				"JOIN shipping_detail sd ON sd.id = fc.primary_address_id " +
//				"where soi.suborder_code = '" +suborderCode+"'";
////				"vfm.enabled = 1 and vfm.fulfillment_type = 'FC_VOI' and vfm.vendor_code = '"+vendorCode+"'";
				
				query ="select fp_code as fcCode from shipping_order_item soi " +
						"JOIN shipping_order so ON soi.shipping_order_id = so.id " +
						"JOIN fulfillment_centre fc ON so.fp_code = fc.code " +
						"WHERE soi.suborder_code = '"+suborderCode+"' ";
				
				ResultSet resultSet = statement.executeQuery(query);
				if(resultSet != null)
				{
					fcCode = resultSet.getString("fcCode");
				}
				resultSet.close();
				connection.close();
				
				connection = (Connection) dataSourceShipping.getConnection();
				statement = (Statement) connection.createStatement();
				
				query = "SELECT fca.name as name,fca.email as email,fca.address_line1 as line1," +
						"fca.address_line2 as line2,fca.city as city,fca.state as state,fca.pincode as pincode,fca.mobile as contact " +
						"FROM fc_address fca JOIN fc_details fcd ON fca.id=fcd.primary_address_id " +
						"WHERE fcd.code='"+fcCode+"'";
				
				
			}else if(returnType.equals("vendor")){
				connection = (Connection) dataSourceShippingLocal.getConnection();
				statement = (Statement) connection.createStatement();
		
				query = "select AES_DECRYPT(email,'725558a2c301795cccf2ed4b3bfd218baf16a6e9c0154c133051c60c5b74c188') as email, address_line1 as line1, address_line2 as line2," +
				"city as city, address_state as state, pin_code as pincode, AES_DECRYPT(mobile,'725558a2c301795cccf2ed4b3bfd218baf16a6e9c0154c133051c60c5b74c188') as contact,name as name " +
				"from vendor_contact where contact_type = 4 and vendor_code = '"+vendorCode+"'";	
			}
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null)
			{
				
	
				if(resultSet.next()){
					
					receiverDetail = new ReceiverDetail();
					receiverDetail.setEmail(resultSet.getString("email"));
					receiverDetail.setAddressLine1(resultSet.getString("line1"));
					receiverDetail.setAddressLine2(resultSet.getString("line2"));
					receiverDetail.setCity(resultSet.getString("city"));
					receiverDetail.setState(resultSet.getString("state"));
					receiverDetail.setPincode(resultSet.getString("pincode"));
					receiverDetail.setContactNumber(resultSet.getString("contact"));
					receiverDetail.setName(resultSet.getString("name"));
					receiverDetail.setVendorCode(vendorCode);
				}
			}
			
			System.out.println(receiverDetail.getAddressLine1());
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return receiverDetail;
	}


	public int getPickupInitiatedBySeller(String vendorCode)
	{
		Connection connection = null;
		Statement statement = null;
		int count = -1;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			String query = "select count(*) as count from customer_returned_item cri join " +
			"customer_returned_package crp ON crp.id = cri.crp_id " +
			"where crp.status_code IN ('RTS','PUC') and cri.vendor_code = '"+vendorCode+"'";
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null)
			{
				if(resultSet.next()){
					count = resultSet.getInt("count");
				}
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return count;
	}
	
	public String getShippingMode(String supc, String subCategory){
		Connection connection = null;
		Statement statement = null;
		String shippingMode = null;
		try{
			connection = (Connection) dataSourceScore.getConnection();
			statement = (Statement) connection.createStatement();
			
			/** Get shipping_mode from supc_shipping_mode_mapping **/
			String query = "(select shipping_mode_code as shipping_mode from supc_shipping_mode_mapping where supc ='" + supc + "'" +
					" and enabled = 1) union (select shipping_mode_code as shipping_mode from category_shipping_mode_mapping" +
					" where category_url = '" + subCategory + "' and enabled = 1) limit 1";
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null){
				if(resultSet.next()){
					shippingMode = resultSet.getString("shipping_mode");
				}
			}
			resultSet.close();
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		
		return shippingMode;
	}
	
	//Suryansh

	public ReceiverDetail getCustomerAddressDetail(String inventoryCri) {
		Connection connection = null;
		Statement statement = null;
		ReceiverDetail receiverDetail=new ReceiverDetail();
		List<Integer> pickupId = new ArrayList<Integer>();
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			String query1 = "SELECT pickup_detail_id FROM customer_returned_package crp JOIN customer_returned_item cri WHERE crp.id=cri.crp_id AND cri.code IN "+inventoryCri+";";
			ResultSet resultSet = statement.executeQuery(query1);
			//String pickupIds="(";
			 
			if(resultSet != null)
			{
				while(resultSet.next()){
					pickupId.add(resultSet.getInt("pickup_detail_id"));
				}		
			}
			resultSet.close();
			
//			StringBuilder pickupIdList = new StringBuilder();
//			for(Integer i : pickupId)
//			{
//				pickupIdList.append(","+i);
//			}
//			pickupIdList.setCharAt(pickupIdList.length()-1, ' ');
			String query2="SELECT * from pickup_detail where id IN ("+pickupId.get(0)+")";
			ResultSet resultSet2 = statement.executeQuery(query2);
			if(resultSet2!=null)
			{
				while(resultSet2.next()){
					receiverDetail.setVendorCode(null);
					receiverDetail.setName(resultSet2.getString("name"));
					receiverDetail.setEmail(resultSet2.getString("email"));
					receiverDetail.setAddressLine1(resultSet2.getString("address_line_1"));
					receiverDetail.setAddressLine2(resultSet2.getString("address_line_2"));
					receiverDetail.setCity(resultSet2.getString("city"));
					receiverDetail.setState(resultSet2.getString("state_code"));
					receiverDetail.setPincode(resultSet2.getString("pincode"));
					receiverDetail.setContactNumber(resultSet2.getString("mobile"));
				}
			}
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return receiverDetail;

	}
	
	/** For Direct Putaway **/
	public ProductDetails getProductDetails(String barcode)
	{
		Connection connection = null;
		Statement statement = null;
		ProductDetails productDetails = null;
		try{
			connection = (Connection) dataSourceRms.getConnection();
			statement = (Statement) connection.createStatement();
			String query = "select cri.vendor_code as vendor,cri.seller_name as seller,cri.product_name as productName, " +
			"cri.item_price as price, cri.sku_code as sku,cri.order_code, " +
			"cri.fulfillment_model_code as fulfillment_model,cri.physical_custom_details as remarks, " +
			"cri.suborder_code as suborder,cri.supc as supc,cri.code as barcode,cri.weight as weight," + 
			"cri.returns_center_code as rms_center, cri.status_code as cristatus, cc.status_code as ccstatus, " +
			"cc.issue_category_code as issuecategory, cc.zendesk_ticket_code as ticket_id, " + 
			"pd.name as customer_name , qc.qc_status ,qc.post_qc_item_status " +
			"from customer_returned_item cri JOIN customer_complaint cc On cri.cc_id = cc.id " + 
			"JOIN customer_returned_package crp on crp.id = cri.crp_id " +
			"JOIN pickup_detail pd ON pd.id = crp.pickup_detail_id " +
			"JOIN qc_cri_status qc ON qc.cri_code = cri.code " +
			"where cri.code = '"+barcode+"' and qc.enabled = 1";
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null)
			{
				productDetails = new ProductDetails();
				if(resultSet.next()){
					productDetails.setBarcode(Util.specialTrim(resultSet.getString("barcode")));
					productDetails.setCriStatus(Util.specialTrim(resultSet.getString("cristatus")));
					productDetails.setCcStatus(Util.specialTrim(resultSet.getString("ccstatus")));
					productDetails.setVendorCode(Util.specialTrim(resultSet.getString("vendor")));
					productDetails.setSellerName(Util.specialTrim(resultSet.getString("seller")));
					productDetails.setPrice(resultSet.getLong("price"));
					productDetails.setWeight(Util.specialTrim(resultSet.getString("weight")));
					productDetails.setRmsCenter(Util.specialTrim(resultSet.getString("rms_center")));
					productDetails.setFulfillmentModel(Util.specialTrim(resultSet.getString("fulfillment_model")));
					productDetails.setQcRemarks(Util.specialTrim(resultSet.getString("remarks")));
					productDetails.setOrderCode(Util.specialTrim(resultSet.getString("order_code")));
					productDetails.setSuborderCode(Util.specialTrim(resultSet.getString("suborder")));
					productDetails.setSupc(Util.specialTrim(resultSet.getString("supc")));
					productDetails.setSku(Util.specialTrim(resultSet.getString("sku")));
					productDetails.setIssueCategory(Util.specialTrim(resultSet.getString("issuecategory")));
					productDetails.setTicketId(Util.specialTrim(resultSet.getString("ticket_id")));
					productDetails.setProductName(Util.specialTrim(resultSet.getString("productName")));
					productDetails.setCustomerName(Util.specialTrim(resultSet.getString("customer_name")));
					
					productDetails.setQcStatus(Util.specialTrim(resultSet.getString("qc_status")));
					productDetails.setPostQcStatus(Util.specialTrim(resultSet.getString("post_qc_item_status")));
				}
			}
			resultSet.close();
			connection.close();
			connection = (Connection) dataSourceDwh.getConnection();
			statement = (Statement) connection.createStatement();
			query = "select subcategory_url as subcategory from dwh.d_product where supc= '"+productDetails.getSupc()+"'";
			resultSet = statement.executeQuery(query);
			if(resultSet != null) {
				if(resultSet.next()) {
					productDetails.setSubCategory(Util.specialTrim(resultSet.getString("subcategory")));
				}
			}
			resultSet.close();
			connection.close();
//			connection = (Connection) dataSourceShipping.getConnection();
//			statement = (Statement) connection.createStatement();
//			query = "select sp.created as manifest, sp.tracking_number as awb from shipping_package sp " +
//			"JOIN shipping_order_item soi ON soi.shipping_package_id = sp.id where " +
//			"soi.suborder_code = '"+productDetails.getSuborderCode()+"' "+
//			"and sp.created > '2014-01-01' and soi.created > '2014-01-01'";
//			resultSet = statement.executeQuery(query);
//			
//			if(resultSet != null) {
//			
//				if(resultSet.next()) {
//					productDetails.setManifestDate(resultSet.getDate("manifest"));
//					productDetails.setForwardAwbNumber(Util.specialTrim(resultSet.getString("awb")));
//				}
//			}
//			resultSet.close();
//			connection.close();
//			
			productDetails.setManifestDate(null);
			productDetails.setForwardAwbNumber(null);
			/** Get shipping Mode from Score DB **/
			productDetails.setShippingMode(getShippingMode(productDetails.getSupc() , productDetails.getSubCategory()));
		}catch(SQLException se){
			LOGGER.error("SQL Exception occured", se);
		}catch(Exception e){
			LOGGER.error("Exception occured", e);
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("SQL Exception occured", e);
			}
		}
		return productDetails;
	}	
}
