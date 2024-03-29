package satb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import satb.model.Coordinate;
import satb.model.dao.CollarDataDAO;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import satb.behavior.ActivityRecognition;
import satb.behavior.Observation;
import satb.behavior.Util;
import satb.model.CoordinateVenus;
import satb.model.MeteorologicalData;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Classe que representa a camada de controle para CollarData.
 *
 * @author Marcel Tolentino Pinheiro de Oliveira.
 */
public class CollarDataController {
    //Atributos

    private FileChooser fileChooser;
    private File file;
    private final Label labelFile;

    /**
     * Construtor Default.
     */
    public CollarDataController() {
        fileChooser = new FileChooser();
        labelFile = new Label();
    }

    /**
     * Método que executa a operação hourB - hourA.
     */
    public String subtractHours(String hourA, String hourB) {
        /// A= 11:00:00  B = 11:15:00
        // parse the string
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hourB);

        String[] parts = hourA.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));

        return dateTime.toString(formatter);
    }

    /**
     * Recebe a coordenada e devolve os segundos da Latitude.
     */
    private static String getLatitude(String word) {
        String degree = word.substring(0, 2);
        double lat = Double.parseDouble(word.substring(2));
        lat = lat / 60.0;

        String decimals = String.valueOf(lat);

        return degree.concat(decimals.substring(1));
    }

    /**
     * Recebe a coordenada e devolve os segundos da Longitude.
     */
    private static String getLongitude(String word) {
        String degree = word.substring(0, 3);
        double lon = Double.parseDouble(word.substring(3));
        lon = lon / 60;

        String decimals = String.valueOf(lon);

        return degree.concat(decimals.substring(1));
    }

    /**
     * Retorna a data formatada.
     */
    private static String getDate(String word) {
        String date = word.substring(0, 2);
        date = date.concat("/");
        date = date.concat(word.substring(2, 4));
        date = date.concat("/");
        date = date.concat(word.substring(4));

        return date;
    }

    /**
     * Retorna a hora formatada.
     */
    private static String getHour(String word) {
        String hour = word.substring(0, 2);
        hour = hour.concat(":");
        hour = hour.concat(word.substring(2, 4));
        hour = hour.concat(":");
        hour = hour.concat(word.substring(4));

        return hour;
    }

    /*  ddmm.mmmm  convert to D.d
     divide mm.mmmm by 60 to get .d
     add .d to D to get D.d(Decimal degrees) 
     */
    /**
     * Extrai as informações da linha.
     */
    public Coordinate extractTxTInfo(String line) throws Exception {
        StringTokenizer st = new StringTokenizer(line);
        String word; //string que receberá as informacoes retiradas das linhas
        String id, lat_d, lon_d, date, hour;
        String minus = "-";

        //colar
        word = st.nextToken(",");
        System.out.println(word);
        id = word.substring(1);

        //latitude - posicao geografica
        word = st.nextToken(",");
        System.out.println(word);
        System.out.println(getLatitude(word));
        lat_d = getLatitude(word);

        //latitude (N e S)
        word = st.nextToken(",");
        System.out.println(word);

        //Se a latitude for South (Sul), o grau será negativo.
        if (word.contains("S")) {
            lat_d = minus.concat(lat_d);
        }

        //longitude - posicao geografica
        word = st.nextToken(",");
        System.out.println(word);
        System.out.println(getLongitude(word));
        lon_d = getLongitude(word);

        //longitude (W e E)
        word = st.nextToken(",");
        System.out.println(word);

        //Se a longitude for West (Oeste), o grau será negativo.
        if (word.contains("W")) {
            lon_d = minus.concat(lon_d);
        }

        //dia
        word = st.nextToken(",");
        System.out.println(word);
        date = getDate(word);
        System.out.println(date);

        //hora
        word = st.nextToken();
        System.out.println(word);
        hour = getHour(word);
        System.out.println(hour);

        //Acesso ao Banco de Dados
        CollarDataDAO cdd = new CollarDataDAO();
        cdd.insertData(id, lon_d, lat_d, date, hour);

        return new Coordinate(id, date, hour);
    }

    /**
     * Método que efetua a leitura de um .txt com os dados dos colares.
     */
    public void readTxt() {
        ObservableList<Coordinate> trajectory = FXCollections.observableArrayList();
        CollarDataDAO cdd = new CollarDataDAO();

        //Abre um diretório de um já existente
        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
            fileChooser.getExtensionFilters().clear();
        }

        //Define o filtro da extensão (.txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                labelFile.setText(file.getPath());
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //string que receberá as linhas do arquivo
                String line;

                while ((line = is.readLine()) != null) {
                    //se a linha possui caracteres
                    if (line.length() > 0) {
                        System.out.println(line);
                        trajectory.add(extractTxTInfo(line));
                    }
                }

                //Adiciona a trajetória
                String gapTime = subtractHours(trajectory.get(0).getHour(), trajectory.get(1).getHour());
                cdd.insertTrajectoryARFF(trajectory.get(0).getCollar(), trajectory.get(0).getDate(), gapTime, trajectory.get(0).getHour(), trajectory.get(trajectory.size() - 1).getHour());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Método que efetua a leitura de um .txt com os dados dos colares com GPS
     * Venus
     */
    public void readTxtVenus(String collar) {

        CollarDataDAO collarDataDAO = new CollarDataDAO();

        //Abre um diretório de um já existente
        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
            fileChooser.getExtensionFilters().clear();
        }

        //Define o filtro da extensão (.txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                labelFile.setText(file.getPath());
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //String que receberá as linhas do arquivo
                String line;

                LinkedList<CoordinateVenus> listCoordinate = new LinkedList<>();

                //Atributos
                String utcTime = null; // UTC time in hhmmss.sss format (000000.00 ~ 235959.999)
                Double latitude = null; // Latitude in dddmm.mmmm format   Leading zeros transmitted
                Character nsIndicator = null; // Latitude hemisphere indicator ‘N’ = North ‘S’ = South
                Double longitude = null; // Longitude in dddmm.mmmm format   Leading zeros transmitted
                Character ewIndicator = null; // Longitude hemisphere indicator 'E' = East 'W' = West
                /**
                 * GPS quality indicator 0: position fix unavailable 1: valid
                 * position fix, SPS mode 2: valid position fix, differential
                 * GPS mode 3: GPS PPS Mode, fix valid 4: Real Time Kinematic.
                 * System used in RTK mode with fixed integers 5: Float RTK.
                 * Satellite system used in RTK mode. Floating integers 6:
                 * Estimated (dead reckoning) Mode 7: Manual Input Mode 8:
                 * Simulator Mode
                 *
                 */
                Character gpsQuality = null;
                Integer satellitesUsed = null;
                Double PDOP = null; //Position dilution of precision (00.0 to 99.9)
                Double HDOP = null; // Horizontal dilution of precision (00.0 to 99.9)
                Double VDOP = null; // Vertical dilution of precision (00.0 to 99.9)                          
                Double altitude = null;
                Integer differencialReferenceStationID = null;
                Character status = null; // ‘V’ = Navigation receiver warning ‘A’ = Data Valid
                Double speedGPS = null; //Speed over ground in kilometers per hour (0000.0 ~ 1800.0)
                Double course = null; //Course over ground in degrees (000.0 ~ 359.9)
                String utcDate = null; // UTC date of position fix, ddmmyy format
                // Mode indicator ‘N’ = Data not valid ‘A’ = Autonomous mode ‘D’ = Differential mode 
                // ‘E’ = Estimated (dead reckoning) mode ‘M’ = Manual input mode ‘S’ = Simulator mode
                Character modeIndicator = null;

                CoordinateVenus ponto = null;
                Boolean excecao = false;

                int quantidade = 0;

                while ((line = is.readLine()) != null) {

                    try {

                        //se a linha possui caracteres
                        if (line.length() > 0) {
                            String[] split = line.split(",");

                            if (split[0].equals("$GPGGA")) {

                                if (gpsQuality != null && status != null && modeIndicator != null
                                        && gpsQuality != '0' && status == 'A' && modeIndicator != 'N') {

                                    //if(ponto != null)
                                    //    collarDataDAO.insertDataVenus(collar, ponto);
                                    ponto = new CoordinateVenus();

                                    ponto.setUtcTime(Double.parseDouble(utcTime));
                                    if (nsIndicator == 'S') {
                                        latitude = latitude * -1;
                                    }
                                    ponto.setLatitude(Util.coordinateMinToDegree(latitude));
                                    ponto.setNsIndicator(nsIndicator);
                                    if (ewIndicator == 'W') {
                                        longitude = longitude * -1;
                                    }
                                    ponto.setLongitude(Util.coordinateMinToDegree(longitude));
                                    ponto.setEwIndicator(ewIndicator);
                                    ponto.setGpsQuality(gpsQuality);
                                    ponto.setSatellitesUsed(satellitesUsed);
                                    ponto.setPDOP(PDOP);
                                    ponto.setHDOP(HDOP);
                                    ponto.setVDOP(VDOP);
                                    ponto.setAltitude(altitude);
                                    ponto.setDifferencialReferenceStationID(differencialReferenceStationID);
                                    ponto.setStatus(status);
                                    ponto.setSpeedGPS(speedGPS);
                                    ponto.setCourse(course);
                                    ponto.setUtcDate(Integer.parseInt(utcDate));
                                    ponto.setModeIndicator(modeIndicator);

                                    // UTC time in hhmmss.sss format (000000.00 ~ 235959.999)
                                    // UTC date of position fix, ddmmyy format
                                    Date data = null;
                                    try {
                                        //data = new SimpleDateFormat("ddMMyyHHmmss.SSSZ").parse(utcDate+utcTime+"+0000");  
                                        data = new SimpleDateFormat("ddMMyyHHmmss.SSS").parse(utcDate + utcTime);

                                        ponto.setDateObject(data);

                                        //ponto.print();
                                        quantidade++;

                                        //if(quantidade % 10 == 0)
                                        listCoordinate.add(ponto);

                                    } catch (ParseException e) {
                                        // e.printStackTrace(); 
                                    } finally {
                                        gpsQuality = null;
                                        status = null;
                                        modeIndicator = null;
                                    }
                                    //SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zZ");
                                    //System.out.println( format1.format(data) );                                    
                                }

                                utcTime = split[1].trim();
                                latitude = Double.parseDouble(split[2]);
                                nsIndicator = split[3].charAt(0);
                                longitude = Double.parseDouble(split[4]);
                                ewIndicator = split[5].charAt(0);
                                gpsQuality = split[6].charAt(0);
                                satellitesUsed = Integer.parseInt(split[7]);
                                //HDOP = Double.parseDouble(split[8]);
                                altitude = Double.parseDouble(split[9]);
                                differencialReferenceStationID = Integer.parseInt(split[14].split("\\*")[0]);
                            }

                            if (split[0].equals("$GPGSA")) {
                                PDOP = Double.parseDouble(split[15]);
                                HDOP = Double.parseDouble(split[16]);
                                VDOP = Double.parseDouble(split[17].split("\\*")[0]);
                            }

                            if (split[0].equals("$GPRMC")) {
                                status = split[2].charAt(0);
                                //speedGPS = Double.parseDouble(split[7]);
                                course = Double.parseDouble(split[8]);
                                utcDate = split[9].trim();
                                modeIndicator = split[12].split("\\*")[0].charAt(0);
                            }

                            if (split[0].equals("$GPVTG")) {
                                speedGPS = Double.parseDouble(split[7]);
                            }
                        }
                    } catch (Exception e) {

                        gpsQuality = null;
                        status = null;
                        modeIndicator = null;

                    }
                } // end while

                // adiciona o último
                //if(ponto != null)
                //  collarDataDAO.insertDataVenus(collar, ponto);
                if (ActivityRecognition.DEBUG) {
                    System.out.println("Lista de coordenadas de GPS criada. Tamanho = " + listCoordinate.size());
                }

                LinkedList<Observation> lo = readTxtObservations(collar);

                collarDataDAO.insertDataVenus(collar, listCoordinate);
                collarDataDAO.insertObservation(lo);

                /*for( CoordinateVenus o : listCoordinate) {
                 System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(o.getDateObject()) + ";"+ o.getDateObject().getTime() + "; 1;");
                 }*/
 /* for(Observation o : lo) {
                 System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(o.getData())+ ";"+ o.getObservation() +";" + o.getData().getTime() + "; 2;");
                 }
                
                 ActivityRecognition ac = new ActivityRecognition(listCoordinate, lo);
                 */
                //System.out.println("quantidade = "+quantidade);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void readTxtVenusLomba(String collar) {

        CollarDataDAO collarDataDAO = new CollarDataDAO();


        /*if (file != null) {
         File existDirectory = file.getParentFile();
         fileChooser.setInitialDirectory(existDirectory);
         fileChooser.getExtensionFilters().clear();
         }
        
         //Define o filtro da extensão (.txt)
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
         fileChooser.getExtensionFilters().add(extFilter);

         //Abrir e mostrar a janela de diretórios
         file = fileChooser.showOpenDialog(null);*/
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Diretório dados GPS");
        if (file != null) {
            chooser.setInitialDirectory(file.getAbsoluteFile());
        } else {
            chooser.setInitialDirectory(new File("I:\\Leandro\\Doutorado Uniderp\\Lomba"));
        }

        file = chooser.showDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {

            File[] files = file.listFiles();

            for (File fileAtual : files) {
                collar = fileAtual.getName().substring(0, 4);
                if (fileAtual.getName().length() == 8) {
                    try {
                        labelFile.setText(fileAtual.getPath());
                        BufferedReader is = new BufferedReader(new FileReader(fileAtual.getPath()));

                        //String que receberá as linhas do arquivo
                        String line;

                        LinkedList<CoordinateVenus> listCoordinate = new LinkedList<>();

                        //Atributos
                        String utcTime = null; // UTC time in hhmmss.sss format (000000.00 ~ 235959.999)
                        Double latitude = null; // Latitude in dddmm.mmmm format   Leading zeros transmitted
                        Character nsIndicator = null; // Latitude hemisphere indicator ‘N’ = North ‘S’ = South
                        Double longitude = null; // Longitude in dddmm.mmmm format   Leading zeros transmitted
                        Character ewIndicator = null; // Longitude hemisphere indicator 'E' = East 'W' = West
                        /**
                         * GPS quality indicator 0: position fix unavailable 1:
                         * valid position fix, SPS mode 2: valid position fix,
                         * differential GPS mode 3: GPS PPS Mode, fix valid 4:
                         * Real Time Kinematic. System used in RTK mode with
                         * fixed integers 5: Float RTK. Satellite system used in
                         * RTK mode. Floating integers 6: Estimated (dead
                         * reckoning) Mode 7: Manual Input Mode 8: Simulator
                         * Mode
                         *
                         */
                        Character gpsQuality = null;
                        Integer satellitesUsed = null;
                        Double PDOP = null; //Position dilution of precision (00.0 to 99.9)
                        Double HDOP = null; // Horizontal dilution of precision (00.0 to 99.9)
                        Double VDOP = null; // Vertical dilution of precision (00.0 to 99.9)                          
                        Double altitude = null;
                        Integer differencialReferenceStationID = null;
                        Character status = null; // ‘V’ = Navigation receiver warning ‘A’ = Data Valid
                        Double speedGPS = null; //Speed over ground in kilometers per hour (0000.0 ~ 1800.0)
                        Double course = null; //Course over ground in degrees (000.0 ~ 359.9)
                        String utcDate = null; // UTC date of position fix, ddmmyy format
                        // Mode indicator ‘N’ = Data not valid ‘A’ = Autonomous mode ‘D’ = Differential mode 
                        // ‘E’ = Estimated (dead reckoning) mode ‘M’ = Manual input mode ‘S’ = Simulator mode
                        Character modeIndicator = null;

                        Double ldr = null;
                        Double ax = null;
                        Double ay = null;
                        Double az = null;
                        Double gx = null;
                        Double gy = null;
                        Double gz = null;
                        Double mx = null;
                        Double my = null;
                        Double mz = null;

                        CoordinateVenus ponto = null;
                        Boolean excecao = false;

                        int quantidade = 0;

                        while ((line = is.readLine()) != null) {
                            try {
                                //se a linha possui caracteres
                                if (line.length() > 0) {
                                    String[] split = line.split(",");

                                    if (split[0].equals("$GPGGA")) {

                                        //if (gpsQuality != null && status != null && modeIndicator != null
                                        //        && gpsQuality != '0' && status == 'A' && modeIndicator != 'N') {
                                        if (latitude != null && longitude != null && course != null) {

                                            //if(ponto != null)
                                            //    collarDataDAO.insertDataVenus(collar, ponto);
                                            ponto = new CoordinateVenus();

                                            ponto.setUtcTime(Double.parseDouble(utcTime));
                                            if (nsIndicator == 'S') {
                                                latitude = latitude * -1;
                                            }
                                            ponto.setLatitude(Util.coordinateMinToDegree(latitude));
                                            ponto.setNsIndicator(nsIndicator);
                                            if (ewIndicator == 'W') {
                                                longitude = longitude * -1;
                                            }
                                            ponto.setLongitude(Util.coordinateMinToDegree(longitude));
                                            ponto.setEwIndicator(ewIndicator);
                                            ponto.setGpsQuality(gpsQuality);
                                            ponto.setSatellitesUsed(satellitesUsed);
                                            ponto.setPDOP(PDOP);
                                            ponto.setHDOP(HDOP);
                                            ponto.setVDOP(VDOP);
                                            ponto.setAltitude(altitude);
                                            ponto.setDifferencialReferenceStationID(differencialReferenceStationID);
                                            ponto.setStatus(status);
                                            ponto.setSpeedGPS(speedGPS);
                                            ponto.setCourse(course);
                                            ponto.setUtcDate(Integer.parseInt(utcDate));
                                            ponto.setModeIndicator(modeIndicator);

                                            // UTC time in hhmmss.sss format (000000.00 ~ 235959.999)
                                            // UTC date of position fix, ddmmyy format
                                            Date data = null;
                                            try {
                                                //data = new SimpleDateFormat("ddMMyyHHmmss.SSSZ").parse(utcDate+utcTime+"+0000");  
                                                data = new SimpleDateFormat("ddMMyyHHmmss.SSS").parse(utcDate + utcTime);

                                                ponto.setDateObject(data);

                                                //ponto.print();
                                                quantidade++;

                                                //if(quantidade % 10 == 0)
                                                listCoordinate.add(ponto);

                                            } catch (ParseException e) {
                                                // e.printStackTrace(); 
                                            } finally {
                                                gpsQuality = null;
                                                status = null;
                                                modeIndicator = null;
                                            }
                                            //SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zZ");
                                            //System.out.println( format1.format(data) );          

                                            ponto.setLdr(ldr);
                                            ponto.setAx(ax);
                                            ponto.setAy(ay);
                                            ponto.setAz(az);
                                            ponto.setGx(gx);
                                            ponto.setGy(gy);
                                            ponto.setGz(gz);
                                            ponto.setMx(mx);
                                            ponto.setMy(my);
                                            ponto.setMz(mz);

                                        }

                                        utcTime = split[1].trim();
                                        latitude = Double.parseDouble(split[2]);
                                        nsIndicator = split[3].charAt(0);
                                        longitude = Double.parseDouble(split[4]);
                                        ewIndicator = split[5].charAt(0);
                                        // gpsQuality = split[6].charAt(0);
                                        // satellitesUsed = Integer.parseInt(split[7]);
                                        //HDOP = Double.parseDouble(split[8]);
                                        // altitude = Double.parseDouble(split[9]);
                                        // differencialReferenceStationID = Integer.parseInt(split[14].split("\\*")[0]);
                                    }

                                    if (split[0].equals("$GPGSA")) {
                                        //PDOP = Double.parseDouble(split[15]);
                                        //HDOP = Double.parseDouble(split[16]);
                                        //VDOP = Double.parseDouble(split[17].split("\\*")[0]);
                                    }

                                    if (split[0].equals("$GPRMC")) {
                                        //status = split[2].charAt(0);
                                        //speedGPS = Double.parseDouble(split[7]);
                                        course = Double.parseDouble(split[8]);
                                        utcDate = split[9].trim();
                                        //modeIndicator = split[12].split("\\*")[0].charAt(0);
                                    }

                                    if (split[0].equals("$GPVTG")) {
                                        speedGPS = Double.parseDouble(split[7]);
                                    }

                                    if (split[0].equals("$LAGM")) {
                                        ldr = Double.parseDouble(split[4]);
                                        ax = Double.parseDouble(split[5]);
                                        ay = Double.parseDouble(split[6]);
                                        az = Double.parseDouble(split[7]);
                                        gx = Double.parseDouble(split[11]);
                                        gy = Double.parseDouble(split[12]);
                                        gz = Double.parseDouble(split[13]);
                                        mx = Double.parseDouble(split[8]);
                                        my = Double.parseDouble(split[9]);
                                        mz = Double.parseDouble(split[10]);
                                    }
                                }
                            } catch (Exception e) {

                                //e.printStackTrace();
                                /*gpsQuality = null;
                                 status = null;
                                 modeIndicator = null;*/
                                latitude = null;
                                longitude = null;
                                course = null;

                            }
                        } // end while

                        // adiciona o último
                        //if(ponto != null)
                        //  collarDataDAO.insertDataVenus(collar, ponto);
                        if (ActivityRecognition.DEBUG) {
                            System.out.println("Lista de coordenadas de GPS criada. Tamanho = " + listCoordinate.size());
                        }

                        //LinkedList<Observation> lo = readTxtObservationsLomba(collar);
                        collarDataDAO.insertDataVenus(collar, listCoordinate);
                        //collarDataDAO.insertObservation(lo);

                        /*for( CoordinateVenus o : listCoordinate) {
                         System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(o.getDateObject()) + ";"+ o.getDateObject().getTime() + "; 1;");
                         }                
                         for(Observation o : lo) {
                         System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(o.getData())+ ";"+ o.getObservation() +";" + o.getData().getTime() + "; 2;");
                         }
                         */
                        //ActivityRecognition ac = new ActivityRecognition(listCoordinate, lo);
                        //System.out.println("quantidade = "+quantidade);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    // dados de observações
                    LinkedList<Observation> lo = readTxtObservationsLomba(collar, fileAtual);
                    try {
                        collarDataDAO.insertObservation(lo);
                    } catch (Exception ex) {
                        Logger.getLogger(CollarDataController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public LinkedList<Observation> readTxtObservationsLomba(String collar, File file) {
        CollarDataDAO collarDataDAO = new CollarDataDAO();

        //Define o filtro da extensão (.txt)
        /*FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);
         */
        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                labelFile.setText(file.getPath());
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //String que receberá as linhas do arquivo
                String line;

                LinkedList<Observation> listObservations = new LinkedList<>();

                //Atributos
                Long time;
                Date data;
                String observation;

                while ((line = is.readLine()) != null) {
                    //se a linha possui caracteres
                    if (line.length() > 0) {

                        String[] split = line.split(",");

                        if (split.length > 2) {
                            data = new SimpleDateFormat("HHmmss ddMMyy").parse(split[0] + " " + split[1]);
                            time = data.getTime();
                            observation = split[2].trim();

                            Observation o = new Observation(time, data, observation, collar);
                            listObservations.add(o);
                        }
                    }
                }
                if (ActivityRecognition.DEBUG) {
                    System.out.println("Lista de observações criada. Tamanho = " + listObservations.size());
                }

                return listObservations;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public LinkedList<Observation> readTxtObservations(String collar) {
        CollarDataDAO collarDataDAO = new CollarDataDAO();

        //Define o filtro da extensão (.txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                labelFile.setText(file.getPath());
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //String que receberá as linhas do arquivo
                String line;

                LinkedList<Observation> listObservations = new LinkedList<>();

                //Atributos
                Long time;
                Date data;
                String observation;

                while ((line = is.readLine()) != null) {
                    //se a linha possui caracteres
                    if (line.length() > 0) {

                        String[] split = line.split(";");

                        time = Long.parseLong(split[0]);
                        data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zZ").parse(split[1]);
                        observation = split[2].trim();

                        Observation o = new Observation(time, data, observation, collar);
                        listObservations.add(o);

                        //collarDataDAO.insertObservation(o);
                    }
                }
                if (ActivityRecognition.DEBUG) {
                    System.out.println("Lista de observações criada. Tamanho = " + listObservations.size());
                }

                return listObservations;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Extrai as informações da linha.
     */
    public Coordinate extractTxT2ndInfo(String line, String collar) throws Exception {
        StringTokenizer st = new StringTokenizer(line);
        String word; //string que receberá as informacoes retiradas das linhas
        String lat_d, lon_d, date, hour;
        String minus = "-";

        //latitude - posicao geografica
        word = st.nextToken(",");
        System.out.println(word);
        System.out.println(getLatitude(word.substring(1)));
        lat_d = getLatitude(word.substring(1));

        //latitude (N e S)
        word = st.nextToken(",");
        System.out.println(word);

        //Se a latitude for South (Sul), o grau será negativo.
        if (word.contains("S")) {
            lat_d = minus.concat(lat_d);
        }

        //longitude - posicao geografica
        word = st.nextToken(",");
        System.out.println(word);
        System.out.println(getLongitude(word));
        lon_d = getLongitude(word);

        //longitude (W e E)
        word = st.nextToken(",");
        System.out.println(word);

        //Se a longitude for West (Oeste), o grau será negativo.
        if (word.contains("W")) {
            lon_d = minus.concat(lon_d);
        }

        //dia
        word = st.nextToken(",");
        System.out.println(word);
        date = getDate(word);
        System.out.println(date);

        //hora
        word = st.nextToken(",");
        System.out.println(word);
        hour = getHour(word);
        System.out.println(hour);

        //Acesso ao Banco de Dados
        CollarDataDAO cdd = new CollarDataDAO();
        cdd.insertData(collar, lon_d, lat_d, date, hour);

        return new Coordinate(collar, date, hour);
    }

    /**
     * Método que efetua a leitura de um .txt com os dados dos colares da
     * segunda versão dos equipamentos OTAG.
     */
    public void readTxt2nd(String collar) {
        ObservableList<Coordinate> trajectory = FXCollections.observableArrayList();
        CollarDataDAO cdd = new CollarDataDAO();

        //Abre um diretório de um já existente
        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
            fileChooser.getExtensionFilters().clear();
        }

        //Define o filtro da extensão (.txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                labelFile.setText(file.getPath());
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //String que receberá as linhas do arquivo
                String line;

                while ((line = is.readLine()) != null) {
                    //se a linha possui caracteres
                    if (line.length() > 0) {
                        System.out.println(line);
                        if (!line.contains("%")) {
                            trajectory.add(extractTxT2ndInfo(line, collar));
                        }
                    }
                }

                //Adiciona a trajetória
                String gapTime = subtractHours(trajectory.get(0).getHour(), trajectory.get(1).getHour());
                cdd.insertTrajectoryARFF(trajectory.get(0).getCollar(), trajectory.get(0).getDate(), gapTime, trajectory.get(0).getHour(), trajectory.get(trajectory.size() - 1).getHour());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Extrai as informações de Instances.
     */
    public void extractARFFInfo(Instances datas) throws Exception {
        CollarDataDAO cdd = new CollarDataDAO();

        for (int k = 0; k < datas.numInstances(); k++) {
            Instance data = datas.instance(k);
            //for (Instance data : datas) {
            String collar = data.stringValue(0);

            Coordinate coord = new Coordinate(data.value(1), data.value(2), data.stringValue(3), data.stringValue(4));
            cdd.insertDataARFF(collar, coord);
        }

    }

    /**
     * Extrai as informações de Instances.
     */
    public void extractTrajectoryARFFIntro(Instances datas) throws Exception {
        CollarDataDAO cdd = new CollarDataDAO();

        String collar = datas.instance(0).stringValue(0);
        String date = datas.instance(0).stringValue(3);
        String hourBegin = datas.instance(0).stringValue(4);
        String gapTime;

        for (int i = 0; i < datas.numInstances(); i++) {
            if (!date.equals(datas.instance(i).stringValue(3))) {
                gapTime = subtractHours(datas.instance(i - 2).stringValue(4), datas.instance(i - 1).stringValue(4));
                System.out.println(collar + " " + date + " " + gapTime + " " + hourBegin + " " + datas.instance(i - 1).stringValue(4));
                cdd.insertTrajectoryARFF(collar, date, gapTime, hourBegin, datas.instance(i - 1).stringValue(4));

                collar = datas.instance(i).stringValue(0);
                date = datas.instance(i).stringValue(3);
                hourBegin = datas.instance(i).stringValue(4);
            }

        }

        gapTime = subtractHours(datas.instance(datas.numInstances() - 2).stringValue(4), datas.instance(datas.numInstances() - 1).stringValue(4));
        cdd.insertTrajectoryARFF(collar, date, gapTime, hourBegin, datas.instance(datas.numInstances() - 1).stringValue(4));
    }

    /**
     * Método que efetua a leitura de um .txt com os dados dos colares.
     */
    public void readARFF() throws IOException, Exception {
        //Abre um diretório de um já existente
        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
            fileChooser.getExtensionFilters().clear();
        }

        //Define o filtro da extensão (.arrd)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF files (*.arff)", "*.arff");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            try {
                ConverterUtils.DataSource source = new ConverterUtils.DataSource(file.getPath());
                labelFile.setText(file.getPath());

                Instances data = source.getDataSet();
                System.out.println(data.toString());
                // setting class attribute if the data format does not provide this information
                // For example, the XRFF format saves the class attribute information as well
                if (data.classIndex() == -1) {
                    data.setClassIndex(data.numAttributes() - 1);
                }

                extractARFFInfo(data);
                extractTrajectoryARFFIntro(data);

            } catch (Exception e) {

                System.out.println("Erro: " + e.getMessage());
            }
        }

    }

    /**
     *
     *
     */
    public void readTxtINMET() {

        CollarDataDAO collarDataDAO = new CollarDataDAO();

        //Abre um diretório de um já existente
        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
            fileChooser.getExtensionFilters().clear();
        }

        //Define o filtro da extensão (.txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null);

        //Caso algum arquivo tenha sido selecionado
        if (file != null) {
            labelFile.setText(file.getPath());            
            try {
                BufferedReader is = new BufferedReader(new FileReader(file.getPath()));

                //String que receberá as linhas do arquivo
                String line;
                String variavelAmbiental[];
                String horaUTC[];

                LinkedList<MeteorologicalData> listMeteorologicalData = new LinkedList<>();

                //Atributos
                Date date = null;
                String label = null;
                Float value = null;

                variavelAmbiental = is.readLine().split(";");
                horaUTC = is.readLine().split(";");

                while ((line = is.readLine()) != null) {
                    if (line.length() > 0) {
                        String[] split = line.split(";");

                        for (int i = 1; i < split.length; i++) {
                            try {
                                // 01-jan-2013    ....    0300
                                date = new SimpleDateFormat("dd-MMM-yyyy HHmm z").parse(split[0] + " " + horaUTC[i] + " UTC");
                                //date = new SimpleDateFormat("dd-MMM-yyyy").parse(split[0]);
                                label = variavelAmbiental[i];
                                value = Float.parseFloat(split[i]);
                            } catch (ParseException ex) {
                                Logger.getLogger(CollarDataController.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            } catch (NumberFormatException ex) {
                                //Logger.getLogger(CollarDataController.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            }

                            listMeteorologicalData.add(new MeteorologicalData(date, label, value));
                        }
                    }
                } // end while
                
                if(listMeteorologicalData.size() > 0) {
                    collarDataDAO.insertMeteorologicalData(listMeteorologicalData);
                }

            } catch (Exception ex) {
                Logger.getLogger(CollarDataController.class.getName()).log(Level.SEVERE, null, ex);
            }     
        }
    }
}
