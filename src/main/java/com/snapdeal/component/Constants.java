package com.snapdeal.component;

public class Constants {

	public static final String PUTAWAY_STATUS = "PA";
	public static final String RTV_STATUS = "RTV";
	public static final String IN_WAREHOUSE_STATUS = "IW";
	public static final String PUTAWAY_LIST = "PAL";
	public static final String PICK_LIST = "PKL";
	public static final String PUTAWAY_LIST_PRINTED = "PALPR";
	public static final String PICK_LIST_PRINTED = "PKLPR";
	public static final String MANIFEST_PRINTED = "MFP";
	public static final String PICK_LIST_GENERATED = "PKLGR";
	public static final String MISSING = "MSG";
	public static final String RTV_INITIATED = "RTVI";
	public static final String FCVOI = "FC_VOI";
	public static final String GATEPASS = "GTP";
	public static final String SHIPPING_MODE_HARD_CODED = "Air";
	public static final String SELLER_NAME_HARD_CODED = "NA";
	public static final String HISTORY_ACTION_RTV = "RTV Initiated";
	public static final String HISTORY_ACTION_IW = "Confirmed in warehouse after Putaway.";
	public static final String HISTORY_ACTION_PICKLIST = "Picking Initiated.";
	public static final String HISTORY_ACTION_RTV_DIRECT = "Direct RTV selected after scanning.";
	public static final String HISTORY_ACTION_MISSING_PICKLIST = "Not brought in Picklist. Marked in warehouse.";
	
	public static final String SHIPPING_DAYS = "shipping_days";
	public static final String RETURN_TYPE_TO_VENDOR = "To Vendor";
	public static final String RETURN_TYPE_TO_CENTRE = "To Centre";
	public static final String RETURN_TYPE_TO_LIQUIDATION="To Liquidation";
	public static final String RETURN_TYPE_TO_3PL="To 3PL";
	public static final String RETURN_TYPE_TO_CUSTOMER="To Customer";
	
	public static final Long RULE_ID_DIRECT_PUTAWAY = 0L;
	public static final String GROUP_NAME_DIRECT_PUTAWAY = "NA";
	
	public static final Integer MAX_NO_OF_RETRY = 1;
}
