package com.shohrab.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shohrab.model.CarmudiModelOffline;
import com.shohrab.model.MetaData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SqlLiteDatabaseHandler extends SQLiteOpenHelper {
	
	// Books table name
	public static final String TABLE_CARMUDI = "cars";

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "carmudioffline";

	public SqlLiteDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
					
		String CREATE_CARMUDI_TABLE = "CREATE TABLE cars ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"car_name TEXT, "+
				"car_brand TEXT, "+
				"car_price TEXT, "+
				"car_image_url TEXT, "+
				"sorting_tag TEXT )";

		db.execSQL(CREATE_CARMUDI_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older plants table if existed
		db.execSQL("DROP TABLE IF EXISTS cars");
		
		// create fresh tables
		this.onCreate(db);
	}

	/**
	 * This method is used to insert cars
	 * @param carModels
	 */
	public void addCachedCars( List<MetaData.Result> carModels, String sortingTag){
        //First delete existing data
        deleteCachedData(sortingTag);

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		for(MetaData.Result offlineCar : carModels) {
			// 2. create ContentValues to add key "column"/value
			ContentValues values = new ContentValues();
			values.put("car_name", offlineCar.getData().getName());
			values.put("car_brand", offlineCar.getData().getBrand());
			values.put("car_price", offlineCar.getData().getPrice());
			values.put("car_image_url", offlineCar.getImages().get(0).getUrl()); //taking first images only
            values.put("sorting_tag", sortingTag);


			// 3. insert
			db.insert(TABLE_CARMUDI, // table
					null, //nullColumnHack
					values); // key/value -> keys = column names/ values = column values

		}
		// 4. close
		db.close(); 
	}

	
	// Get All cars
	public List<CarmudiModelOffline> getOfflineCars(String sortingTag) {
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		
		List<CarmudiModelOffline> offlineCarList = new LinkedList<CarmudiModelOffline>();

		// 1. build the query
		Cursor cursor = db.rawQuery("SELECT car_name, car_brand, car_price, car_image_url  FROM " + TABLE_CARMUDI +
                " where sorting_tag = ?", new String[]{sortingTag});

		// 3. go over each row, build book and add it to list
		
		if (cursor.moveToFirst()) {
			do {
				CarmudiModelOffline carObj = new CarmudiModelOffline();

				carObj.setCarName(cursor.getString(0));
				carObj.setCarBrand(cursor.getString(1));
				carObj.setCarPrice(cursor.getString(2));
				carObj.setCarImage(cursor.getString(3));

				offlineCarList.add(carObj);
				
			} while (cursor.moveToNext());
		}

		return offlineCarList;
	}
	
	// Delete cached Data
	public void deleteCachedData(String deleteTag) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		
		// 2. delete row
		 db.delete(TABLE_CARMUDI, //table
                 "sorting_tag = ?", // selections
                 new String[]{deleteTag}); //selection args

		// 4. close
		db.close();
	}

	public ArrayList<Cursor> getData(String Query){
		//get writable database
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		String[] columns = new String[] { "mesage" };
		//an array list of cursor to save two cursors one has results from the query
		//other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


		try{
			String maxQuery = Query ;
			//execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);


			//add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });

			alc.set(1,Cursor2);
			if (null != c && c.getCount() > 0) {


				alc.set(0,c);
				c.moveToFirst();

				return alc ;
			}
			return alc;
		} catch(SQLException sqlEx){
			Log.d("printing exception", sqlEx.getMessage());
			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		} catch(Exception ex){

			Log.d("printing exception", ex.getMessage());

			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+ex.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		}


	}

	
}