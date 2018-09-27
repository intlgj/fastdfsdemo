package com.intlgj.fastdfsdemo;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.GroupState;
import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: huangguoji
 * @Date: 2018/9/27 09:34
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(FdfsClientConfig.class)
public class FastdfsClientApplicationTests {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private TrackerClient trackerClient;

    @Test
    public void contextLoads() {
        List<GroupState> groupStates = trackerClient.listGroups();
        for (GroupState groupState : groupStates) {
            System.out.println(groupState);
        }

    }

    /**
     * 测试文件上传
     */
    @Test
    public void upload() {

        try {
            File file = new File("D:\\testsources\\fastdfs.jpg");

            FileInputStream inputStream = new FileInputStream(file);
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), "jpg", null);

            fastFileStorageClient.uploadSlaveFile(storePath.getGroup(),storePath.getPath(),inputStream,inputStream.available(),"a_",null);
            //fastFileStorageClient.uploadSlaveFile("group1","M00/00/00/wKiAjVlpNjiAK5IHAADGA0F72jo578.jpg",inputStream,inputStream.available(),"a_",null);

            System.out.println(storePath.getGroup() + " " + storePath.getPath());

            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试上传缩略图
     */
    @Test
    public void uploadCrtThumbImage() {
        try {
            File file = new File("D:\\testsources\\fastdfs2.jpg");

            FileInputStream inputStream = new FileInputStream(file);
            Set<MateData> metadata = new HashSet<>();
            metadata.add(new MateData("width","150"));
            metadata.add(new MateData("height","150"));
            // 测试上传 缩略图
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, file.length(), "jpg", metadata);

            System.out.println(storePath.getGroup() + "  " + storePath.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试文件下载
     */
    @Test
    public void download() {
        try {
            byte[] bytes = fastFileStorageClient.downloadFile("group1", "M00/45/2A/CgMiIlusmU2AK8C4AAFvEx4et2c387.jpg", new DownloadByteArray());

            FileOutputStream stream = new FileOutputStream("D:\\testsources\\c.jpg");

            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试文件删除
     */
    @Test
    public void deleteFile(){
        fastFileStorageClient.deleteFile("group1","M00/00/00/wKiAjVlpQVyARpQwAADGA0F72jo566.jpg");
    }

}
