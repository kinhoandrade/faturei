package com.oranz.faturei;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.oranz.faturei.R;
import com.oranz.dao.DBAdapter;
import com.oranz.entidades.Cartao;
import com.oranz.entidades.Compra;

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

        //dropTables();

        setContentView(R.layout.activity_main);
    }
	
	@SuppressWarnings("unused")
	private void dropTables(){
		try{
			db = db.open();
			db.dropTables();
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, Menu.NONE, R.string.info );
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
          case 1:   	
              Toast.makeText(this, "Faturei v1.0\nDesenvolvido por Oranz", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void incluirCompra(View view){    	
    	Intent nextScreen = new Intent(getApplicationContext(), IncluirCompra.class);
    	
      	nextScreen.putExtra("origin", "main");
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
	    		String id = cursor.getString(cursor.getColumnIndex("_id"));
	    		compraAux.setId(Integer.parseInt(id));
	    		compraAux.setValor(Double.parseDouble(cursor.getString(cursor.getColumnIndex("valor"))));
	    		compraAux.setData(cursor.getString(cursor.getColumnIndex("data")));
	    		compraAux.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
	    		compraAux.setParcelas(cursor.getString(cursor.getColumnIndex("parcelas")));
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
    
	public static List<Cartao> getCartoes() {
    	List<Cartao> cartoes = new ArrayList<Cartao>();
    	List<String> nomesCartoes = new ArrayList<String>();
    	try{
	    	db = db.open();
	    	Cursor cursor = db.getAllCartoes();
	    	String nome = "";
	    	String fechamento = "";
	    	
	    	cursor.moveToFirst();
	    	while (cursor.isAfterLast() == false){
		    	Cartao cartao = new Cartao();
		    	cartao.setId(cursor.getInt(cursor.getColumnIndex("_id")));
	    		nome = (cursor.getString(cursor.getColumnIndex("cartao")));
	    		fechamento = (cursor.getString(cursor.getColumnIndex("fechamento")));
	    		cartao.setCartao(nome);
	    		cartao.setFechamento(fechamento);
	    		if(!nomesCartoes.contains(nome)){
	    			nomesCartoes.add(nome);
	    			cartoes.add(cartao);
	    		}
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
			db.insertCompra(compra.getValor().toString(), compra.getData(), compra.getCartao(), compra.getDescricao(), compra.getParcelas());
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
	}
	
	public static void addCartao(String nome, String fechamento){
		try{
			db = db.open();
			db.insertCartao(nome, fechamento);
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
	
	public static void removeCompra(String id){
		try{
			db = db.open();
			db.deleteCompra(id);
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
