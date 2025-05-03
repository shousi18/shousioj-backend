package com.shousi.shousioj.judge.codesandox.impl;

import com.shousi.shousioj.judge.codesandox.CodeSandbox;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeRequest;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
