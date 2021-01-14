package com.eapplication.eapplicationback.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encoding {

	//	private static final Charset utf8charset = Charset.forName(StandardCharsets.UTF_8.name());
	//	private static final Charset iso88591charset = Charset.forName(StandardCharsets.ISO_8859_1.name());

	public static String toISO8859_1(String text) {

		//String encodedWithUTF8 = "üzüm bağları";
		try {
			return new String(text.getBytes(), "ISO-8859-1");
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		//		try {
		//
		//			ByteBuffer inputBuffer = ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8));
		//			// decode UTF-8
		//			CharBuffer data = StandardCharsets.UTF_8.decode(inputBuffer);
		//			// encode ISO-8559-1
		//			ByteBuffer outputBuffer = StandardCharsets.ISO_8859_1.encode(data);
		//			byte[] outputData = outputBuffer.array();
		//
		//			return new String(outputData, StandardCharsets.ISO_8859_1);
		//
		//		} catch (Exception e) {
		//			throw new IllegalStateException(e);
		//		}
	}

	public static String toISO8859_1UrlEncode(String text) throws UnsupportedEncodingException {
		return URLEncoder.encode(text, StandardCharsets.ISO_8859_1.toString());
	}
}
