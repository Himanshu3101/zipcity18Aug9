package com.yoeki.iace.societymanagment.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yoeki.iace.societymanagment.DataObject.loginObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by himanshu.Srivastava on 5/4/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    String create_table_complaint, create_table_society, create_table_UserProfessionDetail, create_table_Profession,create_table_Uint, create_table_verification,create_table_request, create_table_rejection, create_table_flat, create_table_UserWiseRoleID, create_table_services, create_table_visitor,result = "";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "society_mgmnt";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            create_table_complaint = "CREATE TABLE Complaint_List ( ComplaintTypeId INTEGER PRIMARY KEY, ComplaintTypeName VARCHAR UNIQUEKEY)";
            create_table_request = "CREATE TABLE Request_List ( RequestTypeId INTEGER PRIMARY KEY, RequestTypeName VARCHAR UNIQUEKEY)";
            create_table_rejection = "CREATE TABLE Rejection_List ( RejectionId INTEGER PRIMARY KEY, RejectionName VARCHAR UNIQUEKEY)";
            create_table_services = "CREATE TABLE Service_List ( ServiceId INTEGER PRIMARY KEY, ServiceName VARCHAR UNIQUEKEY)";
            create_table_flat = "CREATE TABLE Flat_List ( UnitMasterDetailId INTEGER PRIMARY KEY, Location VARCHAR UNIQUEKEY)";
            create_table_visitor = "CREATE TABLE Visitor_List ( visitor_lst_id INTEGER PRIMARY KEY, visitor_nme VARCHAR UNIQUEKEY)";
            create_table_UserWiseRoleID = "CREATE TABLE UserWiseRoleID_List ( UserRoleId INTEGER PRIMARY KEY)";
            create_table_verification = "CREATE TABLE Verification_List ( verification_lst_id INTEGER PRIMARY KEY, verification_nme VARCHAR UNIQUEKEY)";
            create_table_society = "CREATE TABLE Society_List ( Society_lst_id INTEGER PRIMARY KEY, Society_nme VARCHAR UNIQUEKEY)";
            create_table_Uint = "CREATE TABLE Unit_List ( Unit_lst_id INTEGER PRIMARY KEY, Unit_type VARCHAR UNIQUEKEY)";
            create_table_Profession = "CREATE TABLE Profession_List ( Profession_List_id INTEGER PRIMARY KEY, Profession_Name VARCHAR UNIQUEKEY)";

            create_table_UserProfessionDetail = "CREATE TABLE Member_Detail_Directory (Member_Profession VARCHAR, Member_Name VARCHAR, Member_Location VARCHAR, Member_MobileNo VARCHAR)";
