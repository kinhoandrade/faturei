package com.mobile.faturei;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.entidades.Compra;

public class IncluirCompra extends Activity {
	private EditText valor;
	private TextView vezes;
	private Spinner parcelas;
	private Spinner dia;
	private Spinner mes;
	private Spinner ano;
	private Spinner cartao;
    DecimalFormat df = new DecimalFormat("0.00");  
    private String array_spinner[];


    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_compra);
        valor = (EditText) findViewById(R.id.editText1);
        vezes = (TextView) findViewById(R.id.textView5);
        Button btnVoltar = (Button) findViewById(R.id.button1);

        int aux = 0; 
        array_spinner= new String[12];
        array_spinner[0] = "1";
        array_spinner[1] = "2";
        array_spinner[2] = "3";
        array_spinner[3] = "4";
        array_spinner[4] = "5";
        array_spinner[5] = "6";
        array_spinner[6] = "7";
        array_spinner[7] = "8";
        array_spinner[8] = "9";
        array_spinner[9] = "10";
        array_spinner[10] = "11";
        array_spinner[11] = "12";
        parcelas = (Spinner) findViewById(R.id.spinner4);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        parcelas.setAdapter(adapter);
        parcelas.setSelection(0);
        
        array_spinner= new String[31];
        for(int i = 0; i <= 30; i ++){
        	aux = i + 1;
        	array_spinner[i] = "" + aux;
        }
        dia = (Spinner) findViewById(R.id.spinner1);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        dia.setAdapter(adapter2);
        dia.setSelection(0);
        
        array_spinner= new String[12];
        for(int i = 0; i <= 11; i++){
        	aux = i + 1;
        	array_spinner[i] = "" + aux;
        }
        mes = (Spinner) findViewById(R.id.spinner2);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        mes.setAdapter(adapter3);
        mes.setSelection(0);
        
        array_spinner= new String[7];
        array_spinner[0] = "2012";
        array_spinner[1] = "2012";
        array_spinner[2] = "2013";
        array_spinner[3] = "2014";
        array_spinner[4] = "2015";
        array_spinner[5] = "2016";
        array_spinner[6] = "2017";
        ano = (Spinner) findViewById(R.id.spinner3);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        ano.setAdapter(adapter4);
        ano.setSelection(0);       
        
        
        array_spinner= new String[3];
        array_spinner[0] = "VISA";
        array_spinner[1] = "MASTERCARD";
        array_spinner[2] = "SUBMARINO";
        
        cartao = (Spinner) findViewById(R.id.spinner5);
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        cartao.setAdapter(adapter5);
        cartao.setSelection(0);
 		
        // Binding Click event to Button
        btnVoltar.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
	}
    
    public void incluir(View view) {
        switch (view.getId()) {
        case R.id.button2:
        	CheckBox parcelado = (CheckBox) findViewById(R.id.checkBox1);
            //Spinner parcelas = (Spinner) findViewById(R.id.spinner4);
                    	
            if (valor.getText().length() == 0) {
                Toast.makeText(this, "Favor Inserir o total", Toast.LENGTH_LONG).show();
                return;
            }
            
            if(parcelado.isChecked()){
                Toast.makeText(this, "Compra parcelada", Toast.LENGTH_LONG).show();
                return;
            }
            
            Compra compra = new Compra();
            //compra.setCartao(cartao.getSelectedItem().toString());
            compra.setData(dia.getSelectedItem().toString() + "/" + mes.getSelectedItem().toString() + "/" + ano.getSelectedItem().toString());
            compra.setDescricao("compra");
            compra.setCartao(cartao.getSelectedItem().toString());
            compra.setValor(Double.parseDouble(valor.getText().toString()));
            MainActivity.addCompra(compra);
            
            Toast.makeText(getApplicationContext(), "INSERIDO COM SUCESSO", Toast.LENGTH_LONG).show();
            
	        //valor.setText(String.valueOf(df.format(0)));
            valor.setText(null);
	        break;
        }
      }
    
    public void parcelado(View view){
    	vezes.setVisibility(0);
        parcelas.setVisibility(0);
    }
}
