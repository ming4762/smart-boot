## docker 镜像构建命名
> 参数MODULE：指定要构建的模块，例如：smart-service-system
> 
> 参数PROFILE：指定要构建的环境，例如：uat、prod等
### 1、单体应用
```bash
docker build -f deployment/docker/Dockerfile-standalone \
  --build-arg MODULE=smart-service-system \
  --build-arg PROFILE=uat \
  -t smart-service-system:uat \
  .
```

### 2、微服务应用

```bash
docker build \
  -f deployment/docker/Dockerfile-cloud \
  --build-arg MODULE=smart-cloud-service-system \
  --build-arg PROFILE=uat \
  -t module-a:uat \
  ../
```