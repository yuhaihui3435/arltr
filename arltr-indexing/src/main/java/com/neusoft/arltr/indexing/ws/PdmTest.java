package com.neusoft.arltr.indexing.ws;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(serviceName = "pdmTestService",targetNamespace = "http://kingdom.com/")
@Component
public class PdmTest {
    private static final Logger logger = Logger.getLogger(PdmTest.class.getName());


    public String getDataXML(@WebParam(name="arg0")String arg0){
        System.out.println(arg0);
        String xml="<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<root>\n" +
                "    <data>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000001</documentId>\n" +
                "            <documentTitle>测试文档1</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/1.doc</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000002</documentId>\n" +
                "            <documentTitle>测试文档2</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/2.pdf</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000003</documentId>\n" +
                "            <documentTitle>测试文档3</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/3.pdf</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000004</documentId>\n" +
                "            <documentTitle>测试文档4</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/3.pdf</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000005</documentId>\n" +
                "            <documentTitle>测试文档5</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/3.pdf</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000006</documentId>\n" +
                "            <documentTitle>测试文档6</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn/3.pdf</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000007</documentId>\n" +
                "            <documentTitle>测试文档7</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000008</documentId>\n" +
                "            <documentTitle>测试文档8</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000009</documentId>\n" +
                "            <documentTitle>测试文档9</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "        <documentInfo>\n" +
                "            <documentId>pdm000000010</documentId>\n" +
                "            <documentTitle>测试文档10</documentTitle>\n" +
                "            <documentDate>2018-08-18 00:00:11</documentDate>\n" +
                "            <author>xtf</author>\n" +
                "            <secretLevel>90</secretLevel>\n" +
                "            <versionSno>1.1</versionSno>\n" +
                "            <url>http://www.3435.net.cn</url>\n" +
                "            <fileType>ApplicationData</fileType>\n" +
                "        </documentInfo>\n" +
                "\n" +
                "    </data>\n" +
                "</root>";

                return xml;
    }

    public String getDocumentApplicationData(@WebParam(name = "arg0")String arg0){

            return "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<root>\n" +
                    "    <data>\n" +
                    "        <documentInfo>\n" +
                    "            <filePath>http://www.3435.net.cn/3.pdf</filePath>\n" +
                    "        </documentInfo>\n" +
                    "    </data>\n" +
                    "</root>";

    }
}
