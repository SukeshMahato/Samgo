package com.app.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.app.model.CountryModel;
import com.app.model.DocketDetail;
import com.app.model.DocketList;
import com.app.model.ErrorCodeModel;
import com.app.model.Job;
import com.app.model.JobDetails;
import com.app.model.Machine;
import com.app.model.MachineDetails;
import com.app.model.MachineHistory;
import com.app.model.MachineManufacturer;
import com.app.model.MachineMaster;
import com.app.model.MachineModel;
import com.app.model.MachineServiceHistory;
import com.app.model.MachineSiteMain;
import com.app.model.MachineView;
import com.app.model.Machinetype;
import com.app.model.MasterSpareParts;
import com.app.model.Site;
import com.app.model.SiteEnggModel;
import com.app.model.SparePartsModel;
import com.app.model.StartJobModel;
import com.app.model.TrainingAddedToJob;
import com.app.model.TrainingPojo;
import com.app.model.TrainingSyncPojo;
import com.app.model.TrainingVideoModel;
import com.app.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class  SamgoSQLOpenHelper extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	// database name
	private static final String database_NAME = "SAMGODB";
	// Table Name
	private static final String TABLE_LOGIN = "login";
	// Table column name
	private static final String ENGID = "id";
	private static final String ROLE = "role";
	private static final String FIRSTNAME = "first_name";
	private static final String LASTNAME = "last_name";
	private static final String STAFFNO = "staff_no";
	private static final String SAGENO = "sage_no";
	private static final String EMAILID = "email_id";
	private static final String PHONE = "phone";
	private static final String ADDRESS = "address";
	private static final String COUNTRYID = "country_id";
	private static final String COUNTY = "county";
	private static final String CITY = "city";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String COUNTRYNAME = "country_name";
	private static final String SHIFTSTART = "shift_start";
	private static final String SHIFTEND = "shift_end";
	private static final String PHOTOPATH = "photo_path";

	// Table Name
	private static final String TABLE_TODAYJOB_LIST = "today_job_list";
	// Table column name
	private static final String TODAYJOBID = "id";
	private static final String JOBONEROWITEM = "job_one_row";
	private static final String CREATEDAT = "created_at";

	// Table Name
	private static final String TABLE_TRAINING_LIST = "training_list";
	// Table column name
	private static final String ID = "id";
	private static final String TRAINING_ID = "training_id";
	private static final String DOCKET_ID = "docket_id";
	private static final String TRAINING_TITLE = "training_title";
	private static final String SITE_NAME = "site_name";
	private static final String SECTOR_NAME = "sector_name";
	private static final String DUE_DATE = "due_date";
	private static final String JOB_DATE = "job_date";
	private static final String STATUS = "status";
	private static final String CLIENT_NAME = "client_name";

	// Table Name
	private static final String TABLE_VIDEO_LIST = "video_list";
	// Table column name
	private static final String VID_ID = "id";
	private static final String VIDEO_ID = "video_id";
	private static final String VIDEO_TRAINING_ID = "training_id";
	private static final String VIDEO_TITLE = "video_title";
	private static final String VIDEO_URL = "video_url";

	// Table Name for TomorrowJob
	private static final String TABLE_TOMORROWJOB_LIST = "tomorrow_job_list";
	// Table column name for TomorrowJob
	private static final String TOMORROWJOBID = "id";
	private static final String JOBTOMORROWONEROWITEM = "job_tomorrow_one_row";
	private static final String CREATEDTOMORROWAT = "created_tomorrow_at";

	// Table Name for Job
	private static final String TABLE_JOB_LIST = "job_list";
	// Table column name for Job
	private static final String JOBID = "id";
	private static final String JOB_ID = "job_id";
	private static final String DOCKETID = "docket_id";
	private static final String JOBTITLE = "job_title";
	private static final String JOBSECTORNAME = "sector_name";
	private static final String JOBSITENAME = "site_name";
	private static final String JOBDUEDATE = "due_date";
	private static final String JOBSTATUS = "status";
	private static final String JOBCLIENTNAME = "client_name";
	private static final String JOBCONTRACTORNAME = "contractor_name";
	private static final String JOBSITEADDRESS = "site_address";
	private static final String JOBSITEMANAGERLIST = "site_manager_list";
	private static final String JOBTASKDETAILS = "task_details";

	private static final String JOBUPDATEDON = "updated_on";
	private static final String JOBUPDATEDBY = "updated_by";
	private static final String JOBSTARTTIME = "start_time";
	private static final String JOBSTARTPLACE = "start_place";
	private static final String JOBSITEID = "site_id";
	private static final String JOBSITELATITUDE = "site_latitude";
	private static final String JOBSITELONGITUDE = "site_longitude";
	private static final String JOBSTARTDATE = "job_date";

	private static final String JOBSTART_PLACE = "docket_start_place";
	private static final String JOBFINISHPLACE = "finish_place";
	private static final String JOBCLIENTID = "client_id";
	private static final String JOBPARKDETAIL = "park_comment";
	private static final String JOBCOMMENTS = "job_coment";

	// Table Name for MachineLIst
	private static final String TABLE_MACHINE_LIST = "machine_list";
	// Table column name for MachineList
	private static final String MACHINEID = "id";
	private static final String MACHINESERIALNO = "machine_serial_no";
	private static final String MACHINENAME = "machine_name";
	private static final String MACHINETYPE = "machine_type";
	private static final String MANUFACTURER = "machine_manufacture";
	private static final String SITENAME = "site_name";
	private static final String MARKS = "marks";

	// Table Name for SiteLIst
	private static final String TABLE_SITE_LIST = "site_list";
	// Table column name for SiteList
	private static final String SITEID = "id";
	private static final String SITE_ID = "site_id";
	private static final String SITENAME2 = "site_name";
	private static final String SITECITY = "site_city";
	private static final String SITECOUNTY = "site_county";
	private static final String SITECLIENTNAME = "client_name";
	private static final String SITELATITUDE = "latitude";
	private static final String SITELONGITUDE = "longitude";
	private static final String SITEPHOTOPATH = "photo_path";
	private static final String SITEADDRESS = "site_address";
	private static final String SITECONTRACTORNAME = "contractor_name";

	// Table Name for start_job
	private static final String TABLE_START_JOB = "start_job";
	// Table column name for start_job
	private static final String START_ID = "id";
	private static final String START_JOB_ID = "job_id";
	private static final String START_JOB_ENGINEER_ID = "engineer_id";
	private static final String START_JOB_SITE_ID = "site_id";
	private static final String DISTANCE = "distance";
	private static final String DISTANCE_UNIT = "distance_unit";
	private static final String START_TIME = "start_time";
	private static final String END_TIME = "end_time";

	// Table Name for machine_details
	private static final String TABLE_MACHINE_DETAILS = "machine_details";
	// Table column name for start_job
	private static final String MACHINE_ID = "id";
	private static final String MACHINE_SITE_ID = "site_id";
	private static final String MACHINE_MANUFACTURE_NAME = "manufacture_name";
	private static final String MACHINE_TYPE = "machine_type";
	private static final String MACHINE_MODEL = "machine_model";
	private static final String MACHINE_NAME = "machine_name";
	private static final String MACHINE_SERIAL_NO = "machine_sn";
	private static final String MACHINE_ADDED_BY = "added_by";
	private static final String MACHINE_VOLTAGE = "voltage";
	private static final String MACHINE_SUCTION = "suction";
	private static final String MACHINE_TRACTION = "traction";
	private static final String MACHINE_WATER = "water";
	private static final String MACHINE_MFG_YEAR = "manufacture_year";
	private static final String MACHINE_MARKS = "machine_marks";
	private static final String MACHINE_WORKS = "machine_works";
	private static final String MACHINE_VISUAL = "machine_visual";
	private static final String MACHINE_SPARE = "machine_spare";

	// Table Name for machine_details
	private static final String TABLE_MACHINE_VIEW = "machine_view";
	// Table column name for start_job
	private static final String MACHINE_VIEW_ID = "id";
	private static final String MACHINE_VIEW_DETAILS_ID = "machine_id";
	private static final String MACHINE_MASTER_NAME = "machine_master_name";
	private static final String MACHINE_SL_NO = "machine_sl_no";
	private static final String MACHINE_QR_CODE = "machine_qr_code";
	private static final String MACHINE_ASSOCIATED_SITE = "site_name";
	private static final String MACHINE_MANUFACTURER = "machine_manufacturer";
	private static final String MACHINE_M_TYPE = "machine_type";
	private static final String MACHINE_M_MODEL = "machine_model";
	private static final String MACHINE_DESC = "machine_desc";
	private static final String MACHINE_TOTAL_MARKS = "machine_marks";
	private static final String MACHINE_YOUTUBE_LINK1 = "youtube_link_1";
	private static final String MACHINE_YOUTUBE_LINK2 = "youtube_link_2";
	private static final String MACHINE_PARTS_DRAWLING = "parts_drawling";
	private static final String MACHINE_USER_MANUAL = "user_manual";
	private static final String MACHINE_DATA_SHEET = "data_sheet";
	private static final String MACHINE_PHOTO_PATH = "photo_path";

	// Table Name for machine_details
	private static final String TABLE_MACHINE_HISTORY = "machine_history";
	// Table column name for start_job
	private static final String MACHINE_HISTORY_ID = "id";
	private static final String MACHINE_HISTORY_MACHINE_ID = "machine_id";
	private static final String MACHINE_HISTORY_SITE_NAME = "site_name";
	private static final String MACHINE_HISTORY_ASSIGN_DATE = "machine_assign";

	// Table Name for machine_details
	private static final String TABLE_MACHINE_SERVICE_HISTORY = "machine_service_history";
	// Table column name for start_job
	private static final String MACHINE_SERVICE_ID = "id";
	private static final String MACHINE_SERVICE_MACHINE_ID = "machine_id";
	private static final String MACHINE_SERVICE_ERROR_ID = "error_id";
	private static final String MACHINE_SERVICE_PROBLEM = "problem";
	private static final String MACHINE_SERVICE_ENGINEER_NAME = "engineer_name";
	private static final String MACHINE_SERVICE_DATE_TIME = "work_date_time";
	private static final String MACHINE_SERVICE_ENGINEER_COMMENTS = "engineer_comments";
	private static final String MACHINE_SERVICE_WORK_CARRIED_OUT = "work_carried_out";

	// Tables Name for MachineMaster
	private static final String TABLE_MACHINEMASTER_LIST = "machineMaster_list";
	// column Names
	private static final String MACHINEMASTER_ID = "machineMaster_id";
	private static final String TYPE_ID = "type_id";
	private static final String MANUFACTURER_ID = "manufacturer_id";
	private static final String MODEL_ID = "model_id";
	private static final String MACHINEMASTER_NAME = "machine_name";
	private static final String MACHINEMASTER_DESC = "machine_desc";

	private static final String TABLE_MACHINEMANUFACTURER_LIST = "machineManufacture_list";
	// column Names
	private static final String MANUFACTURERMASTER_ID = "manufacturerMaster_id";
	private static final String MANUFACTURER_NAME = "manufacturer_name";

	private static final String TABLE_MACHINEMODEL_LIST = "machineModel_list";
	// column Names
	private static final String MODELMASTER_ID = "modelMaster_id";
	private static final String MACHINEMODEL_ID = "machine_id";
	private static final String MODEL_NAME = "model_name";

	private static final String TABLE_MACHINETYPE_LIST = "machineType_list";
	// column Names
	private static final String TYPEMASTER_ID = "typeMaster_id";
	private static final String TYPE_NAME = "type_name";
	private static final String TYPE_DESC = "type_desc";

	// Table Name
	private static final String TABLE_ADDMACHINE_LIST = "table_addMachine_list";
	// column Names
	private static final String MACHINE_MANUFACTURER_NAME = "machine_manufacture";// 1
	private static final String MACHINE_MANUFACTURER_ID = "machine_manufacture_ID";// 2
	private static final String MACHINE_TYPE_NAME = "machine_type";// 3
	private static final String MACHINE_TYPE_ID = "machine_type_id";// 4
	private static final String MACHINE = "machine";// 5
	private static final String MACHINE_IDS = "machine_id";// 6
	private static final String MACHINE_MODEL_NAME = "machine_model";// 7
	private static final String MACHINE_MODEL_ID = "machine_model_id";// 8
	private static final String MACHINE_VOLTAGE_VALUE = "machine_voltage";// 9
	private static final String MACHINE_SUCTION_VALUE = "machine_suction";// 10
	private static final String MACHINE_TRACTION_VALUE = "machine_traction";// 11
	private static final String MACHINE_WATER_VALUE = "machine_water";// 12
	private static final String MACHINE_WORK_ORDER = "machine_work_order";// 13
	private static final String MACHINE_SPARE_PARTS = "machine_spare_parts";// 14
	private static final String MACHINE_MARKS_AVAIL = "machine_marks_avail";// 15
	private static final String MACHINE__VISUAL_INSPECTION = "machine_visual_inspection";// 16
	private static final String MACHINE_YEAR = "machine_year";// 17
	private static final String MACHINE_WARRANTY = "machine_warranty";// 18
	private static final String MACHINE_SI_NO = "machine_si_no";// 19
	private static final String PURCHASE_DATE = "purchase_date";// 20
	private static final String MACHINE_ADD_SITE_ID = "site_id";// 21
	private static final String MACHINE_SAVED_LOCALLY = "machine_saved_local";// 22

	// Table Name
	private static final String TABLE_SPARE_PARTS_MASTER = "table_spare_parts_master";
	// column Names
	private static final String SPARE_AUTO_INC = "id";
	private static final String SPARE_ID = "spare_id";
	private static final String SPARE_PRODUCT_ID = "product_id";
	private static final String SPARE_DESCRIPTION = "description";
	private static final String SPARE_SALES_PRICE = "sales_price";
	private static final String SPARE_QUANTITY = "quantity";
	private static final String SPARE_BARCODE = "barcode";

	// Table Name
	private static final String TABLE_SPARE_ADDED_MACHINE = "table_spare_added_machine";
	// column Names
	private static final String SPARE_ADDED_INC = "id";
	private static final String SPARE_ADDED_ID = "spare_id";
	private static final String SPARE_ADDED_JOB_ID = "spare_job_id";
	private static final String SPARE_ADDED_PRODUCT_ID = "product_id";
	private static final String SPARE_ADDED_DESCRIPTION = "description";
	private static final String SPARE_ADDED_SALES_PRICE = "sales_price";
	private static final String SPARE_ADDED_QUANTITY = "quantity";
	private static final String SPARE_ADDED_MACHINE_ID = "machine_id";

	// Table Name
	private static final String TABLE_ERROR_CODE_DESCRIPTOIN = "error_code_desc";
	// column names
	private static final String ERROR_CODE_ID_AUTO = "id";
	private static final String ERROR_CODE_ID = "error_code_id";
	private static final String ERROR_CODE_NAME = "error_code";
	private static final String ERROR_CODE_DESC = "description";

	// Table Name
	private static final String TABLE_COUNTRY_LIST = "table_country_list";
	// Table column name
	private static final String COUNTRIESID = "country_id";
	private static final String COUNTRIESNAME = "country_name";

	// Table Name adding machine in job
	private static final String TABLE_JOBDETAIL = "jobdetails";
	// Table column name
	private static final String JOBDETAIL_ID = "id";
	private static final String JOBDETAIL_JOB_ID = "job_id";
	private static final String JOBDETAIL_MACHINE_ID = "machine_id";
	private static final String JOBDETAIL_ERROR_CODE = "error_code";
	private static final String JOBDETAIL_PROBLEM = "problem";
	private static final String JOBDETAIL_STATUS = "status";
	private static final String JOBDETAIL_COMMENTS = "comments";

	// Table Name for work done
	private static final String TABLE_DOCKET_DETAIL = "docket_detail";
	// Table column name
	private static final String DOCKET_DETAIL_ID = "id";
	private static final String DOCKET_DETAIL_JOB_ID = "job_id";
	private static final String DOCKET_DETAIL_MACHINE_ID = "machine_id";
	private static final String DOCKET_DETAIL_JOBDETAIL_ID = "jobdetail_id";
	private static final String DOCKET_DETAIL_WORK_CARRIED = "work_carried_out";
	private static final String DOCKET_DETAIL_PARTS_UNABLE_TO_FIND = "parts_unable_to_find";
	private static final String DOCKET_DETAIL_EOL_STATUS = "eol_status";

	// Table Name
	private static final String TABLE_MACHINE_IMAGE = "machine_image_table";
	// Table column name
	private static final String MACHINE_IMAGE_ID_AUTO = "id";
	private static final String MACHINE_JOB_ID = "job_id";
	private static final String MACHINE_IMAGE_ID = "job_detail_id";
	private static final String MACHINE_IMAGE_NAME = "machine_image";

	// Table Name
	private static final String TABLE_TRAINING_ADDED_TO_JOB = "training_added_job";
	// Table column name
	private static final String TRAINING_ID_AUTO = "training_id";
	private static final String TRAINING_CLIENT_ID = "client_id";
	private static final String TRAINING_SITE_ID = "site_id";
	private static final String TRAINING_JOB_TITLE = "job_title";
	private static final String TRAINING_ASSIGNED_DATE = "assign_date";
	private static final String TRAINING_CREATED_DATE = "created_date";
	private static final String TRAINING_JOB_ID = "job_id";

	// Table Name
	private static final String TABLE_DOCKET_LIST = "docket_list";
	// Table column name
	private static final String DOCKET_JOB_ID = "job_id"; // 1
	private static final String DOCKET_USERTYPE_ID = "usertype_id"; // 2
	private static final String DOCKET_ENGG_ID = "engg_id"; // 3
	private static final String DOCKET_DATE = "docket_date"; // 4
	private static final String CHECK_IN_TIME = "check_in"; // 5
	private static final String CHECK_OUT_TIME = "check_out"; // 6
	private static final String PAT_TEST = "pat_test"; // 7
	private static final String DOCKET_COMMENT = "comment"; // 8
	private static final String DOCKET_STATUS = "status"; // 9
	private static final String DOCKET_COMPLETE = "docket_complete"; // 10
	private static final String DOCKET_SIGNATURE = "docket_signature"; // 11
	private static final String DOCKET_SIGNATURE_NAME = "docket_signature_name"; // 12

	// Table Name
	private static final String TABLE_TRAINING_TRAINEE = "trainee_names";
	// Table column name
	private static final String TRAINING_TRAINEE_ID = "training_id"; // 1
	private static final String TRAINING_TRAINEE_NAMES = "trainee_names"; // 2
	private static final String TRAINING_TRAINEE_DATE = "trainee_date"; // 3

	public SamgoSQLOpenHelper(Context context) {
		super(context, database_NAME, null, database_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + ENGID + " INTEGER PRIMARY KEY," + ROLE
				+ " TEXT," + FIRSTNAME + " TEXT," + LASTNAME + " TEXT," + STAFFNO + " TEXT," + SAGENO + " TEXT,"
				+ EMAILID + " TEXT," + PHONE + " TEXT," + ADDRESS + " TEXT," + COUNTRYID + " TEXT," + COUNTY + " TEXT,"
				+ CITY + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT," + COUNTRYNAME + " TEXT," + SHIFTSTART
				+ " TEXT," + SHIFTEND + " TEXT," + PHOTOPATH + " TEXT" + ")";

		db.execSQL(CREATE_LOGIN_TABLE);

		String CREATE_TODAY_JOB_LIST_TABLE = "CREATE TABLE " + TABLE_TODAYJOB_LIST + "(" + TODAYJOBID
				+ " INTEGER PRIMARY KEY," + JOBONEROWITEM + " TEXT," + CREATEDAT + " TEXT" + ")";

		db.execSQL(CREATE_TODAY_JOB_LIST_TABLE);

		String CREATE_TRAINING_LIST_TABLE = "CREATE TABLE " + TABLE_TRAINING_LIST + "(" + ID + " INTEGER PRIMARY KEY,"
				+ TRAINING_ID + " TEXT," + DOCKET_ID + " TEXT," + TRAINING_TITLE + " TEXT," + SITE_NAME + " TEXT,"
				+ SECTOR_NAME + " TEXT," + DUE_DATE + " TEXT," + JOB_DATE + " TEXT," + STATUS + " TEXT," + CLIENT_NAME
				+ " TEXT" + ")";

		db.execSQL(CREATE_TRAINING_LIST_TABLE);

		String CREATE_VIDEO_LIST_TABLE = "CREATE TABLE " + TABLE_VIDEO_LIST + "(" + VID_ID + " INTEGER PRIMARY KEY,"
				+ VIDEO_ID + " TEXT," + VIDEO_TRAINING_ID + " TEXT," + VIDEO_TITLE + " TEXT," + VIDEO_URL + " TEXT"
				+ ")";

		db.execSQL(CREATE_VIDEO_LIST_TABLE);

		// create table for tomorrowJob
		String CREATE_TOMORROW_JOB_LIST_TABLE = "CREATE TABLE " + TABLE_TOMORROWJOB_LIST + "(" + TOMORROWJOBID
				+ " INTEGER PRIMARY KEY," + JOBTOMORROWONEROWITEM + " TEXT," + CREATEDTOMORROWAT + " TEXT" + ")";

		db.execSQL(CREATE_TOMORROW_JOB_LIST_TABLE);

		String CREATE_JOB_LIST_TABLE = "CREATE TABLE " + TABLE_JOB_LIST + "(" + JOBID + " INTEGER PRIMARY KEY," + JOB_ID
				+ " TEXT," + DOCKETID + " TEXT," + JOBTITLE + " TEXT," + JOBSECTORNAME + " TEXT," + JOBSITENAME
				+ " TEXT," + JOBDUEDATE + " TEXT," + JOBSTARTDATE + " TEXT," + JOBSTATUS + " TEXT," + JOBCLIENTNAME
				+ " TEXT," + JOBCONTRACTORNAME + " TEXT," + JOBSITEADDRESS + " TEXT," + JOBSITEMANAGERLIST + " TEXT,"
				+ JOBUPDATEDON + " TEXT," + JOBUPDATEDBY + " TEXT," + JOBSTARTTIME + " TEXT," + JOBSTARTPLACE + " TEXT,"
				+ JOBSITEID + " TEXT," + JOBSITELONGITUDE + " TEXT," + JOBSITELATITUDE + " TEXT," + JOBTASKDETAILS
				+ " TEXT," + JOBSTART_PLACE + " TEXT," + JOBFINISHPLACE + " TEXT," + JOBCLIENTID + " TEXT,"
				+ JOBPARKDETAIL + " TEXT," + JOBCOMMENTS + " TEXT" + ")";

		db.execSQL(CREATE_JOB_LIST_TABLE);

		// create table for machineList
		String CREATE_MACHINE_LIST_TABLE = "CREATE TABLE " + TABLE_MACHINE_LIST + "(" + MACHINEID
				+ " INTEGER PRIMARY KEY," + MACHINESERIALNO + " TEXT," + MACHINENAME + " TEXT," + MACHINETYPE + " TEXT,"
				+ MANUFACTURER + " TEXT," + SITENAME + " TEXT," + MARKS + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_LIST_TABLE);

		// create table for siteList
		String CREATE_SITE_LIST_TABLE = "CREATE TABLE " + TABLE_SITE_LIST + "(" + SITEID + " INTEGER PRIMARY KEY,"
				+ SITE_ID + " TEXT," + SITENAME2 + " TEXT," + SITECITY + " TEXT," + SITECOUNTY + " TEXT,"
				+ SITECLIENTNAME + " TEXT," + SITELATITUDE + " TEXT," + SITELONGITUDE + " TEXT," + SITEPHOTOPATH
				+ " TEXT," + SITEADDRESS + " TEXT," + SITECONTRACTORNAME + " TEXT " + ")";

		db.execSQL(CREATE_SITE_LIST_TABLE);

		String CREATE_START_JOB_TABLE = "CREATE TABLE " + TABLE_START_JOB + "(" + START_ID + " INTEGER PRIMARY KEY,"
				+ START_JOB_ID + " TEXT," + START_JOB_ENGINEER_ID + " TEXT," + START_JOB_SITE_ID + " TEXT," + DISTANCE
				+ " TEXT," + DISTANCE_UNIT + " TEXT," + START_TIME + " TEXT," + END_TIME + " TEXT " + ")";

		db.execSQL(CREATE_START_JOB_TABLE);

		String CREATE_MACHINE_DETAILS_TABLE = "CREATE TABLE " + TABLE_MACHINE_DETAILS + "(" + MACHINE_ID
				+ " INTEGER PRIMARY KEY," + MACHINE_SITE_ID + " TEXT," + MACHINE_MANUFACTURE_NAME + " TEXT,"
				+ MACHINE_TYPE + " TEXT," + MACHINE_MODEL + " TEXT," + MACHINE_NAME + " TEXT," + MACHINE_SERIAL_NO
				+ " TEXT," + MACHINE_ADDED_BY + " TEXT," + MACHINE_VOLTAGE + " TEXT," + MACHINE_SUCTION + " TEXT,"
				+ MACHINE_TRACTION + " TEXT," + MACHINE_WATER + " TEXT," + MACHINE_MFG_YEAR + " TEXT," + MACHINE_MARKS
				+ " TEXT," + MACHINE_WORKS + " TEXT," + MACHINE_VISUAL + " TEXT," + MACHINE_SPARE + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_DETAILS_TABLE);

		String CREATE_MACHINE_VIEW_TABLE = "CREATE TABLE " + TABLE_MACHINE_VIEW + "(" + MACHINE_VIEW_ID
				+ " INTEGER PRIMARY KEY," + MACHINE_VIEW_DETAILS_ID + " TEXT," + MACHINE_MASTER_NAME + " TEXT,"
				+ MACHINE_SL_NO + " TEXT," + MACHINE_QR_CODE + " TEXT," + MACHINE_ASSOCIATED_SITE + " TEXT,"
				+ MACHINE_MANUFACTURER + " TEXT," + MACHINE_M_TYPE + " TEXT," + MACHINE_M_MODEL + " TEXT,"
				+ MACHINE_DESC + " TEXT," + MACHINE_TOTAL_MARKS + " TEXT," + MACHINE_YOUTUBE_LINK1 + " TEXT,"
				+ MACHINE_YOUTUBE_LINK2 + " TEXT," + MACHINE_PARTS_DRAWLING + " TEXT," + MACHINE_USER_MANUAL + " TEXT,"
				+ MACHINE_DATA_SHEET + " TEXT," + MACHINE_PHOTO_PATH + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_VIEW_TABLE);

		String CREATE_MACHINE_HISTORY_TABLE = "CREATE TABLE " + TABLE_MACHINE_HISTORY + "(" + MACHINE_HISTORY_ID
				+ " INTEGER PRIMARY KEY," + MACHINE_HISTORY_MACHINE_ID + " TEXT," + MACHINE_HISTORY_SITE_NAME + " TEXT,"
				+ MACHINE_HISTORY_ASSIGN_DATE + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_HISTORY_TABLE);

		String CREATE_MACHINE_SERVICE_HISTORY_TABLE = "CREATE TABLE " + TABLE_MACHINE_SERVICE_HISTORY + "("
				+ MACHINE_SERVICE_ID + " INTEGER PRIMARY KEY," + MACHINE_SERVICE_MACHINE_ID + " TEXT,"
				+ MACHINE_SERVICE_ERROR_ID + " TEXT," + MACHINE_SERVICE_PROBLEM + " TEXT,"
				+ MACHINE_SERVICE_ENGINEER_NAME + " TEXT," + MACHINE_SERVICE_DATE_TIME + " TEXT,"
				+ MACHINE_SERVICE_ENGINEER_COMMENTS + " TEXT," + MACHINE_SERVICE_WORK_CARRIED_OUT + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_SERVICE_HISTORY_TABLE);

		// create table for machineMaster
		String CREATE_MACHINE_MASTER_TABLE = "CREATE TABLE " + TABLE_MACHINEMASTER_LIST
				+ "( machineMasterAuto INTEGER PRIMARY KEY AUTOINCREMENT," + MACHINEMASTER_ID + " TEXT," + TYPE_ID
				+ " TEXT," + MANUFACTURER_ID + " TEXT," + MODEL_ID + " TEXT," + MACHINEMASTER_NAME + " TEXT,"
				+ MACHINEMASTER_DESC + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_MASTER_TABLE);

		// create table for machineTypeMaster
		String CREATE_MACHINETYPE_MASTER_TABLE = "CREATE TABLE " + TABLE_MACHINETYPE_LIST + "(" + TYPEMASTER_ID
				+ " TEXT, typeMasterAuto " + " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE_NAME + " TEXT," + TYPE_DESC
				+ " TEXT" + ")";

		db.execSQL(CREATE_MACHINETYPE_MASTER_TABLE);

		// create table for machineModelMaster
		String CREATE_MACHINEMODEL_MASTER_TABLE = "CREATE TABLE " + TABLE_MACHINEMODEL_LIST + "(" + MODELMASTER_ID
				+ " TEXT, modelMasterAuto " + " INTEGER PRIMARY KEY AUTOINCREMENT," + MACHINEMODEL_ID + " TEXT,"
				+ MODEL_NAME + " TEXT" + ")";

		db.execSQL(CREATE_MACHINEMODEL_MASTER_TABLE);

		// create table for machineManufacturerMaster
		String CREATE_MACHINEMANUFACTURER_MASTER_TABLE = "CREATE TABLE " + TABLE_MACHINEMANUFACTURER_LIST + "("
				+ MANUFACTURERMASTER_ID + " TEXT, machineMasterAuto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MANUFACTURER_NAME + " TEXT" + ")";

		db.execSQL(CREATE_MACHINEMANUFACTURER_MASTER_TABLE);

		// create table for addMachineToSiteMaster
		String CREATE_ADDMACHINETOSITE_MASTER_TABLE = "CREATE TABLE " + TABLE_ADDMACHINE_LIST + "("
				+ MACHINE_MANUFACTURER_NAME + " TEXT," + MACHINE_MANUFACTURER_ID + " TEXT," + MACHINE_TYPE_NAME
				+ " TEXT," + MACHINE_TYPE_ID + " TEXT," + MACHINE + " TEXT," + MACHINE_IDS + " TEXT,"
				+ MACHINE_MODEL_NAME + " TEXT," + MACHINE_MODEL_ID + " TEXT," + MACHINE_VOLTAGE_VALUE + " TEXT,"
				+ MACHINE_SUCTION_VALUE + " TEXT," + MACHINE_TRACTION_VALUE + " TEXT," + MACHINE_WATER_VALUE + " TEXT,"
				+ MACHINE_WORK_ORDER + " TEXT," + MACHINE_SPARE_PARTS + " TEXT," + MACHINE_MARKS_AVAIL + " TEXT,"
				+ MACHINE__VISUAL_INSPECTION + " TEXT," + MACHINE_YEAR + " TEXT," + MACHINE_WARRANTY + " TEXT,"
				+ MACHINE_SI_NO + " TEXT," + PURCHASE_DATE + " TEXT," + MACHINE_ADD_SITE_ID + " TEXT,"
				+ MACHINE_SAVED_LOCALLY + " TEXT" + ")";

		db.execSQL(CREATE_ADDMACHINETOSITE_MASTER_TABLE);

		// create table for spare Parts Master table
		String CREATE_SPARE_PARTS_MASTER_TABLE = "CREATE TABLE " + TABLE_SPARE_PARTS_MASTER + "(" + SPARE_AUTO_INC
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + SPARE_ID + " TEXT," + SPARE_PRODUCT_ID + " TEXT,"
				+ SPARE_DESCRIPTION + " TEXT," + SPARE_SALES_PRICE + " TEXT," + SPARE_BARCODE + " TEXT,"
				+ SPARE_QUANTITY + " TEXT" + ")";

		db.execSQL(CREATE_SPARE_PARTS_MASTER_TABLE);

		// create table for spare Parts Master table
		String CREATE_SPARE_ADDED_MACHINE_TABLE = "CREATE TABLE " + TABLE_SPARE_ADDED_MACHINE + "(" + SPARE_ADDED_INC
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + SPARE_ADDED_ID + " TEXT," + SPARE_ADDED_JOB_ID + " TEXT,"
				+ SPARE_ADDED_PRODUCT_ID + " TEXT," + SPARE_ADDED_DESCRIPTION + " TEXT," + SPARE_ADDED_SALES_PRICE
				+ " TEXT," + SPARE_ADDED_QUANTITY + " TEXT," + SPARE_ADDED_MACHINE_ID + " TEXT" + ")";

		db.execSQL(CREATE_SPARE_ADDED_MACHINE_TABLE);

		// create table for error code table
		String CREATE_ERROR_CODE_TABLE = "CREATE TABLE " + TABLE_ERROR_CODE_DESCRIPTOIN + "(" + ERROR_CODE_ID_AUTO
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ERROR_CODE_ID + " TEXT," + ERROR_CODE_NAME + " TEXT,"
				+ ERROR_CODE_DESC + " TEXT" + ")";

		db.execSQL(CREATE_ERROR_CODE_TABLE);

		// create table for countryMaster
		String CREATE_COUNTRY_MASTER_TABLE = "CREATE TABLE " + TABLE_COUNTRY_LIST + "(" + COUNTRIESID + " TEXT, "
				+ COUNTRIESNAME + " TEXT" + ")";

		db.execSQL(CREATE_COUNTRY_MASTER_TABLE);

		// create table for error code table
		String CREATE_JOBDETAIL_TABLE = "CREATE TABLE " + TABLE_JOBDETAIL + "(" + JOBDETAIL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + JOBDETAIL_JOB_ID + " TEXT," + JOBDETAIL_MACHINE_ID + " TEXT,"
				+ JOBDETAIL_ERROR_CODE + " TEXT," + JOBDETAIL_PROBLEM + " TEXT," + JOBDETAIL_STATUS + " TEXT,"
				+ JOBDETAIL_COMMENTS + " TEXT" + ")";

		db.execSQL(CREATE_JOBDETAIL_TABLE);

		// create table for error code table
		String CREATE_DOCKET_DETAIL_TABLE = "CREATE TABLE " + TABLE_DOCKET_DETAIL + "(" + DOCKET_DETAIL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + DOCKET_DETAIL_JOB_ID + " TEXT," + DOCKET_DETAIL_MACHINE_ID
				+ " TEXT," + DOCKET_DETAIL_JOBDETAIL_ID + " TEXT," + DOCKET_DETAIL_WORK_CARRIED + " TEXT,"
				+ DOCKET_DETAIL_PARTS_UNABLE_TO_FIND + " TEXT," + DOCKET_DETAIL_EOL_STATUS + " TEXT" + ")";

		db.execSQL(CREATE_DOCKET_DETAIL_TABLE);

		// create table for error code table
		String CREATE_MACHINE_IMAGE_TABLE = "CREATE TABLE " + TABLE_MACHINE_IMAGE + "(" + MACHINE_IMAGE_ID_AUTO
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + MACHINE_IMAGE_ID + " TEXT," + MACHINE_JOB_ID + " TEXT,"
				+ MACHINE_IMAGE_NAME + " TEXT" + ")";

		db.execSQL(CREATE_MACHINE_IMAGE_TABLE);

		// create table for error code table
		String CREATE_TRAINING_ADDED_TABLE = "CREATE TABLE " + TABLE_TRAINING_ADDED_TO_JOB + "(" + TRAINING_ID_AUTO
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRAINING_CLIENT_ID + " TEXT," + TRAINING_SITE_ID + " TEXT,"
				+ TRAINING_JOB_TITLE + " TEXT," + TRAINING_ASSIGNED_DATE + " TEXT," + TRAINING_CREATED_DATE + " TEXT,"
				+ TRAINING_JOB_ID + " TEXT" + ")";

		db.execSQL(CREATE_TRAINING_ADDED_TABLE);

		// create table for docket list
		String CREATE_DOCKET_LIST_TABLE = "CREATE TABLE " + TABLE_DOCKET_LIST + "(" + DOCKET_JOB_ID + " TEXT,"
				+ DOCKET_USERTYPE_ID + " TEXT," + DOCKET_ENGG_ID + " TEXT," + DOCKET_DATE + " TEXT," + CHECK_IN_TIME
				+ " TEXT," + CHECK_OUT_TIME + " TEXT," + PAT_TEST + " TEXT," + DOCKET_COMMENT + " TEXT," + DOCKET_STATUS
				+ " TEXT," + DOCKET_COMPLETE + " TEXT," + DOCKET_SIGNATURE + " TEXT," + DOCKET_SIGNATURE_NAME + " TEXT"
				+ ")";

		db.execSQL(CREATE_DOCKET_LIST_TABLE);

		// create table for training trainee list
		String CREATE_TRAINING_TRAINEE_TABLE = "CREATE TABLE " + TABLE_TRAINING_TRAINEE + "(" + TRAINING_TRAINEE_ID
				+ " TEXT," + TRAINING_TRAINEE_NAMES + " TEXT," + TRAINING_TRAINEE_DATE + " TEXT" + ")";

		db.execSQL(CREATE_TRAINING_TRAINEE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAYJOB_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINING_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOMORROWJOB_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_START_JOB);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE_VIEW);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE_SERVICE_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPARE_PARTS_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPARE_ADDED_MACHINE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ERROR_CODE_DESCRIPTOIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBDETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCKET_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINING_TRAINEE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCKET_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINING_ADDED_TO_JOB);

		// Create tables again
		onCreate(db);
	}

	// Adding new login details
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(ENGID, user.getId());
		values.put(ROLE, user.getRole());
		values.put(FIRSTNAME, user.getFirstName());
		values.put(LASTNAME, user.getLastName());
		values.put(ADDRESS, user.getAddress());
		values.put(STAFFNO, user.getStaffNo());
		values.put(SAGENO, user.getSageNo());
		values.put(EMAILID, user.getEmailId());
		values.put(PHONE, user.getPhone());
		values.put(COUNTRYID, user.getCountryId());
		values.put(COUNTY, user.getCounty());
		values.put(CITY, user.getCity());
		values.put(LATITUDE, user.getLatitude());
		values.put(LONGITUDE, user.getLongitude());
		values.put(COUNTRYNAME, user.getCountryName());
		values.put(SHIFTSTART, user.getShiftStart());
		values.put(SHIFTEND, user.getShiftEnd());
		values.put(PHOTOPATH, user.getPhoto());
		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}

	public User getUserDetails() {
		User user = new User();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				// get the data into array,or class variable
				user.setId(cursor.getString(0));
				user.setRole(cursor.getString(1));
				user.setFirstName(cursor.getString(2));
				user.setLastName(cursor.getString(3));
				user.setStaffNo(cursor.getString(4));
				user.setSageNo(cursor.getString(5));
				user.setEmailId(cursor.getString(6));
				user.setPhone(cursor.getString(7));
				user.setCountryId(cursor.getString(8));
				user.setCounty(cursor.getString(9));
				user.setCity(cursor.getString(10));
				user.setLatitude(cursor.getString(11));
				user.setLongitude(cursor.getString(11));

			} while (cursor.moveToNext());
		}
		db.close();
		return user;
	}

	// Adding new todays job list
	public void addTodaysJobDetails(String data) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOBONEROWITEM, data);
		values.put(CREATEDAT, getDateTime());

		// Inserting Row
		db.insert(TABLE_TODAYJOB_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection

	}

	// Add in tomorrowJob Database
	public void addTomorrowJobDetails(String data) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOBTOMORROWONEROWITEM, data);
		values.put(CREATEDTOMORROWAT, getTomorrowDateTime());

		// Inserting Row
		db.insert(TABLE_TOMORROWJOB_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection

	}

	// Retrieve from tomorrowJob Database
	public ArrayList<String> getAllTomorrowJobs() {
		ArrayList<String> listOfTomorrowJobs = new ArrayList<String>();

		String selectQuery = "SELECT  * FROM " + TABLE_TOMORROWJOB_LIST + " WHERE " + CREATEDTOMORROWAT + "= ?";
		String[] whereArgs = new String[] { getTomorrowDateTime() };
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {
				// get the data into array,or class variable
				listOfTomorrowJobs.add(cursor.getString(1));

			} while (cursor.moveToNext());
		}
		db.close();

		return listOfTomorrowJobs;
	}

	// Delete all records from tomorrowJob Database
	public void truncateTableForTomorrowJobs() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_TOMORROWJOB_LIST);
	}


	public ArrayList<String> getAllTodaysJobs() {
		ArrayList<String> listOfTodayJobs = new ArrayList<String>();

		String selectQuery = "SELECT  * FROM " + TABLE_TODAYJOB_LIST + " WHERE " + CREATEDAT + "= ?";
		String[] whereArgs = new String[] { getDateTime() };
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {
				// get the data into array,or class variable
				listOfTodayJobs.add(cursor.getString(1));

			} while (cursor.moveToNext());
		}
		db.close();

		return listOfTodayJobs;
	}

	public int getCountForTodaysJobs() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"SELECT count(*) from " + TABLE_TODAYJOB_LIST + " WHERE " + CREATEDAT + "=" + getDateTime(), null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		db.close();
		return count;
	}

	public void deleteTodaysAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_TODAYJOB_LIST);
		db.close();
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void addTrainingData(TrainingPojo trainingItem) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(TRAINING_ID, trainingItem.getTrainId());
		values.put(DOCKET_ID, trainingItem.getDocket_no());
		values.put(TRAINING_TITLE, trainingItem.getTitle());
		values.put(SITE_NAME, trainingItem.getSite());
		values.put(SECTOR_NAME, trainingItem.getSectorName());
		values.put(DUE_DATE, trainingItem.getDue_date());
		values.put(JOB_DATE, trainingItem.getRegister_date());
		values.put(STATUS, trainingItem.getTrainingStatus());
		values.put(CLIENT_NAME, trainingItem.getClientName());

		// Inserting Row
		db.insert(TABLE_TRAINING_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection

	}

	public void updateTrainingStatus(String training_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(STATUS, "Closed");

		db.update(TABLE_TRAINING_LIST, values, TRAINING_ID + "=?", new String[] { training_id });

		db.close();
	}

	public void deleteTrainingData() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_TRAINING_LIST);
	}

	public void addTrainingVideoData(TrainingVideoModel trainingVideoItem) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(VIDEO_ID, trainingVideoItem.getVideoId());
		values.put(VIDEO_TRAINING_ID, trainingVideoItem.getTrainingId());
		values.put(VIDEO_TITLE, trainingVideoItem.getVideoTitle());
		values.put(VIDEO_URL, trainingVideoItem.getVideoUrl());

		// Inserting Row
		db.insert(TABLE_VIDEO_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection

	}

	public ArrayList<TrainingPojo> getAllTrainingData() {
		ArrayList<TrainingPojo> trainingDataOneRow = new ArrayList<TrainingPojo>();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_TRAINING_LIST;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				TrainingPojo trainingItem = new TrainingPojo();

				String training_id = cursor.getString(1);
				String docket_id = cursor.getString(2);
				String training_title = cursor.getString(3);
				String site_name = cursor.getString(4);
				String sector_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);

				trainingItem.setTrainId(training_id);
				trainingItem.setDocket_no(docket_id);
				trainingItem.setTitle(training_title);
				trainingItem.setSite(site_name);
				trainingItem.setSectorName(sector_name);
				trainingItem.setDue_date(due_date);
				trainingItem.setRegister_date(job_date);
				trainingItem.setTrainingStatus(status);
				trainingItem.setClientName(client_name);

				trainingDataOneRow.add(trainingItem);

			} while (cursor.moveToNext());
		}
		db.close();
		return trainingDataOneRow;
	}

	public TrainingPojo getTrainingRowById(String id) {

		TrainingPojo trainingItem = new TrainingPojo();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_TRAINING_LIST + " WHERE " + TRAINING_ID + "= ?";
		String[] whereArgs = new String[] { id };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {

				String training_id = cursor.getString(1);
				String docket_id = cursor.getString(2);
				String training_title = cursor.getString(3);
				String site_name = cursor.getString(4);
				String sector_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);

				trainingItem.setTrainId(training_id);
				trainingItem.setDocket_no(docket_id);
				trainingItem.setTitle(training_title);
				trainingItem.setSite(site_name);
				trainingItem.setSectorName(sector_name);
				trainingItem.setDue_date(due_date);
				trainingItem.setRegister_date(job_date);
				trainingItem.setTrainingStatus(status);
				trainingItem.setClientName(client_name);

			} while (cursor.moveToNext());
		}
		db.close();
		return trainingItem;

	}

	public ArrayList<TrainingVideoModel> getVideoListOfTraining(String id) {
		ArrayList<TrainingVideoModel> trainingVideo = new ArrayList<TrainingVideoModel>();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_LIST + " WHERE " + VIDEO_TRAINING_ID + "= ?";
		String[] whereArgs = new String[] { id };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {
				TrainingVideoModel trainingVideoModel = new TrainingVideoModel();

				String videoId = cursor.getString(1);
				String videoTrainingId = cursor.getString(2);
				String videoTitle = cursor.getString(3);
				String videoURL = cursor.getString(4);

				trainingVideoModel.setVideoId(videoId);
				trainingVideoModel.setTrainingId(videoTrainingId);
				trainingVideoModel.setVideoTitle(videoTitle);
				trainingVideoModel.setVideoUrl(videoURL);

				trainingVideo.add(trainingVideoModel);

			} while (cursor.moveToNext());
		}
		db.close();
		return trainingVideo;
	}

	public void deleteTrainingVideoData() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_VIDEO_LIST);
		db.close();
	}

	private String getTomorrowDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		return dateFormat.format(tomorrow);
	}

	// Add operation for job_list database
	public void addDataToJobList(Job jobItem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOB_ID, jobItem.getId());
		values.put(DOCKETID, jobItem.getJobId());
		values.put(JOBTITLE, jobItem.getJobTitle());
		values.put(JOBSECTORNAME, jobItem.getSectorName());
		values.put(JOBSITENAME, jobItem.getSiteName());
		values.put(JOBDUEDATE, jobItem.getDueDate());
		values.put(JOBSTATUS, jobItem.getJobStatus());

		values.put(JOBCLIENTNAME, jobItem.getClient_name());
		values.put(JOBCONTRACTORNAME, jobItem.getContractor_name());
		values.put(JOBSITEADDRESS, jobItem.getSiteAddress());
		values.put(JOBSITEMANAGERLIST, jobItem.getSiteManagerList());
		values.put(JOBTASKDETAILS, jobItem.getTask_details());

		values.put(JOBUPDATEDON, "");
		values.put(JOBUPDATEDBY, "ENGINEER");
		values.put(JOBSTARTTIME, "");
		values.put(JOBSTARTPLACE, "");

		values.put(JOBSITEID, jobItem.getSiteid());
		values.put(JOBSITELATITUDE, jobItem.getLatitude());
		values.put(JOBSITELONGITUDE, jobItem.getLongitude());
		values.put(JOBSTARTDATE, jobItem.getJob_date());

		values.put(JOBSTART_PLACE, jobItem.getStartPlace());
		values.put(JOBFINISHPLACE, jobItem.getFinishPlace());
		values.put(JOBCLIENTID, jobItem.getClient_id());
		values.put(JOBCOMMENTS, jobItem.getComment());

		// Inserting Row
		db.insert(TABLE_JOB_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection

	}

	// count for job_list
	public int getCountForJobList(String jobid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT count(*) FROM " + TABLE_JOB_LIST + " where " + JOB_ID + " = ? ";

		String[] whereas = { jobid };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return count;
	}

	// Retrieve data from job_list table
	public ArrayList<Job> getAllDataFromJob() {
		ArrayList<Job> jobDataList = new ArrayList<Job>();

		SQLiteDatabase db = this.getWritableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JOB_LIST;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Job jobItem = new Job();

				String job_id = cursor.getString(1);
				String docket_id = cursor.getString(2);
				String job_title = cursor.getString(3);
				String sector_name = cursor.getString(4);
				String site_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);
				String contractor_name = cursor.getString(10);
				String site_address = cursor.getString(11);
				String site_manager_list = cursor.getString(12);
				String updated_on = cursor.getString(13);

				String start_time = cursor.getString(15);
				String start_place = cursor.getString(16);

				String site_id = cursor.getString(17);
				String latitude = cursor.getString(18);
				String longitude = cursor.getString(19);
				String task_details = cursor.getString(20);
				String docket_start_place = cursor.getString(21);
				String finish_place = cursor.getString(22);
				String client_id = cursor.getString(23);

				jobItem.setId(job_id);
				jobItem.setJobId(docket_id);
				jobItem.setJobTitle(job_title);
				jobItem.setSectorName(sector_name);
				jobItem.setSiteName(site_name);
				jobItem.setDueDate(due_date);
				jobItem.setJobStatus(status);
				jobItem.setClient_name(client_name);
				jobItem.setContractor_name(contractor_name);
				jobItem.setSiteAddress(site_address);
				jobItem.setSiteManagerList(site_manager_list);
				jobItem.setTask_details(task_details);
				jobItem.setJob_date(job_date);
				jobItem.setUpdatedOn(updated_on);
				jobItem.setJob_start_time(start_time);
				jobItem.setJob_start_place(start_place);
				jobItem.setSiteid(site_id);
				jobItem.setLatitude(latitude);
				jobItem.setLongitude(longitude);
				jobItem.setStartPlace(docket_start_place);
				jobItem.setFinishPlace(finish_place);
				jobItem.setClient_id(client_id);

				jobDataList.add(jobItem);

				// Log.e("TAG", "Job id with status::" + jobItem.getJobId() + "
				// and " + jobItem.getJobStatus());

			} while (cursor.moveToNext());
		}
		db.close();
		return jobDataList;
	}

	// Retrieve data from job_list table
	public ArrayList<Job> getAllDataFromJobForToday(String todaysDate) {
		ArrayList<Job> jobDataList = new ArrayList<Job>();

		SQLiteDatabase db = this.getWritableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JOB_LIST + " WHERE " + JOBDUEDATE + "=?";

		String[] whereArgs = new String[] { todaysDate };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {
				Job jobItem = new Job();

				String job_id = cursor.getString(1);
				String docket_id = cursor.getString(2);
				String job_title = cursor.getString(3);
				String sector_name = cursor.getString(4);
				String site_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);
				String contractor_name = cursor.getString(10);
				String site_address = cursor.getString(11);
				String site_manager_list = cursor.getString(12);
				String updated_on = cursor.getString(13);

				String start_time = cursor.getString(15);
				String start_place = cursor.getString(16);

				String site_id = cursor.getString(17);
				String latitude = cursor.getString(18);
				String longitude = cursor.getString(19);
				String task_details = cursor.getString(20);
				String docket_start_place = cursor.getString(21);
				String finish_place = cursor.getString(22);
				String client_id = cursor.getString(23);

				jobItem.setId(job_id);
				jobItem.setJobId(docket_id);
				jobItem.setJobTitle(job_title);
				jobItem.setSectorName(sector_name);
				jobItem.setSiteName(site_name);
				jobItem.setDueDate(due_date);
				jobItem.setJobStatus(status);
				jobItem.setClient_name(client_name);
				jobItem.setContractor_name(contractor_name);
				jobItem.setSiteAddress(site_address);
				jobItem.setSiteManagerList(site_manager_list);
				jobItem.setTask_details(task_details);
				jobItem.setJob_date(job_date);
				jobItem.setUpdatedOn(updated_on);
				jobItem.setJob_start_time(start_time);
				jobItem.setJob_start_place(start_place);
				jobItem.setSiteid(site_id);
				jobItem.setLatitude(latitude);
				jobItem.setLongitude(longitude);
				jobItem.setStartPlace(docket_start_place);
				jobItem.setFinishPlace(finish_place);
				jobItem.setClient_id(client_id);

				jobDataList.add(jobItem);

			} while (cursor.moveToNext());
		}
		db.close();

		return jobDataList;
	}

	public Job getJobItemByDocketId(String jobId) {
		Job jobItem = new Job();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JOB_LIST + " WHERE " + DOCKETID + "= ?";
		String[] whereArgs = new String[] { jobId };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);
		if (cursor.moveToFirst()) {
			do {

				String job_id = cursor.getString(1);
				// Log.e("TAG", "Azizzzzz >>> " + job_id);
				String docket_id = cursor.getString(2);
				String job_title = cursor.getString(3);
				String sector_name = cursor.getString(4);
				String site_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);
				// Log.e("TAG", "client_name >>> " + client_name);
				String contractor_name = cursor.getString(10);
				// Log.e("TAG", "contractor_name >>> " + contractor_name);
				String site_address = cursor.getString(11);
				// Log.e("TAG", "site_address >>> " + site_address);
				String site_manager_list = cursor.getString(12);
				// Log.e("TAG", "site_manager_list >>> " + site_manager_list);
				String updated_on = cursor.getString(13);
				// Log.e("TAG", "updated_on >>> " + updated_on);

				String start_time = cursor.getString(15);
				// Log.e("TAG", "start_time >>> " + start_time);
				String start_place = cursor.getString(16);
				// Log.e("TAG", "start_place >>> " + start_place);

				String site_id = cursor.getString(17);
				// Log.e("TAG", "site_id >>> " + site_id);
				String latitude = cursor.getString(18);
				// Log.e("TAG", "latitude >>> " + latitude);
				String longitude = cursor.getString(19);
				// Log.e("TAG", "longitude >>> " + longitude);
				String task_details = cursor.getString(20);
				String docket_start_place = cursor.getString(21);
				// Log.e("TAG", "docket_start_place >>> " + docket_start_place);
				String finish_place = cursor.getString(22);
				// Log.e("TAG", "finish_place >>> " + finish_place);
				String client_id = cursor.getString(23);

				// Log.e("TAG", "comment >>> " + cursor.getString(25));
				String comment = cursor.getString(25);

				jobItem.setId(job_id);
				jobItem.setJobId(docket_id);
				jobItem.setJobTitle(job_title);
				jobItem.setSectorName(sector_name);
				jobItem.setSiteName(site_name);
				jobItem.setDueDate(due_date);
				jobItem.setJobStatus(status);
				jobItem.setClient_name(client_name);
				jobItem.setContractor_name(contractor_name);
				jobItem.setSiteAddress(site_address);
				jobItem.setSiteManagerList(site_manager_list);
				jobItem.setTask_details(task_details);
				jobItem.setJob_date(job_date);
				jobItem.setUpdatedOn(updated_on);
				jobItem.setJob_start_time(start_time);
				jobItem.setJob_start_place(start_place);
				jobItem.setSiteid(site_id);
				jobItem.setLatitude(latitude);
				jobItem.setLongitude(longitude);
				jobItem.setStartPlace(docket_start_place);
				jobItem.setFinishPlace(finish_place);
				jobItem.setClient_id(client_id);
				jobItem.setComment(comment);

			} while (cursor.moveToNext());
		}
		db.close();

		return jobItem;

	}

	public Job getJobItemByJobId(String jobId) {
		Job jobItem = new Job();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JOB_LIST + " WHERE " + JOB_ID + "= ?";
		String[] whereArgs = new String[] { jobId };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);
		if (cursor.moveToFirst()) {
			do {

				String docket_id = cursor.getString(2);
				String job_title = cursor.getString(3);
				String site_name = cursor.getString(5);
				String client_name = cursor.getString(9);

				jobItem.setJobId(docket_id);
				jobItem.setJobTitle(job_title);
				jobItem.setSiteName(site_name);
				jobItem.setClient_name(client_name);

			} while (cursor.moveToNext());
		}
		db.close();

		return jobItem;

	}

	public void updateJobStatus(String jobId) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOBSTATUS, "Completed by Eng");

		db.update(TABLE_JOB_LIST, values, JOB_ID + " = ?", new String[] { jobId });
		db.close();

	}

	public int updateParkJobStatus(String jobId, String comment) {

		SQLiteDatabase db = this.getReadableDatabase();
		/*
		 * String strSQL =
		 * "UPDATE job_list SET status = 'Call Back',park_comment='" + comment +
		 * "' WHERE job_id = '" + jobId + "'";
		 * 
		 * db.execSQL(strSQL);
		 */

		ContentValues values = new ContentValues();

		values.put(JOBUPDATEDON, getDateTime());
		values.put(JOBSTATUS, "Call Back");
		values.put(JOBPARKDETAIL, comment);

		return db.update(TABLE_JOB_LIST, values, JOB_ID + " = ?", new String[] { jobId });

		// db.close();

	}

	// Delete all records from job list Database
	public void deleteJobListAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_JOB_LIST);
		db.close();
	}

	// Add in machineList Database
	public void addMachine(Machine machine) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINEID, machine.getMachine_id());
		values.put(MACHINESERIALNO, machine.getMachine_si_no());
		values.put(MACHINENAME, machine.getMachine_name());
		values.put(MACHINETYPE, machine.getMachine_type());
		values.put(MANUFACTURER, machine.getManufacturer());
		values.put(SITENAME, machine.getSite_name());
		values.put(MARKS, machine.getMarks());

		// Inserting Row
		db.insert(TABLE_MACHINE_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection
	}

	public int getMachineListCountByMachineId(String machineId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINE_LIST + " WHERE " + MACHINEID + "='" + machineId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return count;
	}

	// Add in siteList Database
	public void addSite(Site site) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(SITE_ID, site.getId());
		values.put(SITENAME2, site.getName());
		values.put(SITECITY, site.getCity());
		values.put(SITECOUNTY, site.getCounty());
		values.put(SITECLIENTNAME, site.getClientName());
		values.put(SITELATITUDE, site.getLatitude());
		values.put(SITELONGITUDE, site.getLongitude());
		values.put(SITEPHOTOPATH, site.getPhotoPath());
		values.put(SITEADDRESS, site.getSiteAddress());
		values.put(SITECONTRACTORNAME, site.getContractorName());

		// Inserting Row
		db.insert(TABLE_SITE_LIST, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection
	}

	// Retrieve from machineList Database
	public ArrayList<Machine> getMachineLIstDetails() {
		ArrayList<Machine> machineList = new ArrayList<Machine>();
		String selectQuery = "SELECT  * FROM " + TABLE_MACHINE_LIST;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Machine machine = new Machine();
				// get the data into array,or class variable
				machine.setMachine_id(cursor.getString(0));
				machine.setMachine_si_no(cursor.getString(1));
				machine.setMachine_name(cursor.getString(2));
				machine.setMachine_type(cursor.getString(3));
				machine.setManufacturer(cursor.getString(4));
				machine.setSite_name(cursor.getString(5));
				machine.setMarks(cursor.getString(6));
				machineList.add(machine);
			} while (cursor.moveToNext());
		}
		db.close();

		return machineList;
	}

	// Retrieve from siteList Database
	public ArrayList<Site> getSiteLIstDetails() {
		ArrayList<Site> siteList = new ArrayList<Site>();
		String selectQuery = "SELECT  * FROM " + TABLE_SITE_LIST;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Site site = new Site();
				// get the data into array,or class variable

				site.setId(cursor.getString(1));
				site.setName(cursor.getString(2));
				site.setCity(cursor.getString(3));
				site.setCounty(cursor.getString(4));
				site.setClientName(cursor.getString(5));
				site.setLatitude(cursor.getString(6));
				site.setLongitude(cursor.getString(7));
				site.setPhotoPath(cursor.getString(8));
				site.setSiteAddress(cursor.getString(9));
				site.setContractorName(cursor.getString(10));

				siteList.add(site);

			} while (cursor.moveToNext());
		}
		db.close();

		return siteList;
	}

	// Delete all records from machineList Database
	public void truncateTableForMachineLIst() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINE_LIST);
	}

	// Delete all records from siteList Database
	public void deletesiteListAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_SITE_LIST);
	}

	public void addDataToStartJob(StartJobModel startJobModel) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(START_JOB_ID, startJobModel.getJob_id());
		values.put(START_JOB_ENGINEER_ID, startJobModel.getEngineer_id());
		values.put(START_JOB_SITE_ID, startJobModel.getSite_id());
		values.put(DISTANCE, startJobModel.getDistance());
		values.put(DISTANCE_UNIT, startJobModel.getDistance_unit());
		values.put(START_TIME, startJobModel.getStart_time());
		values.put(END_TIME, startJobModel.getEnd_time());

		// inserting a row
		db.insert(TABLE_START_JOB, null, values);

		// Log.e("TAG", "INSERT DONE " + TABLE_START_JOB);

		db.close(); // Closing database connection

	}

	public ArrayList<StartJobModel> getAllStartData(String job_id) {

		ArrayList<StartJobModel> startJobArray = new ArrayList<StartJobModel>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_START_JOB + " WHERE " + START_JOB_ID + "= '" + job_id + "'";

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {

			StartJobModel startJobModel = new StartJobModel();

			String eng_id = cursor.getString(2);
			String site_id = cursor.getString(3);
			String distance = cursor.getString(4);
			String distance_unit = cursor.getString(5);
			String start_time = cursor.getString(6);
			String end_time = cursor.getString(7);

			startJobModel.setDistance(distance);
			startJobModel.setDistance_unit(distance_unit);
			startJobModel.setEnd_time(end_time);
			startJobModel.setEngineer_id(eng_id);
			startJobModel.setSite_id(site_id);
			startJobModel.setStart_time(start_time);

			startJobArray.add(startJobModel);

		}

		db.close(); // Closing database connection

		return startJobArray;

	}

	public void updateStartJobTable(String job_id, String end_date) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(END_TIME, end_date);

		db.update(TABLE_START_JOB, values, START_JOB_ID + " = ?", new String[] { job_id });

		db.close();

	}

	public void updateJobTable(Job jobIItem) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOBSTATUS, "Inprogress");
		values.put(JOBUPDATEDON, jobIItem.getUpdatedOn());
		values.put(JOBUPDATEDBY, "ENGINEER");
		values.put(JOBSTARTTIME, jobIItem.getJob_start_time());
		values.put(JOBSTARTPLACE, jobIItem.getJob_start_place());
		values.put(JOBSTART_PLACE, jobIItem.getJob_start_place());

		// Log.e("TAG", "Start Place >>> " + jobIItem.getJob_start_place());

		db.update(TABLE_JOB_LIST, values, DOCKET_ID + " = ?", new String[] { jobIItem.getJobId() });

		// Log.e("TAG", "Update done ");

		db.close();
	}

	public void updateJobTable(String job_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(JOBSTATUS, "Closed");

		db.update(TABLE_JOB_LIST, values, JOB_ID + " = ?", new String[] { job_id });

		// Log.e("TAG", "Update done Aziz::" + job_id);

		db.close();
	}

	public void addMachineDetails(MachineDetails machineDetails, String site_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_SITE_ID, site_id);
		values.put(MACHINE_MANUFACTURE_NAME, machineDetails.getManufacturer());
		values.put(MACHINE_TYPE, machineDetails.getMachine_type());
		values.put(MACHINE_MODEL, machineDetails.getMachine_model());
		values.put(MACHINE_NAME, machineDetails.getMachine_name());
		values.put(MACHINE_SERIAL_NO, machineDetails.getMachine_si_no());
		values.put(MACHINE_ADDED_BY, machineDetails.getMachine_added_by());
		values.put(MACHINE_VOLTAGE, machineDetails.getVoltage());
		values.put(MACHINE_SUCTION, machineDetails.getSuction());
		values.put(MACHINE_TRACTION, machineDetails.getTraction());
		values.put(MACHINE_WATER, machineDetails.getWater());
		values.put(MACHINE_MFG_YEAR, machineDetails.getMfg_year());
		values.put(MACHINE_MARKS, machineDetails.getMarks_avail());
		values.put(MACHINE_WORKS, machineDetails.getWork_order());
		values.put(MACHINE_VISUAL, machineDetails.getVisual_inspect());
		values.put(MACHINE_SPARE, machineDetails.getSpare_parts());

		// Inserting Row
		db.insert(TABLE_MACHINE_DETAILS, null, values);

		// Log.e("TAG", "DONE");

		db.close(); // Closing database connection
	}

	public ArrayList<MachineDetails> getMachineDetailsBySiteId(String site_id) {
		ArrayList<MachineDetails> machineDetails = new ArrayList<MachineDetails>();

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_MACHINE_DETAILS + " WHERE " + MACHINE_SITE_ID + "= ?";
		String[] whereArgs = new String[] { site_id };
		// Log.e("TAG", "MACHINE LISTING Site Id::" + site_id);
		// Log.e("TAG", "MACHINE LISTING FETCH::" + selectQuery);

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);

		if (cursor.moveToFirst()) {
			do {

				String machine_si_no = cursor.getString(6);
				String machine_name = cursor.getString(5);
				String machine_type = cursor.getString(3);
				String manufacturer = cursor.getString(2);
				String machine_model = cursor.getString(4);
				String machine_added_by = cursor.getString(7);
				String voltage = cursor.getString(8);
				String suction = cursor.getString(9);
				String traction = cursor.getString(10);
				String water = cursor.getString(11);
				String mfg_year = cursor.getString(12);

				MachineDetails machineDetails2 = new MachineDetails(manufacturer, machine_type, machine_model,
						machine_name, machine_si_no, machine_added_by, voltage, suction, traction, water, mfg_year);
				machineDetails2.setMarks_avail(cursor.getString(13));
				machineDetails2.setWork_order(cursor.getString(14));
				machineDetails2.setVisual_inspect(cursor.getString(15));
				machineDetails2.setSpare_parts(cursor.getString(16));

				machineDetails.add(machineDetails2);
				// Log.e("TAG", "Value Add for view site..");

			} while (cursor.moveToNext());
		}

		db.close();

		return machineDetails;
	}

	public int getMachineCountBySerialNo(String serialNo) {

		SQLiteDatabase db = this.getReadableDatabase();

		// Log.e("TAG", "Ready Checking Error:" + serialNo);

		String selectQuery = "SELECT  count(*) FROM " + TABLE_MACHINE_DETAILS + " WHERE " + MACHINE_SERIAL_NO + "= ?";
		String[] whereArgs = new String[] { serialNo };

		Cursor cursor = db.rawQuery(selectQuery, whereArgs);
		// Log.e("TAG", "Checking Error:" + serialNo);
		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		db.close();

		return count;
	}

	// Delete all records from siteList Database
	public void deleteMachineDetailsRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINE_DETAILS);
	}

	// Add records to machine view table
	public void addMachineViewData(MachineView machineView) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_VIEW_DETAILS_ID, machineView.getMachine_id());
		values.put(MACHINE_MASTER_NAME, machineView.getMachine_master_name());
		values.put(MACHINE_SL_NO, machineView.getMachine_sl_no());
		values.put(MACHINE_QR_CODE, machineView.getMachine_qr_code());
		values.put(MACHINE_ASSOCIATED_SITE, machineView.getSite_name());
		values.put(MACHINE_MANUFACTURER, machineView.getMachine_manufacturer());
		values.put(MACHINE_M_TYPE, machineView.getMachine_type());
		values.put(MACHINE_M_MODEL, machineView.getMachine_model());
		values.put(MACHINE_DESC, machineView.getMachine_desc());
		values.put(MACHINE_TOTAL_MARKS, machineView.getMachine_marks());
		values.put(MACHINE_YOUTUBE_LINK1, machineView.getYoutube_link1());
		values.put(MACHINE_YOUTUBE_LINK2, machineView.getYoutube_link2());
		values.put(MACHINE_PARTS_DRAWLING, machineView.getParts_drawling());
		values.put(MACHINE_USER_MANUAL, machineView.getUser_manual());
		values.put(MACHINE_DATA_SHEET, machineView.getData_sheet());
		values.put(MACHINE_PHOTO_PATH, machineView.getMachine_photo());

		db.insert(TABLE_MACHINE_VIEW, null, values);

		db.close();

	}

	public int getMachineViewCountByMachineId(String machine_id) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT count(*) FROM " + TABLE_MACHINE_VIEW + " WHERE " + MACHINE_VIEW_DETAILS_ID + " = ?";
		String[] whereas = { machine_id };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		db.close();

		return count;

	}

	// Get Machine serial number by machine id
	public MachineView getMachineViewDataByMachineId(String machineId) {

		SQLiteDatabase db = this.getReadableDatabase();

		MachineView machineView = new MachineView();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINE_VIEW + " WHERE " + MACHINE_VIEW_DETAILS_ID + " = ?";
		String[] whereas = { machineId };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		if (cursor.moveToFirst()) {
			do {

				machineView.setMachine_id(cursor.getString(1));
				machineView.setMachine_master_name(cursor.getString(2));
				machineView.setMachine_sl_no(cursor.getString(3));
				machineView.setMachine_qr_code(cursor.getString(4));
				machineView.setSite_name(cursor.getString(5));
				machineView.setMachine_manufacturer(cursor.getString(6));
				machineView.setMachine_type(cursor.getString(7));
				machineView.setMachine_model(cursor.getString(8));
				machineView.setMachine_desc(cursor.getString(9));
				machineView.setMachine_marks(cursor.getString(10));
				machineView.setYoutube_link1(cursor.getString(11));
				machineView.setYoutube_link2(cursor.getString(12));
				machineView.setParts_drawling(cursor.getString(13));
				machineView.setUser_manual(cursor.getString(14));
				machineView.setData_sheet(cursor.getString(15));
				machineView.setMachine_photo(cursor.getString(16));

			} while (cursor.moveToNext());
		}

		// Log.e("TAG", "machineSlNo >> " + machineSlNo);

		db.close();

		return machineView;

	}

	// Get records from machine view table based on machine id
	public MachineView getMachineViewData(String machine_id) {

		SQLiteDatabase db = this.getReadableDatabase();

		MachineView machineView = new MachineView();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINE_VIEW + " WHERE " + MACHINE_SL_NO + " = ?";
		String[] whereas = { machine_id };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		while (cursor.moveToNext()) {
			machineView.setMachine_id(cursor.getString(1));
			machineView.setMachine_master_name(cursor.getString(2));
			machineView.setMachine_sl_no(cursor.getString(3));
			machineView.setMachine_qr_code(cursor.getString(4));
			machineView.setSite_name(cursor.getString(5));
			machineView.setMachine_manufacturer(cursor.getString(6));
			machineView.setMachine_type(cursor.getString(7));
			machineView.setMachine_model(cursor.getString(8));
			machineView.setMachine_desc(cursor.getString(9));
			machineView.setMachine_marks(cursor.getString(10));
			machineView.setYoutube_link1(cursor.getString(11));
			machineView.setYoutube_link2(cursor.getString(12));
			machineView.setParts_drawling(cursor.getString(13));
			machineView.setUser_manual(cursor.getString(14));
			machineView.setData_sheet(cursor.getString(15));
			// Log.e("TAG", "Cursor Image::::" + cursor.getString(16));
			machineView.setMachine_photo(cursor.getString(16));
		}

		db.close();

		return machineView;

	}

	public String getMachineSlNoData(String qrCode) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + MACHINE_SL_NO + " FROM " + TABLE_MACHINE_VIEW + " WHERE " + MACHINE_QR_CODE
				+ " = ?";
		String[] whereas = { qrCode };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		String machineSlNo = "";

		if (cursor.moveToFirst()) {
			do {

				machineSlNo = cursor.getString(0);

			} while (cursor.moveToNext());
		}

		db.close();

		return machineSlNo;

	}

	// Get Machine serial number by machine id
	public String getMachineSlNoByMachineId(String machineId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + MACHINE_SL_NO + " FROM " + TABLE_MACHINE_VIEW + " WHERE "
				+ MACHINE_VIEW_DETAILS_ID + " = ?";
		String[] whereas = { machineId };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		String machineSlNo = "";

		if (cursor.moveToFirst()) {
			do {

				machineSlNo = cursor.getString(0);

			} while (cursor.moveToNext());
		}

		// Log.e("TAG", "machineSlNo >> " + machineSlNo);

		db.close();

		return machineSlNo;

	}

	// Delete all the records for table machine view
	public void deleteAllRecordsForMachineView() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINE_VIEW);

		db.close();
	}

	// Add records to Machine History table
	public void addMachineHistoryData(MachineHistory machineHistory) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_HISTORY_MACHINE_ID, machineHistory.getMachine_id());
		values.put(MACHINE_HISTORY_SITE_NAME, machineHistory.getSiteName());
		values.put(MACHINE_HISTORY_ASSIGN_DATE, machineHistory.getAssignedOn());

		db.insert(TABLE_MACHINE_HISTORY, null, values);

		db.close();
	}

	// Get records From Machine History table
	public ArrayList<MachineHistory> getMachineHistoryDataById(String machine_id) {

		ArrayList<MachineHistory> machineHistoryArray = new ArrayList<MachineHistory>();

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINE_HISTORY + " WHERE " + MACHINE_HISTORY_MACHINE_ID + " = ? ";
		String[] whereas = { machine_id };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		if (cursor.moveToFirst()) {
			do {
				MachineHistory machineHistory = new MachineHistory();

				machineHistory.setMachine_id(cursor.getString(1));
				machineHistory.setSiteName(cursor.getString(2));
				machineHistory.setAssignedOn(cursor.getString(3));

				Log.e("TAG", "machine site name >>> " + cursor.getString(2));

				machineHistoryArray.add(machineHistory);

			} while (cursor.moveToNext());
		}

		return machineHistoryArray;
	}

	// Delete all the records for table Machine History
	public void deleteAllRecordsForMachineHistory() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINE_HISTORY);

		db.close();
	}

	// Add records to Machine Service History
	public void addMachineServiceHistory(MachineServiceHistory machineServiceHistory) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_SERVICE_MACHINE_ID, machineServiceHistory.getMachine_id());
		values.put(MACHINE_SERVICE_ERROR_ID, machineServiceHistory.getErrorCode());
		values.put(MACHINE_SERVICE_PROBLEM, machineServiceHistory.getProblemcomments());
		values.put(MACHINE_SERVICE_ENGINEER_NAME, machineServiceHistory.getEngineerName());
		values.put(MACHINE_SERVICE_DATE_TIME, machineServiceHistory.getDateTime());
		values.put(MACHINE_SERVICE_ENGINEER_COMMENTS, machineServiceHistory.getEngineerComments());
		values.put(MACHINE_SERVICE_WORK_CARRIED_OUT, machineServiceHistory.getWorkCarriedOut());

		db.insert(TABLE_MACHINE_SERVICE_HISTORY, null, values);

		// Log.e("TAG", "DONE >> " + TABLE_MACHINE_SERVICE_HISTORY);

		db.close();
	}

	// Get records from Machine Service History table
	public ArrayList<MachineServiceHistory> getMachineServiceHistoryById(String machine_id) {

		ArrayList<MachineServiceHistory> machineServiceArray = new ArrayList<MachineServiceHistory>();

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINE_SERVICE_HISTORY + " WHERE " + MACHINE_SERVICE_MACHINE_ID
				+ " = ?";
		String[] whereas = { machine_id };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		if (cursor.moveToFirst()) {
			do {

				MachineServiceHistory machineServiceHistory = new MachineServiceHistory();

				machineServiceHistory.setMachine_id(cursor.getString(1));
				machineServiceHistory.setErrorCode(cursor.getString(2));
				machineServiceHistory.setProblemcomments(cursor.getString(3));
				machineServiceHistory.setEngineerName(cursor.getString(4));
				machineServiceHistory.setDateTime(cursor.getString(5));
				machineServiceHistory.setEngineerComments(cursor.getString(6));
				machineServiceHistory.setWorkCarriedOut(cursor.getString(7));

				machineServiceArray.add(machineServiceHistory);

			} while (cursor.moveToNext());
		}
		db.close();
		return machineServiceArray;
	}

	// Delete all the records for table Service History
	public void deleteAllRecordsForServiceHistory() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINE_SERVICE_HISTORY);

		db.close();
	}

	// Delete all records from machineMaster Database
	public void deletemachineMasterAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINEMASTER_LIST);
	}

	// Delete all records from machineModelMaster Database
	public void deletemachineModelMasterAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINEMODEL_LIST);
		db.close();
	}

	// Delete all records from machineTypeMaster Database
	public void deletemachineTypeMasterAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINETYPE_LIST);
		db.close();
	}

	// Delete all records from machineManufacturerMaster Database
	public void deletemachineManufacturerMasterrAllJobRecords() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_MACHINEMANUFACTURER_LIST);
		db.close();
	}

	public ArrayList<Machinetype> getListForMachineType(String var_manufacture_id) {
		// TODO Auto-generated method stub
		ArrayList<Machinetype> typeList = new ArrayList<Machinetype>();
		SQLiteDatabase db = this.getReadableDatabase();
		String rawQuery = "Select distinct t1.TYPEMASTER_ID,t1.TYPE_NAME from " + TABLE_MACHINETYPE_LIST
				+ " t1 INNER JOIN " + TABLE_MACHINEMASTER_LIST
				+ " t2 on t1.TYPEMASTER_ID=t2.TYPE_ID where t2.manufacturer_id=" + var_manufacture_id
				+ " order by t1.TYPE_NAME";

		Cursor cursor = db.rawQuery(rawQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Machinetype machinetype = new Machinetype();
				machinetype.setType_id(cursor.getString(0));
				machinetype.setType_name(cursor.getString(1));

				typeList.add(machinetype);
			} while (cursor.moveToNext());
		}
		db.close();
		return typeList;
	}

	public ArrayList<MachineMaster> getListForMachineMaster(String var_type_id, String manufacturer_id2) {
		// TODO Auto-generated method stub
		ArrayList<MachineMaster> masterMachineList = new ArrayList<MachineMaster>();
		SQLiteDatabase db = this.getReadableDatabase();
		String rawQuery = "Select distinct t1.machineMaster_id,t1.machine_name from " + TABLE_MACHINEMASTER_LIST
				+ " t1 INNER JOIN " + TABLE_MACHINETYPE_LIST
				+ " t2 on t1.type_id=t2.typeMaster_id where t2.typeMaster_id=" + var_type_id
				+ " and t1.manufacturer_id=" + manufacturer_id2 + " order by t1.machine_name";

		Cursor cursor = db.rawQuery(rawQuery, null);
		if (cursor.moveToFirst()) {
			do {
				MachineMaster machineMaster = new MachineMaster();
				machineMaster.setMachine_id(cursor.getString(0));
				machineMaster.setMachine_name(cursor.getString(1));

				masterMachineList.add(machineMaster);
			} while (cursor.moveToNext());
		}
		db.close();
		return masterMachineList;
	}

	public ArrayList<MachineModel> getModelForMachine(String machine_id2) {
		// TODO Auto-generated method stub
		ArrayList<MachineModel> machineModelList = new ArrayList<MachineModel>();
		SQLiteDatabase db = this.getReadableDatabase();
		String rawQuery = "Select distinct t1.modelMaster_id,t1.model_name from " + TABLE_MACHINEMODEL_LIST
				+ " t1 where t1.machine_id=" + machine_id2 + " order by t1.model_name";

		Cursor cursor = db.rawQuery(rawQuery, null);
		if (cursor.moveToFirst()) {
			do {
				MachineModel machineModel = new MachineModel();
				machineModel.setModel_id(cursor.getString(0));
				machineModel.setModel_name(cursor.getString(1));

				machineModelList.add(machineModel);
			} while (cursor.moveToNext());
		}
		db.close();
		return machineModelList;
	}

	public void addMachineToSiteMain(MachineSiteMain machineSiteMain) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_MANUFACTURER_NAME, machineSiteMain.getMachineMAnufacturer());
		values.put(MACHINE_MANUFACTURER_ID, machineSiteMain.getMachineMAnufacturer_id());
		values.put(MACHINE_TYPE_NAME, machineSiteMain.getMachineType());
		values.put(MACHINE_TYPE_ID, machineSiteMain.getMachineType_id());
		values.put(MACHINE, machineSiteMain.getMachineName());
		values.put(MACHINE_IDS, machineSiteMain.getMachineName_id());
		values.put(MACHINE_MODEL_NAME, machineSiteMain.getMachineModel());
		values.put(MACHINE_MODEL_ID, machineSiteMain.getMachineModel_id());
		values.put(MACHINE_VOLTAGE_VALUE, machineSiteMain.getMachineVoltage());
		values.put(MACHINE_SUCTION_VALUE, machineSiteMain.getMachineSuction());
		values.put(MACHINE_TRACTION_VALUE, machineSiteMain.getMachineTraction());
		values.put(MACHINE_WATER_VALUE, machineSiteMain.getMachineWater());
		values.put(MACHINE_SPARE_PARTS, machineSiteMain.getMachineSpareParts());
		values.put(MACHINE_WORK_ORDER, machineSiteMain.getMachineWorkOrder());
		values.put(MACHINE_MARKS_AVAIL, machineSiteMain.getMachineMarksAvail());
		values.put(MACHINE__VISUAL_INSPECTION, machineSiteMain.getMachineVisualInspection());
		values.put(MACHINE_YEAR, machineSiteMain.getMachineManuYear());
		values.put(MACHINE_WARRANTY, machineSiteMain.getMachineWarranty());
		values.put(MACHINE_SI_NO, machineSiteMain.getMachineSINo());
		values.put(PURCHASE_DATE, machineSiteMain.getPurchase_date());
		values.put(MACHINE_ADD_SITE_ID, machineSiteMain.getSite_id());
		values.put(MACHINE_SAVED_LOCALLY, machineSiteMain.getLocally());

		// Inserting Row
		db.insert(TABLE_ADDMACHINE_LIST, null, values);
		// Log.e("TAG", "DONE MACHINE LISTING.....");

		db.close(); // Closing database connection
	}

	public MachineSiteMain getMachineDataListByMachineSlNo(String machineSlNo) {

		MachineSiteMain machineSiteMain = new MachineSiteMain();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_ADDMACHINE_LIST + " WHERE " + MACHINE_SI_NO + "='" + machineSlNo + "'";

		// Log.e("TAG", "sql >>> " + sql);

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {

				String manufacture = cursor.getString(0);

				// //Log.e("TAG", "manufacture >>> " + manufacture);
				String type_name = cursor.getString(2);
				// //Log.e("TAG", "type_name >>> " + type_name);
				String machine_name = cursor.getString(4);
				// //Log.e("TAG", "machine_name >>> " + machine_name);
				String machine_id = cursor.getString(5);
				// //Log.e("TAG", "machine_id >>> " + machine_id);
				String model_name = cursor.getString(6);
				// //Log.e("TAG", "model_name >>> " + model_name);
				String machine_sl_no = cursor.getString(18);
				// //Log.e("TAG", "machine_sl_no >>> " + machine_sl_no);
				String site_id = cursor.getString(20);

				machineSiteMain.setMachineMAnufacturer(manufacture);

				machineSiteMain.setMachineType(type_name);

				machineSiteMain.setMachineName(machine_name);
				machineSiteMain.setMachineName_id(machine_id);
				machineSiteMain.setMachineModel(model_name);

				machineSiteMain.setMachineSINo(machine_sl_no);
				machineSiteMain.setSite_id(site_id);

			} while (cursor.moveToNext());
		}

		cursor.close();

		db.close();

		return machineSiteMain;

	}

	public ArrayList<MachineSiteMain> getMachineDataList(String siteId) {

		ArrayList<MachineSiteMain> machineSiteArraylist = new ArrayList<MachineSiteMain>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_ADDMACHINE_LIST + " WHERE " + MACHINE_ADD_SITE_ID + "='" + siteId + "'";

		// Log.e("TAG", "sql >>> " + sql);

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {

				String manufacture = cursor.getString(0);

				// //Log.e("TAG", "manufacture >>> " + manufacture);
				String type_name = cursor.getString(2);
				// //Log.e("TAG", "type_name >>> " + type_name);
				String machine_name = cursor.getString(4);
				// //Log.e("TAG", "machine_name >>> " + machine_name);
				String machine_id = cursor.getString(5);
				// //Log.e("TAG", "machine_id >>> " + machine_id);
				String model_name = cursor.getString(6);
				// //Log.e("TAG", "model_name >>> " + model_name);
				String machine_sl_no = cursor.getString(18);
				// //Log.e("TAG", "machine_sl_no >>> " + machine_sl_no);

				MachineSiteMain machineSiteMain = new MachineSiteMain();

				machineSiteMain.setMachineMAnufacturer(manufacture);

				machineSiteMain.setMachineType(type_name);

				machineSiteMain.setMachineName(machine_name);
				machineSiteMain.setMachineName_id(machine_id);
				machineSiteMain.setMachineModel(model_name);

				machineSiteMain.setMachineSINo(machine_sl_no);

				machineSiteArraylist.add(machineSiteMain);

			} while (cursor.moveToNext());
		}

		cursor.close();

		db.close();

		return machineSiteArraylist;

	}

	public int getCountAddMachineList(String machineSLNo) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT count(machine_si_no) FROM " + TABLE_ADDMACHINE_LIST + " WHERE " + MACHINE_SI_NO + "='"
				+ machineSLNo + "'";

		Cursor mCount = db.rawQuery(sql, null);
		int count = 0;
		while (mCount.moveToNext()) {
			count = mCount.getInt(0);
		}

		// Log.e("TAG", "count machine serial no >> " + count);

		mCount.close();
		db.close();
		return count;

	}

	public ArrayList<String> getSIMachineList() {
		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT machine_si_no FROM " + TABLE_ADDMACHINE_LIST;
		// Log.e("TAG", "sql >> " + sql);
		Cursor mCount = db.rawQuery(sql, null);
		ArrayList<String> siNo = new ArrayList<String>();
		while (mCount.moveToNext()) {
			// mCount.moveToFirst();
			// Log.e("TAG", "Value Of SI>>" + mCount.getString(0));
			siNo.add(mCount.getString(0));
		}

		// //Log.e("TAG", "count >> " + count);
		// Log.e("TAG", "sql >> " + sql);
		mCount.close();
		db.close();
		return siNo;

	}

	// Add in machineMaster Database
	public void addmachineMaster(MachineMaster machineMaster) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINEMASTER_ID, machineMaster.getMachine_id());
		values.put(TYPE_ID, machineMaster.getType_id());
		values.put(MANUFACTURER_ID, machineMaster.getManufacturer_id());
		values.put(MODEL_ID, machineMaster.getModel_id());
		values.put(MACHINEMASTER_NAME, machineMaster.getMachine_name());
		values.put(MACHINEMASTER_DESC, machineMaster.getMachine_desc());

		// Inserting Row
		db.insert(TABLE_MACHINEMASTER_LIST, null, values);

		// Log.e("TAG", "DONE MachineMaster");

		db.close(); // Closing database connection
	}

	public MachineMaster getMachineDataByMachineId(String machineId) {
		MachineMaster machineMaster = new MachineMaster();

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINEMASTER_LIST + " WHERE " + MACHINEMASTER_ID + "='" + machineId
				+ "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				machineMaster.setMachine_id(cursor.getString(1));
				machineMaster.setType_id(cursor.getString(2));
				machineMaster.setManufacturer_id(cursor.getString(3));
				machineMaster.setModel_id(cursor.getString(4));
				machineMaster.setMachine_name(cursor.getString(5));
			} while (cursor.moveToNext());
		}

		return machineMaster;
	}

	public int getMachineCountByMachineId(String machineId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINEMASTER_LIST + " WHERE " + MACHINEMASTER_ID + "='" + machineId
				+ "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		return count;
	}

	// Add in machineManufactureMaster Database
	public void addmachineManufactureMaster(MachineManufacturer machineManufacturerList) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MANUFACTURERMASTER_ID, machineManufacturerList.getManufacture_id());
		values.put(MANUFACTURER_NAME, machineManufacturerList.getManufacturer_name());

		// Inserting Row
		db.insert(TABLE_MACHINEMANUFACTURER_LIST, null, values);

		// Log.e("TAG", "DONE MachineManufacturer");

		db.close(); // Closing database connection
	}

	// Add in machineModelMaster Database
	public void addmachineModelMaster(MachineModel modelMaster) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MODELMASTER_ID, modelMaster.getModel_id());
		values.put(MACHINEMODEL_ID, modelMaster.getMachine_id());
		values.put(MODEL_NAME, modelMaster.getModel_name());

		// Inserting Row
		db.insert(TABLE_MACHINEMODEL_LIST, null, values);

		// Log.e("TAG", "DONE MachineMode");

		db.close(); // Closing database connection
	}

	public int getModelCountByModelId(String modelId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINEMODEL_LIST + " WHERE " + MODELMASTER_ID + "='" + modelId
				+ "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		db.close();

		return count;
	}

	// Retrieve from manufactureId for Database
	public ArrayList<MachineManufacturer> getManufactureIdLIstDetails() {
		ArrayList<MachineManufacturer> manufacturerList = new ArrayList<MachineManufacturer>();
		String selectQuery = "SELECT  manufacturerMaster_id,manufacturer_name FROM " + TABLE_MACHINEMANUFACTURER_LIST
				+ " order by manufacturer_name";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				MachineManufacturer machineManufacturer = new MachineManufacturer();
				// get the data into array,or class variable
				machineManufacturer.setManufacture_id(cursor.getString(0));
				machineManufacturer.setManufacturer_name(cursor.getString(1));
				// Log.e("TAG", "Value>>" + cursor.getString(0));
				// Log.e("TAG", "Value>>" + cursor.getString(1));
				manufacturerList.add(machineManufacturer);
			} while (cursor.moveToNext());
		}
		db.close();

		return manufacturerList;
	}

	// Add in machineTypeMaster Database
	public void addMachineTypeMaster(Machinetype machinetype) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(TYPEMASTER_ID, machinetype.getType_id());
		values.put(TYPE_NAME, machinetype.getType_name());
		values.put(TYPE_DESC, machinetype.getType_desc());

		// Inserting Row
		db.insert(TABLE_MACHINETYPE_LIST, null, values);

		//// Log.e("TAG", "DONE MachineType");

		db.close(); // Closing database connection
	}

	public int getMachineTypeCountByTypeId(String typeId) {

		SQLiteDatabase db = this.getWritableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_MACHINETYPE_LIST + " WHERE " + TYPEMASTER_ID + "='" + typeId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			do {
				count = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		db.close();
		return count;
	}

	public ArrayList<String> getMachineManufaturesId() {
		ArrayList<String> machineManufactureIds = new ArrayList<String>();

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "Select * from " + TABLE_MACHINEMANUFACTURER_LIST;

		Cursor cursor = db.rawQuery(rawQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String macnufactureId = cursor.getString(0);

				machineManufactureIds.add(macnufactureId);
			} while (cursor.moveToNext());
		}

		db.close();
		return machineManufactureIds;
	}

	public String getMachineNameFromMachineId(String machineId) {
		String machineName = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + MACHINEMASTER_NAME + " FROM " + TABLE_MACHINEMASTER_LIST + " WHERE "
				+ MACHINEMASTER_ID + "='" + machineId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				machineName = cursor.getString(0);

				//// Log.e("TAG", "Machine Name >> " + machineName);

			} while (cursor.moveToNext());
		}
		db.close();
		return machineName;
	}

	public String getMachineManufactureFromManufactureId(String machineId) {
		String manufactureName = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + MANUFACTURER_NAME + " FROM " + TABLE_MACHINEMANUFACTURER_LIST + " WHERE "
				+ MANUFACTURERMASTER_ID + "='" + machineId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				manufactureName = cursor.getString(0);

				//// Log.e("TAG", "Manufacture Name >> " + manufactureName);

			} while (cursor.moveToNext());
		}
		db.close();
		return manufactureName;
	}

	public String getMachineModelFromModelId(String modelId) {
		String modelName = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + MODEL_NAME + " FROM " + TABLE_MACHINEMODEL_LIST + " WHERE " + MODELMASTER_ID
				+ "='" + modelId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				modelName = cursor.getString(0);

				//// Log.e("TAG", "Manufacture Name >> " + modelName);

			} while (cursor.moveToNext());
		}
		db.close();
		return modelName;
	}

	public String getMachineTypeFromTypeId(String typeId) {
		String typeName = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT " + TYPE_NAME + " FROM " + TABLE_MACHINETYPE_LIST + " WHERE " + TYPEMASTER_ID + "='"
				+ typeId + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				typeName = cursor.getString(0);

				//// Log.e("TAG", "Manufacture Name >> " + typeName);

			} while (cursor.moveToNext());
		}
		db.close();
		return typeName;
	}

	public void insertStatement(String v){

		String sql = "INSERT OR REPLACE INTO " + TABLE_SPARE_PARTS_MASTER + " (" + SPARE_ID + ","+ SPARE_PRODUCT_ID + ","+ SPARE_DESCRIPTION + "," + SPARE_SALES_PRICE + "," + SPARE_BARCODE + "," +
				SPARE_QUANTITY +") VALUES "+v;

		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL(sql);
		db.close();
	}

	public void addSpareParts(MasterSpareParts masterSpareParts) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(SPARE_ID, masterSpareParts.getId());
		values.put(SPARE_PRODUCT_ID, masterSpareParts.getProduct_id());
		values.put(SPARE_DESCRIPTION, masterSpareParts.getDescription());
		values.put(SPARE_SALES_PRICE, masterSpareParts.getUnit_sales());
		values.put(SPARE_BARCODE, masterSpareParts.getBarcode());
		values.put(SPARE_QUANTITY, masterSpareParts.getQuantity());
		

		db.insert(TABLE_SPARE_PARTS_MASTER, null, values);

		// //Log.e("TAG", "DONE " + TABLE_SPARE_PARTS_MASTER);

		db.close();
	}
