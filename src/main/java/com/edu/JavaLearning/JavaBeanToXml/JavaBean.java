package com.edu.JavaLearning.JavaBeanToXml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Tangzhihao
 * @date 2018/5/7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class JavaBean {
    @XmlElement(name = "ID")
    private String id;
    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "Amount")
    private String amount;

    public JavaBean(String id,String name,String password,String amount){
        this.id = id;
        this.name = name;
        this.password = password;
        this.amount = amount;
    }
    public JavaBean(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "JavaBean [id="+id+",name="+name+",password="+password+",amount="+amount+" ]";
    }
}

class JavaBeanToXml{

    public static void main(String[] args) {
        JavaBean javaBean = new JavaBean("1", "唐", "123", "29.9");
        String xml = beanToXml(javaBean, "UTF-8");
        System.out.println(xml);
        JavaBean bean = xmlToBean(xml, JavaBean.class);
        System.out.println(bean.toString());
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/5/7 14:2
     * Bean To Xml
     */
    public static String beanToXml(Object obj,String encoding){
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            //创建一个可以用来将 java 内容树转换为 XML 数据的 Marshaller 对象
            Marshaller marshaller = context.createMarshaller();
            //用来指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //指定编码
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //用来指定已编组 XML 数据中输出编码的属性名称。
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            StringWriter writer = new StringWriter();
            //将以 jaxbElement 为根的内容树编组到 Writer 中。
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description: Xml To Bean
     */
    public static <T> T xmlToBean(String xml,Class<T> c){
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            //创建一个可以用来将 XML 数据转换为 java 内容树的 Unmarshaller 对象
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //从指定内容中解组xml数据并返回java内容树
            t = (T)unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return t;
    }
}
