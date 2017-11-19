package com.example.jj.contadorasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText valorContadorET;
    private ProgressBar barraProgreso;
    private TextView mostrarValor;
    private Contador contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valorContadorET = (EditText) findViewById(R.id.valorContadorET);
        barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
        mostrarValor = (TextView) findViewById(R.id.mostrarValor);



    }

    public void lanzarContador(View v) {
        if(contador == null) {
            int n = Integer.parseInt(String.valueOf(valorContadorET.getText()));
            barraProgreso.setMax(n);
            contador = (Contador) new Contador().execute(n);
        }
    }

    Contador c = new Contador();
    public void cancelar(View v) {
        if(contador!=null)
            contador.cancel(true);
    }


    public class Contador extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute(){
            barraProgreso.setProgress(0);
            barraProgreso.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... valorCuentaAtras) {
            for (int i = 1; i <= valorCuentaAtras[0] && !this.isCancelled(); i++) {
                publishProgress(i);
                try {
                    Thread.sleep(1000); //duerme el hilo
                } catch (InterruptedException e) {
                    Log.d("Threading", e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progreso) {
            barraProgreso.setProgress(progreso[0]);
            mostrarValor.setText(barraProgreso.getProgress()+"");
        }

        @Override
        protected void onPostExecute(Void result) {
            contador=null;
        }

        @Override
        protected void onCancelled() {
            barraProgreso.setVisibility(ProgressBar.INVISIBLE);
            contador=null;
        }
    }
}
