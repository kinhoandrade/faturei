package com.mobile.faturei;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.entidades.Compra;

public class VerFatura extends Activity {
	private Button btnVoltar;
	private TextView comprasView;
	private Spinner cartoes;
    private String array_spinner[];
    private DecimalFormat df = new DecimalFormat("0.00"); 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fatura);
        btnVoltar = (Button) findViewById(R.id.faturavoltar);
        comprasView = (TextView) findViewById(R.id.compras);
        List<String> listaCartoes = MainActivity.getCartoes();
        
        array_spinner= new String[listaCartoes.size()+1];
        int i = 1;
        array_spinner[0] = "TODOS";
        for (String cartao : listaCartoes) {
            array_spinner[i] = cartao;
            i++;
		}
        cartoes = (Spinner) findViewById(R.id.spinnercartoes);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        cartoes.setAdapter(adapter);
        cartoes.setSelection(0);
        
        verFatura();
        
        btnVoltar.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }
    
    public void verFatura(){
        List<Compra> compras = new ArrayList<Compra>();
        
        try {
        	compras = MainActivity.getCompras();
        	listaCompras(compras);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    public void verFaturaCartao(View view){
    	switch (view.getId()) {
	        case R.id.okfatura:
	        List<Compra> compras = new ArrayList<Compra>();
	        
	        try {
	        	compras = MainActivity.getCompras();
	        	listaCompras(compras);
	        }catch (Exception e) {
	            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	        }
    	}
    }
    
    public void listaCompras(List<Compra> lista){
    	String cartaoEscolhido = cartoes.getSelectedItem().toString();
        double total = 0.0;
    	
		comprasView.setText("");
    	for (Compra compraAux : lista) {
    		if(cartaoEscolhido.equals(compraAux.getCartao().toString()) || cartaoEscolhido.equals("TODOS")){
        		total = total + Double.parseDouble(df.format(compraAux.getValor()));
        		double compraValor = compraAux.getValor();
        		comprasView.setText(comprasView.getText() + " [" + compraAux.getCartao().toString() + "] " + compraAux.getData() + " = R$ " + df.format(compraValor) + " - " + compraAux.getDescricao() + "\n");
    		}
		}
    	comprasView.setText(comprasView.getText() + "\nTotal = R$ " + df.format(total));    	
    }

    public void limpar(View view){
        new AlertDialog.Builder(this)
        .setTitle("Apagar tudo")
        .setMessage("Deseja apagar todos os registros?")
        .setPositiveButton("SIM",
                        new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                	MainActivity.deleteAll();
                        			Toast.makeText(getApplicationContext(), "APAGADO COM SUCESSO", Toast.LENGTH_LONG).show();
                                }
                        }).setNegativeButton("Não", null).show();
        comprasView.setText("Total = R$ 0,00");
    }

}
