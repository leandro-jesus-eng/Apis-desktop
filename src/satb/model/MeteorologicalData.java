/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.model;

import java.util.Date;

/**
 *
 * @author Leandro
 */
public class MeteorologicalData {

    public static String TEMPERATURA_DO_AR = "TEMPERATURA DO AR (°C)";
    public static String TEMPERATURA_MAXIMA = "TEMPERATURA MAXIMA (°C)";
    public static String TEMPERATURA_MINIMA = "TEMPERATURA MINIMA (°C)";

    public static String UMIDADE_RELATIVA_DO_AR = "UMIDADE RELATIVA DO AR (%)";
    public static String UMIDADE_RELATIVA_DO_AR_MAXIMA = "UMIDADE RELATIVA DO AR MAXIMA (%)";
    public static String UMIDADE_RELATIVA_DO_AR_MINIMA = "UMIDADE RELATIVA DO AR MINIMA (%)";

    public static String TEMPERATURA_DO_PONTO_DE_ORVALHO = "TEMPERATURA DO PONTO DE ORVALHO (°C)";
    public static String TEMPERATURA_DO_PONTO_DE_ORVALHO_MAXIMA = "TEMPERATURA DO PONTO DE ORVALHO MAXIMA (°C)";
    public static String TEMPERATURA_DO_PONTO_DE_ORVALHO_MINIMA = "TEMPERATURA DO PONTO DE ORVALHO MINIMA (°C)";

    public static String PRESSAO_ATMOSFERICA = "PRESSAO ATMOSFERICA (hPa)";
    public static String PRESSAO_ATMOSFERICA_MAXIMA = "PRESSAO ATMOSFERICA MAXIMA (hPa)";
    public static String PRESSAO_ATMOSFERICA_MINIMA = "PRESSAO ATMOSFERICA MINIMA (hPa)";

    public static String VENTO_VELOCIDADE = "VENTO VELOCIDADE (m/s)";
    public static String VENTO_DIRECAO = "VENTO DIRECAO (graus)";
    public static String VENTO_RAJADA_MAXIMA = "VENTO RAJADA MAXIMA (m/s)";

    public static String RADIACAO_GLOBAL = "RADIACAO GLOBAL (KJ/M²)";

    public static String PRECIPITACAO = "PRECIPITACAO (mm)";

    private Date date;
    private String label;
    private Float value;

    public MeteorologicalData(Date date, String label, Float value) {
        this.date = date;
        this.label = label;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    

}
