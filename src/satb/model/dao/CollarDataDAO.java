package satb.model.dao;

import java.sql.*;
import java.util.LinkedList;
import org.postgis.Point;
import satb.Util;
import satb.model.Coordinate;
import satb.model.Database;
import satb.behavior.Observation;
import satb.model.CoordinateVenus;

/**Classe com manipulação de métodos de inserção de dados capturados pelos colares.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class CollarDataDAO 
{
    //Atributos
    private Database base;
       
    /**Construtor default. */
    public CollarDataDAO(){}
    
    
    /**Recebe latitude e longitude e retorna uma coordenada do tipo Geometry. */     
    private static String getPoint(String x, String y)
    {
        String point = "POINT(";
        point = point.concat(x);
        point = point.concat(" ");
        point = point.concat(y);
        point = point.concat(")");
        
        return point;
    }
       
    /**Insere no banco os dados obtidos pelos colares. */
    public void insertData(String collar, String longitude, String latitude, String date, String hour) throws Exception 
    {
        base = new Database();
	// iniciando a conexao
	System.out.println("Conectado e preparando a inserção dos dados capturados pelos colares");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO animal_position (collar, longitude, latitude, coordinate, date, hour) "
            + "VALUES ('" + collar + "', '" + longitude + "', '" + latitude + "','" + getPoint(longitude, latitude) +  "', '" + date + "', '" + hour + "')");                
        }
        catch (SQLException e) 
	{
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
		}
	} 
   }
   

    
   /**Insere no banco os dados obtidos pelos colares e armazenados em ARFFs. */
   public void insertDataARFF(String collar, Coordinate c) throws Exception 
   {
        base = new Database();
	System.out.println("Conectado e preparando a inserção dos dados do ARFF");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO animal_position (collar, longitude, latitude, coordinate, date, hour) "
            + "VALUES ('" + collar + "', '" + c.getLongitudeX() + "', '" + c.getLatitudeY() + "','" + getPoint(String.valueOf(c.getLongitudeX()), String.valueOf(c.getLatitudeY())) +  "', '" + c.getDate() + "', '" + c.getHour() + "')");                
        }
        catch (SQLException e) 
	{
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
		}
	} 
   }
   
   
    public void insertDataVenus (String collar, CoordinateVenus c) throws Exception {
        insertDataARFF(collar, c);
       
        base = new Database();
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select max(id) as id from animal_position");
            rs.next();
            c.setId(rs.getInt("id"));
            
            stmt.execute("INSERT INTO public.animal_position_venus(" +
                    "	id, \"nsIndicator\", \"ewIndicator\", \"utcTime\", \"utcDate\", \"speedGPS\", date, \"gpsQuality\", \"satellitesUsed\","
                    + " \"PDOP\", \"HDOP\", \"VDOP\", altitude, \"differencialReferenceStationID\", status, course, \"modeIndicator\") "
            + " VALUES ("+c.getId()+", '"+c.getNsIndicator()+"', '"+c.getEwIndicator()+"', "+c.getUtcTime()+", "+c.getUtcDate()+", "+c.getSpeedGPS()+", "+
                    c.getDateObject().getTime()+", '"+c.getGpsQuality()+"', "+c.getSatellitesUsed()+", "+c.getPDOP()+", "+c.getHDOP()+", "+c.getVDOP()+", "+
                    c.getAltitude()+", "+c.getDifferencialReferenceStationID()+", '"+c.getStatus()+"', "+c.getCourse()+", '"+c.getModeIndicator()+"');");                
        }
        catch (SQLException e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
    }
    
    public void insertDataVenus (String collar, LinkedList<CoordinateVenus> listCoordinate) throws Exception {
        
        base = new Database();
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            
            
            for( CoordinateVenus c : listCoordinate) {
            
                //insertDataARFF(collar, c);
                stmt.execute("INSERT INTO animal_position (collar, longitude, latitude, coordinate, date, hour) "
                + "VALUES ('" + collar + "', '" + c.getLongitudeX() + "', '" + c.getLatitudeY() + "','" + getPoint(String.valueOf(c.getLongitudeX()), String.valueOf(c.getLatitudeY())) +  "', '" + c.getDate() + "', '" + c.getHour() + "')");                

                ResultSet rs = stmt.executeQuery("select max(id) as id from animal_position");
                rs.next();
                c.setId(rs.getInt("id"));

                // Character
                
                stmt.execute("INSERT INTO public.animal_position_venus(" +
                        "	id, \"nsIndicator\", \"ewIndicator\", \"utcTime\", \"utcDate\", \"speedGPS\", date, \"gpsQuality\", \"satellitesUsed\","
                        + " \"PDOP\", \"HDOP\", \"VDOP\", altitude, \"differencialReferenceStationID\", status, course, \"modeIndicator\", ldr, ax, ay, az, gx, gy, gz, mx, my, mz) "
                        + " VALUES ("+c.getId()+", "+Util.toNullSQL(c.getNsIndicator())+", "+Util.toNullSQL(c.getEwIndicator())+", "+
                        Util.toNullSQL(c.getUtcTime())+", "+Util.toNullSQL(c.getUtcDate())+", "+Util.toNullSQL(c.getSpeedGPS())+", "+
                        Util.toNullSQL(c.getDateObject().getTime())+", "+Util.toNullSQL(c.getGpsQuality())+", "+Util.toNullSQL(c.getSatellitesUsed())+", "+
                        Util.toNullSQL(c.getPDOP())+", "+Util.toNullSQL(c.getHDOP())+", "+Util.toNullSQL(c.getVDOP())+", "+
                        Util.toNullSQL(c.getAltitude())+", "+Util.toNullSQL(c.getDifferencialReferenceStationID())+", "+
                        Util.toNullSQL(c.getStatus())+", "+Util.toNullSQL(c.getCourse())+", "+Util.toNullSQL(c.getModeIndicator())+", "+
                        Util.toNullSQL(c.getLdr())+", "+
                        Util.toNullSQL(c.getAx())+", "+Util.toNullSQL(c.getAy())+", "+Util.toNullSQL(c.getAz())+", "+
                        Util.toNullSQL(c.getGx())+", "+Util.toNullSQL(c.getGy())+", "+Util.toNullSQL(c.getGz())+", "+
                        Util.toNullSQL(c.getMx())+", "+Util.toNullSQL(c.getMy())+", "+Util.toNullSQL(c.getMz())+");");                
            }
        }
        catch (SQLException e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
    }
    
    private CoordinateVenus populateObjectCoordinateVenus(ResultSet rs) throws SQLException {
        
        CoordinateVenus c = new CoordinateVenus();
        
        c.setId(rs.getInt("id"));
        //ap.id, ap.collar, ap.longitude, ap.latitude, ap.coordinate
        c.setCollar(rs.getString("collar"));
        c.setLongitude(rs.getDouble("longitude"));
        c.setLatitude(rs.getDouble("latitude"));
        c.setCoordinate( new Point(c.getLongitude(), c.getLatitude()) );
        if(rs.getString("nsIndicator") != null)
            c.setNsIndicator(rs.getString("nsIndicator").charAt(0));
        if(rs.getString("ewIndicator") != null)
            c.setEwIndicator(rs.getString("ewIndicator").charAt(0));
        c.setUtcTime(rs.getDouble("utcTime"));
        c.setUtcDate(rs.getInt("utcDate"));
        c.setSpeedGPS(rs.getDouble("speedGPS"));
        c.setDateObject(new java.util.Date(rs.getLong("date")));
        if(rs.getString("gpsQuality") != null)
            c.setGpsQuality(rs.getString("gpsQuality").charAt(0));
        c.setSatellitesUsed(rs.getInt("satellitesUsed"));
        c.setPDOP(rs.getDouble("PDOP"));
        c.setHDOP(rs.getDouble("HDOP"));
        c.setVDOP(rs.getDouble("VDOP"));
        c.setAltitude(rs.getDouble("altitude"));
        c.setDifferencialReferenceStationID(rs.getInt("differencialReferenceStationID"));
        if(rs.getString("status") != null)
            c.setStatus(rs.getString("status").charAt(0));
        c.setCourse(rs.getDouble("course"));
        if(rs.getString("modeIndicator") != null)
            c.setModeIndicator(rs.getString("modeIndicator").charAt(0));     
        
        if(rs.getString("ldr") != null)
            c.setLdr(rs.getDouble("ldr"));        
        if(rs.getString("ax") != null)
            c.setAx(rs.getDouble("ax"));        
        if(rs.getString("ay") != null)
            c.setAy(rs.getDouble("ay"));        
        if(rs.getString("az") != null)
            c.setAz(rs.getDouble("az"));        
        if(rs.getString("gx") != null)
            c.setGx(rs.getDouble("gx"));        
        if(rs.getString("gy") != null)
            c.setGy(rs.getDouble("gy"));        
        if(rs.getString("gz") != null)
            c.setGz(rs.getDouble("gz"));        
        if(rs.getString("mx") != null)
            c.setMx(rs.getDouble("mx"));        
        if(rs.getString("my") != null)
            c.setMy(rs.getDouble("my"));
        if(rs.getString("mz") != null)
            c.setMz(rs.getDouble("mz"));
        
        return c;
    }
    
    public LinkedList<CoordinateVenus> selectAllDataVenus () {
        
        LinkedList<CoordinateVenus> listCoordinate = new LinkedList<>() ;
        
	Statement stmt = null;
	try 
	{
            base = new Database();
            stmt = base.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select ap.id, ap.collar, ap.longitude, ap.latitude, ap.coordinate, \"nsIndicator\", \"ewIndicator\", \"utcTime\", \"utcDate\", \"speedGPS\", apv.\"date\", \n" +
                "\"gpsQuality\", \"satellitesUsed\", \"PDOP\", \"HDOP\", \"VDOP\", altitude, \n" +
                "\"differencialReferenceStationID\", status, course, \"modeIndicator\"\n" +
                ", ldr, ax, ay, az, gx, gy, gz, mx, my, mz "+
                "from animal_position ap\n" +
                "inner join animal_position_venus apv on apv.id = ap.id order by id");                

            while (rs.next()) 
            {                
                listCoordinate.add( populateObjectCoordinateVenus(rs) );
            }
        }
        catch (Exception e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
        
        return listCoordinate;
    }
    
    public LinkedList<CoordinateVenus> selectDataVenusWithLdr (String collar) {
        
        LinkedList<CoordinateVenus> listCoordinate = new LinkedList<>() ;
        
	Statement stmt = null;
	try 
	{
            base = new Database();
            stmt = base.getConnection().createStatement();
            
            String collarWhere = "";
            if(collar != null)
                collarWhere = " and collar like '"+collar+"' ";
            
            ResultSet rs = stmt.executeQuery("select ap.id, ap.collar, ap.longitude, ap.latitude, ap.coordinate, \"nsIndicator\", \"ewIndicator\", \"utcTime\", \"utcDate\", \"speedGPS\", apv.\"date\", \n" +
                "\"gpsQuality\", \"satellitesUsed\", \"PDOP\", \"HDOP\", \"VDOP\", altitude, \n" +
                "\"differencialReferenceStationID\", status, course, \"modeIndicator\"\n" +
                ", ldr, ax, ay, az, gx, gy, gz, mx, my, mz "+
                "from animal_position ap\n" +
                "inner join animal_position_venus apv on apv.id = ap.id where ldr is not null "+collarWhere+" order by id");                

            while (rs.next()) 
            {                
                listCoordinate.add( populateObjectCoordinateVenus(rs) );
            }
        }
        catch (Exception e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
        
        return listCoordinate;
    }
    
    public LinkedList<CoordinateVenus> selectDataVenus (String collar) {
        
        LinkedList<CoordinateVenus> listCoordinate = new LinkedList<>() ;
        
	Statement stmt = null;
	try 
	{
            base = new Database();
            stmt = base.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select ap.id, ap.collar, ap.longitude, ap.latitude, ap.coordinate, \"nsIndicator\", \"ewIndicator\", \"utcTime\", \"utcDate\", \"speedGPS\", apv.\"date\", \n" +
                "\"gpsQuality\", \"satellitesUsed\", \"PDOP\", \"HDOP\", \"VDOP\", altitude, \n" +
                "\"differencialReferenceStationID\", status, course, \"modeIndicator\"\n" +
                ", ldr, ax, ay, az, gx, gy, gz, mx, my, mz "+
                "from animal_position ap\n" +
                "inner join animal_position_venus apv on apv.id = ap.id where ap.collar = '"+collar+"' order by id ");                

            while (rs.next()) 
            {                
                listCoordinate.add( populateObjectCoordinateVenus(rs) );
            }
        }
        catch (Exception e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
        
        return listCoordinate;
    }
    
    
    private Observation populateObjectObservation(ResultSet rs) throws SQLException {        
        return new Observation( rs.getLong("time"), new java.util.Date(rs.getLong("time")), rs.getString("observation"), rs.getString("collar") );
    }
        
    public LinkedList<Observation> selectAllObservation () {
        
        LinkedList<Observation> listObservation = new LinkedList<>() ;
        
        Statement stmt = null;
	try 
	{
            base = new Database();
            stmt = base.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT \"time\", observation, collar\n" +
                "	FROM public.observation order by time;");                

            while (rs.next()) 
            {                
                listObservation.add( populateObjectObservation(rs) );
            }
        }
        catch (Exception e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
        
        return listObservation;
    }
    
    public LinkedList<Observation> selectObservation (String collar) {
        
        LinkedList<Observation> listObservation = new LinkedList<>() ;
        
        Statement stmt = null;
	try 
	{
            base = new Database();
            stmt = base.getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT \"time\", observation, collar\n" +
                "	FROM public.observation where collar like '"+collar+"' order by time;");                

            while (rs.next()) 
            {                
                listObservation.add( populateObjectObservation(rs) );
            }
        }
        catch (Exception e) 
	{
                e.printStackTrace();
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
                        e.printStackTrace();
		}
	}
        
        return listObservation;
    }
    
        
    public void insertObservation (LinkedList<Observation> lo) throws Exception {
        
        base = new Database();
	Statement stmt = null;
        try 
	{            
            stmt = base.getConnection().createStatement();
            
            for( Observation o : lo) {            
                stmt.execute("INSERT INTO public.observation(" +
                        "	time, observation, collar ) "
                + " VALUES ("+o.getTime()+",'"+o.getObservation()+"','"+o.getCollar()+"');");                
            }
        }
        catch (SQLException e) 
	{
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
		}
	}
    }
    
    public void insertObservation (Observation o) throws Exception {
        
        base = new Database();
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            
            stmt.execute("INSERT INTO public.observation(" +
                    "	time, observation, collar ) "
            + " VALUES ("+o.getTime()+",'"+o.getObservation()+"','"+o.getCollar()+"');");                
        }
        catch (SQLException e) 
	{
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
		}
	}
    }
  
   
   /**Insere no banco os dados as trajetórias contidas no ARFFs. */
   public void insertTrajectoryARFF(String collar, String date, String gap, String hourBegin, String hourEnd) throws Exception 
   {
        base = new Database();
	System.out.println("Conectado e preparando a inserção das trajetórias");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO trajectory (collar, date, gap_time, begin_hour, end_hour) "
            + "VALUES ('" + collar + "', '" + date + "', '" + gap + "','" + hourBegin +  "', '" + hourEnd + "')");                
        }
        catch (SQLException e) 
	{
		System.out.println(e.getMessage());
	} 
	finally 
	{
		try 
		{
			stmt.close();
			base.closeConnection();
		} 
		catch (SQLException e) 
		{
			System.out.println("Erro ao desconectar");
		}
	} 
   }
          
}