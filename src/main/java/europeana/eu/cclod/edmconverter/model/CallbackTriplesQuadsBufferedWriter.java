package europeana.eu.cclod.edmconverter.model;

import org.apache.jena.riot.Lang;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;

import java.io.BufferedWriter;
import java.io.IOException;

public class CallbackTriplesQuadsBufferedWriter implements Callback {
	private final BufferedWriter _bw;

	private long _cnt = 0;
	private long _time, _time1;
	private final boolean _close;

	private final static String DOTNEWLINE = "."
			+ System.getProperty("line.separator");

	private Lang nxOption;
	
	/**
	 * ...if close flag is set true, endDocument() will close
	 * the BufferedWriter
	 */
	public CallbackTriplesQuadsBufferedWriter(BufferedWriter out, boolean close, Lang triplesOrQuads) {
		_bw = out;
		_close = close;
		nxOption=triplesOrQuads;
	}

	public synchronized void processStatement(Node[] nx) {
		try {
			int maxNodes=nxOption==Lang.NTRIPLES ? 3 : 4;
			for(int i=0; i<maxNodes; i++) {
				_bw.write(nx[i].toN3());
				_bw.write(' ');
			}
			_bw.write(DOTNEWLINE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		_cnt++;
	}

	public void startDocument() {
		_time = System.currentTimeMillis();
	}
	
	public void endDocument() {
		try {
			if (_close)
				_bw.close();
			else
				_bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		_time1 = System.currentTimeMillis();
	}

	public String toString() {
		return _cnt + " tuples in " + (_time1 - _time) + " ms";
	}
}
