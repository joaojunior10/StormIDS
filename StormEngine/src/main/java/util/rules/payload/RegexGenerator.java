package util.rules.payload;

import util.rules.payload.options.Content;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by joao on 8/11/15.
 */
public class RegexGenerator {

    public static Pattern generate(List<Content> contents){
        StringBuilder regex = new StringBuilder();
        for(Content content : contents){
            String depth = "";
            String offset = "";
            String distance = "0";
            String nocase = "";
            if(content.nocase){
                nocase = "(?i)";
            }
            if(content.depth != null) {
                depth = content.depth.toString();
            }
            if(content.offset != null) {
                offset = "^";
                distance = content.offset.toString();
            }
            if(content.distance != null){
                distance = content.distance.toString();
            }
            if(content.within != null){
                depth = content.within.toString();
            }
            content.content =  content.content.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
            String contentPattern = String.format("%s%s(.|\\n|\\r\\n){%s,%s}%s",nocase,offset, distance,depth,content.content);
            regex.append(contentPattern);
        }
//        try {
//            Pattern test = Pattern.compile(regex.toString());
//        }catch (Exception e){
//            Gson gson = new Gson();
////            System.err.println(gson.toJson(contents));
//        }
        return Pattern.compile(regex.toString());
    }
}
