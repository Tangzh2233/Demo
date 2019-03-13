package com.edu.controller;

import com.edu.common.httpresp.HttpResponse;
import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.service.ILoginService;
import com.edu.service.quartz.DailyJob;
import com.edu.util.DateUtil;
import com.edu.util.ZipFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Tangzhihao
 * @date 2018/3/13
 */
@Controller
@RequestMapping("/myspringboot/test")
public class TestController {

    @Resource
    private ILoginService loginService;
    @Resource
    private DailyJob dailyJob;
    @Resource
    private UserMapper userMapper;
    public String userId;


    @RequestMapping("/upFile.html")
    public String showTest(){
        return "UpOrDownFile";
    }

    @RequestMapping("/addDlog.do")
    public String addDlog(Dlog dlog){
//        int i = new Random().nextInt(10);
//        dlog.setId(String.valueOf(i));
//        System.out.println(dlog.toString());
//        loginService.addDlog(dlog);
        new Thread(() -> userMapper.getUserByName("tang")).start();
        new Thread(()-> userMapper.getUserByName("tang")).start();
        return "main";
    }

    @ResponseBody
    @RequestMapping("/httpPost.do")
    public HttpResponse httpTest(User user){
        HttpResponse resp = new HttpResponse();
        HttpResponse.HttpRespData data = new HttpResponse.HttpRespData();
        HttpResponse.HttpRespError error = new HttpResponse.HttpRespError();
        if("tang".equals(user.getUsername())&&"123".equals(user.getPassword())){
            error.setReturnCode("00");
            error.setReturnMessage("SUCCESS");
        }else {
            error.setReturnCode("11");
            error.setReturnMessage("FAIL");
        }
        resp.setData(data);
        resp.setError(error);
        return resp;
    }

    @RequestMapping("/sendEmail.do")
    private String sendEmail(){
        dailyJob.execute();
        return "main";
    }

    @RequestMapping("/addUsers.do")
    public String addUsers(){
        loginService.inserListUsers();
        return "main";
    }

    @ResponseBody
    @RequestMapping("/upFile.do")
    public String getFileFromForm(MultipartFile file) throws Exception {

        String fileName = "/data/downloads/"+ DateUtil.getCurDateForHour() + ".xlsx";
        File file1 = new File(fileName);
        file1.createNewFile();
        file.transferTo(file1);
        HashMap<Integer, String> data = new HashMap<>();
        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(file1));
        //一个文件的第n个单元
        XSSFSheet sheet = sheets.getSheetAt(0);
        //单元行
        for(int i=0;i<sheet.getLastRowNum();i++){
            StringBuilder str = new StringBuilder();
            XSSFRow row = sheet.getRow(i);
            //单元列
            for(int j=0;j<6;j++){
                if(row!=null){
                    XSSFCell cell = row.getCell(j);
                    String cellVale;
                    if(cell!=null && StringUtils.isNotBlank(cell.toString())){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cellVale = cell.getStringCellValue().trim();
                        if(j==5){
                            str.append(cellVale);
                        }else{
                            str.append(cellVale).append("@@");
                        }
                    }
                }
            }
            data.put(i,str.toString());
        }
        return data.toString();
    }

    /**
      * @description:下载zip文件
    **/
    @RequestMapping("/zipFile.do")
    @ResponseBody
    public void getZipFile(HttpServletResponse response) throws IOException {
        File test = ZipFileUtil.init();
        FileInputStream fis;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(test);
            bis = new BufferedInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=merchant.zip");
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[1024*1024];
        int length;
        while ((length=bis.read(bytes))>0){
            os.write(bytes,0,length);
        }
        os.flush();
        os.close();
        ZipFileUtil.deleteDir(new File("/data/merchant/cache"));
    }

}
