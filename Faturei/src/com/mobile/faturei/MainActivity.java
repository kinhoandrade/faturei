package com.mobile.faturei;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

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
    
    private PopupWindow pw;
    @SuppressWarnings("unused")
	private void apagarRegistro(final int id) {
        new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deseja apagar o registro?")
                        .setMessage("Deseja apagar o registro?")
                        .setPositiveButton("SIM",
                                        new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                int which) {

                                                        //db.delete(id);

                                                        Toast.makeText(getApplicationContext(),
                                                                        "APAGADO COM SUCESSO",
                                                                        Toast.LENGTH_LONG).show();

                                                        pw.dismiss();
                                                }

                                        }).setNegativeButton("Não", null).show();
    }
    
    public static List<Compra> getCompras() {
    	List<Compra> comprasAux = new ArrayList<Compra>();
    	try{
	    	db.open();
	    	Cursor cursor = db.getAllCompras();
	    	Compra compraAux = new Compra();
	    	
	    	cursor.moveToFirst();
	    	while (cursor.isAfterLast() == false){
	    		compraAux.setValor(Double.parseDouble(cursor.getString(cursor.getColumnIndex("valor"))));
	    		comprasAux.add(compraAux);
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

	public static void addCompra(Compra compra){
		try{
			db.open();
			db.insertCompra(compra.getValor().toString(), compra.getData(), compra.getCartao(), compra.getDescricao());
			db.close();
			compras.add(compra);
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
    		db.close();
    	}
	}
}
