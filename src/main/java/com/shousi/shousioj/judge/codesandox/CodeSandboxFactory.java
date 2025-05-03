package com.shousi.shousioj.judge.codesandox;

import com.shousi.shousioj.judge.codesandox.impl.ExampleCodeSandbox;
import com.shousi.shousioj.judge.codesandox.impl.RemoteCodeSandbox;
import com.shousi.shousioj.judge.codesandox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 根据参数创建指定代码沙箱
     * @param type 沙箱类型
     * @return 代码沙箱实例
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
