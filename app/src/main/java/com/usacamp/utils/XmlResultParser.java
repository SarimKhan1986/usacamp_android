package com.usacamp.utils; /**
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

/**
 * <p>Title: XmlResultParser</p>
 * <p>Description: </p>
 * <p>Company: www.iflytek.com</p>
 * @author iflytek
 * @date 2015年1月12日 下午5:21:53
 */
public class XmlResultParser {
	
	public FinalResult parse(String xml) {
		if (TextUtils.isEmpty(xml)) {
			return null;
		}
		
		XmlPullParser pullParser = Xml.newPullParser();
		
		try {
			InputStream ins = new ByteArrayInputStream(xml.getBytes());
			pullParser.setInput(ins, "utf-8");
			FinalResult finalResult = null;
			
			int eventType = pullParser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("FinalResult".equals(pullParser.getName())) {
						// 只有一个总分的结果
						finalResult = new FinalResult();
					} else if ("ret".equals(pullParser.getName())) {
						finalResult.ret = getInt(pullParser, "value");
					} else if ("total_score".equals(pullParser.getName())) {
						finalResult.total_score = getFloat(pullParser, "value");
					}
					
					break;
				case XmlPullParser.END_TAG:
					if ("FinalResult".equals(pullParser.getName())) {
						return finalResult;
					}
					break;
				
				default:
					break;
				}
				eventType = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private float getFloat(XmlPullParser pullParser, String attrName) {
		String val = pullParser.getAttributeValue(null, attrName);
		if (null == val) {
			return 0f;
		}
		return Float.parseFloat(val);
	}

	private String getContent(XmlPullParser pullParser) {
		return pullParser.getAttributeValue(null, "content");
	}
	
	private int getInt(XmlPullParser pullParser, String attrName) {
		String val = pullParser.getAttributeValue(null, attrName);
		if (null == val) {
			return 0;
		}
		return Integer.parseInt(val);
	}
}
