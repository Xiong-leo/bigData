# 大数据wordCount流程（基于docker hadoop实现）

It's the record of big data practice process
JAVA 8 + hadoop 2.7.4
## 一、 代码实现
### 1、 Mapper实现
文字分离
mapper实现，key value返回

eg: *context.write(keyOut, valueOut);*

context 为org.apache.hadoop.mapreduce.Mapper.Context对象

### 2、 Reduce实现
统计计数，合并key，key value返回

eg: *context.write(key, valueOut);*

context 为org.apache.hadoop.mapreduce.Reducer.Context对象

### 3、 设置Job规则
初始化Job
设置Job启动类，Mapper，Reducer
设置输入输出文件路径
提交等待任务完成

## 二、 docker执行任务
### 1、 拉取镜像
注：配置好拉取镜像仓库，国内镜像源（阿里云，华为云）目前不支持拉取镜像，确保目标镜像中有jdk。
可以在github上找对应的解决方案，或者购买国外云服务器
docker pull bde2020/hadoop-base:2.0.0-hadoop2.7.4-java8

### 2、 启动镜像
命令如下：
-p：映射端口
-v：挂载地址映射
docker run -itd --name hadoop-2.7.4 -p 9870:9870 -p 8088:8088 -v /home/share/wordCount/jarDir/:/container/share 018b406bd78b

### 3、 启动HDFS
执行find / -name start-dfs.sh查找shell脚本地址，
执行./start-dfs.sh启动HDFS
执行过程中遇到如下问题
```
root@018b406bd78b:/# bash /opt/hadoop-2.7.4/sbin/start-dfs.sh
Starting namenodes on [018b406bd78b]
018b406bd78b: /opt/hadoop-2.7.4/sbin/slaves.sh: line 60: ssh: command not found
localhost: /opt/hadoop-2.7.4/sbin/slaves.sh: line 60: ssh: command not found
Starting secondary namenodes [0.0.0.0]
0.0.0.0: /opt/hadoop-2.7.4/sbin/slaves.sh: line 60: ssh: command not found
```
======> hadoop-2.7.4包中不存在ssh工具包
解决方案：
```
# 使用仍然可用的镜像源直接下载
wget https://vault.centos.org/7.6.1810/os/x86_64/Packages/openssh-7.4p1-16.el7.x86_64.rpm
wget https://vault.centos.org/7.6.1810/os/x86_64/Packages/openssh-clients-7.4p1-16.el7.x86_64.rpm
wget https://vault.centos.org/7.6.1810/os/x86_64/Packages/openssh-server-7.4p1-16.el7.x86_64.rpm
# 手动安装
rpm -ivh openssh-*.rpm --nodeps --force
```

### 执行wordCount大数据程序
上传输入文件至HDFS中，输入文件可以使用python脚本(src/main/resources/generateWords.py)
```
# 创建HDFS输入目录（如果不存在）
hdfs dfs -mkdir -p /input
# 上传当前目录下所有json文件
hdfs dfs -put *.json /input/
# 验证上传结果
hdfs dfs -ls /input
```
执行jar程序，在jar目录下运行如下命令
```
# 提交MapReduce作业（假设主类为WordCount）
hadoop jar wordCount_8-1.0-SNAPSHOT.jar \
WordCount \
/input \
/output
```

查看执行结果
```
hdfs dfs -cat /output/part-r-00000
```