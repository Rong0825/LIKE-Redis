package com.Rong.commands;

import com.Rong.Command;
import com.Rong.Database;
import com.Rong.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
/* Lpush 命令将一个或多个值插入到列表头部。
如果 key 不存在，一个空列表会被创建并执行 LPUSH操作。
当 key 存在但不是列表类型时，返回一个错误。*/
public class LPUSHCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(LPUSHCommand.class);
    private List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }

    @Override
    public void run(OutputStream os) throws IOException {
        if (args.size() != 2) {
            Protocol.writeError(os, "命令至少需要两个参数");
            return;
        }
        String key = new String((byte[])args.get(0));
        String value = new String((byte[])args.get(1));
        logger.debug("运行的是 lpush 命令: {} {}", key, value);

        // 这种方式不是一个很好的线程同步的方式
        List<String> list = Database.getList(key);
        list.add(0, value);//头插

        logger.debug("插入后数据共有 {} 个", list.size());

        Protocol.writeInteger(os, list.size());
    }
}
