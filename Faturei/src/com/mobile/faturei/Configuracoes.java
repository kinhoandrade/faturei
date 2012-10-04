package com.mobile.faturei;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobile.entidades.Compra;

public class Configuracoes extends Activity {
	private Button btnVoltar;
	private EditText diaVencimento;
	private EditText nomeCartao;
	private Spinner cartoes;
    private String array_spinner[];
    private int dia;
    private Spinner compras;
    private DecimalFormat df = new DecimalFormat("0.00"); 
    
    List<Compra> comprasLista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        btnVoltar = (Button) findViewById(R.id.voltarConfiguracoes);
        nomeCartao = (EditText) findViewById(R.id.putNomeCartao);
        diaVencimento = (EditText)findViewById(R.id.diaEditText);
        compras = (Spinner) findViewById(R.id.spinnerCompras);
        dia = 1;
        
        carregaCartoes();
        carregaCompras();
        
        diaVencimento.setText("" + dia);
        btnVoltar.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }
    
    public void incluirCartao(View view) {
        switch (view.getId()) {
        case R.id.okConfig:                    	
            if (nomeCartao.getText().length() == 0) {
                Toast.makeText(this, "Favor Inserir o nome do cartão", Toast.LENGTH_LONG).show();
                return;
            }
                        
            MainActivity.addCartao(nomeCartao.getText().toString().toUpperCase(), diaVencimento.getText().toString());
            
            Toast.makeText(getApplicationContext(), "INSERIDO COM SUCESSO", Toast.LENGTH_LONG).show();
            
            nomeCartao.setText(null);
            carregaCartoes();
	        break;
        }
      }
    
    public void excluirCartao(View view){
        switch (view.getId()) {
        case R.id.excluirCartao:
        	if(cartoes.getCount() < 1){
        		Toast.makeText(getApplicationContext(), "SEM CARTÕES PARA EXCLUSAO", Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	MainActivity.removeCartao(cartoes.getSelectedItem().toString());
        	
        	Toast.makeText(getApplicationContext(), "EXCLUIDO COM SUCESSO", Toast.LENGTH_LONG).show();
        	
        	carregaCartoes();
        }
    }
    
    public void excluirCompra(View view){
        switch (view.getId()) {
        case R.id.excluirCompraButton:
        	if(compras.getCount() < 1){
        		Toast.makeText(getApplicationContext(), "SEM COMPRAS PARA EXCLUSAO", Toast.LENGTH_LONG).show();
        		return;
        	}

        	String selecionado = "";
        	selecionado = compras.getSelectedItem().toString();
        	String[] campos = new String[5];
        	campos = selecionado.split("_");
        	MainActivity.removeCompra(campos[0]);        	
        	
        	carregaCompras();
        }
    }
    
    public void carregaCartoes(){        
        List<String> cartoesLista = MainActivity.getCartoes();
        int i = 0;
        array_spinner= new String[cartoesLista.size()];
        for (String string : cartoesLista) {
			array_spinner[i] = string;
        	i++;
		}
        
        cartoes = (Spinner) findViewById(R.id.cartoesLista);
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        cartoes.setAdapter(adapter);
        cartoes.setSelection(0);    	
    }
    
    public void carregaCompras(){ 
        comprasLista = MainActivity.getCompras();
        int i = 0;
        array_spinner= new String[comprasLista.size()];
        for (Compra compra : comprasLista) {
    		double compraValor = compra.getValor();
			array_spinner[i] = ( "" + compra.getId() + "_" + compra.getCartao().toString() + "_" + compra.getData() + "_" + df.format(compraValor) + "_" + compra.getDescricao());
        	i++;
		}
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        compras.setAdapter(adapter);
        compras.setSelection(0);    	
    }
    
    public void diminuiDia(View view){
	    switch (view.getId()) {
	    case R.id.menosDia:
	    	dia = dia - 1;
	    	
	    	if(dia == 0){
	    		dia = 31;
	    		diaVencimento.setText("" + dia);
	    	}else{
	    		diaVencimento.setText("" +dia);
	    	}	    	
	    }
    }
    
    public void somaDia(View view){
	    switch (view.getId()) {
	    case R.id.maisDia:
	    	dia = dia + 1;
	    	
	    	if(dia == 32){
	    		dia = 1;
	    		diaVencimento.setText("" + dia);
	    	}else{
	    		diaVencimento.setText("" + dia);
	    	}	    
	    }
    }
}
