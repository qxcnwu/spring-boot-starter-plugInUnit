# spring-boot-starter-plugInUnit
#   1. 什么是spring-boot-starter-plugInUnit

​	spring-boot-starter-plugInUnit是一款面向独立插件的管理容器，综合维护各个小型插件之间的增、删、改、查等迭代操作，能够对各个插件的各个组成部分进行细致管理。

# 2. spring-boot-starter-plugInUnit能做什么

1. 插件版本管理
2. 插件服务管理
3. 插件文档管理
4. 插件静态资源管理

# 3. spring-boot-starter-plugInUnit管理的最小单元

**对于插件我们在给予足够的用户开放的前提下，对插件的文件系统结构进行规范，对于任意的插件其需要满足以下结构**

+ 插件根目录 root
  + 文档所在目录 root/doc
    + 文档文件 root/doc/readme.md
  + 服务所在目录 root/server
    + 服务1所在目录 root/server/server1
      + 服务配置文件 root/server/server1/config.json
    + 服务2所在目录 root/server/server2
      + 服务配置文件 root/server/server2/config.json
  + 核心插件功能实现所在目录 root/src
  + 插件界面静态文件所在目录 root/static

> 只有满足基础的spring-boot-starter-plugInUnit插件结构，插件才能够被正常管理，反之，将在控制台打印见检查内容

# 4. spring-boot-starter-plugInUnit插件更新流程

1. 管理员通过插件管理中心上传新版本插件
2. spring-boot-starter-plugInUnit注册中心感知插件传输完成
3. spring-boot-starter-plugInUnit注册中心解压插件压缩包
4. spring-boot-starter-plugInUnit插件规范检测模块异步检测
5. spring-boot-starter-plugInUnit数据持久化模块写入
6. spring-boot-starter-plugInUnit文档管理模块同步刷新
7. spring-boot-starter-plugInUnit服务管理中心分配服务管理模块
8. spring-boot-starter-plugInUnit服务管理模块启动服务

# 5. spring-boot-starter-plugInUnit项目结构

​	spring-boot-starter-plugInUnit主要由启动配置中心、线程管理中心、插件注册中心、文档管理中心、服务管理中心组成，其中线程管理中心是最核心的模块，负责其他模块线程池、线程的获取以及管理。插件注册中心的核心功能包括插件释放，插件检测，流程配置，数据持久化。文档管理模块的功能包括spinx文档定时更新、文档的手动刷新。服务管理中心负责服务的发现，更新。

## 5.1 spring-boot-starter-plugInUnit插件状态检测

​	对于所有处在spring-boot-starter-plugInUnit中的插件，我们需要定时轮询其中的src与static文件的状态判断他们呢是否进行修改，如果有插件发生修改，我们将更新唯一性校验，也就是对src与static文件夹中的内容重新计算校验值。为减少客户端与服务端传输数据量过大，我们需要**5.1.1**中的编码方式对每个插件的各个版本进行编码。

### 5.1.1 文件夹编码方式

| 名称                | 字节长度 | 注释                                                         |
| ------------------- | -------- | ------------------------------------------------------------ |
| 是否是文件夹        | 1 byte   | 1为文件夹，0为文件                                           |
| 文件夹父目录层级    | 1 byte   | 最多支持127层的文件夹嵌套因此采用1个byte                     |
| 当前文件夹层级      | 1 byte   | 最多支持127层的文件夹嵌套因此采用1个byte                     |
| 文件夹名称/文件名称 | 16 byte  | 文件夹名称通过MD16进行校验，存在碰撞的可能性但概率在2^128次方 |
| 文件夹内部文件/文件 | 32 byte  | 通过Sha256对内部文件内容进行加密然后传输                     |

文件夹编码的具体实现如下，采用递归的方式对插件的src以及static文件夹进行递归编码

```java
public static @NotNull ArrayList<byte[]> encodeDirectory(String path, int n, boolean isFirst) {
    if (isFirst) {
        NOW = 0;
    } else {
        NOW++;
    }
    ArrayList<byte[]> ans = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    byte[] tmp = new byte[ALL];
    tmp[0] = 1;
    // 文件夹层数
    tmp[ISFILE] = (byte) n;
    tmp[ISFILE + CENT] = (byte) NOW;
    byte[] tp = new byte[CENT + CENTSON];
    System.arraycopy(tmp, ISFILE, tp, 0, CENT + CENTSON);
    File file = new File(path);
    for (File f : Objects.requireNonNull(file.listFiles())) {
        if (f.isDirectory()) {
            ans.addAll(encodeDirectory(f.getPath(), NOW, false));
        } else {
            final ByteWithSha fileByte = getFileByte(f.getPath(), tp);
            ans.add(fileByte.ans);
            sb.append(fileByte.sha256);
        }
    }
    // 文件夹名称
    final byte[] md5 = FileEncoderUtiles.getMd5(file.getName());
    System.arraycopy(md5, 0, tmp, ISFILE + CENT + CENTSON, NAME);
    // 文件夹内容的sha64
    final byte[] sha256 = FileEncoderUtiles.getSHA256(sb.toString());
    System.arraycopy(sha256, 0, tmp, ISFILE + CENT + NAME + CENTSON, CEN);
    ans.add(tmp);
    return ans;
}
```



### 5.1.2 文件夹内容压缩传输方式

