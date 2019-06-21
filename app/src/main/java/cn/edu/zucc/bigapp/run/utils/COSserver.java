package cn.edu.zucc.bigapp.run.utils;

import android.content.Context;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SessionCredentialProvider;
import com.tencent.qcloud.core.http.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class COSserver {
  private CosXmlService cosXmlService;
  private TransferManager transferManager;
  private Context context;
  private String appid = "1256928893";
  private String region = "ap-shanghai";
  private URL url = null;
  private String Bucket = "dbstore-1256928893";
  QCloudCredentialProvider credentialProvider = null;
  //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
  CosXmlServiceConfig serviceConfig = null;

  public COSserver(Context context) {
    this.context = context;
    init();
  }

  private void init() {
    try {
      url = new URL("http://118.25.195.72:3000/sts");
      credentialProvider = new SessionCredentialProvider(new HttpRequest.Builder<String>()
          .url(url)
          /**
           * 注意这里的 HTTP method 为 GET，请根据您自己密钥服务的发布方式进行修改
           */
          .method("GET")
          .build());


    } catch (MalformedURLException e) {
      e.printStackTrace();
    }


    serviceConfig = new CosXmlServiceConfig.Builder()
        .setAppidAndRegion(appid, region)
        .setDebuggable(true)
        .builder();
    cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);


  }

  public void upload(String srcPath, String cosPath) {
    TransferConfig transferConfig = new TransferConfig.Builder().build();
    transferManager = new TransferManager(cosXmlService, transferConfig);
    transferManager.upload(Bucket, cosPath, srcPath, null);
  }

  public void download(String cosPath) {
    TransferConfig transferConfig = new TransferConfig.Builder().build();
    transferManager = new TransferManager(cosXmlService, transferConfig);
    transferManager.download(context, Bucket, cosPath, "/data/data/cn.edu.zucc.bigapp/databases/", cosPath);

  }
}
