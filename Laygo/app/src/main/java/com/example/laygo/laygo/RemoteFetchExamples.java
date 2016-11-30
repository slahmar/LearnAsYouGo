package com.example.laygo.laygo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RemoteFetchExamples {

    private static final String DICTIONARY_API =
            "http://www.dictionaryapi.com/api/v1/references/thesaurus/xml/%s?key=214b2fb8-6e13-42c2-aa19-94bc1c0d4fbf";

    public static String getXML(Context context, String word){
        try {
            URL url = new URL(String.format(DICTIONARY_API, word));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer xml = new StringBuffer(100000);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                xml.append(tmp).append("\n");
            reader.close();
            DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            StringReader sr=new StringReader(xml.toString());
            is.setCharacterStream(sr);
            Document doc = db.parse(is);
            NodeList examples = doc.getElementsByTagName("vi");
            if(examples.getLength() > 0){
                Node example = examples.item(0);
                return example.getTextContent();
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }
}