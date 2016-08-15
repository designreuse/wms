package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="inventory")
public class Inventory extends BaseWarehouseEntity implements Comparable<Inventory>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="barcode",length=200,unique=true,nullable=false)
	private String barcode;

	@Column(name="supc")
	private String supc;

	@Column(name="sku")
	private String sku;

	@Column(name="warehouse_location",nullable=false)
	private String location;

	@Column(name="group_name",nullable=false)
	private String groupName;

	@Column(name="rule_id",nullable=false)
	private Long ruleId;

	@Column(name="is_bulk_rule")
	private Boolean bulkRule;

	@Column(name="vendor_code",nullable=false)
	private String vendorCode;

	@Column(name="status",nullable=false)
	private String status;

	@Column(name="seller_name",nullable=false)
	private String sellerName;
	
	@Column(name="customer_name")
	private String customerName;

	@Column(name="product_name",length=2048)
	private String productName;

	@Column(name="qc_remarks")
	private String qcRemarks;
	
	@Column(name="shipping_mode")
	private String shippingMode;
	
	@Column(name="rms_center")
	private String rmsCenter;
	
	@Column(name="ticket_id")
	private String ticketId;

	private String issueCategory;

	private String fulfillmentModel;

	@Temporal(TemporalType.DATE)
	@Column(name="manifestDate")
	private Date manifestDate;

	private Long price;
	
	private String weight;

	private String subCategory;

	private String ccStatus;

	@Column(nullable=false)
	private String suborderCode;
	
	@Column(name="order_code")
	private String orderCode;

	@Column(name="forwardAwbNumber")
	private String forwardAwbNumber;
	
	@Column(name="qc_status")
	private String qcStatus;
	
	@Column(name="post_qc_status")
	private String postQcStatus;

	public String getIssueCategory() {
		return issueCategory;
	}

	public void setIssueCategory(String issueCategory) {
		this.issueCategory = issueCategory;
	}

	public String getFulfillmentModel() {
		return fulfillmentModel;
	}

	public void setFulfillmentModel(String fulfillmentModel) {
		this.fulfillmentModel = fulfillmentModel;
	}

	public Date getManifestDate() {
		return manifestDate;
	}

	public void setManifestDate(Date manifestDate) {
		this.manifestDate = manifestDate;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getCcStatus() {
		return ccStatus;
	}

	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}

	public String getSuborderCode() {
		return suborderCode;
	}

	public void setSuborderCode(String suborderCode) {
		this.suborderCode = suborderCode;
	}

	public String getForwardAwbNumber() {
		return forwardAwbNumber;
	}

	public void setForwardAwbNumber(String forwardAwbNumber) {
		this.forwardAwbNumber = forwardAwbNumber;
	}

	public String getQcRemarks() {
		return qcRemarks;
	}

	public void setQcRemarks(String qcRemarks) {
		this.qcRemarks = qcRemarks;
	}

	public String getSupc() {
		return supc;
	}

	public void setSupc(String supc) {
		this.supc = supc;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Boolean getBulkRule() {
		return bulkRule;
	}

	public void setBulkRule(Boolean bulkRule) {
		this.bulkRule = bulkRule;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getShippingMode() {
		return shippingMode;
	}

	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRmsCenter() {
		return rmsCenter;
	}

	public void setRmsCenter(String rmsCenter) {
		this.rmsCenter = rmsCenter;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public int compareTo(Inventory inventory) {
		if(this.groupName.equalsIgnoreCase(inventory.getGroupName()))
		{
			if(this.location.length() < inventory.getLocation().length())
			{
				return -1;
			}
			else if(this.location.length() > inventory.getLocation().length())
			{
				return 1;
			}
			else {
				return this.location.compareTo(inventory.getLocation());	
			}
		}
		else {
			return this.groupName.compareTo(inventory.getGroupName());
		}
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public void setQcStatus(String qcStatus) {
		this.qcStatus = qcStatus;
	}

	public String getQcStatus() {
		return qcStatus;
	}

	public void setPostQcStatus(String postQcStatus) {
		this.postQcStatus = postQcStatus;
	}

	public String getPostQcStatus() {
		return postQcStatus;
	}

}
