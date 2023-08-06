package com.qxc.threadexector.registrationcentercore;

import com.qxc.databasecentral.pojo.PlugInUnitPojo;
import com.qxc.databasecentral.server.PlugInUnitService;
import com.qxc.pluginunit.construct.PlugInUnitMaker;
import com.qxc.threadexector.configuration.DirectoryTargetMonitorConfiguration;
import com.qxc.threadexector.documentthreadexector.DocumentPipeLine;
import com.qxc.threadexector.documentthreadexector.DocumentThreadExectorFactory;
import com.qxc.threadexector.eventbus.EventBus;
import com.qxc.threadexector.eventbus.Subscribe;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import com.qxc.threadexector.registerthreadexector.RegisterThreadExector;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.containertools.HashMapWithNode;
import com.qxc.utiles.containertools.HashMapWithNodeImpl;
import com.qxc.utiles.containertools.ThreadSafeSetExector;
import com.qxc.utiles.CurrentSystemInfo;
import com.qxc.utiles.filesystemtools.PathParseUtiles;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/16 9:51
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.registrationcentercore
 */
@Component
@Slf4j
@Data
public class RegisterCenterFactory {
    private final ThreadSafeSetExector<String, DocumentPipeLine> threadSafeSetExector;
    private final HashMapWithNode hashMapWithNode = new HashMapWithNodeImpl();
    private final PlugInUnitService service;
    private final DirectoryTargetMonitorConfiguration configuration;
    private final RegisterThreadExector registerThreadExector;
    private final PlugInUnitMaker plugInUnitMaker;
    private final ExecutorService pool = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("RegisterCenterFactoryThreadPool",CurrentSystemInfo.getInstance().getCPU_Processors()*2);

    private final PlugInUnitService plugInUnitService;
    private final EventBus eventBus;

    @SuppressWarnings("MissingJavadoc")
    @Autowired
    public RegisterCenterFactory(PlugInUnitService service, DirectoryTargetMonitorConfiguration configuration, RegisterThreadExector registerThreadExector, PlugInUnitMaker plugInUnitMaker, PlugInUnitService plugInUnitService, EventBus eventBus, @NotNull DocumentThreadExectorFactory documentThreadExectorFactory) {
        this.service = service;
        this.configuration = configuration;
        this.registerThreadExector = registerThreadExector;
        this.plugInUnitMaker = plugInUnitMaker;
        this.plugInUnitService = plugInUnitService;
        this.eventBus = eventBus;
        threadSafeSetExector = documentThreadExectorFactory.getThreadSafeSetExector();
    }

    /**
     * 初始化添加所有的有效目录
     * @param rootPath
     * @param plugInUnitName
     * @param versionName
     */
    @Contract(pure = true)
    public void initInsert(String rootPath, String plugInUnitName, String versionName) {
        NewPlugInUnitWithContext context = new NewPlugInUnitWithContext(rootPath, plugInUnitName, versionName, plugInUnitMaker);
        pool.submit(makeRun(context));
    }

    /**
     * 私有的插入方法
     *
     */
    @Subscribe(topic = {"pluginunit unzip"})
    public void initInsert(String rootPath) {
        NewPlugInUnitWithContext context = new NewPlugInUnitWithContext(rootPath, PathParseUtiles.getPlugInUnitName(rootPath), PathParseUtiles.getVersionName(rootPath), plugInUnitMaker);
        pool.submit(makeRun(context, false));
    }

    /**
     * 指定当前插件的父目录
     *
     * @param isLatest
     */
    @Contract(pure = true)
    public void initInsert(String plugInUnitPath, boolean isLatest) {
        NewPlugInUnitWithContext context = new NewPlugInUnitWithContext(plugInUnitPath, PathParseUtiles.getPlugInUnitName(plugInUnitPath), PathParseUtiles.getVersionName(plugInUnitPath), plugInUnitMaker);
        pool.submit(makeRun(context, isLatest));
    }

    /**
     * 重新进行刷新
     */
    @Contract(pure = true)
    private void refreshAll() {
        // 查询当前开启最新地插件版本
        final List<PlugInUnitPojo> latestVersion = plugInUnitService.getLatestVersion();
        HashSet<String> set = new HashSet<>();
        for (PlugInUnitPojo p : latestVersion) {
            set.add(p.getRootPath());
        }
        // 首先需要遍历当前根目录下所有的子文件夹
        File files = new File(configuration.getBaseDir());
        for (File file : Objects.requireNonNull(files.listFiles())) {
            for (File fileSon : Objects.requireNonNull(file.listFiles())) {
                // 初始化
                log.debug(fileSon.toString() + " init");
                initInsert(fileSon.toString(), set.contains(fileSon.toString()));
            }
        }
    }


