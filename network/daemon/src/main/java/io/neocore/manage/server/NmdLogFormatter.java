package io.neocore.manage.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class NmdLogFormatter extends Formatter {

	private static final String formatStr = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);

	@Override
	public String format(LogRecord record) {

		Date d = new Date(record.getMillis());
		String df = dateFormat.format(d);
		String tName = Thread.currentThread().getName();

		StringBuilder sb = new StringBuilder(
				String.format("[%s] %s %s : %s", df, record.getLevel(), tName, record.getMessage()));

		// Just check to see if we have an exception to deal with.
		Throwable t = record.getThrown();
		if (t != null) {

			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			sb.append("\nStack Trace:\n" + sw.toString() + "\n");

		}

		return sb.toString() + "\n";

	}

	static {

		// ^_^
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

	}

}
