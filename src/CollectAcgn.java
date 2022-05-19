import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.Stack;

public class CollectAcgn {
    static Stack<File[]> dirs;

    public static void run(){
        dirs=new Stack<>();

        Scanner scanner=new Scanner(System.in);
        System.out.print("请输入目录存放文件夹");
        String menu=scanner.nextLine();
        System.out.println("使用 "+menu+" 作为工作目录");
        System.out.print("请输入动漫存放文件夹");
        String animes=scanner.nextLine();
        System.out.println("使用 "+animes+" 作为动漫存放目录");

        File menufile=new File(menu);
        File animesfile=new File(animes);
        dirs.push(new File[]{menufile,animesfile});
        System.out.println(dirs.size());
        operateFile();
    }

    private static void operateFile() {
        while(true){
            //showStack();
            if(dirs.size()<4){
                newAFile();
            }else{
                newAXml();
            }

        }

    }

    private static void newAXml() {
        //newAFile();
        try {
            newxml();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void newAFile() {
        File[] dir=dirs.lastElement();
        Scanner scanner=new Scanner(System.in);
        System.out.println();
        System.out.println("now: "+dir[0].getPath());
        System.out.println("要做什么？\n 1：新建文件夹，2：向新建文件夹写资源，3：返回上一级,4:回根");
        String todo=scanner.nextLine();
        if(todo.equals("1")){
            System.out.println("请输入文件名");
            todo=scanner.nextLine();
            mkdir(todo);
        }else if (todo.equals("2")){
            try {
                newxml();
            }catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }else if(todo.equals("3")) {
            dirs.pop();
        }else if(todo.equals("4")){
            File[] fi=dirs.firstElement();
            dirs.clear();
            dirs.push(fi);
        }else{
            mkdir(todo);
        }
    }

    private static void newxml() throws ParserConfigurationException, TransformerException {
        File[] dir=dirs.pop();
        //dirs.pop();
        System.out.println("新建一个xml文件");
        File f=new File(dir[0],"from.xml");
        Scanner scanner=new Scanner(System.in);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
        Document document=documentBuilder.newDocument();

        System.out.println("请输入源文件");
        String ori=scanner.nextLine();
        System.out.println("请输入文件名变化");
        String changes=scanner.nextLine();

        Element kindnode=document.createElement("kindnode");

        Element originpath=document.createElement("path");
        originpath.setTextContent(ori);

        Element change=document.createElement("change");
        change.setTextContent(changes);

        kindnode.appendChild(originpath);
        kindnode.appendChild(change);

        document.appendChild(kindnode);

        TransformerFactory transformerFactory=TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("encoding","UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        StringWriter writer=new StringWriter();
        //transformer.transform(new DOMSource(document),new StreamResult(writer));
        transformer.transform(new DOMSource(document),new StreamResult(f));
    }

    private static void mkdir(String todo) {
        File[] dir=dirs.lastElement();

        File submenu=new File(dir[0],todo);
        File subanime=new File(dir[1],todo);
        if(!subanime.exists()){
            subanime.mkdir();
        }
        if(!submenu.exists()){
            submenu.mkdir();
        }
        dirs.push(new File[]{submenu,subanime});
    }
    private static void showStack(){
        System.out.println("Stack:");
        for(int i=dirs.size();i<0;i++){
            System.out.println("i: "+dirs.get(i));
        }
    }
}
