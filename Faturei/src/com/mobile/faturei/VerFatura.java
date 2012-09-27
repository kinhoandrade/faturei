package com.mobile.faturei;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.entidades.Compra;

public class VerFatura extends Activity {
	Button btnVoltar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fatura);
        Button btnVoltar = (Button) findViewById(R.id.faturavoltar);
        TextView comprasView = (TextView) findViewById(R.id.compras);
        
        Intent i = getIntent();
        // Receiving the Data
        String operacoes = i.getStringExtra("operacoes");
 
        // Displaying Received data
        comprasView.setText(operacoes);
        
        btnVoltar.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
    }
    
    public void verFatura(){
        List<Compra> compras = new ArrayList<Compra>();
        TextView comprasView = (TextView) findViewById(R.id.compras);
        
        try {
        	compras = MainActivity.getCompras();
        	
        	for (Compra compraAux : compras) {
        		comprasView.setText(comprasView.getText() + compraAux.getData() + " = R$ " + compraAux.getValor().toString());
			}
        	
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