    /**
     * 重新进行初始化
     */
    public void restart() {
        clear();
        refreshAll();
    }


    /**
     * 初始化状态下进行启动注册
     */
    public void start() {
        refreshAll();
    }

    /**
     * 一整套插入流程
     *
     */
    @Contract(pure = true)
    private @NotNull Runnable makeRun(NewPlugInUnitWithContext context, boolean isLatest) {
        return new Runnable() {
            /**
             * When an object implementing interface {@code Runnable} is used
             * to create a thread, starting the thread causes the object's
             * {@code run} method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method {@code run} is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            @Override
            public void run() {
                context.setLatest(isLatest);
                registerThreadExector.submit(context);
                while (context.getFuture() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    if (context.getFuture().get()) {
                        log.info(context.getPlugInUnitName() + ":" + context.getVersionName() + " initializer success!!");
                        // 插入当前的内存中
                        insert(context, isLatest);
                        // 需要插入到表中
                        eventBus.post(context, "scan pluginunit");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Contract(pure = true)
    private @NotNull Runnable makeRun(NewPlugInUnitWithContext context) {
        return makeRun(context, true);
    }

    /**
     * 插入需要设置为私有方法，因为调用前需要异步检查
     *
     */
    private void insert(NewPlugInUnitWithContext context) {
        hashMapWithNode.insert(context);
    }

    /**
     * 插入需要设置为私有方法，因为调用前需要异步检查
     *
     */
    private void insert(NewPlugInUnitWithContext context, boolean isLatest) {
        hashMapWithNode.insert(context, isLatest);
    }

    /**
     * 删除对象
     *
     */
    void delete(String pName, String version) {
        hashMapWithNode.delete(pName, version);
    }

    /**
     * 删除对象
     *
     */
    void delete(NewPlugInUnitWithContext plug) {
        hashMapWithNode.delete(plug);
    }

    /**
     * 查询对应的对象是否存在
     *
     */
    boolean contains(NewPlugInUnitWithContext plug) {
        return hashMapWithNode.contains(plug);
    }

    /**
     * 是否包含对应版本的插件
     *
     */
    boolean contains(String pName, String version) {
        return hashMapWithNode.contains(pName, version);
    }

    /**
     * 是否是最新的插件
     *
     */
    boolean isLatest(NewPlugInUnitWithContext plug) {
        return hashMapWithNode.isLatest(plug);
    }

    /**
     * 是否是最新的插件
     *
     */
    boolean isLatest(String pName, String version) {
        return hashMapWithNode.isLatest(pName, version);
    }

    /**
     * 设置当前的执行对象
     *
     */
    boolean setLatest(String pName, String version) {
        final boolean setB = hashMapWithNode.setLatest(pName, version);
        if (setB) {
            eventBus.post(new PlugInUnitPojo(pName, version, ""), "setLatest");
        }
        return setB;
    }

    /**
     * 通过插件名称以及版本获取对象
     *
     */
    public HashMapWithNodeImpl.PlugInUnitContextWithNode getByName(String name, String version) {
        return hashMapWithNode.getByName(name, version);
    }

    /**
     * 获取当前执行插件
     *
     */
    public NewPlugInUnitWithContext getLatest(String name) {
        return hashMapWithNode.getLatest(name);
    }

    /**
     * 获取全部的插件
     *
     * @param latest 是否需要筛选当前最新版本
     * @return
     */
    public List<HashMapWithNodeImpl.PlugInUnitContextWithNode> getAll(boolean latest) {
        return hashMapWithNode.getAll(latest);
    }

    /**
     * 获取插件的全部版本
     *
     */
    public List<HashMapWithNodeImpl.PlugInUnitContextWithNode> getAll(String pName) {
        return hashMapWithNode.getAll(pName);
    }

    /**
     * 立即刷新文档
     */
    public void refreshDoc(){
        threadSafeSetExector.refresh();
    }

    /**
     * 清除容器内部所有的对象
     */
    void clear() {
        hashMapWithNode.clear();
    }
}
