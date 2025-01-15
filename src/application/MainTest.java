package application;

import java.util.List;
import model.entities.Card;
import model.services.CardServices;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class MainTest {

    public static void main(String[] args) {
        // Criando o serviço e o objeto de regressão simples
        CardServices service = new CardServices();
        SimpleRegression regression = new SimpleRegression();

        // Obtendo os dados
        List<Card> lCard = service.findAll();
        double[] gastos = new double[lCard.size()];

        // Preenchendo o vetor com os valores dos gastos
        int cont = 0;
        for (Card card : lCard) {
            gastos[cont] = card.getValue();
            cont += 1;
        }

        // Calculando os diferenciais
        double[] diferenciais = diferenciaDados(gastos);

        // Adicionando os dados diferenciados à regressão
        for (int i = 1; i < diferenciais.length; i++) {
            regression.addData(i, diferenciais[i]);
        }

        // Fazendo a previsão do próximo valor
        double previsao = regression.predict(diferenciais.length + 1);

        // Reconstruindo o valor original da previsão
        double valorPrevistoOriginal = gastos[gastos.length - 1] + previsao;
        System.out.println("Valor previsto do proximo mes (original): " + valorPrevistoOriginal);
    }

    // Função para calcular os diferenciais
    public static double[] diferenciaDados(double[] dados) {
        double[] diferenciais = new double[dados.length - 1];
        for (int i = 1; i < dados.length; i++) {
            diferenciais[i - 1] = dados[i] - dados[i - 1];
        }
        return diferenciais;
    }

}
