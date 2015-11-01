package util.rules.payload.options;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class Content implements Serializable{
	public String content;
    public byte[] bytecontent;
	public boolean nocase = false;
	public boolean rawbytes = false;
	public Integer depth;
	public Integer offset;
	public Integer distance;
	public Integer within;
	public boolean http_client_body;
	public boolean http_cookie;
	public boolean http_raw_cookie;
	public boolean http_header;
	public boolean http_uri;
	public boolean http_raw_uri;
	public boolean http_stat_code;
	public boolean http_stat_msg;
	public FastPattern fast_pattern;

	public void parseContent(String data) {
        StringBuilder content = new StringBuilder();
        ByteArrayOutputStream bytecontent = new ByteArrayOutputStream( );

        data = data.replace("\"", "");
        data = data.replace(";","");
        boolean isHex = false;
        StringBuilder item = new StringBuilder();
        for (int i = 0; i < data.length(); i++){
            if(data.charAt(i) == '|'){
                appendContent(content, bytecontent, isHex, item);
                //When find another | invert isHex
                isHex = !isHex;
                item = new StringBuilder();
            }
            item.append(data.charAt(i));
        }
        appendContent(content, bytecontent, isHex, item);

        this.content =  content.toString();
        this.bytecontent = bytecontent.toByteArray();

	}

    private void appendContent(StringBuilder content, ByteArrayOutputStream bytecontent, boolean isHex, StringBuilder item) {
        if(item.length() > 0) {
            if (isHex) {
                //hex string to string
                appendHex(content, bytecontent, item);
            } else {
                appendString(content, bytecontent, item);

            }
        }
    }

    private void appendString(StringBuilder content, ByteArrayOutputStream bytecontent, StringBuilder item) {
        byte[] encode = item.toString().replace("|", "").getBytes();
        String string = new String(encode, StandardCharsets.ISO_8859_1);
        content.append(string);
        try {
            bytecontent.write(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendHex(StringBuilder content, ByteArrayOutputStream bytecontent, StringBuilder item) {
        String hexString = item.toString().replace(" ", "").replace("|","");
        byte[] encode = DatatypeConverter.parseHexBinary(hexString);
        String string = new String(encode, StandardCharsets.ISO_8859_1);
        content.append(string);
        try {
            bytecontent.write(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toNocase() {
        if(nocase){
            this.content = this.content.toLowerCase();
        }
    }
}
