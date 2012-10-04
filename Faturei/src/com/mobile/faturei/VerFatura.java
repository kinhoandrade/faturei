package com.mobile.faturei;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.entidades.Cartao;
import com.mobile.entidades.Compra;

@SuppressWarnings("unused")
public class VerFatura extends Activity {
	private Button btnVoltar;
	private TextView comprasView;
	private Spinner cartoes;
	private Spinner mesSpinner;
    private String array_spinner[];
	private ListView listaComprasListView;
    private DecimalFormat df = new DecimalFormat("0.00"); 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fatura);
        btnVoltar = (Button) findViewById(R.id.faturavoltar);
        comprasView = (TextView) findViewById(R.id.compras);
        listaComprasListView = (ListView) findViewById(R.id.listaComprasListView);        
        List<Cartao> listaCartoes = MainActivity.getCartoes();
        
        array_spinner= new String[listaCartoes.size()+1];
        int i = 1;
        array_spinner[0] = "TODOS";
        for (Cartao cartao : listaCartoes) {
            array_spinner[i] = cartao.getCartao();
            i++;
		}
        cartoes = (Spinner) findViewById(R.id.spinnercartoes);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        cartoes.setAdapter(adapter);
        cartoes.setSelection(0);
        
        array_spinner= new String[13];
        array_spinner[0] = "TODOS";
        array_spinner[1] = "1-Janeiro";
        array_spinner[2] = "2-Fevereiro";
        array_spinner[3] = "3-Março";
        array_spinner[4] = "4-Abril";
        array_spinner[5] = "5-Maio";
        array_spinner[6] = "6-Junho";
        array_spinner[7] = "7-Julho";
        array_spinner[8] = "8-Agosto";
        array_spinner[9] = "9-Setembro";
        array_spinner[10] = "10-Outubro";
        array_spinner[11] = "11-Novembro";
        array_spinner[12] = "12-Dezembro";
        mesSpinner = (Spinner) findViewById(R.id.mesSpinner);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        mesSpinner.setAdapter(adapter2);
        mesSpinner.setSelection(0);
        
        verFatura();
        
        btnVoltar.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }
    
    public void verFatura(){
        try {
           	listaFiltrado();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    public void filtraMes(View view){
    	switch (view.getId()) {
	        case R.id.okMesButton:
	        	listaFiltrado();
    	}
    }
    
    public void listaFiltrado(){
    	List<Compra> listaCompras = new ArrayList<Compra>();
    	List<Cartao> listaCartao = new ArrayList<Cartao>();
    	String cartaoEscolhido = cartoes.getSelectedItem().toString();
    	String mesSelecionado = mesSpinner.getSelectedItem().toString();
    	String[] mesArray = mesSelecionado.split("-");
    	String mesEscolhido = mesArray[0];
		int mesEscolhidoInt = 0;
    	if(!mesEscolhido.equals("TODOS")){ 
    		mesEscolhidoInt = Integer.parseInt(mesEscolhido);
    	}
    	
        double total = 0.0;
    	
        listaCompras = MainActivity.getCompras();
        listaCartao = MainActivity.getCartoes();
		comprasView.setText("");
    	for (Compra compraAux : listaCompras) {
    		if(cartaoEscolhido.equals(compraAux.getCartao().toString()) || cartaoEscolhido.equals("TODOS")){
    			String dataCompra = compraAux.getData().toString();
    			String cartaoCompra = compraAux.getCartao().toString();
    			String[] dataArray = dataCompra.split("/");
    			String diaCompra = dataArray[0];
    			String mesCompra = dataArray[1];
    			int diaFechamentoCartao = 0;
    			
    			for (Cartao cartaoAux : listaCartao) {
					if(cartaoAux.getCartao().equals(cartaoCompra)){
						if(cartaoAux.getFechamento().toString().length() > 0){
							diaFechamentoCartao = Integer.parseInt(cartaoAux.getFechamento());
						}else{
							diaFechamentoCartao = 0;}
					}
				}
    			
    			int diaCompraInt = Integer.parseInt( diaCompra );
    			int mesCompraInt = Integer.parseInt( mesCompra );
    			
    			
    			if( ((mesEscolhidoInt == mesCompraInt + 1 ) && diaCompraInt > diaFechamentoCartao ) || ((mesEscolhidoInt == mesCompraInt ) && diaCompraInt <= diaFechamentoCartao ) || mesEscolhido.equals("TODOS")){
    				total = total + Double.parseDouble(df.format(compraAux.getValor()));
    				double compraValor = compraAux.getValor();
    				comprasView.setText(comprasView.getText().toString() + "[" + compraAux.getCartao().toString() + "] " + compraAux.getData() + " = R$ " + df.format(compraValor) + " - " + compraAux.getDescricao() + " - " + compraAux.getParcelas() + "\n");
    				//listaComprasListView.addView(comprasView);
    			}
    		}
		}
    	comprasView.setText(comprasView.getText() + "\nTotal = R$ " + df.format(total)); 
    	
    }
}
