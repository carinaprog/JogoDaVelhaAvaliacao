package br.com.progiv.jogodavelha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView jogadorUmPlacar, jogadorDoisPlacar, jogadorStatus;
    private Button[] buttons = new Button[9];
    private Button resetarJogo;

    private int jogadorUmPlacarContador, jogadorDoisPlacarContador, rodadaContador;
    boolean jogadorAtivo;

    //jogador um -> 0
    //jogador dois -> 1
    //vazio -> 2

    int[] estadoJogo = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] posicoesVencedoras = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //linhas
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},//colunas
            {0, 4, 8}, {2, 4, 6} //diagonal

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jogadorUmPlacar = (TextView) findViewById(R.id.jogadorUmPlacar);
        jogadorDoisPlacar = (TextView) findViewById(R.id.jogadorDoisPlacar);
        jogadorStatus = (TextView) findViewById(R.id.jogadorStatus);
        resetarJogo = (Button) findViewById(R.id.reiniciarJogo);

        for (int i = 0; i < buttons.length; i++) {
            String botaoID = "btn_" + i;
            int recursoID = getResources().getIdentifier(botaoID, "id", getPackageName());
            buttons[i] = (Button) findViewById(recursoID);
            buttons[i].setOnClickListener(this);
        }

        rodadaContador = 0;
        jogadorUmPlacarContador = 0;
        jogadorDoisPlacarContador = 0;
        jogadorAtivo = true;

    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        String botaoID = v.getResources().getResourceEntryName(v.getId());
        int estadoJogoPonto = Integer.parseInt(botaoID.substring(botaoID.length() - 1, botaoID.length()));

        if (jogadorAtivo) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FFFF00"));
            estadoJogo[estadoJogoPonto] = 0;
        } else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#00BFFF"));
            estadoJogo[estadoJogoPonto] = 1;
        }

        rodadaContador++;

        if (verificaVencedor()) {
            if (jogadorAtivo) {
                jogadorUmPlacarContador++;
                atualizarPlacar();
                Toast.makeText(this, "Jogador UM pontuou", Toast.LENGTH_SHORT).show();
                jogarNovamente();
            } else {
                jogadorDoisPlacarContador++;
                atualizarPlacar();
                Toast.makeText(this, "Jogador DOIS pontuou", Toast.LENGTH_SHORT).show();
                jogarNovamente();
            }
        } else if (rodadaContador == 9) {
            jogarNovamente();
            Toast.makeText(this, "Ninguém ganhou", Toast.LENGTH_SHORT).show();
        } else {
            jogadorAtivo = !jogadorAtivo;
        }
        if (jogadorUmPlacarContador > jogadorDoisPlacarContador) {
            jogadorStatus.setText("Jogador UM está ganhando");
        } else if (jogadorDoisPlacarContador > jogadorUmPlacarContador) {
            jogadorStatus.setText("Jogador DOIS está ganhando");
        } else {
            jogadorStatus.setText("");
        }

        resetarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogadorUmPlacarContador = 0;
                jogadorDoisPlacarContador = 0;
                jogadorStatus.setText("");
                jogarNovamente();

            }
        });
    }

    public boolean verificaVencedor() {
        boolean resultadoVencedor = false;

        for (int[] posicaoVencedora : posicoesVencedoras) {
            if (estadoJogo[posicaoVencedora[0]] == estadoJogo[posicaoVencedora[1]] && estadoJogo[posicaoVencedora[1]] == estadoJogo[posicaoVencedora[2]] && (estadoJogo[posicaoVencedora[0]] != 2)) {
                resultadoVencedor = true;
            }

        }
        return resultadoVencedor;
    }

    public void atualizarPlacar() {
        jogadorUmPlacar.setText(Integer.toString(jogadorUmPlacarContador));
        jogadorDoisPlacar.setText(Integer.toString(jogadorDoisPlacarContador));
    }

    public void jogarNovamente() {
        rodadaContador = 0;
        jogadorAtivo = true;

        for (int i = 0; i < buttons.length; i++) {
            estadoJogo[i] = 2;
            buttons[i].setText("");
        }
    }
}