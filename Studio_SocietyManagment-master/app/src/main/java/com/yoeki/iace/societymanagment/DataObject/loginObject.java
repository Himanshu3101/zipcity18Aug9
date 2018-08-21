package com.yoeki.iace.societymanagment.DataObject;

import java.io.Serializable;

/**
 * Created by IACE on 13-Jul-18.
 */

public class loginObject implements Serializable {

    public static  String PDOB;
    public static String ComplaintTypeId;
    public static String ComplaintTypeName;

    public static String RequestTypeId;
    public static String RequestTypeName;

    public static String RejectionId;
    public static String RejectionName;

    public static String Location;
    public static String UnitMasterDetailId;

    public static String UserRoleId;

    public static String ServiceId;
    public static String ServiceName;
    public String societyCode;
    public String SocietyeName;
    public String VerCode;
    public String VerName;

    public String BDetailCode;
    public String BDetailName;
    public String BDetailContactPerson1;
    public String BDetailMobile;
    public String BDetailAddress;
    public String BDetailPincode;
    public String BDetailCountryName;
    public String BDetailStateName;
    public String BDetailCityName;

    public String LiftCode;
    public String LiftName;
    public String LiftType;
    public String GateName;
    public String GateID;
    public String ReqTitle;
    public String ReqCreated;
    public String ReqDescription;
    public String ReqUnit;
    public String ReqAssigen;
    public String ReqStatus;
    public String ReqType;
    public String ReqID;
    public String ReqClosedID;
    public String PUserName;
    public String PPhoneNumber;
    public String PEmailId;
    public String PParkingNo;
    public String PFlatNo;
    public String VendorList_Name;
    public String VendorList_Contact;
    public String VendorList_Verification_type;
    public String VendorList_Verification_no;
    public String VendorList_Verification_by;
    public String VendorList_Verification_on;
    public String VendorList_FromDate;
    public String VendorList_ToDate;
    public String VendorList_Servicetyoe;
    public String VendorList_type;
    public String VendorList_typevalue;
    public String VendorApproval_Name;
    public String VendorApproval_Contact;
    public String VendorApproval_Verification_type;
    public String VendorApproval_Verification_no;
    public String VendorApproval_FromDate;
    public String VendorApproval_ToDate;
    public String VendorApproval_Servicetype;
    public String VendorApproval_type;
    public String VendorApproval_typevalue;
    public String Society_code;
    public String Society_name;
    public String Lift_Type;
    public String Lift_Count;
    public String Parking_Type;
    public String Parking_Count;
    public String Unit_Type;
    public String Unit_Count;
    public String Facility_Type;
    public String Facility_Count;
    public String Owner_name;
    public String New_Parking_Count;
    public String New_Unit_Count;
    public String Mem_Name;
    public String Mem_Gender;
    public String Mem_Mobile;
    public String Mem_Status;
    public String Vendor_name;
    public String Vendor_Contct;
    public String Vendor_Creat;
    public String Visitor_name;
    public String Visitor_Address;
    public String Visitor_Fromdte;
    public String Visitor_Todte;
    public String Visitor_Status;
    public String Visitor_Contact;
    public String Home_stat;
    public String req_title;
    public String req_unit;
    public String req_status;
    public String req_type;
    public String req_fromdte;
    public String req_todte;
    public String req_vendr_mobile;
    public String Cir_title;
    public String Cir_fdatetime;
    public String Cir_tdatetime;
    public String Cir_description;
    public String req_vndr_nme;
    public String req_creat_on;
    public String req_creat_by;
    public String req_req_nu;
    public String req_desc;
    public String Helpline_Name;
    public String Helpline_Contact;
    public String com_title;
    public String com_creat_on;
    public String com_creat_by;
    public String com_unit;
    public String com_complaintNo;
    public String com_desc;
    public String com_status;
    public String com_vndr_nme;
    public String com_type;
    public String com_vendr_mobile;
    public String Vendor_Rating;
    public String Vendor_Status;
    public String com_UniqueCode;
    public String com_ComplaintCode;
    public String Rules_title;
    public String Rules_description;
    public String VerificationCode;
    public String VerificationName;
    public String Parking_no;
    public String Unit_no;
    public String Unit_status;
    public String req_uniq_code;
    public String GK_Name;
    public String GK_Address;
    public String GK_Fdate;
    public String GK_Tdate;
    public String GK_Status;
    public String GK_ContactNo;
    public String GK_name;
    public String GK_role;
    public String GK_contactno;
    public String req_req_id;
    public String req_Id;
    public String Mem_logId;
    public String Mem_Id;
    public String Profile_Name;
    public String Profile_Contact;
    public String Profile_ServiceType;
    public String Owner_role;
    public String Charges_Type;
    public String Charges_Amount;

    public static String getVisitor_nme() {
        return visitor_nme;
    }

    public void setVisitor_nme(String visitor_nme) {
        this.visitor_nme = visitor_nme;
    }

    public static String getVisitor_lst_id() {
        return visitor_lst_id;
    }

    public void setVisitor_lst_id(String visitor_lst_id) {
        this.visitor_lst_id = visitor_lst_id;
    }

    public static String visitor_nme;
    public static String visitor_lst_id;


    public loginObject(){}

    public loginObject(String ComplaintTypeId, String ComplaintTypeName){
        this.ComplaintTypeId=ComplaintTypeId;
        this.ComplaintTypeName=ComplaintTypeName;
    }

    public loginObject(String id) {
        this.UserRoleId = id;
    }

    public static String getComplaintTypeId() {
        return ComplaintTypeId;
    }

    public void setComplaintTypeId(String complaintTypeId) {
        ComplaintTypeId = complaintTypeId;
    }

    public static String getComplaintTypeName() {
        return ComplaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        ComplaintTypeName = complaintTypeName;
    }

    public static String getRequestTypeId() {
        return RequestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        RequestTypeId = requestTypeId;
    }

    public static String getRequestTypeName() {
        return RequestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        RequestTypeName = requestTypeName;
    }

    public static String getRejectionId() {
        return RejectionId;
    }

    public void setRejectionId(String rejectionId) {
        RejectionId = rejectionId;
    }

    public static String getRejectionName() {
        return RejectionName;
    }

    public void setRejectionName(String rejectionName) {
        RejectionName = rejectionName;
    }

    public static String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public static String getUnitMasterDetailId() {
        return UnitMasterDetailId;
    }

    public void setUnitMasterDetailId(String unitMasterDetailId) {
        UnitMasterDetailId = unitMasterDetailId;
    }

    public static String getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        UserRoleId = userRoleId;
    }

    public static String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public static String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public String getSocietyeName() {
        return SocietyeName;
    }

    public void setSocietyeName(String societyeName) {
        SocietyeName = societyeName;
    }

    public String getVerCode() {
        return VerCode;
    }

    public void setVerCode(String verCode) {
        VerCode = verCode;
    }

    public String getVerName() {
        return VerName;
    }

    public void setVerName(String verName) {
        VerName = verName;
    }
}
