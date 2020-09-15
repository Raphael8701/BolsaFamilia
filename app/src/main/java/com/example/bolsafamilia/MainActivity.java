package com.example.bolsafamilia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    EditText municipio;
    EditText ano;
    TextView saida;
    List<BolsaFamilia> bolsas;

    String key = "ff721f5a3cfd8cf8aac74a40d42b0ff7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bolsas = new ArrayList<>();
        setContentView(R.layout.activity_main);

        municipio = findViewById(R.id.inputMunicipio);
        ano = findViewById(R.id.inputAno);
        saida = findViewById(R.id.saida);

    }

    public void buscarBolsa(View view) {
        bolsas.clear();
        RequestQueue queue = Volley.newRequestQueue(this);

        String codigo = municipio.getText().toString();
        String ano_ = ano.getText().toString();

        if (codigo.length() != 7 || ano_.length() != 4) {
            saida.setText("Código ou ano inválido");
            return;
        }

        for (int i = 1; i <= 12; i++) {
            String mes = i < 10 ? "0" + i : "" + i;
            String url = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=" + ano + mes + "&codigoIbge=" + codigo + "&pagina=1";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject result = response.getJSONObject(0);
                                if (result != null) {
                                    JSONObject municipio_object = result.getJSONObject("municipio");

                                    BolsaFamilia bolsa_familia = new BolsaFamilia();
                                    bolsa_familia.setMunicipio(municipio_object.getString("nomeIBGE"));
                                    bolsa_familia.setEstadoSigla(municipio_object.getJSONObject("uf").getString("sigla"));
                                    bolsa_familia.setEstado(municipio_object.getJSONObject("uf").getString("nome"));
                                    bolsa_familia.setBeneficiarios(result.getInt("quantidadeBeneficiados"));
                                    bolsa_familia.setTotalPago(result.getDouble("valor"));
                                    bolsas.add(bolsa_familia);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            saida.setText("Erro ao acessar transparencia.gov.br");
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("chave-api-dados", key);
                    headers.put("Accept", "*/*");
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    120 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request);
        }



    }


}