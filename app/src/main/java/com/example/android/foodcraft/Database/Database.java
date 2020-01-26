package com.example.android.foodcraft.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.android.foodcraft.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="FoodCraftDB.db";
    private static final int DB_VER=2;
    public Database(Context context)
    {
        super(context,DB_NAME,null,DB_VER);
    }
    public List<Order> getCarts()
    {
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"ID","FoodName","FoodId","Quantity","Price","Image"};
        String sqlTable="OrderDetails";

        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result=new ArrayList<>();
        if(c.moveToFirst())
        {
            do {

                    result.add(new Order(
                                    c.getInt(c.getColumnIndex("ID")),
                                    c.getString(c.getColumnIndex("FoodId")),
                                    c.getString(c.getColumnIndex("FoodName")),
                                    c.getString(c.getColumnIndex("Quantity")),
                                    c.getString(c.getColumnIndex("Price")),
                                    c.getString(c.getColumnIndex("Image"))
                    )

                    );
                }while(c.moveToNext());
            }
            return result;
    }

    public void  addToCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();



        String query=String.format("INSERT INTO OrderDetails(FoodId,FoodName,Quantity,Price,Image) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getImage());


        db.execSQL(query);
    }

    public  void deleteCart()
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format(Locale.ENGLISH, "DELETE from OrderDetails");
        db.execSQL(query);

    }

    public void updateCart(Order order) {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format(Locale.ENGLISH ,"UPDATE OrderDetails SET Quantity=%s WHERE ID=%d",order.getQuantity(),order.getID());
        db.execSQL(query);

    }

    public void removeFromCart(Order deleteItem) {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format(Locale.ENGLISH, "DELETE from OrderDetails where ID=%d",deleteItem.getID());
        db.execSQL(query);
    }

    public void addToFavorites(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO Favorites(FoodId) VALUES('%s');",foodId);
        db.execSQL(query);
    }
    public void removeFavorites(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM Favorites WHERE FoodId='%s';",foodId);
        db.execSQL(query);
    }

    public boolean isFavorite(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("SELECT * FROM Favorites WHERE FoodId='%s';",foodId);
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()<=0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
