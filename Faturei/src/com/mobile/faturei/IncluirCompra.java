package com.mobile.faturei;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.entidades.Compra;

public class IncluirCompra extends Activity {
	private EditText valor;
	private TextView vezes;
	private Spinner parcelas;
	private DatePicker dataCompra;
	private Spinner cartao;
    private DecimalFormat df = new DecimalFormat("0.00");  
    private String array_spinner[];
    private Button btnVoltar;


    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_compra);
        valor = (EditText) findViewById(R.id.editText1);
        vezes = (TextView) findViewById(R.id.textView5);
        btnVoltar = (Button) findViewById(R.id.button1);
        dataCompra = (DatePicker) findViewById(R.id.dataCompra);
 
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
            compra.setData( dataCompra.getDayOfMonth() + "/" + dataCompra.getMonth() + "/" + dataCompra.getYear());
            compra.setDescricao("compra");
            compra.setCartao(cartao.getSelectedItem().toString());
            compra.setValor(Double.parseDouble(valor.getText().toString()));
            MainActivity.addCompra(compra);
            
            Toast.makeText(getApplicationContext(), "INSERIDO COM SUCESSO", Toast.LENGTH_LONG).show();
            df.format(0);
            
            valor.setText(null);
	        break;
        }
      }
    
    public void parcelado(View view){
    	vezes.setVisibility(0);
        parcelas.setVisibility(0);
    }
}
