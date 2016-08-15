package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="rtv_sheet_detail")
public class RtvSheet extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="vendor_code",nullable=false)
	private String vendorCode;

	@Column(name="seller_name",nullable=false)
	private String sellerName;

	@ManyToOne
	private Warehouse warehouseDetails;

	@ManyToMany(fetch=FetchType.EAGER)
	private List<Inventory> productDetails;

	@Column(name="courier_code")
	private String courierCode;

	@Column(name="awb_number",length=100)
	private String awbNumber;

	@Column(name="return_warehouse")
	private String returnType;

	@OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private ReceiverDetail receiverDetail;
	
	@Column(name="area_code")
	private String areaCode;
	
	@Column(name="bag")
	private String bag;

	
	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Warehouse getWarehouseDetails() {
		return warehouseDetails;
	}

	public void setWarehouseDetails(Warehouse warehouseDetails) {
		this.warehouseDetails = warehouseDetails;
	}

	public List<Inventory> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<Inventory> productDetails) {
		this.productDetails = productDetails;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getAwbNumber() {
		return awbNumber;
	}

	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}

	public ReceiverDetail getReceiverDetail() {
		return receiverDetail;
	}

	public void setReceiverDetail(ReceiverDetail receiverDetail) {
		this.receiverDetail = receiverDetail;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getBag() {
		return bag;
	}

	public void setBag(String bag) {
		this.bag = bag;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnType() {
		return returnType;
	}
}
