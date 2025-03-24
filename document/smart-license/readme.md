## 系统添加证书的步骤
### 1、生成证书
### 2、修改系统配置文件
这里需要注意如果是docker部署，license和公钥路径需要做映射
示例：
```yml
smart:
  license:
    client:
      key:
        subject: easylink_jiamei_pro
        publicAlias: jiameiPublicCert
        storePass: public_password1234
        licensePath: /conf/license/easylink_jiamei_PRO_20230101_20240101.lic
        publicKeysStorePath: /conf/license/jiameiPublicCerts.keystore
      project:
        project: easylink-嘉美
```
### 3、修改docker启动命令
如果是docker运行需要修改启动命名，docker可以获取到宿主机硬件信息

--privileged -v /dev/mem:/dev/mem  -v /usr/sbin/dmidecode:/usr/sbin/dmidecode
示例：
```bash
docker run -d -e "JAVA_OPTS=-Xmx2048m -Dfile.encoding=utf8 -Duser.timezone=GMT+08" --restart=always --privileged -v /dev/mem:/dev/mem  -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /mnt/easylink/conf:/conf -v /mnt/easylink/log:/log -p 9096:8080 --name easylink-main easylink-main
```