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

public class CollectAcgn {
    public static void run(){
        Scanner scanner=new Scanner(System.in);

        System.out.println("请输入目录存放文件夹");
        String menu=scanner.nextLine();
        System.out.println("使用 "+menu+" 作为工作目录");
        String animes=scanner.nextLine();
        System.out.println("使用 "+animes+" 作为动漫存放目录");

        File menufile=new File(menu);
        File animesfile=new File(animes);

        operateFile(menufile,animesfile);
    }

    private static void operateFile(File menufile, File animesfile) {
        if(!menufile.exists()){
            menufile.mkdir();
        }
        if(!animesfile.exists()){
            animesfile.mkdir();
        }
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.println("要做什么？\n 1：新建文件夹，2：向新建文件夹写资源，3：返回上一级");
            String todo=scanner.nextLine();
            if(todo.equals("1")){
                System.out.println("请输入文件名");
                todo=scanner.nextLine();
                mkdir(todo,menufile,animesfile);
            }else if (todo.equals("2")){
                try {
                    newxml(menufile,animesfile);
                }catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            }else if(todo.equals("3")) {
                break;
            }else{
                mkdir(todo,menufile,animesfile);
            }
        }

    }

    private static void newxml(File menufile, File animesfile) throws ParserConfigurationException, TransformerException {
        System.out.println("新建一个xml文件");
        File f=new File(menufile,"from.xml");
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

    private static void mkdir(String todo, File menufile, File animesfile) {
        File submenu=new File(menufile,todo);
        File subanime=new File(animesfile,todo);
        if(!subanime.exists()){
            subanime.mkdir();
        }
        if(!submenu.exists()){
            submenu.mkdir();
        }
        operateFile(submenu,subanime);
    }
}
