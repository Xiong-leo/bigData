FROM ubuntu:22.04

# 设置环境变量
ENV HADOOP_VERSION=3.3.6
ENV HADOOP_HOME=/opt/hadoop
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin"

# 添加 Hadoop 组件操作用户环境变量
ENV HDFS_NAMENODE_USER=root
ENV HDFS_DATANODE_USER=root
ENV HDFS_SECONDARYNAMENODE_USER=root
ENV YARN_RESOURCEMANAGER_USER=root
ENV YARN_NODEMANAGER_USER=root

# 安装必要的软件包
RUN apt-get update && \
    apt-get install -y \
        openssh-server \
        openjdk-8-jdk \
        vim \
        wget && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir -p /var/run/sshd && \
    chmod 755 /var/run/sshd

# 下载并解压 Hadoop
RUN wget https://downloads.apache.org/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz \
&& tar -xzf hadoop-$HADOOP_VERSION.tar.gz -C /opt \
&& mv /opt/hadoop-$HADOOP_VERSION $HADOOP_HOME \
&& rm hadoop-$HADOOP_VERSION.tar.gz

# 在 hadoop-env.sh 中正确设置 JAVA_HOME
RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" > $HADOOP_HOME/etc/hadoop/hadoop-env.sh

# 配置 SSH 免密登录
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' \
&& cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

# 复制 Hadoop 配置文件

COPY config/* $HADOOP_HOME/etc/hadoop/

# 配置SSH
RUN echo 'root:your_password' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config && \
    ssh-keygen -A

# 暴露SSH端口
EXPOSE 22

# 启动 SSH 服务
CMD ["/usr/sbin/sshd", "-D"]
