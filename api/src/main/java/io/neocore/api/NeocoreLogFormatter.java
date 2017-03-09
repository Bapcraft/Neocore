package io.neocore.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class NeocoreLogFormatter extends Formatter {

	private static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);

	private static final String LOG_FORMAT = "[%s] %s %s %s:%s %s";

	@Override
	public String format(LogRecord r) {

		String message = r.getMessage();
		Level l = r.getLevel();
		String clazz = r.getSourceClassName();
		String meth = r.getSourceMethodName();
		Throwable t = r.getThrown();
		String threadName = Thread.currentThread().getName();
		String time = DATE_FORMAT.format(new Date(r.getMillis()));

		String formatted = String.format(LOG_FORMAT, time, l.getName(), threadName, clazz, meth, message);
		StringBuilder sb = new StringBuilder(formatted);
		if (t != null) {

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			sb.append("\nStack Trace:\n" + sw.toString() + "\n");

		}

		return sb.toString();

	}

}
