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
    //Variables privadas
    //Edit text en el que se introduce el valor de para el contador
    private EditText valorContadorET;
    //Barra de progreso que mostrara el progreso del contador
    private ProgressBar barraProgreso;
    //Cuadro de texto que mostrara en porcentaje el progreso del contador
    private TextView mostrarValor;
    //Objeto de la clase que extiende de AsyncTask
    private Contador contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creo el EditText, ProgressBar y TextView buscando por el id
        valorContadorET = (EditText) findViewById(R.id.valorContadorET);
        barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
        mostrarValor = (TextView) findViewById(R.id.mostrarValor);
    }


    /**
     * Metodo asociado al boton Comenzar Contador, cuando este es pulsado
     * llama a este metodo, en el que se comprueba que la variable contador
     * sea nula, es decir que no este ejecutandose un contador.
     *
     * Si la variable contador es nula coge el valor del EditText y ejecuta la
     * tarea asincrona del contador.
     *
     * Ademas le indica a la barra de progreso que el mayor maximo que puede adquirir
     * es del valor indicado en el EditText
     *
     */
    public void lanzarContador(View v) {
        //Compruebo que no haya ningun contador iniciado
        if(contador == null) {
            //Leo el contenido del EditText
            int n = Integer.parseInt(String.valueOf(valorContadorET.getText()));
            //Indico el valor maximo para la barra de progreso
            barraProgreso.setMax(n);
            //Ejecuto la tarea contador indicandole que debe llegar hasta n
            contador = (Contador) new Contador().execute(n);
        }
    }

    /**
     * Este metodo va asociado al boton Cancelar, cuando este boton es pulsado
     * comprueba que la variable contador sea distinta de null, lo que significa
     * que hay una tarea contador ejecutandose y pararia dicha tarea.
     */
    public void cancelar(View v) {
        //Compruebo que hay un contador iniciado
        if(contador!=null)
            //Cancelo la tarea contador que se esta ejecutando
            contador.cancel(true);
    }


    /**
     * Extiende de la clase AsyncTask por lo que nos permite realizar tareas asincronas
     */
    public class Contador extends AsyncTask<Integer, Integer, Void> {
        /**
         * Antes de ejecutar la tarea hago que la barra de progresotenga un
         * valor inicial de la barra es 0
         */
        @Override
        protected void onPreExecute(){
            barraProgreso.setProgress(0);
        }

        /**
         * En segundo plano la tarea va haciendo una cuenta a partir del valor que se le
         * indica en la llamada, en cada iteracion hago que el hilo duerma 1s y llame al metodo
         * publishProgress(ValorActualContador).
         * @param valorContador-->Valor para el contador
         * @return --> no devuelve nada
         */
        @Override
        protected Void doInBackground(Integer... valorContador) {
            for (int i = 1; i <= valorContador[0] && !this.isCancelled(); i++) {
                //Muestro al usuario el progreso del contador
                publishProgress(i);
                try {
                    Thread.sleep(1000); //duerme el hilo
                } catch (InterruptedException e) {
                    Log.d("Threading", e.getLocalizedMessage());
                }
            }
            return null;
        }

        /**
         * Durante el progreso de la tarea voy actualizando la barra de progreso
         * indicando que el valor de la barra de progreso es igual al valor por el
         * que va el contador, además actualizo el cuadro de texto que muestra el
         * progreso en tanto por ciento
         * @param progreso --> Valor actual del contador
         */
        @Override
        protected void onProgressUpdate(Integer... progreso) {
            barraProgreso.setProgress(progreso[0]);
            mostrarValor.setText(barraProgreso.getProgress()*100/barraProgreso.getMax()+"%");
        }

        /**
         * Despues de ejecutar la tarea hago que la variable contador sea nula
         * parapoder iniciar un nuevo contador en el caso de que se pulse de
         * nuevo el boton Comenzar contador
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            contador=null;
        }

        /**
         * Si la tarea es cancelada hago que la variable contador sea igual
         * a nulo para poder iniciar un nuevo contador en el caso de que se
         * pulse el boton Comenzar contador
         */
        @Override
        protected void onCancelled() {
            contador=null;
        }
    }
}
