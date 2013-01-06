package com.oranz.faturei;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oranz.faturei.R;
import com.oranz.dao.DBAdapter;
import com.oranz.entidades.Cartao;
import com.oranz.entidades.Compra;

import com.tapfortap.AdView;
import com.tapfortap.TapForTap;

public class MainActivity extends Activity {
	
	static List<Compra> compras;
	private static DBAdapter db;
	private TextView faturaProxMes;
	
	@SuppressWarnings("unused")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        compras = new ArrayList<Compra>();
        faturaProxMes = (TextView) findViewById(R.id.faturaTextView);
        
        try{
        	db = new DBAdapter(getApplicationContext());    
        	db.close();
        }catch(Exception e){
        	e.printStackTrace();        	
        }

        //dropTables();
        
        TapForTap.initialize(this, "8607E43DACE92FBD0BA571439A4D38F0");
        
        AdView adView = (AdView) findViewById(R.id.ad_view);
        
        calculaFatura();
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
              Toast.makeText(this, "Faturei v1.7\nDesenvolvido por Oranz", Toast.LENGTH_LONG).show();
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
    		db.close();
    		cursor.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
   	
		return comprasAux;
	}
    
    public void calculaFatura(){
    	List<Compra> compras = getCompras();
        List<Cartao> listaCartao = MainActivity.getCartoes();
        Calendar cal = Calendar.getInstance(); 
        int mesEscolhidoInt = cal.get(Calendar.MONTH)+2;
        double total = 0;
        DecimalFormat df = new DecimalFormat("0.00"); 
    	
    	for (Compra compraAux : compras) {
    			String dataCompra = compraAux.getData().toString();
    			String cartaoCompra = compraAux.getCartao().toString();
    			String[] dataArray = dataCompra.split("/");
    			String diaCompra = dataArray[0];
    			String mesCompra = dataArray[1];
    			int diaFechamentoCartao = 0;
    			
    			for (Cartao cartaoAux : listaCartao) {
					if(cartaoAux.getCartao().equals(cartaoCompra)){
						if(cartaoAux.getFechamento().toString().length() > 0){
							if(cartaoAux.getFechamento().contains("/")){
								String[] dataFechamento = cartaoAux.getFechamento().split("/");
								diaFechamentoCartao = Integer.parseInt(dataFechamento[0]);
							}else{
								diaFechamentoCartao = Integer.parseInt(cartaoAux.getFechamento());
							}
						}else{
							diaFechamentoCartao = 0;}
					}
				}
    			
    			int diaCompraInt = Integer.parseInt( diaCompra );
    			int mesCompraInt = Integer.parseInt( mesCompra );
    			
    			if(mesCompraInt == 1){
    				mesCompraInt = 13;
    			}
    			
    			if( ((mesEscolhidoInt == mesCompraInt ) && diaCompraInt > diaFechamentoCartao ) || ((mesEscolhidoInt == mesCompraInt ) && diaCompraInt <= diaFechamentoCartao )){
    				total = total + Double.parseDouble(df.format(compraAux.getValor()));
    			}
		}  	
    	if (mesEscolhidoInt < 13){
    		faturaProxMes.setText("Fatura do mês " + mesEscolhidoInt + ":$" + df.format(total));
    	}else {
    		faturaProxMes.setText("Fatura do mês 01:$" + df.format(total));
    	}
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
	    	
	    	db.close();
	    	cursor.close();
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
