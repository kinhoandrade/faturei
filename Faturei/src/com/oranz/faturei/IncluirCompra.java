package com.oranz.faturei;

import java.text.DecimalFormat;
import java.util.List;

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

import com.oranz.faturei.R;
import com.oranz.entidades.Cartao;
import com.oranz.entidades.Compra;

public class IncluirCompra extends Activity {
	private EditText valor;
	private EditText descricao;
	private TextView vezes;
	private Spinner parcelas;
	private DatePicker dataCompra;
	private Spinner cartao;
    private DecimalFormat df = new DecimalFormat("0.00");  
    private String array_spinner[];
    private Button btnVoltar;
    private List<Cartao> cartoes;


    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_compra);
        valor = (EditText) findViewById(R.id.editText1);
        descricao = (EditText) findViewById(R.id.descricaoEditText);
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
        

        cartoes = MainActivity.getCartoes();
        int i = 0;
        array_spinner= new String[cartoes.size()];
        for (Cartao cartao : cartoes) {
			array_spinner[i] = cartao.getCartao();
        	i++;
		}
        
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
            Spinner parcelas = (Spinner) findViewById(R.id.spinner4);
            int qtdParcelas = 1;
                    	
            if (valor.getText().length() == 0) {
                Toast.makeText(this, "Favor Inserir o total", Toast.LENGTH_LONG).show();
                return;
            }
            
            if(!parcelado.isChecked()){
	            Compra compra = new Compra();
	            String data = dataCompra.getDayOfMonth() + "/" + (dataCompra.getMonth() + 1) + "/" + dataCompra.getYear();
	            compra.setData( data );
	            compra.setParcelas("A vista");
	            if (descricao.getText().length() > 0) {
	            	compra.setDescricao(descricao.getText().toString());
	            }else{
	            	compra.setDescricao("");
	            }
	            if(cartoes.size() < 1){
	            	MainActivity.addCartao("DEFAULT", data);
	            	compra.setCartao("DEFAULT");
	            }else{
	            	compra.setCartao(cartao.getSelectedItem().toString());
	            }
	            compra.setValor(Double.parseDouble(df.format(Double.parseDouble(valor.getText().toString()))));
	            MainActivity.addCompra(compra);
            }else{
            	qtdParcelas = Integer.parseInt(parcelas.getSelectedItem().toString());
            	double valorCompra = Double.parseDouble(valor.getText().toString());
            	double valorCompraParcelada = valorCompra / qtdParcelas;
            	
            	for (int i = 0; i < qtdParcelas; i ++){
		            Compra compra = new Compra();
		            String data = "";
		            if (dataCompra.getMonth() + i > 12){
		            	data = dataCompra.getDayOfMonth() + "/" + ((dataCompra.getMonth()-12) + 1 + i)  + "/" + (dataCompra.getYear() + 1);
		            }else{
		            	data = dataCompra.getDayOfMonth() + "/" + (dataCompra.getMonth() + 1 + i)  + "/" + dataCompra.getYear();
		            }
		            compra.setData( data );
		            compra.setParcelas((i + 1) + " de " + qtdParcelas );
		            if (descricao.getText().length() > 0) {
		            	compra.setDescricao(descricao.getText().toString());
		            }else{
		            	compra.setDescricao("");
		            }
		            if(cartoes.size() < 1){
		            	MainActivity.addCartao("DEFAULT", data);
		            	compra.setCartao("DEFAULT");
		            }else{
		            	compra.setCartao(cartao.getSelectedItem().toString());
		            }
		            compra.setValor(Double.parseDouble(df.format(valorCompraParcelada)));
		            MainActivity.addCompra(compra);
            	}            	
            }
            
            if(parcelado.isChecked()){
                Toast.makeText(this, "COMPRA PARCELADA INSERIDA", Toast.LENGTH_LONG).show();
            }else {
            	Toast.makeText(getApplicationContext(), "INSERIDO COM SUCESSO", Toast.LENGTH_LONG).show();
            }
            
            parcelado.setChecked(false);
            parcelas.setSelection(0);       
            valor.setText(null);  
            descricao.setText(null);
	        break;
        }
      }
    
    public void parcelado(View view){
    	vezes.setVisibility(0);
        parcelas.setVisibility(0);
    }
}
