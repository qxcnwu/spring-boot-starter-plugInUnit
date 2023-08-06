package com.qxc.databasecentral.controller;

import com.qxc.databasecentral.pojo.Result;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactory;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import org.jetbrains.annotations.Contract;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 20:43
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.controller
 */
@RestController
@RequestMapping("/threadpool")
public class ThreadPoolController {
    private final static ThreadPoolFactory TH = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance();

    @Contract(pure = true)
    public ThreadPoolController() {
    }

    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getalljavapoolsize")
    private Result getAllThreadPoolSize() {
        return new Result().setData(TH.getAllJavaPoolSize()).setFinish(true).setMessage("get all java pool!!");
    }

    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getalltmpoolsize")
    private Result getAllTmPoolSize() {
        return new Result().setData(TH.getAllBasicPoolSize()).setFinish(true).setMessage("get all tm pool!!");
    }

    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getallsinglesize")
    private Result getAllSingleThreadSize() {
        return new Result().setData(TH.getAllSingleSize()).setFinish(true).setMessage("get all single pool!!");
    }


    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getalljavapoolname")
    private Result getAllThreadPoolName() {
        return new Result().setData(TH.getAllJavaPoolName()).setFinish(true).setMessage("get all java pool!!");
    }

    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getalltmpoolname")
    private Result getAllTmPoolName() {
        return new Result().setData(TH.getAllBasicPoolName()).setFinish(true).setMessage("get all tm pool!!");
    }

    /**
     * 获取全部的线程池
     *
     * @return
     */
    @RequestMapping("/getallsinglename")
    private Result getAllSingleThreadName() {
        return new Result().setData(TH.getAllSingleName()).setFinish(true).setMessage("get all single pool!!");
    }


    /**
     * 获取存活的线程池
     *
     * @return
     */
    @RequestMapping("/live/getalljavapool")
    private Result getAllLiveThreadPool() {
        return new Result().setData(TH.getAliveJavaPoolString()).setFinish(true).setMessage("get all live java pool!!");
    }

    /**
     * 获取存活的线程池
     *
     * @return
     */
    @RequestMapping("/live/getalltmpool")
    private Result getAllLiveTmPool() {
        return new Result().setData(TH.getAliveBasicPoolString()).setFinish(true).setMessage("get all live tm pool!!");
    }

    /**
     * 获取存活的线程池
     *
     * @return
     */
    @RequestMapping("/live/getallsingle")
    private Result getAllLiveSingleThread() {
        return new Result().setData(TH.getAliveSingleString()).setFinish(true).setMessage("get all live single pool!!");
    }

    /**
     * 获取存活的线程池数量
     *
     * @return
     */
    @RequestMapping("/livesize/getalljavapool")
    private Result getAllLiveSizeThreadPool() {
        return new Result().setData(TH.getAliveJavaPoolSize()).setFinish(true).setMessage("get all live java pool!!");
    }

    /**
     * 获取存活的线程池数量
     *
     * @return
     */
    @RequestMapping("/livesize/getalltmpool")
    private Result getAllLiveSizeTmPool() {
        return new Result().setData(TH.getAliveBasicPoolSize()).setFinish(true).setMessage("get all live tm pool!!");
    }

    /**
     * 获取存活的线程池数量
     *
     * @return
     */
    @RequestMapping("/livesize/getallsingle")
    private Result getAllLiveSizeSingleThread() {
        return new Result().setData(TH.getAllSingleSize()).setFinish(true).setMessage("get all live single pool!!");
    }

    /**
     * 查询是否存活
     *
     * @return
     */
    @RequestMapping("/isalive/{name}")
    private Result isAlive(@PathVariable String name) {
        return new Result().setData(TH.isAlive(name)).setFinish(true).setMessage("update state");
    }

    /**
     * 关闭线程池
     *
     * @return
     */
    @RequestMapping("/shutdown/{name}")
    private Result shutdownByName(@PathVariable String name) {
        return new Result().setData(TH.shutdoneByName(name)).setFinish(true).setMessage("shutdown thread!!");
    }

    /**
     * 关闭线程池
     *
     * @return
     */
    @RequestMapping("/shutdownall")
    private Result shutdownAll() {
        return new Result().setData(TH.shutdoneAll()).setFinish(true).setMessage("all shutdown");
    }

    /**
     * 关闭java池
     *
     * @return
     */
    @RequestMapping("/shutdown/javapool")
    private Result shutdownThreadPool() {
        return new Result().setData(TH.shutdoneAllJava()).setFinish(true).setMessage("shutdown java pool!!");
    }

    /**
     * 关闭tm池
     *
     * @return
     */
    @RequestMapping("/shutdown/tmpool")
    private Result shutdownTmPool() {
        return new Result().setData(TH.shutdoneAllTm()).setFinish(true).setMessage("shutdown tm pool!!");
    }

    /**
     * 关闭线程
     *
     * @return
     */
    @RequestMapping("/shutdown/single")
    private Result shutdownSingleThread() {
        return new Result().setData(TH.shutdoneAllSingle()).setFinish(true).setMessage("shutdown single pool!!");
    }
}
