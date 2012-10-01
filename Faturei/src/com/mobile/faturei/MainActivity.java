package com.mobile.faturei;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.mobile.dao.DBAdapter;
import com.mobile.entidades.Compra;

public class MainActivity extends Activity {
	
	static List<Compra> compras;
	private static DBAdapter db;
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compras = new ArrayList<Compra>();
        try{
        	db = new DBAdapter(getApplicationContext());        	
        }catch(Exception e){
        	e.printStackTrace();        	
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void incluirCompra(View view){    	
    	Intent nextScreen = new Intent(getApplicationContext(), IncluirCompra.class);
    	
    	startActivity(nextScreen);
    }
    
    public void verFatura(View view){
    	Intent nextScreen = new Intent(getApplicationContext(), VerFatura.class);
    	
    	String operacoes = "";
    	Double total = 0.0;
    	int i = 1;    	
    	for (Compra compra : compras) {
			operacoes += "[" + i + "] " + compra.getData() + " = R$ " + compra.getValor() + "\n";
			total = total + compra.getValor();
			i++;
		}
    	operacoes += "\n";
    	operacoes += "Total = R$ " + total + "\n";
    	
    	nextScreen.putExtra("operacoes", operacoes);
    	startActivity(nextScreen);
    }
    
	public static void deleteAll() {
		try{
			db = db.open();
			db.deleteAll();;
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
    }
    
    public static List<Compra> getCompras() {
    	List<Compra> comprasAux = new ArrayList<Compra>();
    	try{
	    	db = db.open();
	    	Cursor cursor = db.getAllCompras();
	    	Compra compraAux = new Compra();
	    	
	    	cursor.moveToFirst();
	    	while (cursor.isAfterLast() == false){
	    		System.out.println(cursor.getString(cursor.getColumnIndex("valor")) + cursor.getString(cursor.getColumnIndex("data")) + cursor.getString(cursor.getColumnIndex("descricao")) + cursor.getString(cursor.getColumnIndex("cartao")));
	    		compraAux.setValor(Double.parseDouble(cursor.getString(cursor.getColumnIndex("valor"))));
	    		compraAux.setData(cursor.getString(cursor.getColumnIndex("data")));
	    		compraAux.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
	    		compraAux.setCartao(cursor.getString(cursor.getColumnIndex("cartao")));
	    		comprasAux.add(compraAux);
	    		compraAux = new Compra();
	    	    cursor.moveToNext();
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
   	
		return comprasAux;
	}
    
    public static List<String> getCartoes() {
    	List<String> cartoes = new ArrayList<String>();
    	try{
	    	db = db.open();
	    	Cursor cursor = db.getAllCartoes();
	    	String cartao = "";
	    	
	    	cursor.moveToFirst();
	    	while (cursor.isAfterLast() == false){
	    		cartao = (cursor.getString(cursor.getColumnIndex("cartao")));
	    		if(!cartoes.contains(cartao))
	    			cartoes.add(cartao);
	    	    cursor.moveToNext();
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
   	
		return cartoes;
	}

	public static void addCompra(Compra compra){
		try{
			db = db.open();
			db.insertCompra(compra.getValor().toString(), compra.getData(), compra.getCartao(), compra.getDescricao());
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
	}
	
	public static void addCartao(String nome, String vencimento){
		try{
			db = db.open();
			db.insertCartao(nome, vencimento);
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
	}
	
	public static void removeCartao(String nome){
		try{
			db = db.open();
			db.deleteCartao(nome);
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
	}
	
    public void irConfiguracoes(View view){    	
    	Intent nextScreen = new Intent(getApplicationContext(), Configuracoes.class);
    	
    	startActivity(nextScreen);
    }
    
}
