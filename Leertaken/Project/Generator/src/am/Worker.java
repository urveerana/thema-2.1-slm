package am;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class Worker implements Runnable {

	public static int ID = 0;

	public int id;

	private Socket socket;
	private Corrector corrector;
	private Database database;

	public Worker( Socket socket, Database database ) {
		this.id        = ++Worker.ID;
		this.socket    = socket;
		this.corrector = new Corrector();
		this.database  = database;
	}

	public void run() {
		// System.out.println( "[Worker #" + this.id + "] Started." );

		BufferedReader in;
		Object[] record;

		try {
			// Prepare
			in = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );

			while( Server.isOpen ) {
				// System.out.println( "[Worker] worker #" + this.id + " working hard." );
				// Skip preface
				try {
					while( ! in.readLine().equals( "<?xml version=\"1.0\"?>" ) ); // Reset and skip
				}
				catch( Exception e ) {
					// System.out.println( "[Worker #" + this.id + "] Socket closed. Shutting down." );

					break;
				}

				in.readLine(); // <WEATHERDATA>

				// Read <MEASUREMENT> until </WEATHERDATA> is found
				while( in.readLine().equals( "\t<MEASUREMENT>" ) ) {
					record = new Object[ Record.SIZE ];

					this.parseRecord( in, record );

					this.corrector.validate( record );

					this.database.insertValue( Record.toDBObject( record ) );
				}
			}

			// System.out.println("[Worker] Shutting down worker #" + this.id + "..");

			// Close connection
			socket.close();
		}
		catch( Exception e ) {
			// Print error
			System.err.println( e );
			e.printStackTrace();
		}
		finally {
			// Clean up
			in = null;
		}
	}

	private void parseRecord( BufferedReader in, Object[] record ) throws java.io.IOException {
		try {
			record[ Record.STN ] = Integer.parseInt( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for STN" );
		}

		try {
			record[ Record.DATE ] = this.strip( in.readLine() );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for DATE" );
		}

		try {
			record[ Record.TIME ] = this.strip( in.readLine() );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for TIME" );
		}

		try {
			record[ Record.TEMP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for TEMP" );
		}

		try {
			record[ Record.DEWP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for DEWP" );
		}

		try {
			record[ Record.STP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for STP" );
		}

		try {
			record[ Record.SLP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for SLP" );
		}

		try {
			record[ Record.VISIB ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for VISIB" );
		}

		try {
			record[ Record.WDSP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for WDSP" );
		}

		try {
			record[ Record.PRCP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for PRCP" );
		}

		try {
			record[ Record.SNDP ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for SNDP" );
		}

		try {
			record[ Record.FRSHTT ] = Integer.parseInt( this.strip( in.readLine() ), 2 ); // Binary to integer
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for FRSHTT" );
		}

		try {
			record[ Record.CLDC ] = Double.parseDouble( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for CLDC" );
		}

		try {
			record[ Record.WNDDIR ] = Integer.parseInt( this.strip( in.readLine() ) );
		}
		catch( NumberFormatException e ) {
			// System.err.println( "[Worker #" + this.id + "] Invalid data for WNDDIR" );
		}

		in.readLine(); // </MEASUREMENT>
	}

	private String strip( String value )
	{
		return value.substring( value.indexOf( '>' ) + 1, value.lastIndexOf( '<' ) );
	}
}