//            table_for_token = "CREATE TABLE Token_List ( token VARCHAR)";
            db.execSQL(create_table_complaint);
            db.execSQL(create_table_request);
            db.execSQL(create_table_rejection);
            db.execSQL(create_table_services);
            db.execSQL(create_table_flat);
            db.execSQL(create_table_visitor);
            db.execSQL(create_table_UserWiseRoleID);
            db.execSQL(create_table_verification);
            db.execSQL(create_table_society);
            db.execSQL(create_table_Uint);
            db.execSQL(create_table_Profession);
            db.execSQL(create_table_UserProfessionDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Complaint_List");
        db.execSQL("DROP TABLE IF EXISTS Request_List");
        db.execSQL("DROP TABLE IF EXISTS Rejection_List");
        db.execSQL("DROP TABLE IF EXISTS Service_List");
        db.execSQL("DROP TABLE IF EXISTS Flat_List");
        db.execSQL("DROP TABLE IF EXISTS Visitor_List");
        db.execSQL("DROP TABLE IF EXISTS UserWiseRoleID_List");
        db.execSQL("DROP TABLE IF EXISTS Verification_List");
        db.execSQL("DROP TABLE IF EXISTS Society_List");
        db.execSQL("DROP TABLE IF EXISTS Unit_List");
        db.execSQL("DROP TABLE IF EXISTS Profession_List");
        db.execSQL("DROP TABLE IF EXISTS Member_Detail_Directory");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public Void savetoken(loginObject set) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("token_value", loginObject.getComplaintTypeName());
//        db.insert("Token_List", null, values);
//        db.close();
//        return null;
//    }
    public Void saveUserProfession_details(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Member_Profession", loginObject.getMember_profession());
        values.put("Member_Name", loginObject.getMember_name());
        values.put("Member_Location", loginObject.getMember_location());
        values.put("Member_MobileNo", loginObject.getMember_mobile());
        db.insert("Member_Detail_Directory", null, values);
        db.close();
        return null;
    }

    public Void saveProfessioln(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Profession_List_id",loginObject.getDir_ProfessionID());
        values.put("Profession_Name",loginObject.getDir_Profession_Name());
        db.insert("Profession_List", null, values);
        db.close();
        return null;
    }

    public Void saveServices(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ServiceId", loginObject.getServiceId());
        values.put("ServiceName", loginObject.getServiceName());
        db.insert("Service_List", null, values);
        db.close();
        return null;
    }

    public Void saveComplaint(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ComplaintTypeId", loginObject.getComplaintTypeId());
        values.put("ComplaintTypeName", loginObject.getComplaintTypeName());
        db.insert("Complaint_List", null, values);
        db.close();
        return null;
    }

    public Void saveUnit(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Unit_lst_id", loginObject.getDir_UnitID());
        values.put("Unit_type", loginObject.getDir_Unit_Name());
        db.insert("Unit_List", null, values);
        db.close();
        return null;
    }

    public Void VerificationList(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("verification_lst_id", loginObject.getComplaintTypeId());
        values.put("verification_nme", loginObject.getComplaintTypeName());
        db.insert("Verification_List", null, values);
        db.close();
        return null;
    }

    public Void SocietyList(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Society_lst_id", loginObject.getComplaintTypeId());
        values.put("Society_nme", loginObject.getComplaintTypeName());
        db.insert("Society_List", null, values);
        db.close();
        return null;
    }

    public Void saveRequest(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RequestTypeId", loginObject.getRequestTypeId());
        values.put("RequestTypeName", loginObject.getRequestTypeName());
        db.insert("Request_List", null, values);
        db.close();
        return null;
    }

    public Void saveRejection(loginObject set) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("RejectionId", loginObject.getRejectionId());
            values.put("RejectionName", loginObject.getRejectionName());
            db.insert("Rejection_List", null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Void saveFlatList(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UnitMasterDetailId", loginObject.getUnitMasterDetailId());
        values.put("Location", loginObject.getLocation());
        db.insert("Flat_List", null, values);
        db.close();
        return null;
    }

    public Void saveUserWiseRoleID(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserRoleId", loginObject.getUserRoleId());
        db.insert("UserWiseRoleID_List", null, values);
        db.close();
        return null;
    }

    public Void saveVisitorList(loginObject set) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("visitor_lst_id", loginObject.getVisitor_lst_id());
            values.put("visitor_nme", loginObject.getVisitor_nme());
            db.insert("Visitor_List", null, values);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getRoleID() {
        String selectQuery = "SELECT * FROM UserWiseRoleID_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String uname = cursor.getString(cursor.getColumnIndex("UserRoleId"));
            array.add(uname);
        }
        cursor.close();
        return array;
    }

    public List<String> getFlatList() {
        String selectQuery = "SELECT * FROM Flat_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String flatname = cursor.getString(cursor.getColumnIndex("Location"));
            array.add(flatname);
        }
        cursor.close();
        return array;
    }

    public List<String> getVisitList() {
        String selectQuery = "SELECT * FROM Visitor_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String flatname = cursor.getString(cursor.getColumnIndex("visitor_nme"));
            array.add(flatname);
        }
        cursor.close();
        return array;
    }

    public String getVisitListID(String Visitname) {
        String linkservid = null;
        try {
            String selectQuery = "SELECT visitor_lst_id FROM Visitor_List where visitor_nme = '" + Visitname + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linkservid = cursor.getString(cursor.getColumnIndex("visitor_lst_id"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkservid;
    }

    public String getServiceListID(String Servname) {
        String linkservid = null;
        try {
            String selectQuery = "SELECT ServiceId FROM Service_List where ServiceName = '" + Servname + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linkservid = cursor.getString(cursor.getColumnIndex("ServiceId"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkservid;
    }

    public String getVerificationListID(String verificationName) {
        String linkverificid = null;
        try {
            String selectQuery = "SELECT verification_lst_id FROM Verification_List where verification_nme = '" + verificationName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linkverificid = cursor.getString(cursor.getColumnIndex("verification_lst_id"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkverificid;
    }

    public String getSocietyListID(String societyName) {
        String linkSocietyid = null;
        try {
            String selectQuery = "SELECT Society_lst_id FROM Society_List where Society_nme = '" + societyName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linkSocietyid = cursor.getString(cursor.getColumnIndex("Society_lst_id"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkSocietyid;
    }

    public String getFlatListID(String name) {
        String linkgrpid = null;
        try {
            String selectQuery = "SELECT UnitMasterDetailId FROM Flat_List where Location = '" + name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linkgrpid = cursor.getString(cursor.getColumnIndex("UnitMasterDetailId"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkgrpid;
    }

    public List<String> getReqList() {
        String selectQuery = "SELECT * FROM Request_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String reqname = cursor.getString(cursor.getColumnIndex("RequestTypeName"));
            array.add(reqname);
        }
        cursor.close();
        return array;
    }

    public String getReqListID(String name) {
        String reqid = null;
        try {
            String selectQuery = "SELECT RequestTypeId FROM Request_List where RequestTypeName = '" + name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                reqid = cursor.getString(cursor.getColumnIndex("RequestTypeId"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return reqid;
    }

    public String getUnit_ListID(String s) {
        String unitid = null;
        String selectQuery = "SELECT Unit_lst_id FROM Unit_List where Unit_type = '" + s + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            unitid = cursor.getString(cursor.getColumnIndex("Unit_lst_id"));
        }
        cursor.close();
        return unitid;
    }

    public List<String> getUnit_List() {
        String selectQuery = "SELECT * FROM Unit_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String complaintname = cursor.getString(cursor.getColumnIndex("Unit_type"));
            array.add(complaintname);
        }
        cursor.close();
        return array;
    }

    public List<String> getComplaintList() {
        String selectQuery = "SELECT * FROM Complaint_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String complaintname = cursor.getString(cursor.getColumnIndex("ComplaintTypeName"));
            array.add(complaintname);
        }
        cursor.close();
        return array;
    }

    public List<String> getProfeesionList() {
        String selectQuery = "SELECT * FROM Profession_List";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> array = new ArrayList<String>();
        while(cursor.moveToNext()){
            String complaintname = cursor.getString(cursor.getColumnIndex("Profession_Name"));
            array.add(complaintname);
        }
        cursor.close();
        return array;
    }

    public List<String> getMemberDetails(String name) {
        List<String> array = null;
        try {
            String selectQuery = "SELECT Member_Profession, Member_Name, Member_Location, Member_MobileNo  FROM Member_Detail_Directory where Member_Profession = '" + name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            array = new ArrayList<String>();
            while(cursor.moveToNext()){
                String mem_profess = cursor.getString(cursor.getColumnIndex("Member_Profession"));
                String mem_name = cursor.getString(cursor.getColumnIndex("Member_Name"));
                String mem_locat = cursor.getString(cursor.getColumnIndex("Member_Location"));
                String mem_mobile = cursor.getString(cursor.getColumnIndex("Member_MobileNo"));
                String FullData = mem_profess+"$"+mem_name+"$"+mem_locat+"$"+mem_mobile;
                array.add(FullData);
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return array;
    }

    public String getComplaintListID(String name) {
        String complaintid = null;
        try {
            String selectQuery = "SELECT ComplaintTypeId FROM Complaint_List where ComplaintTypeName = '" + name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                complaintid = cursor.getString(cursor.getColumnIndex("ComplaintTypeId"));
                cursor.moveToNext();
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintid;
    }

    public String deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Complaint_List");
        db.execSQL("delete from Request_List");
        db.execSQL("delete from Rejection_List");
        db.execSQL("delete from Flat_List");
        db.execSQL("delete from UserWiseRoleID_List");
        db.execSQL("delete from Service_List");
        db.execSQL("delete from Visitor_List");
        db.execSQL("delete from Verification_List");
        db.execSQL("delete from Society_List");
        db.execSQL("delete from Unit_List");
        db.close();
        return null;
    }

    public String deleteMem_directory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Member_Detail_Directory");
        db.execSQL("delete from Profession_List");
        db.close();
        return null;
    }
}