//sukesh mahato
	public void addSpareParts2(String id,String pid,String desc,String sp,String bcode,String qty) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(SPARE_ID, id);
		values.put(SPARE_PRODUCT_ID, pid);
		values.put(SPARE_DESCRIPTION, desc);
		values.put(SPARE_SALES_PRICE, sp);
		values.put(SPARE_BARCODE, bcode);
		values.put(SPARE_QUANTITY, qty);


		db.insert(TABLE_SPARE_PARTS_MASTER, null, values);

		// //Log.e("TAG", "DONE " + TABLE_SPARE_PARTS_MASTER);

		db.close();
	}

	public int getCountSparePartsById(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT count(*) FROM " + TABLE_SPARE_PARTS_MASTER + " WHERE " + SPARE_ID + " ='" + id + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int count = 0;

		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}

		db.close();
		return count;
	}

	public String countdata(){
		SQLiteDatabase db = this.getReadableDatabase();
		//String rawQuery = "SELECT * FROM table_spare_parts_master where id=2069";
		String rawQuery = "SELECT * FROM table_spare_parts_master ORDER BY id DESC LIMIT 1";
		Cursor cursor = db.rawQuery(rawQuery, null);
		String data="";
		if (cursor.moveToFirst()) {
			do {
				data = cursor.getString(0);

				//// Log.e("TAG", "Manufacture Name >> " + typeName);

			} while (cursor.moveToNext());
		}
		db.close();
		return data;
	}
	
	public MasterSpareParts[] getBarcodeSpareParts(String barcode) {
		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_SPARE_PARTS_MASTER + " WHERE " + SPARE_BARCODE + " LIKE'%" + barcode + "%'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		int recCount = cursor.getCount();

		Log.e("recCount", "recCount >> " + recCount);

		MasterSpareParts[] ObjectItemData = new MasterSpareParts[recCount];
		int x = 0;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MasterSpareParts masterSpareParts = new MasterSpareParts();
				String spareId = cursor.getString(1);
				String spareDescription = cursor.getString(2);
				String spareProductId = cursor.getString(3);
				String spareUnitSales = cursor.getString(4);
				String spareBarcode = cursor.getString(5);
				String spareQuantity = cursor.getString(6);
				Log.e("Barcode >> ", "spareBarcode >> " + spareBarcode);
				masterSpareParts.setId(spareId);
				masterSpareParts.setDescription(spareDescription);
				masterSpareParts.setProduct_id(spareProductId);
				masterSpareParts.setUnit_sales(spareUnitSales);
				masterSpareParts.setQuantity(spareQuantity);
				masterSpareParts.setBarcode(spareBarcode);
				ObjectItemData[x] = masterSpareParts;
				x++;

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return ObjectItemData;
	}

	public void truncateSpareParts() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_SPARE_PARTS_MASTER);
		db.close();
	}

	public MasterSpareParts[] getMatchedSpareParts(String spareString) {

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_SPARE_PARTS_MASTER + " WHERE " + SPARE_PRODUCT_ID + " like '%"
				+ spareString + "%' or " + SPARE_DESCRIPTION + " like '%" + spareString + "%'";

		//// Log.e("Set", "rawQuery >> " + rawQuery);

		Cursor cursor = db.rawQuery(rawQuery, null);

		int recCount = cursor.getCount();

		//// Log.e("recCount", "recCount >> " + recCount);

		MasterSpareParts[] ObjectItemData = new MasterSpareParts[recCount];
		int x = 0;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MasterSpareParts masterSpareParts = new MasterSpareParts();

				String spareId = cursor.getString(1);
				String spareDescription = cursor.getString(2);
				String spareProductId = cursor.getString(3);
				String spareUnitSales = cursor.getString(4);
				String spareBarcode = cursor.getString(5);
				String spareQuantity = cursor.getString(6);
				

				masterSpareParts.setId(spareId);
				masterSpareParts.setDescription(spareDescription);
				masterSpareParts.setProduct_id(spareProductId);
				masterSpareParts.setUnit_sales(spareUnitSales);
				masterSpareParts.setQuantity(spareQuantity);
				masterSpareParts.setBarcode(spareBarcode);

				ObjectItemData[x] = masterSpareParts;

				x++;

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return ObjectItemData;

	}

	public ArrayList<String> getMatchedSparePartsString(String spareString) {
		ArrayList<String> masterSparePartsArray = new ArrayList<String>();

		//// Log.e("Set", "Text >> " + spareString);

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT * FROM " + TABLE_SPARE_PARTS_MASTER + " WHERE " + SPARE_PRODUCT_ID + " like '%"
				+ spareString + "%' or " + SPARE_DESCRIPTION + " like '%" + spareString + "%'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		//// Log.e("Set", "rawQuery >> " + rawQuery);

		if (cursor.moveToFirst()) {
			do {
				MasterSpareParts masterSpareParts = new MasterSpareParts();

				String spareId = cursor.getString(1);
				String spareDescription = cursor.getString(2);
				String spareProductId = cursor.getString(3);
				String spareUnitSales = cursor.getString(4);
				String spareQuantity = cursor.getString(5);

				masterSpareParts.setId(spareId);
				masterSpareParts.setDescription(spareDescription);
				masterSpareParts.setProduct_id(spareProductId);
				masterSpareParts.setUnit_sales(spareUnitSales);
				masterSpareParts.setQuantity(spareQuantity);

				String spareStringtest = spareProductId + "||" + spareQuantity + "||" + spareDescription + "||"
						+ spareId;

				// Log.e("String", "Joint string >> " + spareStringtest);

				masterSparePartsArray.add(spareStringtest);

			} while (cursor.moveToNext());
		}
		db.close();

		return masterSparePartsArray;
	}

	public void addSpareToMachine(SparePartsModel sparePartsModel) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		// Log.e("TAG", "spare parts >>> " + sparePartsModel.getSparePartsId());

		values.put(SPARE_ADDED_ID, sparePartsModel.getSpareId());
		values.put(SPARE_ADDED_JOB_ID, sparePartsModel.getJobId());
		values.put(SPARE_ADDED_PRODUCT_ID, sparePartsModel.getSparePartsId());
		values.put(SPARE_ADDED_DESCRIPTION, sparePartsModel.getDescription());
		values.put(SPARE_ADDED_SALES_PRICE, sparePartsModel.getUnitSales());
		values.put(SPARE_ADDED_QUANTITY, sparePartsModel.getQuantity());
		values.put(SPARE_ADDED_MACHINE_ID, sparePartsModel.getMachineId());

		db.insert(TABLE_SPARE_ADDED_MACHINE, null, values);

		// Log.e("TAG", "spare added done");

		db.close();

	}

	public void deleteSparePartsByMachineId(String machineId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "DELETE FROM " + TABLE_SPARE_ADDED_MACHINE + " WHERE " + SPARE_ADDED_MACHINE_ID + " = "
				+ machineId;

		db.execSQL(sql);
	}

	public void addErrorCodeToTable(ErrorCodeModel errorCodeModel) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(ERROR_CODE_ID, errorCodeModel.getId());
		values.put(ERROR_CODE_NAME, errorCodeModel.getErrorCode());
		values.put(ERROR_CODE_DESC, errorCodeModel.getProblem());

		db.insert(TABLE_ERROR_CODE_DESCRIPTOIN, null, values);

		Log.e("TAG", "Error code done");

		db.close();

	}

	public ArrayList<ErrorCodeModel> getAllErrorCodeDetails() {
		ArrayList<ErrorCodeModel> erArrayList = new ArrayList<ErrorCodeModel>();

		String rawQuery = "SELECT * FROM " + TABLE_ERROR_CODE_DESCRIPTOIN;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(rawQuery, null);

		if (cursor.moveToFirst()) {
			do {
				String id = cursor.getString(1);
				String error_code = cursor.getString(2);
				String desc = cursor.getString(3);

				ErrorCodeModel errorCodeModel = new ErrorCodeModel();

				errorCodeModel.setId(id);
				errorCodeModel.setErrorCode(error_code);
				errorCodeModel.setProblem(desc);

				erArrayList.add(errorCodeModel);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return erArrayList;
	}

	public void truncateErrorCodeTable() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_ERROR_CODE_DESCRIPTOIN);
		db.close();
	}

	public SiteEnggModel SiteEnggDetails(String id2, FragmentActivity activity) {
		// TODO Auto-generated method stub
		SiteEnggModel siteEnggModel = new SiteEnggModel();
		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT first_name,last_name,phone,address,city,county,latitude,longitude,country_name,shift_start,shift_end,photo_path FROM "
				+ TABLE_LOGIN + " WHERE " + ENGID + "='" + id2 + "'";

		Cursor cursor = db.rawQuery(rawQuery, null);

		while (cursor.moveToNext()) {

			siteEnggModel.setFirstName(cursor.getString(0));
			siteEnggModel.setLastName(cursor.getString(1));
			siteEnggModel.setMobNo(cursor.getString(2));
			siteEnggModel.setAddress(cursor.getString(3));
			siteEnggModel.setCityName(cursor.getString(4));
			siteEnggModel.setCountyName(cursor.getString(5));
			siteEnggModel.setLatitude(cursor.getString(6));
			siteEnggModel.setLogitude(cursor.getString(7));
			siteEnggModel.setCountryName(cursor.getString(8));
			siteEnggModel.setShiftStart(cursor.getString(9));
			siteEnggModel.setShiftEnd(cursor.getString(10));
			siteEnggModel.setPhotoPath(cursor.getString(11));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(0));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(1));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(2));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(3));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(4));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(5));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(6));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(7));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(8));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(9));
			// Log.e("TAG", "Profile Name >> " + cursor.getString(10));
			// Log.e("TAG", "Profile Photo >> " + cursor.getString(11));
		}
		db.close();
		return siteEnggModel;
	}

	public void addCountry(CountryModel countryModel) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(COUNTRIESID, countryModel.getCountryId());
		values.put(COUNTRIESNAME, countryModel.getCountryName());

		// Inserting Row
		db.insert(TABLE_COUNTRY_LIST, null, values);

		// Log.e("TAG", "COUNTRIES DONE");

		db.close(); // Closing database connection
	}

	public void truncateTableForCountry() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from " + TABLE_COUNTRY_LIST);
	}

	public ArrayList<CountryModel> getCountry() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<CountryModel> countryModelList = new ArrayList<CountryModel>();
		String rawQuery = "SELECT country_id, country_name FROM " + TABLE_COUNTRY_LIST;
		Cursor cursor = db.rawQuery(rawQuery, null);

		while (cursor.moveToNext()) {
			CountryModel countryModel = new CountryModel();
			countryModel.setCountryId(cursor.getString(0));
			countryModel.setCountryName(cursor.getString(1));

			// Log.e("TAG", "Country Id >> " + cursor.getString(0));
			// Log.e("TAG", "country Name >> " + cursor.getString(1));
			countryModelList.add(countryModel);
		}
		db.close();
		return countryModelList;
	}

	public void addJobDetails(JobDetails jobDetails) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(JOBDETAIL_JOB_ID, jobDetails.getJobId());
		values.put(JOBDETAIL_MACHINE_ID, jobDetails.getMachineId());
		values.put(JOBDETAIL_ERROR_CODE, jobDetails.getErrorCode());
		values.put(JOBDETAIL_PROBLEM, jobDetails.getProblem());
		values.put(JOBDETAIL_STATUS, "new");
		values.put(JOBDETAIL_COMMENTS, "");

		db.insert(TABLE_JOBDETAIL, null, values);

		// Log.e("TAG", "DONE" + TABLE_JOBDETAIL);

		db.close();
	}

	public int getCountForJobDetails(String jobId, String machineId, String error_code) {
		int count = 0;

		String sql = "SELECT count(*) FROM " + TABLE_JOBDETAIL + " WHERE " + JOBDETAIL_JOB_ID + " = ? AND "
				+ JOBDETAIL_MACHINE_ID + " = ? AND " + JOBDETAIL_ERROR_CODE + " = ?";

		String[] whereas = { jobId, machineId, error_code };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}

		db.close();
		cursor.close();

		return count;
	}

	public void deleteJobDetailsById(String jobDetailId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "DELETE FROM " + TABLE_JOBDETAIL + " WHERE " + JOBDETAIL_ID + " = " + jobDetailId;

		db.execSQL(sql);
	}

	public ArrayList<JobDetails> getAllJobDetails(String jobId) {
		ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_JOBDETAIL + " WHERE " + JOBDETAIL_JOB_ID + " = ?";

		String[] whereas = { jobId };

		Cursor cursor = db.rawQuery(sql, whereas);

		// Log.e("TAG", "Query for jobDetails::" + sql + " " + jobId);

		// if (cursor.moveToFirst()) {
		while (cursor.moveToNext()) {
			String jobDetailId = cursor.getString(0);
			String job_id = cursor.getString(1);
			String machineId = cursor.getString(2);
			String error_code = cursor.getString(3);
			String problem = cursor.getString(4);

			JobDetails jobDetails = new JobDetails();

			jobDetails.setId(jobDetailId);
			jobDetails.setJobId(job_id);
			jobDetails.setMachineId(machineId);
			jobDetails.setErrorCode(error_code);
			jobDetails.setProblem(problem);

			jobDetailsArray.add(jobDetails);

		}
		/*
		 * while (cursor.moveToNext()) ;
		 */
		// }
		db.close();
		return jobDetailsArray;
	}

	public void addDocketDetail(DocketDetail docketDetail) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(DOCKET_DETAIL_JOB_ID, docketDetail.getJob_id());
		values.put(DOCKET_DETAIL_MACHINE_ID, docketDetail.getMachine_id());
		values.put(DOCKET_DETAIL_JOBDETAIL_ID, docketDetail.getJobdetail_id());
		values.put(DOCKET_DETAIL_WORK_CARRIED, docketDetail.getWork_carried_out());
		values.put(DOCKET_DETAIL_PARTS_UNABLE_TO_FIND, docketDetail.getParts_unable_to_find());
		values.put(DOCKET_DETAIL_EOL_STATUS, docketDetail.getEol());

		db.insert(TABLE_DOCKET_DETAIL, null, values);

		// Log.e("TAG", "TAble docket details >> Done");

		db.close();
	}

	public void deleteDocketDetailByJobdetailId(String jobDetailId) {

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "DELETE FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOBDETAIL_ID + " = "
				+ jobDetailId;

		db.execSQL(sql);
		db.close();
	}

	public void updateDocketDetailById(String jobDetail_id, DocketDetail docketDetail) {
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(DOCKET_DETAIL_JOB_ID, docketDetail.getJob_id());
		values.put(DOCKET_DETAIL_MACHINE_ID, docketDetail.getMachine_id());
		values.put(DOCKET_DETAIL_WORK_CARRIED, docketDetail.getWork_carried_out());
		values.put(DOCKET_DETAIL_PARTS_UNABLE_TO_FIND, docketDetail.getParts_unable_to_find());
		values.put(DOCKET_DETAIL_EOL_STATUS, docketDetail.getEol());

		db.update(TABLE_DOCKET_DETAIL, values, DOCKET_DETAIL_JOBDETAIL_ID + " = ?", new String[] { jobDetail_id });

		db.close();
	}

	public int getCountDocketDetail(String jobDetailId) {
		int count = 0;

		String sql = "SELECT count(*) FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOBDETAIL_ID + " = ?";

		String[] whereas = { jobDetailId };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}

	public ArrayList<DocketDetail> getDocketDetailByJobId(String jobId) {

		ArrayList<DocketDetail> docketArrayList = new ArrayList<DocketDetail>();

		String sql = "SELECT * FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOB_ID + " = ?";

		String[] whereas = { jobId };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		// if (cursor.moveToFirst()) {
		while (cursor.moveToNext()) {

			DocketDetail docketDetail = new DocketDetail();

			String job_id = cursor.getString(1);
			String machineId = cursor.getString(2);
			String jobDetail_id = cursor.getString(3);
			String work_carried = cursor.getString(4);
			String parts_unable_to_find = cursor.getString(5);
			String eolStatus = cursor.getString(6);

			docketDetail.setJob_id(job_id);
			docketDetail.setMachine_id(machineId);
			docketDetail.setJobdetail_id(jobDetail_id);
			docketDetail.setWork_carried_out(work_carried);
			docketDetail.setParts_unable_to_find(parts_unable_to_find);
			docketDetail.setEol(eolStatus);

			docketArrayList.add(docketDetail);

		}
		// }
		db.close();
		return docketArrayList;

	}

	public DocketDetail getDocketDetailByJobDetailId(String jobDetailId) {

		DocketDetail docketDetail = new DocketDetail();

		String sql = "SELECT * FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOBDETAIL_ID + " = ?";

		String[] whereas = { jobDetailId };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		if (cursor.moveToFirst()) {
			do {

				String jobId = cursor.getString(1);
				String machineId = cursor.getString(2);
				String jobDetail_id = cursor.getString(3);
				String work_carried = cursor.getString(4);
				String parts_unable_to_find = cursor.getString(5);
				String eolStatus = cursor.getString(6);

				docketDetail.setJob_id(jobId);
				docketDetail.setMachine_id(machineId);
				docketDetail.setJobdetail_id(jobDetail_id);
				docketDetail.setWork_carried_out(work_carried);
				docketDetail.setParts_unable_to_find(parts_unable_to_find);
				docketDetail.setEol(eolStatus);

			} while (cursor.moveToNext());
		}
		db.close();
		return docketDetail;

	}

	public void addMachineImage(String job_detail_id, String job_id, String machine_image_name) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(MACHINE_IMAGE_ID, job_detail_id);
		values.put(MACHINE_JOB_ID, job_id);
		values.put(MACHINE_IMAGE_NAME, machine_image_name);

		db.insert(TABLE_MACHINE_IMAGE, null, values);

		// Log.e("TAG", "Insert Done " + TABLE_MACHINE_IMAGE);

		db.close();

	}

	public ArrayList<String> getMachineImageName(String job_detail_id) {

		ArrayList<String> machineImageArray = new ArrayList<String>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT " + MACHINE_IMAGE_NAME + " FROM " + TABLE_MACHINE_IMAGE + " WHERE " + MACHINE_IMAGE_ID
				+ " =" + job_detail_id;

		Cursor cursor = db.rawQuery(sql, null);

		// if (cursor.moveToFirst()) {
		while (cursor.moveToNext()) {
			String machineImageName = cursor.getString(0);
			machineImageArray.add(machineImageName);
		}
		// }
		db.close();
		return machineImageArray;
	}

	public void deleteSparePartsById(String spare_id) {

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "DELETE FROM " + TABLE_SPARE_ADDED_MACHINE + " WHERE id= " + spare_id;

		db.execSQL(sql);

		db.close();
	}

	public SparePartsModel getSparePartsById(String spare_id) {
		SparePartsModel sparePartsModel = new SparePartsModel();

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT id, spare_id, product_id, description, quantity, sales_price FROM "
				+ TABLE_SPARE_ADDED_MACHINE + " where id= " + spare_id + " and " + SPARE_ADDED_MACHINE_ID + "=0";

		Cursor cursor = db.rawQuery(rawQuery, null);

		while (cursor.moveToNext()) {

			sparePartsModel.setId(cursor.getString(0));
			sparePartsModel.setSpareId(cursor.getString(1));
			sparePartsModel.setSparePartsId(cursor.getString(2));
			sparePartsModel.setDescription(cursor.getString(3));
			sparePartsModel.setQuantity(cursor.getString(4));
			sparePartsModel.setUnitSales(cursor.getString(5));

			// Log.e("TAG", "Spare Id >> " + cursor.getString(0));
			// Log.e("TAG", "Spare Id >> " + cursor.getString(1));
			// Log.e("TAG", "Spare Parts Id >> " + cursor.getString(2));
			// Log.e("TAG", "Description >> " + cursor.getString(3));
			// Log.e("TAG", "Quantity >> " + cursor.getString(4));
			// Log.e("TAG", "Price >> " + cursor.getString(5));
		}

		db.close();

		return sparePartsModel;
	}

	public ArrayList<SparePartsModel> getSparePartsByJob(String job_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<SparePartsModel> sparePartsModelList = new ArrayList<SparePartsModel>();

		String rawQuery = "SELECT * FROM " + TABLE_SPARE_ADDED_MACHINE + " where spare_job_id= " + job_id;

		Cursor cursor = db.rawQuery(rawQuery, null);

		while (cursor.moveToNext()) {
			SparePartsModel sparePartsModel = new SparePartsModel();
			sparePartsModel.setSpareId(cursor.getString(1));
			sparePartsModel.setJobId(cursor.getString(2));
			sparePartsModel.setSparePartsId(cursor.getString(3));
			sparePartsModel.setDescription(cursor.getString(4));
			sparePartsModel.setUnitSales(cursor.getString(5));
			sparePartsModel.setQuantity(cursor.getString(6));
			sparePartsModel.setMachineId(cursor.getString(7));

			// Log.e("TAG", "Spare Id >> " + cursor.getString(0));
			// Log.e("TAG", "Spare Parts Id >> " + cursor.getString(1));
			// Log.e("TAG", "Description >> " + cursor.getString(2));
			// Log.e("TAG", "Quantity >> " + cursor.getString(3));
			// Log.e("TAG", "Price >> " + cursor.getString(4));
			sparePartsModelList.add(sparePartsModel);
		}
		db.close();
		return sparePartsModelList;
	}

	public ArrayList<SparePartsModel> getSpareParts(String job_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<SparePartsModel> sparePartsModelList = new ArrayList<SparePartsModel>();

		String rawQuery = "SELECT id, spare_id, description, quantity, sales_price FROM " + TABLE_SPARE_ADDED_MACHINE
				+ " where spare_job_id= '" + job_id + "' and " + SPARE_ADDED_MACHINE_ID + "=0";

		Cursor cursor = db.rawQuery(rawQuery, null);

		while (cursor.moveToNext()) {
			SparePartsModel sparePartsModel = new SparePartsModel();
			sparePartsModel.setSpareId(cursor.getString(0));
			sparePartsModel.setSparePartsId(cursor.getString(1));
			sparePartsModel.setDescription(cursor.getString(2));
			sparePartsModel.setQuantity(cursor.getString(3));
			sparePartsModel.setUnitSales(cursor.getString(4));

			// Log.e("TAG", "Spare Id >> " + cursor.getString(0));
			// Log.e("TAG", "Spare Parts Id >> " + cursor.getString(1));
			// Log.e("TAG", "Description >> " + cursor.getString(2));
			// Log.e("TAG", "Quantity >> " + cursor.getString(3));
			// Log.e("TAG", "Price >> " + cursor.getString(4));
			sparePartsModelList.add(sparePartsModel);
		}
		db.close();
		return sparePartsModelList;
	}

	public void addTrainingToJob(TrainingAddedToJob trainingAddedJob) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(TRAINING_CLIENT_ID, trainingAddedJob.getClient_id());
		values.put(TRAINING_SITE_ID, trainingAddedJob.getSite_id());
		values.put(TRAINING_JOB_TITLE, trainingAddedJob.getJob_title());
		values.put(TRAINING_ASSIGNED_DATE, trainingAddedJob.getAssign_date());
		values.put(TRAINING_CREATED_DATE, trainingAddedJob.getCreated_date());
		values.put(TRAINING_JOB_ID, trainingAddedJob.getJob_id());

		db.insert(TABLE_TRAINING_ADDED_TO_JOB, null, values);

		// Log.e("TAG", "Done training added >> ");

		db.close();
	}

	public ArrayList<TrainingAddedToJob> getTrainingDetails() {
		ArrayList<TrainingAddedToJob> trainingAddedArray = new ArrayList<TrainingAddedToJob>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql1 = "SELECT count(*) FROM " + TABLE_TRAINING_ADDED_TO_JOB;

		Cursor cursor1 = db.rawQuery(sql1, null);

		if (cursor1.moveToFirst()) {
			int count = cursor1.getInt(0);

			Log.e("TAG", "Coutn >> " + count);
		}

		String sql = "SELECT * FROM " + TABLE_TRAINING_ADDED_TO_JOB;

		Cursor cursor = db.rawQuery(sql, null);

		// if (cursor.moveToFirst()) {
		while (cursor.moveToNext()) {

			TrainingAddedToJob trainingAddedToJob = new TrainingAddedToJob();

			String training_id = cursor.getString(0);
			String jobTitle = cursor.getString(3);
			String assignedDate = cursor.getString(4);
			String createdDate = cursor.getString(5);
			String job_id = cursor.getString(6);

			trainingAddedToJob.setTraining_id(training_id);
			trainingAddedToJob.setJob_title(jobTitle);
			trainingAddedToJob.setAssign_date(assignedDate);
			trainingAddedToJob.setCreated_date(createdDate);
			trainingAddedToJob.setJob_id(job_id);

			trainingAddedArray.add(trainingAddedToJob);

		}
		// }
		db.close();
		return trainingAddedArray;
	}

	public void addDocketList(DocketList docketList) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		// Log.e("TAG", "job_id >> " + docketList.getJob_id());
		// Log.e("TAG", "user_type_id >> " + docketList.getUsertype_id());
		// Log.e("TAG", "eng_id >> " + docketList.getEngg_id());
		// Log.e("TAG", "docket_date >> " + docketList.getDocket_date());
		// Log.e("TAG", "check_in >> " + docketList.getCheck_in());
		// Log.e("TAG", "check_out >> " + docketList.getCheck_out());
		// Log.e("TAG", "pat_test >> " + docketList.getPad_test());
		// Log.e("TAG", "comment >> " + docketList.getComment());
		// Log.e("TAG", "status >> " + docketList.getStatus());
		// Log.e("TAG", "sign Name >> " + docketList.getSignName());
		// Log.e("TAG", "sign Decode >> " + docketList.getSignDecode());

		values.put(DOCKET_JOB_ID, docketList.getJob_id());
		values.put(DOCKET_USERTYPE_ID, docketList.getUsertype_id());
		values.put(DOCKET_ENGG_ID, docketList.getEngg_id());
		values.put(DOCKET_DATE, docketList.getDocket_date());
		values.put(CHECK_IN_TIME, docketList.getCheck_in());
		values.put(CHECK_OUT_TIME, docketList.getCheck_out());
		values.put(PAT_TEST, docketList.getPad_test());
		values.put(DOCKET_COMMENT, docketList.getComment());
		values.put(DOCKET_STATUS, docketList.getStatus());
		values.put(DOCKET_COMPLETE, docketList.getDocket_complete());
		values.put(DOCKET_SIGNATURE_NAME, docketList.getSignName());
		values.put(DOCKET_SIGNATURE, docketList.getSignDecode());

		// Inserting Row
		db.insert(TABLE_DOCKET_LIST, null, values);

		// Log.e("TAG", "DONE Aziz::" + docketList.getJob_id());

		db.close(); // Closing database connection
	}

	public void updateDocket(DocketList docketList) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		// Log.e("TAG", "job_id >> " + docketList.getJob_id());
		// Log.e("TAG", "user_type_id >> " + docketList.getUsertype_id());
		// Log.e("TAG", "eng_id >> " + docketList.getEngg_id());
		// Log.e("TAG", "docket_date >> " + docketList.getDocket_date());
		// Log.e("TAG", "check_in >> " + docketList.getCheck_in());
		// Log.e("TAG", "check_out >> " + docketList.getCheck_out());
		// Log.e("TAG", "pat_test >> " + docketList.getPad_test());
		// Log.e("TAG", "comment >> " + docketList.getComment());
		// Log.e("TAG", "status >> " + docketList.getStatus());

		values.put(DOCKET_USERTYPE_ID, docketList.getUsertype_id());
		values.put(DOCKET_ENGG_ID, docketList.getEngg_id());
		values.put(DOCKET_DATE, docketList.getDocket_date());
		values.put(CHECK_IN_TIME, docketList.getCheck_in());
		values.put(CHECK_OUT_TIME, docketList.getCheck_out());
		values.put(PAT_TEST, docketList.getPad_test());
		values.put(DOCKET_COMMENT, docketList.getComment());
		values.put(DOCKET_STATUS, docketList.getStatus());
		values.put(DOCKET_COMPLETE, docketList.getDocket_complete());

		db.update(TABLE_DOCKET_LIST, values, DOCKET_JOB_ID + " = ?", new String[] { docketList.getJob_id() });

		db.close();

	}

	public int getCountForDocket(String jobId) {
		int count = 0;

		String sql = "SELECT count(*) FROM " + TABLE_DOCKET_LIST + " WHERE " + DOCKET_JOB_ID + " = ?";

		String[] whereas = { jobId };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}

	public DocketList getDocketByJobId(String jobId) {
		DocketList docketList = new DocketList();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_DOCKET_LIST + " WHERE " + DOCKET_JOB_ID + " = ?";

		String[] whereas = { jobId };

		Cursor cursor = db.rawQuery(sql, whereas);

		// if (cursor.moveToFirst()) {
		while (cursor.moveToNext()) {

			/*
			 * String job_id = cursor.getString(1); String user_type_id =
			 * cursor.getString(2); String eng_id = cursor.getString(3); String
			 * docket_date = cursor.getString(4); String check_in =
			 * cursor.getString(5); String check_out = cursor.getString(6);
			 * String pat_test = cursor.getString(7); String comment =
			 * cursor.getString(8); String status = cursor.getString(9);
			 */

			// Log.e("TAG", "job_id >> " + job_id);
			// Log.e("TAG", "user_type_id >> " + user_type_id);
			// Log.e("TAG", "eng_id >> " + eng_id);
			// Log.e("TAG", "docket_date >> " + docket_date);
			// Log.e("TAG", "check_in >> " + check_in);
			// Log.e("TAG", "check_out >> " + check_out);
			// Log.e("TAG", "pat_test >> " + pat_test);
			// Log.e("TAG", "comment >> " + comment);
			// Log.e("TAG", "status >> " + status);

			docketList.setDocket_date(cursor.getString(3));
			docketList.setCheck_in(cursor.getString(4));
			docketList.setCheck_out(cursor.getString(5));
			docketList.setPad_test(cursor.getString(6));
			docketList.setComment(cursor.getString(7));
			docketList.setStatus(cursor.getString(8));
			docketList.setSignName(cursor.getString(11));
			docketList.setSignDecode(cursor.getString(10));
		}
		// }
		db.close();
		return docketList;

	}

	public ArrayList<DocketList> getDocketDetails() {

		ArrayList<DocketList> docketListArray = new ArrayList<DocketList>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_DOCKET_LIST;

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {

				DocketList docketList = new DocketList();

				String job_id = cursor.getString(0);
				/*
				 * String user_type_id = cursor.getString(1); String eng_id =
				 * cursor.getString(2); String docket_date =
				 * cursor.getString(3); String check_in = cursor.getString(4);
				 * String check_out = cursor.getString(5); String pat_test =
				 * cursor.getString(6); String comment = cursor.getString(7);
				 * String status = cursor.getString(8);
				 */

				// Log.e("TAG", "job_id >> " + job_id);
				// Log.e("TAG", "user_type_id >> " + user_type_id);
				// Log.e("TAG", "eng_id >> " + eng_id);
				// Log.e("TAG", "docket_date >> " + docket_date);
				// Log.e("TAG", "check_in >> " + check_in);
				// Log.e("TAG", "check_out >> " + check_out);
				// Log.e("TAG", "pat_test >> " + pat_test);
				// Log.e("TAG", "comment >> " + comment);
				// Log.e("TAG", "status >> " + status);

				docketList.setJob_id(job_id);
				docketList.setDocket_date(cursor.getString(3));
				docketList.setCheck_in(cursor.getString(4));
				docketList.setCheck_out(cursor.getString(5));
				docketList.setPad_test(cursor.getString(6));
				docketList.setComment(cursor.getString(7));
				docketList.setStatus(cursor.getString(8));

				docketListArray.add(docketList);

			} while (cursor.moveToNext());
		}
		db.close();
		return docketListArray;
	}

	public boolean getCountWorkDoneForJob(String jobDetailId) {

		// Log.e("TAG", "job details >> " + jobDetailId);

		boolean count = true;

		String sql = "SELECT id FROM " + TABLE_JOBDETAIL + " WHERE " + JOBDETAIL_JOB_ID + " = ?";

		String[] whereas = { jobDetailId };

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, whereas);

		while (cursor.moveToNext()) {
			String sql1 = "SELECT count(*) FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOBDETAIL_ID
					+ " = ?";

			String[] whereas1 = { cursor.getString(0) };

			db = this.getReadableDatabase();

			Cursor cursor1 = db.rawQuery(sql1, whereas1);

			while (cursor1.moveToNext()) {
				if (cursor1.getString(0).equalsIgnoreCase("1"))
					count = false;
			}
		}
		db.close();
		return count;
	}

	public boolean getWorkDoneForJobBoolean(String jobDetailId) {

		// Log.e("TAG", "job detail >> " + jobDetailId);

		boolean count = true;

		SQLiteDatabase db = this.getReadableDatabase();

		String sql1 = "SELECT count(*) FROM " + TABLE_DOCKET_DETAIL + " WHERE " + DOCKET_DETAIL_JOBDETAIL_ID + " = ?";

		String[] whereas1 = { jobDetailId };

		Cursor cursor1 = db.rawQuery(sql1, whereas1);

		if (cursor1.moveToFirst()) {
			int mCount = cursor1.getInt(0);

			if (mCount > 0) {
				count = false;
			} else {
				count = true;
			}

		}

		// Log.e("TAG", "count >> " + count);
		db.close();
		return count;
	}

	public void addTrainingTraineeName(String training_id, String trainee_names) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(TRAINING_TRAINEE_ID, training_id);
		values.put(TRAINING_TRAINEE_NAMES, trainee_names);
		values.put(TRAINING_TRAINEE_DATE, getTodayDate());

		// //Log.e("TAG", "TRAINING_TRAINEE_DATE >> " + getTodayDate());

		db.insert(TABLE_TRAINING_TRAINEE, null, values);

		db.close();

	}

	public void deleteTrainingTraineeTable() {
		String sql = "DELETE FROM " + TABLE_TRAINING_TRAINEE;

		SQLiteDatabase db = this.getReadableDatabase();

		db.execSQL(sql);

		db.close();
	}

	// get all data from training for Sync

	public ArrayList<TrainingSyncPojo> getTrainingSyncDetails() {

		ArrayList<TrainingSyncPojo> trainingSyncPojoList = new ArrayList<TrainingSyncPojo>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_TRAINING_TRAINEE;

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {

			TrainingSyncPojo trainingSyncPojo = new TrainingSyncPojo();

			String training_id = cursor.getString(0);
			String trainee_names = cursor.getString(1);
			String trainee_date = cursor.getString(2);

			trainingSyncPojo.setTraining_id(training_id);
			trainingSyncPojo.setTrainee_names(trainee_names);
			trainingSyncPojo.setTrainee_date(trainee_date);

			trainingSyncPojoList.add(trainingSyncPojo);

		}
		db.close();
		return trainingSyncPojoList;
	}

	public int getCountForStartJob(String enggId, String siteId, String job_id) {
		int count = 0;
		SQLiteDatabase db = this.getReadableDatabase();

		String sqlPro = "select " + JOBSTATUS + " from " + TABLE_JOB_LIST + " where site_id=?";
		String[] whereasPro = { siteId };
		Cursor cursorPro = db.rawQuery(sqlPro, whereasPro);

		while (cursorPro.moveToNext()) {

			if (cursorPro.getString(0).equalsIgnoreCase("Inprogress")) {
				count = 1;
			}

		}
		db.close();
		return count;
	}

	public void updateJobTableAtCloseDocket(String jobItemId, String time) {

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "select end_time from start_job where job_id='" + jobItemId + "'";
		Cursor cursorPro = db.rawQuery(sql, null);

		while (cursorPro.moveToNext()) {
			if (cursorPro.getString(0).equalsIgnoreCase("")) {
				ContentValues values = new ContentValues();
				values.put(END_TIME, time);

				// Log.e("TAG", "END_TIME>>> " + time);

				db.update(TABLE_START_JOB, values, JOB_ID + " = ?", new String[] { jobItemId });
			}
		}

		// Log.e("TAG", "Update done from docket");

		db.close();
	}

	// comment for Park job_list
	public String getCountForParkJob(String jobid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT updated_on ,park_comment FROM " + TABLE_JOB_LIST + " where " + JOB_ID + " = ? ";

		String[] whereas = { jobid };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		String park_comment = "";
		String updated_on = "";

		while (cursor.moveToNext()) {
			updated_on = cursor.getString(0);
			park_comment = cursor.getString(1);
		}

		cursor.close();
		db.close();

		return park_comment + "," + updated_on;
	}

	// count for Park job_list
	public boolean getCheckForParkJob(String jobid) {
		SQLiteDatabase db = this.getReadableDatabase();

		boolean check = true;

		String rawQuery = "SELECT park_comment FROM " + TABLE_JOB_LIST + " where " + JOB_ID + " = ? ";

		String[] whereas = { jobid };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		while (cursor.moveToNext()) {
			// Log.e("TAG", "JOB ID For Park Check::I M:" +
			// cursor.getString(0));
			if (cursor.getString(0) != null)
				check = false;
		}

		cursor.close();
		db.close();

		return check;
	}

	// Retrieve data from job_list table for sync
	public ArrayList<Job> getAllDataFromJobforSync(String Status) {
		ArrayList<Job> jobDataList = new ArrayList<Job>();

		SQLiteDatabase db = this.getWritableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JOB_LIST + " where " + JOBSTATUS + "='" + Status + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Job jobItem = new Job();

				String job_id = cursor.getString(1);
				String docket_id = cursor.getString(2);
				String job_title = cursor.getString(3);
				String sector_name = cursor.getString(4);
				String site_name = cursor.getString(5);
				String due_date = cursor.getString(6);
				String job_date = cursor.getString(7);
				String status = cursor.getString(8);
				String client_name = cursor.getString(9);
				String contractor_name = cursor.getString(10);
				String site_address = cursor.getString(11);
				String site_manager_list = cursor.getString(12);
				String updated_on = cursor.getString(13);

				String start_time = cursor.getString(15);
				String start_place = cursor.getString(16);

				String site_id = cursor.getString(17);
				String latitude = cursor.getString(18);
				String longitude = cursor.getString(19);
				String task_details = cursor.getString(20);
				String docket_start_place = cursor.getString(21);
				String finish_place = cursor.getString(22);
				String client_id = cursor.getString(23);

				jobItem.setId(job_id);
				jobItem.setJobId(docket_id);
				jobItem.setJobTitle(job_title);
				jobItem.setSectorName(sector_name);
				jobItem.setSiteName(site_name);
				jobItem.setDueDate(due_date);
				jobItem.setJobStatus(status);
				jobItem.setClient_name(client_name);
				jobItem.setContractor_name(contractor_name);
				jobItem.setSiteAddress(site_address);
				jobItem.setSiteManagerList(site_manager_list);
				jobItem.setTask_details(task_details);
				jobItem.setJob_date(job_date);
				jobItem.setUpdatedOn(updated_on);
				jobItem.setJob_start_time(start_time);
				jobItem.setJob_start_place(start_place);
				jobItem.setSiteid(site_id);
				jobItem.setLatitude(latitude);
				jobItem.setLongitude(longitude);
				jobItem.setStartPlace(docket_start_place);
				jobItem.setFinishPlace(finish_place);
				jobItem.setClient_id(client_id);

				jobDataList.add(jobItem);

			} while (cursor.moveToNext());
		}
		db.close();
		return jobDataList;
	}

	public ArrayList<MachineSiteMain> getMachineDataListForSync() {

		MachineSiteMain machineSiteMain;
		ArrayList<MachineSiteMain> machineSiteMainList = new ArrayList<MachineSiteMain>();

		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + TABLE_ADDMACHINE_LIST + " WHERE " + MACHINE_SAVED_LOCALLY + "='local'";

		// Log.e("TAG", "sql >>> " + sql);

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {

			machineSiteMain = new MachineSiteMain();

			// Log.e("TAG", "Saved Locally::" + cursor.getString(21));

			machineSiteMain.setMachineMAnufacturer(cursor.getString(0));
			machineSiteMain.setMachineMAnufacturer_id(cursor.getString(1));
			machineSiteMain.setMachineType(cursor.getString(2));
			machineSiteMain.setMachineType_id(cursor.getString(3));
			machineSiteMain.setMachineName(cursor.getString(4));
			machineSiteMain.setMachineName_id(cursor.getString(5));
			machineSiteMain.setMachineModel(cursor.getString(6));
			machineSiteMain.setMachineModel_id(cursor.getString(7));
			machineSiteMain.setMachineVoltage(cursor.getString(8));
			machineSiteMain.setMachineSuction(cursor.getString(9));
			machineSiteMain.setMachineTraction(cursor.getString(10));
			machineSiteMain.setMachineWater(cursor.getString(11));
			machineSiteMain.setMachineWorkOrder(cursor.getString(12));
			machineSiteMain.setMachineSpareParts(cursor.getString(13));
			machineSiteMain.setMachineMarksAvail(cursor.getString(14));
			machineSiteMain.setMachineVisualInspection(cursor.getString(15));
			machineSiteMain.setMachineManuYear(cursor.getString(16));
			machineSiteMain.setMachineWarranty(cursor.getString(17));
			machineSiteMain.setMachineSINo(cursor.getString(18));
			machineSiteMain.setPurchase_date(cursor.getString(19));
			machineSiteMain.setSite_id(cursor.getString(20));

			machineSiteMainList.add(machineSiteMain);
		}

		cursor.close();

		db.close();

		return machineSiteMainList;

	}

	// fetch model_id and machine_name from Machine_Master
	public String getModelMachineFromMaster(String machineId) {

		String returnData = "";
		String modelId = "";
		String machineName = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String rawQuery = "SELECT machine_model,machine_manufacturer FROM " + TABLE_MACHINE_VIEW + " where "
				+ MACHINE_VIEW_DETAILS_ID + " = ? ";

		String[] whereas = { machineId };

		Cursor cursor = db.rawQuery(rawQuery, whereas);

		while (cursor.moveToNext()) {
			modelId = cursor.getString(0);
			machineName = cursor.getString(1);
		}
		returnData = modelId + "," + machineName;

		return returnData;
	}

	public String getTodayDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public int updateMachineDetailsBySI(MachineSiteMain result, String machineSNo) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();

		int returnVal = 0;

		ContentValues values = new ContentValues();

		values.put(MACHINE_VOLTAGE, result.getMachineVoltage());
		values.put(MACHINE_TRACTION, result.getMachineTraction());
		values.put(MACHINE_SUCTION, result.getMachineSuction());
		values.put(MACHINE_WATER, result.getMachineWater());
		values.put(MACHINE_SPARE, result.getMachineSpareParts());
		values.put(MACHINE_MFG_YEAR, result.getMachineManuYear());
		values.put(MACHINE_MARKS, result.getMachineMarksAvail());
		values.put(MACHINE_WORKS, result.getMachineWorkOrder());
		values.put(MACHINE_VISUAL, result.getMachineVisualInspection());

		returnVal = db.update(TABLE_MACHINE_DETAILS, values, MACHINE_SERIAL_NO + " = ?", new String[] { machineSNo });

		db.close();
		return returnVal;

	}

	public int updateMachineBySI(MachineSiteMain result, String machineSNo) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getReadableDatabase();

		int returnVal = 0;

		ContentValues values = new ContentValues();

		values.put(MACHINE_VOLTAGE_VALUE, result.getMachineVoltage());
		values.put(MACHINE_TRACTION_VALUE, result.getMachineTraction());
		values.put(MACHINE_SUCTION_VALUE, result.getMachineSuction());
		values.put(MACHINE_WATER_VALUE, result.getMachineWater());
		values.put(MACHINE_SPARE_PARTS, result.getMachineSpareParts());
		values.put(MACHINE_YEAR, result.getMachineManuYear());
		values.put(MACHINE_MARKS_AVAIL, result.getMachineMarksAvail());
		values.put(MACHINE_WORK_ORDER, result.getMachineWorkOrder());
		values.put(MACHINE__VISUAL_INSPECTION, result.getMachineVisualInspection());

		returnVal = db.update(TABLE_ADDMACHINE_LIST, values, MACHINE_SI_NO + " = ?", new String[] { machineSNo });

		db.close();

		return returnVal;

	}

	public int getCountMachineHistory(String machineSLNo) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT count(machine_id) FROM " + TABLE_MACHINE_HISTORY + " WHERE " + MACHINE_HISTORY_MACHINE_ID
				+ "='" + machineSLNo + "'";

		Cursor mCount = db.rawQuery(sql, null);
		int count = 0;
		while (mCount.moveToNext()) {
			count = mCount.getInt(0);
		}

		// Log.e("TAG", "count machine serial no >> " + count);

		mCount.close();
		db.close();
		return count;

	}

	public int getCountMachineServiceHistory(String machineSLNo) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT count(machine_id) FROM " + TABLE_MACHINE_SERVICE_HISTORY + " WHERE "
				+ MACHINE_SERVICE_MACHINE_ID + "='" + machineSLNo + "'";

		Cursor mCount = db.rawQuery(sql, null);
		int count = 0;
		while (mCount.moveToNext()) {
			count = mCount.getInt(0);
		}

		// Log.e("TAG", "count machine serial no >> " + count);

		mCount.close();
		db.close();
		return count;

	}

	// Delete all table data except spare parts
	public void deletePreviousData() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM  " + TABLE_TODAYJOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_LIST);
		db.execSQL("DELETE FROM  " + TABLE_VIDEO_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TOMORROWJOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_JOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_LIST);
		db.execSQL("DELETE FROM  " + TABLE_SITE_LIST);
		db.execSQL("DELETE FROM  " + TABLE_START_JOB);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_DETAILS);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_VIEW);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_HISTORY);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_SERVICE_HISTORY);
		db.execSQL("DELETE FROM  " + TABLE_SPARE_ADDED_MACHINE);
		db.execSQL("DELETE FROM  " + TABLE_ERROR_CODE_DESCRIPTOIN);
		db.execSQL("DELETE FROM  " + TABLE_JOBDETAIL);
		db.execSQL("DELETE FROM  " + TABLE_DOCKET_DETAIL);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_TRAINEE);
		db.execSQL("DELETE FROM  " + TABLE_DOCKET_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_ADDED_TO_JOB);

		Log.d("TAG", "Previous Records Deleted");

	}

	// Delete all table data except spare parts
	public void deleteAllDataLogout() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM  " + TABLE_TODAYJOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_LIST);
		db.execSQL("DELETE FROM  " + TABLE_VIDEO_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TOMORROWJOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_JOB_LIST);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_LIST);
		db.execSQL("DELETE FROM  " + TABLE_SITE_LIST);
		db.execSQL("DELETE FROM  " + TABLE_START_JOB);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_DETAILS);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_VIEW);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_HISTORY);
		db.execSQL("DELETE FROM  " + TABLE_MACHINE_SERVICE_HISTORY);
		db.execSQL("DELETE FROM  " + TABLE_SPARE_ADDED_MACHINE);
		db.execSQL("DELETE FROM  " + TABLE_ERROR_CODE_DESCRIPTOIN);
		db.execSQL("DELETE FROM  " + TABLE_JOBDETAIL);
		db.execSQL("DELETE FROM  " + TABLE_DOCKET_DETAIL);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_TRAINEE);
		db.execSQL("DELETE FROM  " + TABLE_DOCKET_LIST);
		db.execSQL("DELETE FROM  " + TABLE_TRAINING_ADDED_TO_JOB);
		db.execSQL("DELETE FROM  " + TABLE_LOGIN);
		//db.execSQL("DELETE FROM  " + TABLE_COUNTRY_LIST);

		Log.d("TAG", "Previous Records Deleted");

	}

}
